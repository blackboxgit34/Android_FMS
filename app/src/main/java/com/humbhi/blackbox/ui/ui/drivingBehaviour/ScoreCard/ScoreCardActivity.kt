package com.humbhi.blackbox.ui.ui.drivingBehaviour.ScoreCard

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityScoreCardBinding
import com.humbhi.blackbox.ui.Utility.EndlessRecyclerOnScrollListener
import com.humbhi.blackbox.ui.adapters.ScoreCardAdapter
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.models.AaData
import com.humbhi.blackbox.ui.data.models.ScoreCardReportModel
import com.humbhi.blackbox.ui.data.models.WorkingHourData
import com.humbhi.blackbox.ui.retofit.NetworkService
import com.humbhi.blackbox.ui.retofit.NewRetrofitClient
import com.humbhi.blackbox.ui.utils.CommonUtil
import com.humbhi.blackbox.ui.utils.Constants
import com.kal.rackmonthpicker.RackMonthPicker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class ScoreCardActivity : AppCompatActivity(), View.OnClickListener {
    private var binding: ActivityScoreCardBinding? = null
    private lateinit var monthOftheYear: String
    private lateinit var Year: String
    lateinit var  scoreCardReportModel : ScoreCardReportModel
    var picker: DatePicker? = null
    var totalRecords = 0
    var startlimit = 0
    var limit = 20
    var list : java.util.ArrayList<AaData> = java.util.ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityScoreCardBinding.inflate(layoutInflater)
        setContentView(binding!!.root)
        binding!!.toolbar.ivBack.visibility = View.VISIBLE
        binding!!.toolbar.tvTitle.text = "Score card"
        binding!!.toolbar.ivMenu.visibility = View.GONE
        binding!!.toolbar.ivBell.visibility = View.GONE
        binding!!.rvScoreCard.setHasFixedSize(true)
        binding!!.rvScoreCard.layoutManager = LinearLayoutManager(this)
        binding!!.toolbar.ivBack.setOnClickListener {
            finish()
        }
        dateFilter()
        hitApi()
    }
    private fun dateFilter() {
        monthOftheYear = CommonUtil.getMonthDateNew()
        Year = CommonUtil.getYear()
        binding!!.tvToday.setOnClickListener(this)
        binding!!.tvYesterday.setOnClickListener(this)
        binding!!.tvWeek.setOnClickListener(this)
        binding!!.tvCustom.setOnClickListener(this)
        binding!!.tvStartDate.setOnClickListener(this)
        binding!!.tvEndDate.setOnClickListener(this)
        binding!!.btnAppy.setOnClickListener(this)
    }

    fun hitApi(){
        binding!!.progressLayout.progressLayout.visibility = View.VISIBLE
        val api = NewRetrofitClient.getInstance().create(NetworkService::class.java)
        val apiCall = api.getScoreCardReport(monthOftheYear,Year,CommonData.getCustIdFromDB(),"null","","1",startlimit,limit,"","0","asc")
        apiCall.enqueue(object : Callback<ScoreCardReportModel> {
            override fun onResponse(
                call: Call<ScoreCardReportModel>,
                response: Response<ScoreCardReportModel>
            ) {
                binding!!.progressLayout.progressLayout.visibility = View.GONE
                scoreCardReportModel = response.body()!!
                totalRecords = scoreCardReportModel.iTotalRecords
                for(i in 0 until  scoreCardReportModel.aaData.size) {
                    list.add(scoreCardReportModel.aaData[i])
                }
                val adapter = ScoreCardAdapter(this@ScoreCardActivity,list)
                binding!!.rvScoreCard.adapter = adapter
                binding!!.rvScoreCard.scrollToPosition(startlimit)
                if(totalRecords>20){
                    binding!!.loadMore.visibility = View.VISIBLE
                }
                binding!!.loadMore.setOnClickListener {
                    if(list.size<totalRecords) {
                        startlimit += 20
                        hitApi()
                    }
                }
            }

            override fun onFailure(call: Call<ScoreCardReportModel>, t: Throwable) {
                binding!!.progressLayout.progressLayout.visibility = View.GONE
                Constants.alertDialog(this@ScoreCardActivity,"Something went wrong.")
            }

        })
    }

    override fun onClick(v: View?) {
            when(v?.id){
                R.id.tvToday ->{
                    val rackMonthPicker: RackMonthPicker

                    RackMonthPicker(this)
                        .setLocale(Locale.ENGLISH)
                        .setPositiveButton { month, startDate, endDate, year, monthLabel ->

                            monthOftheYear = "$month"
                            binding!!.tvToday.text = monthLabel.toString()
                            startlimit = 0
                            list.clear()
                            hitApi()
                        }
                        .setNegativeButton {  }.show()

                }
                R.id.tvYesterday ->{
                    monthOftheYear = CommonUtil.getMonthDateNew()
                    Year = CommonUtil.getYear()
                    binding!!.tvYesterday.setBackground(ContextCompat.getDrawable(this, R.drawable.black_cyrve_rect));
                    binding!!.tvToday.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                    binding!!.tvWeek.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                    binding!!.tvCustom.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                    binding!!.llCustomDateRange.visibility = View.GONE
                    startlimit = 0
                    list.clear()
                    hitApi()
                }
                R.id.tvWeek ->{
                    monthOftheYear = CommonUtil.getMonthDateNew()
                    Year = CommonUtil.getYear()
                    binding!!.tvYesterday.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                    binding!!.tvWeek.setBackground(ContextCompat.getDrawable(this, R.drawable.black_cyrve_rect));
                    binding!!.tvToday.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                    binding!!.tvCustom.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                    binding!!.llCustomDateRange.visibility = View.GONE
                    startlimit = 0
                    list.clear()
                    hitApi()
                }
                R.id.tvCustom ->{
                    binding!!.tvYesterday.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                    binding!!.tvCustom.setBackground(ContextCompat.getDrawable(this, R.drawable.black_cyrve_rect));
                    binding!!.tvToday.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                    binding!!.tvWeek.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                    binding!!.llCustomDateRange.visibility = View.VISIBLE
                }
                R.id.tvStartDate ->{
                    datepicker("1")
                }
                R.id.tvEndDate ->{
                    datepicker("2")
                }
                R.id.btnAppy ->{
                    binding!!.llCustomDateRange.visibility = View.GONE
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
                    monthOftheYear = "$y"
                    binding!!.tvStartDate.setText("$y")
                }
                if (flag == "2") {
                    Year = "$year"
                    binding!!.tvEndDate.setText("$year")
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

