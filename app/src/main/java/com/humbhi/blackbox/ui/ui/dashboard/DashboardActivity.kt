package com.humbhi.blackbox.ui.ui.dashboard

import android.Manifest
import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallState
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType.IMMEDIATE
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityDashboardBinding
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.db.CommonData.getCustIdFromDB
import com.humbhi.blackbox.ui.retofit.Retrofit2
import com.humbhi.blackbox.ui.retofit.RetrofitResponse
import com.humbhi.blackbox.ui.ui.addonReports.AddOnReportActivity
import com.humbhi.blackbox.ui.ui.alerts.AlertsOnOffSettings
import com.humbhi.blackbox.ui.ui.billingPayments.BillAccountActivity
import com.humbhi.blackbox.ui.ui.customerCare.CustomerCare
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
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException

class DashboardActivity : AppCompatActivity(), View.OnClickListener, RetrofitResponse {
    private lateinit var binding: ActivityDashboardBinding
    private var homeFragment = DashboardFragment()
    private lateinit var custId:String
    private lateinit var username:String
    var VersionName = ""
    var VersionCode = 116
    var counter = 0

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
            if (!permissionList.isEmpty()) {
                requestPermissions(permissionList.toArray(arrayOfNulls<String>(0)), 2)
            }
        }
        //checkforUpdate()
        versionCheckApi()
        getNotificationHistory()
        binding.navigation.tvUsername.text = username
        addFragmentToActivity(homeFragment)
        checkPermission()
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
            Constants.GET_NOTIFICATION_LIST + "custid=" + getCustIdFromDB()
        )
            .callServicehitec(false)
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
            }
            R.id.tvLocateMyVehicle ->{
                val intent = Intent(this, LocateMyVehicle::class.java)
                startActivity(intent)
            }
            R.id.tvVehicleOnMap -> {
//                val intent = Intent(this, LiveTrackingActivity::class.java)
                val intent = Intent(this, GLocationOnMap::class.java)
                startActivity(intent)
            }
            R.id.tvReports -> {
                val intent = Intent(this, FleetReportsDashboard::class.java)
                startActivity(intent)
            }
            R.id.tvAddOn -> {
                val intent = Intent(this, AddOnReportActivity::class.java)
                startActivity(intent)
            }
            R.id.tvAlerts -> {
                val intent = Intent(this, AlertsOnOffSettings::class.java)
                startActivity(intent)
            }
            R.id.tvRoutePlayBack -> {
                val intent = Intent(this, RoutePlayInitialSelection::class.java)
                startActivity(intent)
            }
            R.id.ivBell -> {
                val intent = Intent(this, GNotifications::class.java)
                startActivity(intent)
            }
            R.id.tvBill -> {
                val intent = Intent(this, BillAccountActivity::class.java)
                startActivity(intent)
            }
            R.id.tvFMS -> {
                CommonUtil.alertDialogWithOkOnly(this,"Blackbox","Coming Soon")
            }
            R.id.tvCustomerCare -> {
                val intent = Intent(this, CustomerCare::class.java)
                startActivity(intent)
            }
            R.id.tvGeofencing -> {
                CommonUtil.alertDialogWithOkOnly(this,"Blackbox","Coming Soon")
                /*val intent = Intent(this, ManageGeofenceActivity::class.java)
                startActivity(intent)*/
            }
            R.id.tvSettings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
            R.id.tvAIS140Reminder -> {
                CommonUtil.alertDialogWithOkOnly(this,"Blackbox","Coming Soon")
               /* val intent = Intent(this, AIS140VehicleActivity::class.java)
                startActivity(intent)*/
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
                val hasNotificationAllowed =
                    checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                if (hasNotificationAllowed != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            if (!permissionList.isEmpty()) {
                requestPermissions(permissionList.toArray(arrayOfNulls<String>(0)), 2)
            }
        }
    }

    private fun getAppVersion(appversionfromserver: String) {
        val manager = this.packageManager
        var info: PackageInfo? = null
        try {
            info = manager.getPackageInfo(this.packageName, PackageManager.GET_ACTIVITIES)
            VersionName = info.versionCode.toString()
            if(appversionfromserver != ""){
            if(appversionfromserver.toInt() > VersionName.toInt())
            {
                newUpdateDialog()
            }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun newUpdateDialog() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        val li = LayoutInflater.from(this)
        val promptsView: View = li.inflate(R.layout.update_dialog, null)
        dialog.setContentView(promptsView)
        val tvUpdateButton: TextView = promptsView.findViewById<TextView>(R.id.tvUpdateButton)
        tvUpdateButton.setOnClickListener { view: View? ->
            val intent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=com.humbhi.blackbox")
            )
            startActivity(intent)
        }
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
        dialog.show()
    }

    private fun logout(){
        Retrofit2(this@DashboardActivity, this@DashboardActivity, Constants.REQ_LOGOUT, Constants.LOGOUT + "custid=" + getCustIdFromDB()+"&DeviceType=Android").callService(true)
    }

    private fun versionCheckApi(){
        Retrofit2(this,this,Constants.REQ_GET_UPDATE_APP_VERSION,Constants.GET_UPDATE_APP_VERSION+"version="+VersionCode+"&type=3").callService(false)
    }

    override fun onServiceResponse(requestCode: Int, response: Response<ResponseBody>?) {
        when(requestCode){
            Constants.REQ_LOGOUT ->{
                if (response != null) {
                    if (response.isSuccessful) {
                        Paper.book().destroy()
                        val intent = Intent(this, LoginActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
            }

            Constants.REQ_GET_NOTIFICATION_LIST -> {
                try {
                    val data = JSONArray(response!!.body()!!.string())
                    for (i in 0 until data.length()) {
                        val obj = data.getJSONObject(i)
                        if(obj.getString("NotificationRead").equals("UnRead")){
                            counter++
                        }
                    }
                    if(counter>0){
                        binding.toolBar.badge.visibility = View.VISIBLE
                        binding.toolBar.badge.setText(counter.toString())
                    }
                } catch (e: JSONException) {
                    e.printStackTrace()
                    if(e is SocketTimeoutException){
                        Toast.makeText(this,"Connection time out.",Toast.LENGTH_SHORT).show()
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                    if(e is SocketTimeoutException){
                        Toast.makeText(this,"Connection time out.",Toast.LENGTH_SHORT).show()
                    }
                }
            }
            Constants.REQ_GET_UPDATE_APP_VERSION -> {
                try {
                    val responseBody = JSONObject(response!!.body()!!.string())
                    VersionName = responseBody.getString("result")
                    getAppVersion(VersionName)
                } catch (e: JSONException) {
                    if(e is SocketTimeoutException){
                        Toast.makeText(this,"Connection time out.",Toast.LENGTH_SHORT).show()
                    }
                    e.printStackTrace()
                } catch (e: IOException) {
                    if(e is SocketTimeoutException){
                        Toast.makeText(this,"Connection time out.",Toast.LENGTH_SHORT).show()
                    }
                    e.printStackTrace()
                }
            }
        }
    }
}