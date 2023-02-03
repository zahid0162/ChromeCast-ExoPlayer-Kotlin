package com.example.myexoplayer

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.myexoplayer.Utilis.imageList
import com.example.myexoplayer.Utilis.videoList
import com.squareup.picasso.Picasso

class VideoListAdapter(
    var videos: MutableList<String>,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<VideoViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        return VideoViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.video_item_holder,
                parent,
                false
            )
        ){
            onItemClick(videos[it])
        }
    }

    override fun getItemCount(): Int {
        return videos.size
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val currentItem = videos[position]

        holder.itemView.findViewById<TextView>(R.id.title).text = "This is a Sample Video"
        Picasso.get()
            .load(imageList[position])
            .into(holder.itemView.findViewById<ImageView>(R.id.thumb))


    }

    fun setData() {
        this.videos = videoList
        notifyDataSetChanged()
    }

}
