package com.kawakp.demingliu.jinandemo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;

import com.kawakp.demingliu.jinandemo.R;
import com.kawakp.demingliu.jinandemo.constant.Config;
import com.kawakp.demingliu.jinandemo.listener.IOnNetResultListener;
import com.kawakp.demingliu.jinandemo.net.NetController;
import com.kawakp.demingliu.jinandemo.service.ServiceHelper;
import com.kawakp.demingliu.jinandemo.service.WarnService;
import com.kawakp.demingliu.jinandemo.utils.AnimationUtil;
import com.kawakp.demingliu.jinandemo.utils.IToast;
import com.kawakp.demingliu.jinandemo.utils.Path;
import com.kawakp.demingliu.jinandemo.utils.SharedPerferenceHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by deming.liu on 2016/8/25.
 */
public class StartAtivity extends BaseActivity implements IOnNetResultListener {
    private static final long DELAY_TIME = 3000L;
    private String userName;
    private String passWord;
    private String jsonString;
    private String auto;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.startact);
        initData();
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initData() {
        SharedPreferences sharedPreferences = SharedPerferenceHelper.getUserMessage(StartAtivity.this);
        userName = sharedPreferences.getString("username", null);
        passWord = sharedPreferences.getString("password", null);
        auto = SharedPerferenceHelper.getRememberAuto(StartAtivity.this);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.d("Start", userName + "　" + passWord + "  " + auto);
                if (userName == null || passWord == null || auto == null || auto.equals("false")) {
                    startActivity(new Intent(StartAtivity.this, LoginActivity.class));
                    AnimationUtil.finishActivityAnimation(StartAtivity.this);

                } else {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("username", userName);
                    map.put("password", passWord);
                    NetController netController = new NetController();
                    netController.requestNet(StartAtivity.this, Path.LOGIN_PATH, NetController.HttpMethod.POST, Config.FLAG_ZERO, StartAtivity.this, null, null, map);

                }
            }
        }, DELAY_TIME);


    }

    @Override
    protected void setListen() {

    }

    @Override
    public void onNetResult(int flag, String jsonResult) {
        jsonString = jsonResult;
    }

    @Override
    public void onNetComplete(int flag) {
        if (jsonString == null || jsonString.equals("-1")) {
            IToast.showToast(StartAtivity.this, "登录失败，请检查网络");
            startActivity(new Intent(StartAtivity.this, LoginActivity.class));
            AnimationUtil.finishActivityAnimation(StartAtivity.this);
        } else {
            JSONObject object = null;
            try {
                object = new JSONObject(jsonString);
                if (jsonString.contains("\"error\"")) {
                    IToast.showToast(StartAtivity.this, object.getString("error"));
                } else {
                    String username = object.getString("username");
                    boolean b = SharedPerferenceHelper.saveUserMessage(StartAtivity.this, username, passWord);
                    if (b) {
                        Intent intent_Login = new Intent(StartAtivity.this, DeviceListActivity.class);
                        startActivity(intent_Login);

                        finish();
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
