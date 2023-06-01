package com.humbhi.blackbox.ui.ui.notification;

import android.content.Intent;
import android.os.Bundle;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.humbhi.blackbox.R;
import com.humbhi.blackbox.ui.adapters.NotificationListAdapter;
import com.humbhi.blackbox.ui.data.db.CommonData;
import com.humbhi.blackbox.ui.data.models.NotificationModel;
import com.humbhi.blackbox.ui.retofit.Retrofit2;
import com.humbhi.blackbox.ui.retofit.RetrofitResponse;
import com.humbhi.blackbox.ui.ui.dashboard.DashboardActivity;
import com.humbhi.blackbox.ui.utils.Constants;
import com.humbhi.blackbox.ui.utils.DateFormatter;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class GNotifications extends AppCompatActivity implements RetrofitResponse, View.OnClickListener {
    private RecyclerView rvNotifications;
    ImageView markIcon;
    NotificationListAdapter adapter;
    ArrayList<NotificationModel>list = new ArrayList<>();
    private AppCompatTextView tvMarkAll;
    TextView markCounter;
    int counter = 0;
    int number = 0;
    private final ArrayList<String> mArrSelectedId = new ArrayList<>();

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
        tvMarkAll = findViewById(R.id.tvMarkAll);
        ivBack.setVisibility(View.VISIBLE);
        ivBell.setVisibility(View.GONE);
        ivMenu.setVisibility(View.GONE);
        tvTitle.setText("Notifications");
        markCounter = findViewById(R.id.markCount);
        markIcon = findViewById(R.id.ivMarkIcon);
        ivBack.setOnClickListener(view ->
        {
          startActivity(new Intent(this,DashboardActivity.class));
          finish();
        });
        tvMarkAll.setOnClickListener(view ->{
            readAllNotification();
        });
        markIcon.setOnClickListener(view ->{
           selectNotiffication(mArrSelectedId);
        });
        getNotificationHistory();
    }

    private void getNotificationHistory()
    {
        new Retrofit2(this, this, Constants.REQ_GET_NOTIFICATION_LIST
                , Constants.GET_NOTIFICATION_LIST+"custid="+CommonData.INSTANCE.getCustIdFromDB())
                .callServicehitec(true);
    }

    private void getDeleteNotification(String id)
    {
        new Retrofit2(this, this, Constants.REQ_GET_DEL_NOTIFICATION
                ,Constants.GET_DEL_NOTIFICATION+"notificationIds="+id)
                .callServicehitec(true);
    }

    private void readAllNotification()
    {
        new Retrofit2(this, this, Constants.REQ_GET_UPDATE_NOTIFICATION
                ,Constants.GET_UPDATE_NOTIFICATION+"notificationIds=all&custid=" + CommonData.INSTANCE.getCustIdFromDB())
                .callServicehitec(true);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {

        switch (requestCode)
        {
            case Constants.REQ_GET_NOTIFICATION_LIST:
                try
                {
                    list.clear();
                    JSONArray data = new JSONArray(response.body().string());
                    for (int i = 0; i < data.length(); i++)
                    {
                        JSONObject obj = data.getJSONObject(i);
                        if(obj.getString("NotificationRead").equals("UnRead")){
                            counter++;
                        }
                        NotificationModel model = new NotificationModel();
                        model.setTypeId(obj.getString("TypeId"));
                        model.setNotificationMsg(obj.getString("NotificationMsg"));
                        model.setNotificationRead(obj.getString("NotificationRead"));
                        model.setNotificationId(obj.getString("MessageId"));
                        if (!TextUtils.isEmpty(obj.getString("NotificationDate"))
                                && !obj.getString("NotificationDate").equalsIgnoreCase("null"))
                        {
                            Date date = null;
                            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                            try {
                                date = format.parse(obj.getString("NotificationDate"));
                            } catch (java.text.ParseException exception) {
                                // TODO Auto-generated catch block
                                exception.printStackTrace();
                            }
                            long serviceDate = date.getTime();
                            String mNotificationDate = DateFormatter.getCurrentDateTime(DateFormatter.FORMAT_USER_FRIENDLY_DATE_TIME, serviceDate, null, DateFormatter.TimeZoneFormat.NONE);
                            model.setNotificationDate(mNotificationDate);
                        }
                        list.add(model);
                    }
                    if(counter==0){
                        tvMarkAll.setVisibility(View.GONE);
                    }
                    else{
                        tvMarkAll.setVisibility(View.VISIBLE);
                    }
                    adapter = new NotificationListAdapter(this,list);
                    rvNotifications.setLayoutManager(new LinearLayoutManager(this));
                    rvNotifications.setAdapter(adapter);

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
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
                break;
            case Constants.REQ_GET_DEL_NOTIFICATION:
            case Constants.REQ_GET_UPDATE_NOTIFICATION:
                if (response.isSuccessful())
                {
                   Intent intent = new Intent(this,GNotifications.class);
                   startActivity(intent);
                   finish();
                }
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

}
