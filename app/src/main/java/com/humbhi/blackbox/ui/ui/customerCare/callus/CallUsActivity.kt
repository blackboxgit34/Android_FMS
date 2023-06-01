package com.humbhi.blackbox.ui.ui.customerCare.callus

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityCallUsBinding
import com.humbhi.blackbox.ui.data.DataManagerImpl
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.models.CustomerCareDataModel
import com.humbhi.blackbox.ui.data.network.RestClient
import com.humbhi.blackbox.ui.ui.reports.dailyreport.DailyReportPresenter
import com.humbhi.blackbox.ui.ui.reports.dailyreport.DailyReportPresenterImpl
import com.humbhi.blackbox.ui.utils.CommonUtil


class CallUsActivity : AppCompatActivity(),CallUsView,View.OnClickListener {
    private lateinit var binding:ActivityCallUsBinding
    private lateinit var presenter: CallUsPresenter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCallUsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbarDetails()
        callClicks()
        presenter = CallUsPresenterImpl(this,
            DataManagerImpl(RestClient.getRetrofitBuilderForTrackMaster())
        )
        presenter.hitCustomerCareComplaintApi(CommonData.getCustIdFromDB())
    }


    private fun setToolbarDetails(){
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.tvTitle.visibility = View.VISIBLE
        binding.toolbar.tvTitle.text = "Call Us"
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
    }

    private fun callClicks(){
        binding.tvCallSales.setOnClickListener(this)
        binding.tvCallAccount.setOnClickListener(this)
        binding.tvCallCust.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.tvCallSales ->{
                calling(binding.tvSalesPersonContact.text.toString())
            }

            R.id.tvCallAccount ->{
                calling(binding.tvAccPersonContact.text.toString())
            }

            R.id.tvCallCust ->{
                calling(binding.tvCustContact.text.toString())
            }
        }
    }

    private fun calling( phNumber:String){
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:$phNumber")
        startActivity(intent)
    }

    override fun getCustomerCareNumberResponse(customerCareDataModel: CustomerCareDataModel) {
        binding.tvSalesPersonName.text=customerCareDataModel.Marketname
        binding.tvSalesPersonContact.text=customerCareDataModel.Marketing
        binding.tvAccPersonName.text=customerCareDataModel.Collectioname
        binding.tvAccPersonContact.text=customerCareDataModel.Collection
        binding.tvCustName.text=customerCareDataModel.Servicename
        binding.tvCustContact.text=customerCareDataModel.Service
    }

    override fun isNetworkConnected(): Boolean {
        CommonUtil.isNetworkAvailable(this)
        return true
    }

    override fun isShowLoading(): Boolean {
        binding.llMainLayout.visibility = View.GONE
        binding.llProgressLayout.progressLayout.visibility = View.VISIBLE
        return true
    }

    override fun isHideLoading(): Boolean {
        binding.llMainLayout.visibility = View.VISIBLE
        binding.llProgressLayout.progressLayout.visibility = View.GONE
        return true
    }

    override fun showErrorMessage(string: String) {
        CommonUtil.alertDialogWithOkOnly(this, "Error", string)
    }

}