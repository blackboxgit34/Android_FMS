package com.humbhi.blackbox.ui.ui.banner;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.cardview.widget.CardView;

import com.humbhi.blackbox.R;
import com.humbhi.blackbox.ui.data.db.CommonData;
import com.humbhi.blackbox.ui.retofit.Retrofit2;
import com.humbhi.blackbox.ui.retofit.RetrofitResponse;
import com.humbhi.blackbox.ui.ui.billingPayments.BillAccountActivity;
import com.humbhi.blackbox.ui.ui.dashboard.DashboardActivity;
import com.humbhi.blackbox.ui.ui.login.LoginActivity;
import com.humbhi.blackbox.ui.utils.Constants;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.paperdb.Paper;
import okhttp3.ResponseBody;
import retrofit2.Response;

public class BillBanner extends AppCompatActivity implements RetrofitResponse {
    String softBanner,hardBanner,billmessage,phone1,name1,phone2,name2;
    private TextView tvName1,tvNum1,tvName2,tvNum2,tvTextBill,tvNothanks,tvPay,tvLogout;
    private RelativeLayout progress;
    ExecutorService executors = Executors.newSingleThreadExecutor();
    private Handler handlerMain = new Handler(Looper.getMainLooper());
    private CardView cvMain;
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
        cvMain = findViewById(R.id.cvMain);
        progress = findViewById(R.id.progress);
        tvLogout = findViewById(R.id.tvLogout);
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
        switch (requestCode) {
            case Constants.REQ_GET_BANNER:
            executors.execute(() -> {
                try {
                    JSONObject result = new JSONObject(response.body().string());
                    handlerMain.post(() -> {
                        progress.setVisibility(View.GONE);
                        cvMain.setVisibility(View.VISIBLE);
                    });
                    final String status = result.getString("Status");
                    if (hardBanner.equalsIgnoreCase("false")) {
                        handlerMain.post(() -> {
                            tvNothanks.setVisibility(View.VISIBLE);
                            tvNothanks.setOnClickListener(view -> {
                                CommonData.INSTANCE.setSkip("yes");
                                Intent intent = new Intent(BillBanner.this, DashboardActivity.class);
                                startActivity(intent);
                                finish();
                            });
                        });
                    } else {
                        handlerMain.post(() -> {
                            tvNothanks.setVisibility(View.GONE);
                            tvLogout.setVisibility(View.VISIBLE);
                            tvLogout.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    alertDialogWithOkAndCancel(BillBanner.this, "Logout", "Are you sure you want to logout?");
                                }
                            });
                        });
                    }
                    handlerMain.post(() -> {
                        if (status.equalsIgnoreCase("false")) {
                            try {
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
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        } else {
                            try {
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
                            } catch (JSONException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });
                } catch (JSONException | IOException e) {
                    handlerMain.post(() -> {
                        e.printStackTrace();
                    });
                }
            });
            break;
            case Constants.REQ_LOGOUT:
                executors.execute(() -> {
                    if (response != null && response.isSuccessful()) {
                        // Assuming you are using Paper for data storage
                        Paper.book().destroy();

                        // Clear cache method
                        clearAllCache(this);

                        // Run the following code on the main thread
                        handlerMain.post(() -> {
                            Intent intent = new Intent(this, LoginActivity.class);
                            startActivity(intent);
                            finish();
                        });
                    }
                });
                break;
        }
    }

    private void clearAllCache(Context context) {
        File cacheDir = context.getCacheDir();
        deleteDir(cacheDir);
    }

    private boolean deleteDir(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory()) {
            String[] children = fileOrDirectory.list();
            if (children != null) {
                for (String child : children) {
                    boolean success = deleteDir(new File(fileOrDirectory, child));
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        return fileOrDirectory.delete();
    }

    private void alertDialogWithOkAndCancel(Context context, String title, String message) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.layout_alert_ok_cancel);

        AppCompatTextView titleHeading = dialog.findViewById(R.id.tvErrorTitle);
        AppCompatTextView tvMessage = dialog.findViewById(R.id.tvErrorMessage);
        titleHeading.setText(title);
        tvMessage.setText(message);

        TextView cancelBtn = dialog.findViewById(R.id.tvErrorCancel);
        TextView okBtn = dialog.findViewById(R.id.tvErrorOK);

        cancelBtn.setOnClickListener(v -> dialog.dismiss());

        okBtn.setOnClickListener(v -> {
            dialog.dismiss();
            logout();
        });

        dialog.show();
    }

    private void logout(){
        new Retrofit2(BillBanner.this, this, Constants.REQ_LOGOUT, Constants.LOGOUT + "custid=" + CommonData.INSTANCE.getCustIdFromDB()+"&DeviceType=Android").callService(true);
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
