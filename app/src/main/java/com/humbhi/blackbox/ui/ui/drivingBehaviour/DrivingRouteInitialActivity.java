package com.humbhi.blackbox.ui.ui.drivingBehaviour;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.humbhi.blackbox.R;
import com.humbhi.blackbox.databinding.ActivityDrivingBehaviourBinding;
import com.humbhi.blackbox.databinding.ActivityDrivingRouteInitialBinding;
import com.humbhi.blackbox.ui.adapters.CustSpinnerAdapter;
import com.humbhi.blackbox.ui.data.db.CommonData;
import com.humbhi.blackbox.ui.data.models.AllVehicleModel;
import com.humbhi.blackbox.ui.retofit.Retrofit2;
import com.humbhi.blackbox.ui.retofit.RetrofitResponse;
import com.humbhi.blackbox.ui.ui.routePlayback.RoutePlayBack;
import com.humbhi.blackbox.ui.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class DrivingRouteInitialActivity extends AppCompatActivity implements RetrofitResponse, View.OnClickListener {

    private ActivityDrivingRouteInitialBinding binding;
    ArrayList<AllVehicleModel> vehicleModel = new ArrayList<>();
    ArrayList<String>vehicleList = new ArrayList<>();
    String vehicleId = "",vehicleName = "",backendStartDate,backendEndDate,startTime="",endTime="";
    DatePicker picker;
    int hour,minute,second;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDrivingRouteInitialBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setToolbar();
        getAllVehicles();
    }


    private void setToolbar(){
        binding.toolbar.ivBell.setVisibility(View.GONE);
        binding.toolbar.ivMenu.setVisibility(View.GONE);
        binding.toolbar.ivBack.setVisibility(View.VISIBLE);
        binding.toolbar.tvTitle.setVisibility(View.VISIBLE);
        binding.toolbar.tvTitle.setText("Driving Behaviour");
        binding.toolbar.ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

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
        binding.spVehicles.setThreshold(0); //will start working from first character
        binding.spVehicles.setAdapter(CustSpinnerAdapter.getAdapter(this, vehicleList)); //setting the adapter data into the AutoCompleteTextView
        binding.spVehicles.setOnItemClickListener((parent, view, position, id) -> {
            String selection = (String) parent.getItemAtPosition(position);
            int pos = -1;

            for (int i = 0; i < vehicleList.size(); i++) {
                if (vehicleList.get(i).equals(selection)) {
                    pos = i;
                    break;
                }
            }
            vehicleId = vehicleModel.get(pos).getBbid();
            vehicleName = vehicleModel.get(pos).getVehname();
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
                            spinVehicles();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
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
                 binding.customDate.setVisibility(View.VISIBLE);
//                }
//                else
//                {
//                    binding.customDate.setVisibility(View.VISIBLE);
//                }
                c = Calendar.getInstance();
                df = new SimpleDateFormat("yyyy-MM-dd");
                c.add(Calendar.DATE, -1);
                formattedDate = df.format(c.getTime());
                backendStartDate = formattedDate;
                backendEndDate = formattedDate;
                binding.tvStartDate.setText(backendStartDate);
                binding.tvEndDate.setText(backendEndDate);

                break;
            case R.id.btToday:
//                if (binding.customDate.isShown())
//                {
                  binding.customDate.setVisibility(View.VISIBLE);
//                }
//                else
//                {
//                    binding.customDate.setVisibility(View.VISIBLE);
//                }
                c = Calendar.getInstance();
                df = new SimpleDateFormat("yyyy-MM-dd");
                formattedDate = df.format(c.getTime());
                backendStartDate = formattedDate;
                backendEndDate = formattedDate;
                binding.tvStartDate.setText(backendStartDate);
                binding.tvEndDate.setText(backendEndDate);

                break;
            case R.id.btCustom:
//                if (binding.customDate.isShown())
//                {
//                    binding.customDate.setVisibility(View.GONE);
//                }
//                else
//                {
                    binding.customDate.setVisibility(View.VISIBLE);
        //        }
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
                    Intent intent = new Intent(this, RoutePlayBack.class);
                    intent.putExtra("tableName",vehicleId);
                    intent.putExtra("fromDate",backendStartDate);
                    intent.putExtra("endDate",backendEndDate);
                    intent.putExtra("vehicleName",vehicleName);
                    intent.putExtra("flag","DrivingBehaveRoute");
                    if(!startTime.equals("")){
                        intent.putExtra("startTime",startTime);
                    }
                    if( !endTime.equals("")){
                        intent.putExtra("endTime",endTime);
                    }
                    if (binding.switch1.isChecked()){
                        intent.putExtra("showStoppages","1");
                    }
                    else {
                        intent.putExtra("showStoppages","0");
                    }
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
                new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute)
                    {
                        int hour = hourOfDay % 12;
                        binding.tvEndTime.setText(String.format(Locale.ENGLISH,"%02d:%02d %s", hour == 0 ? 12 : hour, minute, hourOfDay < 12 ? "AM" : "PM"));
                        endTime = hourOfDay + ":" + minute;
                    }
                }, hour, minute, false);
        timePickerDialog.show();
    }

    private void startTime()
    {
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener()
                {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute)
                    {
                        int hour = hourOfDay % 12;
                        binding.tvStartTime.setText(String.format(Locale.ENGLISH,"%02d:%02d %s", hour == 0 ? 12 : hour, minute, hourOfDay < 12 ? "AM" : "PM"));
                        startTime=hourOfDay + ":" + minute;
                    }
                }, hour, minute, false);
        timePickerDialog.show();

    }
}