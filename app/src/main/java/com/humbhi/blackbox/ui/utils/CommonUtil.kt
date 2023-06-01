package com.humbhi.blackbox.ui.utils

import android.app.Dialog
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.view.Window
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import com.humbhi.blackbox.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


object CommonUtil {
    fun isNetworkAvailable(context:Context):Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            val nw = connectivityManager.activeNetwork ?: return false
            val actnw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when{
                actnw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)->true
                actnw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)->true
                actnw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)->true
                else -> false

            }

        }else{
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }

    fun alertDialogWithOkAndCancel(context: Context,title:String,message:String){
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_alert_ok_cancel)
        val titleHeading = dialog.findViewById(R.id.tvErrorTitle) as AppCompatTextView
        val tvMessage = dialog.findViewById(R.id.tvErrorMessage) as AppCompatTextView
        titleHeading.text = title
        tvMessage.text = message
        val cancelBtn = dialog.findViewById(R.id.tvErrorCancel) as TextView
        val okBtn = dialog.findViewById(R.id.tvErrorOK) as TextView
        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        okBtn.setOnClickListener {
            if (title=="Logout"){
                dialog.dismiss()
            }
            else{
                dialog.dismiss()
            }

        }
        dialog.show()
    }

    fun alertDialogWithOkOnly(context: Context,title:String,message:String){
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.layout_alert_ok_cancel)
        val titleHeading = dialog.findViewById(R.id.tvErrorTitle) as AppCompatTextView
        val tvMessage = dialog.findViewById(R.id.tvErrorMessage) as AppCompatTextView
        titleHeading.text = title
        tvMessage.text = message
        val cancelBtn = dialog.findViewById(R.id.tvErrorCancel) as TextView
        val okBtn = dialog.findViewById(R.id.tvErrorOK) as TextView
        cancelBtn.visibility = View.GONE
        cancelBtn.setOnClickListener {
            dialog.dismiss()
        }
        okBtn.setOnClickListener {
                dialog.dismiss()
        }
        dialog.show()
    }

    fun getCurrentDate(): String {
        val c: Date = Calendar.getInstance().time
        val df = SimpleDateFormat("MM-dd-yyyy", Locale.getDefault())
        return df.format(c)
    }

    fun getCurrentDateYearFirst(): String {
        val c: Date = Calendar.getInstance().time
        val df = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return df.format(c)
    }

    fun getYesterdayDate():String{

        val dateFormat: DateFormat = SimpleDateFormat("MM-dd-yyyy")
        return dateFormat.format(yesterday())
    }
    fun getYesterdayDateYearFirst():String{

        val dateFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        return dateFormat.format(yesterday())
    }
    private fun yesterday(): Date? {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -1)
        return cal.time
    }
    fun getWeekDate():String{
        val dateFormat: DateFormat = SimpleDateFormat("MM-dd-yyyy")
        return dateFormat.format(week())
    }

    private fun week(): Date? {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -7)
        return cal.time
    }

    fun firstDayOfMonth(): String {
        val c = Calendar.getInstance()
        c.add(Calendar.MONTH,1)
        c.add(Calendar.DAY_OF_MONTH,0)// this takes current date
        c.add(Calendar.YEAR,0)
        val month = c.get(Calendar.MONTH).toString()
        val date = c.getActualMinimum(Calendar.DATE).toString()
        val year = c.get(Calendar.YEAR).toString()
        return "${month}/${date}/${year}"
    }

    fun lastDayOfPreviousMonth(): String {
        val c = Calendar.getInstance()
        c.add(Calendar.MONTH,-1)
        c.add(Calendar.YEAR,0)
        c.add(Calendar.DATE,c.getActualMaximum(Calendar.DATE))
        val date = c.get(Calendar.DATE).toString()
        val month = c.get(Calendar.MONTH).toString()
        val year = c.get(Calendar.YEAR).toString()
        return "${month}/${date}/${year}"
    }

    fun firstDayOfLastMonth(): String {
        val c = Calendar.getInstance()
        c.add(Calendar.MONTH,0)
        c.add(Calendar.YEAR,0)
        c.set(Calendar.DAY_OF_MONTH,1)
        val month = c.get(Calendar.MONTH).toString()
        val date = c.get(Calendar.DAY_OF_MONTH).toString()
        val year = c.get(Calendar.YEAR).toString()
        return "${month}/${date}/${year}"
    }

    fun lastDayOfBeforePreviousMonth(): String {
        val c = Calendar.getInstance()
//        c.add(Calendar.MONTH,-2)
//        c.add(Calendar.YEAR,0)
//        c.add(Calendar.DATE,c.getActualMaximum(Calendar.DATE))
        val date = c.getActualMaximum(Calendar.DATE).toString()
        val month = c.get(Calendar.MONTH-2).toString()
        val year = c.get(Calendar.YEAR).toString()
        return "${month}/${date}/${year}"
    }

    fun firstDayOfBeforeLastMonth(): String {
        val c = Calendar.getInstance()
        c.add(Calendar.MONTH,-1)
        c.add(Calendar.YEAR,0)
        c.set(Calendar.DAY_OF_MONTH,1)
        val month = c.get(Calendar.MONTH).toString()
        val date = c.get(Calendar.DAY_OF_MONTH).toString()
        val year = c.get(Calendar.YEAR).toString()
        return "${month}/${date}/${year}"
    }

    fun getMonthDate():String{
        val dateFormat: DateFormat = SimpleDateFormat("MM-dd-yyyy")
        return dateFormat.format(month())
    }

    fun getMonthDateNew():String{
        val dateFormat: DateFormat = SimpleDateFormat("MM")
        return dateFormat.format(monthOfYear())
    }

    fun getYear():String{
        val dateFormat: DateFormat = SimpleDateFormat("yyyy")
        return dateFormat.format(year())
    }

    private fun month(): Date? {
        val cal = Calendar.getInstance()
        cal.add(Calendar.DATE, -30)
        return cal.time
    }

    private fun monthOfYear(): Date? {
        val cal = Calendar.getInstance()
        cal.set(Calendar.MONTH,1)
        return cal.time
    }

    private fun year(): Date? {
        val cal = Calendar.getInstance()
        cal.add(Calendar.YEAR, 0)
        return cal.time
    }


    /*
 * Calculate stoppage time*/
    fun calculateStoppageTime(stoppageTime: String):String {
        var sDays = stoppageTime.substringBefore("-")
        val sHours = stoppageTime.substringAfter("-")
        if (sDays == "00") {
            var newStrg = sHours
            val mString = newStrg.split(":").toTypedArray()
            if (mString[0] == "00") {

                return mString[1] + "M " + mString[2] + "S "

            } else {
                return mString[1] + "M " + mString[2] + "S "
            }

        } else {
            return sDays + " Days"
        }
    }


}