package com.humbhi.blackbox.ui.ui.drivingBehaviour.DriverToDriverComparison

import CustomDatePickerFragment
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityDriverToDriverComparisonBinding
import com.humbhi.blackbox.ui.adapters.SearchableAdapter
import com.humbhi.blackbox.ui.data.DataManagerImpl
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.models.AllDriverModel
import com.humbhi.blackbox.ui.data.models.DataX
import com.humbhi.blackbox.ui.data.models.DriverToDriverCompModel
import com.humbhi.blackbox.ui.data.network.RestClient
import com.humbhi.blackbox.ui.retofit.Retrofit2
import com.humbhi.blackbox.ui.retofit.RetrofitResponse
import com.humbhi.blackbox.ui.utils.CommonUtil
import com.humbhi.blackbox.ui.utils.Constants
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import retrofit2.Response
import java.io.IOException
import java.util.Calendar
import java.util.Locale

class DriverToDriverComparisonActivity : AppCompatActivity(), View.OnClickListener, DrToDrComparisonView, RetrofitResponse, CustomDatePickerFragment.OnDateSelectedListener {
    private lateinit var binding: ActivityDriverToDriverComparisonBinding
    var beginMonth = ""
    var endMonth = ""
    var picker: DatePicker? = null
    var DistanceChart: BarChart? = null
    var driverId1 = ""
    var driverName1 = ""
    var driverId2 = ""
    var driverName2 = ""
    var vehicleModel = ArrayList<AllDriverModel>()
    var vehicleList = ArrayList<String>()
    var list : ArrayList<DataX> = ArrayList<DataX>()
    var bbid1 = ""
    var bbid2 = ""
    var flag = ""

