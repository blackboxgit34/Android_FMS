package com.humbhi.blackbox.ui.ui.reports.monthlyReport

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityMonthlyReportBinding
import com.humbhi.blackbox.ui.Utility.EndlessRecyclerOnScrollListener
import com.humbhi.blackbox.ui.adapters.MonthlyReportAdapter
import com.humbhi.blackbox.ui.adapters.OverstoppageAdapter
import com.humbhi.blackbox.ui.data.DataManagerImpl
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.models.MonthData
import com.humbhi.blackbox.ui.data.models.MonthlyDataReponseModel
import com.humbhi.blackbox.ui.data.models.ObjDailyReport
import com.humbhi.blackbox.ui.data.network.RestClient
import com.humbhi.blackbox.ui.ui.reports.stoppagereport.StoppageReportPresenter
import com.humbhi.blackbox.ui.ui.reports.stoppagereport.StoppageReportPresenterImpl
import com.humbhi.blackbox.ui.utils.CommonUtil
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class MonthlyReportActivity : AppCompatActivity(),MonthlyReportView,View.OnClickListener {

    private lateinit var binding:ActivityMonthlyReportBinding
    private lateinit var adapter:MonthlyReportAdapter
    private lateinit var mPresenter: MonthlyReportPresenter
    var startlimit = 0
    var limit = 20
    var list : ArrayList<MonthData> = ArrayList()
    var totalRecords = 0
    var startDateParam = ""
    var endDateParam = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonthlyReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mPresenter = MonthlyReportPresenterImpl(
            this,
            DataManagerImpl(RestClient.getRetrofitBuilderForTrackMaster())
        )
        binding.toolbar.tvTitle.text = "Monthly Report"
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivSort.visibility = View.GONE
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }

        binding.etSearchBar.setOnEditorActionListener(TextView.OnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                startlimit = 0
                binding.loadMore.visibility = View.GONE
                list.clear()
                hitApi()
            }
            false
        })
        dateFilter()
        hitApi()
    }

    private fun dateFilter(){
        startDateParam = CommonUtil.firstDayOfMonth()
        endDateParam = CommonUtil.getCurrentDate()
        binding.tvCurrent.setOnClickListener(this)
        binding.tvPrevious.setOnClickListener(this)
        binding.tvBeforePrevious.setOnClickListener(this)
    }

    private fun hitApi() {
        binding.progressLayout.progressLayout.visibility = View.VISIBLE
        mPresenter.hitMonthlyReportApi(
            "$startDateParam%2012:00%20AM",
            endDateParam + "%2011:59%20PM",
            "",
            CommonData.getCustIdFromDB(),
            "",
            "",
            "1",
            startlimit,
            limit,
            binding.etSearchBar.text.toString().trim().toUpperCase(),
            "1",
            "",
            ""
        )
    }

    override fun getMonthlyReport(monthlyDataReponseModel: MonthlyDataReponseModel) {
        val layoutManager = LinearLayoutManager(this)
        totalRecords = monthlyDataReponseModel.iTotalRecords
        for(i in 0 until  monthlyDataReponseModel.aaData.size) {
            list.add(monthlyDataReponseModel.aaData[i])
        }
        binding.rvRecycler.layoutManager = layoutManager
        adapter = MonthlyReportAdapter(this,list)
        binding.rvRecycler.adapter = adapter
        binding.rvRecycler.scrollToPosition(startlimit)
        if(totalRecords>20){
            binding.loadMore.visibility = View.VISIBLE
        }
        binding.loadMore.setOnClickListener {
            if(list.size<totalRecords) {
                startlimit += 20
                hitApi()
            }
        }
    }

    override fun isNetworkConnected(): Boolean {
        return true
    }

    override fun isShowLoading(): Boolean {
        binding.progressLayout.progressLayout.visibility = View.VISIBLE
        return true
    }

    override fun isHideLoading(): Boolean {
        binding.progressLayout.progressLayout.visibility = View.GONE
        return true
    }

    override fun showErrorMessage(string: String) {
        CommonUtil.alertDialogWithOkOnly(this, "Error", string)
    }

    override fun onClick(v: View?) {
        when(v?.id) {
            R.id.tvCurrent -> {
                val currentDate = CommonUtil.getCurrentDate()
                startDateParam = CommonUtil.firstDayOfMonth()
                endDateParam = currentDate
                binding.tvCurrent.setBackground(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.black_cyrve_rect
                    )
                );
                binding.tvPrevious.setBackground(
                    ContextCompat.getDrawable(
                        this,
                        R.color.primary_little_fade
                    )
                );
                binding.tvBeforePrevious.setBackground(
                    ContextCompat.getDrawable(
                        this,
                        R.color.primary_little_fade
                    )
                );
                startlimit = 0
                list.clear()
                hitApi()
            }
            R.id.tvPrevious -> {
                startDateParam = CommonUtil.firstDayOfLastMonth()
                val lastDayOfPreviousMonth = CommonUtil.lastDayOfPreviousMonth()
                endDateParam = lastDayOfPreviousMonth
                binding.tvCurrent.background = ContextCompat.getDrawable(
                    this,
                    R.color.primary_little_fade
                );
                binding.tvPrevious.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.black_cyrve_rect
                );
                binding.tvBeforePrevious.background = ContextCompat.getDrawable(
                    this,
                    R.color.primary_little_fade
                );
                startlimit = 0
                list.clear()
                hitApi()
            }
            R.id.tvBeforePrevious -> {
                val currentDate = CommonUtil.firstDayOfBeforeLastMonth()
                val endDate = CommonUtil.lastDayOfBeforePreviousMonth()
                startDateParam = currentDate
                endDateParam = endDate
                binding.tvCurrent.setBackground(
                    ContextCompat.getDrawable(
                        this,
                        R.color.primary_little_fade
                    )
                );
                binding.tvBeforePrevious.setBackground(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.black_cyrve_rect
                    )
                );
                binding.tvPrevious.setBackground(
                    ContextCompat.getDrawable(
                        this,
                        R.color.primary_little_fade
                    )
                )
                startlimit = 0
                list.clear()
                hitApi()
            }
        }
    }
}