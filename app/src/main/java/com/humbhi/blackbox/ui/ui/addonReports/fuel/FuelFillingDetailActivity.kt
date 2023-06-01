package com.humbhi.blackbox.ui.ui.addonReports.fuel

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.humbhi.blackbox.databinding.ActivityFuelFillingDetailBinding
import com.humbhi.blackbox.ui.MyApplication
import com.humbhi.blackbox.ui.adapters.FuelFillingAdapter
import com.humbhi.blackbox.ui.adapters.FuelFillingDetailAdapter
import com.humbhi.blackbox.ui.adapters.FuelTheftDetailAdapter
import com.humbhi.blackbox.ui.data.models.FuelData
import com.humbhi.blackbox.ui.data.models.FuelFillingData
import com.humbhi.blackbox.ui.data.models.FuelFillingResponseData

class FuelFillingDetailActivity : AppCompatActivity() {
    private lateinit var dataList:ArrayList<FuelData>
    private lateinit var binding: ActivityFuelFillingDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFuelFillingDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbarDetails()
        getIntentData()
    }

    private fun getIntentData(){
            dataList = MyApplication.fuelData
            binding.rvRecycler.layoutManager = LinearLayoutManager(this)
            val adapter = FuelFillingDetailAdapter(dataList)
            binding.rvRecycler.adapter = adapter
    }

    private fun setToolbarDetails(){
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.tvTitle.visibility = View.VISIBLE
        binding.toolbar.tvTitle.text = "Fuel Filling Details"
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }
}