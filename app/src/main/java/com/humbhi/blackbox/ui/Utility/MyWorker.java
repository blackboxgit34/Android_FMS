package com.humbhi.blackbox.ui.Utility;

import static android.os.Build.VERSION.SDK_INT;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.humbhi.blackbox.R;
import com.humbhi.blackbox.ui.ui.notification.GNotifications;

import java.util.List;

public class MyWorker extends Worker {
    public MyWorker(
        @NonNull Context context,
        @NonNull WorkerParameters params
    ) {
        super(context, params);
    }
    private static final String MY_APP_PACKAGE = "com.humbhi.blackbox";
    @NonNull
    @Override
    public Result doWork() {
        // Retrieve input data
        Data inputData = getInputData();
        String title = inputData.getString("notification_title");
        String message = inputData.getString("notification_description");
        if (isAppRunningInForeground()) {
            if (!message.equals("")) {
                //  PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_MUTABLE);
                if (SDK_INT >= Build.VERSION_CODES.N) {
                    showNotificationMessage(getApplicationContext(), title, message);
                }
                Log.e("app detected","yes");
            }
        }
        return Result.success(); // Indicate success
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
}
