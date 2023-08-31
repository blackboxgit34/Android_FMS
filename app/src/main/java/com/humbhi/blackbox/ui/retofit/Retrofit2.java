package com.humbhi.blackbox.ui.retofit;

/**
 * Created by pro202 on 18/1/18.
 */

import static com.google.android.gms.common.util.CollectionUtils.listOf;

import android.app.Dialog;
import android.content.Context;
import android.net.DnsResolver;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.esotericsoftware.kryo.util.Util;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.humbhi.blackbox.R;
import com.humbhi.blackbox.ui.MyApplication;
import com.humbhi.blackbox.ui.ui.livetracking.LiveCarActivity;
import com.humbhi.blackbox.ui.utils.Constants;
import com.humbhi.blackbox.ui.utils.Network;


import org.chromium.net.NetworkException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
import okhttp3.Protocol;
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
    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private Handler handlerMain = new Handler(Looper.getMainLooper());

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

    public void callService(boolean dialog) {
        if (!Network.isNetworkAvailable(mContext)) {
            //Alert.showDialog(mContext,"Oops!","Internet connection is not available!");
            Toast.makeText(mContext, mContext.getString(R.string.no_network), Toast.LENGTH_SHORT).show();
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
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .callTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true) // Enable retries on connection failures
                .followRedirects(true) // Enable following redirects
                .followSslRedirects(true) // Enable following SSL redirects
                .retryOnConnectionFailure(true) // Enable retries on connection failures
                .protocols(listOf(Protocol.HTTP_1_1))
                .build();

        System.setProperty("http.keepAlive", "false");
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.BASEURL)
                //.client(getUnsafeOkHttpClient().build())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Log.e("URL", "URL- " + Constants.BASEURL + "" + url);
   //     if(MyApplication.Companion.getApiCall()==true) {
            executor.execute(() -> {
                if (postParam == null) {
                    call = retrofitService.callGetService(url);
                } else {
                    Log.e("Params", "Params- " + postParam.toString());
                    call = retrofitService.callPostService(url,
                            RequestBody.create(MediaType.parse("application/json"), postParam.toString()));
                }
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> callback, Response<ResponseBody> response) {
                        handlerMain.post(() -> {
                            try {
                                //      if(response.code()==200) {
                                if (pd.isShowing()) {
                                    pd.dismiss();
                                }
                                //       }
                                if (response.isSuccessful()) {
                                    if (response.code() == 200) {
                                        result.onServiceResponse(requestCode, response);
                                    }
                                } else {
                                    Toast.makeText(mContext, "Sorry, Connection time out", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                if (pd.isShowing()) {
                                    pd.dismiss();
                                }
                                e.printStackTrace();
                                if (e instanceof SocketTimeoutException) {
                                    Constants.alertDialog(mContext, mContext.getString(R.string.time_out));
                                } else if (e instanceof java.net.UnknownHostException) {
                                    Constants.alertDialog(mContext, mContext.getString(R.string.no_network));
                                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    if (e instanceof DnsResolver.DnsException) {
                                        Constants.alertDialog(mContext, mContext.getString(R.string.dns_error));
                                    } else {
                                        Constants.alertDialog(mContext, mContext.getString(R.string.something_went_wrong));
                                    }
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        handlerMain.post(() -> {
                                    try {
                                        if (pd.isShowing()) {
                                            pd.dismiss();
                                        }
                                        if (t instanceof SocketTimeoutException) {
                                            Constants.alertDialog(mContext, mContext.getString(R.string.time_out));
                                        } else if (t instanceof java.net.UnknownHostException) {
                                            Constants.alertDialog(mContext, mContext.getString(R.string.no_network));
                                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                            if (t instanceof DnsResolver.DnsException) {
                                                Constants.alertDialog(mContext, mContext.getString(R.string.dns_error));
                                            } else {
                                                Constants.alertDialog(mContext, mContext.getString(R.string.something_went_wrong));
                                            }
                                        }
                                        call.cancel();
                                        Log.e("FAILURE!!!", t.getLocalizedMessage() + "----message----" + t.getMessage());
                                    } catch (Exception e) {
                                        if (pd.isShowing()) {
                                            pd.dismiss();
                                        }
                                        if (e instanceof SocketTimeoutException) {
                                            Constants.alertDialog(mContext, mContext.getString(R.string.time_out));
                                        } else if (e instanceof java.net.UnknownHostException) {
                                            Constants.alertDialog(mContext, mContext.getString(R.string.no_network));
                                        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                            if (e instanceof DnsResolver.DnsException) {
                                                Constants.alertDialog(mContext, mContext.getString(R.string.dns_error));
                                            } else {
                                                Constants.alertDialog(mContext, mContext.getString(R.string.something_went_wrong));
                                            }
                                        }
                                        call.cancel();
                                        e.printStackTrace();
                                    }
                                }
                        );
                    }
                });
            });
   //     }
    }

    public void callServicehitec(boolean dialog)
    {
        if (!Network.isNetworkAvailable(mContext)) {
            //Alert.showDialog(mContext,"Oops!","Internet connection is not available!");
            Toast.makeText(mContext, mContext.getString(R.string.no_network), Toast.LENGTH_SHORT).show();
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
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .callTimeout(60, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true) // Enable retries on connection failures
                .followRedirects(true) // Enable following redirects
                .followSslRedirects(true) // Enable following SSL redirects
                .retryOnConnectionFailure(true) // Enable retries on connection failures
                .protocols(listOf(Protocol.HTTP_1_1))
                .build();

        System.setProperty("http.keepAlive", "false");
        Gson gson = new GsonBuilder().setLenient().create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.HITECH_BASE)
                //.client(getUnsafeOkHttpClient().build())
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Log.e("URL", "URL- " +Constants.HITECH_BASE+""+ url);
//        if(MyApplication.Companion.getApiCall()==true) {
            executor.execute(() -> {
                if (postParam == null) {
                    call = retrofitService.callGetService(url);
                } else {

                    Log.e("Params", "Params- " + postParam.toString());
                    call = retrofitService.callPostService(url,
                            RequestBody.create(MediaType.parse("application/json"), postParam.toString()));
                }
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> callback, Response<ResponseBody> response) {
                        handlerMain.post(() -> {
                            try {
                                //    if(response.code()==200) {
                                if (pd.isShowing()) {
                                    pd.dismiss();
                                }
                                //    }
                                if (response.isSuccessful()) {
                            //        if (response.code() == 200) {
                                        result.onServiceResponse(requestCode, response);
                             //       }
                                } else {
                                    call.cancel();
                                    Toast.makeText(mContext, mContext.getString(R.string.something_went_wrong), Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                if (pd.isShowing()) {
                                    pd.dismiss();
                                }
                                call.cancel();
                                e.printStackTrace();
                                if (e instanceof SocketTimeoutException) {
                                    Constants.alertDialog(mContext, mContext.getString(R.string.time_out));
                                } else if (e instanceof java.net.UnknownHostException) {
                                    Constants.alertDialog(mContext, mContext.getString(R.string.no_network));
                                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    if (e instanceof DnsResolver.DnsException) {
                                        Constants.alertDialog(mContext, mContext.getString(R.string.dns_error));
                                    } else {
                                        Constants.alertDialog(mContext, mContext.getString(R.string.something_went_wrong));
                                    }
                                }
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        handlerMain.post(() -> {
                            try {
                                if (pd.isShowing()) {
                                    pd.dismiss();
                                }
                                call.cancel();
                                if (t instanceof SocketTimeoutException) {
                                    Constants.alertDialog(mContext, mContext.getString(R.string.time_out));
                                } else if (t instanceof java.net.UnknownHostException) {
                                    Constants.alertDialog(mContext, mContext.getString(R.string.no_network));
                                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    if (t instanceof DnsResolver.DnsException) {
                                        Constants.alertDialog(mContext, mContext.getString(R.string.dns_error));
                                    } else {
                                        Constants.alertDialog(mContext, mContext.getString(R.string.something_went_wrong));
                                    }
                                }
                                Log.e("FAILURE!!!", t.getLocalizedMessage() + "----message----" + t.getMessage());
                            } catch (Exception e) {
                                if (pd.isShowing()) {
                                    pd.dismiss();
                                }
                                if (e instanceof SocketTimeoutException) {
                                    Constants.alertDialog(mContext, mContext.getString(R.string.time_out));
                                } else if (e instanceof java.net.UnknownHostException) {
                                    Constants.alertDialog(mContext, mContext.getString(R.string.no_network));
                                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    if (e instanceof DnsResolver.DnsException) {
                                        Constants.alertDialog(mContext, mContext.getString(R.string.dns_error));
                                    } else {
                                        Constants.alertDialog(mContext, mContext.getString(R.string.something_went_wrong));
                                    }
                                }
                                call.cancel();
                                e.printStackTrace();
                            }
                        });
                    }
                });
            });
   //     }
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
