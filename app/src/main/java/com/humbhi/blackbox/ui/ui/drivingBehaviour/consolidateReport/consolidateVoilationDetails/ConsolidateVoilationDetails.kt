package com.humbhi.blackbox.ui.ui.drivingBehaviour.consolidateReport.consolidateVoilationDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.humbhi.blackbox.databinding.ActivityConsolidateVoilationDetailsBinding
import com.humbhi.blackbox.databinding.ActivityOverspeedDetailBinding
import com.humbhi.blackbox.ui.adapters.ConsolidateVoilationAdapter
import com.humbhi.blackbox.ui.adapters.ConsolidateVoilationDetailAdapter
import com.humbhi.blackbox.ui.data.models.ConsolidatedViolationSub
import com.humbhi.blackbox.ui.data.models.OverSpeedData

class ConsolidateVoilationDetails : AppCompatActivity() {
    private lateinit var binding: ActivityConsolidateVoilationDetailsBinding
    private lateinit var dataList:ArrayList<ConsolidatedViolationSub>
    private lateinit var vehicleName:String
    private lateinit var adapter: ConsolidateVoilationDetailAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityConsolidateVoilationDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()
    }

    private fun getIntentData(){
        val bundle = intent.extras
        dataList = bundle?.getParcelableArrayList<ConsolidatedViolationSub>("VoilationDetail")!!
        vehicleName = bundle.getString("VehicleName").toString()
        setToolbar()

        val layoutManager = LinearLayoutManager(this)
        binding.rvRecycler.layoutManager = layoutManager
        adapter = ConsolidateVoilationDetailAdapter(this, dataList)
        binding.rvRecycler.adapter = adapter
    }

    private fun setToolbar() {
        binding.toolbar.tvTitle.text = vehicleName
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivSort.visibility = View.GONE
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }
}