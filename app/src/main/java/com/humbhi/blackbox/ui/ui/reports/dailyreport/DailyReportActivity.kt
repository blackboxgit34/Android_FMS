package com.humbhi.blackbox.ui.ui.reports.dailyreport

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
import com.humbhi.blackbox.databinding.ActivityDailyReportBinding
import com.humbhi.blackbox.ui.Utility.EndlessRecyclerOnScrollListener
import com.humbhi.blackbox.ui.adapters.DailyReportAdapter
import com.humbhi.blackbox.ui.adapters.StoppageReportAdapter
import com.humbhi.blackbox.ui.data.DataManagerImpl
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.models.DailyReportResponse
import com.humbhi.blackbox.ui.data.models.ObjDailyReport
import com.humbhi.blackbox.ui.data.models.VehicleLiveStatusDataItem
import com.humbhi.blackbox.ui.data.network.RestClient
import com.humbhi.blackbox.ui.ui.vehicleStatus.VehcileStatusPresenterImpl
import com.humbhi.blackbox.ui.utils.CommonUtil
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

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
        var enddate = Calendar.getInstance().time
        val sdf = SimpleDateFormat("HH:mm:ss")
        endTime =  "%20" + sdf.format(enddate)
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
        presenter.getDailyReportDataApi(startDateParam+startTime,endDateParam+endTime,CommonData.getCustIdFromDB(),startlimit,limit,search.trim())
    }

    override fun getDailyReportResponse(dailyReportResponse: DailyReportResponse) {
        val layoutManager = LinearLayoutManager(this)
        val totalRecords = dailyReportResponse.totalRecords
        binding.rvRecycler.layoutManager = layoutManager
        for(i in 0 until  dailyReportResponse.objDailyReport.size) {
            list.add(dailyReportResponse.objDailyReport[i])
        }
        adapter = DailyReportAdapter(this,list)
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
            R.id.tvToday -> {
                val currentDate = CommonUtil.getCurrentDate()
                startDateParam = currentDate
                endDateParam = currentDate
                binding.tvYesterday.setBackground(
                    ContextCompat.getDrawable(
                        this,
                        R.color.primary_little_fade
                    )
                );
                binding.tvToday.setBackground(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.black_cyrve_rect
                    )
                );
                binding.tvWeek.setBackground(
                    ContextCompat.getDrawable(
                        this,
                        R.color.primary_little_fade
                    )
                );
                binding.tvCustom.setBackground(
                    ContextCompat.getDrawable(
                        this,
                        R.color.primary_little_fade
                    )
                );
                binding.llCustomDateRange.visibility = View.GONE
                startlimit = 0
                list.clear()
                hitApi()
            }
            R.id.tvYesterday -> {
                val yesterdayDate = CommonUtil.getYesterdayDate()
                startDateParam = yesterdayDate
                endDateParam = CommonUtil.getCurrentDate()
                binding.tvYesterday.setBackground(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.black_cyrve_rect
                    )
                );
                binding.tvToday.setBackground(
                    ContextCompat.getDrawable(
                        this,
                        R.color.primary_little_fade
                    )
                );
                binding.tvWeek.setBackground(
                    ContextCompat.getDrawable(
                        this,
                        R.color.primary_little_fade
                    )
                );
                binding.tvCustom.setBackground(
                    ContextCompat.getDrawable(
                        this,
                        R.color.primary_little_fade
                    )
                );
                binding.llCustomDateRange.visibility = View.GONE
                startlimit = 0
                list.clear()
                hitApi()
            }
            R.id.tvWeek -> {
                val currentDate = CommonUtil.getCurrentDate()
                val endDate = CommonUtil.getWeekDate()
                startDateParam = endDate
                endDateParam = currentDate
                binding.tvYesterday.setBackground(
                    ContextCompat.getDrawable(
                        this,
                        R.color.primary_little_fade
                    )
                );
                binding.tvWeek.setBackground(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.black_cyrve_rect
                    )
                );
                binding.tvToday.setBackground(
                    ContextCompat.getDrawable(
                        this,
                        R.color.primary_little_fade
                    )
                );
                binding.tvCustom.setBackground(
                    ContextCompat.getDrawable(
                        this,
                        R.color.primary_little_fade
                    )
                );
                binding.llCustomDateRange.visibility = View.GONE
                startlimit = 0
                list.clear()
                hitApi()
            }
            R.id.tvCustom -> {
                binding.tvYesterday.setBackground(
                    ContextCompat.getDrawable(
                        this,
                        R.color.primary_little_fade
                    )
                );
                binding.tvCustom.setBackground(
                    ContextCompat.getDrawable(
                        this,
                        R.drawable.black_cyrve_rect
                    )
                );
                binding.tvToday.setBackground(
                    ContextCompat.getDrawable(
                        this,
                        R.color.primary_little_fade
                    )
                );
                binding.tvWeek.setBackground(
                    ContextCompat.getDrawable(
                        this,
                        R.color.primary_little_fade
                    )
                );
                binding.llCustomDateRange.visibility = View.VISIBLE
            }
            R.id.tvStartDate -> {
                datepicker("1")
            }
            R.id.tvEndDate -> {
                datepicker("2")
            }
            R.id.btnAppy -> {
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
                    endTime = "%2011:59:00"
                    endDateParam = "$year-$x-$y"
                    binding.tvEndDate.setText("$y-$x-$year")
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
}