package com.example.myexoplayer

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myexoplayer.Utilis.videoList
import com.example.myexoplayer.databinding.ActivityHomeBinding

class HomeActivity : AppCompatActivity() {
    lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val adapter=VideoListAdapter(videoList){
              val intent=Intent(this,MainActivity::class.java)
            intent.putExtra("uri",it)
            startActivity(intent)
        }
        binding.listview.adapter=adapter
        binding.listview.layoutManager=LinearLayoutManager(this)
        adapter.setData()
    }
}