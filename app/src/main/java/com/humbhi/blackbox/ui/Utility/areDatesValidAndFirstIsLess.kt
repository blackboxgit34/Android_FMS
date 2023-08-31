package com.humbhi.blackbox.ui.Utility

import java.text.SimpleDateFormat
import java.util.*
fun areDatesValidAndFirstIsLess(startDateStr: String, endDateStr: String): Boolean {
    // Define the date format
    val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)
    try {
        // Parse the date strings to date objects
        val startDate = dateFormat.parse(startDateStr)
        val endDate = dateFormat.parse(endDateStr)
        // Check if the first date is less than the second date
        if (startDate != null) {
            return startDate.before(endDate)
        }
    } catch (e: Exception) {
        // Handle any parsing exceptions (e.g., if the date format is incorrect)
        e.printStackTrace()
        return false
    }
    return false
}
