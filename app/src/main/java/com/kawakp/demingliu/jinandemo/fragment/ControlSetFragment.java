package com.kawakp.demingliu.jinandemo.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kawakp.demingliu.jinandemo.R;
import com.kawakp.demingliu.jinandemo.activity.AnimotionActivity;
import com.kawakp.demingliu.jinandemo.activity.MainActivity;
import com.kawakp.demingliu.jinandemo.adapter.ControlAdapter;
import com.kawakp.demingliu.jinandemo.adapter.ExpandableListViewAdapter;
import com.kawakp.demingliu.jinandemo.bean.Bean;
import com.kawakp.demingliu.jinandemo.bean.ChildInfo;
import com.kawakp.demingliu.jinandemo.bean.ControlChildInfo;
import com.kawakp.demingliu.jinandemo.bean.DataDisplayActBean;
import com.kawakp.demingliu.jinandemo.bean.MyElementBean;
import com.kawakp.demingliu.jinandemo.constant.Config;
import com.kawakp.demingliu.jinandemo.constant.MyConstant;
import com.kawakp.demingliu.jinandemo.http.OkHttpHelper;
import com.kawakp.demingliu.jinandemo.http.SimpleCallback;
import com.kawakp.demingliu.jinandemo.listener.IOnNetResultListener;
import com.kawakp.demingliu.jinandemo.net.NetController;
import com.kawakp.demingliu.jinandemo.utils.IToast;
import com.kawakp.demingliu.jinandemo.utils.Path;
import com.kawakp.demingliu.jinandemo.utils.SharedPerferenceHelper;
import com.kawakp.demingliu.jinandemo.widget.CheckSwitchButton;
import com.kawakp.demingliu.jinandemo.widget.CustomEmptyView;
import com.kawakp.demingliu.jinandemo.widget.CustomExpandableListView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.Bind;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import okhttp3.Response;


/**
 * Created by deming.liu on 2016/7/5.
 */
