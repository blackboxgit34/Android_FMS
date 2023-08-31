package com.humbhi.blackbox.ui.ui.addonReports.temperature.tempDetails

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityTemperatureDetailReportBinding
import com.humbhi.blackbox.databinding.ActivityTemperatureSensorReportBinding
import com.humbhi.blackbox.ui.adapters.TempDetailAdapter
import com.humbhi.blackbox.ui.adapters.TemperatureReportAdapter
import com.humbhi.blackbox.ui.data.DataManagerImpl
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.models.TempDetailResponse
import com.humbhi.blackbox.ui.data.network.RestClient
import com.humbhi.blackbox.ui.ui.addonReports.temperature.TempSensorPresenter
import com.humbhi.blackbox.ui.ui.addonReports.temperature.TempSensorPresenterImpl
import com.humbhi.blackbox.ui.utils.CommonUtil
import com.humbhi.blackbox.ui.utils.Constants

class TemperatureDetailReport : AppCompatActivity(),TempDetailInterface {

    private lateinit var binding: ActivityTemperatureDetailReportBinding
    private lateinit var startDateParam: String
    private lateinit var endDateParam: String
    private lateinit var mPresenter: TempDetailPresenter
    private lateinit var bbID: String
    private lateinit var vehicleName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTemperatureDetailReportBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getIntentData()
        setToolbarDetails()
        mPresenter = TempDetailPresenterImpl(
            this,
            DataManagerImpl(RestClient.getRetrofitBuilderForTrackMaster())
        )
        startDateParam = CommonUtil.getCurrentDate().replace("-", "/")
        endDateParam = CommonUtil.getCurrentDate().replace("-", "/")
        hitApi()
        binding.etSearchBar.setOnEditorActionListener(TextView.OnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                hitApi()
            }
            false
        })
    }

    private fun hitApi() {

        mPresenter.hitTemperatureDetailReportApi(
            "$startDateParam%2012:00:00%20AM",
            "$endDateParam%2023:59:00%20PM",
            bbID,
            CommonData.getCustIdFromDB(),
            1,
            0,
            "",
            "",
            "0",
            900,
            binding.etSearchBar.text.toString(),
            0,
            0,"asc"
        )
    }

    private fun getIntentData(){
        if (intent.hasExtra("bbid")){
            val bundle :Bundle?= intent.extras
            bbID = bundle?.getString("bbid").toString()
            vehicleName = bundle?.getString("vehicleName").toString()
        }
    }

    private fun setToolbarDetails() {
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.tvTitle.visibility = View.VISIBLE
        binding.toolbar.tvTitle.text = vehicleName
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }

    override fun getTempReportDetailData(tempDetailResponse: TempDetailResponse) {
        binding.rvTemperature.layoutManager = LinearLayoutManager(this)
        val adapter = TempDetailAdapter(tempDetailResponse.aaData)
        binding.rvTemperature.adapter = adapter
    }

    override fun isNetworkConnected(): Boolean {
        if(com.humbhi.blackbox.ui.utils.Network.isNetworkAvailable(this)) {
            return true
        }
        return false
    }

    override fun isShowLoading(): Boolean {
        binding.llCustomProgress.progressLayout.visibility = View.VISIBLE
        binding.llMainLayout.visibility = View.GONE
        return true
    }

    override fun isHideLoading(): Boolean {
        binding.llCustomProgress.progressLayout.visibility = View.GONE
        binding.llMainLayout.visibility = View.VISIBLE
        return true
    }

    override fun showErrorMessage(string: String) {
        CommonUtil.alertDialogWithOkOnly(this, "Error", string)
    }
}