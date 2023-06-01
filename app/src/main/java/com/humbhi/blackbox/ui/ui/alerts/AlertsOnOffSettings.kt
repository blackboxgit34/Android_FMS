package com.humbhi.blackbox.ui.ui.alerts

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityAlertsOnOffSettingsBinding
import com.humbhi.blackbox.ui.ui.alerts.setAlert.SetAlertActivity
import com.humbhi.blackbox.ui.ui.vehicleStatus.VehicleStatusActivity

class AlertsOnOffSettings : AppCompatActivity() ,View.OnClickListener{

    private lateinit var binding:ActivityAlertsOnOffSettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAlertsOnOffSettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.cvIgnition.setOnClickListener(this)
        binding.cvOverspeed.setOnClickListener(this)
        binding.cvOverStop.setOnClickListener(this)
        binding.cvBattery.setOnClickListener(this)
        setToolbarDetails()
    }
    private fun setToolbarDetails(){
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.tvTitle.visibility = View.VISIBLE
        binding.toolbar.tvTitle.text = "Alerts Setting"
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.cvIgnition->
            {
                val intent = Intent(this, IgnitionAlertSetting::class.java)
                startActivity(intent)
            }
            R.id.cvOverspeed->
            {
                val intent = Intent(this, OverspeedAlert::class.java)
                startActivity(intent)
            }
            R.id.cvOverStop->
            {
                val intent = Intent(this, OverstopAlert::class.java)
                startActivity(intent)
            }
            R.id.cvBattery->
            {
                val intent = Intent(this, BatteryAlert::class.java)
                startActivity(intent)
            }
        }
    }
}