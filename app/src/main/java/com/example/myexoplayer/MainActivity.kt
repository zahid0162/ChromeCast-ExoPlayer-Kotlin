package com.example.myexoplayer

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.mediarouter.app.MediaRouteButton
import com.example.myexoplayer.databinding.ActivityMainBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.mradzinski.caster.Caster
import com.mradzinski.caster.Caster.OnCastSessionStateChanged
import com.mradzinski.caster.Caster.OnConnectChangeListener
import com.mradzinski.caster.ExpandedControlsStyle
import com.mradzinski.caster.MediaData


class MainActivity : AppCompatActivity() {
    lateinit var caster: Caster
    var uri=""
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        uri= this.intent.getStringExtra("uri").toString()
        println("Uri playing: "+uri)
        val style = ExpandedControlsStyle.Builder()
            .setSeekbarLineColor(resources.getColor(R.color.purple_500))
            .setSeekbarThumbColor(resources.getColor(R.color.white))
            .setStatusTextColor(resources.getColor(R.color.purple_500))
            .build()
        caster=Caster.create(this)
        caster.setExpandedPlayerStyle(style)
        caster.addMiniController()
        val mediaRouteButton: MediaRouteButton = binding.mediaRouteButton
        caster.setupMediaRouteButton(mediaRouteButton, false)

        binding.buttonPlay.setOnClickListener {
            if (caster.isConnected) {
                caster.player.loadMediaAndPlay(createSampleMediaData())
                val playingURL = caster.player.currentPlayingMediaUrl
                println(playingURL)
            }
        }
        caster.setOnConnectChangeListener(object : Caster.OnConnectChangeListener{
            override fun onConnected() {
                Log.d("Caster", "Connected with Chromecast")
            }

            override fun onDisconnected() {
                Log.d("Caster", "Disconnected from Chromecast")
            }

        })
        caster.setOnCastSessionUpdatedListener {

        }
        caster.isConnected
        val uri = this.intent.getStringExtra("uri")
        var player = ExoPlayer.Builder(this).build()
        binding.videoPlayer.player = player
        player.addMediaItem(MediaItem.fromUri(uri.toString()))
        player.prepare()
        player.playWhenReady = true
        binding.loading.visibility = View.GONE
        player.addListener(object : Player.Listener {
            override fun onPlaybackStateChanged(playbackState: Int) {
                super.onPlaybackStateChanged(playbackState)
                when (playbackState) {
                    Player.STATE_BUFFERING -> {
                        binding.loading.visibility = View.VISIBLE
                    }

                    Player.STATE_ENDED -> {
                        player.seekToNext()
                    }
                    Player.STATE_IDLE -> {
                        binding.loading.visibility = View.GONE

                    }
                    Player.STATE_READY -> {
                        binding.loading.visibility = View.GONE
                        player.play()
                    }
                }
            }
        })
        caster.setOnConnectChangeListener(object : OnConnectChangeListener {
            override fun onConnected() {
                binding.buttonPlay.setEnabled(true)
            }

            override fun onDisconnected() {
                binding.buttonPlay.setEnabled(false)
            }
        })

        caster.setOnCastSessionStateChanged(object : OnCastSessionStateChanged {
            override fun onCastSessionBegan() {
                binding.buttonPlay.setEnabled(false)
                Log.e("Caster", "Began playing video")
            }

            override fun onCastSessionFinished() {
                binding.buttonPlay.setEnabled(true)
                Log.e("Caster", "Finished playing video")
            }

            override fun onCastSessionPlaying() {
                val playingURL = caster.player.currentPlayingMediaUrl
                if (playingURL != null && playingURL == uri) {
                    binding.buttonPlay.setEnabled(false)
                } else {
                    binding.buttonPlay.setEnabled(true)
                }
                Log.e("Caster", "Playing video")
            }

            override fun onCastSessionPaused() {
                binding.buttonPlay.setEnabled(false)
                Log.e("Caster", "Paused video")
            }
        })

    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        caster.addMediaRouteMenuItem(menu!!,true)
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }
    private fun createSampleMediaData(): MediaData {
        println(uri)
        return MediaData.Builder(uri)
            .setStreamType(MediaData.STREAM_TYPE_BUFFERED)
            .setContentType("application/x-mpegURL")
            .setMediaType(MediaData.MEDIA_TYPE_MOVIE)
            .setTitle("two birds, many stones.")
            .setDescription("Isaac searches for Rebekah to retrieve Arachnid's stolen XP.")
            .setThumbnailUrl("https://dg8ynglluh5ez.cloudfront.net/151/1517168873360394134/square_thumbnail.jpg")
            .setPlaybackRate(MediaData.PLAYBACK_RATE_NORMAL)
            .setAutoPlay(true)
            .build()
    }
}