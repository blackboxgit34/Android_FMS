package com.humbhi.blackbox.ui.ui.reports.detailSummeryReport

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.humbhi.blackbox.databinding.ActivityDetailSummeryReportBinding
import com.humbhi.blackbox.ui.adapters.DetailSummeryReportAdapter

class DetailSummeryReportActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailSummeryReportBinding
    private lateinit var adapter: DetailSummeryReportAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailSummeryReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.tvTitle.text = "Summery Report"
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivSort.visibility = View.VISIBLE
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvRecycler.layoutManager = layoutManager
        adapter = DetailSummeryReportAdapter(this)
        binding.rvRecycler.adapter = adapter
    }
}