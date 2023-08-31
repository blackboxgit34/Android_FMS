package com.humbhi.blackbox.ui.ui.drivingBehaviour.overspeed

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.humbhi.blackbox.databinding.ActivityOverspeedDetailBinding
import com.humbhi.blackbox.ui.MyApplication
import com.humbhi.blackbox.ui.adapters.OverSpeedDetailDBAdapter
import com.humbhi.blackbox.ui.data.models.DrOverSpeedSubLst

class OverspeedDetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOverspeedDetailBinding
    private lateinit var dataList:ArrayList<DrOverSpeedSubLst>

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
        dataList = MyApplication.OverspeedDetailDBList
        val layoutManager = LinearLayoutManager(this)
        binding.rvOverSpeed.layoutManager = layoutManager
        val adapter = OverSpeedDetailDBAdapter(this,dataList)
        binding.rvOverSpeed.adapter = adapter
    }
}