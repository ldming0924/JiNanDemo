package com.kawakp.demingliu.jinandemo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.kawakp.demingliu.jinandemo.R;
import com.kawakp.demingliu.jinandemo.constant.Config;
import com.kawakp.demingliu.jinandemo.listener.IOnNetResultListener;
import com.kawakp.demingliu.jinandemo.net.NetController;
import com.kawakp.demingliu.jinandemo.utils.SharedPerferenceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by deming.liu on 2016/8/30.
 */
public class MyLockActivity extends Activity implements IOnNetResultListener {
    private String cookie;
    private String json;
     TextView message;
     TextView time;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final Window win = getWindow();
        win.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        setContentView(R.layout.lockact);
        initView();
        initData();

    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        PowerManager pm = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        if (!pm.isScreenOn()) {
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |
                    PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");
            wl.acquire();
            wl.release();
        }


    }


    protected void initView() {
        message = (TextView) findViewById(R.id.textView_message);
        time = (TextView) findViewById(R.id.textView_time);


    }


    protected void initData() {
        cookie = SharedPerferenceHelper.getCookie(MyLockActivity.this);

        String url = "http://kawakp.chinclouds.com:58010/userconsle/deviceAlarms?pageSize=1";
        //请求报警内容
        NetController netController = new NetController();
        netController.requestNet(MyLockActivity.this, url, NetController.HttpMethod.GET, Config.FLAG_ZERO, MyLockActivity.this, cookie, null, null);
    }



    @Override
    public void onNetResult(int flag, String jsonResult) {
        json = jsonResult;
    }

    @Override
    public void onNetComplete(int flag) {
        if (json != null) {
            JSONObject object = null;
            try {
                object = new JSONObject(json);
                JSONArray array = object.getJSONArray("list");
                JSONObject obj = array.getJSONObject(0);
                message.setText(obj.getString("displayName"));
                time.setText(obj.getString("createDate"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
