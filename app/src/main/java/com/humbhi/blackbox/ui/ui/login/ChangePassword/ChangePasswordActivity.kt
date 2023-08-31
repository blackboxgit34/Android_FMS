package com.humbhi.blackbox.ui.ui.login.ChangePassword

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivityChangePasswordBinding
import com.humbhi.blackbox.databinding.ActivityForgotPasswordBinding
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.retofit.NetworkService
import com.humbhi.blackbox.ui.retofit.NewRetrofitClient
import com.humbhi.blackbox.ui.ui.login.LoginActivity
import com.humbhi.blackbox.ui.utils.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.net.SocketTimeoutException

class ChangePasswordActivity : AppCompatActivity() {
    lateinit var binding: ActivityChangePasswordBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initViews()
    }

    private fun initViews() {
        binding.toolbar.ivBell.visibility = View.GONE
        binding.toolbar.ivMenu.visibility = View.GONE
        binding.toolbar.ivBack.visibility = View.VISIBLE
        binding.toolbar.tvTitle.visibility = View.VISIBLE
        binding.toolbar.tvTitle.text = "Change Password"
        binding.toolbar.ivBack.setOnClickListener {
            finish()
        }
        binding.btSubmit.setOnClickListener {
            if(binding.etPassword.text.toString().isEmpty()){
                Constants.alertDialog(this,"Please enter password")
            }
            else if(binding.etPassword.text.toString().length<6){
                Constants.alertDialog(this,"Password is too short, it must be longer than 5 characters")
            }
            else if(binding.etConfirmPassword.text.toString().isEmpty()){
                Constants.alertDialog(this,"Please enter confirm password")
            }
            else if(!binding.etPassword.text.toString().equals(binding.etConfirmPassword.text.toString())){
                Constants.alertDialog(this,"Both Passwords must match")
            }
            else{
                binding.progress.progressLayout.visibility = View.VISIBLE
                CoroutineScope(Dispatchers.IO).launch {
                    val Api = NewRetrofitClient.getInstance().create(NetworkService::class.java)
                    Api.changePassword(
                        CommonData.getCustIdFromDB(),
                        binding.etPassword.text.toString(),
                        binding.etPassword.text.toString()
                    ).enqueue(object : Callback<String> {
                        override fun onResponse(call: Call<String>, response: Response<String>) {
                            if (response.isSuccessful) {
                                CoroutineScope(Dispatchers.Main).launch {
                                    binding.progress.progressLayout.visibility = View.GONE
                                    Toast.makeText(
                                        this@ChangePasswordActivity,
                                        "Password changed successfully",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    CommonData.setCustId("")
                                    startActivity(
                                        Intent(
                                            this@ChangePasswordActivity,
                                            LoginActivity::class.java
                                        )
                                    )
                                    finish()
                                }
                            }
                        }

                        override fun onFailure(call: Call<String>, t: Throwable) {
                            CoroutineScope(Dispatchers.Main).launch {
                                binding.progress.progressLayout.visibility = View.GONE
                                if (t is SocketTimeoutException) {
                                    Constants.alertDialog(
                                        this@ChangePasswordActivity,
                                        "Check your internet connection"
                                    )
                                } else {
                                    Constants.alertDialog(
                                        this@ChangePasswordActivity,
                                        "Something went wrong"
                                    )
                                }
                            }
                        }
                    })
                }
            }
        }
    }
}