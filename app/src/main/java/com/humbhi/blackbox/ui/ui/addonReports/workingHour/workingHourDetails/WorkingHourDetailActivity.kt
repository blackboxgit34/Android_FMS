package com.humbhi.blackbox.ui.ui.addonReports.workingHour.workingHourDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.humbhi.blackbox.databinding.ActivityWorkingHourDetailBinding
import com.humbhi.blackbox.ui.MyApplication
import com.humbhi.blackbox.ui.adapters.FuelTheftDetailAdapter
import com.humbhi.blackbox.ui.adapters.WorkingHourDetailAdapter
import com.humbhi.blackbox.ui.data.models.FueltheftMainModel
import com.humbhi.blackbox.ui.data.models.ObjIgnitionStatusReport
import com.humbhi.blackbox.ui.data.models.WorkingHourData

class WorkingHourDetailActivity : AppCompatActivity() {
    private lateinit var dataList:ArrayList<ObjIgnitionStatusReport>
    private lateinit var binding:ActivityWorkingHourDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkingHourDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()
        setToolbarDetails()
    }

    private fun getIntentData(){
            dataList = MyApplication.workHourReportDetail
            binding.rvRecycler.layoutManager = LinearLayoutManager(this)
            val adapter = WorkingHourDetailAdapter(dataList)
            binding.rvRecycler.adapter = adapter
    }

    private fun setToolbarDetails() {
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.tvTitle.visibility = View.VISIBLE
        binding.toolbar.tvTitle.text = "Working Hours Details"
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }
}