package com.humbhi.blackbox.ui.ui.addonReports

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.humbhi.blackbox.databinding.ActivityAddOnReportBinding
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.ui.addonReports.fuel.FuelReportActivity
import com.humbhi.blackbox.ui.ui.addonReports.immobalize.ImmobilizeActivity
import com.humbhi.blackbox.ui.ui.addonReports.temperature.TemperatureSensorReport
import com.humbhi.blackbox.ui.ui.addonReports.workingHour.WorkingHourReportActivity
import com.humbhi.blackbox.ui.utils.CommonUtil

class AddOnReportActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddOnReportBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddOnReportBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.tvTitle.visibility = View.VISIBLE
        binding.toolbar.tvTitle.text = "Add On Report"
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }

        binding.cvFuel.setOnClickListener {
            if(CommonData.getFuelRodStatus()==true) {
                val intent = Intent(this, FuelReportActivity::class.java)
                startActivity(intent)
            }
            else{
                CommonUtil.alertDialogWithOkOnly(this,"Blackbox VTS","No fuel rod attached, contact our sales team to buy.")
            }
        }
        binding.cvTemperature.setOnClickListener {
            if(CommonData.getTempRodStatus()==true) {
                val intent = Intent(this, TemperatureSensorReport::class.java)
                startActivity(intent)
            }
            else{
                CommonUtil.alertDialogWithOkOnly(this,"Blackbox VTS","No temperature sensor attached, contact our sales team to buy.")
            }
        }
        binding.cvWorkingHour.setOnClickListener {
            val intent = Intent(this, WorkingHourReportActivity ::class.java)
            startActivity(intent)
        }
        binding.cvImmobalize.setOnClickListener {
            val intent = Intent(this, ImmobilizeActivity ::class.java)
            startActivity(intent)
        }
    }
}