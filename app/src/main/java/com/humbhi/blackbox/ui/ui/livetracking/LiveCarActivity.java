package com.humbhi.blackbox.ui.ui.livetracking;

import static com.humbhi.blackbox.ui.utils.MapUtils.getBearing;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.humbhi.blackbox.R;
import com.humbhi.blackbox.databinding.ActivityLiveCarBinding;
import com.humbhi.blackbox.ui.Utility.Constants;
import com.humbhi.blackbox.ui.Utility.WebViewActivity;
import com.humbhi.blackbox.ui.adapters.VehicleAlertAdapter;
import com.humbhi.blackbox.ui.data.db.CommonData;
import com.humbhi.blackbox.ui.data.models.AlertForApp;
import com.humbhi.blackbox.ui.data.models.CommonResponseMQTT;
import com.humbhi.blackbox.ui.data.models.LiveMovementModel;
import com.humbhi.blackbox.ui.data.network.api.ApiEndpoints;
import com.humbhi.blackbox.ui.retofit.NetworkService;
import com.humbhi.blackbox.ui.retofit.NewRetrofitClient;
import com.humbhi.blackbox.ui.retofit.Retrofit2;
import com.humbhi.blackbox.ui.retofit.RetrofitResponse;
import com.humbhi.blackbox.ui.ui.notification.GNotifications;
import com.humbhi.blackbox.ui.ui.notification.NotificationActivity;
import com.humbhi.blackbox.ui.ui.routePlayback.RoutePlayBack;
import com.humbhi.blackbox.ui.utils.ApiCallsHelper.ApiClient;
import com.humbhi.blackbox.ui.utils.CommonUtil;
import com.humbhi.blackbox.ui.utils.LocationAddress;
import com.humbhi.blackbox.ui.utils.MqttHelper;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import info.mqtt.android.service.Ack;
import info.mqtt.android.service.MqttAndroidClient;
import kotlinx.coroutines.GlobalScope;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveCarActivity extends FragmentActivity implements OnMapReadyCallback, RetrofitResponse,MqttHelper.MqttMessageCallback  {
    private static final long DELAY = 4500;
    private static final long ANIMATION_TIME_PER_ROUTE = 4000;
    GoogleMap googleMap;
    private SupportMapFragment mapFragment;
    private Handler handler;
    private Marker carMarker;
    private LatLng startPosition;
    private LatLng endPosition;
    private float v;
    List<LatLng> polyLineList;
    private double lat, lng;
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
            String vehicleNameIcon = getIntent().getStringExtra("vehicleNameIcon");
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
            NetworkService api = NewRetrofitClient.INSTANCE.getInstance().create(NetworkService.class);
            executor.execute(()->{
                Call<CommonResponseMQTT> call = api.updateMQTTCmd("J860181063592734","0");
                call.enqueue(new Callback<CommonResponseMQTT>() {
                    @Override
                    public void onResponse(Call<CommonResponseMQTT> call, Response<CommonResponseMQTT> response) {
                        executor.execute(()->{
                            mqttHelper.unsubscribe("Live/J860181063593567" , new IMqttActionListener() {
                                @Override
                                public void onSuccess(IMqttToken asyncActionToken) {
                                    // Main Thread
                                    handlerMain.post(() -> {
                                        // Start the screen timeout countdown
                                        startGettingOnlineDataFromCar();
                                        Log.e("Message", "Topic Unsubscribed successfully");
                                    });
                                }

                                @Override
                                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                                    // Handle subscription failure
                                    handlerMain.post(() -> {
                                        Log.e("Message", "Topic Unsubscription unsuccessfully");
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
                Log.e("Message", "Topic Subscribed successfully");
            });
            finish();
        };
        binding.tvPlayback.setOnClickListener(v -> {
            backendStartDate = CommonUtil.INSTANCE.getCurrentDate();
            backendEndDate = CommonUtil.INSTANCE.getCurrentDate();
            Bundle bundle = new Bundle();
            Intent intent = new Intent(LiveCarActivity.this, RoutePlayBack.class);
            intent.putExtra("tableName",vehicleId);
            intent.putExtra("fromDate",backendStartDate);
            intent.putExtra("endDate",backendEndDate);
            intent.putExtra("vehicleName",vehicleName);
            intent.putExtra("flag","simple");
            startActivity(intent);
        });

        binding.mapView.setOnClickListener(v -> {
            if(binding.mapView.getText()=="Satelite View"){
                googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                binding.mapView.setText("Normal View");
            }
            else{
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                binding.mapView.setText("Satelite View");
            }
        });
    }

    private void setToolbar(){
        binding.toolbar.ivMenu.setVisibility(View.GONE);
        binding.toolbar.ivBell.setVisibility(View.GONE);
        binding.toolbar.ivBack.setVisibility(View.VISIBLE);
        binding.toolbar.tvTitle.setText(vehicleName);
        binding.toolbar.ivBack.setOnClickListener(view -> {
            stopRepeatingTask();
            startScreenTimeout();
            mqttHelper.disconnect();
            finish();
        });
    }

    void stopRepeatingTask() {
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopRepeatingTask();
        stopScreenTimeout();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        googleMap.setTrafficEnabled(false);
        googleMap.setIndoorEnabled(false);
        googleMap.setBuildingsEnabled(false);
        //googleMap.getUiSettings().setZoomControlsEnabled(true);
    }

    private void runMQTTTask(){
        executor.execute(() -> {
            mqttHelper.connect(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // Connected successfully, perform further operations
                    //J860181063593567
                    mqttHelper.subscribe("Live/J860181063592734" , 1, new IMqttActionListener() {
                        @Override
                        public void onSuccess(IMqttToken asyncActionToken) {
                            // Main Thread
                            handlerMain.post(() -> {
                                // Start the screen timeout countdown
                                startScreenTimeout();
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
         new Retrofit2(LiveCarActivity.this, this, 200,
                ApiEndpoints.GET_VEHICLE_DETAILS + "custid=" + CommonData.INSTANCE.getCustIdFromDB()
                        + "&StatusCode=&sEcho=0&iDisplayStart=0&iDisplayLength=1&sSearch="+vehicleName+"&iSortCol_0=0&sSortDir_0").callService(false);
    }

    private void startBikeAnimation(final LatLng start, final LatLng end, String progresstatus, String vehicleType) {

        Log.i(TAG, "startBikeAnimation called...");

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(ANIMATION_TIME_PER_ROUTE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(valueAnimator1 -> {
            if (!vehicleType.equals("Other")){

                if(vehicleType.equalsIgnoreCase("OilTanker")) {
                    switch (progresstatus) {
                        case "Unreachable":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_red_truck_final));
                            break;
                        case "Stopped":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_yellow_truck_final));
                            break;
                        case "IgnitionOn":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_orange_truck_final));
                            break;
                        case "Moving":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_green_truck_final));
                            break;
                        case "Towed":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_white_truck_final));
                            break;
                        case "Hispeed":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_blue_truck_final));
                            break;
                    }
                }
                else if(vehicleType.equalsIgnoreCase("Car"))
                    switch (progresstatus){
                        case "Unreachable":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_car_red));
                            break;
                        case "Stopped":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_car_yellow));
                            break;
                        case "IgnitionOn":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_car_orange));
                            break;
                        case "Moving":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_car_green_with_shadow));
                            break;
                        case "Towed":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_car_white));
                            break;
                        case "Hispeed":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_car_blue_with_shadow));
                            break;
                    }
                else if (vehicleType.equalsIgnoreCase("Bus"))
                    switch (progresstatus) {
                        case "Unreachable":
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
                else if(vehicleType.equalsIgnoreCase("Ambulance"))
                    switch (progresstatus){
                        case "Unreachable":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_red_truck_final));
                            break;
                        case "Stopped":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_yellow_truck_final));
                            break;
                        case "IgnitionOn":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_orange_truck_final));
                            break;
                        case "Moving":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_green_truck_final));
                            break;
                        case "Towed":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_white_truck_final));
                            break;
                        case "Hispeed":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_blue_truck_final));
                            break;
                    }
                else if(vehicleType.equalsIgnoreCase("Truck"))
                    switch (progresstatus){
                        case "Unreachable":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_red_truck_final));
                            break;
                        case "Stopped":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_yellow_truck_final));
                            break;
                        case "IgnitionOn":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_orange_truck_final));
                            break;
                        case "Moving":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_green_truck_final));
                            break;
                        case "Towed":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_white_truck_final));
                            break;
                        case "Hispeed":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_blue_truck_final));
                            break;
                    }
                else if(vehicleType.equalsIgnoreCase("RoadRoller"))
                {
                    switch (progresstatus){
                        case "Unreachable":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_car_red));
                            break;
                        case "Stopped":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_car_yellow));
                            break;
                        case "IgnitionOn":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_car_orange));
                            break;
                        case "Moving":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_car_green_with_shadow));
                            break;
                        case "Towed":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_car_white));
                            break;
                        case "Hispeed":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_car_blue_with_shadow));
                            break;
                    }
                }
                else {
                    switch (progresstatus){
                        case "Unreachable":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_red_truck_final));
                            break;
                        case "Stopped":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_yellow_truck_final));
                            break;
                        case "IgnitionOn":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_orange_truck_final));
                            break;
                        case "Moving":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_green_truck_final));
                            break;
                        case "Towed":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_white_truck_final));
                            break;
                        case "Hispeed":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_blue_truck_final));
                            break;
                    }
                }
            }
            else {
                switch (progresstatus){
                    case "Unreachable":
                        carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_red_truck_final));
                        break;
                    case "Stopped":
                        carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_yellow_truck_final));
                        break;
                    case "IgnitionOn":
                        carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_orange_truck_final));
                        break;
                    case "Moving":
                        carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_green_truck_final));
                        break;
                    case "Towed":
                        carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_white_truck_final));
                        break;
                    case "Hispeed":
                        carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_blue_truck_final));
                        break;
                }
            }

            //LogMe.i(TAG, "Car Animation Started...");
            v = valueAnimator1.getAnimatedFraction();
            lng = v * end.longitude + (1 - v)
                    * start.longitude;
            lat = v * end.latitude + (1 - v)
                    * start.latitude;

            LatLng newPos = new LatLng(lat, lng);
            carMarker.setPosition(newPos);
            carMarker.setAnchor(0.5f, 0.5f);
            carMarker.setRotation(bearingBetweenLocations(start, end));

            googleMap.moveCamera(CameraUpdateFactory
                    .newCameraPosition
                            (new CameraPosition.Builder()
                                    .target(newPos)
                                    .zoom(15.5f)
                                    .build()));

            startPosition = carMarker.getPosition();
        });
        valueAnimator.start();
    }

    private void startBikeAnimationLiveMovement(final LatLng start, final LatLng end, String progresstatus, String vehicleType, Float angle) {

        Log.i(TAG, "startBikeAnimation called...");

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1);
        valueAnimator.setDuration(ANIMATION_TIME_PER_ROUTE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(valueAnimator1 -> {
            if (!vehicleType.equals("Other")){

                if(vehicleType.equalsIgnoreCase("OilTanker")) {
                    switch (progresstatus) {
                        case "Unreachable":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_red_truck_final));
                            break;
                        case "Stopped":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_yellow_truck_final));
                            break;
                        case "IgnitionOn":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_orange_truck_final));
                            break;
                        case "Moving":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_green_truck_final));
                            break;
                        case "Towed":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_white_truck_final));
                            break;
                        case "Hispeed":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_blue_truck_final));
                            break;
                    }
                }
                else if(vehicleType.equalsIgnoreCase("Car"))
                    switch (progresstatus){
                        case "Unreachable":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_car_red));
                            break;
                        case "Stopped":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_car_yellow));
                            break;
                        case "IgnitionOn":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_car_orange));
                            break;
                        case "Moving":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_car_green_with_shadow));
                            break;
                        case "Towed":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_car_white));
                            break;
                        case "Hispeed":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_car_blue_with_shadow));
                            break;
                    }
                else if (vehicleType.equalsIgnoreCase("Bus"))
                    switch (progresstatus) {
                        case "Unreachable":
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
                else if(vehicleType.equalsIgnoreCase("Ambulance"))
                    switch (progresstatus){
                        case "Unreachable":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_red_truck_final));
                            break;
                        case "Stopped":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_yellow_truck_final));
                            break;
                        case "IgnitionOn":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_orange_truck_final));
                            break;
                        case "Moving":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_green_truck_final));
                            break;
                        case "Towed":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_white_truck_final));
                            break;
                        case "Hispeed":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_blue_truck_final));
                            break;
                    }
                else if(vehicleType.equalsIgnoreCase("Truck"))
                    switch (progresstatus){
                        case "Unreachable":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_red_truck_final));
                            break;
                        case "Stopped":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_yellow_truck_final));
                            break;
                        case "IgnitionOn":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_orange_truck_final));
                            break;
                        case "Moving":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_green_truck_final));
                            break;
                        case "Towed":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_white_truck_final));
                            break;
                        case "Hispeed":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_blue_truck_final));
                            break;
                    }
                else if(vehicleType.equalsIgnoreCase("RoadRoller"))
                {
                    switch (progresstatus){
                        case "Unreachable":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_car_red));
                            break;
                        case "Stopped":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_car_yellow));
                            break;
                        case "IgnitionOn":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_car_orange));
                            break;
                        case "Moving":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_car_green_with_shadow));
                            break;
                        case "Towed":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_car_white));
                            break;
                        case "Hispeed":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_car_blue_with_shadow));
                            break;
                    }
                }
                else {
                    switch (progresstatus){
                        case "Unreachable":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_red_truck_final));
                            break;
                        case "Stopped":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_yellow_truck_final));
                            break;
                        case "IgnitionOn":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_orange_truck_final));
                            break;
                        case "Moving":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_green_truck_final));
                            break;
                        case "Towed":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_white_truck_final));
                            break;
                        case "Hispeed":
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_blue_truck_final));
                            break;
                    }
                }
            }
            else {
                switch (progresstatus){
                    case "Unreachable":
                        carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_red_truck_final));
                        break;
                    case "Stopped":
                        carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_yellow_truck_final));
                        break;
                    case "IgnitionOn":
                        carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_orange_truck_final));
                        break;
                    case "Moving":
                        carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_green_truck_final));
                        break;
                    case "Towed":
                        carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_white_truck_final));
                        break;
                    case "Hispeed":
                        carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_blue_truck_final));
                        break;
                }
            }

            //LogMe.i(TAG, "Car Animation Started...");
            v = valueAnimator1.getAnimatedFraction();
            lng = v * end.longitude + (1 - v)
                    * start.longitude;
            lat = v * end.latitude + (1 - v)
                    * start.latitude;

            LatLng newPos = new LatLng(lat, lng);
            carMarker.setPosition(newPos);
            carMarker.setAnchor(0.5f, 0.5f);
            carMarker.setRotation(angle);

            googleMap.moveCamera(CameraUpdateFactory
                    .newCameraPosition
                            (new CameraPosition.Builder()
                                    .target(newPos)
                                    .zoom(17.5f)
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
                Uri uriAc= Uri.parse("https://trackmaster.in"+obj.getString("AcStatus").replace("~",""));
                Glide.with(this).load(uriAc).into(binding.tvAcStatus);
                binding.tvFuelStatus.setText(obj.getString("FuelStatus"));
                Uri uriBattery = Uri.parse("https://trackmaster.in"+obj.getString("BatteryStatus").replace("~",""));
                Glide.with(this).load(uriBattery).into(binding.tvBatteryStatus);
                binding.tvIgnitionStatus.setText(obj.getString("IgnitionStatus"));
                String stoppageTime = obj.getString("StoppageTime");
                String parkAtLASTstoppageTime = obj.getString("ParkingTimeALasttStop");
                String TotalParkingTime = obj.getString("TotalParkingTime");
                String MovingFromLastStop = obj.getString("MovingFromLastStop");
                String TotalMoving = obj.getString("TotalMoving");
                String ParkAtLASTstoppageTime = CommonUtil.INSTANCE.calculateStoppageTime(parkAtLASTstoppageTime);
                StringTokenizer ParkAtLASTstoppageTimetokenizer = new StringTokenizer(ParkAtLASTstoppageTime," ");
                String ParkAtLASTstoppageTimeminutes = ParkAtLASTstoppageTimetokenizer.nextToken();
                String ParkAtLASTstoppageTimeseconds = ParkAtLASTstoppageTimetokenizer.nextToken();
                int ParkAtLASTstoppageTimeminutesValue  = Integer.parseInt(ParkAtLASTstoppageTimeminutes.replace("M",""));
                int ParkAtLASTstoppageTimehour = ParkAtLASTstoppageTimeminutesValue/60;
                int ParkAtLASTstoppageTimeMinutes = ParkAtLASTstoppageTimeminutesValue%60;
                binding.tvStoppageTime.setText(CommonUtil.INSTANCE.calculateStoppageTime(stoppageTime));
                binding.tvParkAtLastStop.setText(ParkAtLASTstoppageTimehour+"H "+ParkAtLASTstoppageTimeMinutes+"M");
                String totalParkingTime = CommonUtil.INSTANCE.calculateStoppageTime(TotalParkingTime);
                StringTokenizer totalParkingTimetokenizer = new StringTokenizer(CommonUtil.INSTANCE.calculateStoppageTime(totalParkingTime)," ");
                String totalParkingTimeminutes = totalParkingTimetokenizer.nextToken();
                String totalParkingTimeseconds = totalParkingTimetokenizer.nextToken();
                int totalParkingTimeminutesValue  = Integer.parseInt(totalParkingTimeminutes.replace("M",""));
                int  totalParkingTimehour = totalParkingTimeminutesValue/60;
                int  totalParkingTimeMinutes = totalParkingTimeminutesValue%60;
                binding.tvParkingTimeTotal.setText(totalParkingTimehour+"H "+totalParkingTimeMinutes+"M");
                StringTokenizer movingFromLastStopTokenizer = new StringTokenizer(CommonUtil.INSTANCE.calculateStoppageTime(MovingFromLastStop)," ");
                String movingFromLastStopminutes = movingFromLastStopTokenizer.nextToken();
                String movingFromLastStopseconds = movingFromLastStopTokenizer.nextToken();
                int movingFromLastStopminutesValue  = Integer.parseInt(movingFromLastStopminutes.replace("M",""));
                int  movingFromLastStophour = movingFromLastStopminutesValue/60;
                int  movingFromLastStopMinutes = movingFromLastStopminutesValue%60;
                binding.tvMovingFromLastStop.setText(movingFromLastStophour+"H "+movingFromLastStopMinutes+"M");
                StringTokenizer TotalMovingTokenizer = new StringTokenizer(CommonUtil.INSTANCE.calculateStoppageTime(TotalMoving)," ");
                String TotalMovingminutes = TotalMovingTokenizer.nextToken();
                String TotalMovingseconds = TotalMovingTokenizer.nextToken();
                int TotalMovingminutesValue  = Integer.parseInt(TotalMovingminutes.replace("M",""));
                int  TotalMovinghour = TotalMovingminutesValue/60;
                int  TotalMovingMinutes = TotalMovingminutesValue%60;
                binding.tvTotalMovingTime.setText(TotalMovinghour+"H "+TotalMovingMinutes+"M");
                JSONArray alertData = obj.getJSONArray("AlertForApp");
                for (int j = 0; j <alertData.length() ; j++) {
                    JSONObject alertObj = alertData.getJSONObject(j);
                    AlertForApp alertModel = new AlertForApp(alertObj.getString("AlertDetails"),alertObj.getInt("Count"),alertObj.getInt("Id"));
                    alertList.add(alertModel);
                }
                progresstatus = obj.getString("progresstatus");
                binding.tvAlerts.setOnClickListener(v -> {
                    Intent intent = new Intent(LiveCarActivity.this, GNotifications.class);
                    startActivity(intent);
                });

                switch (progresstatus){
                    case "Unreachable":
                        binding.tvProgressStatus.setText("Unreachable");
                        binding.tvProgressStatus.setTextColor(getResources().getColor(R.color.white));
                        binding.tvProgressStatus.setBackgroundResource(R.drawable.round_bg_red);
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
                //        if(vehicleId.startsWith("J") || vehicleId.startsWith("E") && vehicleId.length()>5){
                            handler.removeCallbacks(mStatusChecker);
                            NetworkService api = NewRetrofitClient.INSTANCE.getInstance().create(NetworkService.class);
                            executor.execute(()->{
                                Call<CommonResponseMQTT> call = api.updateMQTTCmd("J860181063592734","300");
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
                                Log.e("Message", "Topic Subscribed successfully");
                            });
                   //     }
                        break;
                    case "Towed":
                        binding.tvProgressStatus.setText("Towed");
                        binding.tvProgressStatus.setTextColor(getResources().getColor(R.color.white_translucent_glass));
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
                StringTokenizer stringTokenizer = new StringTokenizer(obj.getString("StoppageTime"),"-");
                String days = stringTokenizer.nextToken();
                binding.tvStoppageTime.setText(days);
                if(progresstatus.equals("Unreachable")) {
                    carMarker = googleMap.addMarker(new MarkerOptions().position(getLocationFromAddress(this,location)).
                            flat(true).rotation((float) angle));

                    if (!vehicleType.equals("Other")){
                        if(vehicleType.equalsIgnoreCase("OilTanker"))
                            carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_red_truck_final));
                        else if(vehicleType.equalsIgnoreCase("Car"))
                            carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_car_red));
                        else if (vehicleType.equalsIgnoreCase("Bus"))
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_new_bus_red));
                        else if(vehicleType.equalsIgnoreCase("Ambulance"))
                            carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_red_truck_final));
                        else if(vehicleType.equalsIgnoreCase("Truck"))
                            carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_red_truck_final));
                        else if(vehicleType.equalsIgnoreCase("RoadRoller"))
                            carMarker.setIcon(bitmapDescriptorFromVector(LiveCarActivity.this, R.drawable.ic_car_red));
                        else
                            carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_car_red));
                    }
                    else {
                        carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_red_truck_final));
                    }
                    googleMap.moveCamera(CameraUpdateFactory
                            .newCameraPosition(new CameraPosition.Builder()
                                    .target(getLocationFromAddress(this,location))
                                    .zoom(17.5f)
                                    .build()));
                    stopRepeatingTask();
                }
            }

            Log.e("LATLONG--->", startLatitude + "--" + startLongitude);

            getAddress(this, startLatitude, startLongitude);


            if (isFirstPosition) {
                   startPosition = new LatLng(startLatitude, startLongitude);
                   carMarker = googleMap.addMarker(new MarkerOptions().position(startPosition).flat(true).rotation((float) angle));
               // carMarker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_green_truck_final));
                switch (progresstatus){
                    case "Unreachable":
                        binding.tvProgressStatus.setText("Unreachable");
                        binding.tvProgressStatus.setTextColor(getResources().getColor(R.color.white));
                        binding.tvProgressStatus.setBackgroundResource(R.drawable.round_bg_red);
                        break;
                    case "Stopped":
                        binding.tvProgressStatus.setText("Parked");
                        binding.tvProgressStatus.setTextColor(getResources().getColor(R.color.white));
                        binding.tvProgressStatus.setBackgroundResource(R.drawable.round_bg_yellow);
                        break;
                    case "IgnitionOn":
                        binding.tvProgressStatus.setText("Ignition On");
                        binding.tvProgressStatus.setBackgroundResource(R.drawable.round_background);
                        break;
                    case "Moving":
                        binding.tvProgressStatus.setText("Moving");
                        binding.tvProgressStatus.setTextColor(getResources().getColor(R.color.white));
                        binding.tvProgressStatus.setBackgroundResource(R.drawable.round_bg_green);
                        break;
                    case "Towed":
                        binding.tvProgressStatus.setText("Towed");
                        binding.tvProgressStatus.setTextColor(getResources().getColor(R.color.white_translucent_glass));
                        binding.tvProgressStatus.setBackgroundResource(R.drawable.round_bg_creame);
                        break;
                    case "Hispeed":
                        binding.tvProgressStatus.setText("Hi-speed");
                        binding.tvProgressStatus.setTextColor(getResources().getColor(R.color.white));
                        binding.tvProgressStatus.setBackgroundResource(R.drawable.round_bg_blue);
                        break;
                }
                if (!vehicleType.equals("Other")){

                    if(vehicleType.equalsIgnoreCase("OilTanker")) {
                        switch (progresstatus) {
                            case "Unreachable":
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
                    else if(vehicleType.equalsIgnoreCase("Car"))
                        switch (progresstatus){
                            case "Unreachable":
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
                    else if(vehicleType.equalsIgnoreCase("Ambulance"))
                        switch (progresstatus){
                            case "Unreachable":
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
                    else if(vehicleType.equalsIgnoreCase("Truck"))
                        switch (progresstatus){
                            case "Unreachable":
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
                    else if(vehicleType.equalsIgnoreCase("RoadRoller"))
                    {
                        switch (progresstatus){
                            case "Unreachable":
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
                    else {
                        switch (progresstatus){
                                case "Unreachable":
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
                }
                else {
                    switch (progresstatus){
                        case "Unreachable":
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

                carMarker.setAnchor(0.5f, 0.5f);
                googleMap.moveCamera(CameraUpdateFactory
                        .newCameraPosition(new CameraPosition.Builder()
                                .target(startPosition)
                                .zoom(17.5f)
                                .build()));

                isFirstPosition = false;

            } else {
                endPosition = new LatLng(startLatitude, startLongitude);

                Log.d(TAG, startPosition.latitude + "--" + endPosition.latitude + "--Check --" + startPosition.longitude + "--" + endPosition.longitude);

                if ((startPosition.latitude != endPosition.latitude) || (startPosition.longitude != endPosition.longitude)) {

                    Log.e(TAG, "NOT SAME");

                    startBikeAnimation(startPosition, endPosition,progresstatus,vehicleType);
                    googleMap.addPolyline(new PolylineOptions()
                            .add(endPosition)
                            .width(10f)
                            .color(Color.BLACK));
                } else {
                    Log.e(TAG, "SAMME");
                }
            }

        } catch (Exception e ) {
            if (e instanceof SocketTimeoutException) {
                Toast.makeText(LiveCarActivity.this, "Connection time out.", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(LiveCarActivity.this, "Something went wrong.", Toast.LENGTH_SHORT).show();
            }
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

    // get exact location using latitude and longi

    public String getAddress(Context context, double lat, double lng) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
            Address obj = addresses.get(0);

            String add = obj.getAddressLine(0);
            location = add;

            return add;
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
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
        mqttHelper.disconnect();
        stopScreenTimeout();
        stopRepeatingTask();
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Restart the screen timeout countdown when the activity resumes
        startScreenTimeout();
    }

    @Override
    public void onMessageReceived(String topic, MqttMessage message) {
        String payload = new String(message.getPayload());
        JSONObject object;
        try {
            object = new JSONObject(payload);
            if(!object.getString("speed").equals("0.0")) {
                if (isFirstPosition) {
                    startPosition = new LatLng(object.getDouble("lati"), object.getDouble("longi"));
                    LocationAddress.getAddressFromLocation(object.getDouble("lati"), object.getDouble("longi"), this, address -> {
                        if(address.equals("") || address != null){
                            binding.tvLocation.setText(address);
                        }
                    });
                    carMarker = googleMap.addMarker(new MarkerOptions().position(startPosition).
                            flat(true).rotation(Float.parseFloat(object.getString("angle"))));
                    carMarker.setAnchor(0.5f, 0.5f);
                    binding.tvDataDate.setText("Updated at: " + object.getString("datadate"));
                    binding.tvSpeed.setText(object.getString("speed") + " K/H");
                    binding.tvDistance.setText(object.getString("distance") + " KM");
                    googleMap.moveCamera(CameraUpdateFactory
                            .newCameraPosition(new CameraPosition.Builder()
                                    .target(new LatLng(object.getDouble("lati"), object.getDouble("longi")))
                                    .zoom(17.5f)
                                    .build()));
                    isFirstPosition = false;
                }
                else {
                    endPosition = new LatLng(object.getDouble("lati"), object.getDouble("longi"));
                    LocationAddress.getAddressFromLocation(object.getDouble("lati"), object.getDouble("longi"), this, address -> {
                        if(address.equals("") || address != null){
                            binding.tvLocation.setText(address);
                        }
                    });
                    binding.tvDataDate.setText("Updated at: " + object.getString("datadate"));
                    binding.tvSpeed.setText(object.getString("speed") + " K/H");
                    binding.tvDistance.setText(object.getString("distance") + " KM");
                    Log.d(TAG, startPosition.latitude + "--" + endPosition.latitude + "--Check --" + startPosition.longitude + "--" + endPosition.longitude);

                    if ((startPosition.latitude != endPosition.latitude) || (startPosition.longitude != endPosition.longitude)) {

                        Log.e(TAG, "NOT SAME");

                        startBikeAnimationLiveMovement(startPosition, endPosition, progresstatus, vehicleType,Float.parseFloat(object.getString("angle")));
                    }
                    Log.e("Message", payload);
                }
            }
            else{
                NetworkService api = NewRetrofitClient.INSTANCE.getInstance().create(NetworkService.class);
                executor.execute(()->{
                    Call<CommonResponseMQTT> call = api.updateMQTTCmd("J860181063593567","0");
                    call.enqueue(new Callback<CommonResponseMQTT>() {
                        @Override
                        public void onResponse(Call<CommonResponseMQTT> call, Response<CommonResponseMQTT> response) {
                            executor.execute(()->{
                                mqttHelper.unsubscribe("Live/J860181063592734" , new IMqttActionListener() {
                                    @Override
                                    public void onSuccess(IMqttToken asyncActionToken) {
                                        // Main Thread
                                        handlerMain.post(() -> {
                                            // Start the screen timeout countdown
                                            startGettingOnlineDataFromCar();
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
                    Log.e("Message", "Topic Subscribed successfully");
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startScreenTimeout() {
        // Cancel any existing screen timeout countdown
        screenTimeoutHandler.postDelayed(screenTimeoutRunnable, TEN_MINUTES_IN_MILLIS);
    }

    private void stopScreenTimeout() {
        screenTimeoutHandler.removeCallbacks(screenTimeoutRunnable);
    }
}