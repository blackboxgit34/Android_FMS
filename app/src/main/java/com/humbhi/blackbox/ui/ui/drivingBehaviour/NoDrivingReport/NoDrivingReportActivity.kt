package com.humbhi.blackbox.ui.ui.drivingBehaviour.NoDrivingReport

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityDrivingLimitBinding
import com.humbhi.blackbox.databinding.ActivityNoDrivingReportBinding
import com.humbhi.blackbox.ui.adapters.DrivingLimitAdapter
import com.humbhi.blackbox.ui.adapters.NoDrivingAdapter
import com.humbhi.blackbox.ui.data.DataManagerImpl
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.models.AaDataX
import com.humbhi.blackbox.ui.data.models.AaDataXX
import com.humbhi.blackbox.ui.data.models.DrivingLimitModel
import com.humbhi.blackbox.ui.data.models.NoDrivingModel
import com.humbhi.blackbox.ui.data.network.RestClient
import com.humbhi.blackbox.ui.ui.drivingBehaviour.DrivingLimit.DrivingLimitPresenterImpl
import com.humbhi.blackbox.ui.ui.drivingBehaviour.DrivingLimit.DrivingLimitView
import com.humbhi.blackbox.ui.utils.CommonUtil
import java.util.*


class NoDrivingReportActivity : AppCompatActivity(), NoDrivingView, View.OnClickListener {
    private lateinit var binding: ActivityNoDrivingReportBinding
    private lateinit var BeginDate: String
    private lateinit var EndDate: String
    var picker: DatePicker? = null
    private lateinit var mPresenter: NoDrivingPresenterIml
    var totalRecords = 0
    var startlimit = 0
    var limit = 20
    var list : ArrayList<AaDataXX> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNoDrivingReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mPresenter = NoDrivingPresenterIml(
            this,
            DataManagerImpl(RestClient.getRetrofitBuilderForTrackMaster())
        )
        setToolbar()
        dateFilter()
        getNoDrivingLimitData()
    }

    private fun setToolbar() {
        binding.toolbar.tvTitle.text = "No Driving Hours"
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivSort.visibility = View.GONE
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun dateFilter() {
        BeginDate = CommonUtil.getCurrentDate()
        EndDate = CommonUtil.getCurrentDate()
        binding.tvToday.setOnClickListener(this)
        binding.tvYesterday.setOnClickListener(this)
        binding.tvWeek.setOnClickListener(this)
        binding.tvCustom.setOnClickListener(this)
        binding.tvStartDate.setOnClickListener(this)
        binding.tvEndDate.setOnClickListener(this)
        binding.btnAppy.setOnClickListener(this)
    }

    private fun getNoDrivingLimitData(){
        binding.progressLayout.progressLayout.visibility = View.VISIBLE
        mPresenter.getNoDrivingLimitData("$BeginDate%2012:00%20AM", EndDate+"%2011:59%20PM",
            CommonData.getCustIdFromDB(),"null","","1",startlimit,limit,"","0","asc")
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tvToday ->{
                BeginDate = CommonUtil.getCurrentDate()
                EndDate = CommonUtil.getCurrentDate()
                binding.tvYesterday.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                binding.tvToday.setBackground(ContextCompat.getDrawable(this, R.drawable.black_cyrve_rect));
                binding.tvWeek.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                binding.tvCustom.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                binding.llCustomDateRange.visibility = View.GONE
                startlimit = 0
                list.clear()
                getNoDrivingLimitData()
            }
            R.id.tvYesterday ->{
                BeginDate = CommonUtil.getYesterdayDate()
                EndDate = BeginDate
                binding.tvYesterday.setBackground(ContextCompat.getDrawable(this, R.drawable.black_cyrve_rect));
                binding.tvToday.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                binding.tvWeek.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                binding.tvCustom.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                binding.llCustomDateRange.visibility = View.GONE
                startlimit = 0
                list.clear()
                getNoDrivingLimitData()
            }
            R.id.tvWeek ->{
                BeginDate = CommonUtil.getCurrentDate()
                EndDate = CommonUtil.getWeekDate()
                binding.tvYesterday.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                binding.tvWeek.setBackground(ContextCompat.getDrawable(this, R.drawable.black_cyrve_rect));
                binding.tvToday.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                binding.tvCustom.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                binding.llCustomDateRange.visibility = View.GONE
                startlimit = 0
                list.clear()
                getNoDrivingLimitData()
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
                getNoDrivingLimitData()
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
                    BeginDate = "$year-$x-$y"
                    binding.tvStartDate.setText("$y-$x-$year")
                }
                if (flag == "2") {
                    EndDate = "$year-$x-$y"
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


    override fun getNoDrivingLimitData(noDrivingResponseModel: NoDrivingModel) {
        val layoutManager = LinearLayoutManager(this)
        totalRecords = noDrivingResponseModel.iTotalRecords
        for(i in 0 until  noDrivingResponseModel.aaData.size) {
            list.add(noDrivingResponseModel.aaData[i])
        }
        binding.rvRecycler.layoutManager = layoutManager
        val adapter = NoDrivingAdapter(this@NoDrivingReportActivity,list)
        binding.rvRecycler.adapter = adapter
        binding.rvRecycler.scrollToPosition(startlimit)
        if(totalRecords>20){
            binding.loadMore.visibility = View.VISIBLE
        }
        binding.loadMore.setOnClickListener {
            if(list.size<totalRecords) {
                startlimit += 20
                getNoDrivingLimitData()
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

}