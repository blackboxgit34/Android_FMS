package com.humbhi.blackbox.ui.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.humbhi.blackbox.R
import com.humbhi.blackbox.databinding.ActivitySplashBinding
import com.humbhi.blackbox.ui.MyApplication
import com.humbhi.blackbox.ui.data.db.CommonData
import com.humbhi.blackbox.ui.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
        }
            setContentView(R.layout.activity_splash)
            Handler(Looper.getMainLooper()).postDelayed({
                MyApplication.skip = "no"
                MyApplication.cantSkip = "no"
                CommonData.setSkip("no")
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }, 3000) // 3000 is the delayed time in milliseconds.
    // notification Code


    }

    override fun onPause() {
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
    }
}