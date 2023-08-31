package com.humbhi.blackbox.ui.ui.addonReports.fuel;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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
import com.humbhi.blackbox.ui.adapters.FuelFillingDetailAdapterForGraphScreen;
import com.humbhi.blackbox.ui.adapters.FuelTheftDetailAdapterForGraphScreen;
import com.humbhi.blackbox.ui.adapters.SearchableAdapter;
import com.humbhi.blackbox.ui.data.db.CommonData;
import com.humbhi.blackbox.ui.data.models.AllVehicleModel;
import com.humbhi.blackbox.ui.data.models.FuelData;
import com.humbhi.blackbox.ui.data.models.FuelGraphModel;
import com.humbhi.blackbox.ui.data.models.FueltheftMainModel;
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
import java.util.Locale;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
    String fromNavigate = "";
    String startDate = "";
    String endDate = "";
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Handler handlerMain = new Handler(Looper.getMainLooper());
    String day = "";
    String customStartDateSelcted = "";
    String customEndDateSelcted = "";
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
        binding.llCustomDateRange.tvStartTime.setText("12:00 AM");
        binding.llCustomDateRange.tvEndTime.setText("11:59 PM");
        binding.tvToday.setOnClickListener(this);
        binding.tvYesterday.setOnClickListener(this);
        binding.tvWeek.setOnClickListener(this);
        binding.tvCustom.setOnClickListener(this);
        binding.llCustomDateRange.tvStartDate.setOnClickListener(this);
        binding.llCustomDateRange.tvEndDate.setOnClickListener(this);
        binding.llCustomDateRange.tvStartTime.setOnClickListener(this);
        binding.llCustomDateRange.tvEndTime.setOnClickListener(this);
        binding.llCustomDateRange.btnAppy.setOnClickListener(this);
        fromNavigate = getIntent().getStringExtra("fromNavigate".toString());
        if(getIntent().hasExtra("vehicleId")){
            day =  getIntent().getStringExtra("day").toString();
            switch (day) {
                case "Yesterday":
                    binding.llCustomDateRange.customeDate.setVisibility(View.GONE);
                    binding.tvYesterday.setBackground(ContextCompat.getDrawable(this, R.drawable.black_cyrve_rect));
                    binding.tvToday.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                    binding.tvCustom.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                    binding.tvWeek.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                    break;
                case "Today":
                    binding.llCustomDateRange.customeDate.setVisibility(View.GONE);
                    binding.tvYesterday.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                    binding.tvToday.setBackground(ContextCompat.getDrawable(this, R.drawable.black_cyrve_rect));
                    binding.tvCustom.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                    binding.tvWeek.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                    break;
                case "Week":
                    binding.llCustomDateRange.customeDate.setVisibility(View.GONE);
                    binding.tvWeek.setBackground(ContextCompat.getDrawable(this, R.drawable.black_cyrve_rect));
                    binding.tvToday.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                    binding.tvCustom.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                    binding.tvYesterday.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                    break;
                case "Custom":
                    binding.llCustomDateRange.customeDate.setVisibility(View.GONE);
                    binding.tvYesterday.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                    binding.tvCustom.setBackground(ContextCompat.getDrawable(this, R.drawable.black_cyrve_rect));
                    binding.tvToday.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                    binding.tvWeek.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                    break;
            }
            startDate = getIntent().getStringExtra("startDate");
            endDate = getIntent().getStringExtra("endDate");
            vehicleID = getIntent().getStringExtra("vehicleId");
            fuelReportApi(vehicleID);
            binding.tvSelectVehicle.setVisibility(View.GONE);
            binding.searchLayout.setVisibility(View.GONE);
        }
        else{
            startTime = "%2000:00:00";
            Calendar calendar = Calendar.getInstance();
            Date enddate = calendar.getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
            endTime = "%20" + sdf.format(enddate);
            String[] parts = sdf.format(enddate).split(":");
            int hour = Integer.parseInt(parts[0]);
            int minute = Integer.parseInt(parts[1]);
            int hourOfDay = (hour == 0) ? 12 : hour % 12;
            String amPm = (hour < 12) ? "AM" : "PM";
            String endTime = String.format("%02d:%02d %s", hourOfDay, minute, amPm);
            binding.llCustomDateRange.customeDate.setVisibility(View.GONE);
            binding.tvToday.setBackground(ContextCompat.getDrawable(this, R.drawable.black_cyrve_rect));
            binding.tvYesterday.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
            binding.tvCustom.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
            binding.tvWeek.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
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
        //       binding.progress.progressLayout.setVisibility(View.VISIBLE);
        if (startDate.equals("") && endDate.equals("")) {
            new Retrofit2(this, this, Constants.REQ_FUEL_GRAPH_API, Constants.FUEL_GRAPH_API + "vehicleId=" + vehicleId + "&beginDate=" + backendStartDate + startTime + "&endDate=" + backendEndDate + endTime).callService(true);
        }
        else{
            new Retrofit2(this, this, Constants.REQ_FUEL_GRAPH_API, Constants.FUEL_GRAPH_API + "vehicleId=" + vehicleId + "&beginDate=" + startDate + "&endDate=" + endDate).callService(true);
        }
    }

    private void getfuelTankDetails(String vehicleId) {
        new Retrofit2(this, this, Constants.REQ_FUEL_TANK_DETAILS_API, Constants.FUEL_TANK_DETAILS_API + "Vehicleid="+vehicleId).callService(true);
    }
    private void getAllVehicles()
    {
     //   binding.progress.progressLayout.setVisibility(View.VISIBLE);
        new Retrofit2(this, this, Constants.REQ_VEHICLES,Constants.VEHICLES+"custid="+ CommonData.INSTANCE.getCustIdFromDB()).callService(true);

    }
    /*
     * Spinner CustId
     * */
    public void spinVehicles()
    {
        binding.spVehicles.setThreshold(0);
        //will start working from first character
        SearchableAdapter adapter = new SearchableAdapter(this,vehicleList);
        binding.spVehicles.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        binding.spVehicles.setOnItemClickListener((parent, view, position, id) -> {
            ArrayList<String> originalData = adapter.getOriginalData();
            String selection = originalData.get(position);
            int originalPosition = adapter.getOriginalPosition(position);
            if (originalPosition != -1) {
                // Use the original position to retrieve the corresponding item
                vehicleID = vehicleModel.get(originalPosition).getBbid();
                fuelReportApi(vehicleID);
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
                        if (data.length() > 0) {
                            for (int i = 0; i < data.length(); i++) {
                                JSONObject obj = data.getJSONObject(i);
                                FuelGraphModel model = new FuelGraphModel(obj.getString("Speed"), obj.getString("Distance"), obj.getString("Location"), obj.getString("FuelLevel"), obj.getString("DataDate"));
                             //   if(!obj.getString("FuelLevel").equals("0")) {
                                    linedata1.add(new Entry(i, (int) Float.parseFloat(obj.getString("FuelLevel"))));
                           //     }
                            //    if(!obj.getString("Speed").equals("0")) {
                                    linedata2.add(new Entry(i, (int) Float.parseFloat(obj.getString("Speed"))));
                          //      }
                            //    if(!obj.getString("Distance").equals("0")) {
                                    linedata3.add(new Entry(i, (int) Float.parseFloat(obj.getString("Distance"))));
                            //    }
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
                            binding.tvSelectedFuel.setText(list.get(list.size() - 1).getFuelLevel() + " Ltr.");
                            binding.tvSelectedSpeed.setText(list.get(list.size() - 1).getSpeed() + " Km/h");
                            binding.tvSelectedDistance.setText(list.get(list.size() - 1).getDistance() + " Km");
                            binding.tvSelectedDateTime.setText(list.get(list.size() - 1).getDateTime());
                            binding.tvSelectedLocation.setText(list.get(list.size() - 1).getLocation());
                            binding.chart1.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                                @Override
                                public void onValueSelected(Entry e, Highlight h) {

                                    Highlight highlight[] = new Highlight[binding.chart1.getData().getDataSets().size()];
                                    for (int j = 0; j < binding.chart1.getData().getDataSets().size(); j++) {

                                        IDataSet iDataSet = binding.chart1.getData().getDataSets().get(j);

                                        for (int i = 0; i < ((LineDataSet) iDataSet).getValues().size() - 1; i++) {
                                            if (((LineDataSet) iDataSet).getValues().get(i).getX() == e.getX()) {
                                                highlight[j] = new Highlight(e.getX(), e.getY(), j);
                                                Log.e("fuel", list.get(i).getFuelLevel());
                                                Log.e("distance", list.get(i).getDistance());
                                                Log.e("speed", list.get(i).getSpeed());
                                                binding.tvSelectedFuel.setText(list.get(i).getFuelLevel() + " Ltr.");
                                                binding.tvSelectedSpeed.setText(list.get(i).getSpeed() + " Km/h");
                                                binding.tvSelectedDistance.setText(list.get(i).getDistance() + " Km");
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
                            if (fromNavigate.equals("filling fuel")) {
                                binding.fuelFillingLayoutTitle.setText("Fuel Filling Details");
                                binding.fuelFillingLayoutTitle.setVisibility(View.VISIBLE);
                                binding.fuelFillingList.hasFixedSize();
                                binding.fuelFillingList.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
                                ArrayList<FuelData> list = (ArrayList<FuelData>) getIntent().getSerializableExtra("fuelFillingList");
                                FuelFillingDetailAdapterForGraphScreen adapter = new FuelFillingDetailAdapterForGraphScreen(list, this);
                                binding.fuelFillingList.setAdapter(adapter);
                                binding.fuelFillingList.setVisibility(View.VISIBLE);
                                if(list.size()<2){
                                    binding.fuelFillingList.setHorizontalScrollBarEnabled(false);
                                }
                                else{
                                    binding.fuelFillingList.setHorizontalScrollBarEnabled(true);
                                }
                            } else if (fromNavigate.equals("Theft fuel")) {
                                binding.fuelFillingLayoutTitle.setText("Fuel Drain Details");
                                binding.fuelFillingLayoutTitle.setVisibility(View.VISIBLE);
                                binding.fuelFillingList.hasFixedSize();
                                binding.fuelFillingList.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
                                ArrayList<FueltheftMainModel> list = (ArrayList<FueltheftMainModel>) getIntent().getSerializableExtra("fuelDrainList");
                                FuelTheftDetailAdapterForGraphScreen adapter = new FuelTheftDetailAdapterForGraphScreen(list, this);
                                binding.fuelFillingList.setAdapter(adapter);
                                binding.fuelFillingList.setVisibility(View.VISIBLE);
                                if(list.size()<2){
                                    binding.fuelFillingList.setHorizontalScrollBarEnabled(false);
                                }
                            } else {
                                binding.fuelFillingLayoutTitle.setVisibility(View.GONE);
                                binding.fuelFillingList.setVisibility(View.GONE);
                            }
                            getfuelTankDetails(vehicleID);
                        } else {
                            CommonUtil.INSTANCE.alertDialogWithOkOnly(this, "Blackbox", "Fuel details not available for this vehicle.");
                        }

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case Constants.REQ_VEHICLES:
                if (response.isSuccessful())
                {
                    try
                    {
                   //     binding.progress.progressLayout.setVisibility(View.GONE);
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

                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case Constants.REQ_FUEL_TANK_DETAILS_API:
                if (response.isSuccessful()){
                    try {
                 //       binding.progress.progressLayout.setVisibility(View.GONE);
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
                        binding.gauge.setMinMaxSpeed(0f,Float.parseFloat(totalTankCapacity));
                        binding.gauge.speedTo(Float.parseFloat(currentFuelLevel));
                    } catch (IOException | JSONException e) {
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
        Calendar calendar = Calendar.getInstance();
        Date endDate = calendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String formattedDate;
        switch (v.getId())
        {
            case R.id.tvYesterday:
                binding.llCustomDateRange.customeDate.setVisibility(View.GONE);
                String yesterdayDate = CommonUtil.INSTANCE.getYesterdayDate();
                backendStartDate = yesterdayDate;
                backendEndDate = yesterdayDate;
                startTime = "%2000:00:00";
                endTime = "%2023:59:00";
                binding.llCustomDateRange.tvStartDate.setText(backendStartDate);
                binding.llCustomDateRange.tvEndDate.setText(backendEndDate);
                binding.tvYesterday.setBackground(ContextCompat.getDrawable(this, R.drawable.black_cyrve_rect));
                binding.tvToday.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                binding.tvCustom.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                binding.tvWeek.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                if(!vehicleID.equals("")) {
                    binding.fuelFillingList.setVisibility(View.GONE);
                    binding.fuelFillingLayoutTitle.setVisibility(View.GONE);
                    fuelReportApi(vehicleID);
                }
                else{
                    Constants.alertDialog(this,"Please select vehicle first");
                }
                break;

            case R.id.tvToday:
                binding.llCustomDateRange.customeDate.setVisibility(View.GONE);
                String currentDate = CommonUtil.INSTANCE.getCurrentDate();
                backendStartDate = currentDate;
                backendEndDate = currentDate;
                startTime = "%2000:00:00";
                endTime = "%20" + sdf.format(endDate);
                binding.llCustomDateRange.tvStartDate.setText(backendStartDate);
                binding.llCustomDateRange.tvEndDate.setText(backendEndDate);
                binding.tvYesterday.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                binding.tvToday.setBackground(ContextCompat.getDrawable(this, R.drawable.black_cyrve_rect));
                binding.tvCustom.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                binding.tvWeek.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                if(!vehicleID.equals("")) {
                    binding.fuelFillingList.setVisibility(View.GONE);
                    binding.fuelFillingLayoutTitle.setVisibility(View.GONE);
                    fuelReportApi(vehicleID);
                }
                else{
                    Constants.alertDialog(this,"Please select vehicle first");
                }
                break;

            case R.id.tvCustom:
                binding.tvYesterday.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                binding.tvCustom.setBackground(ContextCompat.getDrawable(this, R.drawable.black_cyrve_rect));
                binding.tvToday.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                binding.tvWeek.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                binding.llCustomDateRange.customeDate.setVisibility(View.VISIBLE);
                break;

            case R.id.tvWeek:
                binding.llCustomDateRange.customeDate.setVisibility(View.GONE);
                backendStartDate = CommonUtil.INSTANCE.getWeekDate();
                backendEndDate = CommonUtil.INSTANCE.getCurrentDate();
                startTime = "%2000:00:00";
                endTime = "%20" + sdf.format(endDate);
                binding.llCustomDateRange.tvStartDate.setText(backendStartDate);
                binding.llCustomDateRange.tvEndDate.setText(backendEndDate);
                binding.tvWeek.setBackground(ContextCompat.getDrawable(this, R.drawable.black_cyrve_rect));
                binding.tvYesterday.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                binding.tvToday.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                binding.tvCustom.setBackground(ContextCompat.getDrawable(this, R.color.primary_little_fade));
                if(!vehicleID.equals("")) {
                    binding.fuelFillingList.setVisibility(View.GONE);
                    fuelReportApi(vehicleID);
                }
                else{
                    Constants.alertDialog(this,"Please select vehicle first");
                }
                break;

            case R.id.btnAppy:
                if(!customStartDateSelcted.equals("") && !customEndDateSelcted.equals("")) {
                    if (!vehicleID.equals("")) {
                        binding.fuelFillingList.setVisibility(View.GONE);
                        binding.fuelFillingLayoutTitle.setVisibility(View.GONE);
                        fuelReportApi(vehicleID);
                    } else {
                        Constants.alertDialog(this, "Please select vehicle first");
                    }
                    binding.llCustomDateRange.customeDate.setVisibility(View.GONE);
                }
                else{
                    Constants.alertDialog(this,"Please select both dates first");
                }
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
        }
    }

    // valid upto date picker
    private void datepicker(String flag) {
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.YEAR, 0); // to get back 13 years, add -13
        Date previous_year = cal.getTime();
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, monthOfYear, dayOfMonth) -> {
                    int x;
                    String y;
                    if (monthOfYear < 9) {
                        monthOfYear += 1;
                        x = monthOfYear;
                        String formattedMonth = String.format(Locale.getDefault(), "%02d", x);
                        x = Integer.parseInt(formattedMonth);
                    } else {
                        x = monthOfYear + 1;
                    }
                    if (dayOfMonth < 10) {
                        y = "0" + dayOfMonth;
                    } else {
                        y = String.valueOf(dayOfMonth);
                    }
                    if (flag.equals("1")) {
                        backendStartDate = year + "-" + x + "-" + y;
                        customStartDateSelcted = backendStartDate;
                        binding.llCustomDateRange.tvStartDate.setText(y + "-" + x + "-" + year);
                    }
                    if (flag.equals("2")) {
                        backendEndDate = year + "-" + x + "-" + y;
                        customEndDateSelcted = backendEndDate;
                        binding.llCustomDateRange.tvEndDate.setText(y + "-" + x + "-" + year);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        try {
            DatePicker datePicker = datePickerDialog.getDatePicker();
            datePicker.setMaxDate(previous_year.getTime());
        } catch (Exception e) {
            DatePicker datePicker = datePickerDialog.getDatePicker();
        }
        datePickerDialog.show();
    }

    private void endTime() {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, min) -> {
                    int hourVal = hourOfDay % 12;
                    String amPm = (hourOfDay < 12) ? "AM" : "PM";
                    binding.llCustomDateRange.tvEndTime.setText(String.format(Locale.getDefault(), "%02d:%02d %s",
                            (hourVal == 0) ? 12 : hourVal, min, amPm));
                    endTime = String.format("%%%02d%02d:%02d:00", 20, hourOfDay, min);
                },
                hour,
                minute,
                false
        );
        timePickerDialog.show();
    }

    private void startTime() {
        int hour = 0;
        int minute = 0;
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                this,
                (view, hourOfDay, min) -> {
                    int hourVal = hourOfDay % 12;
                    String amPm = (hourOfDay < 12) ? "AM" : "PM";
                    binding.llCustomDateRange.tvStartTime.setText(String.format(Locale.getDefault(), "%02d:%02d %s",
                            (hourVal == 0) ? 12 : hourVal, min, amPm));
                    startTime = String.format("%%%02d%02d:%02d:00", 20, hourOfDay, min);
                },
                hour,
                minute,
                false
        );
        timePickerDialog.show();
    }

}