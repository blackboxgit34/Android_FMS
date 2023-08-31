package com.humbhi.blackbox.ui.ui.drivingBehaviour.DriverMonthlyComparison

import CustomDatePickerFragment
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.os.Parcel
import android.os.Parcelable
import android.view.View
import android.widget.DatePicker
import android.widget.Toast
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
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.MaterialDatePicker
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityDriverMonthlyComparisonBinding
import com.humbhi.blackbox.ui.Utility.areDatesValidAndFirstIsLess
import com.humbhi.blackbox.ui.adapters.SearchableAdapter
import com.humbhi.blackbox.ui.data.DataManagerImpl
import com.humbhi.blackbox.ui.data.db.CommonData.getCustIdFromDB
import com.humbhi.blackbox.ui.data.models.AllDriverModel
import com.humbhi.blackbox.ui.data.models.Data
import com.humbhi.blackbox.ui.data.models.DriverMonthlyResponse
import com.humbhi.blackbox.ui.data.network.RestClient
import com.humbhi.blackbox.ui.retofit.Retrofit2
import com.humbhi.blackbox.ui.retofit.RetrofitResponse
import com.humbhi.blackbox.ui.utils.CommonUtil
import com.humbhi.blackbox.ui.utils.Constants
import com.kal.rackmonthpicker.RackMonthPicker
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import retrofit2.Response
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


