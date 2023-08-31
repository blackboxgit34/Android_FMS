package com.humbhi.blackbox.ui.Utility;

import static android.os.Build.VERSION.SDK_INT;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.RemoteViews;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.humbhi.blackbox.R;
import com.humbhi.blackbox.ui.Firebase.MyFirebaseMessagingService;
import com.humbhi.blackbox.ui.ui.notification.GNotifications;

import java.util.List;

public class MyForegroundService extends Service {
    String title;
    String description;

    private static final String MY_APP_PACKAGE = "com.humbhi.blackbox";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
             title = intent.getStringExtra("notification_title");
             description = intent.getStringExtra("notification_description");
        }
        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
