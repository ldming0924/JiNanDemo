package com.kawakp.demingliu.jinandemo.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;


import com.kawakp.demingliu.jinandemo.R;
import com.kawakp.demingliu.jinandemo.constant.Config;
import com.kawakp.demingliu.jinandemo.http.OkHttpHelper;
import com.kawakp.demingliu.jinandemo.http.SpotsCallBack;
import com.kawakp.demingliu.jinandemo.listener.IOnNetResultListener;
import com.kawakp.demingliu.jinandemo.net.NetController;
import com.kawakp.demingliu.jinandemo.utils.IToast;
import com.kawakp.demingliu.jinandemo.utils.Path;
import com.kawakp.demingliu.jinandemo.utils.SharedPerferenceHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by deming.liu on 2016/8/5.
 */
public class LoginActivity extends BaseActivity {
    @Bind(R.id.editText_username)
     EditText userEdt;
    @Bind(R.id.editText_password)
     EditText passwordEdt;
    @Bind(R.id.button_login)
     Button loginBtn;
    @Bind(R.id.checkBox_remember_password)
     CheckBox remember_password;
    @Bind(R.id.checkBox_auto_login)
     CheckBox auto_login;
    private String s1 = null; //记住密码
    private String s2 = null; //自动登录



    @Override
    public int setContentViewId() {
        return R.layout.loginact;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        initData();
        setListen();
    }




    protected void initData() {

        String str1 = SharedPerferenceHelper.getRememberPwd(LoginActivity.this);
        String str2 = SharedPerferenceHelper.getRememberAuto(LoginActivity.this);
        SharedPreferences sharedPreferences = SharedPerferenceHelper.getUserMessage(LoginActivity.this);
        String userName = sharedPreferences.getString("username", null);
        String passWord = sharedPreferences.getString("password", null);

        //设置记住密码与自动登录
        if (str1 != null && str1.equals("true")) {
            remember_password.setChecked(true);
            userEdt.setText(userName);
            passwordEdt.setText(passWord);
        } else {
            remember_password.setChecked(false);

        }

        if (str2 != null && str2.equals("true")) {
            auto_login.setChecked(true);

        } else {
            auto_login.setChecked(false);
        }
    }


    protected void setListen() {
        //是否记住密码
        remember_password.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    boolean b1 = SharedPerferenceHelper.saveRememberPwd(LoginActivity.this, "true");
                } else {
                    boolean b1 = SharedPerferenceHelper.saveRememberPwd(LoginActivity.this, "false");
                }
            }
        });
        //是否自动登录
        auto_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    boolean b1 = SharedPerferenceHelper.saveRememberAuto(LoginActivity.this, "true");
                } else {
                    boolean b1 = SharedPerferenceHelper.saveRememberAuto(LoginActivity.this, "false");
                }
            }
        });
    }

    @OnClick(R.id.button_login)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_login:

                if (TextUtils.isEmpty(userEdt.getText().toString())) {
                    IToast.showToast(LoginActivity.this, "请输入账号");
                } else if (TextUtils.isEmpty(passwordEdt.getText().toString())) {
                    IToast.showToast(LoginActivity.this, "请输入密码");
                } else {

                    Map<String, Object> map = new HashMap<String, Object>();
                    map.put("username", userEdt.getText().toString());
                    map.put("password", passwordEdt.getText().toString());

                    OkHttpHelper okHttpHelper = OkHttpHelper.getInstance(LoginActivity.this);
                    okHttpHelper.post(Path.LOGIN_PATH, map, new SpotsCallBack<String>(LoginActivity.this) {

                        @Override
                        public void onSuccess(Response response, String s) {
                            if (s == null ) {
                                IToast.showToast(LoginActivity.this, "登录失败，请检查网络");

                            } else {
                                JSONObject object = null;
                                try {
                                    object = new JSONObject(s);
                                    if (s.contains("\"error\"")) {
                                        IToast.showToast(LoginActivity.this, object.getString("error"));

                                    } else {
                                        String username = object.getString("username");
                                        boolean b = SharedPerferenceHelper.saveUserMessage(LoginActivity.this, username, passwordEdt.getText().toString());
                                        boolean bb = SharedPerferenceHelper.saveOrgId(LoginActivity.this,object.getString("orgId"));
                                        if (b && bb) {
                                            Intent intent_Login = new Intent(LoginActivity.this, DeviceListActivity.class);
                                            startActivity(intent_Login);


                                            finish();
                                        } else {
                                            IToast.showToast(LoginActivity.this, "登录失败");
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }

                        @Override
                        public void onError(Response response, int code, Exception e) {
                            Log.d("TAG",e.toString());
                        }
                    });
                }

                break;
        }
    }


}
