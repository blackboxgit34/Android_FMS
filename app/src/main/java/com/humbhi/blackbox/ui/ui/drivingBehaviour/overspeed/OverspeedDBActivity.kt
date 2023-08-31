package com.humbhi.blackbox.ui.ui.drivingBehaviour.overspeed

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.DatePicker
import android.widget.TextView
import android.widget.TimePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityOverspeedBinding
import com.humbhi.blackbox.ui.MyApplication
import com.humbhi.blackbox.ui.adapters.OverSpeedDBAdapter
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.models.AaDataXXXXXX
import com.humbhi.blackbox.ui.data.models.DrOverSpeedSubLst
import com.humbhi.blackbox.ui.data.models.OverSpeedDBModel
import com.humbhi.blackbox.ui.retofit.NetworkService
import com.humbhi.blackbox.ui.retofit.NewRetrofitClient
import com.humbhi.blackbox.ui.utils.CommonUtil
import com.humbhi.blackbox.ui.utils.Constants
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.Calendar


class OverspeedDBActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var binding: ActivityOverspeedBinding
    private lateinit var startDateParam: String
    private lateinit var endDateParam: String
    var picker: DatePicker? = null
    var totalRecords = 0
    var startlimit = 0
    var limit = 20
    var search = ""
    var list : java.util.ArrayList<AaDataXXXXXX> = java.util.ArrayList()
    var startTime = ""
    var endTime = ""
    var customStartDateSelcted = ""
    var customEndDateSelcted = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOverspeedBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.tvTitle.text = "Over Speed"
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBell.visibility = View.GONE
        binding.rvRecycler.setHasFixedSize(true)
        binding.rvRecycler.layoutManager = LinearLayoutManager(this)
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.etSearchBar.setOnEditorActionListener(TextView.OnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search = textView.text.toString()
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
    private fun dateFilter() {
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

    fun hitApi(){
        binding.progress.progressLayout.visibility = View.VISIBLE
        val api = NewRetrofitClient.getInstance().create(NetworkService::class.java)
        api.getOverSpeedReport(startDateParam+startTime,
            endDateParam+endTime,
            CommonData.getCustIdFromDB(),
            "null",
            "",
            "1",
            startlimit,
            limit,
            search,
            "0",
            "asc"
        ).enqueue(object: Callback<OverSpeedDBModel>{
            override fun onResponse(
                call: Call<OverSpeedDBModel>,
                response: Response<OverSpeedDBModel>
            ) {
                binding.progress.progressLayout.visibility = View.GONE
                val responseBody = response.body()
                if (responseBody != null) {
                    totalRecords = responseBody.iTotalDisplayRecords
                    for(i in 0 until responseBody.aaData.size){
                     list.add(
                         AaDataXXXXXX(
                             responseBody.aaData[i].BBID,
                             responseBody.aaData[i].SNo,
                             responseBody.aaData[i].Speed,
                             responseBody.aaData[i].VehicleName,
                             responseBody.aaData[i].count,
                             responseBody.aaData[i].drOverSpeedSubLst)
                     )
                        val adapter = OverSpeedDBAdapter(this@OverspeedDBActivity,object: OverSpeedDBAdapter.OverSpeedDetails{
                            override fun onVehicleSelection(position: Int){
                                MyApplication.OverspeedDetailDBList = list[position].drOverSpeedSubLst as ArrayList<DrOverSpeedSubLst>
                                startActivity(Intent(this@OverspeedDBActivity,OverspeedDetailActivity::class.java))
                            }
                        },list)
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
                }
            }

            override fun onFailure(call: Call<OverSpeedDBModel>, t: Throwable) {
                binding.progress.progressLayout.visibility = View.GONE
                if(t is SocketTimeoutException){
                    Constants.alertDialog(this@OverspeedDBActivity,"Connection timed out")
                }
                else{
                    Constants.alertDialog(this@OverspeedDBActivity,"Something went wrong")
                }
            }
        })

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
                hitApi()
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
                startlimit = 0
                binding.llCustomDateRange.customeDate.visibility = View.GONE
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