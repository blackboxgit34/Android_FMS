package com.humbhi.blackbox.ui.ui.login.forgotpassword

import android.app.Dialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.chaos.view.PinView
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityForgotPasswordBinding
import com.humbhi.blackbox.ui.Utility.CustomOTPDialog
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.data.models.VerifyOtpModel
import com.humbhi.blackbox.ui.retofit.NetworkService
import com.humbhi.blackbox.ui.retofit.NewRetrofitClient
import com.humbhi.blackbox.ui.retofit.Retrofit2
import com.humbhi.blackbox.ui.retofit.RetrofitResponse
import com.humbhi.blackbox.ui.ui.login.ChangePassword.ChangePasswordActivity
import com.humbhi.blackbox.ui.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

class ForgotPasswordActivity : AppCompatActivity(), RetrofitResponse {
    lateinit var OTPResponse: VerifyOtpModel
    private lateinit var binding: ActivityForgotPasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews()
    {
        binding.tvBackToLogin.setOnClickListener{
            finish()
        }
        binding.btSubmit.setOnClickListener{
            if(binding.etEmail.text!!.trim().isNotEmpty()){
                getForgotPassword(binding.etEmail.text!!.trim().toString())
            }
        }
    }

    private fun getForgotPassword(email: String){
        Retrofit2(this,this,Constants.REQ_FORGOT_PASSWORD,Constants.FORGOT_PASSWORD+"username="+email+"&website=trackmaster.in").callService(true)
    }


    @OptIn(DelicateCoroutinesApi::class)
    override fun onServiceResponse(requestCode: Int, response: Response<ResponseBody>?) {
        when(requestCode){
         Constants.REQ_FORGOT_PASSWORD ->
         {
             try{
                 val responseBody = JSONObject(response!!.body()!!.string())
                 val result = responseBody.getString("message")
                 CommonData.setCustId(responseBody.getString("custid"))
                 if(!result.equals("failed")){
                     val dialog = Dialog(this)
                     dialog.setContentView(R.layout.otp_layout)
                     dialog.setCancelable(false)
                     dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)

                     val otpView = dialog.findViewById<PinView>(R.id.otpView)

                     otpView.addTextChangedListener(object : TextWatcher {
                         override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                         }

                         override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                             if (s?.length == 4) {
                                 binding.progress.progressLayout.visibility = View.VISIBLE
                                 CoroutineScope(Dispatchers.IO).launch{
                                     val Api = NewRetrofitClient.getInstance().create(NetworkService::class.java)
                                     Api.verifyOTP(CommonData.getCustIdFromDB(),"trackmaster.in",s.toString()).enqueue(object: Callback<VerifyOtpModel>{
                                         override fun onResponse(
                                             call: Call<VerifyOtpModel>,
                                             response: Response<VerifyOtpModel>
                                         ){
                                             CoroutineScope(Dispatchers.Main).launch {
                                                 binding.progress.progressLayout.visibility =
                                                     View.GONE
                                                 if (response.code() == 400) {
                                                     Constants.alertDialog(
                                                         this@ForgotPasswordActivity,
                                                         "You entered wrong otp"
                                                     )
                                                 } else {
                                                     OTPResponse = response.body()!!
                                                     if (OTPResponse.message.equals("Verified")) {
                                                         dialog.dismiss()
                                                         val intent = Intent(
                                                             this@ForgotPasswordActivity,
                                                             ChangePasswordActivity::class.java
                                                         )
                                                         startActivity(intent)
                                                     }
                                                 }
                                             }
                                         }

                                         override fun onFailure(call: Call<VerifyOtpModel>, t: Throwable) {
                                             CoroutineScope(Dispatchers.Main).launch {
                                                 binding.progress.progressLayout.visibility =
                                                     View.GONE
                                                 dialog.dismiss()
                                                 if (t is SocketTimeoutException) {
                                                     Constants.alertDialog(
                                                         this@ForgotPasswordActivity,
                                                         "Check your internet connection"
                                                     )
                                                 } else {
                                                     Constants.alertDialog(
                                                         this@ForgotPasswordActivity,
                                                         "Something went wrong"
                                                     )
                                                 }
                                             }
                                         }
                                     })
                                 }
                             }
                         }

                         override fun afterTextChanged(s: Editable?) {

                         }

                     })
                     dialog.show()
                 }
                 else{
                     Constants.alertDialog(this,result)
                 }
             }catch (_:Exception){
             }
         }
        }
    }

}