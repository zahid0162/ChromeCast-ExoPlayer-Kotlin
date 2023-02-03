package com.example.myexoplayer

import android.net.Uri
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaItem.DrmConfiguration
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.util.MimeTypes
import java.util.*


/** Utility methods and constants for the Cast demo application.  */ /* package */
internal object DemoUtil {
    const val MIME_TYPE_DASH = MimeTypes.APPLICATION_MPD
    const val MIME_TYPE_HLS = MimeTypes.APPLICATION_M3U8
    const val MIME_TYPE_SS = MimeTypes.APPLICATION_SS
    const val MIME_TYPE_VIDEO_MP4 = MimeTypes.VIDEO_MP4

    /** The list of samples available in the cast demo app.  */
    var SAMPLES: List<MediaItem>? = null

    init {
        val samples = ArrayList<MediaItem>()

        // Clear content.
        samples.add(
            MediaItem.Builder()
                .setUri("https://storage.googleapis.com/wvmedia/clear/h264/tears/tears.mpd")
                .setMediaMetadata(MediaMetadata.Builder().setTitle("Clear DASH: Tears").build())
                .setMimeType(MIME_TYPE_DASH)
                .build()
        )
        samples.add(
            MediaItem.Builder()
                .setUri("https://storage.googleapis.com/shaka-demo-assets/angel-one-hls/hls.m3u8")
                .setMediaMetadata(MediaMetadata.Builder().setTitle("Clear HLS: Angel one").build())
                .setMimeType(MIME_TYPE_HLS)
                .build()
        )
        samples.add(
            MediaItem.Builder()
                .setUri("https://html5demos.com/assets/dizzy.mp4")
                .setMediaMetadata(MediaMetadata.Builder().setTitle("Clear MP4: Dizzy").build())
                .setMimeType(MIME_TYPE_VIDEO_MP4)
                .build()
        )

        // DRM content.
        samples.add(
            MediaItem.Builder()
                .setUri(Uri.parse("https://storage.googleapis.com/wvmedia/cenc/h264/tears/tears.mpd"))
                .setMediaMetadata(
                    MediaMetadata.Builder().setTitle("Widevine DASH cenc: Tears").build()
                )
                .setMimeType(MIME_TYPE_DASH)
                .setDrmConfiguration(
                    DrmConfiguration.Builder(C.WIDEVINE_UUID)
                        .setLicenseUri("https://proxy.uat.widevine.com/proxy?provider=widevine_test")
                        .build()
                )
                .build()
        )
        samples.add(
            MediaItem.Builder()
                .setUri("https://storage.googleapis.com/wvmedia/cbc1/h264/tears/tears_aes_cbc1.mpd")
                .setMediaMetadata(
                    MediaMetadata.Builder().setTitle("Widevine DASH cbc1: Tears").build()
                )
                .setMimeType(MIME_TYPE_DASH)
                .setDrmConfiguration(
                    DrmConfiguration.Builder(C.WIDEVINE_UUID)
                        .setLicenseUri("https://proxy.uat.widevine.com/proxy?provider=widevine_test")
                        .build()
                )
                .build()
        )
        samples.add(
            MediaItem.Builder()
                .setUri("https://storage.googleapis.com/wvmedia/cbcs/h264/tears/tears_aes_cbcs.mpd")
                .setMediaMetadata(
                    MediaMetadata.Builder().setTitle("Widevine DASH cbcs: Tears").build()
                )
                .setMimeType(MIME_TYPE_DASH)
                .setDrmConfiguration(
                    DrmConfiguration.Builder(C.WIDEVINE_UUID)
                        .setLicenseUri("https://proxy.uat.widevine.com/proxy?provider=widevine_test")
                        .build()
                )
                .build()
        )
        SAMPLES = Collections.unmodifiableList(samples)
    }
}