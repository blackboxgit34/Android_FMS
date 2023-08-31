package com.humbhi.blackbox.ui.ui.fms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.humbhi.blackbox.databinding.ActivityVehicleManagementBinding

class VehicleManagementActivity : AppCompatActivity() {
    lateinit var binding: ActivityVehicleManagementBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVehicleManagementBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar()
    }
    private fun setToolbar()
    {
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.tvTitle.visibility = View.VISIBLE
        binding.toolbar.tvTitle.text = "Vehicle Management"
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }
}