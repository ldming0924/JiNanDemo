package com.kawakp.demingliu.jinandemo.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.kawakp.demingliu.jinandemo.fragment.ControlSetFragment;

/**
 * Created by deming.liu on 2016/6/7.
 */
public class SharedPerferenceHelper {
    //保持cookie到本地
    public static boolean saveCookie(Context context,String sessionID){
        SharedPreferences sharedPreferences = context.getSharedPreferences("COOKIE",context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("COOKIE",sessionID);
        editor.commit();
        return true;

    }
    //读取COOKIE
    public static String getCookie(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("COOKIE",context.MODE_PRIVATE);
        String sessionID = sharedPreferences.getString("COOKIE",null);
        return sessionID;
    }
    //保持用户ID，与type
    //保持cookie到本地
    public static boolean saveUserMessage(Context context,String username,String password){
        SharedPreferences sharedPreferences = context.getSharedPreferences("USERMESSAGE",context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("username",username);
        editor.putString("password",password);
        editor.commit();
        return true;
    }

    //读取COOKIE
    public static SharedPreferences getUserMessage(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("USERMESSAGE",context.MODE_PRIVATE);

        return sharedPreferences;
    }

    //保存deviceID
    public static boolean saveDeviceId(Context context,String deviceId){
        SharedPreferences sharedPreferences = context.getSharedPreferences("DEVICEID",context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("deviceid",deviceId);
        editor.commit();
        return true;
    }



    //保存更新报警记录
    public static boolean saveUpdateNum(Context context,int num){
        SharedPreferences sharedPreferences = context.getSharedPreferences("UPDATENUM",context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("NUM",num);
        editor.commit();
        return true;
    }
    //读取更新报警记录
    public static int getUpdateNum(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("UPDATENUM",context.MODE_PRIVATE);
        return sharedPreferences.getInt("NUM",0);
    }

    //保存deviceModelId

    public static boolean saveDeviceModelId(Context context,String deviceModelId,String deviceID){
        SharedPreferences sharedPreferences = context.getSharedPreferences("DEVICEMODILID",context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("MODELID",deviceModelId);
        editor.putString("DEVICEID",deviceID);
        editor.commit();
        return true;
    }

    //读取deviceModelId
    public static String getDeviceModelId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("DEVICEMODILID",context.MODE_PRIVATE);
        return sharedPreferences.getString("MODELID",null);
    }

    //读取deviceID
    public static String getDeviceId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("DEVICEMODILID",context.MODE_PRIVATE);
        return sharedPreferences.getString("DEVICEID",null);
    }


    //保存登录设置信息

    public static boolean saveRememberPwd(Context context,String pwd){
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserSetMessage",context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("pwd",pwd);
        editor.commit();
        return true;
    }

    //保存登录设置信息

    public static boolean saveRememberAuto(Context context,String auto){
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserSetMessage",context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("auto",auto);
        editor.commit();
        return true;
    }

    public static String getRememberPwd(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserSetMessage",context.MODE_PRIVATE);
        return sharedPreferences.getString("pwd",null);

    }

    public static String getRememberAuto(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("UserSetMessage",context.MODE_PRIVATE);
        return sharedPreferences.getString("auto",null);

    }

    public static boolean savePlcDataModelId(Context context,String id){
        SharedPreferences sharedPreferences = context.getSharedPreferences("PlcDataModelId",context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("PlcDataModelId",id);
        editor.commit();
        return true;
    }

    public static String getPlcDataModelId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("PlcDataModelId",context.MODE_PRIVATE);
        return sharedPreferences.getString("PlcDataModelId",null);
    }

    //保存orgID
    public static boolean saveOrgId(Context context,String id){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ORGID",context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("orgID",id);
        editor.commit();
        return true;
    }

    public static String getOrgId(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences("ORGID",context.MODE_PRIVATE);
        return sharedPreferences.getString("orgID",null);
    }
}