public class ControlSetFragment extends BaseFragment  {
    private OkHttpHelper okHttpHelper;
    private String modelID;
    private String url;
    private String deviceID;
    @Bind(R.id.t1)
    TextView t1;
    @Bind(R.id.t2)
    TextView t2;
    @Bind(R.id.t3)
    TextView t3;
    @Bind(R.id.t4)
    TextView t4;
    @Bind(R.id.customExpandableListView)
    CustomExpandableListView customExpandableListView;
    @Bind(R.id.empty_layout)
    CustomEmptyView mCustomEmptyView;
    private View view;
    private SpotsDialog mDialog;
    private ControlBroadcast controlBroadcast;
    private boolean b = true;
    private  List<Bean> totallist = new ArrayList<Bean>();
    private Map<String, List<ControlChildInfo>> map;
    private List<String> parent;
    private List<Map<String,String>> ml = new ArrayList<Map<String,String>>();
    private boolean bb = true;
    private ControlAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.datarecord_fragment, null);
            ButterKnife.bind(this,view);
            initView(view);
            initData();
            setListen();
            controlBroadcast = new ControlBroadcast();
            getActivity().registerReceiver(controlBroadcast, new IntentFilter("com.kawakp.demingliu.jinandemo.activity.MainActivity"));
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

        okHttpHelper = OkHttpHelper.getInstance(getActivity());
        mDialog = new SpotsDialog(getActivity(),"拼命加载中...");
        mDialog.show();
    }

    @Override
    protected void initData() {

        modelID = SharedPerferenceHelper.getDeviceModelId(getActivity());
        deviceID = SharedPerferenceHelper.getDeviceId(getActivity());

        //获取分类
        url = Path.PARAM_LIST + "plcDataModelId=" + modelID + "&type=CONTROL";
        Log.d("CC",url);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected void setListen() {

    }

    public class ControlBroadcast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(MyConstant.MAINACTIVITY)) {

                //  TODO: 2016/10/14  获取实时数据的element 中的filename
                JSONObject fo = JSON.parseObject(intent.getStringExtra("MESSAGE"));
                JSONObject fobj = fo.getJSONObject("elementTable");
                JSONArray fa = fobj.getJSONArray("elements");
                List<MyElementBean> elist = JSON.parseArray(fa.toString(),MyElementBean.class);

                try {
                    // TODO: 2016/10/14 获取实时数据，通过filenam得到key
                    org.json.JSONObject object = new org.json.JSONObject(intent.getStringExtra("MESSAGE"));
                    org.json.JSONArray array = object.getJSONArray("list");

                    // m.clear();
                    ml.clear();
                    for (int i=0;i<array.length();i++){
                        org.json.JSONObject o = array.getJSONObject(i);
                        for (int j = 0;j<elist.size();j++) {
                            Map<String,String>  m = new HashMap<>();
                            if (o.opt(elist.get(j).getFieldName()) != null) {
                                m.put(elist.get(j).getFieldName(), o.opt(elist.get(j).getFieldName())+"");
                                ml.add(m);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (ml.size()>0) {
                    if (isAdded()) {
                        hideEmptyView();
                        if (bb) {
                            // 获取分类信息
                            if (isAdded()) {

                                if (getActivity() != null) {
                                   // netController.requestNet(getActivity(), url, NetController.HttpMethod.GET, Config.FLAG_ZERO, ControlSetFragment.this, cookie, null, null);
                                    okHttpHelper.get(url, new SimpleCallback<String>(getActivity()) {

                                        @Override
                                        public void onSuccess(Response response, String s) {
                                            JSONArray jsonArray = JSON.parseArray(s);
                                            List<Bean> list = JSON.parseArray(jsonArray.toString(),Bean.class);
                                            totallist.clear();
                                            totallist.addAll(list);
                                            map = new HashMap<>();
                                            parent = new ArrayList<>();
                                            map.clear();
                                            parent.clear();

                                            setData();

                                            customExpandableListView.setGroupIndicator(null);

                                            adapter = new ControlAdapter(map, parent, getActivity());
                                            customExpandableListView.setAdapter(adapter);
                                            bb = false;
                                            //展开每一item
                                            for (int i = 0; i < adapter.getGroupCount(); i++) {
                                                customExpandableListView.expandGroup(i);
                                            }
                                            if (mDialog != null) {
                                                mDialog.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onError(Response response, int code, Exception e) {

                                        }
                                    });
                                }
                            }
                        }else {
                            map.clear();
                            parent.clear();

                            setData();
                            //展开每一item
                            for (int i = 0; i < adapter.getGroupCount(); i++) {
                                customExpandableListView.expandGroup(i);
                            }

                            adapter.notifyDataSetChanged();
                        }
                    }
                }else {
                    initEmptyView();
                }

                setLight(intent);



            }
        }




    }


    private void initEmptyView() {
        mCustomEmptyView.setEmptyImage(R.drawable.ic_launcher);
        mCustomEmptyView.setEmptyText("暂无数据");
        mCustomEmptyView.hideReloadButton();
        // SnackbarUtil.showMessage(mRecyclerView, "数据加载失败,请重新加载或者检查网络是否链接");
            /*mCustomEmptyView.reload(new CustomEmptyView.ReloadOnClickListener()
            {

                @Override
                public void reloadClick()
                {
                    showProgressBar();
                }
            });*/
    }


    public void hideEmptyView()
    {

        mCustomEmptyView.setVisibility(View.GONE);
    }

    private void setLight(Intent intent) {
        JSONObject object = JSON.parseObject(intent.getStringExtra("MESSAGE"));
        JSONArray array = object.getJSONArray("list");
        List<DataDisplayActBean> list = JSON.parseArray(array.toString(),DataDisplayActBean.class);
        for (int i = 0;i<list.size();i++) {
            //显示灯
            if (list.get(0).getRUN_XHV1() == 0) {
                t1.setBackgroundColor(getResources().getColor(R.color.red));

            } else if (list.get(0).getRUN_XHV1() == 1) {
                t1.setBackgroundColor(getResources().getColor(R.color.blue));
            } else {
                t1.setBackgroundColor(getResources().getColor(R.color.green));
            }
            if (list.get(0).getRUN_XHV2() == 0) {
                t3.setBackgroundColor(getResources().getColor(R.color.red));

            } else if (list.get(0).getRUN_XHV2() == 1) {
                t3.setBackgroundColor(getResources().getColor(R.color.blue));
            } else {
                t3.setBackgroundColor(getResources().getColor(R.color.green));
            }
            if (list.get(0).getRUN_BSV1() == 0) {
                t2.setBackgroundColor(getResources().getColor(R.color.red));
            } else if (list.get(0).getRUN_BSV1() == 1) {
                t2.setBackgroundColor(getResources().getColor(R.color.blue));
            } else {
                t2.setBackgroundColor(getResources().getColor(R.color.green));
            }
            if (list.get(0).getRUN_BSV2() == 0) {
                t4.setBackgroundColor(getResources().getColor(R.color.red));

            } else if (list.get(0).getRUN_BSV2() == 1) {
                t4.setBackgroundColor(getResources().getColor(R.color.blue));
            } else {
                t4.setBackgroundColor(getResources().getColor(R.color.green));
            }
        }
    }



    private void setData() {
        for (int i = 0; i < totallist.size(); i++) {
            parent.add(totallist.get(i).getName());
            List<ControlChildInfo> childInfos = new ArrayList<>();
            int size = totallist.get(i).getElements().size();
            for (int j = 0; j < size; j++) {
                String displayName = totallist.get(i).getElements().get(j).getDisplayName();
                String address = totallist.get(i).getElements().get(j).getFieldName();

                for (int k=0;k<ml.size();k++) {


                    Set set = ml.get(k).entrySet();

                    for(Iterator iter = set.iterator(); iter.hasNext();)
                    {
                        Map.Entry entry = (Map.Entry)iter.next();
                        String key = (String)entry.getKey();
                        if (totallist.get(i).getElements().get(j).getFieldName().equals(key)) {
                            ControlChildInfo childInfo = new ControlChildInfo(totallist.get(i).getElements().get(j).getId(),displayName,ml.get(k).get(key),address);
                            childInfos.add(childInfo);
                        }
                    }

                }
            }
            map.put(parent.get(i), childInfos);
        }
    }
    @Override
    public void onPause() {
        super.onPause();
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(controlBroadcast);
    }
}