    private lateinit var mPresenter: DrToDrCompPresenterImpl
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDriverToDriverComparisonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mPresenter = DrToDrCompPresenterImpl(
            this,
            DataManagerImpl(RestClient.getRetrofitBuilderForTrackMaster())
        )
        setToolbar()
        dateFilter()
        getAllVehicles()
        binding.btnGetComparison.setOnClickListener {
            binding.llCustomDateRange.visibility = View.GONE
            getData()
        }
    }

    private fun getAllVehicles() {
        Retrofit2(
            this,
            this,
            Constants.REQ_DRIVERS_LIST,
            Constants.DRIVERS_LIST + "custid=" + CommonData.getCustIdFromDB()
        )
            .callService(true)
    }

    private fun setToolbar() {
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.tvTitle.visibility = View.VISIBLE
        binding.toolbar.tvTitle.text = "Driver's Monthly Comparison"
        binding.toolbar.ivBack.setOnClickListener(View.OnClickListener { finish() })
    }

    private fun dateFilter() {
        endMonth = CommonUtil.firstDayOfMonth()
        beginMonth = CommonUtil.firstDayOfLastMonth()
        binding.tvCustom.setOnClickListener(this)
        binding.tvStartDate.setOnClickListener(this)
        binding.btnAppy.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvCustom ->{
                if( binding.llCustomDateRange.visibility == View.VISIBLE){
                    binding.llCustomDateRange.visibility = View.GONE
                    binding.btnGetComparison.visibility = View.VISIBLE
                }
                else{
                    binding.tvCustom.background = ContextCompat.getDrawable(this, R.drawable.black_cyrve_rect);
                    binding.llCustomDateRange.visibility = View.VISIBLE
                    binding.btnGetComparison.visibility = View.GONE
                }
            }
            R.id.tvStartDate ->{
                showDatePicker()
            }
            R.id.tvEndDate ->{
                datepicker("2")
            }
            R.id.btnAppy ->{
                binding.llCustomDateRange.visibility = View.GONE
                getData()
            }
        }
    }

    private fun getData(){
        if (driverId1 != "" && driverId2 != "") {
            binding.progressLayout.progressLayout.visibility = View.VISIBLE
            if (driverId1.contains("I") || driverId1.contains("J") || driverId1.contains("C") || driverId1.contains("E")) {
                bbid1 = driverId1
                driverId1 = "0"
            }
            if (driverId2.contains("I") || driverId2.contains("J") || driverId2.contains("C") || driverId2.contains("E")) {
                bbid2 = driverId2
                driverId2 = "0"
            }
            mPresenter.apiCallDriverToDriverComparisonReport(
                CommonData.getCustIdFromDB(),
                "$beginMonth%2012:00%20AM",
                driverId1,
                driverId2,
                bbid1,
                bbid2,
                driverName1,
                driverName2
            )
        }
        else{
            Constants.alertDialog(this,"Please select both drivers.")
        }
    }

    private fun datepicker(flag: String) {
//        RackMonthPicker(this)
//            .setLocale(Locale.ENGLISH)
//            .setPositiveButton { month, startDate, endDate, year, monthLabel ->
//                beginMonth = "$month/$startDate/$year"
//                binding.tvStartDate.text = monthLabel.toString()
//                list.clear()
//            }
//            .setNegativeButton {
//                it.dismiss()
//            }
//            .show()
    }

    private fun setMonthlyComparisonChart(list: ArrayList<DataX>) {
        DistanceChart = binding.compChart
        val desc: Description
        val L: Legend = DistanceChart!!.legend
        L.verticalAlignment = Legend.LegendVerticalAlignment.BOTTOM
        L.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        L.textColor = ContextCompat.getColor(DistanceChart!!.context, R.color.white)
        L.textSize = 10f
        desc = DistanceChart!!.description
        desc.text = "" // this is the weirdest way to clear something!!
        desc.textSize = 10f
        desc.textColor = ContextCompat.getColor(DistanceChart!!.context, R.color.white)
        L.isEnabled = true
        val leftAxis = DistanceChart!!.axisLeft
        val rightAxis = DistanceChart!!.axisRight
        val xAxis = DistanceChart!!.xAxis
        val names : ArrayList<String> = ArrayList()
        for(i in 0 until list.size) {
            names.add(list[i].DrName)
        }
        xAxis.setDrawGridLines(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setDrawLabels(true)
        xAxis.setDrawAxisLine(true)
        xAxis.valueFormatter = IndexAxisValueFormatter(names)
        xAxis.textSize = 12f
        xAxis.textColor =  ContextCompat.getColor(DistanceChart!!.context, R.color.white)
        leftAxis.textSize = 10f
        leftAxis.setDrawLabels(false)
        leftAxis.setDrawAxisLine(true)
        leftAxis.setDrawGridLines(false)
        rightAxis.setDrawAxisLine(false)
        rightAxis.setDrawGridLines(false)
        rightAxis.setDrawLabels(false)
        val data = BarData(setData(list))
        DistanceChart!!.setVisibleXRange(0.75f,list.size.toFloat())
        DistanceChart!!.data = data
        DistanceChart!!.setFitBars(true) // make the x-axis fit exactly all bars
        DistanceChart!!.invalidate() // refresh
        DistanceChart!!.setScaleEnabled(true)
        DistanceChart!!.isDoubleTapToZoomEnabled = false
        //DistanceChart.setBackgroundResource(R.drawable.round_bars);
        DistanceChart!!.setBackgroundColor(resources.getColor(R.color.black))
        DistanceChart!!.animateXY(2000, 2000)
        DistanceChart!!.setDrawBorders(false)
        DistanceChart!!.description = desc
        data.setValueTextColor(Color.rgb(255, 255, 255))
        DistanceChart!!.setDrawValueAboveBar(true)
    }


    private fun setData(list: ArrayList<DataX>): BarDataSet? {
        val entries = ArrayList<BarEntry>()
        for(i in 0 until list.size) {
            entries.add(
                BarEntry(
                    i.toFloat(),
                    floatArrayOf(
                        list[i].HA.toFloat(),
                        list[i].HB.toFloat(),
                        list[i].RT.toFloat(),
                        list[i].OS.toFloat()
                    )
                )
            )
        }
        val set = BarDataSet(entries, "")
        set.setColors(
            ContextCompat.getColor(DistanceChart!!.context, R.color.blue),
            ContextCompat.getColor(DistanceChart!!.context, R.color.blackprimary_fade),
            ContextCompat.getColor(DistanceChart!!.context, R.color.green_dark),
            ContextCompat.getColor(DistanceChart!!.context, R.color.primary_dark_orange)
        )
        set.stackLabels = arrayOf("Harsh Acceleration","Harsh Brake","Rash Turn","Over Speed")
        set.valueTextColor = Color.rgb(255, 255, 255)
        set.barBorderColor = ContextCompat.getColor(DistanceChart!!.context, R.color.white)
        set.barBorderWidth = 1f
        set.valueTextSize = 12f
        return set
    }
    private fun showDatePicker() {
        val datePickerFragment = CustomDatePickerFragment()
        datePickerFragment.setOnDateSelectedListener(this)
        datePickerFragment.show(supportFragmentManager, "datePicker")
    }

    override fun getDrivingLimitData(driverToDriverCompModel: DriverToDriverCompModel) {
        list = driverToDriverCompModel.data as ArrayList<DataX>
        setMonthlyComparisonChart(list)
        binding.compChart.visibility = View.VISIBLE
        binding.btnGetComparison.visibility = View.VISIBLE
    }

    override fun isNetworkConnected(): Boolean {
        if(com.humbhi.blackbox.ui.utils.Network.isNetworkAvailable(this)) {
            return true
        }
        return false
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

    fun spinVehicles1() {
        //Getting the instance of AutoCompleteTextView
        binding.spDriver1.threshold = 1 //will start working from first character

        val adapter = SearchableAdapter(this, vehicleList)

        binding.spDriver1.setAdapter(adapter) //setting the adapter data into the AutoCompleteTextView

        binding.spDriver1.setOnItemClickListener { parent, view, position, id ->
            val originalData: ArrayList<String> = adapter.originalData
            val selection = originalData[position]
            val originalPosition: Int = adapter.getOriginalPosition(position)
            if (originalPosition != -1) {
                // Use the original position to retrieve the corresponding item
                driverId1 = vehicleModel[originalPosition].value
                driverName1 = vehicleModel[originalPosition].text
            }
        }
        binding.spDriver1.setOnFocusChangeListener { v, hasFocus -> if (hasFocus) binding.spDriver1.showDropDown() }

        binding.spDriver1.setOnTouchListener { v, event ->
            binding.spDriver1.showDropDown()
            false
        }
    }

    fun spinVehicles2() {
        //Getting the instance of AutoCompleteTextView
        binding.spDriver2.threshold = 1 //will start working from first character

        val adapter = SearchableAdapter(this, vehicleList)

        binding.spDriver2.setAdapter(SearchableAdapter(this, vehicleList)) //setting the adapter data into the AutoCompleteTextView

        binding.spDriver2.setOnItemClickListener { parent, view, position, id ->
            val originalData: ArrayList<String> = adapter.originalData
            val selection = originalData[position]
            val originalPosition: Int = adapter.getOriginalPosition(position)
            if (originalPosition != -1) {
                // Use the original position to retrieve the corresponding item
                driverId2 = vehicleModel[originalPosition].value
                driverName2 = vehicleModel[originalPosition].text
            }
        }
        binding.spDriver2.setOnFocusChangeListener { v, hasFocus -> if (hasFocus) binding.spDriver2.showDropDown() }

        binding.spDriver2.setOnTouchListener { v, event ->
            binding.spDriver2.showDropDown()
            false
        }
    }

    override fun onServiceResponse(requestCode: Int, response: Response<ResponseBody>?) {
        when (requestCode) {
            Constants.REQ_DRIVERS_LIST ->
                if (response!!.isSuccessful) {
                    try {
                        vehicleList.clear()
                        // vehicleList.add(0,"Select Vehicle");
                        val data = JSONArray(response.body()!!.string())
                        var i = 0
                        while (i < data.length()) {
                            val obj = data.getJSONObject(i)
                            val model = AllDriverModel()
                            model.value = obj.getString("Value")
                            model.text = obj.getString("Text")
                            vehicleList.add(obj.getString("Text"))
                            vehicleModel.add(model)
                            spinVehicles1()
                            spinVehicles2()
                            i++
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
        }
    }

    override fun onDateSelected(year: Int, month: Int) {
        list.clear()
        beginMonth =  "$month/01/$year"
        binding.tvStartDate.text = "$month/$year"
    }
}