package com.kawakp.demingliu.jinandemo.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kawakp.demingliu.jinandemo.R;
import com.kawakp.demingliu.jinandemo.adapter.ExpandableListViewAdapter;
import com.kawakp.demingliu.jinandemo.adapter.RealTimeExpandableAdapter;
import com.kawakp.demingliu.jinandemo.bean.Bean;
import com.kawakp.demingliu.jinandemo.bean.ChildInfo;
import com.kawakp.demingliu.jinandemo.bean.DataDisplayActBean;
import com.kawakp.demingliu.jinandemo.bean.MyElementBean;
import com.kawakp.demingliu.jinandemo.constant.Config;
import com.kawakp.demingliu.jinandemo.http.OkHttpHelper;
import com.kawakp.demingliu.jinandemo.http.SimpleCallback;
import com.kawakp.demingliu.jinandemo.http.SpotsCallBack;
import com.kawakp.demingliu.jinandemo.listener.IOnNetResultListener;
import com.kawakp.demingliu.jinandemo.net.NetController;
import com.kawakp.demingliu.jinandemo.utils.DrawChart;
import com.kawakp.demingliu.jinandemo.utils.DrawChart1;
import com.kawakp.demingliu.jinandemo.utils.DrawChart2;
import com.kawakp.demingliu.jinandemo.utils.DrawChart3;
import com.kawakp.demingliu.jinandemo.utils.HttpUtils;
import com.kawakp.demingliu.jinandemo.utils.Path;
import com.kawakp.demingliu.jinandemo.utils.PopUtils;
import com.kawakp.demingliu.jinandemo.utils.SharedPerferenceHelper;
import com.kawakp.demingliu.jinandemo.widget.CustomEmptyView;


import org.json.JSONException;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import okhttp3.Response;


/**
 * Created by deming.liu on 2016/7/5.
 */
public class RealTimeDataFragment extends BaseFragment {
    private boolean bb = true;
    private DrawChart chart;
    private View view;
    private String url;
    private ExpandableListView eLvParameter;
    private String modelID;
    private RealTimeBroadCase realTimeBroadCase;
    private List<Bean> totallist = new ArrayList<Bean>();  //分类
    private List<DataDisplayActBean> real_totallist = new ArrayList<>();//实时数据
    private Map<String, List<ChildInfo>> map;
    private List<String> parent;

    private RealTimeExpandableAdapter adapter;

    private ProgressDialog progressDialog;
    private List<Map<String, String>> ml = new ArrayList<Map<String, String>>();
    private CustomEmptyView mCustomEmptyView;

    private OkHttpHelper okHttpHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {

            view = inflater.inflate(R.layout.fragment_real, null);
            initView(view);
            initData();
            setListen();
            realTimeBroadCase = new RealTimeBroadCase();
            getActivity().registerReceiver(realTimeBroadCase, new IntentFilter("com.kawakp.demingliu.jinandemo.activity.MainActivity"));
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        return view;
    }

    @Override
    protected void initView(View view) {

        eLvParameter = getView(view, R.id.rel_e_lv_parameter);
        mCustomEmptyView = getView(view, R.id.empty_layout);

        okHttpHelper = OkHttpHelper.getInstance(getContext());
    }

