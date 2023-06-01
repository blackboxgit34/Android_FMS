package com.humbhi.blackbox.ui.ui.reports.overspeedReport.overSpeedDetail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.google.android.gms.maps.model.LatLng
import com.humbhi.blackbox.databinding.ActivityDistanceReportDetailBinding
import com.humbhi.blackbox.databinding.ActivityOverspeedDetailBinding
import com.humbhi.blackbox.ui.MyApplication
import com.humbhi.blackbox.ui.adapters.OverSpeedDetailAdapter
import com.humbhi.blackbox.ui.data.models.ObjTravelReport
import com.humbhi.blackbox.ui.data.models.OverSpeedData

class OverspeedDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOverspeedDetailBinding
    private lateinit var dataList:ArrayList<OverSpeedData>
    private lateinit var vehicleName:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOverspeedDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()
        setToolbarDetails()
    }

    private fun setToolbarDetails(){
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.tvTitle.visibility = View.VISIBLE
        binding.toolbar.tvTitle.text = "Over Speed Details"
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun getIntentData(){
        dataList = MyApplication.overSpeedDetailList
        val layoutManager = LinearLayoutManager(this)
        binding.rvOverSpeed.layoutManager = layoutManager
        val adapter = OverSpeedDetailAdapter(this,dataList.toList())
        binding.rvOverSpeed.adapter = adapter
    }
}