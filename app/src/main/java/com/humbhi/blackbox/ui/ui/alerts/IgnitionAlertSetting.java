package com.humbhi.blackbox.ui.ui.alerts;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.humbhi.blackbox.R;

import com.humbhi.blackbox.ui.adapters.IgnitionAlertAdapter;
import com.humbhi.blackbox.ui.data.db.CommonData;
import com.humbhi.blackbox.ui.data.models.AlertVehicleModel;
import com.humbhi.blackbox.ui.retofit.Retrofit2;
import com.humbhi.blackbox.ui.retofit.RetrofitResponse;
import com.humbhi.blackbox.ui.utils.Constants;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class IgnitionAlertSetting extends AppCompatActivity implements RetrofitResponse {

    private RecyclerView rvRecycler;
    ArrayList<AlertVehicleModel> list = new ArrayList<>();
    IgnitionAlertAdapter adapter;
    private String alertStatus = "", allVehicles = "";
    private SwitchCompat switchAll;
    private AppCompatImageView ivMenu,ivBell,ivBack;
    private AppCompatTextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ignition_alert_setting);

        initViews();
    }

    private void initViews() {
        rvRecycler = findViewById(R.id.rvRecycler);
        switchAll = findViewById(R.id.switchAll);
        ivMenu = findViewById(R.id.ivMenu);
        ivBell = findViewById(R.id.ivBell);
        ivBack = findViewById(R.id.ivBack);
        tvTitle = findViewById(R.id.tvTitle);
        ivBack.setVisibility(View.VISIBLE);
        ivBell.setVisibility(View.GONE);
        ivMenu.setVisibility(View.GONE);
        tvTitle.setText("Ignition Alert");
        ivBack.setOnClickListener(view -> finish());
        switchAll.setOnClickListener(view -> {
            if (((SwitchCompat) view).isChecked()) {
                allVehicles = "true";
                alertStatus = "true";
                setAlert("", "0");
            } else {
                allVehicles = "true";
                alertStatus = "false";
                setAlert("", "0");
            }
        });
        getAllVehicles();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getAllVehicles() {

        new Retrofit2(this, this, Constants.REQ_GET_ALLVEHICLES_ALERTS
                , Constants.GET_ALLVEHICLES_ALERTS + "custid=" + CommonData.INSTANCE.getCustIdFromDB())
                .callServicehitec(true);
    }

    private void setAlert(String bbid, String vehicleId) {

        new Retrofit2(this, this, Constants.REQ_SET_IGNITION_ALERT
                , Constants.SET_IGNITION_ALERT + "bbid=" + bbid + "&VehicleId=" + vehicleId + "&messagetype=1&AlertStatus=" + alertStatus + "&custid="
                + CommonData.INSTANCE.getCustIdFromDB() + "&allvehicle=" + allVehicles + "&overspeedlimit=0&overstoplimit=0")
                .callServicehitec(true);
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {

        switch (requestCode) {
            case Constants.REQ_GET_ALLVEHICLES_ALERTS:
                try {
                    list.clear();
                    JSONArray data = new JSONArray(response.body().string());
                    for (int i = 0; i < data.length(); i++) {

                        JSONObject obj = data.getJSONObject(i);
                        AlertVehicleModel model = new AlertVehicleModel();
                        model.setBoxID(obj.getString("BoxID"));
                        model.setVehicleName(obj.getString("VehicleName"));
                        model.setIgnitionStatusAlert(obj.getString("IgnitionStatusAlert"));
                        model.setFMSVehicleId(obj.getString("FMSVehicleId"));
                        model.setBatteryDisconnection(obj.getString("BatteryDisconnection"));
                        model.setMessageType("1");
                        list.add(model);
                    }

                    for (int j = 0; j < list.size(); j++) {

                        switchAll.setChecked(!list.get(j).getIgnitionStatusAlert().equals("false"));
                    }
                    adapter = new IgnitionAlertAdapter(this, list);
                    rvRecycler.setLayoutManager(new LinearLayoutManager(this));
                    rvRecycler.setAdapter(adapter);

                    adapter.onItemSelectedListener(new IgnitionAlertAdapter.MyClickListener() {
                        @Override
                        public void onSwitchClick(int poss, View view1) {


                            if (((SwitchCompat) view1).isChecked()) {
                                alertStatus = "true";
                                allVehicles = "false";
                            } else {
                                alertStatus = "false";
                                allVehicles = "false";
                            }
                            setAlert(list.get(poss).getBoxID(), list.get(poss).getFMSVehicleId());

                        }

                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case Constants.REQ_SET_IGNITION_ALERT:
                if (response.isSuccessful()) {
                    getAllVehicles();
                }
                break;
        }

    }

}
