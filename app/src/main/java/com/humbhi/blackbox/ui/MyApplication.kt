package com.humbhi.blackbox.ui

import android.app.Application
import android.content.Context
import com.humbhi.blackbox.ui.data.models.*
import io.paperdb.Paper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

class MyApplication:Application() {

    init {
        instance = this
    }
    companion object{
        private var instance:MyApplication? =null
        var overSpeedDetailList:ArrayList<OverSpeedData> = ArrayList()
        var stoppageList : ArrayList<ObjStoppageReport> = ArrayList()
        var fuelData: ArrayList<FuelData> = ArrayList()
        var OverspeedDetailDBList: ArrayList<DrOverSpeedSubLst> = ArrayList()
        var fuelTheftData: ArrayList<FueltheftMainModel> = ArrayList()
        var workHourReportDetail : ArrayList<ObjIgnitionStatusReport> = ArrayList()
        var list :ArrayList<LiveMovementModel> = ArrayList()
        var listDocumentRemionder : ArrayList<ReminderData> = ArrayList()
        var skip = "no"
        var notification = false
        @JvmField
        var cantSkip = "no"
        fun getAppContext():Context{
            return instance!!.applicationContext
        }
    }

    override fun onCreate() {
        super.onCreate()
       // Foreground.init(this)
        Paper.init(this)

    }

    override fun onTerminate() {
        super.onTerminate()
        // logout
    }



}