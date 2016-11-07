package com.kawakp.demingliu.jinandemo.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jauker.widget.BadgeView;
import com.kawakp.demingliu.jinandemo.R;
import com.kawakp.demingliu.jinandemo.constant.Config;
import com.kawakp.demingliu.jinandemo.fragment.ControlSetFragment;
import com.kawakp.demingliu.jinandemo.fragment.ParamFragment;
import com.kawakp.demingliu.jinandemo.fragment.ParameterSetFragment;
import com.kawakp.demingliu.jinandemo.fragment.HistoryWarnFragment;
import com.kawakp.demingliu.jinandemo.fragment.RealTimeDataFragment;
import com.kawakp.demingliu.jinandemo.listener.IOnNetResultListener;
import com.kawakp.demingliu.jinandemo.net.NetController;
import com.kawakp.demingliu.jinandemo.receiver.NotificationReceiver;
import com.kawakp.demingliu.jinandemo.service.RealTimeDataService;
import com.kawakp.demingliu.jinandemo.utils.Path;
import com.kawakp.demingliu.jinandemo.utils.SharedPerferenceHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends BaseActivity implements View.OnClickListener, IOnNetResultListener {
    private LinearLayout lin1, lin2, lin3, lin4;
    private ImageView img1, img2, img3, img4;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private RealTimeDataFragment realTimeDataFragment;
    private RealTimeDataService realTimeDataService;
    private int total = 0;
    private int update = 0;
    private BadgeView badgeView;
    private WarmReceive warmReceive;
    private String cookie;
    private String jsonString;
    private String time;
    private LinearLayout lin_back;
    private LinearLayout lin_anim;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_main);

        initView();
        initData();
        setListen();
    }

    @Override
    protected void onResume() {
        super.onResume();
        warmReceive = new WarmReceive();
        registerReceiver(warmReceive, new IntentFilter(Config.WARM_RECEIVE));

    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(warmReceive);
    }

    @Override
    protected void initView() {
        lin1 = (LinearLayout) findViewById(R.id.lin1);
        lin2 = (LinearLayout) findViewById(R.id.lin2);
        lin3 = (LinearLayout) findViewById(R.id.lin3);
        lin4 = (LinearLayout) findViewById(R.id.lin4);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
        img4 = (ImageView) findViewById(R.id.img4);

        title = (TextView) findViewById(R.id.textView_title);
        lin_back = (LinearLayout) findViewById(R.id.lin_back);
        lin_anim = (LinearLayout) findViewById(R.id.lin_anim);

        lin_anim.setVisibility(View.GONE);


        title.setText(getIntent().getStringExtra("TITLE"));

        realTimeDataFragment = new RealTimeDataFragment();
        setFragment(realTimeDataFragment);
    }

    @Override
    protected void initData() {
        Intent intent = new Intent(MainActivity.this, RealTimeDataService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
        cookie = SharedPerferenceHelper.getCookie(MainActivity.this);
    }

    @Override
    protected void setListen() {
        lin1.setOnClickListener(this);
        lin2.setOnClickListener(this);
        lin3.setOnClickListener(this);
        lin4.setOnClickListener(this);

        lin_back.setOnClickListener(this);
        lin_anim.setOnClickListener(this);


    }

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {

        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            //返回一个MsgService对象
            realTimeDataService = ((RealTimeDataService.MsgBinder) service).getService();

            realTimeDataService.setCallBack(new RealTimeDataService.MyCallBack() {
                @Override
                public void callBackMessage(String message) {
                    // Log.d("TAG","Main=============="+message);
                    //发送广播
                    if (message != null) {
                        Intent intent = new Intent("com.kawakp.demingliu.jinandemo.activity.MainActivity");
                        intent.putExtra("MESSAGE", message);
                        sendBroadcast(intent);
                    }

                }
            });

        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(conn);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_back:
                finish();
                break;
            case R.id.lin_anim:
                startActivity(new Intent(MainActivity.this, AnimotionActivity.class));
                break;
            case R.id.lin1:
                setFragment(realTimeDataFragment);
                lin_anim.setVisibility(View.GONE);
                img1.setImageResource(R.drawable.data_selected_btn);
                img2.setImageResource(R.drawable.control_btn);
                img3.setImageResource(R.drawable.parameter_btn);
                img4.setImageResource(R.drawable.history_btn);
                break;
            case R.id.lin2:
                ControlSetFragment controlSetFragment = new ControlSetFragment();
                setFragment(controlSetFragment);
                lin_anim.setVisibility(View.VISIBLE);
                img1.setImageResource(R.drawable.data_btn);
                img2.setImageResource(R.drawable.control_selected_btn);
                img3.setImageResource(R.drawable.parameter_btn);
                img4.setImageResource(R.drawable.history_btn);
                break;
            case R.id.lin3:

                // TODO: 2016/9/21  刷新问题，换成ParamFragment，布局是写死的
                ParameterSetFragment parameterSetFragment = new ParameterSetFragment();
                setFragment(parameterSetFragment);
               /* ParamFragment paramFragment = new ParamFragment();
                setFragment(paramFragment);*/
                lin_anim.setVisibility(View.GONE);
                img1.setImageResource(R.drawable.data_btn);
                img2.setImageResource(R.drawable.control_btn);
                img3.setImageResource(R.drawable.parameter_selected_btn);
                img4.setImageResource(R.drawable.history_btn);
                break;
            case R.id.lin4:
                HistoryWarnFragment historyWarnFragment = new HistoryWarnFragment();
                setFragment(historyWarnFragment);
                lin_anim.setVisibility(View.GONE);
                if (badgeView == null) {
                    badgeView = new BadgeView(MainActivity.this);
                }
                badgeView.setTargetView(lin4);
                badgeView.setBadgeCount(0);
                boolean b = SharedPerferenceHelper.saveUpdateNum(getApplicationContext(), update); //保存报警条数到本地
                img1.setImageResource(R.drawable.data_btn);
                img2.setImageResource(R.drawable.control_btn);
                img3.setImageResource(R.drawable.parameter_btn);
                img4.setImageResource(R.drawable.history_selected_btn);
                break;
        }
    }

    public void setFragment(Fragment fragment) {
        manager = getSupportFragmentManager();
        transaction = manager.beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }

    @Override
    public void onNetResult(int flag, String jsonResult) {
        jsonString = jsonResult;
    }

    @Override
    public void onNetComplete(int flag) {
        if (jsonString != null) {
            Log.d("WARM", jsonString);
            JSONObject object = null;
            try {
                object = new JSONObject(jsonString);
                JSONArray array = object.getJSONArray("list");
                JSONObject obj = array.getJSONObject(0);
                if (!obj.getString("createDate").equals(time)) {
                    //showNotification(MainActivity.this, obj.getString("displayName"), obj.getString("createDate"));
                    //time = obj.getString("createDate");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
    }


    public class WarmReceive extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Config.WARM_RECEIVE)) {
                //从本地读取报警条数
                total = SharedPerferenceHelper.getUpdateNum(MainActivity.this);
                update = intent.getIntExtra("TOTAL", 0);
                int i = update - total;//获取更新的条数
                if (badgeView == null) {
                    badgeView = new BadgeView(MainActivity.this);
                }
                badgeView.setTargetView(lin4);
                badgeView.setBadgeCount(i);

            }
        }
    }

    private void showNotification(Context context, String message, String time) {
        Intent broadcastIntent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.
                getBroadcast(context, Config.REQUEST_CODE, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
        builder.setContentTitle("设备报警")
                .setContentText(time + "   " + message)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.image_device)
                .setAutoCancel(true);

        NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = builder.build();
        notification.defaults = Notification.DEFAULT_SOUND;//系统默认提示音
//        notification.defaults |= Notification.DEFAULT_VIBRATE;//震动
//        long[] vibrate = {0,100,200,300};
//        notification.vibrate = vibrate;

        manager.notify(Config.NOTIFY_ID, notification);

    }

}
