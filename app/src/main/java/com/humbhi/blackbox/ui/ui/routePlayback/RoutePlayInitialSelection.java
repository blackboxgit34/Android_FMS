package com.humbhi.blackbox.ui.ui.routePlayback;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;

import com.humbhi.blackbox.R;
import com.humbhi.blackbox.databinding.ActivityRoutePlayInitialSelectionBinding;
import com.humbhi.blackbox.ui.MyApplication;
import com.humbhi.blackbox.ui.Utility.WhatsNewDialogFragment;
import com.humbhi.blackbox.ui.adapters.SearchableAdapter;
import com.humbhi.blackbox.ui.data.AisModel;
import com.humbhi.blackbox.ui.data.Table;
import com.humbhi.blackbox.ui.data.db.CommonData;
import com.humbhi.blackbox.ui.data.models.AllVehicleModel;
import com.humbhi.blackbox.ui.retofit.NetworkService;
import com.humbhi.blackbox.ui.retofit.NewRetrofitClient;
import com.humbhi.blackbox.ui.retofit.Retrofit2;
import com.humbhi.blackbox.ui.retofit.RetrofitResponse;
import com.humbhi.blackbox.ui.ui.banner.BillBanner;
import com.humbhi.blackbox.ui.ui.dashboard.DashboardActivity;
import com.humbhi.blackbox.ui.ui.livetracking.GLocationOnMap;
import com.humbhi.blackbox.ui.utils.Constants;
import com.humbhi.blackbox.ui.utils.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoutePlayInitialSelection extends AppCompatActivity implements RetrofitResponse , View.OnClickListener {

    private ActivityRoutePlayInitialSelectionBinding binding;
    ArrayList<AllVehicleModel> vehicleModel = new ArrayList<>();
    ArrayList<String>vehicleList = new ArrayList<>();
    String vehicleId = "",vehicleName = "",backendStartDate,backendEndDate,startTime="",endTime="";
    DatePicker picker;
    ArrayAdapter<String> adapter;
    int hour,minute,second;
    String SoftBanner = "";
    String hardBanner = "";
    int aisCount = 0;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRoutePlayInitialSelectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setToolbar();
        getAllVehicles();
    }

    private void setToolbar(){
        binding.toolbar.ivBell.setVisibility(View.GONE);
        binding.toolbar.ivMenu.setVisibility(View.GONE);
        binding.toolbar.ivBack.setVisibility(View.VISIBLE);
        binding.toolbar.tvTitle.setVisibility(View.VISIBLE);
        binding.toolbar.tvTitle.setText("Route Playback");
        binding.toolbar.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RoutePlayInitialSelection.this, DashboardActivity.class);
                startActivity(intent);
                finish();
                if (Network.isNetworkAvailable(RoutePlayInitialSelection.this)) {
                    new Retrofit2(RoutePlayInitialSelection.this, RoutePlayInitialSelection.this,
                            Constants.REQ_EXPIRE_ACCOUNT_DETAILS,
                            Constants.EXPIRE_ACCOUNT_DETAILS
                                    + "custid=" + CommonData.INSTANCE.getCustIdFromDB()).callService(false);
                }
            }
        });
        if(MyApplication.cantSkip.equals("yes")){
            getAisData();
        }
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        String formattedDate = df.format(c.getTime());
        backendStartDate = formattedDate;
        backendEndDate = formattedDate;
        binding.tvStartTime.setText("12:00 AM");
        binding.tvEndTime.setText("11:59 PM");
        startTime = "00:00";
        endTime = "23:59";
        binding.tvStartDate.setText(backendStartDate);
        binding.tvEndDate.setText(backendEndDate);
        binding.bt24Hour.setOnClickListener(this);
        binding.btToday.setOnClickListener(this);
        binding.btCustom.setOnClickListener(this);
        binding.tvStartDate.setOnClickListener(this);
        binding.tvEndDate.setOnClickListener(this);
        binding.tvStartTime.setOnClickListener(this);
        binding.tvEndTime.setOnClickListener(this);
        binding.btnGetRoute.setOnClickListener(this);
    }

    private void getAisData() {
        NetworkService Api = NewRetrofitClient.INSTANCE.getInstance().create(NetworkService.class);
        executor.execute(()->{
            Api.getAis140VehicleStatus(CommonData.INSTANCE.getCustIdFromDB()).enqueue(new Callback<AisModel>() {
                @Override
                public void onResponse(Call<AisModel> call, Response<AisModel> response) {
                    handleAisDataResponse(response);
                }

                @Override
                public void onFailure(Call<AisModel> call, Throwable t) {
                    String errorMessage;
                    if (t instanceof ConnectException || t instanceof UnknownHostException) {
                        errorMessage = String.valueOf(R.string.no_network);
                    } else if (t instanceof SocketTimeoutException) {
                        errorMessage = String.valueOf(R.string.time_out);
                    } else if (t instanceof CancellationException || t instanceof SocketException) {
                        errorMessage = String.valueOf(Integer.parseInt(null)); // Handle network loss
                    } else {
                        errorMessage = String.valueOf(R.string.something_went_wrong);
                    }

                    if (errorMessage != null) {
                        int finalErrorMessage = Integer.parseInt(errorMessage);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Constants.Toastmsg(RoutePlayInitialSelection.this, getString(finalErrorMessage));
                            }
                        });
                    }
                }
            });
        });
    }

   private void handleAisDataResponse(Response<AisModel> response) {
        if (response != null) {
            if (response.isSuccessful()) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        AisModel responseFromBody = response.body();
                        List<Table> tableList = responseFromBody.getTable();
                        Table vehicleWithLeastValidity = null;
                        for (Table tableEntity : tableList) {
                            if (vehicleWithLeastValidity == null || tableEntity.getExpireIndays() < vehicleWithLeastValidity.getExpireIndays()) {
                                vehicleWithLeastValidity = tableEntity;
                            }
                        }
                        String expiry = "";
                        if (vehicleWithLeastValidity != null) {
                            int expireIndays = vehicleWithLeastValidity.getExpireIndays();
                            String paymentStatus = vehicleWithLeastValidity.getPaymentStatus();
                            if (expireIndays >= 29 && expireIndays <= 9 && paymentStatus.equals("Not Paid")) {
                                expiry = "attentionAlert";
                            } else if (expireIndays >= 9 && expireIndays <= 4 && paymentStatus.equals("Not Paid")) {
                                expiry = "justAlert";
                            } else if (expireIndays >= 4 && expireIndays <= 0 && paymentStatus.equals("Not Paid")) {
                                expiry = "expiringToday";
                            } else if (expireIndays < 0 && paymentStatus.equals("Not Paid")) {
                                MyApplication.cantSkip = "yes";
                                expiry = "expired";
                            }
                        }
                        if (!isFinishing()) {
                            WhatsNewDialogFragment dialogFragment = new WhatsNewDialogFragment(RoutePlayInitialSelection.this, expiry, Objects.requireNonNull(vehicleWithLeastValidity.getCommercialvalidity()), Objects.requireNonNull(vehicleWithLeastValidity.getVehname()));
                            dialogFragment.show(getSupportFragmentManager(), "WhatsNewDialog");
                        }
                    }
                });
            }
        }
    }

    private void getAllVehicles()
    {
        new Retrofit2(this, this, Constants.REQ_LOCATION_ON_MAP
                ,Constants.LOCATION_ON_MAP+"id="+ CommonData.INSTANCE.getCustIdFromDB())
                .callServicehitec(true);
    }

    /*
     * Spinner CustId
     * */
    public void spinVehicles()
    {
        SearchableAdapter adapter = new SearchableAdapter(this, vehicleList);
        binding.spVehicles.setAdapter(adapter);
        binding.spVehicles.setOnItemClickListener((parent, view, position, id) -> {
            ArrayList<String> originalData = adapter.getOriginalData();
            String selection = originalData.get(position);
            int originalPosition = adapter.getOriginalPosition(position);
            if (originalPosition != -1) {
                // Use the original position to retrieve the corresponding item
                vehicleId = vehicleModel.get(originalPosition).getBbid();
                vehicleName = vehicleModel.get(originalPosition).getVehname();
            }
        });

        binding.spVehicles.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus)
                binding.spVehicles.showDropDown();
        });

        binding.spVehicles.setOnTouchListener((v, event) -> {
            binding.spVehicles.showDropDown();
            return false;
        });

    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {
        switch (requestCode)
        {
            case Constants.REQ_LOCATION_ON_MAP:
                if (response.isSuccessful())
                {
                    try
                    {
                        vehicleList.clear();
                       // vehicleList.add(0,"Select Vehicle");
                        JSONArray data = new JSONArray(response.body().string());
                        for (int i = 0; i <data.length() ; i++) {
                            JSONObject obj = data.getJSONObject(i);
                            AllVehicleModel model = new AllVehicleModel();
                            model.setBbid(obj.getString("BoxId"));
                            model.setVehname(obj.getString("VehName"));
                            vehicleList.add(obj.getString("VehName"));
                            vehicleModel.add(model);
                        }
                        spinVehicles();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case Constants.REQ_EXPIRE_ACCOUNT_DETAILS:
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject result = new JSONObject(response.body().string());
                            JSONArray table = result.getJSONArray("Table");
                            for (int i = 0; i < table.length(); i++) {
                                JSONObject jsonObject = table.getJSONObject(0);
                                SoftBanner = jsonObject.getString("SoftBanner");
                                hardBanner = jsonObject.getString("hardBanner");
                                aisCount = Integer.parseInt(jsonObject.getString("Ais140Count"));
                            }
                            CommonData.INSTANCE.setAisCount(String.valueOf(aisCount));

                            // Perform operations based on SoftBanner and hardBanner values
                            if (!CommonData.INSTANCE.getSkip().equals("yes")) {
                                if (!"false".equals(SoftBanner)) {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Intent intent = new Intent(RoutePlayInitialSelection.this, BillBanner.class);
                                            intent.putExtra("SoftBanner", SoftBanner);
                                            intent.putExtra("hardBanner", hardBanner);
                                            startActivity(intent);
                                            finish();
                                        }
                                    });
                                }
                            }
                            if (!"false".equals(hardBanner)) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent intent = new Intent(RoutePlayInitialSelection.this, BillBanner.class);
                                        intent.putExtra("SoftBanner", SoftBanner);
                                        intent.putExtra("hardBanner", hardBanner);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                        } catch (IOException e) {
                        }
                    }
                });
                break;
        }
    }

    @Override
    public void onClick(View view) {
        Calendar c;
        SimpleDateFormat df;
        String formattedDate;
        switch (view.getId())
        {
            case R.id.bt24Hour:
//                if (binding.customDate.isShown())
//                {
//                    binding.customDate.setVisibility(View.GONE);
//                }
//                else
//                {
                    binding.customDate.setVisibility(View.GONE);
              //  }
                c = Calendar.getInstance();
                df = new SimpleDateFormat("yyyy-MM-dd");
                c.add(Calendar.DATE, -1);
                formattedDate = df.format(c.getTime());
                backendStartDate = formattedDate;
                backendEndDate = formattedDate;
                binding.tvStartDate.setText(backendStartDate);
                binding.tvEndDate.setText(backendEndDate);
                if (validation()){
                    Intent intent = new Intent(this,RoutePlayBack.class);
                    intent.putExtra("tableName",vehicleId);
                    intent.putExtra("fromDate",backendStartDate);
                    intent.putExtra("endDate",backendEndDate);
                    intent.putExtra("vehicleName",vehicleName);
                    intent.putExtra("flag","simple");
                    if(!startTime.equals("")){
                        intent.putExtra("startTime",startTime);
                    }
                    if( !endTime.equals("")){
                        intent.putExtra("endTime",endTime);
                    }
//                    if (binding.switch1.isChecked()){
//                        intent.putExtra("showStoppages","1");
//                    }
//                    else {
//                        intent.putExtra("showStoppages","0");
//                    }
                    startActivity(intent);
                }
                break;
            case R.id.btToday:
//                if (binding.customDate.isShown())
//                {
//                    binding.customDate.setVisibility(View.GONE);
//                }
//                else
//                {
                binding.customDate.setVisibility(View.GONE);
                //}
                c = Calendar.getInstance();
                df = new SimpleDateFormat("yyyy-MM-dd");
                formattedDate = df.format(c.getTime());
                backendStartDate = formattedDate;
                backendEndDate = formattedDate;
                binding.tvStartDate.setText(backendStartDate);
                binding.tvEndDate.setText(backendEndDate);
                if (validation()){
                    Intent intent = new Intent(this,RoutePlayBack.class);
                    intent.putExtra("tableName",vehicleId);
                    intent.putExtra("fromDate",backendStartDate);
                    intent.putExtra("endDate",backendEndDate);
                    intent.putExtra("vehicleName",vehicleName);
                    intent.putExtra("flag","simple");
                    if(!startTime.equals("")){
                        intent.putExtra("startTime",startTime);
                    }
                    if( !endTime.equals("")){
                        intent.putExtra("endTime",endTime);
                    }
//                    if (binding.switch1.isChecked()){
//                        intent.putExtra("showStoppages","1");
//                    }
//                    else {
//                        intent.putExtra("showStoppages","0");
//                    }
                    startActivity(intent);
                }
                break;
            case R.id.btCustom:
//                if (binding.customDate.isShown())
//                {
//                    binding.customDate.setVisibility(View.GONE);
//                }
//                else
//                {
                    binding.customDate.setVisibility(View.VISIBLE);
               // }
                break;
            case R.id.tvStartDate:
                datepicker("1");
                break;
            case R.id.tvEndDate:
                datepicker("2");
                break;
            case R.id.tvStartTime:
                startTime();
                break;
            case R.id.tvEndTime:
                endTime();
                break;
            case R.id.btnGetRoute:
                if (validation()){
                    Intent intent = new Intent(this,RoutePlayBack.class);
                    intent.putExtra("tableName",vehicleId);
                    intent.putExtra("fromDate",backendStartDate);
                    intent.putExtra("endDate",backendEndDate);
                    intent.putExtra("vehicleName",vehicleName);
                    intent.putExtra("flag","simple");
                    if(!startTime.equals("")){
                        intent.putExtra("startTime",startTime);
                    }
                    if( !endTime.equals("")){
                        intent.putExtra("endTime",endTime);
                    }
//                    if (binding.switch1.isChecked()){
//                        intent.putExtra("showStoppages","1");
//                    }
//                    else {
//                        intent.putExtra("showStoppages","0");
//                    }
                    startActivity(intent);
                }
                break;
        }
    }
    /*
     *  Validations
     * */
    private boolean validation()
    {

        boolean check = true;
        if (vehicleId.equals(""))
        {
            Constants.alertDialog(this, "Please select vehicle.");
            check = false;
        }
        else if (binding.tvStartDate.getText().toString().equals("Start Date"))
        {
            Constants.alertDialog(this, "Please select start date.");
            check = false;
        }
        else if (binding.tvEndDate.getText().toString().equals("End Date"))
        {
            Constants.alertDialog(this, "Please select end date.");
            check = false;
        }
        return check;
    }
    // valid upto date picker
    private void datepicker(final String flag)
    {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 0); // to get back 13 year add -13
        Date previous_year = cal.getTime();

        final java.util.Calendar calendar = java.util.Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String x, y;
                if (monthOfYear < 9) {
                    monthOfYear = monthOfYear + 1;
                    x = "0" + monthOfYear;
                } else {
                    x = String.valueOf(monthOfYear + 1);
                }
                if (dayOfMonth < 10) {
                    y = "0" + dayOfMonth;
                } else {
                    y = String.valueOf(dayOfMonth);
                }

                if (flag.equals("1"))
                {
                    backendStartDate =String.valueOf(year)+ "-" + x + "-" + y ;
                    binding.tvStartDate.setText(String.valueOf(year) + "-" + x + "-" + y);
                }
                if (flag.equals("2"))
                {
                    backendEndDate =String.valueOf(year)+ "-" + x + "-" + y ;
                    binding.tvEndDate.setText(String.valueOf(year) + "-" + x + "-" + y);
                }
            }
        }, calendar.get(java.util.Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(java.util.Calendar.DAY_OF_MONTH));

        try
        {
            picker = datePickerDialog.getDatePicker();
            picker.setMaxDate(previous_year.getTime());
        } catch (Exception e) {
            picker = datePickerDialog.getDatePicker();
        }
        datePickerDialog.show();
    }


    private void endTime()
    {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    int hour = hourOfDay % 12;
                    binding.tvEndTime.setText(String.format("%02d:%02d %s", hour == 0 ? 12 : hour, minute, hourOfDay < 12 ? "AM" : "PM"));
                    endTime = hourOfDay + ":" + minute;
                }, hour, minute, false);
        timePickerDialog.show();
    }

    private void startTime()
    {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute) -> {
                    int hour = hourOfDay % 12;
                    binding.tvStartTime.setText(String.format("%02d:%02d %s", hour == 0 ? 12 : hour, minute, hourOfDay < 12 ? "AM" : "PM"));
                    startTime=hourOfDay + ":" + minute;
                }, hour, minute, false);
        timePickerDialog.show();
    }
    @Override
    public void onBackPressed() {
        // Do nothing to prevent going back
    }
}