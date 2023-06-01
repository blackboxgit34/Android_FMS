package com.humbhi.blackbox.ui.ui.addonReports.temperature

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.humbhi.blackbox.databinding.ActivityTemperatureSensorReportBinding
import com.humbhi.blackbox.ui.Utility.EndlessRecyclerOnScrollListener
import com.humbhi.blackbox.ui.adapters.TemperatureReportAdapter
import com.humbhi.blackbox.ui.data.DataManagerImpl
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.models.TemperatureReportResponse
import com.humbhi.blackbox.ui.data.models.TempratureData
import com.humbhi.blackbox.ui.data.models.WorkingHourData
import com.humbhi.blackbox.ui.data.network.RestClient
import com.humbhi.blackbox.ui.ui.addonReports.fuel.FuelTheftDetail
import com.humbhi.blackbox.ui.ui.addonReports.fuel.fuelFilling.FuelFillingPresenterImpl
import com.humbhi.blackbox.ui.ui.addonReports.temperature.tempDetails.TemperatureDetailReport
import com.humbhi.blackbox.ui.utils.CommonUtil
import com.humbhi.blackbox.ui.utils.ExplicitIntentUtil

class TemperatureSensorReport : AppCompatActivity(), TempSensorView {
    private lateinit var binding: ActivityTemperatureSensorReportBinding
    private lateinit var startDateParam: String
    private lateinit var endDateParam: String
    private lateinit var mPresenter: TempSensorPresenter
    var totalRecords = 0
    var startlimit = 0
    var limit = 20
    var list : java.util.ArrayList<TempratureData> = java.util.ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTemperatureSensorReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mPresenter = TempSensorPresenterImpl(
            this,
            DataManagerImpl(RestClient.getRetrofitBuilderForTrackMaster())
        )
        startDateParam = CommonUtil.getCurrentDate().replace("-", "/")
        endDateParam = CommonUtil.getCurrentDate().replace("-", "/")
        setToolbarDetails()
        hitApi()
        binding.etSearchBar.setOnEditorActionListener(TextView.OnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                startlimit = 0
                binding.loadMore.visibility = View.GONE
                list.clear()
                hitApi()
            }
            false
        })
    }

    private fun hitApi() {
        mPresenter.hitTemperatureReportApi(
            "$startDateParam%2012:00:00%20AM",
            "$endDateParam%2023:59:00%20PM",
            "",
            CommonData.getCustIdFromDB(),
            1,
            startlimit,
            "",
            "",
            "A",
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
        binding.toolbar.tvTitle.text = "Temperature Report"
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }

    override fun getTempReportData(temperatureReportResponse: TemperatureReportResponse) {
        binding.rvTemperature.layoutManager = LinearLayoutManager(this)
        for(i in 0 until  temperatureReportResponse.aaData.size) {
            list.add(temperatureReportResponse.aaData[i])
        }
        totalRecords = temperatureReportResponse.iTotalRecords
        val adapter =
            TemperatureReportAdapter(object : TemperatureReportAdapter.TemperatureDetails {
                override fun onVehicleSelection(position: Int) {
//                    val bundle = Bundle()
//                    bundle.putString("bbid",list[position].bbid)
//                    bundle.putString("vehicleName",list[position].VehicleName)
//                    ExplicitIntentUtil.startActivity(
//                        this@TemperatureSensorReport,
//                        TemperatureDetailReport::class.java, bundle
//                    )
                }
            }, list)
        binding.rvTemperature.adapter = adapter
        binding.rvTemperature.scrollToPosition(startlimit)
        if(totalRecords>20){
            binding.loadMore.visibility = View.VISIBLE
        }
        binding.loadMore.setOnClickListener {
            if(list.size<totalRecords) {
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
}