package com.humbhi.blackbox.ui.ui.banner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.humbhi.blackbox.R;
import com.humbhi.blackbox.ui.data.db.CommonData;
import com.humbhi.blackbox.ui.retofit.Retrofit2;
import com.humbhi.blackbox.ui.retofit.RetrofitResponse;
import com.humbhi.blackbox.ui.ui.billingPayments.BillAccountActivity;
import com.humbhi.blackbox.ui.ui.dashboard.DashboardActivity;
import com.humbhi.blackbox.ui.utils.Constants;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class BillBanner extends AppCompatActivity implements RetrofitResponse {

    String softBanner,hardBanner,billmessage,phone1,name1,phone2,name2;
    private TextView tvName1,tvNum1,tvName2,tvNum2,tvTextBill,tvNothanks,tvPay;
    boolean statusFlag= false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_banner);
        softBanner = getIntent().getStringExtra("SoftBanner");
        hardBanner = getIntent().getStringExtra("hardBanner");
        tvName1 = findViewById(R.id.tvName1);
        tvPay = findViewById(R.id.tvPay);
        tvTextBill = findViewById(R.id.tvTextBill);
        tvNum1 = findViewById(R.id.tvNum1);
        tvName2 = findViewById(R.id.tvName2);
        tvNothanks = findViewById(R.id.tvNothanks);
        tvNum2 = findViewById(R.id.tvNum2);
        showBillBanner();
        tvPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BillBanner.this, BillAccountActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showBillBanner()
    {
        new Retrofit2(this, this, Constants.REQ_GET_BANNER, Constants.GET_BANNER+"custid="+ CommonData.INSTANCE.getCustIdFromDB() +"&type=blackbox").callService(false);
    }

    @Override
    public void onServiceResponse(int requestCode, Response<ResponseBody> response) {
        try

        {
            JSONObject result = new JSONObject(response.body().string());
            final String status = result.getString("Status");

            if (hardBanner.equalsIgnoreCase("false"))
            {
               tvNothanks.setVisibility(View.VISIBLE);
                tvNothanks.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view)
                    {
                        Intent intent = new Intent(BillBanner.this, DashboardActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
            else
            {
                tvNothanks.setVisibility(View.GONE);
            }

            if (status.equalsIgnoreCase("false"))
            {
                billmessage = result.getString("Message");
                phone1 = result.getString("Contact1");
                phone2 = result.getString("Contact2");
                name1 = result.getString("Name1");
                name2 = result.getString("Name2");
                tvTextBill.setText(billmessage);
                tvName1.setText(name1);
                tvName2.setText(name2);
                tvNum1.setText(phone1);
                tvNum2.setText(phone2);
            }
            else
            {
                billmessage = result.getString("Message");
                phone1 = result.getString("Contact1");
                phone2 = result.getString("Contact2");
                name1 = result.getString("Name1");
                name2 = result.getString("Name2");
                tvTextBill.setText(billmessage);
                tvName1.setText(name1);
                tvName2.setText(name2);
                tvNum1.setText(phone1);
                tvNum2.setText(phone2);
                statusFlag = true;
            }


        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onBackPressed() {
//        if (statusFlag)
//        {
//
//        }
//        else
//        {
//            finish();
//        }
    }
}
