package com.example.myexoplayer

import android.view.View
import androidx.recyclerview.widget.RecyclerView

class VideoViewHolder (itemView: View, onItemClicked: (Int) -> Unit): RecyclerView.ViewHolder(itemView) {

    init {
        itemView.setOnClickListener {
            // this will be called only once.
            onItemClicked(adapterPosition)
        }
    }


}
