package com.humbhi.blackbox.ui.Utility

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        // Code to execute when the alarm triggers
        // You can show notifications, update UI, etc. here
        val expiry = intent?.getStringExtra("expiry")
        val validity = intent?.getStringExtra("validity")
        val vehicleName = intent?.getStringExtra("vehicleName")
        val fragmentManager = FragmentManagerHolder.fragmentManager
        Log.e("details",expiry+" ,"+validity+" ,"+vehicleName)
        context?.let {
            if (expiry != null && validity != null && vehicleName != null) {
                val dialogFragment = WhatsNewDialogFragment(it, expiry, validity, vehicleName)
                if (fragmentManager != null) {
                    dialogFragment.show(fragmentManager, "WhatsNewDialog")
                }
                Log.e("status","show")
            }
        }
    }
}

