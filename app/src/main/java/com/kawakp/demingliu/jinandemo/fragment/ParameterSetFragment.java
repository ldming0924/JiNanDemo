package com.kawakp.demingliu.jinandemo.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ProviderInfo;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kawakp.demingliu.jinandemo.R;
import com.kawakp.demingliu.jinandemo.adapter.ExpandableListViewAdapter;
import com.kawakp.demingliu.jinandemo.bean.Bean;
import com.kawakp.demingliu.jinandemo.bean.ChildInfo;
import com.kawakp.demingliu.jinandemo.bean.DataDisplayActBean;
import com.kawakp.demingliu.jinandemo.bean.MyElementBean;
import com.kawakp.demingliu.jinandemo.constant.Config;
import com.kawakp.demingliu.jinandemo.listener.IOnNetResultListener;
import com.kawakp.demingliu.jinandemo.net.NetController;
import com.kawakp.demingliu.jinandemo.utils.IToast;
import com.kawakp.demingliu.jinandemo.utils.Path;
import com.kawakp.demingliu.jinandemo.utils.PopUtils;
import com.kawakp.demingliu.jinandemo.utils.SharedPerferenceHelper;
import com.kawakp.demingliu.jinandemo.widget.CustomEmptyView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Created by deming.liu on 2016/7/5.
 */
public class ParameterSetFragment extends BaseFragment implements IOnNetResultListener {
    private ExpandableListView eLvParameter;
    private String cookie;
    private String modelID;
    private String url;
    private String jsonString;
    private List<Bean> totallist = new ArrayList<Bean>();
    private PopupWindow pw;
    private List<DataDisplayActBean> totallist_data = new ArrayList<DataDisplayActBean>();
    private ExpandableListViewAdapter adapter;
    private View view;
    private ProgressDialog progressDialog;
    private ProgressDialog progressDialog1;

    private ParamsBroadcase paramsBroadcase;

    private Map<String, List<ChildInfo>> map;
    private List<String> parent;
    private boolean bb = true;

