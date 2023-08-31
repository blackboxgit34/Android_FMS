package com.humbhi.blackbox.ui.ui.fms

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.recyclerview.widget.LinearLayoutManager
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityDocumentReminderBinding
import com.humbhi.blackbox.databinding.ActivityDocumnetRemindersDetailsBinding
import com.humbhi.blackbox.ui.MyApplication
import com.humbhi.blackbox.ui.adapters.FMSDocumentReminderDetailAdapter
import com.humbhi.blackbox.ui.data.models.ReminderData

class DocumnetRemindersDetails : AppCompatActivity() {
    lateinit var binding: ActivityDocumnetRemindersDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocumnetRemindersDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.tvTitle.visibility = View.VISIBLE
        binding.toolbar.tvTitle.text = "Reminder Details"
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.rvVehicles.layoutManager = LinearLayoutManager(this@DocumnetRemindersDetails)
        val adapter = FMSDocumentReminderDetailAdapter(this,object :FMSDocumentReminderDetailAdapter.VehicleDetailsUpdate{
            override fun onVehicleSelection(position: Int) {

            }
        },MyApplication.listDocumentRemionder)
        binding.rvVehicles.adapter = adapter
    }
}