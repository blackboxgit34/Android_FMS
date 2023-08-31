package com.humbhi.blackbox.ui.Firebase;


import static android.os.Build.VERSION.SDK_INT;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.humbhi.blackbox.BuildConfig;
import com.humbhi.blackbox.R;
import com.humbhi.blackbox.ui.Utility.MyForegroundService;
import com.humbhi.blackbox.ui.Utility.MyWorker;
import com.humbhi.blackbox.ui.Utility.PackageCheckService;
import com.humbhi.blackbox.ui.data.db.CommonData;
import com.humbhi.blackbox.ui.ui.notification.GNotifications;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String title,description;
    Context context;

    ExecutorService executor = Executors.newSingleThreadExecutor();
    Handler mainHandler = new Handler(Looper.getMainLooper());
    private static final String MY_APP_PACKAGE = "com.humbhi.blackbox";
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN",s);
        CommonData.INSTANCE.setFirebaseToken(s);
        CommonData.INSTANCE.setDeviceId(Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID));
    }

    @SuppressLint("WrongConstant")
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size()>0) {
            context = MyFirebaseMessagingService.this;
            title = remoteMessage.getData().get("title");
            description = remoteMessage.getData().get("description");
            if (isAppRunningInForeground()) {
                if (!description.equals("")) {
                    //  PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_MUTABLE);
                    if (SDK_INT >= Build.VERSION_CODES.N) {
                        showNotificationMessage(getApplicationContext(), title, description);
                    }
                    Log.e("app detected","yes");
                }
            }
        }
        else{
            if (remoteMessage.getNotification()!=null){
                context = MyFirebaseMessagingService.this;
                title = remoteMessage.getNotification().getTitle();
                description = remoteMessage.getNotification().getBody();
                if (isAppRunningInForeground()) {
                    if (!description.equals("")) {
                        //  PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_MUTABLE);
                        if (SDK_INT >= Build.VERSION_CODES.N) {
                            showNotificationMessage(getApplicationContext(), title, description);
                        }
                        Log.e("app detected","yes");
                    }
                }
            }
        }
    }

    private boolean isAppRunningInForeground() {
        ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> processes = activityManager.getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo processInfo : processes) {
            if (processInfo.processName.equals(MY_APP_PACKAGE)  && processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND ||
                    processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND || processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_GONE) {
                Log.e("running","yes");
                return true;
            }
        }
        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showNotificationMessage(Context context, String title, String message) {
        Intent intent = new Intent(context, GNotifications.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = null;
        if (SDK_INT >= Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);
        } else {
            pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;

        String channelId = context.getString(R.string.default_notification_channel_id);
        String channelName = context.getString(R.string.default_notification_channel_name);
        int importance = NotificationManager.IMPORTANCE_HIGH;
        if (SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(mChannel);
        }
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.notification_banner);
        String lowercaseMessage = message.toLowerCase();
        if (lowercaseMessage.contains("geofence")) {
            remoteViews.setImageViewResource(R.id.notification_image, R.drawable.satelite);
            remoteViews.setTextViewText(R.id.notification_title, title);
            remoteViews.setTextViewText(R.id.notification_text, message);
        } else if (lowercaseMessage.contains("speed limit")) {
            remoteViews.setImageViewResource(R.id.notification_image, R.drawable.fire_speed_icon);
            remoteViews.setTextViewText(R.id.notification_title, title);
            remoteViews.setTextViewText(R.id.notification_text, message);
        } else if (lowercaseMessage.contains("battery")) {
            remoteViews.setImageViewResource(R.id.notification_image, R.drawable.ic_battery_notification);
            remoteViews.setTextViewText(R.id.notification_title, title);
            remoteViews.setTextViewText(R.id.notification_text, message);
        } else if (lowercaseMessage.contains("stop")) {
            remoteViews.setImageViewResource(R.id.notification_image, R.drawable.ic_alert_overstoppage);
            remoteViews.setTextViewText(R.id.notification_title, title);
            remoteViews.setTextViewText(R.id.notification_text, message);
        } else if (lowercaseMessage.contains("started")) {
            remoteViews.setImageViewResource(R.id.notification_image, R.drawable.ic_alert_ignitionon);
            remoteViews.setTextViewText(R.id.notification_title, title);
            remoteViews.setTextViewText(R.id.notification_text, message);
        }
        else if (lowercaseMessage.contains("fuel rod")) {
            remoteViews.setImageViewResource(R.id.notification_image, R.drawable.ic_fuel_notification);
            remoteViews.setTextViewText(R.id.notification_title, title);
            remoteViews.setTextViewText(R.id.notification_text, message);
        }
        else if (lowercaseMessage.contains("complaint")) {
            remoteViews.setImageViewResource(R.id.notification_image, R.drawable.ic_customer);
            remoteViews.setTextViewText(R.id.notification_title, title);
            remoteViews.setTextViewText(R.id.notification_text, message);
        }
        else{
            remoteViews.setImageViewResource(R.id.notification_image, R.drawable.black_box_small);
            remoteViews.setTextViewText(R.id.notification_title, title);
            remoteViews.setTextViewText(R.id.notification_text, message);
        }
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.black_box)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(remoteViews)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        Notification notification = mBuilder.build();
        notificationManager.notify(notificationId, notification);
    }

    @Override
    public void onMessageSent(String s) {
        // super.onMessageSent(s);
    }
}
