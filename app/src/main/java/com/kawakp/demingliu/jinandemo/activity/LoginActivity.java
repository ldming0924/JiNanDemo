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
import com.kawakp.demingliu.jinandemo.listener.IOnNetResultListener;
import com.kawakp.demingliu.jinandemo.net.NetController;
import com.kawakp.demingliu.jinandemo.utils.IToast;
import com.kawakp.demingliu.jinandemo.utils.Path;
import com.kawakp.demingliu.jinandemo.utils.SharedPerferenceHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by deming.liu on 2016/8/5.
 */
public class LoginActivity extends BaseActivity implements View.OnClickListener, IOnNetResultListener {
    private EditText userEdt;
    private EditText passwordEdt;
    private Button loginBtn;
    private String jsonString;
    private ProgressDialog progressDialog;
    private CheckBox remember_password;
    private CheckBox auto_login;
    private String s1 = null; //记住密码
    private String s2 = null; //自动登录

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.loginact);
        initView();
        initData();
        setListen();
    }

    @Override
    protected void initView() {
        userEdt = getView(R.id.editText_username);
        passwordEdt = getView(R.id.editText_password);
        loginBtn = getView(R.id.button_login);
        remember_password = getView(R.id.checkBox_remember_password);
        auto_login = getView(R.id.checkBox_auto_login);
    }

    @Override
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

    @Override
    protected void setListen() {
        loginBtn.setOnClickListener(this);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_login:

                if (TextUtils.isEmpty(userEdt.getText().toString())) {
                    IToast.showToast(LoginActivity.this, "请输入账号");
                } else if (TextUtils.isEmpty(passwordEdt.getText().toString())) {
                    IToast.showToast(LoginActivity.this, "请输入密码");
                } else {
                    progressDialog = new ProgressDialog(LoginActivity.this);
                    progressDialog.setMessage("正在登录,请稍候...");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("username", userEdt.getText().toString());
                    map.put("password", passwordEdt.getText().toString());
                    NetController netController = new NetController();
                    netController.requestNet(LoginActivity.this, Path.LOGIN_PATH, NetController.HttpMethod.POST, Config.FLAG_ZERO, this, null, null, map);
                }

                break;
        }
    }

    @Override
    public void onNetResult(int flag, String jsonResult) {
       // Log.d("TAG", jsonResult + "======登录数据返回=====");
        jsonString = jsonResult;
    }

    @Override
    public void onNetComplete(int flag) {
        if (jsonString == null || jsonString.equals("-1")) {
            IToast.showToast(LoginActivity.this, "登录失败，请检查网络");
        } else {
            JSONObject object = null;
            try {
                object = new JSONObject(jsonString);
                if (jsonString.contains("\"error\"")) {
                    IToast.showToast(LoginActivity.this, object.getString("error"));
                } else {
                    String username = object.getString("username");
                    boolean b = SharedPerferenceHelper.saveUserMessage(LoginActivity.this, username, passwordEdt.getText().toString());
                    boolean bb = SharedPerferenceHelper.saveOrgId(LoginActivity.this,object.getString("orgId"));
                    if (b && bb) {
                        Intent intent_Login = new Intent(LoginActivity.this, DeviceListActivity.class);
                        startActivity(intent_Login);

                        progressDialog.dismiss();
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
}
