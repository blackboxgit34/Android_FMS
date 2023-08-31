package com.humbhi.blackbox.ui.ui.drivingBehaviour.ScoreCard

import android.app.DatePickerDialog
import android.net.DnsResolver
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityScoreCardBinding
import com.humbhi.blackbox.ui.Utility.CustomDialogFragment
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException
import java.net.UnknownHostException
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
        binding!!.etSearchBar.setOnEditorActionListener(TextView.OnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                startlimit = 0
                binding!!.loadMore.visibility = View.GONE
                list.clear()
                hitApi()
            }
            false
        })
        dateFilter()
        hitApi()
    }
    private fun dateFilter() {
        val calendar = Calendar.getInstance()
        monthOftheYear = (calendar.get(Calendar.MONTH) + 1).toString()
        Year = CommonUtil.getYear()
        binding!!.tvToday.setOnClickListener(this)
        binding!!.tvYesterday.setOnClickListener(this)
        binding!!.tvWeek.setOnClickListener(this)
        binding!!.tvCustom.setOnClickListener(this)
        binding!!.tvStartDate.setOnClickListener(this)
        binding!!.tvEndDate.setOnClickListener(this)
        binding!!.btnAppy.setOnClickListener(this)
        binding!!.info.setOnClickListener(this)
    }

    fun hitApi(){
        binding!!.progressLayout.progressLayout.visibility = View.VISIBLE
        val api = NewRetrofitClient.getInstance().create(NetworkService::class.java)
        val apiCall = api.getScoreCardReport(monthOftheYear,Year,CommonData.getCustIdFromDB(),"null","","1",startlimit,limit,binding!!.etSearchBar.text.toString(),"0","asc")
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
                binding!!.info.setOnClickListener {
                    // Show the dialog when required (e.g., in an onClickListener)
                    val dialogFragment = CustomDialogFragment.newInstance()
                    dialogFragment.show(supportFragmentManager, "custom_dialog")
                }
                if(list.size==totalRecords){
                    binding!!.loadMore.visibility = View.GONE
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
                if (t is SocketTimeoutException) {
                    Constants.alertDialog(
                        this@ScoreCardActivity,
                        this@ScoreCardActivity.getString(R.string.time_out)
                    )
                } else if (t is UnknownHostException) {
                    Constants.alertDialog(
                        this@ScoreCardActivity,
                        this@ScoreCardActivity.getString(R.string.no_network)
                    )
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    if (t is DnsResolver.DnsException) {
                        Constants.alertDialog(
                            this@ScoreCardActivity,
                            this@ScoreCardActivity.getString(R.string.dns_error)
                        )
                    }
                } else {
                    Constants.alertDialog(
                        this@ScoreCardActivity,
                        this@ScoreCardActivity.getString(R.string.something_went_wrong)
                    )
                }
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
            picker!!.maxDate = previous_year.time
        } catch (e: Exception) {
            // e.printStackTrace();
            picker = datePickerDialog.datePicker
        }
        datePickerDialog.show()
    }
}

