package com.humbhi.blackbox.ui.ui.notification;

import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.humbhi.blackbox.R;
import com.humbhi.blackbox.ui.MyApplication;
import com.humbhi.blackbox.ui.Utility.WhatsNewDialogFragment;
import com.humbhi.blackbox.ui.adapters.NotificationListAdapter;
import com.humbhi.blackbox.ui.data.AisModel;
import com.humbhi.blackbox.ui.data.Table;
import com.humbhi.blackbox.ui.data.db.CommonData;
import com.humbhi.blackbox.ui.data.models.NotificationModel;
import com.humbhi.blackbox.ui.retofit.NetworkService;
import com.humbhi.blackbox.ui.retofit.NewRetrofitClient;
import com.humbhi.blackbox.ui.retofit.Retrofit2;
import com.humbhi.blackbox.ui.retofit.RetrofitResponse;
import com.humbhi.blackbox.ui.ui.banner.BillBanner;
import com.humbhi.blackbox.ui.ui.dashboard.DashboardActivity;
import com.humbhi.blackbox.ui.ui.livetracking.GLocationOnMap;
import com.humbhi.blackbox.ui.ui.livetracking.LiveCarActivity;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class GNotifications extends AppCompatActivity implements RetrofitResponse, View.OnClickListener {
    private RecyclerView rvNotifications;
    ImageView markIcon;
    NotificationListAdapter adapter;
    TextView NoText;
    ArrayList<NotificationModel>list = new ArrayList<>();
    private AppCompatTextView tvMarkAll;
    TextView markCounter;
    int counter = 0;
    int number = 0;
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Handler handlerMain = new Handler(Looper.getMainLooper());
    String intentValue = "";
    int startlimit = 0;
    int limit = 20;
    SwipeRefreshLayout refreshLayout;

    RelativeLayout progress;
    private final ArrayList<String> mArrSelectedId = new ArrayList<>();
    private  TextView loadMore;
    String SoftBanner = "";
    String hardBanner = "";
    int aisCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        initViews();
    }

    private void initViews()
    {
        rvNotifications = findViewById(R.id.rvNotifications);
        AppCompatImageView ivMenu = findViewById(R.id.ivMenu);
        AppCompatImageView ivBell = findViewById(R.id.ivBell);
        AppCompatImageView ivBack = findViewById(R.id.ivBack);
        AppCompatTextView tvTitle = findViewById(R.id.tvTitle);
        loadMore = findViewById(R.id.loadMore);
        NoText = findViewById(R.id.noItemFound);
        tvMarkAll = findViewById(R.id.tvMarkAll);
        ivBack.setVisibility(View.VISIBLE);
        ivBell.setVisibility(View.GONE);
        ivMenu.setVisibility(View.GONE);
        tvTitle.setText("Notifications");
        markCounter = findViewById(R.id.markCount);
        markIcon = findViewById(R.id.ivMarkIcon);
        refreshLayout = findViewById(R.id.refresh_layout);
        progress = findViewById(R.id.progress);
        if(MyApplication.cantSkip.equals("yes")){
            getAisData();
        }

        ivBack.setOnClickListener(view ->
        {
            if(getIntent().hasExtra("backToLive")) {
                Intent intent = new Intent(this, LiveCarActivity.class);
                intent.putExtra("vehicleName",getIntent().getStringExtra("backToLive").toString());
                startActivity(intent);
                finish();
            }
            else {
                Intent intent = new Intent(this, DashboardActivity.class);
                startActivity(intent);
                finish();
                if (Network.isNetworkAvailable(GNotifications.this)) {
                    new Retrofit2(this, this,
                            Constants.REQ_EXPIRE_ACCOUNT_DETAILS,
                            Constants.EXPIRE_ACCOUNT_DETAILS
                                    + "custid=" + CommonData.INSTANCE.getCustIdFromDB()).callService(false);
                }
            }
        });
        tvMarkAll.setOnClickListener(view ->{
            readAllNotification();
        });
        markIcon.setOnClickListener(view ->{
           selectNotiffication(mArrSelectedId);
        });
        if(getIntent().hasExtra("from")) {
            intentValue = getIntent().getStringExtra("from").toString();
        }
        getNotificationHistory();
        refreshLayout.setOnRefreshListener(()->{
            refreshLayout.setRefreshing(true);
            getNotificationHistory();
        });
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
                                Constants.Toastmsg(GNotifications.this, getString(finalErrorMessage));
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
                handlerMain.post(new Runnable() {
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
                            WhatsNewDialogFragment dialogFragment = new WhatsNewDialogFragment(GNotifications.this, expiry, Objects.requireNonNull(vehicleWithLeastValidity.getCommercialvalidity()), Objects.requireNonNull(vehicleWithLeastValidity.getVehname()));
                            dialogFragment.show(getSupportFragmentManager(), "WhatsNewDialog");
                        }
                    }
                });
            }
        }
    }


    private void getNotificationHistory()
    {
        progress.setVisibility(View.VISIBLE);
        new Retrofit2(this, this, Constants.REQ_GET_NOTIFICATION_LIST
                , Constants.GET_NOTIFICATION_LIST+"custid="+CommonData.INSTANCE.getCustIdFromDB()+"&Search="+intentValue
                + "&sEcho=1"+"&iDisplayStart="+startlimit+"&iDisplayLength="+limit+"&iSortCol_0=0&sSortDir_0=asc")
                .callService(false);
    }

    private void getDeleteNotification(String id)
    {
        progress.setVisibility(View.VISIBLE);
        new Retrofit2(this, this, Constants.REQ_GET_DEL_NOTIFICATION
                ,Constants.GET_DEL_NOTIFICATION+"notificationIds="+id)
                .callServicehitec(false);
    }

    private void readAllNotification()
    {
        progress.setVisibility(View.VISIBLE);
        new Retrofit2(this, this, Constants.REQ_GET_UPDATE_NOTIFICATION
                ,Constants.GET_UPDATE_NOTIFICATION+"notificationIds=all&custid=" + CommonData.INSTANCE.getCustIdFromDB())
                .callServicehitec(false);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {

        switch (requestCode)
        {
            case Constants.REQ_GET_NOTIFICATION_LIST:
                executor.execute(()->{
                try
                {
                    handlerMain.post(new Runnable() {
                        @Override
                        public void run() {
                            progress.setVisibility(View.GONE);
                        }
                    });
                    handlerMain.post(()-> {
                        refreshLayout.setRefreshing(false);
                    });
                    JSONObject data = new JSONObject(response.body().string());
                    counter = data.getInt("iTotalRecords");
                    JSONArray array = data.getJSONArray("aaData");
                    for (int i = 0; i < array.length()-1; i++)
                    {
                        JSONObject obj = array.getJSONObject(i);
                        NotificationModel model = new NotificationModel();

                        model.setTypeId(obj.getString("TypeId"));
                        model.setNotificationMsg(obj.getString("NotificationMsg"));
                        model.setNotificationRead(obj.getString("NotificationRead"));
                        model.setNotificationId(obj.getString("MessageId"));
                        if (!TextUtils.isEmpty(obj.getString("NotificationDate")) && !obj.getString("NotificationDate").equalsIgnoreCase("null")) {
                            Date date = null;
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            try {
                                date = format.parse(obj.getString("NotificationDate"));
                            } catch (java.text.ParseException exception) {
                                // TODO Auto-generated catch block
                                exception.printStackTrace();
                            }
                            assert date != null;
                            long serviceDate = date.getTime();
                            String mNotificationDate = DateFormatter.getCurrentDateTime(DateFormatter.FORMAT_USER_FRIENDLY_DATE_TIME, serviceDate, null, DateFormatter.TimeZoneFormat.NONE);
                            model.setNotificationDate(mNotificationDate);
                        }
                                list.add(model);
                        }
                    handlerMain.post(()->{
                    if(counter==0){
                        tvMarkAll.setVisibility(View.GONE);
                    }
                    else{
                        tvMarkAll.setVisibility(View.VISIBLE);
                    }
                    adapter = new NotificationListAdapter(this,list);
                    rvNotifications.setLayoutManager(new LinearLayoutManager(this));
                    rvNotifications.setAdapter(adapter);
                    if(array.length()<20){
                        loadMore.setVisibility(View.GONE);
                    }
                    else{
                        loadMore.setVisibility(View.VISIBLE);
                    }
                    rvNotifications.scrollToPosition(startlimit);
                    loadMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            startlimit += 20;
                            getNotificationHistory();
                        }
                    });
                    adapter.onItemSelectedListener(new NotificationListAdapter.MyClickListener() {
                        @Override
                        public void onSwitchClick(int poss, View view1) {
                            getDeleteNotification(list.get(poss).getNotificationId());
                        }

                        @Override
                        public void onShare(int poss, View view2) {
                            shareNotification(list.get(poss).getNotificationMsg());
                        }

                        @Override
                        public void onSelect(int poss, View view3) {
                            if (!mArrSelectedId.contains(list.get(poss).getNotificationId())) {
                                mArrSelectedId.add(list.get(poss).getNotificationId());
                                markIcon.setVisibility(View.VISIBLE);
                                markCounter.setVisibility(View.VISIBLE);
                                number++;
                                markCounter.setText(String.valueOf(number));
                            }
                            else{
                                if(number>=1) {
                                    mArrSelectedId.remove(list.get(poss).getNotificationId());
                                    number--;
                                    markCounter.setText(String.valueOf(number));
                                    if(number==0){
                                        markIcon.setVisibility(View.GONE);
                                        markCounter.setVisibility(View.GONE);
                                    }
                                }
                                else{
                                    markIcon.setVisibility(View.GONE);
                                    markCounter.setVisibility(View.GONE);
                                }
                            }
                        }
                    });
                    if(list.size()==0){
                        loadMore.setVisibility(View.GONE);
                        NoText.setVisibility(View.VISIBLE);
                    }
                    });
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
                });
                break;
            case Constants.REQ_GET_DEL_NOTIFICATION:
            case Constants.REQ_GET_UPDATE_NOTIFICATION:
                if (response.isSuccessful())
                {
                    handlerMain.post(new Runnable() {
                        @Override
                        public void run() {
                            progress.setVisibility(View.GONE);
                        }
                    });
                   Intent intent = new Intent(this,GNotifications.class);
                   startActivity(intent);
                   finish();
                }
                break;
            case Constants.REQ_EXPIRE_ACCOUNT_DETAILS:
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            JSONObject result = new JSONObject(response.body().string());
                            JSONArray table = result.getJSONArray("Table");
                            handlerMain.post(new Runnable() {
                                @Override
                                public void run() {
                                    progress.setVisibility(View.GONE);
                                }
                            });
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
                                            Intent intent = new Intent(GNotifications.this, BillBanner.class);
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
                                        Intent intent = new Intent(GNotifications.this, BillBanner.class);
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

    private void selectNotiffication(ArrayList<String> idsArr){
       StringBuilder stringBuilder = new StringBuilder();
       String notificationId;
        for (int i = 0; i < idsArr.size(); i++) {
            stringBuilder.append(mArrSelectedId.get(i));
            stringBuilder.append(",");
        }
        if (!stringBuilder.toString().isEmpty()) {
            notificationId = stringBuilder.toString().substring(0, stringBuilder.toString().length() - 1);
        } else {
            notificationId = "";
        }
        new Retrofit2(this, this, Constants.REQ_GET_UPDATE_NOTIFICATION
                ,Constants.GET_UPDATE_NOTIFICATION+"notificationIds="+notificationId)
                .callServicehitec(true);
    }

    private void shareNotification(String text){
        String msg = text + "\n\n" + "From: Blackbox VTS";
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Blackbox VTS");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, msg);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));
    }

    @Override
    public void onBackPressed() {
        // Do nothing to prevent going back
    }

}
