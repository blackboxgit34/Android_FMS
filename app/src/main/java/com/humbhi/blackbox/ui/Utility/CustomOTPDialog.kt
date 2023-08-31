package com.humbhi.blackbox.ui.Utility

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.Window
import com.chaos.view.PinView
import com.humbhi.blackbox.R

class CustomOTPDialog(context: Context, private val otpListener: OTPListener) : Dialog(context) {

    private lateinit var otpView: PinView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        
        val inflater = LayoutInflater.from(context)
        val dialogView = inflater.inflate(R.layout.otp_layout, null)
        setContentView(dialogView)

        otpView = dialogView.findViewById(R.id.otpView)
        otpView.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s?.length == 4) {
                    otpListener?.onOTPComplete(s.toString())
                    dismiss()
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
    }

    interface OTPListener {
        fun onOTPComplete(otp: String)
    }
}
