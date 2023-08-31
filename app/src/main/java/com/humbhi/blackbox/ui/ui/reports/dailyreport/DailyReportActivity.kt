package com.humbhi.blackbox.ui.ui.reports.dailyreport

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityDailyReportBinding
import com.humbhi.blackbox.ui.adapters.DailyReportAdapter
import com.humbhi.blackbox.ui.data.DataManagerImpl
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.models.DailyReportResponse
import com.humbhi.blackbox.ui.data.models.ObjDailyReport
import com.humbhi.blackbox.ui.data.network.RestClient
import com.humbhi.blackbox.ui.utils.CommonUtil
import com.humbhi.blackbox.ui.utils.Constants
import java.text.SimpleDateFormat
import java.util.*

class DailyReportActivity : AppCompatActivity(),DailyReportView , View.OnClickListener{
    private lateinit var binding:ActivityDailyReportBinding
    private lateinit var adapter:DailyReportAdapter
    private lateinit var presenter: DailyReportPresenter
    var startlimit = 0
    var limit = 20
    var list : ArrayList<ObjDailyReport> = ArrayList()
    var search = ""
    var startDateParam = ""
    var endDateParam = ""
    var startTime = ""
    var endTime = ""
    var picker: DatePicker? = null
    var customStartDateSelcted = ""
    var totalRecords = 0
    var customEndDateSelcted = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDailyReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        presenter = DailyReportPresenterImpl(this,
            DataManagerImpl(RestClient.getRetrofitBuilderForHitec())
        )
        binding.toolbar.tvTitle.text = "Daily Report"
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivSort.visibility = View.VISIBLE
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.etSearchBar.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                startlimit = 0
                binding.loadMore.visibility = View.GONE
                list.clear()
                hitApi()
            }
            false
        }
        dateFilter()
        hitApi()
    }

    private fun dateFilter(){
        startDateParam = CommonUtil.getCurrentDate()
        endDateParam = CommonUtil.getCurrentDate()
        startTime = "%2000:00:00"
        val enddate = Calendar.getInstance().time
        val sdf = SimpleDateFormat("HH:mm:ss")
        binding.llCustomDateRange.tvStartTime.setText("12:00 AM")
        binding.llCustomDateRange.tvEndTime.setText("11:59 PM")
        endTime =  "%20" + sdf.format(enddate)
        binding.tvToday.setOnClickListener(this)
        binding.tvYesterday.setOnClickListener(this)
        binding.tvWeek.setOnClickListener(this)
        binding.tvCustom.setOnClickListener(this)
        val parts = sdf.format(enddate).split(":")
        val hour = parts[0].toInt()
        val minute = parts[1].toInt()
        val hourOfDay = if (hour.toInt() == 0) 12 else hour.toInt() % 12
        val amPm = if (hour.toInt() < 12) "AM" else "PM"
        binding.llCustomDateRange.tvEndTime.text = String.format("%02d:%02d %s", hourOfDay, minute, amPm)
        binding.llCustomDateRange.tvStartDate.setOnClickListener(this)
        binding.llCustomDateRange.tvEndDate.setOnClickListener(this)
        binding.llCustomDateRange.tvStartTime.setOnClickListener(this)
        binding.llCustomDateRange.tvEndTime.setOnClickListener(this)
        binding.llCustomDateRange.btnAppy.setOnClickListener(this)
    }

    private fun hitApi(){
        binding.progressLayout.progressLayout.visibility = View.VISIBLE
        presenter.getDailyReportDataApi(startDateParam+startTime,endDateParam+endTime,CommonData.getCustIdFromDB(),startlimit,limit,search.trim())
    }

    override fun getDailyReportResponse(dailyReportResponse: DailyReportResponse) {
        val layoutManager = LinearLayoutManager(this)
        totalRecords = dailyReportResponse.totalrecords
        binding.rvRecycler.layoutManager = layoutManager
        for(i in 0 until  dailyReportResponse.objDailyReport.size) {
            list.add(dailyReportResponse.objDailyReport[i])
        }
        adapter = DailyReportAdapter(this,list)
        binding.rvRecycler.adapter = adapter
        binding.rvRecycler.scrollToPosition(startlimit)
        if(totalRecords==list.size){
            binding.loadMore.visibility = View.GONE
        }
        binding.loadMore.setOnClickListener {
            if(list.size<totalRecords) {
                binding.llCustomDateRange.customeDate.visibility = View.GONE
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
            R.id.tvToday -> {
                val currentDate = CommonUtil.getCurrentDate()
                startDateParam = currentDate
                endDateParam = currentDate
                startTime = "%2000:00:00"
                val enddate = Calendar.getInstance().time
                val sdf = SimpleDateFormat("HH:mm:ss")
                endTime =  "%20" + sdf.format(enddate)
                binding.tvYesterday.background = ContextCompat.getDrawable(
                    this,
                    R.color.primary_little_fade
                )
                binding.tvToday.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.black_cyrve_rect
                )
                binding.tvWeek.background = ContextCompat.getDrawable(
                    this,
                    R.color.primary_little_fade
                )
                binding.tvCustom.background = ContextCompat.getDrawable(
                    this,
                    R.color.primary_little_fade
                )
                binding.llCustomDateRange.customeDate.visibility = View.GONE
                startlimit = 0
                list.clear()
                hitApi()
            }
            R.id.tvYesterday -> {
                val yesterdayDate = CommonUtil.getYesterdayDate()
                startDateParam = yesterdayDate
                endDateParam = CommonUtil.getYesterdayDate()
                startTime = "%2000:00:00"
                endTime = "%2023:59:00"
                binding.tvYesterday.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.black_cyrve_rect
                )
                binding.tvToday.background = ContextCompat.getDrawable(
                    this,
                    R.color.primary_little_fade
                )
                binding.tvWeek.background = ContextCompat.getDrawable(
                    this,
                    R.color.primary_little_fade
                )
                binding.tvCustom.background = ContextCompat.getDrawable(
                    this,
                    R.color.primary_little_fade
                )
                binding.llCustomDateRange.customeDate.visibility = View.GONE
                startlimit = 0
                list.clear()
                hitApi()
            }
            R.id.tvWeek -> {
                val currentDate = CommonUtil.getCurrentDate()
                val endDate = CommonUtil.getWeekDate()
                startDateParam = endDate
                endDateParam = currentDate
                startTime = "%2000:00:00"
                val enddate = Calendar.getInstance().time
                val sdf = SimpleDateFormat("HH:mm:ss")
                endTime =  "%20" + sdf.format(enddate)
                binding.tvYesterday.background = ContextCompat.getDrawable(
                    this,
                    R.color.primary_little_fade
                )
                binding.tvWeek.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.black_cyrve_rect
                )
                binding.tvToday.background = ContextCompat.getDrawable(
                    this,
                    R.color.primary_little_fade
                )
                binding.tvCustom.background = ContextCompat.getDrawable(
                    this,
                    R.color.primary_little_fade
                )
                binding.llCustomDateRange.customeDate.visibility = View.GONE
                startlimit = 0
                list.clear()
                hitApi()
            }
            R.id.tvCustom -> {
                binding.tvYesterday.background = ContextCompat.getDrawable(
                    this,
                    R.color.primary_little_fade
                )
                binding.tvCustom.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.black_cyrve_rect
                )
                binding.tvToday.background = ContextCompat.getDrawable(
                    this,
                    R.color.primary_little_fade
                )
                binding.tvWeek.background = ContextCompat.getDrawable(
                    this,
                    R.color.primary_little_fade
                )
                binding.llCustomDateRange.customeDate.visibility = View.VISIBLE
            }
            R.id.tvStartDate -> {
                datepicker("1")
            }
            R.id.tvEndDate -> {
                datepicker("2")
            }
            R.id.tvStartTime ->{
                startTime()
            }
            R.id.tvEndTime ->{
                endTime()
            }
            R.id.btnAppy -> {
                if(!customStartDateSelcted.equals("") && !customEndDateSelcted.equals("")) {
                    startlimit = 0
                    binding.llCustomDateRange.customeDate.visibility = View.GONE
                    list.clear()
                    hitApi()
                }
                else{
                    Constants.alertDialog(this,"Please select both dates first")
                }
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
                    customStartDateSelcted = startDateParam
                    binding.llCustomDateRange.tvStartDate.setText("$y-$x-$year")
                }
                if (flag == "2") {
                    endTime = "%2011:59:00"
                    endDateParam = "$year-$x-$y"
                    customEndDateSelcted = endDateParam
                    binding.llCustomDateRange.tvEndDate.setText("$y-$x-$year")
                }
            }, calendar[Calendar.YEAR], calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH]
        )
        try {
            picker = datePickerDialog.datePicker
            //  picker.setMinDate(System.currentTimeMillis());
            picker!!.maxDate = previous_year.time
        } catch (e: Exception) {
            // e.printStackTrace();
            picker = datePickerDialog.datePicker
        }
        datePickerDialog.show()
    }

    private fun endTime() {
        val currentTime = Calendar.getInstance()
        val hour = currentTime.get(Calendar.HOUR_OF_DAY)
        val minute = currentTime.get(Calendar.MINUTE)
        val timePickerDialog = TimePickerDialog(this,
            { view: TimePicker?, hourOfDay: Int, minute: Int ->
                val hour = hourOfDay % 12
                binding.llCustomDateRange.tvEndTime.setText(
                    String.format(
                        "%02d:%02d %s",
                        if (hour == 0) 12 else hour,
                        minute,
                        if (hourOfDay < 12) "AM" else "PM"
                    )
                )
                endTime = String.format("%%%02d%02d:%02d:00", 20, hourOfDay, minute)
            }, hour, minute, false
        )
        timePickerDialog.show()
    }

    private fun startTime() {
        val hour = 0
        val minute = 0
        val timePickerDialog = TimePickerDialog(this,
            { view: TimePicker?, hourOfDay: Int, minute: Int ->
                val hour = hourOfDay % 12
                binding.llCustomDateRange.tvStartTime.setText(
                    String.format(
                        "%02d:%02d %s",
                        if (hour == 0) 12 else hour, minute,
                        if (hourOfDay < 12) "AM" else "PM"
                    )
                )
                startTime = String.format("%%%02d%02d:%02d:00", 20, hourOfDay, minute)
            }, hour, minute, false
        )
        timePickerDialog.show()
    }
}