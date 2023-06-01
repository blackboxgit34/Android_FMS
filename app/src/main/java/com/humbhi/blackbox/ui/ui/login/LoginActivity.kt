package com.humbhi.blackbox.ui.ui.login

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityLoginBinding
import com.humbhi.blackbox.ui.data.DataManagerImpl
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.db.CommonData.setDeviceId
import com.humbhi.blackbox.ui.data.db.CommonData.setFirebaseToken
import com.humbhi.blackbox.ui.data.models.LoginResponse
import com.humbhi.blackbox.ui.data.network.RestClient
import com.humbhi.blackbox.ui.retofit.Retrofit2
import com.humbhi.blackbox.ui.retofit.RetrofitResponse
import com.humbhi.blackbox.ui.ui.banner.BillBanner
import com.humbhi.blackbox.ui.ui.dashboard.DashboardActivity
import com.humbhi.blackbox.ui.ui.login.forgotpassword.ForgotPasswordActivity
import com.humbhi.blackbox.ui.utils.CommonUtil
import com.humbhi.blackbox.ui.utils.Constants
import com.humbhi.blackbox.ui.utils.HexCoder
import okhttp3.ResponseBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Response
import java.io.IOException
import java.io.UnsupportedEncodingException
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException


class LoginActivity : AppCompatActivity(), View.OnClickListener,LoginView, RetrofitResponse{
    private lateinit var binding: ActivityLoginBinding
    private lateinit var loginPresenter: LoginPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
        if (!CommonData.getCustIdFromDB().isNullOrEmpty()){
            intent  = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
        }
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("token failed", task.exception.toString())
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            setFirebaseToken(token)
            setDeviceId(Settings.Secure.getString(this.contentResolver, Settings.Secure.ANDROID_ID))
            // Log and toast
        })
        checkPermission()
    }

    private fun initViews() {
        loginPresenter = LoginPresenterImpl(this,DataManagerImpl(RestClient.getRetrofitBuilderForTrackMaster()))
        binding.btLogin.setOnClickListener(this)
        binding.tvForgotPassword.setOnClickListener(this)
    }

    private fun checkPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            val permissionList = ArrayList<String>()
            val hasLocationAllowed = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val hasNotificationAllowed = checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS)
                if (hasNotificationAllowed != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
            if (hasLocationAllowed != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION)
            }
            if (!permissionList.isEmpty()) {
                requestPermissions(permissionList.toArray(arrayOfNulls<String>(0)), 2)
            }
        }
    }

    override fun onClick(v: View?) {
        val intent:Intent
        when (v?.id) {
            R.id.btLogin -> {
                if (validations()){
                    val deviceId = CommonData.getDeviceId()
                    val FCMToken = CommonData.getFirebaseToken()
                    binding.llProgress.progressLayout.visibility = View.GONE
                    loginPresenter.callLoginApi(binding.etUsername.text.toString().trim()
                        , encrptPassword(binding.etPassword.text.toString().trim())!!
                        ,"Android"
                        ,deviceId,binding.etPassword.text.toString().trim(),FCMToken)
                    CommonData.setUserName(binding.etUsername.text.toString().trim())
                    CommonData.setPassword(binding.etPassword.text.toString().trim())
                    encrptPassword(binding.etPassword.text.toString().trim())?.let {
                        CommonData.setPasswordEncry(
                            it
                        )
                    }
                }
            }
            R.id.tvForgotPassword -> {
                intent  = Intent(this, ForgotPasswordActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun validations():Boolean{
        if (binding.etUsername.text.isNullOrEmpty()){
            binding.etUsername.hint = "Please enter username"
            binding.etUsername.setHintTextColor(resources.getColor(R.color.red))
            return false
        }
        else if (binding.etPassword.text.isNullOrEmpty()){
            binding.etPassword.hint = "Please enter password"
            binding.etPassword.setHintTextColor(resources.getColor(R.color.red))
            return false
        }
        binding.etUsername.setHintTextColor(resources.getColor(R.color.white))
        binding.etPassword.setHintTextColor(resources.getColor(R.color.white))
        return true
    }

    private fun getUserData(){
        Retrofit2(this,this,Constants.REQ_EXPIRE_ACCOUNT_DETAILS,Constants.EXPIRE_ACCOUNT_DETAILS+"custid="+ CommonData.getCustIdFromDB()).callService(true)
    }

    override fun getLoginUserData(loginResponseModel: LoginResponse) {
        if(loginResponseModel.ResponseStatus.Status){
            CommonData.setCustId(loginResponseModel.CustId)
            CommonData.setCustName(loginResponseModel.UserName)
            getUserData()
        }
        else{
            showErrorMessage("Incorrect Username and Password")
        }
    }

    override fun isNetworkConnected(): Boolean {
       CommonUtil.isNetworkAvailable(this)
       return true
    }

    override fun isShowLoading(): Boolean {
        binding.llProgress.progressLayout.visibility = View.VISIBLE
        return true
    }

    override fun isHideLoading(): Boolean {
        binding.llProgress.progressLayout.visibility = View.GONE
        return false
    }

    override fun showErrorMessage(string: String) {
        binding.tvError.visibility = View.VISIBLE
        binding.tvError.text = string
    }

    /*Password encryption*/
    /*
     * encrypt password
     *  */
     fun encrptPassword(password: String): String? {
        var password = password
        val md: MessageDigest
        try {
            md = MessageDigest.getInstance("SHA1")
            password = HexCoder.toHex(md.digest(password.toByteArray(charset("UTF-8")))) // HEX CODE IS USED TO CONVERTED THE ENTERED TEXT INTO USER UNDERSTYANBLE TEXT.
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: UnsupportedEncodingException) {
            e.printStackTrace()
        }
        return password
    }

    override fun onServiceResponse(requestCode: Int, response: Response<ResponseBody>?) {
        when(requestCode) {
            Constants.REQ_EXPIRE_ACCOUNT_DETAILS -> {
                try {
                    var SoftBanner = ""
                    var hardBanner = ""
                    var fuelRod = false
                    var tempRod = false
                    val result = JSONObject(response!!.body()!!.string())
                    val table = result.getJSONArray("Table")
                    for (i in 0 until table.length()) {
                        val jsonObject = table.getJSONObject(i)
                        SoftBanner = jsonObject.getString("SoftBanner")
                        hardBanner = jsonObject.getString("hardBanner")
                        fuelRod = jsonObject.getBoolean("FuelRodActive")
                        tempRod = jsonObject.getBoolean("tepsensor")
                    }
                    if (SoftBanner != "false" || hardBanner != "false") {
                        val intent = Intent(this@LoginActivity, BillBanner::class.java)
                        intent.putExtra("SoftBanner", SoftBanner)
                        intent.putExtra("hardBanner", hardBanner)
                        startActivity(intent)
                        finish()
                    }
                    else{
                        val intent = Intent(this@LoginActivity, DashboardActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    CommonData.setFuelRodStatus(fuelRod)
                    CommonData.setTempRodStatus(tempRod)
                } catch (e: JSONException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

}