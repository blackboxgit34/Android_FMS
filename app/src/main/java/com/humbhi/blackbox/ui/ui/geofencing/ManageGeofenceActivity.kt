package com.humbhi.blackbox.ui.ui.geofencing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityManageGeofenceBinding
import com.humbhi.blackbox.ui.adapters.ManageGeofenceAdapter
import com.humbhi.blackbox.ui.adapters.StoppageReportAdapter
import com.humbhi.blackbox.ui.ui.geofencing.addFence.AddGeofenceActivity
import com.humbhi.blackbox.ui.ui.vehicleStatus.VehicleStatusActivity

class ManageGeofenceActivity : AppCompatActivity() {
    private lateinit var binding:ActivityManageGeofenceBinding
    private lateinit var adapter:ManageGeofenceAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageGeofenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.tvTitle.text = "Geofence"
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivAdd.visibility = View.VISIBLE
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.toolbar.ivAdd.setOnClickListener {
            val intent = Intent(this, AddGeofenceActivity::class.java)
            startActivity(intent)
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvRecycler.layoutManager = layoutManager
        adapter = ManageGeofenceAdapter(this)
        binding.rvRecycler.adapter = adapter
    }
}