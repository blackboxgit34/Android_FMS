package com.humbhi.blackbox.ui.ui.ais140

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityAis140VehicleBinding

class AIS140VehicleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAis140VehicleBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAis140VehicleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.tvTitle.text = "AIS 140 Validity"
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivSort.visibility = View.GONE
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }


}