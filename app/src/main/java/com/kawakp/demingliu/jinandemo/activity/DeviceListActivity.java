package com.kawakp.demingliu.jinandemo.activity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kawakp.demingliu.jinandemo.R;
import com.kawakp.demingliu.jinandemo.bean.DeviceListBean;
import com.kawakp.demingliu.jinandemo.bean.OrgBean;
import com.kawakp.demingliu.jinandemo.constant.Config;
import com.kawakp.demingliu.jinandemo.http.OkHttpHelper;
import com.kawakp.demingliu.jinandemo.http.SpotsCallBack;
import com.kawakp.demingliu.jinandemo.service.ServiceHelper;
import com.kawakp.demingliu.jinandemo.service.WarnService;
import com.kawakp.demingliu.jinandemo.tree.bean.Bean;
import com.kawakp.demingliu.jinandemo.tree.bean.Node;
import com.kawakp.demingliu.jinandemo.tree.bean.TreeListViewAdapter;
import com.kawakp.demingliu.jinandemo.tree.view.SimpleTreeAdapter;
import com.kawakp.demingliu.jinandemo.utils.Path;
import com.kawakp.demingliu.jinandemo.utils.PopUtils;
import com.kawakp.demingliu.jinandemo.utils.SharedPerferenceHelper;


import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import okhttp3.Response;

/**
 * Created by deming.liu on 2016/8/10.
 */
public class DeviceListActivity extends BaseActivity  {
    @Bind(R.id.lin_back)
     LinearLayout lin_back;
    @Bind(R.id.id_tree)
     ListView listView;
    private List<OrgBean> totallist = new ArrayList<OrgBean>();
    private String orgId;
    int page = 1;
    int pageSize = 1000;
    private TreeListViewAdapter mAdapter;
    private List<Bean> mDatas = new ArrayList<Bean>();
    private String device_url;
    private OkHttpHelper okHttpHelper;


    @Override
    public int setContentViewId() {
        return R.layout.activity_device;
    }

    @Override
    public void initViews(Bundle savedInstanceState) {
        okHttpHelper = OkHttpHelper.getInstance(DeviceListActivity.this);
        initData();
    }

    public void initData() {
        orgId = SharedPerferenceHelper.getOrgId(DeviceListActivity.this);
        //TODO 判断服务是否在运行
        if (!ServiceHelper.isServiceWork(getApplicationContext(), Config.INTENTFILTER)) {
            Intent intent = new Intent(getApplicationContext(), WarnService.class);
            startService(intent);
        }
        device_url = Path.DEVICELIST_PATH + "&pageNum=" + page + "&pageSize=" + pageSize;
        //获取组织结构url
        String url = "http://kawakp.chinclouds.com:60034/userconsle/orgs" + orgId;
        Log.d("TAG","------"+url+"　＝＝＝"+device_url);
        //获取组织结构列表
        getOrg(url);
    }

    /**
     * 获取组织结构
     *
     * @param url
     */
    private void getOrg(String url) {
        okHttpHelper.get(url, new SpotsCallBack<String>(DeviceListActivity.this) {

            @Override
            public void onSuccess(Response response, String s) {
                //Log.d("TAG","zuzhijiegou ==="+s);
                JSONArray array = JSON.parseArray(s);
                List<OrgBean> list = JSON.parseArray(array.toString(), OrgBean.class);
                totallist.addAll(list);
                for (int i = 0; i < totallist.size(); i++) {
                    mDatas.add(new Bean(list.get(i).getId(), list.get(i).getParentId(), list.get(i).getName(), null, null, null));
                }
                //获取设备列表
                getDeviceList();
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.e("DeviceList","code = "+code + " "+e.toString());
            }
        });
    }

    /**
     * 获取设备类别加到组织结构上
     */
    private void getDeviceList() {
        okHttpHelper.get(device_url, new SpotsCallBack<String>(DeviceListActivity.this) {

            @Override
            public void onSuccess(Response response, String s) {
               // Log.d("TAG","设备列表=="+s);

                JSONObject jsonObject = JSON.parseObject(s);
                JSONArray array = jsonObject.getJSONArray("list");
                List<DeviceListBean> list = JSON.parseArray(array.toString(), DeviceListBean.class);
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getOrgId() != null) {
                        for (int j = 0; j < totallist.size(); j++) {
                            if (totallist.get(j).getId().equals(list.get(i).getOrgId())) {
                                //将设备数据绑定到组织结构上
                                mDatas.add(new Bean(list.get(i).getId() + "", list.get(i).getOrgId(), list.get(i).getName(), list.get(i).getPlcDataModelId(), list.get(i).getId(), list.get(i).getOnlineStatus()));
                            }
                        }
                    }

                }

                try {
                    mAdapter = new SimpleTreeAdapter<Bean>(listView, DeviceListActivity.this, mDatas, 0);
                    mAdapter.setOnTreeNodeClickListener(new TreeListViewAdapter.OnTreeNodeClickListener() {
                        @Override
                        public void onClick(Node node, int position) {
                            if (node.isLeaf()) {
                                if (node.getDeviceId() != null ) {
                                    //if (node.getDeviceId() != null && node.getPlcDataModelId() != null) {
                                    //保存deviceModelId
                                    boolean b = SharedPerferenceHelper.saveDeviceModelId(DeviceListActivity.this, node.getPlcDataModelId(), node.getDeviceId());
                                    if (b) {
                                        Intent intent = new Intent(DeviceListActivity.this, MainActivity.class);
                                        intent.putExtra("TITLE", node.getName());
                                        startActivity(intent);
                                    }
                                } else {
                                    Toast.makeText(getApplicationContext(), "暂无设备",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                    });

                } catch (Exception e) {
                    e.printStackTrace();
                    Log.d("ERR", e.toString());
                }
                listView.setAdapter(mAdapter);
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.e("DeviceList","code = "+code + " "+e.toString());
            }
        });
    }



    @OnClick(R.id.lin_back)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_back:
                showPopWindow();
                break;
        }
    }

    private void showPopWindow() {
        View view = LayoutInflater.from(DeviceListActivity.this).inflate(R.layout.dialog_cancel_login, null);
        final PopupWindow pw = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, false);
        pw.setFocusable(true);
        pw.setOutsideTouchable(false);
        pw.setBackgroundDrawable(new BitmapDrawable());
        pw.setAnimationStyle(R.style.myanimation);
        pw.showAtLocation(view, Gravity.BOTTOM, 0, 0);
        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                PopUtils.setBackgroundAlpha(1.0f, DeviceListActivity.this);//设置Popw消失背景为透明
            }
        });
        PopUtils.setBackgroundAlpha(0.5f, DeviceListActivity.this);//设置popw出现时背景透明度
        LinearLayout lin_sure = (LinearLayout) view.findViewById(R.id.lin_sure);
        LinearLayout lin_cancel = (LinearLayout) view.findViewById(R.id.lin_cancle);
        lin_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DeviceListActivity.this, LoginActivity.class));
                pw.dismiss();
            }
        });
        lin_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pw.dismiss();
            }
        });
    }


    //点击两次退出
    private long firstTime = 0;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                long secondTime = System.currentTimeMillis();
                if (secondTime - firstTime > 2000) { //如果两次按键时间间隔大于2秒，则不退出
                    Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
                    firstTime = secondTime;//更新firstTime
                    return true;
                } else { //两次按键小于2秒时，退出应用

                    System.exit(0);

                }
                break;
        }
        return super.onKeyUp(keyCode, event);
    }
}
