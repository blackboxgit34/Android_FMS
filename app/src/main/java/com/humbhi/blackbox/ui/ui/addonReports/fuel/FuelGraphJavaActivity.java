package com.humbhi.blackbox.ui.ui.addonReports.fuel;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.humbhi.blackbox.R;
import com.humbhi.blackbox.databinding.ActivityFuelGraphJavaBinding;
import com.humbhi.blackbox.ui.adapters.CustSpinnerAdapter;
import com.humbhi.blackbox.ui.data.db.CommonData;
import com.humbhi.blackbox.ui.data.models.AllVehicleModel;
import com.humbhi.blackbox.ui.data.models.FuelGraphModel;
import com.humbhi.blackbox.ui.retofit.Retrofit2;
import com.humbhi.blackbox.ui.retofit.RetrofitResponse;
import com.humbhi.blackbox.ui.ui.routePlayback.RoutePlayBack;
import com.humbhi.blackbox.ui.utils.CommonUtil;
import com.humbhi.blackbox.ui.utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class FuelGraphJavaActivity extends AppCompatActivity implements RetrofitResponse, OnChartGestureListener, OnChartValueSelectedListener, View.OnClickListener {
    private ActivityFuelGraphJavaBinding binding;
    private final ArrayList<String> dates = new ArrayList<>();
    private final ArrayList<Double> amounts = new ArrayList<>();
    private final ArrayList<FuelGraphModel> list = new ArrayList<>();
    ArrayList<AllVehicleModel> vehicleModel = new ArrayList<>();
    ArrayList<String>vehicleList = new ArrayList<>();
    private String  vehicleID = "",currentFuelLevel = "",totalTankCapacity = "",remainingTank = "",fuelTankDateTime = "",backendStartDate,backendEndDate,startTime,endTime;
    DatePicker picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFuelGraphJavaBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setToolbarDetails();

        binding.gauge.setSpeedTextSize(0F);
        binding.gauge.setUnitTextSize(0F);
        String currentDate = CommonUtil.INSTANCE.getCurrentDate();
        backendStartDate = currentDate;
        backendEndDate = currentDate;
        startTime = "00:00";
        endTime = "23:59";
        binding.tvYesterday.setOnClickListener(this);
        binding.tvToday.setOnClickListener(this);
        binding.tvCustom.setOnClickListener(this);
        binding.tvStartDate.setOnClickListener(this);
        binding.tvEndDate.setOnClickListener(this);
        binding.btnAppy.setOnClickListener(this);
        if(getIntent().hasExtra("vehicleId")){
            binding.tvYesterday.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
            binding.tvToday.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
            binding.tvCustom.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
            String[] startDate = getIntent().getStringExtra("startDate").split(" ");
            String[] endDate = getIntent().getStringExtra("endDate").split(" ");
            backendStartDate = startDate[0];
            backendEndDate = endDate[0];
            vehicleID = getIntent().getStringExtra("vehicleId");
            fuelReportApi(vehicleID);
            binding.tvSelectVehicle.setVisibility(View.GONE);
            binding.searchLayout.setVisibility(View.GONE);
        }
        else{
            getAllVehicles();
        }
    }


    private void setToolbarDetails() {
        binding.toolbar.ivBell.setVisibility(View.GONE);
        binding.toolbar.ivMenu.setVisibility(View.GONE);
        binding.toolbar.ivBack.setVisibility(View.VISIBLE);
        binding.toolbar.tvTitle.setVisibility(View.VISIBLE);
        binding.toolbar.tvTitle.setText("Fuel Graph Report");
        binding.toolbar.ivBack.setOnClickListener(view -> finish());
    }


    private void fuelReportApi(String vehicleId) {
        new Retrofit2(this, this, Constants.REQ_FUEL_GRAPH_API
                , Constants.FUEL_GRAPH_API + "vehicleId="+vehicleId+"&beginDate="+backendStartDate+"%2012:00:00%20AM&endDate="+backendEndDate+"%2023:00:00")
                .callService(true);
    }

    private void
    getfuelTankDetails(String vehicleId) {
        new Retrofit2(this, this, Constants.REQ_FUEL_TANK_DETAILS_API
                , Constants.FUEL_TANK_DETAILS_API + "Vehicleid="+vehicleId)
                .callService(true);
    }
    private void getAllVehicles()
    {
        new Retrofit2(this, this, Constants.REQ_VEHICLES
                ,Constants.VEHICLES+"custid="+ CommonData.INSTANCE.getCustIdFromDB())
                .callService(true);

    }
    /*
     * Spinner CustId
     * */
    public void spinVehicles()
    {
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_expandable_list_item_1, vehicleList);
//        CustSpinnerAdapter.getAdapter(this, vehicleList);
        //Getting the instance of AutoCompleteTextView
        binding.spVehicles.setThreshold(0);
        //will start working from first character
        binding.spVehicles.setAdapter(CustSpinnerAdapter.getAdapter(this, vehicleList));//setting the adapter data into the AutoCompleteTextView
        binding.spVehicles.setOnItemClickListener((parent, view, position, id) -> {

            String selection = (String) parent.getItemAtPosition(position);
            int pos = -1;

            for (int i = 0; i < vehicleList.size(); i++) {
                if (vehicleList.get(i).equals(selection)) {
                    pos = i;
                    break;
                }
            }
            vehicleID = vehicleModel.get(pos).getBbid();
            fuelReportApi(vehicleID);
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
        switch (requestCode) {
            case Constants.REQ_FUEL_GRAPH_API:
                if (response.isSuccessful()) {
                    binding.chart1.setVisibility(View.VISIBLE);
                    try {
                        ArrayList<Entry> linedata1 = new ArrayList<>();
                        ArrayList<Entry> linedata2 = new ArrayList<>();
                        ArrayList<Entry> linedata3 = new ArrayList<>();
                        final ArrayList<String> labels1 = new ArrayList<String>();

                        labels1.clear();
                        linedata2.clear();
                        list.clear();
                        JSONArray data = new JSONArray(response.body().string());
                        if (data.length()>0){
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject obj = data.getJSONObject(i);
                                FuelGraphModel model = new FuelGraphModel(obj.getString("Speed"), obj.getString("Distance"), obj.getString("Location"), obj.getString("FuelLevel"),obj.getString("DataDate"));
                                linedata1.add(new Entry(i, (int) Float.parseFloat(obj.getString("FuelLevel"))));
                                linedata2.add(new Entry(i, (int) Float.parseFloat(obj.getString("Speed"))));
                                linedata3.add(new Entry(i, (int) Float.parseFloat(obj.getString("Distance"))));
                                labels1.add(obj.getString("DataDate"));
                                list.add(model);

                            }
                            String[] xaxes = new String[labels1.size()];
                            for (int j = 0; j < labels1.size(); j++) {
                                xaxes[j] = labels1.get(j);
                            }

                            ArrayList<ILineDataSet> lineDataSets = new ArrayList<>();
                            LineDataSet lineDataSet = new LineDataSet(linedata1, "Fuel");
                            lineDataSet.setDrawCircles(false);
                            lineDataSet.setColor(getResources().getColor(R.color.green));

                            LineDataSet lineDataSet2 = new LineDataSet(linedata2, "Speed");
                            lineDataSet2.setDrawCircles(false);
                            lineDataSet2.setColor(getResources().getColor(R.color.red));


                            LineDataSet lineDataSet3 = new LineDataSet(linedata3, "Distance");
                            lineDataSet3.setDrawCircles(false);
                            lineDataSet3.setColor(getResources().getColor(R.color.blue));

                            lineDataSets.add(lineDataSet);
                            lineDataSets.add(lineDataSet2);
                            lineDataSets.add(lineDataSet3);
                            LineData datass = new LineData(lineDataSets);
                            binding.chart1.setData(datass);
                            XAxis xAxis = binding.chart1.getXAxis();
                            xAxis.setTextColor(getResources().getColor(R.color.white));
                            xAxis.setGranularity(1f);
                            binding.chart1.getAxisLeft().setTextColor(getResources().getColor(R.color.white));
                            binding.chart1.getAxisRight().setTextColor(getResources().getColor(R.color.white));
                            binding.chart1.getAxisLeft().setDrawGridLines(false);
                            binding.chart1.getXAxis().setDrawGridLines(false);
                            binding.chart1.invalidate();
                            binding.tvSelectedFuel.setText(list.get(list.size()-1).getFuelLevel()+" Ltr.");
                            binding.tvSelectedSpeed.setText(list.get(list.size()-1).getSpeed()+" Km/h");
                            binding.tvSelectedDistance.setText(list.get(list.size()-1).getFuelLevel()+" Km");
                            binding.tvSelectedDateTime.setText(list.get(list.size()-1).getDateTime());
                            binding.tvSelectedLocation.setText(list.get(list.size()-1).getLocation());
                            binding.chart1.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                                @Override
                                public void onValueSelected(Entry e, Highlight h) {

                                    Highlight highlight[] = new Highlight[binding.chart1.getData().getDataSets().size()];
                                    for (int j = 0; j < binding.chart1.getData().getDataSets().size(); j++) {

                                        IDataSet iDataSet = binding.chart1.getData().getDataSets().get(j);

                                        for (int i = 0; i < ((LineDataSet) iDataSet).getValues().size()-1; i++) {
                                            if (((LineDataSet) iDataSet).getValues().get(i).getX() == e.getX()) {
                                                highlight[j] = new Highlight(e.getX(), e.getY(), j);
                                                Log.e("fuel",list.get(i).getFuelLevel());
                                                Log.e("distance",list.get(i).getDistance());
                                                Log.e("speed",list.get(i).getSpeed());
                                                binding.tvSelectedFuel.setText(list.get(i).getFuelLevel()+" Ltr.");
                                                binding.tvSelectedSpeed.setText(list.get(i).getSpeed()+" Km/h");
                                                binding.tvSelectedDistance.setText(list.get(i).getDistance()+" Km");
                                                binding.tvSelectedDateTime.setText(list.get(i).getDateTime());
                                                binding.tvSelectedLocation.setText(list.get(i).getLocation());
                                            }
                                        }
                                    }
                                    binding.chart1.highlightValues(highlight);
                                }

                                @Override
                                public void onNothingSelected() {

                                }
                            });
                            getfuelTankDetails(vehicleID);
                        }
                        else {
                            CommonUtil.INSTANCE.alertDialogWithOkOnly(this,"Blackbox","Fuel details not available for this vehicle.");
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case Constants.REQ_VEHICLES:
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
                            model.setBbid(obj.getString("Value"));
                            model.setVehname(obj.getString("Text"));
                            vehicleList.add(obj.getString("Text"));
                            vehicleModel.add(model);
                            vehicleID = vehicleModel.get(0).getBbid().toString();
                            spinVehicles();
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case Constants.REQ_FUEL_TANK_DETAILS_API:
                if (response.isSuccessful()){
                    try {
                        JSONArray data = new JSONArray(response.body().string());
                        for (int i = 0; i <data.length() ; i++) {

                            switch (i){
                                // current fuel level
                                case 0:
                                    currentFuelLevel = data.getJSONObject(0).getString("Data1");
                                    break;
                                case 1:
                                    break;
                                case 2:
                                    totalTankCapacity = data.getJSONObject(2).getString("Data1");
                                    break;
                                case 3:
                                    fuelTankDateTime = data.getJSONObject(3).getString("Data1");
                                    break;
                            }

                        }

                        remainingTank = String.valueOf((Float.parseFloat(totalTankCapacity) - Float.parseFloat(currentFuelLevel)));
                        binding.tvTotalTankVolume.setText(totalTankCapacity+" Ltr.");
                        binding.tvFuelinTank.setText(currentFuelLevel+" Ltr.");
                        binding.tvEmptyFuel.setText(remainingTank+" Ltr.");
                        binding.tvDataDate.setText(fuelTankDateTime.replace("/"," "));
                        binding.gauge.setMaxSpeed(Float.parseFloat(totalTankCapacity));
                        binding.gauge.speedTo(Float.parseFloat(currentFuelLevel));

                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }


    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }

    @Override
    public void onClick(View v) {
        Calendar c;
        SimpleDateFormat df;
        String formattedDate;
        switch (v.getId())
        {
            case R.id.tvYesterday:
                binding.llCustomDateRange.setVisibility(View.GONE);
                String yesterdayDate = CommonUtil.INSTANCE.getYesterdayDate();
                backendStartDate = yesterdayDate;
                backendEndDate = yesterdayDate;
                binding.tvStartDate.setText(backendStartDate);
                binding.tvEndDate.setText(backendEndDate);
                binding.tvYesterday.setBackground(ContextCompat.getDrawable(this, R.drawable.black_cyrve_rect));
                binding.tvToday.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                binding.tvCustom.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                fuelReportApi(vehicleID);
                break;
            case R.id.tvToday:
                binding.llCustomDateRange.setVisibility(View.GONE);
                String currentDate = CommonUtil.INSTANCE.getCurrentDate();
                backendStartDate = currentDate;
                backendEndDate = currentDate;
                binding.tvStartDate.setText(backendStartDate);
                binding.tvEndDate.setText(backendEndDate);
                binding.tvYesterday.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                binding.tvToday.setBackground(ContextCompat.getDrawable(this, R.drawable.black_cyrve_rect));
                binding.tvCustom.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                fuelReportApi(vehicleID);
                break;
            case R.id.tvCustom:
                binding.tvYesterday.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                binding.tvCustom.setBackground(ContextCompat.getDrawable(this, R.drawable.black_cyrve_rect));
                binding.tvToday.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                binding.llCustomDateRange.setVisibility(View.VISIBLE);
                break;

            case R.id.btnAppy:
                fuelReportApi(vehicleID);
                binding.llCustomDateRange.setVisibility(View.GONE);
                break;
            case R.id.tvStartDate:
                datepicker("1");
                break;
            case R.id.tvEndDate:
                datepicker("2");
                break;
        }
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
                    binding.tvStartDate.setText(y + "-" + x + "-" + String.valueOf(year));
                }
                if (flag.equals("2"))
                {
                    backendEndDate =String.valueOf(year)+ "-" + x + "-" + y ;
                    binding.tvEndDate.setText(y + "-" + x + "-" + String.valueOf(year));
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
}