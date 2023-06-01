package com.humbhi.blackbox.ui.Firebase;


import static android.os.Build.VERSION.SDK_INT;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.humbhi.blackbox.R;
import com.humbhi.blackbox.ui.data.db.CommonData;
import com.humbhi.blackbox.ui.ui.notification.GNotifications;

import java.util.Map;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    String title,description;
    Intent intent;

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

        if (remoteMessage.getData() != null)
        {
            Map<String, String> data = remoteMessage.getData();
            Log.e("DATA-->", data.toString());
            title = data.get("title");
            description = data.get("description");
            Log.e("TITLE-->",title+"desc-->"+description);
            intent  = new Intent(this, GNotifications.class);

            if(!description.equals("")){
              //  PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_MUTABLE);
                showNotificationMessage(this,title,description,intent);
            }
        }

        //Setting up Notification channels for android O and above
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.e("TAG-->","oreo");
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_MUTABLE);
            showNotificationMessage(this,title,description,intent);
//          showSmallNotification(description, title, pendingIntent);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showNotificationMessage(Context context, String title, String message, Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        int notificationId = 1;
        String channelId = getString(R.string.default_notification_channel_id);
        String channelName = getString(R.string.default_notification_channel_name);
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    channelId, channelName, importance);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(mChannel);
        }
        PendingIntent pendingIntent = null;
        if(SDK_INT>=Build.VERSION_CODES.S) {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_MUTABLE);
        }
        else {
            pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.drawable.black_box)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setContentIntent(pendingIntent);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        stackBuilder.addNextIntent(intent);
        PendingIntent resultPendingIntent = null;
        if(SDK_INT >= Build.VERSION_CODES.S){
            resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT|PendingIntent.FLAG_MUTABLE);
        }
        else{
            resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
        }
        mBuilder.setContentIntent(resultPendingIntent);
        notificationManager.notify((int) System.currentTimeMillis(), mBuilder.build());
    }

    @Override
    public void onMessageSent(String s) {
        // super.onMessageSent(s);
    }
}
