package com.humbhi.blackbox.ui.Utility

import android.app.Service
import android.content.Intent
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.util.Log
import com.humbhi.blackbox.ui.data.db.CommonData
import java.util.Calendar

class TimeCountingService : Service() {
    companion object {
        const val ACTION_STOP_SERVICE = "com.humbhi.blackbox.ACTION_STOP_SERVICE"
    }
    private val handler = Handler(Looper.getMainLooper())
    private val timeInterval = 5 * 24 * 60 * 60 * 1000 // 5 seconds

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent?.action == ACTION_STOP_SERVICE) {
            stopSelf() // Stop the service
        }
        startCountingTime()
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun startCountingTime() {
        handler.postDelayed(object : Runnable {
            override fun run() {
                // Show your dialog or perform the desired action here
                val currentDate = Calendar.getInstance()
                CommonData.setFirstTimeDays(currentDate)
                Log.e("Next Date: ",
                    currentDate.time.toString()
                )
                handler.postDelayed(this, timeInterval.toLong())
            }
        }, timeInterval.toLong())
    }
}
