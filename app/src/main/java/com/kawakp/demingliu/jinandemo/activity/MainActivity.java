package com.kawakp.demingliu.jinandemo.activity;

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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jauker.widget.BadgeView;
import com.kawakp.demingliu.jinandemo.R;
import com.kawakp.demingliu.jinandemo.constant.Config;
import com.kawakp.demingliu.jinandemo.fragment.ControlSetFragment;
import com.kawakp.demingliu.jinandemo.fragment.ParameterSetFragment;
import com.kawakp.demingliu.jinandemo.fragment.HistoryWarnFragment;
import com.kawakp.demingliu.jinandemo.fragment.RealTimeDataFragment;
import com.kawakp.demingliu.jinandemo.service.RealTimeDataService;
import com.kawakp.demingliu.jinandemo.utils.SharedPerferenceHelper;

import butterknife.Bind;
import butterknife.OnClick;


public class MainActivity extends BaseActivity  {
    @Bind(R.id.lin1)
    LinearLayout lin1;
    @Bind(R.id.lin2)
    LinearLayout lin2;
    @Bind(R.id.lin3)
    LinearLayout lin3;
    @Bind(R.id.lin4)
    LinearLayout lin4;
    @Bind(R.id.img1)
    ImageView img1;
    @Bind(R.id.img2)
    ImageView img2;
    @Bind(R.id.img3)
    ImageView img3;
    @Bind(R.id.img4)
    ImageView img4;
    @Bind(R.id.lin_back)
    LinearLayout lin_back;
    @Bind(R.id.lin_anim)
    LinearLayout lin_anim;
    @Bind(R.id.textView_title)
    TextView title;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private RealTimeDataFragment realTimeDataFragment;
    private RealTimeDataService realTimeDataService;
    private int total = 0;
    private int update = 0;
    private BadgeView badgeView;
    private WarmReceive warmReceive;




    @Override
    public int setContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        lin_anim.setVisibility(View.GONE);
        title.setText(getIntent().getStringExtra("TITLE"));
        realTimeDataFragment = new RealTimeDataFragment();
        setFragment(realTimeDataFragment);

        initData();

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


    protected void initData() {
        Intent intent = new Intent(MainActivity.this, RealTimeDataService.class);
        bindService(intent, conn, Context.BIND_AUTO_CREATE);
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


    @OnClick({R.id.lin_back,R.id.lin_anim,R.id.lin1,R.id.lin2,R.id.lin3,R.id.lin4})
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


}
