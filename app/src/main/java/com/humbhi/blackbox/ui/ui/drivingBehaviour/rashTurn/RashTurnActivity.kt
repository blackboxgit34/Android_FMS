package com.humbhi.blackbox.ui.ui.drivingBehaviour.rashTurn

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.google.android.gms.maps.model.CameraPosition
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityRashTurnBinding
import com.humbhi.blackbox.ui.adapters.RashTurnAdapter
import com.humbhi.blackbox.ui.adapters.RashTurnChartAdapter
import com.humbhi.blackbox.ui.data.DataManagerImpl
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.models.HarshBreakData
import com.humbhi.blackbox.ui.data.models.RashTurnData
import com.humbhi.blackbox.ui.data.models.RashTurnDataModel
import com.humbhi.blackbox.ui.data.network.RestClient
import com.humbhi.blackbox.ui.utils.CommonUtil
import com.humbhi.blackbox.ui.utils.Constants
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RashTurnActivity : AppCompatActivity(), RashTurnView,View.OnClickListener {

    private lateinit var binding: ActivityRashTurnBinding
    private lateinit var mPresenter: RashTurnPresenter
    private lateinit var startDateParam: String
    private lateinit var endDateParam: String
    private lateinit var adapter: RashTurnAdapter
    private lateinit var chartAdapter: RashTurnChartAdapter
    var DistanceChart: BarChart? = null
    var picker: DatePicker? = null
    var startlimit = 0
    var limit = 20
    var list : ArrayList<RashTurnData> = ArrayList()
    var totalRecords = 0
    var search = ""
    var startTime = ""
    var endTime = ""
    var customStartDateSelcted = ""
    var customEndDateSelcted = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRashTurnBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar()
        mPresenter = RashTurnPresenterImpl(
            this,
            DataManagerImpl(RestClient.getRetrofitBuilderForTrackMaster())
        )
        startDateParam = CommonUtil.getYesterdayDateYearFirst()
        endDateParam = CommonUtil.getCurrentDateYearFirst()
        dateFilter()
        binding.etSearchBar.setOnEditorActionListener(TextView.OnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search = textView.text.toString()
                startlimit = 0
                binding.loadMore.visibility = View.GONE
                list.clear()
                hitAPI()
            }
            false
        })
        hitAPI()
    }

    private fun hitAPI(){
        binding.progress.progressLayout.visibility = View.VISIBLE
        mPresenter.hitRashTurnReportApi(
            startDateParam+startTime,
            endDateParam+endTime,
            CommonData.getCustIdFromDB(),
            "null",
            "",
            1,
             startlimit,
             limit,
             search,
            "",
            "asc",
        )
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

    private fun setToolbar() {
        binding.toolbar.tvTitle.text = "Rash Turns"
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivSort.visibility = View.GONE
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }

    override fun getRashTurnData(rashTurnDataModel: RashTurnDataModel) {
        binding.progress.progressLayout.visibility = View.GONE
        val layoutManager = LinearLayoutManager(this)
        binding.rvRecycler.layoutManager = layoutManager
        totalRecords = rashTurnDataModel.iTotalRecords
        for(i in 0 until  rashTurnDataModel.aaData.size) {
            list.add(rashTurnDataModel.aaData[i])
        }
        adapter = RashTurnAdapter(this, list)
        binding.rvRecycler.adapter = adapter
        binding.rvRecycler.scrollToPosition(startlimit)
         if(totalRecords==list.size){
            binding.loadMore.visibility = View.GONE
        }
        binding.loadMore.setOnClickListener {
            if(list.size<totalRecords) {
                binding.llCustomDateRange.customeDate.visibility = View.GONE
                startlimit += 20
                hitAPI()
            }
        }
        binding.tvTotalCount.text = rashTurnDataModel.iTotalRecords.toString()

        binding.rvChart.layoutManager = LinearLayoutManager(
            this,
            LinearLayoutManager.HORIZONTAL,
            false
        )
        chartAdapter = RashTurnChartAdapter(this, rashTurnDataModel.aaData)
        binding.rvChart.adapter = chartAdapter
        for (i in rashTurnDataModel.aaData.indices){

            setDistancGraphData(rashTurnDataModel.aaData[i].count.toDouble(),i.toFloat())
        }

    }

    private fun setData(counts:Double,position: Float): BarDataSet? {
        val entries = ArrayList<BarEntry>()

        entries.add(BarEntry(position, counts.toFloat()))


        val set = BarDataSet(entries, "")
//        set.setColors(
//            ContextCompat.getColor(DistanceChart!!.context, R.color.voilet_red),
//        )
        set.valueTextColor = Color.rgb(255, 255, 255)
        return set
    }

    // Driving behaviour chart data

    private fun setDistancGraphData(counts:Double,position: Float) {
        DistanceChart = binding.DistanceChart
        val desc: Description
        val L: Legend
        L = DistanceChart!!.legend
        desc = DistanceChart!!.description
        desc.text = "" // this is the weirdest way to clear something!!
        L.isEnabled = false
        val leftAxis = DistanceChart!!.axisLeft
        val rightAxis = DistanceChart!!.axisRight
        val xAxis = DistanceChart!!.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.textSize = 10f
        xAxis.setDrawAxisLine(true)
        xAxis.setDrawGridLines(false)
        leftAxis.textSize = 10f
        leftAxis.setDrawLabels(false)
        leftAxis.setDrawAxisLine(true)
        leftAxis.setDrawGridLines(false)
        rightAxis.setDrawAxisLine(false)
        rightAxis.setDrawGridLines(false)
        rightAxis.setDrawLabels(false)
        val data = BarData(setData(counts,position))
        data.barWidth = 0.4f // set custom bar width
        DistanceChart!!.data = data
        DistanceChart!!.setFitBars(true) // make the x-axis fit exactly all bars
        DistanceChart!!.invalidate() // refresh
        DistanceChart!!.setScaleEnabled(false)
        DistanceChart!!.isDoubleTapToZoomEnabled = false
        //DistanceChart.setBackgroundResource(R.drawable.round_bars);
        DistanceChart!!.setBackgroundColor(resources.getColor(R.color.black))
        DistanceChart!!.animateXY(2000, 2000)
        DistanceChart!!.setDrawBorders(false)
        DistanceChart!!.description = desc
        data.setValueTextColor(Color.rgb(255, 255, 255))
        DistanceChart!!.setDrawValueAboveBar(true)
//        val customRenderer = BarChartCustomRenderer(
//            DistanceChart, DistanceChart!!.animator, DistanceChart!!.viewPortHandler
//        )
        // DistanceChart!!.renderer = customRenderer
    }


    override fun isNetworkConnected(): Boolean {
        if(com.humbhi.blackbox.ui.utils.Network.isNetworkAvailable(this)) {
            return true
        }
        return false
    }

    override fun isShowLoading(): Boolean {
        return true
    }

    override fun isHideLoading(): Boolean {
        return true
    }

    override fun showErrorMessage(string: String) {
        CommonUtil.alertDialogWithOkOnly(this, "Error", string)
    }

    override fun onClick(v: View?) {
        when(v?.id){
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
                startlimit = 0
                binding.llCustomDateRange.customeDate.visibility = View.GONE
                list.clear()
                hitAPI()
            }
            R.id.tvYesterday -> {
                val yesterdayDate = CommonUtil.getYesterdayDate()
                startDateParam = yesterdayDate
                endDateParam = CommonUtil.getYesterdayDate()
                startTime = "%2000:00:00"
                endTime = "%2023:59:59"
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
                startlimit = 0
                binding.llCustomDateRange.customeDate.visibility = View.GONE
                list.clear()
                hitAPI()
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
                startlimit = 0
                binding.llCustomDateRange.customeDate.visibility = View.GONE
                list.clear()
                hitAPI()
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
                    hitAPI()
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