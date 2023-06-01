package com.humbhi.blackbox.ui.ui.addonReports.fuel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.humbhi.blackbox.databinding.ActivityFuelTheftDetailBinding
import com.humbhi.blackbox.databinding.ActivityFuelTheftReportBinding
import com.humbhi.blackbox.ui.MyApplication
import com.humbhi.blackbox.ui.adapters.FuelTheftAdapter
import com.humbhi.blackbox.ui.adapters.FuelTheftDetailAdapter
import com.humbhi.blackbox.ui.data.models.FuelFillingData
import com.humbhi.blackbox.ui.data.models.FueltheftMainModel
import com.humbhi.blackbox.ui.data.models.OverSpeedData
import com.humbhi.blackbox.ui.utils.ExplicitIntentUtil

class FuelTheftDetail : AppCompatActivity() {
    private lateinit var binding: ActivityFuelTheftDetailBinding
    private lateinit var dataList:ArrayList<FueltheftMainModel>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFuelTheftDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbarDetails()

        getIntentData()
    }

    private fun getIntentData(){
            dataList = MyApplication.fuelTheftData
            binding.rvRecycler.layoutManager = LinearLayoutManager(this)
            val adapter = FuelTheftDetailAdapter(dataList)
            binding.rvRecycler.adapter = adapter
    }


    private fun setToolbarDetails(){
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.tvTitle.visibility = View.VISIBLE
        binding.toolbar.tvTitle.text = "Fuel Theft Detail"
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }
}