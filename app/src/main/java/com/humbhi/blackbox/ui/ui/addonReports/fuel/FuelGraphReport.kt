package com.humbhi.blackbox.ui.ui.addonReports.fuel

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.humbhi.blackbox.databinding.ActivityFuelGraphReportBinding


class FuelGraphReport : AppCompatActivity() {
    private lateinit var binding: ActivityFuelGraphReportBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFuelGraphReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbarDetails()
    }

    private fun setToolbarDetails() {
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.tvTitle.visibility = View.VISIBLE
        binding.toolbar.tvTitle.text = "Fuel Graph Report"
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }


}