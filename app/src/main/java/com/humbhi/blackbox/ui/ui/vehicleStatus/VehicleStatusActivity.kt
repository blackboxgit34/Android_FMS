package com.humbhi.blackbox.ui.ui.vehicleStatus

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.TextView.OnEditorActionListener
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityVehicleStatusBinding
import com.humbhi.blackbox.ui.MyApplication
import com.humbhi.blackbox.ui.Utility.WhatsNewDialogFragment
import com.humbhi.blackbox.ui.adapters.VehicleStatusAdapter
import com.humbhi.blackbox.ui.data.AisModel
import com.humbhi.blackbox.ui.data.DataManagerImpl
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.models.VehicleLiveStatusDataItem
import com.humbhi.blackbox.ui.data.models.VehicleLiveStatusModel
import com.humbhi.blackbox.ui.data.network.RestClient
import com.humbhi.blackbox.ui.retofit.NetworkService
import com.humbhi.blackbox.ui.retofit.NewRetrofitClient
import com.humbhi.blackbox.ui.retofit.Retrofit2
import com.humbhi.blackbox.ui.retofit.RetrofitResponse
import com.humbhi.blackbox.ui.ui.banner.BillBanner
import com.humbhi.blackbox.ui.ui.dashboard.DashboardActivity
import com.humbhi.blackbox.ui.ui.livetracking.LiveCarActivity
import com.humbhi.blackbox.ui.utils.CommonUtil
import com.humbhi.blackbox.ui.utils.Constants
import com.humbhi.blackbox.ui.utils.IntentConstant
import com.humbhi.blackbox.ui.utils.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.concurrent.CancellationException
import java.util.concurrent.Executors

