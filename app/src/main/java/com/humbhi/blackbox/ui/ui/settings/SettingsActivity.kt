package com.humbhi.blackbox.ui.ui.settings

import android.content.Intent
import android.net.DnsResolver
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivitySettingsBinding
import com.humbhi.blackbox.ui.MyApplication
import com.humbhi.blackbox.ui.Utility.WhatsNewDialogFragment
import com.humbhi.blackbox.ui.adapters.BoxInfoAdapter
import com.humbhi.blackbox.ui.data.AisModel
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.models.BoxInfoModel
import com.humbhi.blackbox.ui.data.models.SettingsData
import com.humbhi.blackbox.ui.retofit.NetworkService
import com.humbhi.blackbox.ui.retofit.NewRetrofitClient
import com.humbhi.blackbox.ui.retofit.Retrofit2
import com.humbhi.blackbox.ui.retofit.RetrofitResponse
import com.humbhi.blackbox.ui.ui.banner.BillBanner
import com.humbhi.blackbox.ui.ui.dashboard.DashboardActivity
import com.humbhi.blackbox.ui.utils.Constants
import com.humbhi.blackbox.ui.utils.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.CancellationException
import java.util.concurrent.Executors


class SettingsActivity : AppCompatActivity(), RetrofitResponse {
    private lateinit var binding:ActivitySettingsBinding
    private val executor = Executors.newSingleThreadExecutor()
    private val mainHandler = Handler(Looper.getMainLooper())
    var startLimit = 0
    var Limit = 20
    var list = ArrayList<SettingsData>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.tvTitle.text = "Setting & Customization"
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivSort.visibility = View.GONE
        if(MyApplication.cantSkip.equals("yes")){
            getAisData()
        }
        binding.toolbar.ivBack.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
            if (Network.isNetworkAvailable(this)) {
                Retrofit2(
                    this, this,
                    Constants.REQ_EXPIRE_ACCOUNT_DETAILS,
                    Constants.EXPIRE_ACCOUNT_DETAILS
                            + "custid=" + CommonData.getCustIdFromDB()
                ).callService(false)
            }
        }
