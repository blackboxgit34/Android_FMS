package com.humbhi.blackbox.ui.ui.addonReports.fuel.fuelRodDisconnection

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.humbhi.blackbox.databinding.ActivityFuelRodDisconnectionBinding
import com.humbhi.blackbox.ui.adapters.FuelRodDisconnectionAdapter
import com.humbhi.blackbox.ui.data.DataManagerImpl
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.models.DisconnectionData
import com.humbhi.blackbox.ui.data.models.FuelFillingData
import com.humbhi.blackbox.ui.data.models.FuelRodDisconnectionResponseModel
import com.humbhi.blackbox.ui.data.network.RestClient
import com.humbhi.blackbox.ui.ui.addonReports.fuel.FuelReportActivity
import com.humbhi.blackbox.ui.utils.CommonUtil
import java.util.ArrayList

class FuelRodDisconnectionActivity : AppCompatActivity() ,FuelRodView{
    private lateinit var binding: ActivityFuelRodDisconnectionBinding
    private lateinit var mPresenter: FuelRodPresenter
    private lateinit var startDateParam: String
    private lateinit var endDateParam: String
    var startLimit = 0
    var limit = 20
    var list : ArrayList<DisconnectionData> = ArrayList()
    var totalRecords = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFuelRodDisconnectionBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbarDetails()
        startDateParam = CommonUtil.getCurrentDate().replace("-", "/")
        endDateParam = CommonUtil.getCurrentDate().replace("-", "/")
        mPresenter = FuelRodPresenterImpl(
            this,
            DataManagerImpl(RestClient.getRetrofitBuilderForTrackMaster())
        )

        binding.etSearchBar.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                startLimit = 0
                binding.loadMore.visibility = View.GONE
                list.clear()
                hitApi()
            }
            false
        }
        hitApi()
    }

    private fun hitApi() {
        mPresenter.hitFuelRodDisconnectionApi(
            "$startDateParam%2012:00:00%20AM",
            "$endDateParam%2023:59:00%20PM",
            CommonData.getCustIdFromDB(),
            "",
            "",
            "",
            0,
            startLimit,
            limit,
            binding.etSearchBar.text.toString(),
            0,
            0
        )
    }
    private fun setToolbarDetails(){
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.tvTitle.visibility = View.VISIBLE
        binding.toolbar.tvTitle.text = "Fuel Rod Disconnections"
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }

    override fun getFuelRodDisconnectionData(fuelRodDisconnectionResponseModel: FuelRodDisconnectionResponseModel) {
        totalRecords = fuelRodDisconnectionResponseModel.iTotalRecords
        binding.rvFuelDisconnections.layoutManager = LinearLayoutManager(this)
        for(i in 0 until  fuelRodDisconnectionResponseModel.aaData.size) {
            list.add(fuelRodDisconnectionResponseModel.aaData[i])
        }
        val adapter = FuelRodDisconnectionAdapter(this,list)

        binding.rvFuelDisconnections.adapter = adapter
        binding.rvFuelDisconnections.scrollToPosition(startLimit)
        if(totalRecords>20){
            binding.loadMore.visibility = View.VISIBLE
        }
        binding.loadMore.setOnClickListener {
            if(list.size<totalRecords) {
                startLimit += 20
                hitApi()
            }
        }
    }

    override fun isNetworkConnected(): Boolean {
        return true
    }

    override fun isShowLoading(): Boolean {
        binding.llCustomProgress.progressLayout.visibility = View.VISIBLE
        binding.rvFuelDisconnections.visibility = View.GONE
        return true
    }

    override fun isHideLoading(): Boolean {
        binding.llCustomProgress.progressLayout.visibility = View.GONE
        binding.rvFuelDisconnections.visibility = View.VISIBLE
        return true
    }

    override fun showErrorMessage(string: String) {
        CommonUtil.alertDialogWithOkOnly(this, "Error", string)
    }
}