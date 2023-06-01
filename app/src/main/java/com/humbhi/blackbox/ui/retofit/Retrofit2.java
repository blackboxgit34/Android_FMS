package com.humbhi.blackbox.ui.retofit;

/**
 * Created by pro202 on 18/1/18.
 */

import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.humbhi.blackbox.R;
import com.humbhi.blackbox.ui.ui.livetracking.LiveCarActivity;
import com.humbhi.blackbox.ui.utils.Constants;
import com.humbhi.blackbox.ui.utils.Network;


import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Retrofit2
{
    private Call<ResponseBody> call;
    private RetrofitResponse result;
    private JSONObject postParam;
    public static Context mContext;
    private int requestCode;
    private String url;
    private String value;
    private static Dialog pd;
    private MultipartBody.Part part,thumb;
    private HashMap<String, RequestBody> map;
    private HashMap<String,String> stringHashMap;
    private ArrayList<MultipartBody.Part> partList;
    private String number="";
    private static Toast toast;


    public Retrofit2(Context mContext, RetrofitResponse result, int requestCode, String url)
    {
        this.mContext = mContext;
        this.result = result;
        this.requestCode = requestCode;
        this.url = url;
        pd = new Dialog(mContext);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.setContentView(R.layout.progress_bar);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


    }
    public Retrofit2(Context context, RetrofitResponse result, HashMap<String,RequestBody> map, ArrayList<MultipartBody.Part> partList, int requestCode, String url, String number) {

        this.mContext = context;
        this.result = result;
        this.map = map;
        this.requestCode = requestCode;
        this.url = url;
        this.number = number;
        this.partList = partList;
        pd = new Dialog(mContext);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
      //  pd.setContentView(R.layout.progress_bar);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

    }
    public Retrofit2(Context mContext, RetrofitResponse result, int requestCode, String url, JSONObject postParam)//for URL
    {
        this.mContext = mContext;
        this.result = result;
        this.requestCode = requestCode;
        this.url = url;
        this.postParam = postParam;
        pd = new Dialog(mContext);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
       // pd.setContentView(R.layout.progress_bar);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    //For single  image file send
    public Retrofit2(Context mContext, RetrofitResponse result , int requestCode, String url, HashMap<String, RequestBody> map, MultipartBody.Part part)//for POST URL
    {
        this.mContext = mContext;
        this.result = result;
        this.map = map;
        this.part = part;
        this.requestCode = requestCode;
        this.url = url;

        pd = new Dialog(mContext);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        //pd.setContentView(R.layout.progress_bar);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
    //For single  image file send
    public Retrofit2(Context mContext, RetrofitResponse result , int requestCode, String url, HashMap<String, RequestBody> map, MultipartBody.Part part, MultipartBody.Part thumb, String number)//for POST URL
    {
        this.mContext = mContext;
        this.result = result;
        this.map = map;
        this.part = part;
        this.requestCode = requestCode;
        this.url = url;
        this.thumb=thumb;
        this.number=number;

        pd = new Dialog(mContext);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
       // pd.setContentView(R.layout.progress_bar);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }
    //For send data in array form

    public Retrofit2(Context mContext, RetrofitResponse result , int requestCode, String url, HashMap<String, String> stringHashMap, String number)//for POST URL
    {
        this.mContext = mContext;
        this.result = result;
        this.requestCode = requestCode;
        this.url = url;
        this.number=number;
        this.stringHashMap=stringHashMap;

        pd = new Dialog(mContext);
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
       // pd.setContentView(R.layout.progress_bar);
        pd.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
    }

    public void callService(boolean dialog)
    {
        if (!Network.isNetworkAvailable(mContext))
        {
            //Alert.showDialog(mContext,"Oops!","Internet connection is not available!");
            Toast.makeText(mContext, "Internet connection is not available!", Toast.LENGTH_SHORT).show();
           /* else
            {
               // toast.cancel();
               // toast.show();
            }*/
            return;
        }

        if (dialog) {
            pd.show();
        }
        OkHttpClient okHttpClient = new OkHttpClient.Builder()//Use For Time Out
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .callTimeout(30, TimeUnit.SECONDS)
                .build();

        System.setProperty("http.keepAlive", "false");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASEURL)
                //.client(getUnsafeOkHttpClient().build())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Log.e("URL", "URL- " +Constants.BASEURL+""+ url);
        if (postParam == null)
        {
            call = retrofitService.callGetService(url);
        }

        else
        {

            Log.e("Params", "Params- " + postParam.toString());
            call = retrofitService.callPostService(url,
                    RequestBody.create(MediaType.parse("application/json"), postParam.toString()));
        }
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> callback, Response<ResponseBody> response) {

                try {
                    if (pd.isShowing()) {
                        pd.dismiss();

                    }
                    if (response.isSuccessful()) {
                        result.onServiceResponse(requestCode, response);
                    }
                    else {
                        Toast.makeText(mContext, "Sorry, Connection time out", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    if(e instanceof SocketTimeoutException){
                        Toast.makeText(mContext, "Connection time out.",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
                    call.cancel();
                    if (t instanceof SocketTimeoutException) {
                        Toast.makeText(mContext, "Connection time out.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(mContext, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }
                    Log.e("FAILURE!!!",t.getLocalizedMessage()+"----message----"+t.getMessage());
                    Toast.makeText(mContext, "Sorry, Connection time out", Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }

    public void callServicehitec(boolean dialog)
    {
        if (!Network.isNetworkAvailable(mContext))
        {
            //Alert.showDialog(mContext,"Oops!","Internet connection is not available!");
            Toast.makeText(mContext, "Internet connection is not available!", Toast.LENGTH_SHORT).show();
           /* else
            {
               // toast.cancel();
               // toast.show();
            }*/
            return;
        }

        if (dialog) {
            pd.show();
        }
        OkHttpClient okHttpClient = new OkHttpClient.Builder()//Use For Time Out
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .build();

        System.setProperty("http.keepAlive", "false");
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.HITECH_BASE)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Log.e("URL", "URL- " +Constants.HITECH_BASE+""+ url);
        if (postParam == null)
        {
            call = retrofitService.callGetService(url);
        }

        else
        {

            Log.e("Params", "Params- " + postParam.toString());
            call = retrofitService.callPostService(url,
                    RequestBody.create(MediaType.parse("application/json"), postParam.toString()));
        }
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> callback, Response<ResponseBody> response) {

                try {
                    if (pd.isShowing()) {
                        pd.dismiss();

                    }
                    if (response.isSuccessful()) {
                        result.onServiceResponse(requestCode, response);
                    }
                    else {
                        Toast.makeText(mContext, "Sorry, Connection time out", Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    if(e instanceof SocketTimeoutException){
                        Toast.makeText(mContext, "Connection time out.",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                try {
                    if (pd.isShowing()) {
                        pd.dismiss();
                    }
                    call.cancel();
                    if (t instanceof SocketTimeoutException) {
                        Toast.makeText(mContext, "Connection time out.", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(mContext, "Something went wrong.", Toast.LENGTH_SHORT).show();
                    }
                    Log.e("FAILURE!!!",t.getLocalizedMessage()+"----message----"+t.getMessage());
                    Toast.makeText(mContext, "Sorry, Connection time out", Toast.LENGTH_SHORT).show();

                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
    }




    public static OkHttpClient.Builder getUnsafeOkHttpClient() {

        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.readTimeout(3, TimeUnit.MINUTES);
            builder.connectTimeout(2, TimeUnit.MINUTES);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