//        binding.etSearchBar.setOnEditorActionListener { textView, actionId, keyEvent ->
//            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//                startLimit = 0
//                binding.loadMore.visibility = View.GONE
//                list.clear()
//                getBoxInfo()
//            }
//            false
//        }
//        binding.vehicleList.setHasFixedSize(true)
//        binding.vehicleList.layoutManager = LinearLayoutManager(this)
//        getBoxInfo()
    }

    private fun getAisData() {
        val Api = NewRetrofitClient.getInstance().create(NetworkService::class.java)
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch{
            try {
                val handleAisDataResponse = Api.getAis140VehicleStatuses(CommonData.getCustIdFromDB())
                handleAisDataResponse(handleAisDataResponse)
            }
            catch (e: Exception) {
                scope.coroutineContext.cancelChildren()
                val errorMessage = when (e) {
                    is ConnectException, is UnknownHostException -> R.string.no_network
                    is SocketTimeoutException -> R.string.time_out
                    is CancellationException, is SocketException -> null // Handle network loss
                    else -> R.string.something_went_wrong
                }?.let {
                    withContext(Dispatchers.Main) {
                        Constants.Toastmsg(this@SettingsActivity, getString(it))
                    }
                }
                throw e
            }
        }
    }
 private fun handleAisDataResponse(response: Response<AisModel>?) {
        if (response != null) {
            if (response.isSuccessful) {
                mainHandler.post {
                    val responseFromBody = response.body()
                    val vehicleWithLeastValidity = responseFromBody?.Table?.minByOrNull { it.ExpireIndays }
                    var expiry = ""
                    if (vehicleWithLeastValidity != null) {
                        val expireIndays = vehicleWithLeastValidity.ExpireIndays
                        val paymentStatus = vehicleWithLeastValidity.PaymentStatus
                        when {
                            expireIndays in 29 downTo 9 && paymentStatus == "Not Paid" -> {
                                expiry = "attentionAlert"
                            }
                            expireIndays in 9 downTo 4 && paymentStatus == "Not Paid" -> {
                                expiry = "justAlert"
                            }
                            expireIndays in 4 downTo 0 && paymentStatus == "Not Paid" -> {
                                expiry = "expiringToday"
                            }
                            expireIndays < 0 && paymentStatus == "Not Paid" -> {
                                MyApplication.cantSkip = "yes"
                                expiry = "expired"
                            }
                        }
                    }
                         if (!isFinishing) {
                             val dialogFragment =
                                 vehicleWithLeastValidity?.let { it1 ->
                                     vehicleWithLeastValidity.commercialvalidity?.let { it2 ->
                                         vehicleWithLeastValidity.vehname?.let { it3 ->
                                             WhatsNewDialogFragment(
                                                 this,
                                                 expiry,
                                                 it2,
                                                 it3
                                             )
                                         }
                                     }
                                 }
                        if (dialogFragment != null) {
                            dialogFragment.show(supportFragmentManager, "WhatsNewDialog")
                        }
                    }
                }
            }
        }
    }

    fun getBoxInfo(){
        binding.progress.progressLayout.visibility = View.VISIBLE
        val Api = NewRetrofitClient.getInstance().create(NetworkService::class.java)
        executor.execute {
            Api.boxInfo(
                CommonData.getCustIdFromDB(),
                "1",
                startLimit,
                Limit,
                "",
                "",
                binding.etSearchBar.text.toString(),
                "0",
                "asc"
            ).enqueue(object : Callback<BoxInfoModel> {
                override fun onResponse(
                    call: Call<BoxInfoModel>,
                    response: Response<BoxInfoModel>
                ) {
                    mainHandler.post {
                        binding.progress.progressLayout.visibility = View.GONE
                        val responseFromBody = response.body()
                        if (responseFromBody != null) {
                            val totalRecords = responseFromBody.iTotalRecords
                            for(i in 0 until responseFromBody.aaData.size){
                                list.add(responseFromBody.aaData[i])
                            }
                            val adapter = BoxInfoAdapter(this@SettingsActivity,list)
                            binding.vehicleList.adapter = adapter
                            binding.vehicleList.scrollToPosition(startLimit)
                            if(totalRecords==list.size){
                                binding.loadMore.visibility = View.GONE
                            }
                            binding.loadMore.setOnClickListener {
                                if(list.size<totalRecords) {
                                    startLimit += 20
                                    getBoxInfo()
                                }
                            }
                        }
                    }
                }

                override fun onFailure(call: Call<BoxInfoModel>, t: Throwable) {
                    mainHandler.post {
                        binding.progress.progressLayout.visibility = View.GONE
                        if (t is SocketTimeoutException) {
                            Constants.alertDialog(
                                this@SettingsActivity,
                                this@SettingsActivity.getString(com.humbhi.blackbox.R.string.time_out)
                            )
                        } else if (t is UnknownHostException) {
                            Constants.alertDialog(
                                this@SettingsActivity,
                                this@SettingsActivity.getString(com.humbhi.blackbox.R.string.no_network)
                            )
                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            if (t is DnsResolver.DnsException) {
                                Constants.alertDialog(
                                    this@SettingsActivity,
                                    this@SettingsActivity.getString(com.humbhi.blackbox.R.string.dns_error)
                                )
                            }
                        } else {
                            Constants.alertDialog(
                                this@SettingsActivity,
                                this@SettingsActivity.getString(com.humbhi.blackbox.R.string.something_went_wrong)
                            )
                        }
                    }
                }
            })
        }
    }

    override fun onServiceResponse(requestCode: Int, response: Response<ResponseBody>?) {
        when(requestCode){
            Constants.REQ_EXPIRE_ACCOUNT_DETAILS -> {
                executor.execute {
                    try {
                        var SoftBanner = ""
                        var hardBanner = ""
                        var aisCount = 0
                        val result = JSONObject(response!!.body()!!.string())
                        val table = result.getJSONArray("Table")
                        for (i in 0 until table.length()) {
                            val jsonObject = table.getJSONObject(0)
                            SoftBanner = jsonObject.getString("SoftBanner")
                            hardBanner = jsonObject.getString("hardBanner")
                            aisCount = jsonObject.getString("Ais140Count").toInt()
                        }
                        CommonData.setAisCount(aisCount.toString())
                        if(!CommonData.getSkip().equals("yes")) {
                            if (SoftBanner != "false") {
                                mainHandler.post() {
                                    val intent = Intent(this, BillBanner::class.java)
                                    intent.putExtra("SoftBanner", SoftBanner)
                                    intent.putExtra("hardBanner", hardBanner)
                                    startActivity(intent)
                                    finish()
                                }
                            }
                        }
                        if (hardBanner != "false") {
                            mainHandler.post() {
                                val intent = Intent(this, BillBanner::class.java)
                                intent.putExtra("SoftBanner", SoftBanner)
                                intent.putExtra("hardBanner", hardBanner)
                                startActivity(intent)
                                finish()
                            }
                        }
                    } catch (e: JSONException) {
                    } catch (e: IOException) {
                    }
                }
            }
        }
    }
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        // Do nothing to prevent going back
    }

}