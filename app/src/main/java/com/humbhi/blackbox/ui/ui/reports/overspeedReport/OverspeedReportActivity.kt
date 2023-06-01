package com.humbhi.blackbox.ui.ui.reports.overspeedReport

import android.app.Activity
import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.DatePicker
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityOverspeedReportBinding
import com.humbhi.blackbox.ui.MyApplication
import com.humbhi.blackbox.ui.Utility.EndlessRecyclerOnScrollListener
import com.humbhi.blackbox.ui.adapters.OverspeedAdapter
import com.humbhi.blackbox.ui.data.DataManagerImpl
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.models.MonthData
import com.humbhi.blackbox.ui.data.models.OverSpeedData
import com.humbhi.blackbox.ui.data.models.Overspeed
import com.humbhi.blackbox.ui.data.models.OverspeedResponseModel
import com.humbhi.blackbox.ui.data.network.RestClient
import com.humbhi.blackbox.ui.ui.reports.overspeedReport.overSpeedDetail.OverspeedDetailActivity
import com.humbhi.blackbox.ui.utils.CommonUtil
import com.humbhi.blackbox.ui.utils.ExplicitIntentUtil
import java.util.*
import kotlin.collections.ArrayList

class OverspeedReportActivity : AppCompatActivity(),OverspeedReportView,View.OnClickListener {

    private lateinit var binding:ActivityOverspeedReportBinding
    private lateinit var adapter: OverspeedAdapter
    private lateinit var startDateParam: String
    private lateinit var endDateParam: String
    private lateinit var mPresenter: OverSpeedReportPresenter
    var picker: DatePicker? = null
    var startlimit = 0
    var limit = 20
    var list : ArrayList<Overspeed> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOverspeedReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mPresenter = OverspeedReportPresenterImpl(
            this,
            DataManagerImpl(RestClient.getRetrofitBuilderForTrackMaster())
        )

        binding.toolbar.tvTitle.text = "Overspeed Report"
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
        startDateParam = CommonUtil.getCurrentDate()
        endDateParam = CommonUtil.getCurrentDate()
        binding.tvToday.setOnClickListener(this)
        binding.tvYesterday.setOnClickListener(this)
        binding.tvWeek.setOnClickListener(this)
        binding.tvCustom.setOnClickListener(this)
        binding.tvStartDate.setOnClickListener(this)
        binding.tvEndDate.setOnClickListener(this)
        binding.btnAppy.setOnClickListener(this)
    }

    private fun hitApi(){
        binding.progressLayout.progressLayout.visibility = View.VISIBLE
        mPresenter.hitOverSpeedReportApi("$startDateParam%2012:00%20AM", endDateParam+"%2011:59%20PM", "","over", CommonData.getCustIdFromDB(), "", 1, startlimit, limit, binding.etSearchBar.text.toString().trim().toUpperCase(), "1", "", "")
    }


    override fun getOverSpeedResponseData(overSpeedResponseModel: OverspeedResponseModel) {
        val layoutManager = LinearLayoutManager(this)
        val totalRecords = overSpeedResponseModel.iTotalRecords
        for(i in 0 until  overSpeedResponseModel.aaData.size) {
            list.add(overSpeedResponseModel.aaData[i])
        }

        binding.rvRecycler.layoutManager = layoutManager
        adapter = OverspeedAdapter(this, object: OverspeedAdapter.OverSpeedDetails{
            override fun onVehicleSelection(position: Int) {
                MyApplication.overSpeedDetailList = list[position].overSpeedData as ArrayList<OverSpeedData>
                val bundle = Bundle()
                bundle.putString("VehicleName", list[position].vehname)
                ExplicitIntentUtil.startActivity(
                   this@OverspeedReportActivity,
                    OverspeedDetailActivity::class.java, bundle)
            }
        },list)
        binding.rvRecycler.adapter = adapter
        binding.rvRecycler.scrollToPosition(startlimit)
        if(totalRecords>20){
            binding.loadMore.visibility = View.VISIBLE
        }
        binding.loadMore.setOnClickListener {
            if(list.size<totalRecords) {
                binding.llCustomDateRange.visibility = View.GONE
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
        CommonUtil.alertDialogWithOkOnly(this,"Error",string)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tvToday ->{
                val currentDate = CommonUtil.getCurrentDate()
                startDateParam = currentDate
                endDateParam = currentDate
                binding.tvYesterday.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                binding.tvToday.setBackground(ContextCompat.getDrawable(this, R.drawable.black_cyrve_rect));
                binding.tvWeek.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                binding.tvCustom.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                binding.llCustomDateRange.visibility = View.GONE
                startlimit = 0
                list.clear()
                hitApi()
            }
            R.id.tvYesterday ->{
                val yesterdayDate = CommonUtil.getYesterdayDate()
                startDateParam = yesterdayDate
                endDateParam = CommonUtil.getCurrentDate()
                binding.tvYesterday.setBackground(ContextCompat.getDrawable(this, R.drawable.black_cyrve_rect));
                binding.tvToday.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                binding.tvWeek.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                binding.tvCustom.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                binding.llCustomDateRange.visibility = View.GONE
                startlimit = 0
                list.clear()
                hitApi()
            }
            R.id.tvWeek ->{
                val currentDate = CommonUtil.getCurrentDate()
                val endDate = CommonUtil.getWeekDate()
                startDateParam = endDate
                endDateParam = currentDate
                binding.tvYesterday.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                binding.tvWeek.setBackground(ContextCompat.getDrawable(this, R.drawable.black_cyrve_rect));
                binding.tvToday.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                binding.tvCustom.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                binding.llCustomDateRange.visibility = View.GONE
                startlimit = 0
                list.clear()
                hitApi()
            }
            R.id.tvCustom ->{
                binding.tvYesterday.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                binding.tvCustom.setBackground(ContextCompat.getDrawable(this, R.drawable.black_cyrve_rect));
                binding.tvToday.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                binding.tvWeek.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                binding.llCustomDateRange.visibility = View.VISIBLE
            }
            R.id.tvStartDate ->{
                datepicker("1")
            }
            R.id.tvEndDate ->{
                datepicker("2")
            }
            R.id.btnAppy ->{
                binding.llCustomDateRange.visibility = View.GONE
                startlimit = 0
                list.clear()
                hitApi()
            }
        }

    }

    fun datepicker(flag: String) {
        val cal = Calendar.getInstance()
        cal.add(Calendar.YEAR, 0) // to get back 13 year add -13
        val previous_year = cal.time
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { view, year, monthOfYear, dayOfMonth ->
                var monthOfYear = monthOfYear
                val x: String
                val y: String
                if (monthOfYear < 9) {
                    monthOfYear = monthOfYear + 1
                    x = "0$monthOfYear"
                } else {
                    x = (monthOfYear + 1).toString()
                }
                y = if (dayOfMonth < 10) {
                    "0$dayOfMonth"
                } else {
                    dayOfMonth.toString()
                }
                if (flag == "1") {
                    startDateParam = "$year-$x-$y"
                    binding.tvStartDate.setText("$y-$x-$year")
                }
                if (flag == "2") {
                    endDateParam = "$year-$x-$y"
                    binding.tvEndDate.setText("$y-$x-$year")
                }
            }, calendar[Calendar.YEAR], calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH]
        )
        try {
            picker = datePickerDialog.datePicker
            //  picker.setMinDate(System.currentTimeMillis());
            picker!!.setMaxDate(previous_year.time)
        } catch (e: Exception) {
            // e.printStackTrace();
            picker = datePickerDialog.datePicker
        }
        datePickerDialog.show()
    }
}