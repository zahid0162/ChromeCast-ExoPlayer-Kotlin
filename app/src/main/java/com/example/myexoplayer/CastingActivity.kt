//package com.example.myexoplayer
//
//import android.content.Context
//import android.os.Bundle
//import android.view.*
//import android.widget.*
//import android.widget.AdapterView.OnItemClickListener
//import androidx.appcompat.app.AlertDialog
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.graphics.ColorUtils
//import androidx.recyclerview.widget.ItemTouchHelper
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.google.android.exoplayer2.C
//import com.google.android.exoplayer2.MediaItem
//import com.google.android.exoplayer2.ui.StyledPlayerView
//import com.google.android.exoplayer2.util.Assertions
//import com.google.android.exoplayer2.util.Util
//import com.google.android.gms.cast.framework.CastButtonFactory
//import com.google.android.gms.cast.framework.CastContext
//import com.google.android.gms.dynamite.DynamiteModule
//
//
///**
// * An activity that plays video using [ExoPlayer] and supports casting using ExoPlayer's Cast
// * extension.
// */
//class CastingActivity : AppCompatActivity(), View.OnClickListener,
//    PlayerManager.Listener {
//    private var playerView: StyledPlayerView? = null
//    private var playerManager: PlayerManager? = null
//    private var mediaQueueList: RecyclerView? = null
//    private var mediaQueueListAdapter: CastingActivity.MediaQueueListAdapter? = null
//    private var castContext: CastContext? = null
//
//    // Activity lifecycle methods.
//    public override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        // Getting the cast context later than onStart can cause device discovery not to take place.
//        try {
//            castContext = CastContext.getSharedInstance(this)
//        } catch (e: RuntimeException) {
//            var cause = e.cause
//            while (cause != null) {
//                if (cause is DynamiteModule.LoadingException) {
//                    setContentView(R.layout.cast_context_error)
//                    return
//                }
//                cause = cause.cause
//            }
//            throw e
//        }
//        setContentView(R.layout.main_activity)
//        playerView = findViewById(R.id.player_view)
//        playerView.requestFocus()
//        mediaQueueList = findViewById(R.id.sample_list)
//        val helper = ItemTouchHelper(CastingActivity.RecyclerViewCallback())
//        helper.attachToRecyclerView(mediaQueueList)
//        mediaQueueList.setLayoutManager(LinearLayoutManager(this))
//        mediaQueueList.setHasFixedSize(true)
//        mediaQueueListAdapter = CastingActivity.MediaQueueListAdapter()
//        findViewById<View>(R.id.add_sample_button).setOnClickListener(this)
//    }
//
//    override fun onCreateOptionsMenu(menu: Menu): Boolean {
//        super.onCreateOptionsMenu(menu)
//        menuInflater.inflate(R.menu.menu, menu)
//        CastButtonFactory.setUpMediaRouteButton(this, menu, R.id.media_route_menu_item)
//        return true
//    }
//
//    public override fun onResume() {
//        super.onResume()
//        if (castContext == null) {
//            // There is no Cast context to work with. Do nothing.
//            return
//        }
//        playerManager =
//            PlayerManager( /* listener= */this, this, playerView,  /* context= */castContext)
//        mediaQueueList!!.adapter = mediaQueueListAdapter
//    }
//
//    public override fun onPause() {
//        super.onPause()
//        if (castContext == null) {
//            // Nothing to release.
//            return
//        }
//        mediaQueueListAdapter.notifyItemRangeRemoved(0, mediaQueueListAdapter.getItemCount())
//        mediaQueueList!!.adapter = null
//        playerManager.release()
//        playerManager = null
//    }
//
//    // Activity input.
//    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
//        // If the event was not handled then see if the player view can handle it.
//        return super.dispatchKeyEvent(event) || playerManager.dispatchKeyEvent(event)
//    }
//
//    override fun onClick(view: View) {
//        AlertDialog.Builder(this)
//            .setTitle(R.string.add_samples)
//            .setView(buildSampleListView())
//            .setPositiveButton(android.R.string.ok, null)
//            .create()
//            .show()
//    }
//
//    // PlayerManager.Listener implementation.
//    fun onQueuePositionChanged(previousIndex: Int, newIndex: Int) {
//        if (previousIndex != C.INDEX_UNSET) {
//            mediaQueueListAdapter.notifyItemChanged(previousIndex)
//        }
//        if (newIndex != C.INDEX_UNSET) {
//            mediaQueueListAdapter.notifyItemChanged(newIndex)
//        }
//    }
//
//    fun onUnsupportedTrack(trackType: Int) {
//        if (trackType == C.TRACK_TYPE_AUDIO) {
//            showToast(R.string.error_unsupported_audio)
//        } else if (trackType == C.TRACK_TYPE_VIDEO) {
//            showToast(R.string.error_unsupported_video)
//        }
//    }
//
//    // Internal methods.
//    private fun showToast(messageId: Int) {
//        Toast.makeText(applicationContext, messageId, Toast.LENGTH_LONG).show()
//    }
//
//    private fun buildSampleListView(): View {
//        val dialogList: View = layoutInflater.inflate(R.layout.sample_list, null)
//        val sampleList = dialogList.findViewById<ListView>(R.id.sample_list)
//        sampleList.adapter = MainActivity.SampleListAdapter(this)
//        sampleList.onItemClickListener =
//            OnItemClickListener { parent: AdapterView<*>?, view: View?, position: Int, id: Long ->
//                playerManager.addItem(DemoUtil.SAMPLES.get(position))
//                mediaQueueListAdapter.notifyItemInserted(playerManager.getMediaQueueSize() - 1)
//            }
//        return dialogList
//    }
//
//    // Internal classes.
//    private inner class MediaQueueListAdapter :
//        RecyclerView.Adapter<MainActivity.QueueItemViewHolder?>() {
//        override fun onCreateViewHolder(
//            parent: ViewGroup,
//            viewType: Int
//        ): MainActivity.QueueItemViewHolder {
//            val v = LayoutInflater.from(parent.context)
//                .inflate(android.R.layout.simple_list_item_1, parent, false) as TextView
//            return MainActivity.QueueItemViewHolder(v)
//        }
//
//        override fun onBindViewHolder(holder: MainActivity.QueueItemViewHolder, position: Int) {
//            holder.item = Assertions.checkNotNull(playerManager.getItem(position))
//            val view: TextView = holder.textView
//            view.setText(holder.item.mediaMetadata.title)
//            // TODO: Solve coloring using the theme's ColorStateList.
//            view.setTextColor(
//                ColorUtils.setAlphaComponent(
//                    view.currentTextColor,
//                    if (position == playerManager.getCurrentItemIndex()) 255 else 100
//                )
//            )
//        }
//
//        override fun getItemCount(): Int {
//            return playerManager.getMediaQueueSize()
//        }
//    }
//
//    private inner class RecyclerViewCallback : ItemTouchHelper.SimpleCallback(
//        ItemTouchHelper.UP or ItemTouchHelper.DOWN,
//        ItemTouchHelper.START or ItemTouchHelper.END
//    ) {
//        private var draggingFromPosition: Int
//        private var draggingToPosition: Int
//
//        init {
//            draggingFromPosition = C.INDEX_UNSET
//            draggingToPosition = C.INDEX_UNSET
//        }
//
//        override fun onMove(
//            list: RecyclerView, origin: RecyclerView.ViewHolder, target: RecyclerView.ViewHolder
//        ): Boolean {
//            val fromPosition = origin.bindingAdapterPosition
//            val toPosition = target.bindingAdapterPosition
//            if (draggingFromPosition == C.INDEX_UNSET) {
//                // A drag has started, but changes to the media queue will be reflected in clearView().
//                draggingFromPosition = fromPosition
//            }
//            draggingToPosition = toPosition
//            mediaQueueListAdapter.notifyItemMoved(fromPosition, toPosition)
//            return true
//        }
//
//        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//            val position = viewHolder.bindingAdapterPosition
//            val queueItemHolder: MainActivity.QueueItemViewHolder =
//                viewHolder as MainActivity.QueueItemViewHolder
//            if (playerManager.removeItem(queueItemHolder.item)) {
//                mediaQueueListAdapter.notifyItemRemoved(position)
//                // Update whichever item took its place, in case it became the new selected item.
//                mediaQueueListAdapter.notifyItemChanged(position)
//            }
//        }
//
//        override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
//            super.clearView(recyclerView, viewHolder)
//            if (draggingFromPosition != C.INDEX_UNSET) {
//                val queueItemHolder: MainActivity.QueueItemViewHolder =
//                    viewHolder as MainActivity.QueueItemViewHolder
//                // A drag has ended. We reflect the media queue change in the player.
//                if (!playerManager.moveItem(queueItemHolder.item, draggingToPosition)) {
//                    // The move failed. The entire sequence of onMove calls since the drag started needs to be
//                    // invalidated.
//                    mediaQueueListAdapter.notifyDataSetChanged()
//                }
//            }
//            draggingFromPosition = C.INDEX_UNSET
//            draggingToPosition = C.INDEX_UNSET
//        }
//    }
//
//    private inner class QueueItemViewHolder(val textView: TextView) : RecyclerView.ViewHolder(
//        textView
//    ),
//        View.OnClickListener {
//        var item: MediaItem? = null
//
//        init {
//            textView.setOnClickListener(this)
//        }
//
//        override fun onClick(v: View) {
//            playerManager.selectQueueItem(bindingAdapterPosition)
//        }
//    }
//
//    private class SampleListAdapter(context: Context?) :
//        ArrayAdapter<MediaItem?>(context, android.R.layout.simple_list_item_1, DemoUtil.SAMPLES) {
//        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//            val view = super.getView(position, convertView, parent)
//            (view as TextView).text = Util.castNonNull(getItem(position)).mediaMetadata.title
//            return view
//        }
//    }
//}