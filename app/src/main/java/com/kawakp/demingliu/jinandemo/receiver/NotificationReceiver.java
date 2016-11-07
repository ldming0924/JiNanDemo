package com.kawakp.demingliu.jinandemo.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kawakp.demingliu.jinandemo.activity.MainActivity;


/**
 * 点击通知，跳转到指定界面
 */
public class NotificationReceiver extends BroadcastReceiver {
    private static String PACKAGE_NAME = "com.kawakp.demingliu.jinandemo";

    @Override
    public void onReceive(Context context, Intent intent) {

        //判断app进程是否存活
        if (SystemUtils.isAppAlive(context, PACKAGE_NAME)) {
            Log.i("NotificationReceiver", "the app process is alive");
            Intent mainIntent = new Intent(context, MainActivity.class);
            mainIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(mainIntent);
        } else {
            Log.i("NotificationReceiver", "the app process is dead");
            Intent launchIntent = context.getPackageManager().
                    getLaunchIntentForPackage(PACKAGE_NAME);
            launchIntent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            context.startActivity(launchIntent);
        }
    }
    // }
}
