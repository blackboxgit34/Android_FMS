package com.humbhi.blackbox.ui.ui.reports.stoppagereport.stoppagesdetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityOverspeedDetailBinding
import com.humbhi.blackbox.databinding.ActivityStoppagesDetailBinding
import com.humbhi.blackbox.ui.MyApplication
import com.humbhi.blackbox.ui.adapters.OverSpeedDetailAdapter
import com.humbhi.blackbox.ui.adapters.StoppageReportDetailAdapter
import com.humbhi.blackbox.ui.data.models.ObjStoppageReport
import com.humbhi.blackbox.ui.data.models.OverSpeedData

class StoppagesDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoppagesDetailBinding
    private lateinit var dataList:ArrayList<ObjStoppageReport>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoppagesDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()
        setToolbarDetails()
    }

    private fun setToolbarDetails(){
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.tvTitle.visibility = View.VISIBLE
        binding.toolbar.tvTitle.text = "Stoppages Details"
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun getIntentData(){
        dataList = MyApplication.stoppageList
        val layoutManager = LinearLayoutManager(this)
        binding.rvStoppages.layoutManager = layoutManager
        val adapter = StoppageReportDetailAdapter(this,dataList.toList())
        binding.rvStoppages.adapter = adapter
    }
}