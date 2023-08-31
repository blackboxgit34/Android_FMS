package com.humbhi.blackbox.ui.ui.fms

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.DatePicker
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityDocumentReminderBinding
import com.humbhi.blackbox.ui.MyApplication
import com.humbhi.blackbox.ui.adapters.DailyReportAdapter
import com.humbhi.blackbox.ui.adapters.FMSDocumentsRemindersAdapter
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.models.AaDataXXXX
import com.humbhi.blackbox.ui.data.models.DocumenTReminderModel
import com.humbhi.blackbox.ui.data.models.FuelData
import com.humbhi.blackbox.ui.data.models.ObjDailyReport
import com.humbhi.blackbox.ui.data.models.ReminderData
import com.humbhi.blackbox.ui.retofit.NetworkService
import com.humbhi.blackbox.ui.retofit.NewRetrofitClient
import com.humbhi.blackbox.ui.ui.addonReports.fuel.FuelFillingDetailActivity
import com.humbhi.blackbox.ui.utils.CommonUtil
import com.humbhi.blackbox.ui.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.text.SimpleDateFormat
import java.util.Calendar

class DocumentReminder : AppCompatActivity(), View.OnClickListener {
    lateinit var binding : ActivityDocumentReminderBinding
    var startlimit = 0
    var limit = 20
    var search = ""
    var list: ArrayList<AaDataXXXX> = ArrayList()
    var startDateParam = ""
    var endDateParam = ""
    var startTime = ""
    var endTime = ""
    var picker: DatePicker? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocumentReminderBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.tvTitle.visibility = View.VISIBLE
        binding.toolbar.ivAdd.visibility = View.VISIBLE
        binding.toolbar.tvTitle.text = "Documents Reminder"
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.toolbar.ivAdd.setOnClickListener {
            val intent = Intent(this,AddDocumentActivity::class.java)
            startActivity(intent)
        }
        binding.etSearchBar.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                search = textView.text.toString()
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
        startDateParam = CommonUtil.firstDayOfMonth()
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
        binding.progress.progressLayout.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            val Api = NewRetrofitClient.getInstance().create(NetworkService::class.java)
            Api.getDocumentReminder(
                startDateParam + "" + startTime,
                endDateParam + "" + endTime,
                 CommonData.getCustIdFromDB(),
                "0",
                "",
                "",
                "R",
                "1",
                startlimit.toString(),
                limit.toString(),
                search,
                "0",
                "asc"
            ).enqueue(object : Callback<DocumenTReminderModel> {
                override fun onResponse(
                    call: Call<DocumenTReminderModel>,
                    response: Response<DocumenTReminderModel>
                ) {
                    CoroutineScope(Dispatchers.Main).launch {
                        binding.progress.progressLayout.visibility = View.GONE
                        val responseFromApi = response.body()!!
                        binding.rvVehicles.layoutManager = LinearLayoutManager(this@DocumentReminder)
                        for (i in 0 until responseFromApi.aaData.size){
                                list.add(
                                    AaDataXXXX(
                                        responseFromApi.aaData[i].DueDate,
                                        responseFromApi.aaData[i].Remarks,
                                        responseFromApi.aaData[i].RenewalTypeId,
                                        responseFromApi.aaData[i].beginDate,
                                        responseFromApi.aaData[i].beginTime,
                                        responseFromApi.aaData[i].endDate,
                                        responseFromApi.aaData[i].endTime,
                                        responseFromApi.aaData[i].reminderCount,
                                        responseFromApi.aaData[i].reminderData,
                                        responseFromApi.aaData[i].renewalID,
                                        responseFromApi.aaData[i].vehicleID,
                                        responseFromApi.aaData[i].vehicleName
                                    )
                                )
                        }
                        val adapter = FMSDocumentsRemindersAdapter(this@DocumentReminder,list)
                        binding.rvVehicles.adapter = adapter
                        binding.rvVehicles.scrollToPosition(startlimit)
                        if(list.size==responseFromApi.iTotalRecords){
                            binding.loadMore.visibility = View.GONE
                        }
                        binding.loadMore.setOnClickListener {
                            if(list.size<responseFromApi.iTotalRecords) {
                                startlimit += 20
                                hitApi()
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<DocumenTReminderModel>, t: Throwable) {
                    CoroutineScope(Dispatchers.Main).launch {
                        binding.progress.progressLayout.visibility = View.GONE
                        if (t is SocketTimeoutException) {
                            Constants.alertDialog(
                                this@DocumentReminder,
                                "Check your internet connection"
                            )
                        } else {
                            Constants.alertDialog(
                                this@DocumentReminder,
                                "Something went wrong"
                            )
                        }
                    }
                }

            })
        }
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
        } catch (e: Exception) {
            // e.printStackTrace();
            picker = datePickerDialog.datePicker
        }
        datePickerDialog.show()
    }

}