class DriverMonthlyComparison : AppCompatActivity(), RetrofitResponse, View.OnClickListener, DriverMonthlyComparisonView, CustomDatePickerFragment.OnDateSelectedListener {
    private lateinit var binding: ActivityDriverMonthlyComparisonBinding
    var vehicleModel = ArrayList<AllDriverModel>()
    var vehicleList = ArrayList<String>()
    var driverId = ""
    var driverName = ""
    var backendStartDate = ""
    var backendEndDate = ""
    var picker: DatePicker? = null
    var DistanceChart: BarChart? = null
    var beginMonth = ""
    var endMonth = ""
    var flag = ""
    var list: ArrayList<Data> = ArrayList()
    private lateinit var mPresenter: DriverMonthlyComparisonImpl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_driver_monthly_comparison)
        binding = ActivityDriverMonthlyComparisonBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mPresenter = DriverMonthlyComparisonImpl(
            this,
            DataManagerImpl(RestClient.getRetrofitBuilderForTrackMaster())
        )
        setToolbar()
        dateFilter()
        getAllVehicles()
        binding.btnGetComparison.setOnClickListener {
            binding.llCustomDateRange.visibility = View.GONE
            getMonthlyData()
        }
    }

    private fun getAllVehicles() {
        Retrofit2(
            this,
            this,
            Constants.REQ_DRIVERS_LIST,
            Constants.DRIVERS_LIST + "custid=" + getCustIdFromDB()
        ).callService(true)
    }

    private fun spinVehicles() {
        //Getting the instance of AutoCompleteTextView
        binding.spVehicles.threshold = 0 //will start working from first character
        val adapter = SearchableAdapter(this, vehicleList)
        binding.spVehicles.setAdapter(adapter) //setting the adapter data into the AutoCompleteTextView

        binding.spVehicles.setOnItemClickListener { parent, view, position, id ->
            val originalData = adapter.originalData
            val selection = originalData[position]
            val originalPosition = adapter.getOriginalPosition(position)
            if (originalPosition != -1) {
                // Use the original position to retrieve the corresponding item
                driverId = vehicleModel[originalPosition].value
                driverName = vehicleModel[originalPosition].text
            }
        }
        binding.spVehicles.setOnFocusChangeListener { v, hasFocus -> if (hasFocus) binding.spVehicles.showDropDown() }

        binding.spVehicles.setOnTouchListener { v, event ->
            binding.spVehicles.showDropDown()
            false
        }
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
        binding.tvEndDate.setOnClickListener(this)
        binding.btnAppy.setOnClickListener(this)
    }

    override fun onServiceResponse(requestCode: Int, response: Response<ResponseBody>?) {
        when (requestCode) {
            Constants.REQ_DRIVERS_LIST ->
                if (response!!.isSuccessful) {
                    try {
                        vehicleList.clear()
                        // vehicleList.add(0,"Select Vehicle");
                        val data = JSONArray(response.body()!!.string())
                        for (i in 0 until data.length()) {
                            val obj = data.getJSONObject(i)
                            val model = AllDriverModel()
                            model.value = obj.getString("Value")
                            model.text = obj.getString("Text")
                            vehicleList.add(obj.getString("Text"))
                            vehicleModel.add(model)
                            spinVehicles()
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
        }
    }

    private fun getMonthlyData() {
        if (driverId != "") {
            binding.progressLayout.progressLayout.visibility = View.VISIBLE
            if (driverId.contains("I") || driverId.contains("J") || driverId.contains("C") || driverId.contains("E")) {
                    mPresenter.getMonthlyComparison(
                        getCustIdFromDB(),
                        "$beginMonth%2012:00%20AM", endMonth + "%2011:59%20PM",
                        "0",
                        driverId
                    )
            } else {
                    mPresenter.getMonthlyComparison(
                        getCustIdFromDB(),
                        "$beginMonth%2012:00%20AM", endMonth + "%2011:59%20PM",
                         driverId,
                        ""
                    )
            }
        } else {
            Constants.alertDialog(this, "Please select driver.")
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tvCustom -> {
                if (binding.llCustomDateRange.visibility == View.VISIBLE) {
                    binding.llCustomDateRange.visibility = View.GONE
                    binding.btnGetComparison.visibility = View.VISIBLE
                } else {
                    binding.tvCustom.background =
                        ContextCompat.getDrawable(this, R.drawable.black_cyrve_rect);
                    binding.llCustomDateRange.visibility = View.VISIBLE
                    binding.btnGetComparison.visibility = View.GONE
                }
            }

            R.id.tvStartDate -> {
                flag = "1"
                showDatePicker()
            }

            R.id.tvEndDate -> {
                flag = "2"
                showDatePicker()
            }

            R.id.btnAppy -> {
                binding.llCustomDateRange.visibility = View.GONE
                if (areDatesValidAndFirstIsLess(
                        "$beginMonth%2012:00%20AM",
                        endMonth + "%2011:59%20PM"
                    )
                ) {
                    getMonthlyData()
                }
                else{
                    Constants.alertDialog(this,"Start date must less than the end date.")
                }
            }
        }
    }
    private fun showDatePicker() {
        val datePickerFragment = CustomDatePickerFragment()
        datePickerFragment.setOnDateSelectedListener(this)
        datePickerFragment.show(supportFragmentManager, "datePicker")
    }

    private fun setMonthlyComparisonChart(list: ArrayList<Data>) {
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
        val months : ArrayList<String> = ArrayList()
        for(i in 0 until list.size) {
                months.add(list[i].Monthname)
        }
        xAxis.setDrawGridLines(false)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f
        xAxis.setDrawLabels(true)
        xAxis.setDrawAxisLine(true)
        xAxis.valueFormatter = IndexAxisValueFormatter(months)
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
//        val customRenderer = BarChartCustomRenderer(
//            DistanceChart, DistanceChart!!.animator, DistanceChart!!.viewPortHandler
//        )
        // DistanceChart!!.renderer = customRenderer
    }


    private fun setData(list: ArrayList<Data>): BarDataSet? {
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

    override fun getMonthlyComparison(drivingMonthlyComparison: DriverMonthlyResponse) {
        list = drivingMonthlyComparison.data as ArrayList<Data>
        setMonthlyComparisonChart(list)
        binding.compChart.visibility = View.VISIBLE
        binding.btnGetComparison.visibility = View.VISIBLE
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

    override fun onDateSelected(year: Int, month: Int) {
        list.clear()
        if (flag.equals("1")) {
            beginMonth = "$month/01/$year"
            binding.tvStartDate.text = "$month/$year"
        }
        else{
            endMonth = "$month/01/$year"
            binding.tvEndDate.text = "$month/$year"
        }
    }

}