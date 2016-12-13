package com.kawakp.demingliu.jinandemo.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by deming.liu on 2016/6/11.
 */

public class SystemVerdonCode {
    //获取当前版本号
    public static int getAppVersionCode(Context context) {
        int versionCode = 0;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionCode = packageInfo.versionCode;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionCode;
    }


    public static String getAppVersionName(Context context) {
        String versionName = null;
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            versionName = packageInfo.versionName;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }
}
