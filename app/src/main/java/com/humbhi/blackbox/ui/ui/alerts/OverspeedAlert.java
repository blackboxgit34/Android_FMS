package com.humbhi.blackbox.ui.ui.alerts;

import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.humbhi.blackbox.R;
import com.humbhi.blackbox.ui.adapters.OverAlertAdapter;
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

public class OverspeedAlert extends AppCompatActivity implements RetrofitResponse {

    private RecyclerView rvRecycler;
    ArrayList<AlertVehicleModel> list = new ArrayList<>();
    OverAlertAdapter adapter;
    private String alertStatus="",allVehicles="",messageType="";
    private SwitchCompat switchAll;
    private EditText etAllSpeed;
    private Button btAllSetSpeed;
    private AppCompatImageView ivMenu,ivBell,ivBack;
    private AppCompatTextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overspeed_alert);
        initViews();
    }

    private void initViews()
    {
        rvRecycler = findViewById(R.id.rvRecycler);
        btAllSetSpeed = findViewById(R.id.btAllSetSpeed);
        etAllSpeed = findViewById(R.id.etAllSpeed);
        switchAll = findViewById(R.id.switchAll);
        ivMenu = findViewById(R.id.ivMenu);
        ivBell = findViewById(R.id.ivBell);
        ivBack = findViewById(R.id.ivBack);
        tvTitle = findViewById(R.id.tvTitle);
        ivBack.setVisibility(View.VISIBLE);
        ivBell.setVisibility(View.GONE);
        ivMenu.setVisibility(View.GONE);
        tvTitle.setText("Overspeed Alert");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btAllSetSpeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                messageType = "7";
                allVehicles = "true";
                setSpeedAlert("","0",etAllSpeed.getText().toString().trim(),"true");
            }
        });
        switchAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(((SwitchCompat)view).isChecked()){
                    messageType = "3";
                    allVehicles = "true";
                    alertStatus = "true";
                    if (etAllSpeed.getText().toString().equals(""))
                    {
                        setAlert("","0","0");
                    }
                    else {
                        setAlert("","0",etAllSpeed.getText().toString());
                    }

                }
                else
                {
                    messageType = "3";
                    allVehicles = "true";
                    alertStatus = "false";
                    if (etAllSpeed.getText().toString().equals(""))
                    {
                        setAlert("","0","0");
                    }
                    else {
                        setAlert("","0",etAllSpeed.getText().toString());
                    }
                }

            }
        });
        getAllVehicles();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void getAllVehicles()
    {
        new Retrofit2(this, this, Constants.REQ_GET_ALLVEHICLES_ALERTS
                ,Constants.GET_ALLVEHICLES_ALERTS+"custid="+ CommonData.INSTANCE.getCustIdFromDB())
                .callServicehitec(true);
    }

    private void setAlert(String bbid,String vehicleId,String overspeed)
    {
        new Retrofit2(this, this, Constants.REQ_SET_IGNITION_ALERT
                ,Constants.SET_IGNITION_ALERT+"bbid="+bbid+"&VehicleId="+vehicleId+"&messagetype="+messageType+"&AlertStatus="+alertStatus+"&custid="
                +CommonData.INSTANCE.getCustIdFromDB()+"&allvehicle="+allVehicles+"&overspeedlimit="+overspeed+"&overstoplimit=0")
                .callServicehitec(true);
    }

    private void setSpeedAlert(String bbid,String vehicleId,String overspeed,String alertStatusss)
    {
        new Retrofit2(this, this, Constants.REQ_SET_IGNITION_ALERT
                ,Constants.SET_IGNITION_ALERT+"bbid="+bbid+"&VehicleId="+vehicleId+"&messagetype="+messageType+"&AlertStatus="+alertStatusss+"&custid="
                +CommonData.INSTANCE.getCustIdFromDB()+"&allvehicle="+allVehicles+"&overspeedlimit="+overspeed+"&overstoplimit=0")
                .callServicehitec(true);
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {

        switch (requestCode)
        {
            case Constants.REQ_GET_ALLVEHICLES_ALERTS:
                try
                {
                    list.clear();
                    JSONArray data = new JSONArray(response.body().string());
                    for (int i = 0; i <data.length() ; i++)
                    {

                        JSONObject obj = data.getJSONObject(i);
                        AlertVehicleModel model = new AlertVehicleModel();
                        model.setBoxID(obj.getString("BoxID"));
                        model.setVehicleName(obj.getString("VehicleName"));
                        model.setIgnitionStatusAlert(obj.getString("IgnitionStatusAlert"));
                        model.setFMSVehicleId(obj.getString("FMSVehicleId"));
                        model.setBatteryDisconnection(obj.getString("BatteryDisconnection"));
                        model.setOverSpeedAlert(obj.getString("OverSpeedAlert"));
                        model.setOverSpeedLimit(obj.getString("OverSpeedLimit"));
                        model.setMessageType("3");
                        list.add(model);

                    }

                    for (int j = 0; j <list.size() ; j++)
                    {
                        if (list.get(j).getOverSpeedAlert().equals("false"))
                        {
                            switchAll.setChecked(false);
                        }
                        else
                        {
                            switchAll.setChecked(true);
                        }
                    }
                    adapter = new OverAlertAdapter(this,list);
                    rvRecycler.setLayoutManager(new LinearLayoutManager(this));
                    rvRecycler.setAdapter(adapter);

                  adapter.onItemSelectedListener(new OverAlertAdapter.MyClickListener() {
                      @Override
                      public void onSwitchClick(int poss, View view1, EditText et) {
                          if(((SwitchCompat)view1).isChecked()){
                              messageType = "3";
                              alertStatus = "true";
                              allVehicles = "false";
                          }
                          else
                          {
                              messageType = "3";
                              alertStatus = "false";
                              allVehicles = "false";
                          }
                          setAlert(list.get(poss).getBoxID(),list.get(poss).getFMSVehicleId(),et.getText().toString().trim());
                      }

                      @Override
                      public void onSetSpeed(int poss, View view1, EditText et)
                      {
                          messageType = "7";
                          allVehicles = "false";
                          setSpeedAlert(list.get(poss).getBoxID(),list.get(poss).getFMSVehicleId(),et.getText().toString().trim(),list.get(poss).getOverSpeedAlert());
                      }

                  });
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case Constants.REQ_SET_IGNITION_ALERT:
                if (response.isSuccessful())
                {
                    getAllVehicles();
                }
                break;
        }

    }
}
