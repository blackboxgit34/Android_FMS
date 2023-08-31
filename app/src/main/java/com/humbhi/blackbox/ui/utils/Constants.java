package com.humbhi.blackbox.ui.utils;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Constants {
	public static final int DEVICE_SDK_INT = Build.VERSION.SDK_INT;
	public static final String OPEN_DRAWER_FIRST_TIME="open_drawer";
	public static final boolean DEBUG = false;
	public static final String MOD="mod";
	public static final String MAP_KEY="key";
	public static final String CONTENT = 		"content";
	public static final String FRAGMENT_TAG = 	"fragment_tag";
	public static final String RETURN_MODE="return_mode";
	public static final String PHONE_CALL = "tel:";
	public static final String NOTIFICATION_KEY="notification_key";
	public static final String MESSAGE_ID="message_ID";
	public static final String BILL_ARRAY="bil_array";

	public static final String API_ONE_URL = "http://api1.trackmaster.in/";
	public static final String API_TWO_URL = "http://api1.trackmaster.in/";
	public static final String BASEURL = "http://api1.trackmaster.in/api/";
	public static final String HITECH_BASE = "http://htp2.hitecpoint.in/api/";

	/*todo http://htp2.hitecpoint.in/api/
	* @url used for api*/
	public static final int NewCustomer = Integer.parseInt("13000");

    public static final String GET_BANNER="InvoiceAPI/GetAppBanner?";
    public static final int REQ_GET_BANNER = 100;


    public static final String GET_UPDATE="api/adminapi/UpdateVesrion?";
	public static final int REQ_GET_UPDATE = 102;

	public static final String GET_OCCASION="";
	public static final int REQ_GET_OCCASION = 103;

	public static String LOGIN="user/bbapplogin?";
	public static final int REQ_LOGIN = 104;

	public static String GET_LIVE_STATUS="LiveStatus/LiveStatus?";
	public static final int REQ_GET_LIVE_STATUS = 105;

	public static final String LOGOUT="appapi/LogOutforAndroid?";
	public static final int REQ_LOGOUT = 106;

	public static String VEHICLE_COUNT = "livestatus/VehiclesStatusGraph?";
	public static final int REQ_VEHICLE_COUNT = 107;

	public static String DAILY_REPORT = "Report/DailyNew?";
	public static final int REQ_DAILY_REPORT = 108;

	public static String DISTANCE_REPORT = "AddOnAPI/DistanceReport?";
	public static final int REQ_DISTANCE_REPORT = 109;

	public static String OVER_SPEEDREPORT = "Reportsapi/GetOverSpeedReport?";
	public static final int REQ_OVER_SPEEDREPORT = 110;

	public static String MONTHLY_REPORT = "Reportsapi/GetMonthlyReport?";
	public static final int REQ_MONTHLY_REPORT = 111;

	public static String LOCATION_ON_MAP = "blackbox/live?";
	public static final int REQ_LOCATION_ON_MAP = 112;

	public  static  String DRIVERS_LIST = "ReportsApi/DrBgetDriverName?";
	public static final int REQ_DRIVERS_LIST= 139;

	public static String DETAIL_SUMMARY_REPORT = "Report/DailyNew?";
	public static final int REQ_DETAIL_SUMMARY_REPORT = 113;

	public static String STOPPAGE_REPORT = "Reportsapi/GetallstoppageReport?";
	public static final int REQ_STOPPAGE_REPORT = 114;
	public static final int REQ_STOPPAGE_GRAPH_REPORT = 1144;

	public static String OVER_STOPPAGE_REPORT = "Reportsapi/GetOverStoppageReport?";
	public static final int REQ_OVER_STOPPAGE_REPORT = 115;

	public static String TEMP_REPORT = "Reportsapi/GetRefTempReport?";
	public static final int REQ_TEMP_REPORT = 116;

	public static String GET_ALL_VEHICLES = "Blackbox/Vehicles?";
	public static final int REQ_GET_ALL_VEHICLES = 117;

	public static String GET_COMPLAINT_TYPE = "blackbox/GetComplaintDescription";
	public static final int REQ_GET_COMPLAINT_TYPE = 118;

	public static String GET_ADD_SERVICE_REQUEST = "BlackBox/AddServiceRequest?";
	public static final int REQ_ADD_SERVICE_REQUEST = 119;

	public static String GET_CUSTOMER_CARE = "CommonApi/GetHelpLineNumber?";
	public static final int REQ_GET_CUSTOMER_CARE = 120;

	public static String GET_CHECK_PASSWORD = "FMSAPI/GetPasswordChecked?";
	public static final int REQ_GET_CHECK_PASSWORD = 121;

	public static String GET_ALLVEHICLES_ALERTS = "blackbox/blackboxnotifications/?";
	public static final int REQ_GET_ALLVEHICLES_ALERTS = 122;

	public static String SET_IGNITION_ALERT = "blackbox/ActiveBlackBoxNotificationsNew/?";
	public static final int REQ_SET_IGNITION_ALERT = 123;

	public static String GET_NEW_BILL = "InvoiceAPI/GetAccountSummary?";
	public static final int REQ_GET_NEW_BILL = 124;

	public static String GET_OLD_BILL = "InvoiceAPI/GetAccountSummaryOld?";
	public static final int REQ_GET_OLD_BILL = 125;

	public static String GET_BILLING_DETAILS = "InvoiceAPI/GetInitPayDetails?";
	public static final int REQ_GET_BILLING_DETAILS = 126;

	public static String GET_ROUTE_PLAYBACK = "MapAPI/GetPlayBackDataResult?";
	public static final int REQ_GET_ROUTE_PLAYBACK = 127;

	public static String GET_NOTIFICATION_LIST = "AppApi/GetNotificationApp?";
	public static final int REQ_GET_NOTIFICATION_LIST = 128;

	public static String GET_DEL_NOTIFICATION = "pumas/PumaDeleteNotification?";
	public static final int REQ_GET_DEL_NOTIFICATION = 129;

	public static String GET_ROUTE_STOPPAGES = "MapAPI/RouteHaltsDataforapp?";
	public static final int REQ_GET_ROUTE_STOPPAGES = 130;

	public static String GET_IMMOBILIZE_VEHICLE="BlackboxInstallation/Getimmoblizerveh?";
	public static final int REQ_GET_IMMOBILIZE_VEHICLE = 131;

	public static String SET_IMMOBILIZE_VEHICLE="BlackboxInstallation/updateimmoblizer?";
	public static final int REQ_SET_IMMOBILIZE_VEHICLE = 132;


	public static String LOCATION_ON_MAP_NEW = "AppApi/Livestatus?";
	public static final int REQ_LOCATION_ON_MAP_NEW = 112;


	public static String FUEL_GRAPH_API = "CustomAPI/FuelDataGraph?";
	public static final int REQ_FUEL_GRAPH_API = 134;

	public static String FUEL_TANK_DETAILS_API = "CustomAPI/CurrentFuelData?";
	public static final int REQ_FUEL_TANK_DETAILS_API = 135;


	public static String GET_ALL_VEHICLE_ICONS = "AppApi/GetVehicleType";
	public static final int REQ_GET_ALL_VEHICLE_ICONS = 136;

	public static String GET_SAFE_DRIVERS = "ReportsApi/GetDrBSafeDriverReport?";
	public static final int REQ_GET_SAFE_DRIVERS = 137;

	public static String VEHICLES_ON_MAP = "AppApi/VehicleonmapLiveStatus?";
	public static final int REQ_VEHICLES_ON_MAP = 138;

	public static String VEHICLES = "CommonApi/GetFuelVehicles?";
	public static final int REQ_VEHICLES = 139;

	public static String FORGOT_PASSWORD = "CommonApi/VerifyUser?";
	public static final int REQ_FORGOT_PASSWORD = 140;

	public static String EXPIRE_ACCOUNT_DETAILS = "AppApi/GetUserDetailsMainAccount?";
	public static final int REQ_EXPIRE_ACCOUNT_DETAILS = 141;

	public static String GET_UPDATE_NOTIFICATION = "pumas/PumaUpdateNotificationStatus?";
	public static final int REQ_GET_UPDATE_NOTIFICATION = 142;

	public static String GET_UPDATE_APP_VERSION = "TaxiAPI/updateandgetversion?";
	public static final int REQ_GET_UPDATE_APP_VERSION = 143;


	public static boolean checkActivation(Context context) {

		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

		List<ActivityManager.RunningTaskInfo> services = activityManager.getRunningTasks(Integer.MAX_VALUE);

		boolean isActivityFound = false;

		if (services.get(0).topActivity.getPackageName().toString().equalsIgnoreCase(context.getPackageName().toString())) {
			isActivityFound = true;
		}

		Log.e(" FCM ", "Activity open: " + isActivityFound);  // true  foreground n false background

		return isActivityFound;
	}

	public static void alertDialog(Context context, String msg) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle("Blackbox");
		AlertDialog dialog_card = alertDialogBuilder.create();
		Window window = dialog_card.getWindow();
		window.setGravity(Gravity.CENTER);

		alertDialogBuilder.setMessage(msg);
		alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				arg0.dismiss();
			}
		});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public static void Toastmsg(Context context, String msg) {
		Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
	}

	public static void alertWithIntent(final Context context, String msg, final Class className) {

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
		alertDialogBuilder.setTitle("Blackbox");
		alertDialogBuilder.setMessage(msg);
		alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface arg0, int arg1)
			{
				arg0.dismiss();
				Intent intent=new Intent(context,className);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				context.startActivity(intent);

			}
		});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();

	}

	public static String dateAndTime(String inputDateString) {
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
		SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
		try {
			Date date = inputFormat.parse(inputDateString);
			if (date != null) {
				return outputFormat.format(date);
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date dateAndTimeInDateFormat(String inputDateString) {
		SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());
		SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
		try {
			Date date = inputFormat.parse(inputDateString);
			if (date != null) {
				return date;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void hideKeyboard(Context context, View view)
	{
		InputMethodManager inputManager = (InputMethodManager)
				context.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (view!=null)
		{
			inputManager.hideSoftInputFromWindow(view.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

}
