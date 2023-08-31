package com.humbhi.blackbox.ui.Utility;

import android.app.Notification;
import android.content.Intent;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;

import com.humbhi.blackbox.BuildConfig;

public class MyNotificationListenerService extends NotificationListenerService {

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        // Check if the notification package matches your app's package
        if (sbn.getPackageName().equals(BuildConfig.APPLICATION_ID)) {
            // Perform your action here when your app's notification is posted
        }
    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        // Notification removed
    }
}
