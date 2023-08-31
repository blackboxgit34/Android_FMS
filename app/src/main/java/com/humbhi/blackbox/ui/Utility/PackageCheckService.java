package com.humbhi.blackbox.ui.Utility;
import android.app.ActivityManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import java.util.List;

public class PackageCheckService extends Service {

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        checkForegroundAppPackage();
        return START_STICKY;
    }

    private void checkForegroundAppPackage() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();

        if (runningAppProcesses != null && !runningAppProcesses.isEmpty()) {
            String appPackageName = "com.humbi.blackbox";
            String foregroundPackageName = runningAppProcesses.get(0).processName;

            if (foregroundPackageName.equals(appPackageName)) {


            } else {

            }
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
