package com.humbhi.blackbox.ui.ui.addonReports.fuel

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.humbhi.blackbox.databinding.ActivityFuelReportBinding
import com.humbhi.blackbox.ui.ui.addonReports.fuel.fuelRodDisconnection.FuelRodDisconnectionActivity

class FuelReportActivity : AppCompatActivity() {

    private lateinit var binding:ActivityFuelReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFuelReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbarDetails()

        binding.cvFuelFilling.setOnClickListener {
            startActivity(Intent(this, FuelFillinReportActivity::class.java))
        }
        binding.cvFuelGraph.setOnClickListener {

            startActivity(Intent(this, FuelGraphJavaActivity::class.java))
        }
        binding.cvFuelTheft.setOnClickListener {
            startActivity(Intent(this, FuelTheftReportActivity::class.java))
        }
        binding.cvFuelRodDisconnections.setOnClickListener {
            startActivity(Intent(this, FuelRodDisconnectionActivity::class.java))
        }
    }

    private fun setToolbarDetails(){
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.tvTitle.visibility = View.VISIBLE
        binding.toolbar.tvTitle.text = "Fuel Report"
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }
}