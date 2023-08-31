package com.humbhi.blackbox.ui.ui.livetracking;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.humbhi.blackbox.R;
import com.humbhi.blackbox.ui.MyApplication;
import com.humbhi.blackbox.ui.Utility.WhatsNewDialogFragment;
import com.humbhi.blackbox.ui.data.AisModel;
import com.humbhi.blackbox.ui.data.Table;
import com.humbhi.blackbox.ui.data.db.CommonData;
import com.humbhi.blackbox.ui.retofit.NetworkService;
import com.humbhi.blackbox.ui.retofit.NewRetrofitClient;
import com.humbhi.blackbox.ui.retofit.Retrofit2;
import com.humbhi.blackbox.ui.retofit.RetrofitResponse;
import com.humbhi.blackbox.ui.ui.banner.BillBanner;
import com.humbhi.blackbox.ui.ui.dashboard.DashboardActivity;
import com.humbhi.blackbox.ui.utils.Constants;
import com.humbhi.blackbox.ui.utils.DateFormatter;
import com.humbhi.blackbox.ui.utils.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GLocationOnMap extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLoadedCallback
        ,GoogleMap.OnInfoWindowClickListener, GoogleApiClient.ConnectionCallbacks
        , GoogleApiClient.OnConnectionFailedListener, View.OnClickListener , RetrofitResponse {
    String latitude = "",longitude = "",location="";
    private GoogleMap Map;
    private Marker marker;
    double lat,longi;
    SupportMapFragment mapFragment;
    Boolean isFirstTime = true;
    private ImageView icClose,ivMenu,ivBell,ivBack;
    HashMap<Marker, Object> hm1;
    private LinearLayout llTotal,llRunning,llStopped,llInactive,llIdeal,llTowed,llHiSpeed;
    private RadioButton rbTotal,rbParked,rbMoving,rbIgnition,rbUnreach,rbHighSpeed,rbTowed;
    private CardView cvDetails;
    private TextView tvSpeed,tvDistance,tvLocation,tvProgressStatus,tvDataDate,tvTitle,tvTotalVehicles
            ,tvParkedVehicle,tvMovingCount,tvIgnitionCount,tvUnreachCount,tvTowdCount,tvHighSpeedCount,tvVehicleName;
    private String clientId,VehicleStatus="",vehicleType,statusCode="";
    Handler handler = new Handler(Looper.getMainLooper());
    Runnable runnable;
    String mapType = "";
    ArrayList<Double> arrLatitude = new ArrayList<Double>();
    ArrayList<Double> arrLongitude = new ArrayList<Double>();
    ArrayList<Marker> markers = new ArrayList<Marker>();
    private ImageView mapView;
    private String[] mapTypes = {"Standard", "Satellite", "Terrain", "Hybrid"};
    Boolean StatusChangeFromOption = false;
    private FrameLayout progress;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Handler mainHandler = new Handler(Looper.getMainLooper());
    String SoftBanner = "";
    String hardBanner = "";
    int aisCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glocation_on_map);
        if (Map == null) {
            mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.Mmap);
            mapFragment.getMapAsync(this);
        }
        tvMovingCount = findViewById(R.id.tvMovingCount);
        tvTowdCount = findViewById(R.id.tvTowdCount);
        tvUnreachCount = findViewById(R.id.tvUnreachCount);
        tvIgnitionCount = findViewById(R.id.tvIgnitionCount);
        tvParkedVehicle = findViewById(R.id.tvParkedVehicle);
        tvHighSpeedCount = findViewById(R.id.tvHighSpeedCount);
        tvSpeed = findViewById(R.id.tvSpeed);
        tvVehicleName = findViewById(R.id.tvVehicleName);
        icClose = findViewById(R.id.icClose);
        tvDistance = findViewById(R.id.tvDistance);
        tvLocation = findViewById(R.id.tvLocation);
        tvProgressStatus = findViewById(R.id.tvProgressStatus);
        tvDataDate = findViewById(R.id.tvDataDate);
        cvDetails = findViewById(R.id.cvDetails);
        llTotal = findViewById(R.id.llTotal);
        llRunning = findViewById(R.id.llRunning);
        progress = findViewById(R.id.progress);
        llStopped = findViewById(R.id.llStopped);
        llInactive = findViewById(R.id.llInactive);
        llIdeal = findViewById(R.id.llIdeal);
        llTowed = findViewById(R.id.llTowed);
        llHiSpeed = findViewById(R.id.llHiSpeed);
        rbTotal = findViewById(R.id.rbTotal);
        ivMenu = findViewById(R.id.ivMenu);
        tvTitle = findViewById(R.id.tvTitle);
        ivBell = findViewById(R.id.ivBell);
        ivBack = findViewById(R.id.ivBack);
        rbParked = findViewById(R.id.rbParked);
        rbMoving = findViewById(R.id.rbMoving);
        rbIgnition = findViewById(R.id.rbIgnition);
        rbUnreach = findViewById(R.id.rbUnreach);
        rbTowed = findViewById(R.id.rbTowed);
        rbHighSpeed = findViewById(R.id.rbHiSpeed);
        tvTotalVehicles = findViewById(R.id.tvTotalVehicles);
        mapView = findViewById(R.id.mapView);
        tvTitle.setText("Map");
        ivMenu.setVisibility(View.GONE);
        ivBell.setVisibility(View.GONE);
        ivBack.setVisibility(View.VISIBLE);
        llTotal.setOnClickListener(this);
        rbTotal.setOnClickListener(this);
        rbTotal.setChecked(true);
        llStopped.setOnClickListener(this);
        rbParked.setOnClickListener(this);
        llRunning.setOnClickListener(this);
        rbMoving.setOnClickListener(this);
        llIdeal.setOnClickListener(this);
        rbIgnition.setOnClickListener(this);
        llInactive.setOnClickListener(this);
        rbUnreach.setOnClickListener(this);
        llHiSpeed.setOnClickListener(this);
        rbHighSpeed.setOnClickListener(this);
        llTowed.setOnClickListener(this);
        rbTowed.setOnClickListener(this);
        ivBack.setOnClickListener(view -> {
            handler.removeCallbacksAndMessages(null);
            Intent intent = new Intent(this, DashboardActivity.class);
            startActivity(intent);
            finish();
            if (Network.isNetworkAvailable(this)) {
                new Retrofit2(
                        this, this,
                        Constants.REQ_EXPIRE_ACCOUNT_DETAILS,
                        Constants.EXPIRE_ACCOUNT_DETAILS
                                + "custid=" + CommonData.INSTANCE.getCustIdFromDB()
                ).callService(false);
            }
        });
        if(MyApplication.cantSkip.equals("yes")) {
            getAisData();
        }
        runnable = new Runnable() {
            @Override
            public void run() {
                getLocationOnMap();
                handler.postDelayed(this,60000);
            }
        };
        handler.post(runnable);
        // connect();

        mapView.setOnClickListener(v -> {
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
            case "Standard" : Map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                break;
            case "Hybrid" : Map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                break;
            case "Terrain" : Map.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                break;
            case "Satellite" : Map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                break;
        }
    }

    private void getLocationOnMap()
    {
        if(isFirstTime) {
            progress.setVisibility(View.VISIBLE);
            new Retrofit2(this, this, Constants.REQ_VEHICLES_ON_MAP,
                    Constants.VEHICLES_ON_MAP + "custid=" + CommonData.INSTANCE.getCustIdFromDB() + "&StatusCode=" + statusCode + "&sEcho=0&iDisplayStart=0&iDisplayLength=999&sSearch=&iSortCol_0=0&sSortDir_0=")
                    .callService(false);
        }
        else if(StatusChangeFromOption==true){
            progress.setVisibility(View.VISIBLE);
            new Retrofit2(this, this, Constants.REQ_VEHICLES_ON_MAP,
                    Constants.VEHICLES_ON_MAP + "custid=" + CommonData.INSTANCE.getCustIdFromDB() + "&StatusCode=" + statusCode + "&sEcho=0&iDisplayStart=0&iDisplayLength=999&sSearch=&iSortCol_0=0&sSortDir_0=")
                    .callService(false);
        }
        else{
            new Retrofit2(this, this, Constants.REQ_VEHICLES_ON_MAP,
                    Constants.VEHICLES_ON_MAP + "custid=" + CommonData.INSTANCE.getCustIdFromDB() + "&StatusCode=" + statusCode + "&sEcho=0&iDisplayStart=0&iDisplayLength=999&sSearch=&iSortCol_0=0&sSortDir_0=")
                    .callService(false);
        }
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
                                Constants.Toastmsg(GLocationOnMap.this, getString(finalErrorMessage));
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
                            WhatsNewDialogFragment dialogFragment = new WhatsNewDialogFragment(GLocationOnMap.this, expiry, Objects.requireNonNull(vehicleWithLeastValidity.getCommercialvalidity()), Objects.requireNonNull(vehicleWithLeastValidity.getVehname()));
                            dialogFragment.show(getSupportFragmentManager(), "WhatsNewDialog");
                        }
                    }
                });
            }
        }
    }



    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.llTotal:
                statusCode = "";
                // getLocationOnMap();
                if (llStopped.isShown()){
                    rbTotal.setChecked(true);
                    rbParked.setChecked(false);
                    rbMoving.setChecked(false);
                    rbUnreach.setChecked(false);
                    rbIgnition.setChecked(false);
                    rbHighSpeed.setChecked(false);
                    StatusChangeFromOption=true;
                    getLocationOnMap();
                    llStopped.setVisibility(View.GONE);
                    llRunning.setVisibility(View.GONE);
                    llIdeal.setVisibility(View.GONE);
                    llInactive.setVisibility(View.GONE);
                    llTowed.setVisibility(View.GONE);
                    llHiSpeed.setVisibility(View.GONE);
                }
                else {
                    llStopped.setVisibility(View.VISIBLE);
                    llRunning.setVisibility(View.VISIBLE);
                    llIdeal.setVisibility(View.VISIBLE);
                    llInactive.setVisibility(View.VISIBLE);
                    llTowed.setVisibility(View.GONE);
                    llHiSpeed.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rbTotal:
                statusCode = "";
                if (llStopped.isShown()){
                    rbTotal.setChecked(true);
                    rbParked.setChecked(false);
                    rbMoving.setChecked(false);
                    rbUnreach.setChecked(false);
                    rbIgnition.setChecked(false);
                    rbHighSpeed.setChecked(false);
                    StatusChangeFromOption=true;
                    getLocationOnMap();
                    llStopped.setVisibility(View.GONE);
                    llRunning.setVisibility(View.GONE);
                    llIdeal.setVisibility(View.GONE);
                    llInactive.setVisibility(View.GONE);
                    llTowed.setVisibility(View.GONE);
                    llHiSpeed.setVisibility(View.GONE);
                }
                else {
                    llStopped.setVisibility(View.VISIBLE);
                    llRunning.setVisibility(View.VISIBLE);
                    llIdeal.setVisibility(View.VISIBLE);
                    llInactive.setVisibility(View.VISIBLE);
                    llTowed.setVisibility(View.GONE);
                    llHiSpeed.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.llStopped:
                statusCode = "P";
                if(llTotal.isShown()) {
                    rbParked.setChecked(true);
                    rbMoving.setChecked(false);
                    rbUnreach.setChecked(false);
                    rbIgnition.setChecked(false);
                    rbHighSpeed.setChecked(false);
                    rbTotal.setChecked(false);
                    StatusChangeFromOption = true;
                    llTotal.setVisibility(View.GONE);
                    llStopped.setVisibility(View.VISIBLE);
                    llRunning.setVisibility(View.GONE);
                    llIdeal.setVisibility(View.GONE);
                    llInactive.setVisibility(View.GONE);
                    llTowed.setVisibility(View.GONE);
                    llHiSpeed.setVisibility(View.GONE);
                    getLocationOnMap();
                }
                else{
                    llTotal.setVisibility(View.VISIBLE);
                    llStopped.setVisibility(View.VISIBLE);
                    llRunning.setVisibility(View.VISIBLE);
                    llIdeal.setVisibility(View.VISIBLE);
                    llInactive.setVisibility(View.VISIBLE);
                    llTowed.setVisibility(View.GONE);
                    llHiSpeed.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rbParked:
                statusCode = "P";
                if(llTotal.isShown()) {
                    rbParked.setChecked(true);
                    rbMoving.setChecked(false);
                    rbUnreach.setChecked(false);
                    rbIgnition.setChecked(false);
                    rbHighSpeed.setChecked(false);
                    rbTotal.setChecked(false);
                    StatusChangeFromOption = true;
                    llTotal.setVisibility(View.GONE);
                    llStopped.setVisibility(View.VISIBLE);
                    llRunning.setVisibility(View.GONE);
                    llIdeal.setVisibility(View.GONE);
                    llInactive.setVisibility(View.GONE);
                    llTowed.setVisibility(View.GONE);
                    llHiSpeed.setVisibility(View.GONE);
                    getLocationOnMap();
                }
                else{
                    llTotal.setVisibility(View.VISIBLE);
                    llStopped.setVisibility(View.VISIBLE);
                    llRunning.setVisibility(View.VISIBLE);
                    llIdeal.setVisibility(View.VISIBLE);
                    llInactive.setVisibility(View.VISIBLE);
                    llTowed.setVisibility(View.GONE);
                    llHiSpeed.setVisibility(View.VISIBLE);
                }
                break;

            case R.id.llRunning:
                statusCode = "M";
                if(llTotal.isShown()) {
                    rbParked.setChecked(false);
                    rbMoving.setChecked(true);
                    rbUnreach.setChecked(false);
                    rbIgnition.setChecked(false);
                    rbHighSpeed.setChecked(false);
                    rbTotal.setChecked(false);
                    StatusChangeFromOption = true;
                    getLocationOnMap();
                    llTotal.setVisibility(View.GONE);
                    llStopped.setVisibility(View.GONE);
                    llRunning.setVisibility(View.VISIBLE);
                    llIdeal.setVisibility(View.GONE);
                    llInactive.setVisibility(View.GONE);
                    llTowed.setVisibility(View.GONE);
                    llHiSpeed.setVisibility(View.GONE);
                }
                else{
                    llTotal.setVisibility(View.VISIBLE);
                    llStopped.setVisibility(View.VISIBLE);
                    llRunning.setVisibility(View.VISIBLE);
                    llIdeal.setVisibility(View.VISIBLE);
                    llInactive.setVisibility(View.VISIBLE);
                    llTowed.setVisibility(View.GONE);
                    llHiSpeed.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rbMoving:
                statusCode = "M";
                if(llTotal.isShown()) {
                    rbParked.setChecked(false);
                    rbMoving.setChecked(true);
                    rbUnreach.setChecked(false);
                    rbIgnition.setChecked(false);
                    rbHighSpeed.setChecked(false);
                    rbTotal.setChecked(false);
                    StatusChangeFromOption = true;
                    getLocationOnMap();
                    llTotal.setVisibility(View.GONE);
                    llStopped.setVisibility(View.GONE);
                    llRunning.setVisibility(View.VISIBLE);
                    llIdeal.setVisibility(View.GONE);
                    llInactive.setVisibility(View.GONE);
                    llTowed.setVisibility(View.GONE);
                    llHiSpeed.setVisibility(View.GONE);
                }
                else{
                    llTotal.setVisibility(View.VISIBLE);
                    llStopped.setVisibility(View.VISIBLE);
                    llRunning.setVisibility(View.VISIBLE);
                    llIdeal.setVisibility(View.VISIBLE);
                    llInactive.setVisibility(View.VISIBLE);
                    llTowed.setVisibility(View.GONE);
                    llHiSpeed.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rbIgnition:
                statusCode = "I";
                if(llTotal.isShown()) {
                    rbParked.setChecked(false);
                    rbMoving.setChecked(false);
                    rbUnreach.setChecked(false);
                    rbIgnition.setChecked(true);
                    rbHighSpeed.setChecked(false);
                    rbTotal.setChecked(false);
                    StatusChangeFromOption = true;
                    getLocationOnMap();
                    llTotal.setVisibility(View.GONE);
                    llStopped.setVisibility(View.GONE);
                    llRunning.setVisibility(View.GONE);
                    llIdeal.setVisibility(View.VISIBLE);
                    llInactive.setVisibility(View.GONE);
                    llTowed.setVisibility(View.GONE);
                    llHiSpeed.setVisibility(View.GONE);
                }
                else{
                    llTotal.setVisibility(View.VISIBLE);
                    llStopped.setVisibility(View.VISIBLE);
                    llRunning.setVisibility(View.VISIBLE);
                    llIdeal.setVisibility(View.VISIBLE);
                    llInactive.setVisibility(View.VISIBLE);
                    llTowed.setVisibility(View.GONE);
                    llHiSpeed.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.llIdeal:
                statusCode = "I";
                if(llTotal.isShown()) {
                    rbParked.setChecked(false);
                    rbMoving.setChecked(false);
                    rbUnreach.setChecked(false);
                    rbIgnition.setChecked(true);
                    rbHighSpeed.setChecked(false);
                    rbTotal.setChecked(false);
                    StatusChangeFromOption = true;
                    getLocationOnMap();
                    llTotal.setVisibility(View.GONE);
                    llStopped.setVisibility(View.GONE);
                    llRunning.setVisibility(View.GONE);
                    llIdeal.setVisibility(View.VISIBLE);
                    llInactive.setVisibility(View.GONE);
                    llTowed.setVisibility(View.GONE);
                    llHiSpeed.setVisibility(View.GONE);
                }
                else{
                    llTotal.setVisibility(View.VISIBLE);
                    llStopped.setVisibility(View.VISIBLE);
                    llRunning.setVisibility(View.VISIBLE);
                    llIdeal.setVisibility(View.VISIBLE);
                    llInactive.setVisibility(View.VISIBLE);
                    llTowed.setVisibility(View.GONE);
                    llHiSpeed.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.llInactive:
                statusCode = "U";
                if(llTotal.isShown()) {
                    rbParked.setChecked(false);
                    rbMoving.setChecked(false);
                    rbUnreach.setChecked(true);
                    rbIgnition.setChecked(false);
                    rbHighSpeed.setChecked(false);
                    rbTotal.setChecked(false);
                    StatusChangeFromOption = true;
                    getLocationOnMap();
                    llTotal.setVisibility(View.GONE);
                    llStopped.setVisibility(View.GONE);
                    llRunning.setVisibility(View.GONE);
                    llIdeal.setVisibility(View.GONE);
                    llInactive.setVisibility(View.VISIBLE);
                    llTowed.setVisibility(View.GONE);
                    llHiSpeed.setVisibility(View.GONE);
                }
                else{
                    llTotal.setVisibility(View.VISIBLE);
                    llStopped.setVisibility(View.VISIBLE);
                    llRunning.setVisibility(View.VISIBLE);
                    llIdeal.setVisibility(View.VISIBLE);
                    llInactive.setVisibility(View.VISIBLE);
                    llTowed.setVisibility(View.GONE);
                    llHiSpeed.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rbUnreach:
                statusCode = "U";
                if(llTotal.isShown()) {
                    rbParked.setChecked(false);
                    rbMoving.setChecked(false);
                    rbUnreach.setChecked(true);
                    rbIgnition.setChecked(false);
                    rbHighSpeed.setChecked(false);
                    rbTotal.setChecked(false);
                    StatusChangeFromOption = true;
                    getLocationOnMap();
                    llTotal.setVisibility(View.GONE);
                    llStopped.setVisibility(View.GONE);
                    llRunning.setVisibility(View.GONE);
                    llIdeal.setVisibility(View.GONE);
                    llInactive.setVisibility(View.VISIBLE);
                    llTowed.setVisibility(View.GONE);
                    llHiSpeed.setVisibility(View.GONE);
                }
                else{
                    llTotal.setVisibility(View.VISIBLE);
                    llStopped.setVisibility(View.VISIBLE);
                    llRunning.setVisibility(View.VISIBLE);
                    llIdeal.setVisibility(View.VISIBLE);
                    llInactive.setVisibility(View.VISIBLE);
                    llTowed.setVisibility(View.GONE);
                    llHiSpeed.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.rbHiSpeed:
                statusCode = "H";
                if(llTotal.isShown()) {
                    rbParked.setChecked(false);
                    rbMoving.setChecked(false);
                    rbUnreach.setChecked(false);
                    rbIgnition.setChecked(false);
                    rbHighSpeed.setChecked(true);
                    rbTotal.setChecked(false);
                    StatusChangeFromOption = true;
                    getLocationOnMap();
                    llTotal.setVisibility(View.GONE);
                    llStopped.setVisibility(View.GONE);
                    llRunning.setVisibility(View.GONE);
                    llIdeal.setVisibility(View.GONE);
                    llInactive.setVisibility(View.GONE);
                    llTowed.setVisibility(View.GONE);
                    llHiSpeed.setVisibility(View.VISIBLE);
                }
                else{
                    llTotal.setVisibility(View.VISIBLE);
                    llStopped.setVisibility(View.VISIBLE);
                    llRunning.setVisibility(View.VISIBLE);
                    llIdeal.setVisibility(View.VISIBLE);
                    llInactive.setVisibility(View.VISIBLE);
                    llTowed.setVisibility(View.GONE);
                    llHiSpeed.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
    }

    @Override
    public void onMapLoaded() {
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        if (Map == null)
        {
            Map = googleMap;
            hm1 = new HashMap<>();
            Map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            Map.moveCamera(CameraUpdateFactory
                    .newCameraPosition
                            (new CameraPosition.Builder()
                                    .target(new LatLng(30.709588,76.810326))
                                    .zoom(15.5f)
                                    .build()));
        }
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {
        switch (requestCode) {
            case  Constants.REQ_VEHICLES_ON_MAP:
                if (response.isSuccessful()) {
                    try {
                        if (Map != null) {
                            Map.clear();
                        }
                        progress.setVisibility(View.GONE);
                        isFirstTime = false;
                        StatusChangeFromOption = false;
                        JSONObject jsonObject = new JSONObject(response.body().string());
                        String totalVehicles = jsonObject.getString("iTotalRecords");
                        if (Objects.equals(statusCode, "")) {
                            tvTotalVehicles.setText(totalVehicles);
                        }
                        JSONArray data = jsonObject.getJSONArray("aaData");
                        int parkedCount = 0;
                        int unreachableCount = 0;
                        int MovingCount = 0;
                        int ignitionCount = 0;
                        int towedCount = 0;
                        int highSpeedCount = 0;
                        for (int i = 0; i < data.length(); i++) {
                            JSONObject obj = data.getJSONObject(i);
                            //lat = Double.parseDouble(obj.getString("Latitude"));
                            lat = obj.getDouble("Latitude");
                            // longi = Double.parseDouble(obj.getString("Longitude"));
                            longi = obj.getDouble("Longitude");
                            if (lat != 0)
                                arrLatitude.add(lat);
                            if (longi != 0)
                                arrLongitude.add(longi);
                            location = obj.getString("LocationWithoutLink");
                            String vehicleName = obj.getString("VehicleName");
                            VehicleStatus = obj.getString("ProgressStatus");
                            vehicleType = obj.getString("VIconName");
                            float angle = Float.parseFloat(obj.getString("angle"));
                            if (Objects.equals(statusCode, "")) {
                                if (VehicleStatus.equals("Stopped")) {
                                    Log.e("ParkedCount", "parked");
                                    parkedCount = parkedCount + 1;
                                }
                                if (VehicleStatus.equals("Unreachable")) {
                                    unreachableCount = unreachableCount + 1;
                                }
                                if (VehicleStatus.equals("Moving")) {
                                    MovingCount = MovingCount + 1;
                                }
                                if (VehicleStatus.equals("IgnitionOn")) {
                                    ignitionCount = ignitionCount + 1;
                                }
                                if (VehicleStatus.equals("Towed")) {
                                    towedCount = towedCount + 1;
                                }
                                if (VehicleStatus.equals("Hispeed")) {
                                    highSpeedCount = highSpeedCount + 1;
                                }
                                tvParkedVehicle.setText(String.valueOf(parkedCount));
                                tvMovingCount.setText(String.valueOf(MovingCount));
                                tvIgnitionCount.setText(String.valueOf(ignitionCount));
                                tvUnreachCount.setText(String.valueOf(unreachableCount));
                                tvTowdCount.setText(String.valueOf(towedCount));
                                tvHighSpeedCount.setText(String.valueOf(highSpeedCount));
                            }
                            if (lat != 0 && longi != 0) {
                                marker = Map.addMarker(new MarkerOptions()
                                        .position(new LatLng(lat, longi))
                                        .flat(true).anchor(0.5f, 0.5f)
                                        .rotation(angle));
                                markers.add(marker);

                                if (!vehicleType.equals("Other")) {
                                    if (vehicleType.equalsIgnoreCase("OilTanker"))
                                        switch (VehicleStatus) {
                                            case "Unreachable":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_oil_tanker_red));
                                                break;
                                            case "Stopped":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_oil_tanker_yellow));
                                                break;
                                            case "IgnitionOn":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_oil_tanker_orange));
                                                break;
                                            case "Moving":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_oil_tanker_green));
                                                break;
                                            case "Towed":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_oil_tanker_white));
                                                break;
                                            case "Hispeed":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_oil_tanker_blue));
                                                break;
                                        }
                                    else if (vehicleType.equalsIgnoreCase("Car"))
                                        switch (VehicleStatus) {
                                            case "Unreachable":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_car_red));
                                                break;
                                            case "Stopped":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_car_yellow));
                                                break;
                                            case "IgnitionOn":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_car_orange));
                                                break;
                                            case "Moving":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_car_green_with_shadow));
                                                break;
                                            case "Towed":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_car_white));
                                                break;
                                            case "Hispeed":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_car_blue_with_shadow));
                                                break;
                                        }
                                    else if (vehicleType.equalsIgnoreCase("RoadRoller"))
                                        switch (VehicleStatus) {
                                            case "Unreachable":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_car_red));
                                                break;
                                            case "Stopped":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_car_yellow));
                                                break;
                                            case "IgnitionOn":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_car_orange));
                                                break;
                                            case "Moving":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_car_green_with_shadow));
                                                break;
                                            case "Towed":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_car_white));
                                                break;
                                            case "Hispeed":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_car_blue_with_shadow));
                                                break;
                                        }
                                    else if (vehicleType.equalsIgnoreCase("Bus"))
                                        switch (VehicleStatus) {
                                            case "Unreachable":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_new_bus_red));
                                                break;
                                            case "Stopped":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_new_bus_yellow));
                                                break;
                                            case "IgnitionOn":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_new_bus_orange));
                                                break;
                                            case "Moving":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_new_bus_green));
                                                break;
                                            case "Towed":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_new_bus));
                                                break;
                                            case "Hispeed":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_new_bus_blue));
                                                break;
                                        }
                                    else if (vehicleType.equalsIgnoreCase("Ambulance"))
                                        switch (VehicleStatus) {
                                            case "Unreachable":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_red_truck_final));
                                                break;
                                            case "Stopped":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_yellow_truck_final));
                                                break;
                                            case "IgnitionOn":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_orange_truck_final));
                                                break;
                                            case "Moving":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_green_truck_final));
                                                break;
                                            case "Towed":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_white_truck_final));
                                                break;
                                            case "Hispeed":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_blue_truck_final));
                                                break;
                                        }
                                    else if (vehicleType.equalsIgnoreCase("Truck"))
                                        switch (VehicleStatus) {
                                            case "Unreachable":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_red_truck_final));
                                                break;
                                            case "Stopped":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_yellow_truck_final));
                                                break;
                                            case "IgnitionOn":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_orange_truck_final));
                                                break;
                                            case "Moving":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_green_truck_final));
                                                break;
                                            case "Towed":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_white_truck_final));
                                                break;
                                            case "Hispeed":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_blue_truck_final));
                                                break;
                                        }
                                    else {
                                        switch (VehicleStatus) {
                                            case "Unreachable":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_red_truck_final));
                                                break;
                                            case "Stopped":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_yellow_truck_final));
                                                break;
                                            case "IgnitionOn":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_orange_truck_final));
                                                break;
                                            case "Moving":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_green_truck_final));
                                                break;
                                            case "Towed":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_white_truck_final));
                                                break;
                                            case "Hispeed":
                                                marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_blue_truck_final));
                                                break;
                                        }
                                    }

                                } else {
                                    switch (VehicleStatus) {
                                        case "Unreachable":
                                            marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_red_truck_final));
                                            break;
                                        case "Stopped":
                                            marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_yellow_truck_final));
                                            break;
                                        case "IgnitionOn":
                                            marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_orange_truck_final));
                                            break;
                                        case "Moving":
                                            marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_green_truck_final));
                                            break;
                                        case "Towed":
                                            marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_white_truck_final));
                                            break;
                                        case "Hispeed":
                                            marker.setIcon(bitmapDescriptorFromVector(this, R.drawable.ic_blue_truck_final));
                                            break;
                                    }
                                }

                                Log.e("obj--", obj.toString());
                                hm1.put(marker, obj);
                            }
                        }

                        LatLngBounds.Builder builder = new LatLngBounds.Builder();
                        for (Marker marker : markers) {
                            builder.include(marker.getPosition());
                        }
