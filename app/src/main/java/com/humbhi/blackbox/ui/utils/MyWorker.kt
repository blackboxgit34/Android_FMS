package com.humbhi.blackbox.ui.utils

import android.content.Context
import android.os.Looper
import androidx.work.Data
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.OneTimeWorkRequestBuilder
import com.humbhi.blackbox.ui.MyApplication

import com.humbhi.blackbox.ui.Utility.WhatsNewDialogFragment
import java.util.concurrent.TimeUnit
import java.util.logging.Handler

class MyWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        // Your work to be performed goes here
        // Show a notification, update UI, etc.

        val expiry = inputData.getString("expiry")
        val validity = inputData.getString("validity")
        val vehicleName = inputData.getString("vehicleName")

        // Create and show the dialog fragment
        android.os.Handler(Looper.getMainLooper()).post {
            val dialogFragment = WhatsNewDialogFragment(
                MyApplication.getAppContext(), // Use the application context
                expiry ?: "", // Provide a default value if expiry is null
                validity ?: "", // Provide a default value if validity is null
                vehicleName ?: "" // Provide a default value if vehicleName is null
            )
            dialogFragment.showDialog()
        }
        return Result.success()
    }

    companion object {
        fun scheduleWork(context: Context, expiry: String, validity: String, vehicleName: String) {
            val inputData = Data.Builder()
                .putString("expiry", expiry)
                .putString("validity", validity)
                .putString("vehicleName", vehicleName)
                .build()

            val workRequest = OneTimeWorkRequestBuilder<MyWorker>()
                .setInputData(inputData)
                .setInitialDelay(10, TimeUnit.SECONDS)
                .build()

            WorkManager.getInstance(context).enqueue(workRequest)
        }
    }
}
