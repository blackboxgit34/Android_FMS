package com.humbhi.blackbox.ui.ui.reports

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.humbhi.blackbox.databinding.ActivityFleetReportsDashboardBinding
import com.humbhi.blackbox.ui.adapters.MonthlyReportAdapter
import com.humbhi.blackbox.ui.ui.reports.dailyreport.DailyReportActivity
import com.humbhi.blackbox.ui.ui.reports.detailSummeryReport.DetailSummeryReportActivity
import com.humbhi.blackbox.ui.ui.reports.distanceReport.DistanceReportActivity
import com.humbhi.blackbox.ui.ui.reports.monthlyReport.MonthlyReportActivity
import com.humbhi.blackbox.ui.ui.reports.overspeedReport.OverspeedReportActivity
import com.humbhi.blackbox.ui.ui.reports.overstoppageReport.OverstoppageReportActivtiy
import com.humbhi.blackbox.ui.ui.reports.stoppagereport.StoppageReportActivity

class FleetReportsDashboard : AppCompatActivity() {
    private lateinit var binding:ActivityFleetReportsDashboardBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFleetReportsDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.tvTitle.text = "Reports"
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.cvDistanceReport.setOnClickListener {
            val intent = Intent(this, DistanceReportActivity::class.java)
            startActivity(intent)
        }
        binding.cvStoppageReport.setOnClickListener {
            val intent = Intent(this, StoppageReportActivity::class.java)
            startActivity(intent)
        }
        binding.cvDailyReport.setOnClickListener {
            val intent = Intent(this, DailyReportActivity::class.java)
            startActivity(intent)
        }
        binding.cvOverspeed.setOnClickListener {
            val intent = Intent(this, OverspeedReportActivity::class.java)
            startActivity(intent)
        }
        binding.cvOverstoppage.setOnClickListener {
            val intent = Intent(this, OverstoppageReportActivtiy::class.java)
            startActivity(intent)
        }
        binding.cvMonthlyReport.setOnClickListener {
            val intent = Intent(this, MonthlyReportActivity::class.java)
            startActivity(intent)
        }

        binding.cvDetailSummery.setOnClickListener {
            val intent = Intent(this, DetailSummeryReportActivity::class.java)
            startActivity(intent)
        }

    }

}