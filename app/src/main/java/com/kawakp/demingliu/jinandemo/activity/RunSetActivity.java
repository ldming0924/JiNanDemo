package com.kawakp.demingliu.jinandemo.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kawakp.demingliu.jinandemo.R;
import com.kawakp.demingliu.jinandemo.bean.DataDisplayActBean;
import com.kawakp.demingliu.jinandemo.constant.Config;
import com.kawakp.demingliu.jinandemo.listener.IOnNetResultListener;
import com.kawakp.demingliu.jinandemo.net.NetController;
import com.kawakp.demingliu.jinandemo.utils.HttpUtils;
import com.kawakp.demingliu.jinandemo.utils.Path;
import com.kawakp.demingliu.jinandemo.utils.SharedPerferenceHelper;
import com.kawakp.demingliu.jinandemo.widget.CheckSwitchButton;

import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by deming.liu on 2016/8/6.
 */
public class RunSetActivity extends BaseActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener, IOnNetResultListener {
    private CheckSwitchButton checkSwitchButton;
    private CheckSwitchButton checkSwitchButton1;
    private CheckSwitchButton checkSwitchButton2;
    private CheckSwitchButton checkSwitchButton3;
    private CheckSwitchButton checkSwitchButton4;
    private CheckSwitchButton checkSwitchButton5;
    private LinearLayout lin_back;
    private String cookie;
    private boolean flag = true;
    private List<DataDisplayActBean> list;
    private String PATH = "http://kawakp.chinclouds.com:58010/userconsle/devices/deviceId0022/elementTables/gxznhrjz_t_1/datas?pageNum=1&pageSize=1";
    private String PATH1 = "http://kawakp.chinclouds.com:58010/userconsle/devices/";
    //    private String PATH2 = "/datas?pageNum=1&pageSize=1";
    private String json;
    private NetController netController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.runsetact);
        initView();
        initData();
        setListen();
        // getData();
    }

    @Override
    protected void initView() {

        /*checkSwitchButton1 = getView(R.id.checkswitchButton1);
        checkSwitchButton2 = getView(R.id.checkswitchButton2);
        checkSwitchButton3 = getView(R.id.checkswitchButton3);
        checkSwitchButton4 = getView(R.id.checkswitchButton4);*/

        lin_back = getView(R.id.lin_back);
    }

    @Override
    protected void initData() {
        cookie = SharedPerferenceHelper.getCookie(RunSetActivity.this);
    }

    @Override
    protected void setListen() {
        lin_back.setOnClickListener(this);
        checkSwitchButton.setOnCheckedChangeListener(this);
        checkSwitchButton1.setOnCheckedChangeListener(this);
        checkSwitchButton2.setOnCheckedChangeListener(this);
        checkSwitchButton3.setOnCheckedChangeListener(this);
        checkSwitchButton4.setOnCheckedChangeListener(this);
        checkSwitchButton5.setOnCheckedChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_back:
                finish();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
           /* case R.id.checkswitchButton:
                if (isChecked){
                     json = "{\"value\":1}";
                    checkSwitchButton1.setEnabled(true);
                    checkSwitchButton2.setEnabled(true);
                }else {
                     json = "{\"value\":0}";
                    checkSwitchButton1.setEnabled(false);
                    checkSwitchButton2.setEnabled(false);
                }
                netController = new NetController();
                netController.requestNet(RunSetActivity.this,PATH1+"deviceId0022/elements/gxznhrjz_e_11",NetController.HttpMethod.PUT, Config.FLAG_ZERO,this,cookie,json,null);
               System.out.println(PATH1+"deviceId0022/elements  /gxznhrjz_e_11");
                break;*/
         /*   case R.id.checkswitchButton1:
                if (isChecked){
                    json = "{\"value\":1}";
                }else {
                    json = "{\"value\":0}";
                }
                netController = new NetController();
                netController.requestNet(RunSetActivity.this,PATH1+"deviceId0022/elements/gxznhrjz_e_12",NetController.HttpMethod.PUT, Config.FLAG_ZERO,this,cookie,json,null);
                break;
            case R.id.checkswitchButton2:
                if (isChecked){
                    json = "{\"value\":1}";
                }else {
                    json = "{\"value\":0}";
                }
                netController = new NetController();
                netController.requestNet(RunSetActivity.this,PATH1+"deviceId0022/elements/gxznhrjz_e_13",NetController.HttpMethod.PUT, Config.FLAG_ZERO,this,cookie,json,null);
                break;
            case R.id.checkswitchButton3:
                if (isChecked){
                    json = "{\"value\":1}";
                }else {
                    json = "{\"value\":0}";
                }
                netController = new NetController();
                netController.requestNet(RunSetActivity.this,PATH1+"deviceId0022/elements/gxznhrjz_e_15",NetController.HttpMethod.PUT, Config.FLAG_ZERO,this,cookie,json,null);
                break;
            case R.id.checkswitchButton4:
                if (isChecked){
                    json = "{\"value\":1}";
                }else {
                    json = "{\"value\":0}";
                }
                netController = new NetController();
                netController.requestNet(RunSetActivity.this,PATH1+"deviceId0022/elements/gxznhrjz_e_16",NetController.HttpMethod.PUT, Config.FLAG_ZERO,this,cookie,json,null);
                break;*/
          /*  case R.id.checkswitchButton5:
                if (isChecked){
                    json = "{\"value\":1}";
                    checkSwitchButton3.setEnabled(true);
                    checkSwitchButton4.setEnabled(true);
                }else {
                    json = "{\"value\":0}";
                    checkSwitchButton3.setEnabled(false);
                    checkSwitchButton4.setEnabled(false);
                }
                netController = new NetController();
                netController.requestNet(RunSetActivity.this,PATH1+"deviceId0022/elements/gxznhrjz_e_14",NetController.HttpMethod.PUT, Config.FLAG_ZERO,this,cookie,json,null);
                break;*/
        }
    }

