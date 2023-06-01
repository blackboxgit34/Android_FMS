package com.humbhi.blackbox.ui.utils

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object DateUtil {

    const val DATE_FORMAT = "MMM dd, yyyy| hh:mm a"

    fun getDate(milliSeconds:Long,dateFormat:String):String? {
        val formatter = SimpleDateFormat(dateFormat,Locale.getDefault())
        val calendar:Calendar = Calendar.getInstance()
        calendar.timeInMillis = milliSeconds
        return formatter.format(calendar.time)
    }

    fun convertDateFormat(fromFormat:String,toFormat:String,date:String):String{
        if (date.isNotEmpty()){
            val f:DateFormat = SimpleDateFormat(fromFormat,Locale.ENGLISH)
            return try {
                val d:Date = f.parse(date)!!
                val formattedDate = SimpleDateFormat(toFormat,Locale.ENGLISH)
                formattedDate.format(d)
            }
            catch (e: ParseException){
                e.printStackTrace()
                ""
            }
        }
        return ""
    }

    fun getCurrentDateAndTime():String{
        return SimpleDateFormat(DATE_FORMAT,Locale.getDefault()).format(Calendar.getInstance().time)
    }
}