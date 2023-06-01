package com.humbhi.blackbox.ui.ui.routePlayback

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.humbhi.blackbox.databinding.ActivityRoutePlayBackBinding
import com.humbhi.blackbox.databinding.ActivityRoutePlaybackSelectionBinding
import com.humbhi.blackbox.ui.ui.customerCare.AddServiceRequestActivity

class RoutePlaybackSelection : AppCompatActivity() {

    private lateinit var binding:ActivityRoutePlaybackSelectionBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoutePlaybackSelectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbarDetails()
    }

    private fun setToolbarDetails(){
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.tvTitle.visibility = View.VISIBLE
        binding.toolbar.tvTitle.text = "Route Playback"
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.btnGetRoute.setOnClickListener{
            startActivity(Intent(this, RoutePlayBack::class.java))
        }
    }


}