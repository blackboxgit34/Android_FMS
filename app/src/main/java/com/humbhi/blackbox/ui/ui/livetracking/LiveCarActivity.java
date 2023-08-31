package com.humbhi.blackbox.ui.ui.livetracking;

import android.animation.ValueAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.humbhi.blackbox.R;
import com.humbhi.blackbox.databinding.ActivityLiveCarBinding;
import com.humbhi.blackbox.ui.Utility.Constants;
import com.humbhi.blackbox.ui.data.db.CommonData;
import com.humbhi.blackbox.ui.data.models.AlertForApp;
import com.humbhi.blackbox.ui.data.models.CommonResponseMQTT;
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints;
import com.humbhi.blackbox.ui.retofit.NetworkService;
import com.humbhi.blackbox.ui.retofit.NewRetrofitClient;
import com.humbhi.blackbox.ui.retofit.Retrofit2;
import com.humbhi.blackbox.ui.retofit.RetrofitResponse;
import com.humbhi.blackbox.ui.ui.notification.GNotifications;
import com.humbhi.blackbox.ui.ui.routePlayback.RoutePlayBack;
import com.humbhi.blackbox.ui.utils.CommonUtil;
import com.humbhi.blackbox.ui.utils.LatLngInterpolator;
import com.humbhi.blackbox.ui.utils.LocationAddress;
import com.humbhi.blackbox.ui.utils.MqttHelper;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import info.mqtt.android.service.MqttAndroidClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveCarActivity extends FragmentActivity implements OnMapReadyCallback, RetrofitResponse,MqttHelper.MqttMessageCallback  {
    private static final long DELAY = 6000;
    private static final long ANIMATION_TIME_PER_ROUTE = 4500;
    private static final long ANIMATION_TIME_PER_ROUTE_FOR_LIVE_MOMENT = 1500;
    GoogleMap googleMap;
    private SupportMapFragment mapFragment;
    private Handler handler;
    private Marker carMarker;
    private LatLng startPosition;
    private LatLng endPosition;
    private float v;
    List<LatLng> polyLineList;
    String location = "",vehicleId="",backendStartDate,backendEndDate;
    List<AlertForApp>alertList = new ArrayList<>();
    private final String TAG = "HomeActivity";
    // Give your Server URL here >> where you get car location update
    private boolean isFirstPosition = true;
    private Double startLatitude;
    private Double startLongitude;
    private String vehicleName;
    private String vehicleType="",progresstatus="";
    private ActivityLiveCarBinding binding;

    private MqttAndroidClient client;
    private MqttHelper mqttHelper;
    private  ExecutorService executor;
    private  Handler handlerMain;
    private static final long TEN_MINUTES_IN_MILLIS = 10 * 60 * 1000; // 10 minutes in milliseconds
    private Handler screenTimeoutHandler;
    private Runnable screenTimeoutRunnable;
    private String[] mapTypes = {"Standard", "Satellite", "Terrain", "Hybrid"};
    private final float angle = 0;
    private String vehicleStatus = "";
    String speed = "";
    String gmt = "";
    Boolean isFirstTime = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLiveCarBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        handler = new Handler();
        //handler2 = new Handler();
        if (getIntent().hasExtra("vehicleName")){
            vehicleName = getIntent().getStringExtra("vehicleName");
        }
        setToolbar();
        startGettingOnlineDataFromCar();
        // Generate a random unique identifier
        String uniqueId = UUID.randomUUID().toString();
        String deviceName = "my_device";
        String clientId = deviceName + "_" + uniqueId;
        mqttHelper = MqttHelper.getInstance(this, Constants.BROKER_ADDRESS, Constants.BROKER_PORT, clientId);
        mqttHelper.setMqttMessageCallback(this);
        // Connect to the MQTT broker
        executor = Executors.newSingleThreadExecutor();
        handlerMain = new Handler(Looper.getMainLooper());
        screenTimeoutHandler = new Handler();
        screenTimeoutRunnable = () -> {
            executor.execute(this::stopMQQTT);
            finish();
        };

        binding.tvPlayback.setOnClickListener(v -> {
            backendStartDate = CommonUtil.INSTANCE.getCurrentDate();
            backendEndDate = CommonUtil.INSTANCE.getCurrentDate();
            Intent intent = new Intent(LiveCarActivity.this, RoutePlayBack.class);
            intent.putExtra("tableName",vehicleId);
            intent.putExtra("fromDate",backendStartDate);
            intent.putExtra("endDate",backendEndDate);
            intent.putExtra("vehicleName",vehicleName);
            intent.putExtra("flag","simple");
            intent.putExtra("backToLive","yes");
            startActivity(intent);
            finish();
        });

        binding.mapView.setOnClickListener(v -> {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setTitle("Select Map Type");
            dialogBuilder.setItems(mapTypes, (dialog, which) -> {
                String selectedMapType = mapTypes[which];
                setMapType(selectedMapType);
            });

            AlertDialog dialog = dialogBuilder.create();
            dialog.show();
        });
    }

    private void setMapType(String mapType) {
        // Perform any necessary actions to update the map with the chosen type
        switch (mapType){
            case "Standard" : googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case "Hybrid" : googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case "Terrain" : googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case "Satellite" : googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
        }
    }

    private void setToolbar(){
        binding.toolbar.ivMenu.setVisibility(View.GONE);
        binding.toolbar.ivBell.setVisibility(View.GONE);
        binding.toolbar.ivBack.setVisibility(View.VISIBLE);
        binding.toolbar.tvTitle.setText(vehicleName);
        binding.toolbar.ivBack.setOnClickListener(view -> {
            finish();
        });
    }

    void stopRepeatingTask() {
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopScreenTimeout();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        this.googleMap = map;
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.setTrafficEnabled(false);
        googleMap.setIndoorEnabled(false);
        googleMap.setBuildingsEnabled(false);
        googleMap.moveCamera(CameraUpdateFactory
                .newCameraPosition
                        (new CameraPosition.Builder()
                                .target(new LatLng(30.709588,76.810326))
                                .zoom(17f)
                                .build()));
    }

    private void runMQTTTask(){
        executor.execute(() -> {
            mqttHelper.connect(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // Connected successfully, perform further operations
                    //J860181063593567
                    mqttHelper.subscribe("Live/"+vehicleId , 0, new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            // Main Thread
                            executor.execute(LiveCarActivity.this::startScreenTimeout);
                            handlerMain.post(() -> {
                                // Start the screen timeout countdown
                                Log.e("Message", "Topic Subscribed successfully");
                            });
                        }

                        @Override
                        public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                            // Handle subscription failure
                            handlerMain.post(() -> {
                                Log.e("Message", "Topic Subscription unsuccessfully");
                            });
                        }
                    });
                    //         }
                    handlerMain.post(() -> {
                        Log.e("Message", "Client Connected successfully");
                    });
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    handlerMain.post(() -> {
                        Log.e("Message", "Client Connected Failure");
                    });
                }
            });
        });
    }

    private void getDriverLocationUpdate() {
        if(isFirstTime==true){
            binding.progress.setVisibility(View.VISIBLE);
        }
        else{
            binding.progress.setVisibility(View.GONE);
        }
         new Retrofit2(LiveCarActivity.this, this, 200,
                ApiEndpoints.GET_VEHICLE_DETAILS + "custid=" + CommonData.INSTANCE.getCustIdFromDB()
                        + "&StatusCode=&sEcho=0&iDisplayStart=0&iDisplayLength=1&sSearch="+vehicleName+"&iSortCol_0=0&sSortDir_0").callService(false);
    }

    private void startBikeAnimation(final LatLng start, final LatLng end, String progresstatus, String vehicleType) {
        Log.i(TAG, "startBikeAnimation called...");
        LatLngInterpolator latLngInterpolator = new LatLngInterpolator.Linear();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(ANIMATION_TIME_PER_ROUTE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(valueAnimator1 -> {
            v = valueAnimator1.getAnimatedFraction();
            LatLng newPosition = latLngInterpolator.interpolate(v, start, end);
            carMarker.setPosition(newPosition);
            carMarker.setFlat(true);
            carMarker.setAnchor(0.5f, 0.5f);
            carMarker.setRotation(bearingBetweenLocations(start, end));
            googleMap.moveCamera(CameraUpdateFactory
                    .newCameraPosition
                            (new CameraPosition.Builder()
                                    .target(newPosition)
                                    .zoom(18f)
                                    .build()));
            startPosition = carMarker.getPosition();
        });
        valueAnimator.start();
    }

    private void stopMQQTT()
    {
        NetworkService api = NewRetrofitClient.INSTANCE.getInstance().create(NetworkService.class);
                executor.execute(()->{Call<CommonResponseMQTT> call = api.updateMQTTCmd(vehicleId,"0");
                    call.enqueue(new Callback<CommonResponseMQTT>() {
                        @Override
                        public void onResponse(Call<CommonResponseMQTT> call, Response<CommonResponseMQTT> response) {
                            executor.execute(()->{
                                mqttHelper.unsubscribe("Live/"+vehicleId , new IMqttActionListener() {
                                    @Override
                                    public void onSuccess(IMqttToken asyncActionToken) {
                                        // Main Thread
                                        handlerMain.post(() -> {
                                            // Start the screen timeout countdown
                                            Log.e("Message", "Topic Unsubscribed successfully");
                                        });
                                    }

                                    @Override
                                    public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                        // Handle subscription failure
                                        handlerMain.post(() -> {
                                            Log.e("Message", "Topic Unubscription unsuccessfully");
                                        });
                                    }
                                });
                            });
                        }

                        @Override
                        public void onFailure(Call<CommonResponseMQTT> call, Throwable t) {
                            if (t instanceof SocketTimeoutException) {
                                Toast.makeText(LiveCarActivity.this, "Connection time out.", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Toast.makeText(LiveCarActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                });
    }

    private void startBikeAnimationLiveMovement(final LatLng start, final LatLng end, Float angle) {
        LatLngInterpolator latLngInterpolator = new LatLngInterpolator.Linear();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(ANIMATION_TIME_PER_ROUTE_FOR_LIVE_MOMENT);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(valueAnimator1 -> {
            //LogMe.i(TAG, "Car Animation Started...");
            v = valueAnimator1.getAnimatedFraction();
            LatLng newPosition = latLngInterpolator.interpolate(v, start, end);
            carMarker.setPosition(newPosition);
            carMarker.setFlat(true);
            carMarker.setAnchor(0.5f, 0.5f);
            carMarker.setRotation(angle);
            googleMap.moveCamera(CameraUpdateFactory
                    .newCameraPosition(new CameraPosition.Builder()
                                    .target(newPosition)
                                    .zoom(18f)
                                    .build()));
            startPosition = carMarker.getPosition();
        });
        valueAnimator.start();
    }

    Runnable mStatusChecker = new Runnable() {
        @Override
        public void run() {
            try {
                getDriverLocationUpdate();
            } catch (Exception e) {
                Log.e(TAG, e.getMessage());
            }
            handler.postDelayed(mStatusChecker, DELAY);
        }
    };

    void startGettingOnlineDataFromCar() {
        try {
            handler.post(mStatusChecker);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onServiceResponse(int requestCode, retrofit2.Response<ResponseBody> response) {
        JSONObject obj = null;
        double angle = 0.0;
        try {
            isFirstTime=false;
            binding.progress.setVisibility(View.GONE);
            JSONObject object = new JSONObject(response.body().string());
            JSONArray data = object.getJSONArray("aaData");
            alertList.clear();
            polyLineList = new ArrayList<>();
            for (int i = 0; i < data.length(); i++) {
                obj = data.getJSONObject(i);
                vehicleId = obj.getString("Bbid");
                startLatitude = Double.parseDouble(obj.getString("Lat"));
                startLongitude = Double.parseDouble(obj.getString("Lng"));
                angle = Double.parseDouble(obj.getString("angle"));
                binding.tvLocation.setText(obj.getString("Location"));
                location = obj.getString("Location");
                binding.tvDataDate.setText("Updated at: "+obj.getString("DataDateTime"));
                binding.tvSpeed.setText(obj.getString("Speed")+" K/H");
                binding.tvDistance.setText(obj.getString("Distance")+" KM");
                if(obj.getString("AcStatus").toString().contains("acoff")){
                    binding.tvAcStatus.setText("OFF");
                }
                else{
                    binding.tvAcStatus.setText("ON");
                }
                binding.tvFuelStatus.setText(obj.getString("FuelStatus")+" L");
                Uri uriBattery = Uri.parse("https://trackmaster.in"+obj.getString("BatteryStatus").replace("~",""));
                Glide.with(this).load(uriBattery).into(binding.tvBatteryStatus);
                binding.tvIgnitionStatus.setText(obj.getString("IgnitionStatus"));
                String stoppageTime = obj.getString("StoppageTime");
                binding.tvStoppageTime.setText(calculateStoppageTime(stoppageTime));
                String parkAtLASTstoppageTime = obj.getString("ParkingTimeALasttStop");
                binding.tvParkAtLastStop.setText(calculateStoppageTime(parkAtLASTstoppageTime));
                String TotalParkingTime = obj.getString("TotalParkingTime");
                binding.tvParkingTimeTotal.setText(calculateStoppageTime(TotalParkingTime));
                String MovingFromLastStop = obj.getString("MovingFromLastStop");
                binding.tvMovingFromLastStop.setText(calculateStoppageTime(MovingFromLastStop));
                String TotalMoving = obj.getString("TotalMoving");
                binding.tvTotalMovingTime.setText(calculateStoppageTime(TotalMoving));
                gmt = obj.getString("GMT");
                JSONArray alertData = obj.getJSONArray("AlertForApp");
                for (int j = 0; j <alertData.length() ; j++) {
                    JSONObject alertObj = alertData.getJSONObject(j);
                    AlertForApp alertModel = new AlertForApp(alertObj.getString("AlertDetails"),alertObj.getInt("Count"),alertObj.getInt("Id"));
                    alertList.add(alertModel);
                }
                progresstatus = obj.getString("progresstatus");
                binding.tvAlerts.setOnClickListener(v -> {
                    Intent intent = new Intent(LiveCarActivity.this, GNotifications.class);
                    intent.putExtra("from",vehicleId);
                    intent.putExtra("backToLive",vehicleName);
                    startActivity(intent);
                    finish();
                });
                switch (progresstatus){
                    case "Unreachable":
                        binding.tvProgressStatus.setText("Unreachable");
                        binding.tvProgressStatus.setTextColor(getResources().getColor(R.color.white));
                        binding.tvProgressStatus.setBackgroundResource(R.drawable.round_bg_red);
                        stopRepeatingTask();
                        break;
                    case "BD":
                        binding.tvProgressStatus.setText("Battery Disconnection");
                        binding.tvProgressStatus.setTextColor(getResources().getColor(R.color.white));
                        binding.tvProgressStatus.setBackgroundResource(R.drawable.round_bg_red);
                        stopRepeatingTask();
                        break;
                    case "Stopped":
                        binding.tvProgressStatus.setText("Parked");
                        binding.tvProgressStatus.setTextColor(getResources().getColor(R.color.white));
                        binding.tvProgressStatus.setBackgroundResource(R.drawable.round_bg_yellow);
                        break;
                    case "IgnitionOn":
                        binding.tvProgressStatus.setText("Ignition On");
                        binding.tvProgressStatus.setTextColor(getResources().getColor(R.color.white));
                        binding.tvProgressStatus.setBackgroundResource(R.drawable.round_background);
                        break;
                    case "Moving":
                        binding.tvProgressStatus.setText("Moving");
                        binding.tvProgressStatus.setTextColor(getResources().getColor(R.color.white));
                        binding.tvProgressStatus.setBackgroundResource(R.drawable.round_bg_green);
                       if(vehicleId.startsWith("J") || vehicleId.startsWith("E") && vehicleId.length()>5){
                            stopRepeatingTask();
                            NetworkService api = NewRetrofitClient.INSTANCE.getInstance().create(NetworkService.class);
                            executor.execute(()->{
                                Call<CommonResponseMQTT> call = api.updateMQTTCmd(vehicleId,"600");
                                call.enqueue(new Callback<CommonResponseMQTT>() {
                                    @Override
                                    public void onResponse(Call<CommonResponseMQTT> call, Response<CommonResponseMQTT> response) {
                                        runMQTTTask();
                                    }

                                    @Override
                                    public void onFailure(Call<CommonResponseMQTT> call, Throwable t) {
                                        if (t instanceof SocketTimeoutException) {
                                            Toast.makeText(LiveCarActivity.this, "Connection time out.", Toast.LENGTH_SHORT).show();
                                        }
                                        else{
                                            Toast.makeText(LiveCarActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            });
                        }
                        break;
                    case "Towed":
                        binding.tvProgressStatus.setText("Towed");
                        binding.tvProgressStatus.setTextColor(getResources().getColor(R.color.black));
                        binding.tvProgressStatus.setBackgroundResource(R.drawable.round_bg_creame);
                        break;
                    case "Hispeed":
                        binding.tvProgressStatus.setText("Hi-speed");
                        binding.tvProgressStatus.setTextColor(getResources().getColor(R.color.white));
                        binding.tvProgressStatus.setBackgroundResource(R.drawable.round_bg_blue);
                        break;
                }
                vehicleType = obj.getString("type");
                binding.tvSpeed.setText(obj.getString("Speed")+" K/H");
                binding.tvDistance.setText(obj.getString("Distance")+" KM");
            }

            Log.e("LATLONG--->", startLatitude + "--" + startLongitude);
            if(startLatitude!=0.0 && startLongitude!=0.0) {
                getAddress(this, startLatitude, startLongitude);

                if (isFirstPosition) {
                    startPosition = new LatLng(startLatitude, startLongitude);
                    carMarker = googleMap.addMarker(new MarkerOptions().position(startPosition).anchor(0.5f,0.5f).flat(true).rotation((float) angle));
                    if (!vehicleType.equals("Other")) {
                        if (vehicleType.equalsIgnoreCase("OilTanker")) {
                            switch (progresstatus) {
                                case "Unreachable":
                                case "BD":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_oil_tanker_red));
                                    break;
                                case "Stopped":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_oil_tanker_yellow));
                                    break;
                                case "IgnitionOn":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_oil_tanker_orange));
                                    break;
                                case "Moving":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_oil_tanker_green));
                                    break;
                                case "Towed":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_oil_tanker_white));
                                    break;
                                case "Hispeed":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_oil_tanker_blue));
                                    break;
                            }
                        } else if (vehicleType.equalsIgnoreCase("Car"))
                            switch (progresstatus) {
                                case "Unreachable":
                                case "BD":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_car_red));
                                    break;
                                case "Stopped":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_car_yellow));
                                    break;
                                case "IgnitionOn":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_car_orange));
                                    break;
                                case "Moving":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_car_green_with_shadow));
                                    break;
                                case "Towed":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_car_white));
                                    break;
                                case "Hispeed":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_car_blue_with_shadow));
                                    break;
                            }
                        else if (vehicleType.equalsIgnoreCase("Bus"))
                            switch (progresstatus) {
                                case "Unreachable":
                                case "BD":
                                    carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_new_bus_red));
                                    break;
                                case "Stopped":
                                    carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_new_bus_yellow));
                                    break;
                                case "IgnitionOn":
                                    carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_new_bus_orange));
                                    break;
                                case "Moving":
                                    carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_new_bus_green));
                                    break;
                                case "Towed":
                                    carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_new_bus));
                                    break;
                                case "Hispeed":
                                    carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_new_bus_blue));
                                    break;
                            }
                        else if (vehicleType.equalsIgnoreCase("Ambulance"))
                            switch (progresstatus) {
                                case "Unreachable":
                                case "BD":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_red_truck_final));
                                    break;
                                case "Stopped":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_yellow_truck_final));
                                    break;
                                case "IgnitionOn":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_orange_truck_final));
                                    break;
                                case "Moving":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_green_truck_final));
                                    break;
                                case "Towed":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_white_truck_final));
                                    break;
                                case "Hispeed":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_blue_truck_final));
                                    break;
                            }
                        else if (vehicleType.equalsIgnoreCase("Truck"))
                            switch (progresstatus) {
                                case "Unreachable":
                                case "BD":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_red_truck_final));
                                    break;
                                case "Stopped":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_yellow_truck_final));
                                    break;
                                case "IgnitionOn":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_orange_truck_final));
                                    break;
                                case "Moving":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_green_truck_final));
                                    break;
                                case "Towed":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_white_truck_final));
                                    break;
                                case "Hispeed":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_blue_truck_final));
                                    break;
                            }
                        else if (vehicleType.equalsIgnoreCase("RoadRoller")) {
                            switch (progresstatus) {
                                case "Unreachable":
                                case "BD":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_red_truck_final));
                                    break;
                                case "Stopped":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_yellow_truck_final));
                                    break;
                                case "IgnitionOn":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_orange_truck_final));
                                    break;
                                case "Moving":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_green_truck_final));
                                    break;
                                case "Towed":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_white_truck_final));
                                    break;
                                case "Hispeed":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_blue_truck_final));
                                    break;
                            }
                        } else {
                            switch (progresstatus) {
                                case "Unreachable":
                                case "BD":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_red_truck_final));
                                    break;
                                case "Stopped":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_yellow_truck_final));
                                    break;
                                case "IgnitionOn":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_orange_truck_final));
                                    break;
                                case "Moving":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_green_truck_final));
                                    break;
                                case "Towed":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_white_truck_final));
                                    break;
                                case "Hispeed":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_blue_truck_final));
                                    break;
                            }
                        }
                    } else {
                        switch (progresstatus) {
                            case "Unreachable":
                            case "BD":
                                carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_red_truck_final));
                                break;
                            case "Stopped":
                                carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_yellow_truck_final));
                                break;
                            case "IgnitionOn":
                                carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_orange_truck_final));
                                break;
                            case "Moving":
                                carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_green_truck_final));
                                break;
                            case "Towed":
                                carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_white_truck_final));
                                break;
                            case "Hispeed":
                                carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_blue_truck_final));
                                break;
                        }
                    }
                    // carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_green_truck_final));
                    carMarker.setAnchor(0.5f, 0.5f);
                    googleMap.moveCamera(CameraUpdateFactory
                            .newCameraPosition(new CameraPosition.Builder()
                                    .target(startPosition)
                                    .zoom(18f)
                                    .build()));

                    isFirstPosition = false;
                }
                else {
                    endPosition = new LatLng(startLatitude, startLongitude);

                    Log.d(TAG, startPosition.latitude + "--" + endPosition.latitude + "--Check --" + startPosition.longitude + "--" + endPosition.longitude);
                    if (!vehicleType.equals("Other")) {
                        if (vehicleType.equalsIgnoreCase("OilTanker")) {
                            switch (progresstatus) {
                                case "Unreachable":
                                case "BD":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_oil_tanker_red));
                                    break;
                                case "Stopped":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_oil_tanker_yellow));
                                    break;
                                case "IgnitionOn":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_oil_tanker_orange));
                                    break;
                                case "Moving":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_oil_tanker_green));
                                    break;
                                case "Towed":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_oil_tanker_white));
                                    break;
                                case "Hispeed":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_oil_tanker_blue));
                                    break;
                            }
                        } else if (vehicleType.equalsIgnoreCase("Car"))
                            switch (progresstatus) {
                                case "Unreachable":
                                case "BD":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_car_red));
                                    break;
                                case "Stopped":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_car_yellow));
                                    break;
                                case "IgnitionOn":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_car_orange));
                                    break;
                                case "Moving":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_car_green_with_shadow));
                                    break;
                                case "Towed":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_car_white));
                                    break;
                                case "Hispeed":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_car_blue_with_shadow));
                                    break;
                            }
                        else if (vehicleType.equalsIgnoreCase("Bus"))
                            switch (progresstatus) {
                                case "Unreachable":
                                case "BD":
                                    carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_new_bus_red));
                                    break;
                                case "Stopped":
                                    carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_new_bus_yellow));
                                    break;
                                case "IgnitionOn":
                                    carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_new_bus_orange));
                                    break;
                                case "Moving":
                                    carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_new_bus_green));
                                    break;
                                case "Towed":
                                    carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_new_bus));
                                    break;
                                case "Hispeed":
                                    carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_new_bus_blue));
                                    break;
                            }
                        else if (vehicleType.equalsIgnoreCase("Ambulance"))
                            switch (progresstatus) {
                                case "Unreachable":
                                case "BD":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_red_truck_final));
                                    break;
                                case "Stopped":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_yellow_truck_final));
                                    break;
                                case "IgnitionOn":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_orange_truck_final));
                                    break;
                                case "Moving":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_green_truck_final));
                                    break;
                                case "Towed":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_white_truck_final));
                                    break;
                                case "Hispeed":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_blue_truck_final));
                                    break;
                            }
                        else if (vehicleType.equalsIgnoreCase("Truck"))
                            switch (progresstatus) {
                                case "Unreachable":
                                case "BD":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_red_truck_final));
                                    break;
                                case "Stopped":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_yellow_truck_final));
                                    break;
                                case "IgnitionOn":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_orange_truck_final));
                                    break;
                                case "Moving":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_green_truck_final));
                                    break;
                                case "Towed":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_white_truck_final));
                                    break;
                                case "Hispeed":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_blue_truck_final));
                                    break;
                            }
                        else if (vehicleType.equalsIgnoreCase("RoadRoller")) {
                            switch (progresstatus) {
                                case "Unreachable":
                                case "BD":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_red_truck_final));
                                    break;
                                case "Stopped":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_yellow_truck_final));
                                    break;
                                case "IgnitionOn":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_orange_truck_final));
                                    break;
                                case "Moving":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_green_truck_final));
                                    break;
                                case "Towed":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_white_truck_final));
                                    break;
                                case "Hispeed":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_blue_truck_final));
                                    break;
                            }
                        } else {
                            switch (progresstatus) {
                                case "Unreachable":
                                case "BD":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_red_truck_final));
                                    break;
                                case "Stopped":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_yellow_truck_final));
                                    break;
                                case "IgnitionOn":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_orange_truck_final));
                                    break;
                                case "Moving":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_green_truck_final));
                                    break;
                                case "Towed":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_white_truck_final));
                                    break;
                                case "Hispeed":
                                    carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_blue_truck_final));
                                    break;
                            }
                        }
                    } else {
                        switch (progresstatus) {
                            case "Unreachable":
                            case "BD":
                                carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_red_truck_final));
                                break;
                            case "Stopped":
                                carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_yellow_truck_final));
                                break;
                            case "IgnitionOn":
                                carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_orange_truck_final));
                                break;
                            case "Moving":
                                carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_green_truck_final));
                                break;
                            case "Towed":
                                carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_white_truck_final));
                                break;
                            case "Hispeed":
                                carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_blue_truck_final));
                                break;
                        }
                    }
                    if ((startPosition.latitude != endPosition.latitude) || (startPosition.longitude != endPosition.longitude)) {
                        Log.e(TAG, "NOT SAME");
                        startBikeAnimation(startPosition, endPosition, progresstatus, vehicleType);
                    } else {
                        Log.e(TAG, "SAMME");
                    }
                }
            }
        }
        catch (Exception e ) {
        }
    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    private float bearingBetweenLocations(LatLng position, LatLng position1) {
        double PI = 3.14159;
        double lat1 = position.latitude * PI / 180;
        double long1 = position.longitude * PI / 180;
        double lat2 = position1.latitude * PI / 180;
        double long2 = position1.longitude * PI / 180;
        double dLon = long2 - long1;
        double y = Math.sin(dLon) * Math.cos(lat2);
        double x = Math.cos(lat1) * Math.sin(lat2) - Math.sin(lat1) * Math.cos(lat2) * Math.cos(dLon);
        double brng = Math.atan2(y, x);
        brng = Math.toDegrees(brng);
        brng = (brng + 360) % 360;
        return (float) brng;
    }
    // get exact location using latitude and longitude

    public void getAddress(Context context, double lat, double lng) {
        executor.execute(()->{
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);
            String add = obj.getAddressLine(0);
            location = add;
        } catch (IOException e) {
            e.printStackTrace();
        }
        });
    }

    public LatLng getLocationFromAddress(Context context,String strAddress) {
        Geocoder coder = new Geocoder(context);
        List<Address> address;
        LatLng p1 = null;
        try {
            address = coder.getFromLocationName(strAddress, 1);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();
            p1 = new LatLng(location.getLatitude(), location.getLongitude() );
        } catch (Exception ex) {

            ex.printStackTrace();
        }
        return p1;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopScreenTimeout();
        stopRepeatingTask();
        mqttHelper.disconnect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startScreenTimeout();
    }

    @Override
    public void onMessageReceived(String topic, MqttMessage message) {
        String payload = new String(message.getPayload());
        JSONObject object;
        try {
            object = new JSONObject(payload);
            Log.e("Received message -> ",object.toString());
               if (isFirstPosition) {
                   startPosition = new LatLng(object.getDouble("lati"), object.getDouble("longi"));
                   LocationAddress.getAddressFromLocation(object.getDouble("lati"), object.getDouble("longi"), this, address -> {
                       if (address.equals("") || address != null) {
                           binding.tvLocation.setText(address);
                       }
                   });
                   carMarker = googleMap.addMarker(new MarkerOptions().position(startPosition).flat(true).anchor(0.5f,0.5f).rotation(Float.parseFloat(object.getString("angle"))));
                   vehicleStatus = object.getString("ignition");
                   speed = object.getString("speed");
                   String oldTime = object.getString("datadate");
                   binding.tvDataDate.setText("Updated at: " + TimeConverter(oldTime,gmt));
                   binding.tvSpeed.setText(object.getString("speed") + " K/H");
              //     binding.tvDistance.setText(object.getString("distance") + " KM");
                   googleMap.moveCamera(CameraUpdateFactory
                           .newCameraPosition(new CameraPosition.Builder()
                                   .target(new LatLng(object.getDouble("lati"), object.getDouble("longi")))
                                   .zoom(18f)
                                   .build()));
                   isFirstPosition = false;
               } else {
                   endPosition = new LatLng(object.getDouble("lati"), object.getDouble("longi"));
                   LocationAddress.getAddressFromLocation(object.getDouble("lati"), object.getDouble("longi"), this, address -> {
                       if (address.equals("") || address != null) {
                           binding.tvLocation.setText(address);
                       }
                   });
                   vehicleStatus = object.getString("ignition");
                   speed = object.getString("speed");
                   String oldTime = object.getString("datadate");
                   binding.tvDataDate.setText("Updated at: " + TimeConverter(oldTime,gmt));
                   binding.tvSpeed.setText(object.getString("speed") + " K/H");
                //   binding.tvDistance.setText(object.getString("distance") + " KM");
                   Log.d(TAG, startPosition.latitude + "--" + endPosition.latitude + "--Check --" + startPosition.longitude + "--" + endPosition.longitude);
                   if(speed.equals("0.0")){
                       if(vehicleStatus.equals("1")){
                           binding.tvProgressStatus.setText("Ignition On");
                           binding.tvProgressStatus.setTextColor(getResources().getColor(R.color.white));
                           binding.tvProgressStatus.setBackgroundResource(R.drawable.round_background);
                       }
                       else{
                           binding.tvProgressStatus.setText("Parked");
                           binding.tvProgressStatus.setTextColor(getResources().getColor(R.color.white));
                           binding.tvProgressStatus.setBackgroundResource(R.drawable.round_bg_yellow);
                       }
                   }
                   else{
                       binding.tvProgressStatus.setText("Moving");
                       binding.tvProgressStatus.setTextColor(getResources().getColor(R.color.white));
                       binding.tvProgressStatus.setBackgroundResource(R.drawable.round_bg_green);
                   }
                   if (!vehicleType.equals("Other")){
                       if(vehicleType.equalsIgnoreCase("OilTanker")) {
                           if (!speed.equals("0.0")) {
                               carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_oil_tanker_green));
                           } else {
                               if (vehicleStatus.equals("1")) {
                                   carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_oil_tanker_orange));
                               } else {
                                   carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_oil_tanker_yellow));
                               }
                           }
                       }
                       else if(vehicleType.equalsIgnoreCase("Car")) {
                           if (!speed.equals("0.0")) {
                               carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_car_green_with_shadow));
                           } else {
                               if (vehicleStatus.equals("1")) {
                                   carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_car_orange));
                               } else {
                                   carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_car_yellow));
                               }
                           }
                       }
                       else if (vehicleType.equalsIgnoreCase("Bus")) {
                           if (!speed.equals("0.0")) {
                               carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_new_bus_green));
                           } else {
                               if (vehicleStatus.equals("1")) {
                                   carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_new_bus_orange));
                               } else {
                                   carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_new_bus_yellow));
                               }
                           }
                       }
                       else if(vehicleType.equalsIgnoreCase("Ambulance")) {
                           if (!speed.equals("0.0")) {
                               carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_green_truck_final));
                           } else {
                               if (vehicleStatus.equals("1")) {
                                   carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_orange_truck_final));
                               } else {
                                   carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_yellow_truck_final));
                               }
                           }
                       }
                       else if(vehicleType.equalsIgnoreCase("Truck")) {
                           if (!speed.equals("0.0")) {
                               carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_green_truck_final));
                           } else {
                               if (vehicleStatus.equals("1")) {
                                   carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_orange_truck_final));
                               } else {
                                   carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_yellow_truck_final));
                               }
                           }
                       }
                       else if(vehicleType.equalsIgnoreCase("RoadRoller")){
                           {
                               if (!speed.equals("0.0")) {
                                   carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_car_green_with_shadow));
                               } else {
                                   if (vehicleStatus.equals("1")) {
                                       carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_car_orange));
                                   } else {
                                       carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_car_yellow));
                                   }
                               }
                           }
                       }
                       else {
                           if (!speed.equals("0.0")) {
                               carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_green_truck_final));
                           } else {
                               if (vehicleStatus.equals("1")) {
                                   carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_orange_truck_final));
                               } else {
                                   carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_yellow_truck_final));
                               }
                           }
                       }
                   }
                   else {
                       if (!speed.equals("0.0")) {
                           carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_green_truck_final));
                       } else {
                           if (vehicleStatus.equals("1")) {
                               carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_orange_truck_final));
                           } else {
                               carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_yellow_truck_final));
                           }
                       }
                   }
                   if ((startPosition.latitude != endPosition.latitude) || (startPosition.longitude != endPosition.longitude)) {
                       Log.e(TAG, "NOT SAME");
                       startBikeAnimationLiveMovement(startPosition, endPosition,bearingBetweenLocations(startPosition,endPosition));
                   }
               }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDisconnect(Throwable cause) {
        stopMQQTT();
    }

    private void startScreenTimeout() {
        // Cancel any existing screen timeout countdown
        screenTimeoutHandler.postDelayed(screenTimeoutRunnable, TEN_MINUTES_IN_MILLIS);
    }

    private void stopScreenTimeout() {
        screenTimeoutHandler.removeCallbacks(screenTimeoutRunnable);
    }

    public String calculateStoppageTime(String stoppageTime) {
        String sDays = stoppageTime.substring(0, 2);
        String sHours = stoppageTime.substring(3);

        if (sDays.equals("00")) {
            String[] timeString = sHours.split(":");
            int hours = Integer.parseInt(timeString[0]);
            int minutes = Integer.parseInt(timeString[1]);
            int seconds = Integer.parseInt(timeString[2]);

            if (seconds >= 60) {
                minutes += seconds / 60;
                seconds %= 60;
            }

            if (minutes >= 60) {
                hours += minutes / 60;
                minutes %= 60;
            }
            if (hours == 0) {
                return "0 H "+ minutes + " M ";
            } else {
                return hours + " H " + minutes + " M ";
            }
        } else {
            return sDays + " D";
        }
    }
    private LatLng interpolate(LatLng startPosition, LatLng endPosition, float t) {
        double lat = (1 - t) * startPosition.latitude + t * endPosition.latitude;
        double lng = (1 - t) * startPosition.longitude + t * endPosition.longitude;
        return new LatLng(lat, lng);
    }

    private String TimeConverter(String dateString, String minutesToAdd) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a", Locale.ENGLISH);
        String updatedDateString = null;
        try {
            Date date = dateFormat.parse(dateString);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(Calendar.MINUTE, Integer.parseInt(minutesToAdd));
            Date updatedDate = calendar.getTime();
            updatedDateString = outputFormat.format(updatedDate);
            Log.e("new Time: ",updatedDateString);
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return updatedDateString != null ? updatedDateString : "";
    }

}