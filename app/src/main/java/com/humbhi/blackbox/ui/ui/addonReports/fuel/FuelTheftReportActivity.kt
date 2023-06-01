package com.humbhi.blackbox.ui.ui.addonReports.fuel

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityFuelTheftReportBinding
import com.humbhi.blackbox.ui.MyApplication
import com.humbhi.blackbox.ui.Utility.EndlessRecyclerOnScrollListener
import com.humbhi.blackbox.ui.adapters.FuelTheftAdapter
import com.humbhi.blackbox.ui.data.DataManagerImpl
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.models.FuelTheftReponseModel
import com.humbhi.blackbox.ui.data.models.Theftdata
import com.humbhi.blackbox.ui.data.network.RestClient
import com.humbhi.blackbox.ui.ui.addonReports.fuel.fueltheft.FuelTheftPresenter
import com.humbhi.blackbox.ui.ui.addonReports.fuel.fueltheft.FuelTheftPresenterImpl
import com.humbhi.blackbox.ui.ui.addonReports.fuel.fueltheft.FuelTheftView
import com.humbhi.blackbox.ui.utils.CommonUtil
import com.humbhi.blackbox.ui.utils.ExplicitIntentUtil
import java.util.*

class FuelTheftReportActivity : AppCompatActivity(), FuelTheftView ,View.OnClickListener{

    private lateinit var binding: ActivityFuelTheftReportBinding
    private lateinit var startDateParam: String
    private lateinit var endDateParam: String
    private lateinit var mPresenter: FuelTheftPresenter
    var picker: DatePicker? = null
    var startlimit = 0
    var limit = 20
    var list : ArrayList<Theftdata> = ArrayList()
    var totalRecords = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFuelTheftReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbarDetails()

        mPresenter = FuelTheftPresenterImpl(
            this,
            DataManagerImpl(RestClient.getRetrofitBuilderForTrackMaster())
        )
        startDateParam = CommonUtil.getCurrentDate().replace("-", "/")
        endDateParam = CommonUtil.getCurrentDate().replace("-", "/")
        binding.etSearchBar.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                startlimit = 0
                binding.loadMore.visibility = View.GONE
                list.clear()
                hitApi()
            }
            false
        }
        hitApi()
        dateFilter()
    }

    private fun hitApi() {
        mPresenter.hitFuelTheftReportApi(
            "$startDateParam%2012:00:00%20AM",
            "$endDateParam%2023:59:00%20PM",
            CommonData.getCustIdFromDB(),
            "",
            "",
            "",
            0,
            0,
            startlimit,
            limit,
            binding.etSearchBar.text.toString(),
            0,
            ""
        )
    }

    private fun setToolbarDetails() {
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.tvTitle.visibility = View.VISIBLE
        binding.toolbar.tvTitle.text = "Fuel Theft Report"
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun dateFilter(){
        startDateParam = CommonUtil.getCurrentDate().replace("-","/")
        endDateParam = CommonUtil.getCurrentDate()
        binding.tvToday.setOnClickListener(this)
        binding.tvYesterday.setOnClickListener(this)
        binding.tvWeek.setOnClickListener(this)
        binding.tvCustom.setOnClickListener(this)
        binding.tvStartDate.setOnClickListener(this)
        binding.tvEndDate.setOnClickListener(this)
        binding.btnAppy.setOnClickListener(this)
    }


    override fun getFuelTheftData(fuelTheftReponseModel: FuelTheftReponseModel) {
        totalRecords = fuelTheftReponseModel.iTotalRecords
        for(i in 0 until  fuelTheftReponseModel.aaData.size) {
            list.add(fuelTheftReponseModel.aaData[i])
        }
        binding.rvFuelTheft.layoutManager = LinearLayoutManager(this)
        val adapter = FuelTheftAdapter(object : FuelTheftAdapter.VehicleDetails {
            override fun onVehicleSelection(position: Int) {
//                val bundle = Bundle()
//                bundle.putParcelableArrayList(
//                    "fuelTheftDetail",
//                    ArrayList(list[position].FueltheftMainModel)
//                )
//
//                Log.e(
//                    "SendData-->",
//                    ArrayList(list[position].FueltheftMainModel).size.toString()
//                )
//
//                ExplicitIntentUtil.startActivity(
//                    this@FuelTheftReportActivity,
//                    FuelTheftDetail::class.java, bundle
//                )
                MyApplication.fuelTheftData = ArrayList(list[position].FueltheftMainModel)
                val intent = Intent(this@FuelTheftReportActivity,FuelTheftDetail::class.java)
                startActivity(intent)
            }
        }, list,this)
        binding.rvFuelTheft.adapter = adapter
        binding.rvFuelTheft.scrollToPosition(startlimit)
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
        binding.llCustomProgress.progressLayout.visibility = View.VISIBLE
        return true
    }

    override fun isHideLoading(): Boolean {
        binding.llCustomProgress.progressLayout.visibility = View.GONE
        return true
    }

    override fun showErrorMessage(string: String) {
        CommonUtil.alertDialogWithOkOnly(this, "Error", string)
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
                startlimit = 0
                list.clear()
                binding.llCustomDateRange.visibility = View.GONE
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