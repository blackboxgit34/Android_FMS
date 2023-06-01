package com.humbhi.blackbox.ui.ui.addonReports.immobalize;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.humbhi.blackbox.R;
import com.humbhi.blackbox.ui.adapters.ImmobilizeAdapetr;
import com.humbhi.blackbox.ui.data.db.CommonData;
import com.humbhi.blackbox.ui.data.models.ImmobilizeVehicleModel;
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

public class ImmobilizeActivity extends AppCompatActivity implements RetrofitResponse {

    private RecyclerView rvRecycler;
    ImmobilizeAdapetr adapetr;
    SharedPreferences prefs;
    String mUserID = "",status="";
    TextView tvRefresh;
    ArrayList<ImmobilizeVehicleModel>list = new ArrayList<>();
    View toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_immobilize);
        initViews();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void initViews()
    {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        mUserID = CommonData.INSTANCE.getCustIdFromDB();
        rvRecycler = findViewById(R.id.rvRecycler);
        toolbar = findViewById(R.id.toolbar);
        setToolbar();
        getList();
    }

    private void setToolbar(){
        ImageView ivMenu,ivBell,ivBack;
        TextView tvTitle;
        ivMenu = toolbar.findViewById(R.id.ivMenu);
        ivBell = toolbar.findViewById(R.id.ivBell);
        ivBack = toolbar.findViewById(R.id.ivBack);
        tvTitle = toolbar.findViewById(R.id.tvTitle);
        ivMenu.setVisibility(View.GONE);
        ivBell.setVisibility(View.GONE);
        ivBack.setVisibility(View.VISIBLE);
        tvTitle.setText("Immobalization");
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void getList()
    {
        new Retrofit2(this, this, Constants.REQ_GET_IMMOBILIZE_VEHICLE, Constants.GET_IMMOBILIZE_VEHICLE+"custid="+mUserID).callService(true);
    }

    public void setImmobilize(String bbid,String status)
    {
        new Retrofit2(this, this, Constants.REQ_SET_IMMOBILIZE_VEHICLE,
                Constants.SET_IMMOBILIZE_VEHICLE+"bbid="+bbid+"&status="+status).callService(false);
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {

        switch (requestCode)
        {
            case Constants.REQ_GET_IMMOBILIZE_VEHICLE:
                try
                {
                    list.clear();
                    JSONArray data = new JSONArray(response.body().string());
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject obj = data.getJSONObject(i);
                        ImmobilizeVehicleModel model = new ImmobilizeVehicleModel();
                        model.setVehicleName(obj.getString("vehname"));
                        model.setBbid(obj.getString("bbid"));
                        model.setImmobilisze(obj.getString("immobilisze"));
                        model.setImmobilize(obj.getString("immobilize"));
                        model.setIsImmobilizeActive(obj.getString("IsImmobilizeActive"));
                        model.setLastdate(obj.getString("lastdate"));
                        model.setLocation(obj.getString("location"));
                        list.add(model);
                    }

                    rvRecycler.setLayoutManager(new LinearLayoutManager(this));
                    adapetr = new ImmobilizeAdapetr(this,list);
                    rvRecycler.setAdapter(adapetr);

                    adapetr.onItemSelectedListener(new ImmobilizeAdapetr.MyClickListener() {
                        @Override
                        public void onSwitchClick(int poss, View view1) {
                            if(((SwitchCompat)view1).isChecked()){
                                status = "1";
                            }
                            else {
                                status = "0";
                            }
                            alert(list.get(poss).getBbid(),status);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case Constants.REQ_SET_IMMOBILIZE_VEHICLE:
                if (response.isSuccessful())
                {
                    getList();
                }
                break;
        }
    }
    // immobilization alerts
    public void alert(String bbid, String status)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Blackbox");
        builder.setMessage("Dear customer use this feature only in case of car theft. Do you want to proceed?");
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {

                alert2(bbid,status);
            }
        });
        builder.show();
    }

    public void alert2(String bbid, String status)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Blackbox");
        builder.setMessage("Dear customer Immobilization/Demobilization depends upon GSM Network availability. Do you want to proceed?");
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                setImmobilize(bbid,status);
            }
        });
        builder.show();
    }
}
