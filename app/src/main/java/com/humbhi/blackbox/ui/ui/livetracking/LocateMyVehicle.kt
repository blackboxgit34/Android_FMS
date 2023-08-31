package com.humbhi.blackbox.ui.ui.livetracking

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityLocateMyVehicleBinding
import com.humbhi.blackbox.ui.MyApplication
import com.humbhi.blackbox.ui.Utility.WhatsNewDialogFragment
import com.humbhi.blackbox.ui.adapters.SearchableAdapter
import com.humbhi.blackbox.ui.data.AisModel
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.db.CommonData.getCustIdFromDB
import com.humbhi.blackbox.ui.data.models.AllVehicleModel
import com.humbhi.blackbox.ui.retofit.NetworkService
import com.humbhi.blackbox.ui.retofit.NewRetrofitClient
import com.humbhi.blackbox.ui.retofit.Retrofit2
import com.humbhi.blackbox.ui.retofit.RetrofitResponse
import com.humbhi.blackbox.ui.ui.banner.BillBanner
import com.humbhi.blackbox.ui.ui.dashboard.DashboardActivity
import com.humbhi.blackbox.ui.utils.Constants
import com.humbhi.blackbox.ui.utils.GpsTracker
import com.humbhi.blackbox.ui.utils.Network
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONArray
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


class LocateMyVehicle : AppCompatActivity(), RetrofitResponse {
    lateinit var binding: ActivityLocateMyVehicleBinding
    var vehicleModel = ArrayList<AllVehicleModel>()
    var vehicleList = ArrayList<String>()
    var vehicleId = ""
    var vehicleName = ""
    var lat:Double = 0.0
    var long:Double = 0.0
    var myLat:Double = 0.0
    var myLong:Double = 0.0
    lateinit var fusedLocationClient: FusedLocationProviderClient
    lateinit var locationCallback: LocationCallback
    private val executor = Executors.newSingleThreadExecutor()
    private val mainHandler = Handler(Looper.getMainLooper())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocateMyVehicleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setToolbar()
        if(MyApplication.cantSkip.equals("yes")){
            getAisData()
        }
        getAllVehicles()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        // Initialize the location callback
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                for (location in locationResult.locations) {
                    // Handle location updates here
                    myLat = location.latitude
                    myLong = location.longitude
                    Log.e("Lat and Long :",myLat.toString()+" ,"+myLong.toString())
                    // Update UI or perform any necessary operations
                }
            }
        }
        binding.btnGetRoute.setOnClickListener {
            if (isLocationEnabled(this)) {
                if (!vehicleName.equals("")) {
                    val uri = "http://maps.google.com/maps?saddr=$myLat,$myLong&daddr=$lat,$long"
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri))
                    startActivity(intent)
                }
                else{
                    Constants.alertDialog(this,"Please select vehicle from list")
                }
            }
        }
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
                        Constants.Toastmsg(this@LocateMyVehicle, getString(it))
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
                          val dialogFragment = vehicleWithLeastValidity?.commercialvalidity?.let {
                            vehicleWithLeastValidity.vehname?.let { it1 ->
                                WhatsNewDialogFragment(this, expiry,
                                    it, it1
                                )
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

    private fun setToolbar() {
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.tvTitle.visibility = View.VISIBLE
        binding.toolbar.tvTitle.text = "Locate My Vehicle"
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
    }

    private fun getAllVehicles() {
        Retrofit2(
            this,
            this,
            Constants.REQ_LOCATION_ON_MAP,
            Constants.LOCATION_ON_MAP + "id=" + getCustIdFromDB()
        )
            .callServicehitec(true)
    }

    private fun startLocationUpdates() {
        val locationRequest = LocationRequest.create().apply {
            interval = 1000 // Update interval in milliseconds (10 seconds)
            fastestInterval = 100// Fastest update interval in milliseconds
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        // Request location updates
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    // Function to stop location updates
    private fun stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }

    private fun spinVehicles() {
        //Getting the instance of AutoCompleteTextView
        binding.spVehicles.setThreshold(0)
        val adapter = SearchableAdapter(this,vehicleList)//will start working from first character
        binding.spVehicles.setAdapter(adapter) //setting the adapter data into the AutoCompleteTextView
        binding.spVehicles.setOnItemClickListener { parent, view, position, id ->
            val originalData = adapter.originalData
            val selection = originalData[position]
            val originalPosition = adapter.getOriginalPosition(position)
            if (originalPosition != -1) {
                // Use the original position to retrieve the corresponding item
                vehicleId = vehicleModel[originalPosition].bbid
                vehicleName = vehicleModel[originalPosition].vehname
                Retrofit2(this, this, Constants.REQ_VEHICLES_ON_MAP, Constants.VEHICLES_ON_MAP + "custid=" + getCustIdFromDB() + "&StatusCode=&sEcho=0&iDisplayStart=0&iDisplayLength=999" + "&sSearch=" + vehicleName + "&iSortCol_0=0&sSortDir_0=").callService(true)
            }
        }
        binding.spVehicles.setOnFocusChangeListener { v, hasFocus -> if (hasFocus) binding.spVehicles.showDropDown() }
        binding.spVehicles.setOnTouchListener { v, event ->
            binding.spVehicles.showDropDown()
            false
        }
    }

    override fun onServiceResponse(requestCode: Int, response: Response<ResponseBody>?) {
        when (requestCode) {
            Constants.REQ_LOCATION_ON_MAP ->
                if (response!!.isSuccessful) {
                    try {
                        vehicleList.clear()
                        // vehicleList.add(0,"Select Vehicle");
                        val data = JSONArray(response!!.body()!!.string())
                        var i = 0
                        while (i < data.length()) {
                            val obj = data.getJSONObject(i)
                            val model = AllVehicleModel()
                            model.bbid = obj.getString("BoxId")
                            model.vehname = obj.getString("VehName")
                            vehicleList.add(obj.getString("VehName"))
                            vehicleModel.add(model)
                            spinVehicles()
                            i++
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            Constants.REQ_VEHICLES_ON_MAP ->{
                if (response!!.isSuccessful) {
                    try {
                        val jsonObject = JSONObject(response.body()!!.string())
                        val data = jsonObject.getJSONArray("aaData")
                        for ( i in 0 until data.length()) {
                            val obj = data.getJSONObject(i)
                            lat = obj.getDouble("Latitude")
                            long = obj.getDouble("Longitude")
                        }

                    } catch (e: JSONException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
            }
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

    private fun promptToEnableLocation(context: Context) {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        context.startActivity(intent)
    }

    private fun isLocationEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    private fun checkLocationEnabledAndPrompt(activity: Activity) {
        if (!isLocationEnabled(activity)) {
            promptToEnableLocation(activity)
        }
    }

    override fun onPause() {
        super.onPause()
        stopLocationUpdates() // Stop receiving location updates when the activity is paused
    }

    override fun onResume() {
        super.onResume()
        checkLocationEnabledAndPrompt(this)
        startLocationUpdates() // Start receiving location updates when the activity resumes
    }

}