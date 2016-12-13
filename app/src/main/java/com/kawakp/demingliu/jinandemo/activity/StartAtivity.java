package com.kawakp.demingliu.jinandemo.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.kawakp.demingliu.jinandemo.R;
import com.kawakp.demingliu.jinandemo.http.OkHttpHelper;
import com.kawakp.demingliu.jinandemo.http.SimpleCallback;
import com.kawakp.demingliu.jinandemo.utils.AnimationUtil;
import com.kawakp.demingliu.jinandemo.utils.IToast;
import com.kawakp.demingliu.jinandemo.utils.Path;
import com.kawakp.demingliu.jinandemo.utils.SharedPerferenceHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Response;

/**
 * Created by deming.liu on 2016/8/25.
 */
public class StartAtivity extends BaseActivity {
    private static final long DELAY_TIME = 3000L;
    private String userName;
    private String passWord;

    private String auto;



    @Override
    public int setContentViewId() {
        return R.layout.activity_start;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        initData();
    }

    protected void initData() {
        SharedPreferences sharedPreferences = SharedPerferenceHelper.getUserMessage(StartAtivity.this);
        userName = sharedPreferences.getString("username", null);
        passWord = sharedPreferences.getString("password", null);
        auto = SharedPerferenceHelper.getRememberAuto(StartAtivity.this);
        Log.d("TAG","aaaaaaaaaa");

        doLogin();
    }

    /**
     * 登录
     */
    private void doLogin() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (userName == null || passWord == null || auto == null || auto.equals("false")) {
                    startActivity(new Intent(StartAtivity.this, LoginActivity.class));
                    AnimationUtil.finishActivityAnimation(StartAtivity.this);

                } else {
                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("username", userName);
                    map.put("password", passWord);
                    OkHttpHelper okHttpHelper = OkHttpHelper.getInstance(StartAtivity.this);
                    okHttpHelper.post(Path.LOGIN_PATH, map, new SimpleCallback<String>(StartAtivity.this) {

                        @Override
                        public void onSuccess(Response response, String s) {
                            if (s == null ) {
                                IToast.showToast(StartAtivity.this, "登录失败，请检查网络");
                                startActivity(new Intent(StartAtivity.this, LoginActivity.class));
                                AnimationUtil.finishActivityAnimation(StartAtivity.this);
                            } else {
                                JSONObject object = null;
                                try {
                                    object = new JSONObject(s);
                                    if (s.contains("\"error\"")) {
                                        IToast.showToast(StartAtivity.this, object.getString("error"));
                                    } else {
                                        String username = object.getString("username");
                                        boolean b = SharedPerferenceHelper.saveUserMessage(StartAtivity.this, username, passWord);
                                        if (b) {
                                            Intent intent_Login = new Intent(StartAtivity.this, DeviceListActivity.class);
                                            startActivity(intent_Login);
                                            AnimationUtil.finishActivityAnimation(StartAtivity.this);
                                            finish();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                        @Override
                        public void onError(Response response, int code, Exception e) {

                        }
                    });

                }
            }
        }, DELAY_TIME);
    }




}
