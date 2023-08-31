package com.humbhi.blackbox.ui.ui.dashboard

import NoInternetDialogFragment
import android.Manifest
import android.app.ActivityManager
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.humbhi.blackbox.BuildConfig
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityDashboardBinding
import com.humbhi.blackbox.ui.Utility.NetworkChangeListener
import com.humbhi.blackbox.ui.Utility.NetworkChangeReceiver
import com.humbhi.blackbox.ui.Utility.TimeCountingService
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.db.CommonData.getCustIdFromDB
import com.humbhi.blackbox.ui.retofit.Retrofit2
import com.humbhi.blackbox.ui.retofit.RetrofitResponse
import com.humbhi.blackbox.ui.ui.addonReports.AddOnReportActivity
import com.humbhi.blackbox.ui.ui.ais140.AIS140VehicleActivity
import com.humbhi.blackbox.ui.ui.alerts.AlertsOnOffSettings
import com.humbhi.blackbox.ui.ui.billingPayments.BillAccountActivity
import com.humbhi.blackbox.ui.ui.customerCare.CustomerCare
import com.humbhi.blackbox.ui.ui.fms.FMSActivity
import com.humbhi.blackbox.ui.ui.geofencing.ManageGeofenceActivity
import com.humbhi.blackbox.ui.ui.livetracking.GLocationOnMap
import com.humbhi.blackbox.ui.ui.livetracking.LocateMyVehicle
import com.humbhi.blackbox.ui.ui.login.LoginActivity
import com.humbhi.blackbox.ui.ui.notification.GNotifications
import com.humbhi.blackbox.ui.ui.reports.FleetReportsDashboard
import com.humbhi.blackbox.ui.ui.routePlayback.RoutePlayInitialSelection
import com.humbhi.blackbox.ui.ui.settings.SettingsActivity
import com.humbhi.blackbox.ui.ui.vehicleStatus.VehicleStatusActivity
import com.humbhi.blackbox.ui.utils.CommonUtil
import com.humbhi.blackbox.ui.utils.Constants
import io.paperdb.Paper
import kotlinx.coroutines.cancel
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Response
import java.io.File
import java.net.ConnectException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class DashboardActivity : AppCompatActivity(), View.OnClickListener, RetrofitResponse, NetworkChangeListener {
    private lateinit var binding: ActivityDashboardBinding
    private var homeFragment = DashboardFragment()
    private lateinit var custId:String
    private lateinit var username:String
    var counter = 0
    private val handlerMain = Handler(Looper.getMainLooper())
    private val networkChangeReceiver = NetworkChangeReceiver(this)
    private var isDialogVisible = false
    private var isReceiverRegistered = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.toolBar.ivMenu.setOnClickListener(this)
        binding.toolBar.tvTitle.visibility = View.GONE
        binding.toolBar.ivLogo.visibility = View.VISIBLE
        binding.navigation.tvDashboard.setOnClickListener(this)
        binding.navigation.tvLiveStatus.setOnClickListener(this)
        binding.navigation.tvVehicleOnMap.setOnClickListener(this)
        binding.navigation.tvLocateMyVehicle.setOnClickListener(this)
        binding.navigation.tvReports.setOnClickListener(this)
        binding.navigation.tvAddOn.setOnClickListener(this)
        binding.navigation.tvRoutePlayBack.setOnClickListener(this)
        binding.navigation.tvCustomerCare.setOnClickListener(this)
        binding.navigation.tvAlerts.setOnClickListener(this)
        binding.navigation.tvGeofencing.setOnClickListener(this)
        binding.navigation.tvBill.setOnClickListener(this)
        binding.navigation.tvLogout.setOnClickListener(this)
        binding.navigation.tvFMS.setOnClickListener(this)
        binding.navigation.tvAIS140Reminder.setOnClickListener(this)
        binding.navigation.tvSettings.setOnClickListener(this)
        binding.toolBar.ivBell.setOnClickListener(this)
        custId = getCustIdFromDB()
        username = CommonData.getCustNameFromDB()
        if (Build.VERSION.SDK_INT >= 23) {
            val permissionList = ArrayList<String>()
            val hasNotificationAllowed = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            if (hasNotificationAllowed != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            if (permissionList.isNotEmpty()) {
                requestPermissions(permissionList.toArray(arrayOfNulls<String>(0)), 2)
            }
        }
        if (!isReceiverRegistered) {
            registerReceiver(
                networkChangeReceiver,
                IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
            )
            isReceiverRegistered = true
        }
    }

    private fun addFragmentToActivity(fragment: Fragment?) {
        if (fragment == null) return
        if (fragment.isAdded) {
            return //or return false/true, based on where you are calling from
        } else {
            val fm = supportFragmentManager
            val tr = fm.beginTransaction()
            tr.add(R.id.framlayout, fragment)
            tr.commit()
        }
    }

    private fun getNotificationHistory() {
        Retrofit2(
            this,
            this,
            Constants.REQ_GET_NOTIFICATION_LIST,
            Constants.GET_NOTIFICATION_LIST + "custid=" + getCustIdFromDB()+"&Search="+"&sEcho=1"+"&iDisplayStart=0"+"&iDisplayLength=20"+"&iSortCol_0=0&sSortDir_0=asc"
        ).callService(false)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.ivMenu -> {
                binding.dlNavigation.openDrawer(GravityCompat.START)
            }
            R.id.tvDashboard -> {
                binding.dlNavigation.closeDrawer(GravityCompat.START)
                // addFragmentToActivity(homeFragment)
            }
            R.id.tvLiveStatus -> {
                val intent = Intent(this, VehicleStatusActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.tvLocateMyVehicle ->{
                val intent = Intent(this, LocateMyVehicle::class.java)
                startActivity(intent)
                finish()
            }
            R.id.tvVehicleOnMap -> {
//                val intent = Intent(this, LiveTrackingActivity::class.java)
                val intent = Intent(this, GLocationOnMap::class.java)
                startActivity(intent)
                finish()
            }
            R.id.tvReports -> {
                val intent = Intent(this, FleetReportsDashboard::class.java)
                startActivity(intent)
                finish()
            }
            R.id.tvAddOn -> {
                val intent = Intent(this, AddOnReportActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.tvAlerts -> {
                val intent = Intent(this, AlertsOnOffSettings::class.java)
                startActivity(intent)
                finish()
            }
            R.id.tvRoutePlayBack -> {
                val intent = Intent(this, RoutePlayInitialSelection::class.java)
                startActivity(intent)
                finish()
            }
            R.id.ivBell -> {
                val intent = Intent(this, GNotifications::class.java)
                startActivity(intent)
                finish()
            }
            R.id.tvBill -> {
                val intent = Intent(this, BillAccountActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.tvFMS -> {
                if (BuildConfig.DEBUG) {
                    val intent = Intent(this, FMSActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    Constants.alertDialog(this@DashboardActivity,"Coming soon")
                }
            }
            R.id.tvCustomerCare -> {
                val intent = Intent(this, CustomerCare::class.java)
                startActivity(intent)
                finish()
            }
            R.id.tvGeofencing -> {
              //  CommonUtil.alertDialogWithOkOnly(this,"Blackbox","Coming Soon")
                val intent = Intent(this, ManageGeofenceActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.tvSettings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.tvAIS140Reminder -> {
                startActivity(Intent(this, AIS140VehicleActivity::class.java))
                finish()
            }
            R.id.tvLogout -> {
               alertDialogWithOkAndCancel(this,"Logout","Are you sure you want to logout?")

            }
        }
    }

  private fun alertDialogWithOkAndCancel(context: Context, title:String, message:String){
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_alert_ok_cancel)
        val titleHeading = dialog.findViewById(R.id.tvErrorTitle) as AppCompatTextView
        val tvMessage = dialog.findViewById(R.id.tvErrorMessage) as AppCompatTextView
        titleHeading.text = title
        tvMessage.text = message
        val cancelBtn = dialog.findViewById(R.id.tvErrorCancel) as TextView
        val okBtn = dialog.findViewById(R.id.tvErrorOK) as TextView
        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        okBtn.setOnClickListener {
            dialog.dismiss()
            logout()
        }
        dialog.show()
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            val permissionList = ArrayList<String>()
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val hasNotificationAllowed = checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                if (hasNotificationAllowed != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            if (permissionList.isNotEmpty()) {
                requestPermissions(permissionList.toArray(arrayOfNulls<String>(0)), 2)
            }
        }
    }

    private fun logout(){
        val fragment = supportFragmentManager.findFragmentById(R.id.framlayout) as? DashboardFragment
        if (fragment != null) {
            fragment.coroutineScope.cancel()
            fragment.getAllResultJob?.cancel()
            Retrofit2(this@DashboardActivity, this@DashboardActivity, Constants.REQ_LOGOUT, Constants.LOGOUT + "custid=" + getCustIdFromDB()+"&DeviceType=Android").callService(true)
        }
    }

    override fun onServiceResponse(requestCode: Int, response: Response<ResponseBody>?) {
        when(requestCode){
            Constants.REQ_LOGOUT -> {
                Paper.book().destroy()
                clearAllCache(this)
                handlerMain.post() {
                    val intent = Intent(this, LoginActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            Constants.REQ_GET_NOTIFICATION_LIST -> {
                try {
                    val data = JSONObject(response!!.body()!!.string())
                    counter = data.getInt("iTotalRecords")
                    if (counter > 0) {
                        handlerMain.post(){
                            binding.toolBar.badge.visibility = View.VISIBLE
                            if(counter>99){
                                binding.toolBar.badge.text = "99+"
                            }
                            else{
                                binding.toolBar.badge.text = counter.toString()
                            }
                        }
                    }
                }
                catch (e: Exception) {
                    handlerMain.post() {
                        when (e) {
                            is SocketException,is ConnectException, is UnknownHostException -> {
                                Constants.Toastmsg(
                                    this,
                                    getString(R.string.no_network)
                                )
                            }
                            is SocketTimeoutException -> {
                                Constants.Toastmsg(
                                    this,
                                    getString(R.string.time_out)
                                )
                            }
                            else -> {
                                Constants.Toastmsg(
                                    this,
                                    getString(R.string.something_went_wrong)
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    private fun clearAllCache(context: Context) {
        val cacheDir = context.cacheDir
        deleteDir(cacheDir)
    }

    private fun deleteDir(dir: File?): Boolean {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (child in children!!) {
                val success = deleteDir(File(dir, child))
                if (!success) {
                    return false
                }
            }
        }
        return dir?.delete() ?: false
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isReceiverRegistered) {
            unregisterReceiver(networkChangeReceiver)
            isReceiverRegistered = false
            Log.e("stop","stopped")
        }
    }

    override fun onNetworkChanged(isConnected: Boolean) {
        handleInternetConnectivity(isConnected)
    }

    private fun handleInternetConnectivity(isConnected: Boolean) {
        var dialog : NoInternetDialogFragment ? = null
        if (isConnected) {
            if (isDialogVisible) {
                dialog = supportFragmentManager.findFragmentByTag("noInternetDialog") as? NoInternetDialogFragment
                dialog?.dismiss()
                isDialogVisible = false
            }
            getNotificationHistory()
            binding.navigation.tvUsername.text = username
            addFragmentToActivity(homeFragment)
            checkPermission()
        } else {
            if (!isDialogVisible) {
                dialog = NoInternetDialogFragment()
                dialog.isCancelable = false
                dialog.show(this.supportFragmentManager, "noInternetDialog")
                isDialogVisible = true
            }
        }
    }
}