/*
    private void getData(){
        Observable.create(new Observable.OnSubscribe<List<DataDisplayActBean>>() {
            @Override
            public void call(Subscriber<? super List<DataDisplayActBean>> subscriber) {
                while (flag){
                    byte[] bytes = HttpUtils.loadByteFromURL1(Path.REALTIME_DATA_PATH,cookie);
                    String result = new String(bytes);
//                    System.out.println(result);
                    JSONObject object = JSON.parseObject(result);
                    JSONArray array = object.getJSONArray("list");
                    list = JSON.parseArray(array.toString(),DataDisplayActBean.class);
                    subscriber.onNext(list);
                    SystemClock.sleep(5000);
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<DataDisplayActBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(List<DataDisplayActBean> dataDisplayActBean) {
                       *//* checkSwitchButton.setChecked(dataDisplayActBean.get(0).getD1005());
                        checkSwitchButton1.setChecked(dataDisplayActBean.get(0).getD1007());
                        checkSwitchButton2.setChecked(dataDisplayActBean.get(0).getD1009());
                        checkSwitchButton3.setChecked(dataDisplayActBean.get(0).getD1016());
                        checkSwitchButton4.setChecked(dataDisplayActBean.get(0).getD1018());
                        checkSwitchButton5.setChecked(dataDisplayActBean.get(0).getD1041());*//*
                        if(checkSwitchButton.isChecked()){
                            checkSwitchButton1.setEnabled(true);
                            checkSwitchButton2.setEnabled(true);
                        }else{
                            checkSwitchButton1.setEnabled(false);
                            checkSwitchButton2.setEnabled(false);
                        }
                        if(checkSwitchButton5.isChecked()){
                            checkSwitchButton3.setEnabled(true);
                            checkSwitchButton4.setEnabled(true);
                        }else{
                            checkSwitchButton3.setEnabled(false);
                            checkSwitchButton4.setEnabled(false);
                        }
                    }
                });
    }*/

    @Override
    public void onNetResult(int flag, String jsonResult) {
        System.out.println(jsonResult + "");
    }

    @Override
    public void onNetComplete(int flag) {

    }
}