class VehicleStatusActivity : AppCompatActivity(), View.OnClickListener,VehicleStatusView, RetrofitResponse{
    private lateinit var binding: ActivityVehicleStatusBinding
    private lateinit var vehicleStatusPresenter: VehicleStatusPresenter
    private lateinit var adapter: VehicleStatusAdapter
    private lateinit var progress: ProgressDialog
    var startlimit = 0
    var limit = 20
    var list : ArrayList<VehicleLiveStatusDataItem> = ArrayList()
    var search = ""
    var totalRecords = 0
    var statusFilter:String = ""
    private var handler: Handler? = null
    private val executor = Executors.newSingleThreadExecutor()
    private val mainHandler = Handler(Looper.getMainLooper())
    var apiCalling = false
    var firstTime = true
    val scope = CoroutineScope(Dispatchers.IO)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVehicleStatusBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if(MyApplication.cantSkip.equals("yes")){
            getAisData()
        }
        progress = ProgressDialog(this)
        vehicleStatusPresenter = VehcileStatusPresenterImpl(
            this,
            DataManagerImpl(RestClient.getRetrofitBuilderForTrackMasterSecure())
        )
        binding.tvFilter.setOnClickListener(this)
        setToolbarOptions()
        handler = Handler(Looper.myLooper()!!)
        binding.etSearchBar.setOnEditorActionListener(OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                startlimit = 0
                binding.loadMore.visibility = View.GONE
                list.clear()
                search = v.text.toString().toUpperCase()
                Api()
                return@OnEditorActionListener true
            }
            false
        })

        if(intent.hasExtra("filterValue")) {
            when (intent.getStringExtra("filterValue")) {
                "M" -> {
                    statusFilter = "M"
                }
                "P" -> {
                    statusFilter = "P"
                }
                "U" -> {
                    statusFilter = "U"
                }
                "I" -> {
                    statusFilter = "I"
                }
                "H" -> {
                    statusFilter = "H"
                }
                "BD" -> {
                    statusFilter = "BD"
                }
                else -> {
                    statusFilter = ""
                }
            }
        }
        startGettingOnlineDataFromApi()
        binding.swipeRefreshLayout.setOnRefreshListener {
            performRefresh()
        }
    }

    private fun getAisData() {
        val Api = NewRetrofitClient.getInstance().create(NetworkService::class.java)
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
                        Constants.Toastmsg(this@VehicleStatusActivity, getString(it))
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

    fun stopRepeatingTask() {
        handler!!.removeCallbacksAndMessages(null)
    }

    private fun performRefresh() {
        if(apiCalling==false) {
            binding.swipeRefreshLayout.isRefreshing = true
            list.clear()
            apiCalling=true
            Api()
        }
    }

    var mStatusChecker: Runnable = object : Runnable {
        override fun run() {
            try {
                list.clear()
                Api()
            } catch (e: Exception) {

            }
            handler!!.postDelayed(this,60*1000)
        }
    }

    private fun startGettingOnlineDataFromApi() {
        try {
            handler!!.post(mStatusChecker)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    private fun setToolbarOptions() {
        binding.toolBar.ivMenu.visibility = View.GONE
        binding.toolBar.ivBack.visibility = View.VISIBLE
        binding.toolBar.ivBell.visibility = View.GONE
        binding.toolBar.tvTitle.text = "Live Tracking"
        binding.toolBar.ivBack.setOnClickListener {
            stopRepeatingTask()
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
    }

    private fun Api(){
        if(firstTime==true) {
            binding.llProgressLayout.progressLayout.visibility = View.VISIBLE
            vehicleStatusPresenter.hitLiveStatusApi(
                CommonData.getCustIdFromDB(),
                statusFilter,
                "0",
                startlimit,
                limit,
                search,
                "0",
                ""
            )
        }
        else{
            vehicleStatusPresenter.hitLiveStatusApi(
                CommonData.getCustIdFromDB(),
                statusFilter,
                "0",
                startlimit,
                limit,
                search,
                "0",
                ""
            )
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.tvFilter -> {
                val dialog = BottomSheetDialog(this)
                val view = layoutInflater.inflate(R.layout.vehicle_filterbottomsheet, null)
                val btnClose = view.findViewById<Button>(R.id.idBtnDismiss)
                val rbMoving = view.findViewById<Button>(R.id.rbMoving)
                val rbParked = view.findViewById<Button>(R.id.rbParked)
                val rbUnreach = view.findViewById<Button>(R.id.rbUnreach)
                val rbHiSpeed = view.findViewById<Button>(R.id.rbHiSpeed)
                val rbIgnitionOn = view.findViewById<Button>(R.id.rbIgnitionOn)
                val rbAll = view.findViewById<Button>(R.id.rbAll)
                val rbBD = view.findViewById<Button>(R.id.rbBatteryDisconnection)
                rbMoving.setOnClickListener {
                    statusFilter = "M"
                }
                rbParked.setOnClickListener {
                    statusFilter = "P"
                }
                rbUnreach.setOnClickListener {
                    statusFilter = "U"
                }
                rbHiSpeed.setOnClickListener {
                    statusFilter = "H"
                }
                rbIgnitionOn.setOnClickListener {
                    statusFilter = "I"
                }
                rbBD.setOnClickListener {
                    statusFilter = "BD"
                }
                rbAll.setOnClickListener {
                    statusFilter = ""
                }
                btnClose.setOnClickListener {
                    binding.loadMore.visibility = View.GONE
                    startlimit = 0
                    list.clear()
                    vehicleStatusPresenter.hitLiveStatusApi(CommonData.getCustIdFromDB(),statusFilter,"0",startlimit,limit,binding.etSearchBar.text.toString().trim(),"0","")
                    dialog.dismiss()
                }
                dialog.setCancelable(false)
                dialog.setContentView(view)
                dialog.show()
            }
        }
    }

    override fun getVehicleStatus(listData: VehicleLiveStatusModel) {
        apiCalling=false
        firstTime=false
        binding.llProgressLayout.progressLayout.visibility = View.GONE
        if(binding.swipeRefreshLayout.isRefreshing == true){
            binding.swipeRefreshLayout.isRefreshing = false
        }
        binding.tvTotalCount.text = listData.iTotalRecords.toString()+" Vehicle"
        val layoutManager = LinearLayoutManager(this)
        totalRecords = listData.iTotalRecords
        for(i in 0 until  listData.aaData.size) {
            list.add(listData.aaData[i])
        }
        binding.rvVehicle.layoutManager = layoutManager
        adapter = VehicleStatusAdapter(
            this,
            object : VehicleStatusAdapter.VehicleDetails {
                override fun onVehicleSelection(position: Int) {
                    stopRepeatingTask()
                    val intent = Intent(this@VehicleStatusActivity, LiveCarActivity::class.java)
                    intent.putExtra("vehicleName", list[position].VehicleName)
                    intent.putExtra("vehicleNameIcon", list[position].VIconName)
                    startActivity(intent)
                }
            },
            list)
        binding.rvVehicle.adapter = adapter
       // loadMore = false
        binding.rvVehicle.scrollToPosition(startlimit)
        binding.rvVehicle.visibility = View.VISIBLE
        if(totalRecords==list.size){
            binding.loadMore.visibility = View.GONE
        }
        else{
            binding.loadMore.visibility = View.VISIBLE
        }
        binding.loadMore.setOnClickListener {
            if(list.size<totalRecords) {
                startlimit += 20
                Api()
            }
        }
//        binding.rvVehicle.addOnScrollListener( object : EndlessRecyclerOnScrollListener(){
//            override fun onLoadMore() {
//                if(loadMore==false){
//                    if(list.size<totalRecords){
//                        loadMore = true
//                        startlimit += 20
//                        Api()
//                    }
//                }
//            }
//        })
    }

    override fun isNetworkConnected(): Boolean {
        CommonUtil.isNetworkAvailable(this)
        return true
    }

    override fun isShowLoading(): Boolean {
        return true
    }

    override fun isHideLoading(): Boolean {
        return true
    }

    override fun showErrorMessage(string: String) {
      //  CommonUtil.alertDialogWithOkOnly(this, "Error", string)
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