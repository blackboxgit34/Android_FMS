package com.humbhi.blackbox.ui.ui.customerCare

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.humbhi.blackbox.databinding.ActivityCustomerCareBinding
import com.humbhi.blackbox.ui.ui.customerCare.callus.CallUsActivity
import com.humbhi.blackbox.ui.ui.dashboard.DashboardActivity

class CustomerCare : AppCompatActivity() {
    private lateinit var binding:ActivityCustomerCareBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCustomerCareBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbarDetails()
        clicksOnButtons()
    }

    private fun setToolbarDetails(){
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.tvTitle.visibility = View.VISIBLE
        binding.toolbar.tvTitle.text = "Customer Care"
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun clicksOnButtons(){
        binding.tvCallUs.setOnClickListener {
            startActivity(Intent(this, CallUsActivity::class.java))
        }
        binding.tvServiceRequest.setOnClickListener {
            startActivity(Intent(this, AddServiceRequestActivity::class.java))
        }
    }
}