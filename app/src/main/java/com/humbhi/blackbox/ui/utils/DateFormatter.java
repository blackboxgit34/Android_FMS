/**
 * Copyright (c) Humbhi LLC. 2011 All Rights Reserved
 * Author: Ben Murphy
 * Created: Dec 1, 2011
 */

package com.humbhi.blackbox.ui.utils;

import android.text.TextUtils;
import android.util.SparseArray;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateFormatter {
	
	/** Default DB date time format | yyyy-MM-dd HH:mm:ss | 2013-03-28 14:32:42 */
	public static final String FORMAT_DB = "yyyy-MM-dd HH:mm:ss";
	
	/** Human readable date time format | EEE, MMM dd, h:mm a | Thu, Mar 28, 2:32 PM */
	public static final String FORMAT_USER_FRIENDLY = "EEE, MMM dd, h:mm a";
	
	/** Human readable date time format | EEE, MMM dd, h:mm a | Thu, Mar 28 2013, 2:32 PM */
	public static final String FORMAT_USER_FRIENDLY_DATE_TIME = "EEE, MMM dd yyyy, h:mm a";

	/** Human readable date time format | EEE, MMM dd, h:mm a | Thu, Mar 28 2013, 2:32 PM */
	public static final String FORMAT_USER_FRIENDLY_DATE_TIME_1 = "MMM dd yyyy, hh:mm a";

	/** Human readable date format just date | EEE, MMM dd, yyyy | Thu, Mar 28, 2013 */
	public static final String FORMAT_USER_FRIENDLY_DATE_ONLY = "EEE, MMM dd, yyyy";
	
	/** Human readable date format just date | EEE dd, yyyy | Mar 28, 2013 */
	public static final String FORMAT_USER_FRIENDLY_DATE_ONLY_1 = "MMM dd, yyyy";
	
	/** Human readable date format just time | h:mm a | 2:32 PM */
	public static final String FORMAT_USER_FRIENDLY_TIME_ONLY = "h:mm a";
	
	/** Human readable date format just time | HH:mm | 14:32 */
	public static final String FORMAT_USER_FRIENDLY_TIME_ONLY_24 = "HH:mm";
	
	/** Date time format | MM/dd/yyyy hh:mm:ss aa | 03/28/2013 02:32:42 PM */
	public static final String DATE_FORMAT = "MM/dd/yyyy hh:mm:ss aa";
	
	/** Date time format |YYYY,MM| 2014,May */
	public static final String EXPIRY_DATE_FORMAT = "yyyy,MM";
	
	/** Date time format from teh GCM | yyyy-MM-dd hh:mm:ss aa | 2013-03-28 02:32:42 PM */
	public static final String GCM_INTENT_SERVICE = "yyyy-MM-dd hh:mm:ss aa";
	
	/** Date only | yyyy-MM-dd | 2013-03-28 */
	public static final String FORMAT_DATE_ONLY = "yyyy-MM-dd";
	
	/** Date only | yyyy-MM-dd | 03-28-2016 */
	public static final String FORMAT_DATE_ONLY_1 = "MM-dd-yyyy";
	
	/** Date time format | MM/dd/yyyy HH:mm:ss | 03/28/2013 02:32:42 */
	public static final String DATE_FORMAT_1 = 	"MM/dd/yyyy HH:mm:ss";
	
	/** Date time format | Day MM/dd/yyyy | Wednesday 03-28-2013 */
	public static final String DATE_FORMAT_2 = 	"EEEE dd-MM-yyyy";
	
	/** Date time format | MM/dd/yyyy | 03/28/2013 */
	public static final String SHORT_DATE_FORMAT = 	"MM/dd/yyyy";
	
	/** Date time format | MM/dd/yyyy | 03-Feb-2013 */
	public static final String SHORT_DATE_FORMAT_1 = "dd-MMM-yyyy";
	
	/** Date time format | MM/dd/yyyy | 03-12-2013 */
	public static final String SHORT_DATE_FORMAT_2 = "dd-MM-yyyy";
	
	/** Date time format | HH:mm:ss | 02:32:42 */
	public static final String FORMAT_BLOCK_TIME = "HH:mm:ss";
	
	/** Different ways of showing the timezone in a String date */
	public static class TimeZoneFormat {
		/** Do not show timezone */
		public static final int NONE = 	-1;
		/** Show timezone as | z | "EST" */
		public static final int SHORT = 1;
		/** Show timezone as | zzzz | "Eastern Standard Time" */
		public static final int LONG = 	1<<1;
	}
	private static final SparseArray<String> mTimeZoneFormats; 
	static {
		mTimeZoneFormats = new SparseArray<String>(2);
		mTimeZoneFormats.put(TimeZoneFormat.SHORT, "z");
		mTimeZoneFormats.put(TimeZoneFormat.LONG, "zzzz");
	}
	
	/**
	 * #C 2013_0210 Ben Murphy</br></br>
	 * This will format a <tt>long</tt> value to the given <tt>String</tt> format.
	 * @param format Format of the returned <tt>long</tt> value. Example {@value #FORMAT_DB}
	 * @param dateTime <tt>long</tt> value of a date time can be gotten from <tt>Calendar.getInstance().getTimeInMillis()</tt>
	 * @param timeZone The time zone for the formatter. Send <tt>null</tt> or "" for local time zone.
	 * @param timeZoneFormat The timezone format that you want to use {@link TimeZoneFormat}
	 * @return Formatted date time <tt>String</tt>
	 */
	public static final String getCurrentDateTime(String format, long dateTime, String timeZone, int timeZoneFormat) {
		if (dateTime<1L)
			return null;
		
		if (timeZoneFormat>-1)
			format += " " + mTimeZoneFormats.get(timeZoneFormat);
		
		Date _formatMe = new Date(dateTime);
		SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.US);
		if (!TextUtils.isEmpty(timeZone))
			formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
		
		return formatter.format(_formatMe);
	}
	
	/**
	 * #C 2013_0215 Ben Murphy</br></br>
	 * This will format a <tt>long</tt> value to the given <tt>String</tt> format.
	 * @param format Format of the returned <tt>long</tt> value. Example {@value #FORMAT_DB}
	 * @param dateTime <tt>long</tt> value of a date time can be gotten from <tt>Calendar.getInstance().getTimeInMillis()</tt>
	 * @param addDuration <tt>int</tt> value of minutes to add to the time
	 * @param timeZone The time zone for the formatter. Send <tt>null</tt> or "" for local time zone.
	 * @return Formatted date time <tt>String</tt>
	 */
	public static final String getCurrentDateTime(String format, long dateTime, int addDuration, String timeZone, int timeZoneFormat) {
		if (dateTime<1L)
			return null;
		
		if (timeZoneFormat>-1)
			format += " " + mTimeZoneFormats.get(timeZoneFormat);
		
		// Might use this later
//		Calendar cal = Calendar.getInstance(TextUtils.isEmpty(timeZone) ? TimeZone.getDefault() : TimeZone.getTimeZone(timeZone));
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(dateTime);
		cal.add(Calendar.MINUTE, addDuration);
		SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.US);
		if (!TextUtils.isEmpty(timeZone))
			formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
		
		return formatter.format(cal.getTime());
	}
	
	/**
	 * #Created May 22, 2013 Ben Murphy<br><br>
	 * This will take any long form of time and add the duration to it.
	 * @param dateTime
	 * @param addDuration
	 * @return
	 */
	public static final long getCurrentDateTime(long dateTime, int addDuration) {
		if (dateTime<1L)
			return -1L;

		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(dateTime);
		cal.add(Calendar.MINUTE, addDuration);
		
		return cal.getTimeInMillis();
	}
	
	/**
	 * #C 2013_0210 Ben Murphy</br></br>
	 * This will format current date time to the given <tt>String</tt> format.
	 * @param format Format of the returned current date time on the device. Example {@value #FORMAT_DB}
	 * @param timeZone The time zone for the formatter. Send <tt>null</tt> or "" for local time zone.
	 * @return Formatted date time <tt>String</tt>
	 */
	public static final String getCurrentDateTime(String format, String timeZone, int timeZoneFormat) {
		Calendar cal = Calendar.getInstance();
		
		if (timeZoneFormat>-1)
			format += " " + mTimeZoneFormats.get(timeZoneFormat);
		
		SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.US);
		if (!TextUtils.isEmpty(timeZone))
			formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
		
		return formatter.format(cal.getTime());
	}
	
	/**
	 * #C 2013_0416 Ben Murphy</br></br>
	 * Will format the current time to the nearest 15 minutes
	 * @return The <tt>long</tt> value of the current date and time to the nearest 15 minutes.
	 */
	public static final long getCurrentDateTimeNearest15Min() {
		Calendar cal = Calendar.getInstance();
		
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		int[] adjustTime = getNearest15(cal.get(Calendar.MINUTE));
		
		cal.set(Calendar.MINUTE, adjustTime[0]);
		cal.add(Calendar.HOUR_OF_DAY, adjustTime[1]);
		
		return cal.getTimeInMillis();
	}
	
	private static int[] getNearest15(int min) {
		final int[] result = new int[2];
		if (min<8) {
			result[0] = 0;
			result[1] = 0;
			return result;
		}
		if (min>7 && min<=22) {
			result[0] = 15;
			result[1] = 0;
			return result;
		}
		if (min>21 && min<38) {
			result[0] = 30;
			result[1] = 0;
			return result;
		}
		if (min>37 && min<52) {
			result[0] = 45;
			result[1] = 0;
			return result;
		}

		result[0] = 0;
		result[1] = 1;
		return result;
	}
	
	/**
	 * Used to get the next 15 minute block compared to the current time
	 * @return
	 */
	public static final long getNext15Minutes() {
		Calendar cal = Calendar.getInstance();
		
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		
		int[] adjustTime = getNext15(cal.get(Calendar.MINUTE));
		
		cal.set(Calendar.MINUTE, adjustTime[0]);
		cal.add(Calendar.HOUR_OF_DAY, adjustTime[1]);
		
		return cal.getTimeInMillis();
	}
	
	private static int[] getNext15(int min) {
		final int[] result = new int[2];
		if (min==0) {
			result[0] = 15;
			result[1] = 0;
			return result;
		}
		if (min>0 && min<=15) {
			result[0] = 30;
			result[1] = 0;
			return result;
		}
		if (min>15 && min<=30) {
			result[0] = 45;
			result[1] = 0;
			return result;
		}
		if (min>30 && min<=45) {
			result[0] = 0;
			result[1] = 1;
			return result;
		}
		if (min>45 && min<59) {
			result[0] = 15;
			result[1] = 1;
			return result;
		}

		result[0] = 0;
		result[1] = 1;
		return result;
	}
	
	/**
	 * #C 2013_0218 Ben Murphy</br></br>
	 * This will convert a <tt>String</tt> value of a date and time to the <tt>long</tt> value of that date and time
	 * @param format The current format of the <tt>String</tt> date and time value
	 * @param dateTime The <tt>String</tt> date and time value
	 * @param timeZone The time zone for the formatter. Send <tt>null</tt> or "" for local time zone.
	 * @return The <tt>long</tt> value of the <tt>String</tt> date and time that was passed into this method.
	 */
	public static final long convertDateTime(String format, String dateTime, String timeZone) {
		if (TextUtils.isEmpty(dateTime))
			return 0L;
		
		Date result = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.US);
			if (!TextUtils.isEmpty(timeZone))
				formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
			
			result = formatter.parse(dateTime);
		} catch (Exception e) {
			e.printStackTrace();
			return 0L;
		}
		
		return result.getTime();
	}
	
	/**
	 * 
	 * @param format
	 * @param dateTime
	 * @return
	 */
	public static final Date convertDateTime(String format, String dateTime) {
		if (TextUtils.isEmpty(dateTime))
			return null;
		
		Date result = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.US);			
			result = formatter.parse(dateTime);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}		
		return result;
	}
	
	/**
	 * #C 2013_0218 Ben Murphy</br></br>
	 * This will convert a <tt>String</tt> value of a date and time to the <tt>long</tt> value of that date and time
	 * @param format The current format of the <tt>String</tt> date and time value
	 * @param dateTime The <tt>String</tt> date and time value
	 * @param addMinutes The int value of minutes you want added to the return long day time 
	 * @param timeZone The time zone for the formatter. Send <tt>null</tt> or "" for local time zone.
	 * @return The <tt>long</tt> value of the <tt>String</tt> date and time that was passed into this method.
	 * @return
	 */
	public static final long convertDateTime(String format, String dateTime, int addMinutes, String timeZone) {
		if (TextUtils.isEmpty(dateTime))
			return 0L;
		
		Date result = null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.US);
			if (!TextUtils.isEmpty(timeZone))
				formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
			
			result = formatter.parse(dateTime);
		} catch (Exception e) {
			e.printStackTrace();
			return 0L;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(result);
		cal.add(Calendar.MINUTE, addMinutes);
		return cal.getTimeInMillis();
	}
	
	/**
	 * #A 2013_0315 Prerna Kapoor</br></br>
	 * @param startDate The <tt>String</tt> instance of the Start date and time value
	 * @param endDate The <tt>String</tt> instance of the End date and time value
	 * @param dateFormats The format of the <tt>startDate</tt> and <tt>endDate</tt>. <b><u>The format must be the same for both</u></b>
	 * @param timeZone The time zone for the formatter. Send <tt>null</tt> or "" for local time zone.
	 * @return <tt>true</tt> if endDate is greater than the startDate, <tt>false</tt> if endDate is not greater than startDate
	 */
	public static final boolean isEndDateGreater(String startDate, String endDate, String dateFormats, String timeZone) {
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(dateFormats, Locale.US);
			if (!TextUtils.isEmpty(timeZone))
				formatter.setTimeZone(TimeZone.getTimeZone(timeZone));
			
			Date start = 	formatter.parse(startDate);
			Date end = 		formatter.parse(endDate);

			if (end.compareTo(start) >= 0)
				return true;
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * #C 2013_0603 Ben Murphy</br></br>
	 * @param start
	 * @param end
	 * @return
	 */
	public static final boolean isSameDay(long start, long end) {
		final Calendar cal1 = Calendar.getInstance();
		final Calendar cal2 = Calendar.getInstance();
		
		cal1.setTimeInMillis(start);
		cal2.setTimeInMillis(end);
		
		if (cal1.get(Calendar.DAY_OF_YEAR)==cal2.get(Calendar.DAY_OF_YEAR))
			return true;

		return false;
		
	}
	
	/**
	 * #C 2013_0918 Sukhdeep Singh</br></br>
	 * @param startDateTime
	 * @return
	 */
	public static boolean isCompareDateTime(long startDateTime){
		long currentDateTime = System.currentTimeMillis();
		
		if(currentDateTime < startDateTime)
			return true;
		
		return false;
	}
	
	/**
	 * 
	 * @param cal
	 * @return
	 */
	public static final long getTenMinuteDifference(long timeInMillis) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(timeInMillis);
		return getTenMinuteDifference(cal).getTimeInMillis();
	}
	
	/**
	 * 
	 * @param cal
	 * @return
	 */
	public static final Calendar getTenMinuteDifference(Calendar cal) {
		int minute = cal.get(Calendar.MINUTE);
		
		if (minute==5 || minute==20 || minute==35 || minute==50)
			cal.add(Calendar.MINUTE, 10);
		
		return cal;
	}
	
	/**
	 * Check is checkTime is in the past compared to checkAgainstPoint
	 * @param checkTime The time to see if it is older than checkAgainstPoint
	 * @param checkAgainstPoint The hard point for comparison
	 * @return False if is in future, true if is in past
	 */
	public static final boolean isTimeInPast(long checkTime, long checkAgainstPoint) {
		if (checkTime>=checkAgainstPoint) {
			return false;
		}
		return true;
	}
} 
