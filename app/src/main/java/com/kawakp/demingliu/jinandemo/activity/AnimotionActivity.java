package com.kawakp.demingliu.jinandemo.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import com.kawakp.demingliu.jinandemo.R;
import com.kawakp.demingliu.jinandemo.bean.DataDisplayActBean;
import com.kawakp.demingliu.jinandemo.utils.Animatiom;
import com.kawakp.demingliu.jinandemo.utils.HttpUtils;
import com.kawakp.demingliu.jinandemo.utils.MyAnimation;
import com.kawakp.demingliu.jinandemo.utils.Path;
import com.kawakp.demingliu.jinandemo.utils.SharedPerferenceHelper;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by zuheng.lv on 2016/8/8.
 */
public class AnimotionActivity extends Activity implements View.OnClickListener {



    private TextView animotion_forpressure_1,animotion_fortemp_1,animotion_valve,animotion_fortemp_2,animotion_forpressure_2,animotion_returnpressure_1,animotion_returntemp_1,animotion_returntemp_2,animotion_returnpressure_2,animotion_water;
    private MyAnimation animatiom;
    private boolean flag = true;
    private List<DataDisplayActBean> totallist = new ArrayList<DataDisplayActBean>();
    private String cookie;
    private LinearLayout lin_back;
    private MyThread myThread;
    private String url;
    private String deviceID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setContentView(R.layout.activity_animotion);
        initView();
        initData();
        setListen();
       // DataShow();
    }

    private void setListen() {
        lin_back.setOnClickListener(this);
    }

    private void initView(){
        animotion_forpressure_1 = (TextView) findViewById(R.id.animotion_forpressure_1);
        animotion_fortemp_1 = (TextView) findViewById(R.id.animotion_fortemp_1);
        //animotion_valve = (TextView) findViewById(R.id.animotion_valve);
        animotion_fortemp_2 = (TextView) findViewById(R.id.textView32);
        animotion_forpressure_2 = (TextView) findViewById(R.id.animotion_forpressure_2);
        animotion_returnpressure_1 = (TextView) findViewById(R.id.animotion_returnpressure_1);
        animotion_returntemp_1 = (TextView) findViewById(R.id.animotion_returntemp_1);
        animotion_returntemp_2 = (TextView) findViewById(R.id.animotion_returntemp_2);
        animotion_returnpressure_2 = (TextView) findViewById(R.id.animotion_returnpressure_2);
        animotion_water = (TextView) findViewById(R.id.animotion_water);
        lin_back = (LinearLayout) findViewById(R.id.lin_back);



        animatiom= (MyAnimation) findViewById(R.id.anition);
        animatiom.start();
        flag = true;
    }
    private void initData(){
        cookie = SharedPerferenceHelper.getCookie(AnimotionActivity.this);
        deviceID = SharedPerferenceHelper.getDeviceId(AnimotionActivity.this);
        url = "http://kawakp.chinclouds.com:60034/userconsle/devices/"+deviceID+"/elementTables/jngn_t_sjxs/datas?pageNum=1&pageSize=1";
        if (myThread == null) {
            myThread = new MyThread();
        }

        new Thread(myThread).start();
    }
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.lin_back:
                finish();
                break;
        }
    }

    public class MyThread implements Runnable {

        @Override
        public void run() {
            while (flag) {
                try {
                    Thread.sleep(2000);
                    byte[] bytes = HttpUtils.loadByteFromURL1(url,cookie);
                    if (bytes != null) {
                        String result = new String(bytes);
                        Message message = Message.obtain();
                        message.what = 10001;
                        message.obj = result;
                        handler.sendMessage(message);
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 10001:
                    String message = (String) msg.obj;
                    JSONObject object = JSON.parseObject(message);
                    JSONArray array = object.getJSONArray("list");
                    List<DataDisplayActBean> list = JSON.parseArray(array.toString(),DataDisplayActBean.class);
                    totallist.clear();
                    totallist.addAll(list);

                    animotion_forpressure_1.setText(totallist.get(0).getGY1()+"MPa");
                    animotion_fortemp_1.setText(totallist.get(0).getGW1() + "℃");

                    animotion_fortemp_2.setText(totallist.get(0).getGW2() + "℃");
                    animotion_forpressure_2.setText(totallist.get(0).getGY2()+"MPa");
                    animotion_returnpressure_1.setText(totallist.get(0).getHY1()+"MPa");
                    animotion_returntemp_1.setText(totallist.get(0).getHW1() + "℃");
                    animotion_returntemp_2.setText(totallist.get(0).getHW2() + "℃");
                    animotion_returnpressure_2.setText(totallist.get(0).getHY2()+"MPa");
                    animotion_water.setText(totallist.get(0).getWL()+"m");

                    int xhv1 = totallist.get(0).getRUN_XHV1();
                    int xhv2 = totallist.get(0).getRUN_XHV2();
                    int bsv1 = totallist.get(0).getRUN_BSV1();
                    int bsv2 = totallist.get(0).getRUN_BSV2();

                    if (xhv1 != 0  || xhv2 != 0){
                        animatiom.openCircle(1);
                    }else {
                        animatiom.stopCircle(1);
                    }
                    ////////////////////////////////////
                    if (xhv1 != 0 && xhv2 == 0) {
                       animatiom.startBeng(1);
                        animatiom.stopBeng(2);
                        animatiom.openCircle(2);
                        animatiom.stopCircle(7);
                        animatiom.stopCircle(8);
                    }else if (xhv1 == 0 && xhv2 != 0){
                        animatiom.startBeng(2);
                        animatiom.stopBeng(1);
                        animatiom.openCircle(2);
                    }else if (xhv1 !=0 && xhv2 != 0){
                        animatiom.startBeng(1);
                        animatiom.startBeng(2);
                        animatiom.openCircle(2);
                    }else {
                        animatiom.stopCircle(7);
                        animatiom.stopCircle(8);
                        animatiom.stopCircle(2);
                    }
                    // /////////////////////////////  1号补水泵通1号循环泵通

                    if (bsv1 != 0 && bsv2 == 0){
                        if (xhv1 != 0 && xhv2 == 0) {
                            animatiom.startBeng(1);//打开循环泵1
                            animatiom.stopBeng(2);
                        }else if (xhv1 == 0 && xhv2 != 0){
                            animatiom.stopBeng(1);
                            animatiom.startBeng(2);
                        }else if (xhv1 == 0 && xhv2 == 0){
                            animatiom.stopBeng(1);
                            animatiom.stopBeng(2);
                        }else {
                            animatiom.startBeng(2); //打开循环泵2
                            animatiom.stopBeng(1);
                        }
                        animatiom.stopCircle(4);
                        animatiom.stopCircle(5);
                        animatiom.openCircle(3);
                    }else if (bsv1 == 0){
                        animatiom.stopCircle(3);
                    }
////////////////////////////////////////////////////////////////////////
                    if (bsv1 == 0 && bsv2 != 0){

                        if (xhv1 != 0 && xhv2 == 0) {
                            animatiom.startBeng(1); //打开循环泵1
                            animatiom.stopBeng(2);
                        }else if (xhv1 == 0 && xhv2 != 0){
                            animatiom.startBeng(2);
                            animatiom.stopBeng(1);
                        }else if (xhv1 == 0 && xhv2 == 0){
                            animatiom.stopBeng(1);
                            animatiom.stopBeng(2);
                        }else {
                            animatiom.startBeng(2); //打开循环泵2
                            animatiom.stopBeng(1);
                        }
                        animatiom.stopCircle(3);
                        animatiom.stopCircle(5);
                        animatiom.openCircle(4);
                    }else if (bsv2 ==0){
                        animatiom.stopCircle(4);
                    }
///////////////////////////////////////////////////////////////////////////////
                    if (bsv1 != 0 && bsv2 != 0){
                        animatiom.stopCircle(3);
                        animatiom.stopCircle(4);
                        if (xhv1 != 0 && xhv2 == 0) {
                            animatiom.startBeng(1); //打开循环泵1
                            animatiom.stopBeng(2);
                        }else if (xhv1 == 0 && xhv2 != 0){
                            animatiom.startBeng(2);
                            animatiom.stopBeng(1);
                        }else if (xhv1 == 0 && xhv2 == 0){
                            animatiom.stopBeng(1);
                            animatiom.stopBeng(2);
                        }else {
                            //animatiom.stopBeng(1);
                           // animatiom.startBeng(2);//打开循环泵2
                        }
                        animatiom.openCircle(5);
                    }else {
                        animatiom.stopCircle(5);
                        animatiom.stopCircle(6);
                    }
                    break;
            }
        }
    };


    @Override
    protected void onDestroy() {
        super.onDestroy();
        flag = false;
        handler.removeCallbacks(myThread);

    }

}