    private List<Map<String, String>> ml = new ArrayList<Map<String, String>>();
    //private Map<String,String>  m = new HashMap<>();
    private CustomEmptyView mCustomEmptyView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.parameter_setting, null);
            initView(view);
            initData();
            setListen();

            paramsBroadcase = new ParamsBroadcase();
            getActivity().registerReceiver(paramsBroadcase, new IntentFilter("com.kawakp.demingliu.jinandemo.activity.MainActivity"));
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
        eLvParameter = getView(view, R.id.e_lv_parameter);
        mCustomEmptyView = getView(view, R.id.empty_layout);
    }

    @Override
    protected void initData() {
        cookie = SharedPerferenceHelper.getCookie(getActivity());
        modelID = SharedPerferenceHelper.getDeviceModelId(getActivity());

        //获取分类
        url = Path.PARAM_LIST + "plcDataModelId=" + modelID + "&type=PARAM";
        Log.d("TAG", url);
        if (getActivity() != null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载,请稍候...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

        }


        eLvParameter.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Log.d("ParameterSetFragment", totallist.get(i).getElements().get(i1).getDisplayName());
                ShowPopupWindow(totallist.get(i).getElements().get(i1).getId());
                return false;
            }
        });
    }

    @Override
    protected void setListen() {

    }

    private void ShowPopupWindow(final String id) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_set, null);
        pw = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, false);
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
        LinearLayout lin_sure = (LinearLayout) view.findViewById(R.id.dialog_lin_sure);
        LinearLayout lin_cancel = (LinearLayout) view.findViewById(R.id.lin_cancle);
        final EditText editText = (EditText) view.findViewById(R.id.editText_param_set);

        lin_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String deviceID = SharedPerferenceHelper.getDeviceId(getActivity());
                String url = Path.DEVICE_SET + deviceID + "/elements/" + id;
                Log.d("TAG", "------------------------------" + url);
                String json = "{\"value\":" + editText.getText().toString() + "}";

                NetController netController = new NetController();
                netController.requestNet(getActivity(), url, NetController.HttpMethod.PUT, 1, ParameterSetFragment.this, cookie, json, null);


            }
        });
        lin_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pw.dismiss();
            }
        });
    }

    @Override
    public void onNetResult(int flag, String jsonResult) {

        jsonString = jsonResult;
    }

    @Override
    public void onNetComplete(int flag) {
        if (jsonString != null) {
            switch (flag) {
                case Config.FLAG_ZERO:
                    Log.d("FLAG_ZERO", jsonString);

                    JSONArray jsonArray = JSON.parseArray(jsonString);
                    List<Bean> list = JSON.parseArray(jsonArray.toString(), Bean.class);
                    totallist.clear();
                    totallist.addAll(list);
                    map = new HashMap<>();
                    parent = new ArrayList<>();
                    getParentChildDatas();


                    eLvParameter.setGroupIndicator(null);
                    adapter = new ExpandableListViewAdapter(map, parent, getActivity());
                    eLvParameter.setAdapter(adapter);
                    bb = false;
                    //展开每一item
                    openItem();
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }


                    break;
                case Config.FLAG_ONE:
                   // Log.d("ParameterSetFragment","参数设定返回=="+jsonString);
                    if (jsonString.contains("'error'")){
                        IToast.showToast(getActivity(),"设置失败");

                    }else {
                        try {
                            org.json.JSONObject object = new org.json.JSONObject(jsonString);
                            IToast.showToast(getActivity(),object.getString("success"));
                            pw.dismiss();
                            if (getActivity() != null) {
                                progressDialog1 = new ProgressDialog(getActivity());
                                progressDialog1.setMessage("请稍候...");
                                progressDialog1.setCanceledOnTouchOutside(false);
                                progressDialog1.show();
                                Handler handler = new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        //重新请求一下数据
                                      /*  NetController netController1 = new NetController();
                                        if (getActivity() != null) {
                                            netController1.requestNet(getActivity(), Path.REALTIME_DATA_PATH, NetController.HttpMethod.GET, Config.FLAG_TWO, ParameterSetFragment.this, cookie, null, null);
                                        }*/
                                        progressDialog1.dismiss();
                                    }
                                }, 8000);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case Config.FLAG_TWO:
                    JSONObject object = JSON.parseObject(jsonString);
                    JSONArray array = object.getJSONArray("list");
                    Log.d("FLAG_TWO", array.toString());
                    List<DataDisplayActBean> list1 = JSON.parseArray(array.toString(), DataDisplayActBean.class);
                    totallist_data.clear();
                    totallist_data.addAll(list1);
                    if (totallist_data.size() > 0) {
                        // 获取分类信息
                        NetController netController = new NetController();
                        if (getActivity() != null) {
                            netController.requestNet(getActivity(), url, NetController.HttpMethod.GET, Config.FLAG_ZERO, ParameterSetFragment.this, cookie, null, null);
                        }
                    }
                    break;


            }


        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(paramsBroadcase);
        Log.d("TAG", "-----------------------------");
    }

    public class ParamsBroadcase extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.kawakp.demingliu.jinandemo.activity.MainActivity")) {
                //  TODO: 2016/10/14  获取实时数据的element 中的filename
                List<MyElementBean> elist = getKeyDatas(intent);

                getValueDatas(intent, elist);

                if (ml.size() > 0) {
                    hideEmptyView();
                    if (isAdded()) {
                        if (bb) {
                            // 获取分类信息
                            if (isAdded()) {
                                NetController netController = new NetController();
                                if (getActivity() != null) {
                                    netController.requestNet(getActivity(), url, NetController.HttpMethod.GET, Config.FLAG_ZERO, ParameterSetFragment.this, cookie, null, null);
                                }
                            }
                        } else {
                            getParentChildDatas();
                            //展开每一item
                            openItem();
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

    /**
     * 张开expandableListView的每一项
     */
    private void openItem() {
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            eLvParameter.expandGroup(i);
        }
    }

    private void getValueDatas(Intent intent, List<MyElementBean> elist) {
        try {
            // TODO: 2016/10/14 获取实时数据，通过filenam得到key
            org.json.JSONObject object = new org.json.JSONObject(intent.getStringExtra("MESSAGE"));
            org.json.JSONArray array = object.getJSONArray("list");
            // m.clear();
            ml.clear();
            for (int i = 0; i < array.length(); i++) {
                org.json.JSONObject o = array.getJSONObject(i);
                for (int j = 0; j < elist.size(); j++) {
                    Map<String, String> m = new HashMap<>();
                    if (o.opt(elist.get(j).getFieldName()) != null) {
                        m.put(elist.get(j).getFieldName(), o.opt(elist.get(j).getFieldName()) + "");
                        ml.add(m);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取key
     * @param intent
     * @return
     */
    @Nullable
    private List<MyElementBean> getKeyDatas(Intent intent) {
        JSONObject fo = JSON.parseObject(intent.getStringExtra("MESSAGE"));
        JSONObject fobj = fo.getJSONObject("elementTable");
        JSONArray fa = fobj.getJSONArray("elements");
        return JSON.parseArray(fa.toString(), MyElementBean.class);
    }

    /**
     * 设置parent与child的值
     */
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

    private void initEmptyView() {
        mCustomEmptyView.setEmptyImage(R.drawable.ic_launcher);
        mCustomEmptyView.setEmptyText("暂无数据");
        mCustomEmptyView.hideReloadButton();

    }

    public void hideEmptyView() {
        mCustomEmptyView.setVisibility(View.GONE);
    }
}
