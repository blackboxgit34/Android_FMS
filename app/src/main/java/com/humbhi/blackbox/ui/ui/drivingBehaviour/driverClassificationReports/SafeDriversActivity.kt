package com.humbhi.blackbox.ui.ui.drivingBehaviour.driverClassificationReports

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.DatePicker
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivitySafeDriversBinding
import com.humbhi.blackbox.ui.adapters.SafeDriverAdapter
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.models.ConsolidateVoilationData
import com.humbhi.blackbox.ui.data.models.SafeDriversDataModel
import com.humbhi.blackbox.ui.retofit.Retrofit2
import com.humbhi.blackbox.ui.retofit.RetrofitResponse
import com.humbhi.blackbox.ui.utils.CommonUtil
import com.humbhi.blackbox.ui.utils.Constants
import com.kal.rackmonthpicker.RackMonthPicker
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Response
import java.util.*


class SafeDriversActivity : AppCompatActivity(), RetrofitResponse,View.OnClickListener {

    // this activity is user for all three reports
    // i.e. Safe, moderate, risky
    private lateinit var binding: ActivitySafeDriversBinding
    private lateinit var adapter: SafeDriverAdapter
    private lateinit var rankType: String
    private lateinit var startDateParam: String
    private lateinit var endDateParam: String
    var list: ArrayList<SafeDriversDataModel> = java.util.ArrayList<SafeDriversDataModel>()
    var picker: DatePicker? = null
    var startlimit = 0
    var limit = 20
    var listNew : ArrayList<ConsolidateVoilationData> = ArrayList()
    var totalRecords = 0
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySafeDriversBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()
        setToolbar()

        binding.etSearchBar.setOnEditorActionListener(TextView.OnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                startlimit = 0
                binding.loadMore.visibility = View.GONE
                list.clear()
                getDriversReport()
            }
            false
        })
        dateFilter()
        getDriversReport()
    }

    private fun setToolbar() {

        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivSort.visibility = View.GONE
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun getIntentData(){
        if (intent.hasExtra("rankType")){
            rankType = intent.getStringExtra("rankType").toString()
            binding.toolbar.tvTitle.text = intent.getStringExtra("title").toString()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun dateFilter(){
        startDateParam = CommonUtil.firstDayOfMonth()
        endDateParam = CommonUtil.getCurrentDate()
        binding.tvToday.setOnClickListener(this)
        binding.tvYesterday.setOnClickListener(this)
        binding.tvWeek.setOnClickListener(this)
        binding.tvCustom.setOnClickListener(this)
        binding.tvStartDate.setOnClickListener(this)
        binding.tvEndDate.setOnClickListener(this)
        binding.btnAppy.setOnClickListener(this)
    }

    private fun getDriversReport() {
        Retrofit2(
            this, this, Constants.REQ_GET_SAFE_DRIVERS,
            Constants.GET_SAFE_DRIVERS.toString()+"custid="+CommonData.getCustIdFromDB()
                    +"&rankType="+rankType+"&stMonth=$startDateParam%2012:00%20AM&downloadType=null&reportName=&sEcho=1&iDisplayStart="+startlimit+"&iDisplayLength="+limit+"&sSearch="+binding.etSearchBar.text.toString()+"&iSortCol_0=0&sSortDir_0=asc"
        )
            .callService(true)
    }

    override fun onServiceResponse(requestCode: Int, response: Response<ResponseBody>?) {
        when (requestCode) {
            Constants.REQ_GET_SAFE_DRIVERS ->
                if (response!!.isSuccessful) {
                list.clear()
                val jsonObj = JSONObject(response.body()?.string())
                var i = 0
                var jsonArray = JSONArray()
                jsonArray = jsonObj.getJSONArray("aaData")
                while (i < jsonArray.length()) {
                    val obj = jsonArray.getJSONObject(i)
                    val model = SafeDriversDataModel()
                    model.driver = obj.getString("Driver")
                    model.ha = obj.getString("HA")
                    model.hb = obj.getString("HB")
                    model.rt = obj.getString("RT")
                    model.os = obj.getString("OS")
                    model.total = obj.getString("Total")
                    list.add(model)
                    i++

                }
                    totalRecords= jsonObj.getInt("iTotalRecords")
                    val layoutManager = LinearLayoutManager(this)
                    binding.rvRecycler.layoutManager = layoutManager
                    adapter = SafeDriverAdapter(this, list)
                    binding.rvRecycler.adapter = adapter
                    binding.rvRecycler.scrollToPosition(startlimit)
                    if(list.size==totalRecords){
                        binding.loadMore.visibility = View.GONE
                    }
                    binding.loadMore.setOnClickListener {
                        if(list.size<totalRecords) {
                            binding.llCustomDateRange.visibility = View.GONE
                            startlimit += 20
                            getDriversReport()
                        }
                    }

            }
        }
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tvToday ->{
                    RackMonthPicker(this)
                    .setLocale(Locale.ENGLISH)
                    .setPositiveButton { month, startDate, endDate, year, monthLabel ->
                        startDateParam = "$month/$startDate/$year"
                        binding.tvToday.text = monthLabel.toString()
                        startlimit = 0
                        list.clear()
                        getDriversReport()
                    }
                    .setNegativeButton {
                        it.dismiss()
                    }
                    .show()

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
                getDriversReport()
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
                getDriversReport()
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
                getDriversReport()
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