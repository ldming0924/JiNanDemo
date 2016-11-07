package com.kawakp.demingliu.jinandemo.receiver;

import android.app.KeyguardManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.kawakp.demingliu.jinandemo.activity.MyLockActivity;
import com.kawakp.demingliu.jinandemo.constant.Config;

/**
 * Created by deming.liu on 2016/8/15.
 */
public class MyReceive extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Config.WARM_RECEIVE)) {
            KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            if (km.inKeyguardRestrictedInputMode()) {
                //Intent alarmIntent = new Intent(context, MyLockActivity.class);
                //alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                // context.startActivity(alarmIntent);
            }
        }
    }
}
