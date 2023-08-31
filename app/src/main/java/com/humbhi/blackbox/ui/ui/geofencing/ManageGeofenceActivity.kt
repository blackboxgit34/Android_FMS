package com.humbhi.blackbox.ui.ui.geofencing

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.humbhi.blackbox.ui.ui.geofencing.addFence.AddGeofenceActivity
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityManageGeofenceBinding
import com.humbhi.blackbox.ui.MyApplication
import com.humbhi.blackbox.ui.Utility.WhatsNewDialogFragment
import com.humbhi.blackbox.ui.adapters.ManageGeofenceAdapter
import com.humbhi.blackbox.ui.data.AisModel
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.models.FenceData
import com.humbhi.blackbox.ui.data.models.ViewFenceModel
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
import java.util.Locale
import java.util.concurrent.CancellationException
import java.util.concurrent.Executors

class ManageGeofenceActivity : AppCompatActivity(), RetrofitResponse {
    private lateinit var binding:ActivityManageGeofenceBinding
    private lateinit var adapter:ManageGeofenceAdapter
    private val mainHandler = Handler(Looper.getMainLooper())
    val scope = CoroutineScope(Dispatchers.IO)
    private val executor = Executors.newSingleThreadExecutor()
    val Api = NewRetrofitClient.getInstance().create(NetworkService::class.java)
    var startlimit = 0
    var limit = 20
    var search = ""
    var list : ArrayList<FenceData> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityManageGeofenceBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolbar.tvTitle.text = "Geofence"
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivAdd.visibility = View.VISIBLE
        if(MyApplication.cantSkip.equals("yes")) {
            getAisData()
        }
        val layoutManager = LinearLayoutManager(this)
        binding.rvRecycler.layoutManager = layoutManager

        binding.toolbar.ivBack.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
            if (Network.isNetworkAvailable(this)) {
                Retrofit2(
                    this, this,
                    Constants.REQ_EXPIRE_ACCOUNT_DETAILS,
                    Constants.EXPIRE_ACCOUNT_DETAILS + "custid=" + CommonData.getCustIdFromDB()
                ).callService(false)
            }
        }

        binding.toolbar.ivAdd.setOnClickListener {
            val intent = Intent(this, AddGeofenceActivity::class.java)
            startActivity(intent)
        }

        binding.tvAdd.setOnClickListener {
            val intent = Intent(this, AddGeofenceActivity::class.java)
            startActivity(intent)
        }

        binding.etSearchBar.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                startlimit = 0
                binding.loadMore.visibility = View.GONE
                list.clear()
                search = v.text.toString().toUpperCase(Locale.ROOT)
                viewFences()
                return@OnEditorActionListener true
            }
            false
        })
        viewFences()
    }

    private fun viewFences(){
        binding.progress.progressLayout.visibility = View.VISIBLE
        Api.viewAllFences(CommonData.getCustIdFromDB(),"",startlimit,limit,search
            ,"","").enqueue(object: Callback<ViewFenceModel>{
            override fun onResponse(call: Call<ViewFenceModel>, response: Response<ViewFenceModel>) {
                binding.progress.progressLayout.visibility = View.GONE
                val responseFromApi = response.body()
                val list = ArrayList<FenceData>()
                if (responseFromApi != null) {
                    for(i in 0 until responseFromApi.aaData.size){
                        list.add(responseFromApi.aaData[i])
                    }
                }
                adapter = ManageGeofenceAdapter(this@ManageGeofenceActivity,list)
                binding.rvRecycler.adapter = adapter
                if(list.size==0){
                    binding.llNoData.visibility = View.VISIBLE
                    binding.loadMore.visibility = View.GONE 
                }
            }

            override fun onFailure(call: Call<ViewFenceModel>, t: Throwable) {
                binding.progress.progressLayout.visibility = View.GONE
                when (t) {
                    is ConnectException, is UnknownHostException -> R.string.no_network
                    is SocketTimeoutException -> R.string.time_out
                    is CancellationException, is SocketException -> null // Handle network loss
                    else -> R.string.something_went_wrong
                }?.let {
                    Constants.Toastmsg(this@ManageGeofenceActivity, getString(it))
                }
                throw t
            }
        })
    }
    private fun getAisData() {
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
                        Constants.Toastmsg(this@ManageGeofenceActivity, getString(it))
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
                       val dialogFragment = vehicleWithLeastValidity?.let { it1 ->
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
                        if(CommonData.getSkip() != "yes") {
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
}