// Build the bounds object
                        LatLngBounds bounds = builder.build();
// Animate the camera to show all markers within the bounds
                        int padding = 135; // adjust this as desired
                        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
                        Map.animateCamera(cu);

                        Map.setOnMarkerClickListener(marker -> {
                            Log.e("BoxId_GET", "clickeddd ");
                            HashMap<Marker, Object> hm = hm1;
                            JSONObject d = (JSONObject) hm.get(marker);
                            try {
                                //tvSpeed,tvDistance,tvLocation,tvProgressStatus,tvDataDate
                                cvDetails.setVisibility(View.VISIBLE);

                                icClose.setOnClickListener(view -> cvDetails.setVisibility(View.GONE));
                                tvSpeed.setText(d.getString("Speed"));
                                tvDistance.setText(d.getString("Distance"));
                                tvLocation.setText(d.getString("LocationWithoutLink"));
                                tvVehicleName.setText(d.getString(("VehicleName")));
                                switch (d.getString("ProgressStatus")) {
                                    case "Unreachable":
                                        tvProgressStatus.setText("Unreachable");
                                        tvProgressStatus.setTextColor(getResources().getColor(R.color.white));
                                        tvProgressStatus.setBackgroundResource(R.drawable.round_bg_red);
                                        break;
                                    case "Stopped":
                                        tvProgressStatus.setText("Parked");
                                        tvProgressStatus.setTextColor(getResources().getColor(R.color.white));
                                        tvProgressStatus.setBackgroundResource(R.drawable.round_bg_yellow);
                                        break;
                                    case "IgnitionOn":
                                        tvProgressStatus.setText("Ignition On");
                                        tvProgressStatus.setTextColor(getResources().getColor(R.color.white));
                                        tvProgressStatus.setBackgroundResource(R.drawable.round_background);
                                        break;
                                    case "Moving":
                                        tvProgressStatus.setText("Moving");
                                        tvProgressStatus.setTextColor(getResources().getColor(R.color.white));
                                        tvProgressStatus.setBackgroundResource(R.drawable.round_bg_green);
                                        break;
                                    case "Towed":
                                        tvProgressStatus.setText("Towed");
                                        tvProgressStatus.setTextColor(getResources().getColor(R.color.white));
                                        tvProgressStatus.setBackgroundResource(R.drawable.round_bg_creame);
                                        break;
                                    case "Hispeed":
                                        tvProgressStatus.setText("Hi-speed");
                                        tvProgressStatus.setTextColor(getResources().getColor(R.color.white));
                                        tvProgressStatus.setBackgroundResource(R.drawable.round_bg_blue);
                                        break;
                                }
                                String datatime = d.getString("DateTime");
                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                Date objDate = new Date();
                                try {
                                    objDate = sdf.parse(datatime);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }

                                long milliseconds = objDate.getTime();
                                final String latestDate = DateFormatter.getCurrentDateTime(DateFormatter.FORMAT_USER_FRIENDLY_DATE_TIME_1, milliseconds, null, DateFormatter.TimeZoneFormat.NONE);

                                tvDataDate.setText(latestDate);
                                String BoxId = d.getString("BBID");

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            return true;
                        });

                      /*  Map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
                            HashMap<Marker, Object> hm = hm1;

                            public View getInfoWindow(final Marker marker) {
                                // TODO Auto-generated method stub
                                JSONObject d = (JSONObject) hm.get(marker);
                                View v = getLayoutInflater().inflate(R.layout.map_infowindow, null);
                                TextView vehicleName =  v.findViewById(R.id.tvVehicle);
                                View vView =  v.findViewById(R.id.vView);
                                TextView driverName =  v.findViewById(R.id.tvDrivername);
                                TextView vehicleDistance =  v.findViewById(R.id.tvdistance);
                                TextView vehicleSpeed =  v.findViewById(R.id.tvSpeed);
                                TextView vehicleTime =  v.findViewById(R.id.tvDatetime);
                                TextView tvhLocation =  v.findViewById(R.id.tvhLocation);
                                TextView status =  v.findViewById(R.id.tvStatus);

                                if (d != null) {
                                    if (d.length() > 0) {
                                        if (marker != null) {
                                            if (d != null) {
                                                try {
                                                    driverName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_customer, 0, 0, 0);
                                                    vehicleDistance.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_distance_color, 0, 0, 0);
                                                    vehicleSpeed.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_speedometer, 0, 0, 0);
                                                    vehicleTime.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_date_time, 0, 0, 0);
                                                    tvhLocation.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_location, 0, 0, 0);

                                                    String BoxId = d.getString("BoxId");
                                                    vehicleName.setText(d.getString("VehName"));
                                                    vehicleName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_caticon, 0, 0, 0);
                                                    tvhLocation.setText(d.getString("Location"));
                                                    status.setText(d.getString("VehicleStatus"));

                                                    if(d.getString("VehicleStatus").equals("Stopped"))
                                                    {
                                                        status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.parked, 0, 0, 0);
                                                        vView.setBackground(getResources().getDrawable(R.color.yellow));
                                                    }

                                                    else if(d.getString("VehicleStatus").equals("Moving"))
                                                    {
                                                        vView.setBackground(getResources().getDrawable(R.color.green));
                                                        status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.moving, 0, 0, 0);
                                                    }

                                                    else if(d.getString("VehicleStatus").equals("IgnitionOn"))
                                                    {
                                                        vView.setBackground(getResources().getDrawable(R.color.logo));
                                                        status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.parked, 0, 0, 0);
                                                    }

                                                    else if(d.getString("VehicleStatus").equals("Unreachable"))
                                                    {
                                                        vView.setBackground(getResources().getDrawable(R.color.red));
                                                        status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.unreachable, 0, 0, 0);
                                                    }

                                                    else if(d.getString("VehicleStatus").equals("Hispeed"))
                                                    {
                                                        vView.setBackground(getResources().getDrawable(R.color.sky));
                                                        status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.bluedot, 0, 0, 0);
                                                    }

                                                    else if(d.getString("VehicleStatus").equals("Towed"))
                                                    {
                                                        vView.setBackground(getResources().getDrawable(R.color.grey));
                                                        status.setCompoundDrawablesWithIntrinsicBounds(R.drawable.blackdot, 0, 0, 0);
                                                    }


                                                    driverName.setText(d.getString("DriverName"));
                                                    *//*if (PreferenceKey.getInstance().getPreferenceData(GLocationOnMap.this,PreferenceKey.IS_SUB_USER).equals("false"))
                                                    {
                                                        vehicleSpeed.setVisibility(View.GONE);
                                                        vehicleDistance.setVisibility(View.GONE);
                                                    }
                                                    else {
                                                        vehicleSpeed.setText(d.getString("Speed") + " km/h");
                                                        vehicleDistance.setText(d.getString("Distance") + " km");
                                                    }*//*
                                                    vehicleSpeed.setText(d.getString("Speed") + " km/h");
                                                    vehicleDistance.setText(d.getString("Distance") + " km");
                                                    String datatime = d.getString("Lastdate");
                                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                                                    Date objDate = new Date();
                                                    try
                                                    {
                                                        objDate = sdf.parse(datatime);
                                                    }
                                                    catch (ParseException e)
                                                    {
                                                        e.printStackTrace();
                                                    }

                                                    long milliseconds = objDate.getTime();
                                                    final String latestDate = DateFormatter.getCurrentDateTime(DateFormatter.FORMAT_USER_FRIENDLY_DATE_TIME_1, milliseconds, null, DateFormatter.TimeZoneFormat.NONE);

                                                    vehicleTime.setText(latestDate);

                                                    status.setOnClickListener(new View.OnClickListener()
                                                    {
                                                        @Override
                                                        public void onClick(View v)
                                                        {
                                                            // TODO Auto-generated method stub
                                                            marker.hideInfoWindow();
                                                        }
                                                    });

                                                    return v;

                                                } catch (JSONException e) {
                                                    // TODO Auto-generated catch block
                                                    e.printStackTrace();
                                                }
                                            }
                                        }
                                    } else {
                                        return null;
                                    }
                                }
                                return null;
                            }

                            public View getInfoContents(Marker marker) {
                                return null;
                            }

                        });

                        Map.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

                            @Override
                            public void onInfoWindowClick(Marker m) {
                                // TODO Auto-generated method stub
                                m.hideInfoWindow();
                            }
                        });*/
// end-----------------

                        //  Map.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,longi), 14.0f));
                   /* marker =  Map.addMarker(new MarkerOptions().position(new LatLng(lat,longi))
                            .title(location).icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_car)));*/
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
                                            Intent intent = new Intent(GLocationOnMap.this, BillBanner.class);
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
                                        Intent intent = new Intent(GLocationOnMap.this, BillBanner.class);
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

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    @Override
    protected void onPause() {
        super.onPause();
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
    @Override
    public void onBackPressed() {
        // Do nothing to prevent going back
    }

}
