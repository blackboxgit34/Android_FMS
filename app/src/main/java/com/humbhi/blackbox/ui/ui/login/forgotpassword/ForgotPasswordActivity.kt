package com.humbhi.blackbox.ui.ui.login.forgotpassword

import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.humbhi.blackbox.databinding.ActivityForgotPasswordBinding
import com.humbhi.blackbox.ui.retofit.Retrofit2
import com.humbhi.blackbox.ui.retofit.RetrofitResponse
import com.humbhi.blackbox.ui.utils.Constants
import okhttp3.ResponseBody
import retrofit2.Response

class ForgotPasswordActivity : AppCompatActivity(), RetrofitResponse {
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
        Retrofit2(this,this,Constants.REQ_FORGOT_PASSWORD,Constants.FORGOT_PASSWORD+"Login="+email).callServicehitec(true)
    }


    override fun onServiceResponse(requestCode: Int, response: Response<ResponseBody>?) {
        when(requestCode){
         Constants.REQ_FORGOT_PASSWORD ->
         {
             val responseBody = response!!.body()!!.string()
             val result = responseBody.replace(responseBody[0].toString(), "")
             Constants.alertDialog(this,result)
         }
        }
    }
}