    @Override
    protected void initData() {
        modelID = SharedPerferenceHelper.getDeviceModelId(getActivity());
        url = Path.PARAM_LIST + "plcDataModelId=" + modelID + "&type=MONITOR";
        Log.d("TAG", url);
        if (getActivity() != null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载,请稍候...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void setListen() {
        eLvParameter.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Log.d("REAL", map.get(parent.get(i)).get(i1).getValue() + map.get(parent.get(i)).get(i1).getName());
                //能否获取到key
                if (parent.get(i).equals("温度参数")) {
                    showPopWindow(map.get(parent.get(i)).get(i1).getName(), i, i1);
                } else if (parent.get(i).equals("压力参数")) {
                    showPopWindow2(map.get(parent.get(i)).get(i1).getName(), i, i1);
                } else if (parent.get(i).equals("液位参数")) {
                    showPopWindow3(map.get(parent.get(i)).get(i1).getName(), i, i1);
                } else {
                    showPopWindow4(map.get(parent.get(i)).get(i1).getName(), i, i1);
                }
                return false;
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(realTimeBroadCase);

    }

    @Override
    public void onPause() {
        super.onPause();

        getActivity().registerReceiver(realTimeBroadCase, new IntentFilter("com.kawakp.demingliu.jinandemo.activity.MainActivity"));
    }

    private void showPopWindow(String s, final int parentID, final int childID) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.chart_popuwindow, null);
        final PopupWindow pw = new PopupWindow(view,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, false);
        pw.setFocusable(true);
        pw.setOutsideTouchable(false);
        pw.setBackgroundDrawable(new BitmapDrawable());
        pw.setAnimationStyle(R.style.myanimation);
        pw.showAtLocation(view, Gravity.CENTER, 0, 0);

        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                PopUtils.setBackgroundAlpha(1.0f, getActivity());//设置Popw消失背景为透明
            }
        });
        PopUtils.setBackgroundAlpha(0.5f, getActivity());//设置popw出现时背景透明度
        chart = (DrawChart) view.findViewById(R.id.chart);

        TextView title = (TextView) view.findViewById(R.id.textView_pop_title);
        title.setText(s);
        ImageView img_cancle = (ImageView) view.findViewById(R.id.imageView_pop_cancel);

        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (map.size() > 0 && parent.size() > 0) {
                    chart.setY(Double.parseDouble(map.get(parent.get(parentID)).get(childID).getValue()));
                } else {
                    chart.setY(0);
                }
            }
        };
        timer.schedule(timerTask, 0, 1000);
        img_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                pw.dismiss();
            }
        });
    }

    private void showPopWindow2(String s, final int parentID, final int childID) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.chart_p2, null);
        final PopupWindow pw = new PopupWindow(view,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, false);
        pw.setFocusable(true);
        pw.setOutsideTouchable(false);
        pw.setBackgroundDrawable(new BitmapDrawable());
        pw.setAnimationStyle(R.style.myanimation);
        pw.showAtLocation(view, Gravity.CENTER, 0, 0);
        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                PopUtils.setBackgroundAlpha(1.0f, getActivity());//设置Popw消失背景为透明
            }
        });
        PopUtils.setBackgroundAlpha(0.5f, getActivity());//设置popw出现时背景透明度
        final DrawChart1 chart = (DrawChart1) view.findViewById(R.id.chart);

        TextView title = (TextView) view.findViewById(R.id.textView_pop_title);
        title.setText(s);
        ImageView img_cancle = (ImageView) view.findViewById(R.id.imageView_pop_cancel);

        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (map.size() > 0 && parent.size() > 0) {
                    chart.setY(Double.parseDouble(map.get(parent.get(parentID)).get(childID).getValue()));
                } else {
                    chart.setY((double) 0);
                }
            }
        };
        timer.schedule(timerTask, 0, 1000);
        img_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                pw.dismiss();
            }
        });
    }

    private void showPopWindow3(String s, final int parentID, final int childID) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.chart_p3, null);
        final PopupWindow pw = new PopupWindow(view,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, false);
        pw.setFocusable(true);
        pw.setOutsideTouchable(false);
        pw.setBackgroundDrawable(new BitmapDrawable());
        pw.setAnimationStyle(R.style.myanimation);
        pw.showAtLocation(view, Gravity.CENTER, 0, 0);
        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                PopUtils.setBackgroundAlpha(1.0f, getActivity());//设置Popw消失背景为透明
            }
        });
        PopUtils.setBackgroundAlpha(0.5f, getActivity());//设置popw出现时背景透明度
        final DrawChart2 chart = (DrawChart2) view.findViewById(R.id.chart);

        TextView title = (TextView) view.findViewById(R.id.textView_pop_title);
        title.setText(s);
        ImageView img_cancle = (ImageView) view.findViewById(R.id.imageView_pop_cancel);

        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (map.size() > 0 && parent.size() > 0) {
                    chart.setY(Double.parseDouble(map.get(parent.get(parentID)).get(childID).getValue()));

                } else {
                    chart.setY((double) 0);
                }
            }
        };
        timer.schedule(timerTask, 0, 1000);
        img_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                pw.dismiss();
            }
        });
    }

    private void showPopWindow4(String s, final int parentID, final int childID) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.chart_p4, null);
        final PopupWindow pw = new PopupWindow(view,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT, false);
        pw.setFocusable(true);
        pw.setOutsideTouchable(false);
        pw.setBackgroundDrawable(new BitmapDrawable());
        pw.setAnimationStyle(R.style.myanimation);
        pw.showAtLocation(view, Gravity.CENTER, 0, 0);
        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                PopUtils.setBackgroundAlpha(1.0f, getActivity());//设置Popw消失背景为透明
            }
        });
        PopUtils.setBackgroundAlpha(0.5f, getActivity());//设置popw出现时背景透明度
        final DrawChart3 chart = (DrawChart3) view.findViewById(R.id.chart);

        TextView title = (TextView) view.findViewById(R.id.textView_pop_title);
        title.setText(s);
        ImageView img_cancle = (ImageView) view.findViewById(R.id.imageView_pop_cancel);

        final Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                if (map.size() > 0 && parent.size() > 0) {
                    chart.setY(Double.parseDouble(map.get(parent.get(parentID)).get(childID).getValue()) + 30);
                } else {
                    chart.setY((double) 0);
                }
            }
        };
        timer.schedule(timerTask, 0, 1000);
        img_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timer.cancel();
                pw.dismiss();

            }
        });
    }


    private class RealTimeBroadCase extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.kawakp.demingliu.jinandemo.activity.MainActivity")) {
                //   获取key
                List<MyElementBean> elist = getKeyDatas(intent);
                //  通过key，获取值
                getValueDatas(intent, elist);

                if (ml.size() > 0) {
                    hideEmptyView();
                    if (isAdded()) {
                        if (bb) {
                            //是否是第一次请求，是就请求分类信息，不是就不请求

                            if (getActivity() != null) {
                                progressDialog.dismiss();
                                okHttpHelper.get(url, new SimpleCallback<String>(getActivity()) {

                                    @Override
                                    public void onSuccess(Response response, String s) {
                                        JSONArray jsonArray = JSON.parseArray(s);
                                        List<Bean> list = JSON.parseArray(jsonArray.toString(), Bean.class);
                                        totallist.clear();
                                        totallist.addAll(list);
                                        map = new HashMap<>();
                                        parent = new ArrayList<>();
                                        getParentChildDatas();
                                        eLvParameter.setGroupIndicator(null);
                                        adapter = new RealTimeExpandableAdapter(map, parent, getActivity());
                                        eLvParameter.setAdapter(adapter);
                                        bb = false;
                                        //展开每一item
                                        for (int i = 0; i < adapter.getGroupCount(); i++) {
                                            eLvParameter.expandGroup(i);
                                        }
                                    }

                                    @Override
                                    public void onError(Response response, int code, Exception e) {

                                    }
                                });
                            }
                        } else {
                            getParentChildDatas();
                            //展开每一item
                            for (int i = 0; i < adapter.getGroupCount(); i++) {
                                eLvParameter.expandGroup(i);
                            }
                            //更新数据
                            adapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    progressDialog.dismiss();
                    initEmptyView();

                }

            }
        }


    }

    private void initEmptyView() {
        mCustomEmptyView.setEmptyImage(R.drawable.ic_launcher);
        mCustomEmptyView.setEmptyText("暂无数据");
        mCustomEmptyView.hideReloadButton();

    }

    public void hideEmptyView() {
        mCustomEmptyView.setVisibility(View.GONE);
    }

    private List<MyElementBean> getKeyDatas(Intent intent) {
        //  TODO: 2016/10/14  获取实时数据的element 中的filename
        JSONObject fo = JSON.parseObject(intent.getStringExtra("MESSAGE"));
        JSONObject fobj = fo.getJSONObject("elementTable");
        JSONArray fa = fobj.getJSONArray("elements");
        return JSON.parseArray(fa.toString(), MyElementBean.class);
    }

    private void getValueDatas(Intent intent, List<MyElementBean> elist) {
        try {
            // TODO: 2016/10/14 获取实时数据，通过filenam得到key
            org.json.JSONObject object = new org.json.JSONObject(intent.getStringExtra("MESSAGE"));
            org.json.JSONArray array = object.getJSONArray("list");
            ml.clear();
            for (int i = 0; i < array.length(); i++) {
                org.json.JSONObject o = array.getJSONObject(i);
                for (int j = 0; j < elist.size(); j++) {
                    Map<String, String> m = new HashMap<String, String>();
                    if (o.opt(elist.get(j).getFieldName()) != null) {
                        m.put(elist.get(j).getFieldName(), o.opt(elist.get(j).getFieldName()) + "");
                        ml.add(m);
                    }


                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("TAG", e.toString());
        }
    }

    private void getParentChildDatas() {
        map.clear();
        parent.clear();
        for (int i = 0; i < totallist.size(); i++) {
            parent.add(totallist.get(i).getName());
            List<ChildInfo> childInfos = new ArrayList<>();
            int size = totallist.get(i).getElements().size();
            for (int j = 0; j < size; j++) {
                String displayName = totallist.get(i).getElements().get(j).getDisplayName();
                String address = totallist.get(i).getElements().get(j).getDefaultAddress();

                for (int k = 0; k < ml.size(); k++) {
                    Set set = ml.get(k).entrySet();
                    for (Iterator iter = set.iterator(); iter.hasNext(); ) {
                        Map.Entry entry = (Map.Entry) iter.next();
                        String key = (String) entry.getKey();
                        if (totallist.get(i).getElements().get(j).getFieldName().equals(key)) {
                            ChildInfo childInfo = new ChildInfo(displayName, ml.get(k).get(key), totallist.get(i).getElements().get(j).getUnit());
                            childInfos.add(childInfo);
                        }
                    }

                }
            }
            map.put(parent.get(i), childInfos);
        }
    }
}
