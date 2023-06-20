package com.humbhi.blackbox.ui.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.humbhi.blackbox.ui.MyApplication;
import com.humbhi.blackbox.ui.data.models.LiveMovementModel;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONObject;

import java.net.SocketTimeoutException;
import java.util.ArrayList;

import info.mqtt.android.service.Ack;
import info.mqtt.android.service.MqttAndroidClient;

public class MqttHelper {
    private static final String TAG = MqttHelper.class.getSimpleName();

    private static MqttHelper instance;
    private MqttAndroidClient mqttClient;
    private Context context;
    private String brokerAddress;
    private int brokerPort;
    private String clientId;
    private MqttMessageCallback mqttMessageCallback;

    public interface MqttMessageCallback {
        void onMessageReceived(String topic, MqttMessage message);
        void onDisconnect(Throwable cause);
    }

    private MqttHelper(Context context, String brokerAddress, int brokerPort, String clientId) {
        this.context = context.getApplicationContext();
        this.brokerAddress = brokerAddress;
        this.brokerPort = brokerPort;
        this.clientId = clientId;
    }

    public static synchronized MqttHelper getInstance(Context context, String brokerAddress, int brokerPort, String clientId) {
        if (instance == null) {
            instance = new MqttHelper(context, brokerAddress, brokerPort, clientId);
        }
        return instance;
    }

    public void setMqttMessageCallback(MqttMessageCallback callback) {
        this.mqttMessageCallback = callback;
    }

    public void connect(IMqttActionListener callback) {
        if (mqttClient == null || !mqttClient.isConnected()) {
            String serverUri = brokerAddress + ":" + brokerPort;
            mqttClient = new MqttAndroidClient(context, serverUri, clientId, Ack.AUTO_ACK);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setCleanSession(true);
            options.setKeepAliveInterval(300);
            options.setUserName("bbox");
            options.setPassword("admin@123".toCharArray());
            try {
                mqttClient.connect(options, null, callback);
                mqttClient.setCallback(new MqttCallbackExtended() {
                    @Override
                    public void connectComplete(boolean reconnect, String serverURI) {
                        Log.e("serverURI",serverURI);
                        Log.e("Message","Client Connected");
                    }

                    @Override
                    public void connectionLost(Throwable cause) {
                        try {
                            if (mqttMessageCallback != null) {
                                mqttMessageCallback.onDisconnect(cause);
                            }
                        }
                        catch (Exception e) {
                            if(e instanceof SocketTimeoutException){
                                Toast.makeText(context, "Connection time out", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void messageArrived(String topic, MqttMessage message) throws Exception {
                        // Message received
                        try {
                            if (mqttMessageCallback != null) {
                                mqttMessageCallback.onMessageReceived(topic, message);
                            }
                        }
                        catch (Exception e) {
                            if(e instanceof SocketTimeoutException){
                                Toast.makeText(context, "Connection time out", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken token) {
                        Log.e("Message","Delivery completed");
                    }
                });
//                mqttClient.setCallback(new MqttCallbackExtended() {
//                    @Override
//                    public void connectComplete(boolean reconnect, String serverURI) {
//                        Log.e("serverURI",serverURI);
//                        Log.e("Message","Client Connected");
//                    }
//
//                    @Override
//                    public void connectionLost(Throwable cause) {
//                        Log.e("Message","Connection lost");
//                    }
//
//                    @Override
//                    public void messageArrived(String topic, MqttMessage message) throws Exception {
//                        // Message received
//                     //   if(topic.equals("GET_LIVE:60/" + "J860181063593567")) {
//                            String payload = new String(message.getPayload());
//                            JSONObject object = new JSONObject(payload);
//                            MyApplication.Companion.getList().add(new LiveMovementModel(
//                                    object.getString("angle"),
//                                    object.getString("bbid"),
//                                    object.getString("datadate"),
//                                    object.getString("distance"),
//                                    object.getString("lati"),
//                                    object.getString("longi"),
//                                    object.getString("speed"),
//                                    object.getString("vid"))
//                            );
//                            Log.e("Message", payload);
//                   //     }
//                    }
//
//                    @Override
//                    public void deliveryComplete(IMqttDeliveryToken token) {
//                        Log.e("Message","Delivery completed");
//                    }
//                });
            } catch (Exception e) {
                e.printStackTrace();
                if(e instanceof SocketTimeoutException){
                    Toast.makeText(context, "Connection time out", Toast.LENGTH_SHORT).show();
                }
                if (callback != null) {
                    callback.onFailure(null, e);
                }
            }
        } else {
            if (callback != null) {
                callback.onSuccess(null);
            }
        }
    }

    public void disconnect() {
        try {
            if (mqttClient != null && mqttClient.isConnected()) {
                mqttClient.disconnect();
            }
        } catch (Exception e) {
            if(e instanceof SocketTimeoutException){
                Toast.makeText(context, "Connection time out", Toast.LENGTH_SHORT).show();
            }
            e.printStackTrace();
        }
    }

    public void subscribe(String topic, int qos, IMqttActionListener callback) {
        if (mqttClient != null && mqttClient.isConnected()) {
            try {
                mqttClient.subscribe(topic, qos, null, callback);
            } catch (Exception e) {
                e.printStackTrace();
                if(e instanceof SocketTimeoutException){
                    Toast.makeText(context, "Connection time out", Toast.LENGTH_SHORT).show();
                }
                if (callback != null) {
                    callback.onFailure(null, e);
                }
            }
        } else {
            Log.e(TAG, "MQTT client is not connected.");
        }
    }

    public void unsubscribe(String topic, IMqttActionListener callback) {
        if (mqttClient != null && mqttClient.isConnected()) {
            try {
                mqttClient.unsubscribe(topic, null, callback);

            } catch (Exception e) {
                e.printStackTrace();
                if(e instanceof SocketTimeoutException){
                    Toast.makeText(context, "Connection time out", Toast.LENGTH_SHORT).show();
                }
                if (callback != null) {
                    callback.onFailure(null, e);
                }
            }
        } else {
            Log.e(TAG, "MQTT client is not connected.");
        }
    }

    public void publish(String topic, String payload, int qos, boolean retained) {
        if (mqttClient != null && mqttClient.isConnected()) {
            try {
                MqttMessage message = new MqttMessage(payload.getBytes());
                message.setQos(qos);
                message.setRetained(retained);
                mqttClient.publish(topic, message);
            } catch (Exception e) {
                e.printStackTrace();
                if(e instanceof SocketTimeoutException){
                    Toast.makeText(context, "Connection time out", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            Log.e(TAG, "MQTT client is not connected.");
        }
    }
}

