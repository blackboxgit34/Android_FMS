package com.humbhi.blackbox.ui.Utility

import android.widget.Toast
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class TimeoutInterceptor : Interceptor {

    companion object {
        private const val TIMEOUT_DURATION_MILLISECONDS = 30000 // Timeout duration in milliseconds
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val startTime = System.currentTimeMillis()

        val response = chain.proceed(request)

        val endTime = System.currentTimeMillis()
        val elapsedTime = endTime - startTime

        if (elapsedTime > TIMEOUT_DURATION_MILLISECONDS) {

        }

        return response
    }
}
