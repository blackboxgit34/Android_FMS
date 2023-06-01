package com.humbhi.blackbox.ui.utils

import android.app.Activity
import android.content.Intent
import android.os.Bundle

object ExplicitIntentUtil {

    fun startActivityForResult(fromContext: Activity, toClass:Class<*>, reqCode:Int, extras: Bundle?){
        val intentt = Intent(fromContext,toClass)
        if (extras!=null){
            intentt.putExtras(extras)
        }
        fromContext.startActivityForResult(intentt,reqCode)
    }

    fun startActivity(fromContext: Activity,toClass: Class<*>,extras: Bundle?){
        val intentt = Intent(fromContext,toClass)
        if (extras != null){
            intentt.putExtras(extras)
        }
        fromContext.startActivity(intentt)
    }
}