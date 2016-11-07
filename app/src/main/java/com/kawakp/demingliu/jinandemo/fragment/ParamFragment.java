package com.kawakp.demingliu.jinandemo.fragment;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.kawakp.demingliu.jinandemo.R;
import com.kawakp.demingliu.jinandemo.bean.DataDisplayActBean;
import com.kawakp.demingliu.jinandemo.constant.Config;
import com.kawakp.demingliu.jinandemo.listener.IOnNetResultListener;
import com.kawakp.demingliu.jinandemo.net.NetController;
import com.kawakp.demingliu.jinandemo.utils.IToast;
import com.kawakp.demingliu.jinandemo.utils.Path;
import com.kawakp.demingliu.jinandemo.utils.PopUtils;
import com.kawakp.demingliu.jinandemo.utils.SharedPerferenceHelper;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deming.liu on 2016/9/21.
 */
public class ParamFragment extends BaseFragment implements View.OnClickListener ,IOnNetResultListener{
    private View view;
    private ParamsBroadcase paramsBroadcase;
    private List<DataDisplayActBean> totallist1 = new ArrayList<DataDisplayActBean>();
    private TextView t1,t2,t3,t4,t5,t6,t7,t8,t9,t10,t11;
    private String cookie;
    private PopupWindow pw;
    private String jsonString;
    private ProgressDialog progressDialog1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.paramfragment, null);
            initView(view);
            initData();
            setListen();

            paramsBroadcase = new ParamsBroadcase();
            getActivity().registerReceiver(paramsBroadcase, new IntentFilter("com.kawakp.demingliu.jinandemo.activity.MainActivity"));
        }else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        return view;
    }

    @Override
    protected void initView(View view) {
        t1 = getView(view,R.id.t1);
        t2 = getView(view,R.id.t2);
        t3 = getView(view,R.id.t3);
        t4 = getView(view,R.id.t4);
        t5 = getView(view,R.id.t5);
        t6 = getView(view,R.id.t6);
        t7 = getView(view,R.id.t7);
        t8 = getView(view,R.id.t8);
        t9 = getView(view,R.id.t9);
        t10 = getView(view,R.id.t10);
        t11 = getView(view,R.id.t11);
    }

    @Override
    protected void initData() {
        cookie = SharedPerferenceHelper.getCookie(getActivity());
    }

    @Override
    protected void setListen() {
        t1.setOnClickListener(this);
        t2.setOnClickListener(this);
        t3.setOnClickListener(this);
        t4.setOnClickListener(this);
        t5.setOnClickListener(this);
        t6.setOnClickListener(this);
        t7.setOnClickListener(this);
        t8.setOnClickListener(this);
        t9.setOnClickListener(this);
        t10.setOnClickListener(this);
        t11.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.t1:
                ShowPopupWindow("gxznhrjz_e_15");
                break;
            case R.id.t2:
                ShowPopupWindow("gxznhrjz_e_16");
                break;
            case R.id.t3:
                ShowPopupWindow("gxznhrjz_e_17");
                break;
            case R.id.t4:
                ShowPopupWindow("gxznhrjz_e_18");
                break;
            case R.id.t5:
                ShowPopupWindow("gxznhrjz_e_29");
                break;
            case R.id.t6:
                ShowPopupWindow("gxznhrjz_e_30");
                break;
            case R.id.t7:
                ShowPopupWindow("gxznhrjz_e_32");
                break;
            case R.id.t8:
                ShowPopupWindow("gxznhrjz_e_34");
                break;
            case R.id.t9:
                ShowPopupWindow("gxznhrjz_e_31");
                break;
            case R.id.t10:
                ShowPopupWindow("gxznhrjz_e_33");
                break;
            case R.id.t11:
                ShowPopupWindow("gxznhrjz_e_35");
                break;
        }
    }

    private void ShowPopupWindow(final String id){
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_set,null);
        pw = new PopupWindow(view, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT,false);
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
                String url = Path.DEVICE_SET+deviceID+"/elements/"+id;
                String json = "{\"value\":"+ editText.getText().toString()+"}";
                Log.d("ParameterSetFragment",url+json);
                NetController netController = new NetController();
                netController.requestNet(getActivity(),url, NetController.HttpMethod.PUT,1,ParamFragment.this,cookie,json,null);


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
        if (jsonString != null){
            switch (flag){
                case 1:
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
                                       /* NetController netController1 = new NetController();
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
            }
        }
    }

    public class ParamsBroadcase extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("com.kawakp.demingliu.jinandemo.activity.MainActivity")) {
                //获取开关状态
                JSONObject object = JSON.parseObject(intent.getStringExtra("MESSAGE"));
                JSONArray array = object.getJSONArray("list");
                List<DataDisplayActBean> list = JSON.parseArray(array.toString(), DataDisplayActBean.class);
                if (isAdded()) {
                    totallist1.clear();
                    totallist1.addAll(list);
                    if (totallist1.size()>0 && totallist1 != null) {
                        /*t1.setText(totallist1.get(0).getD1295()+"℃");
                        t2.setText(totallist1.get(0).getD1399()+"Mpa");
                        t3.setText(totallist1.get(0).getD1409()+"Mpa");
                        t4.setText(totallist1.get(0).getD1411()+"Mpa");
                        t5.setText(totallist1.get(0).getD1279()+"℃");
                        t6.setText(totallist1.get(0).getD1281()+"Mpa");
                        t7.setText(totallist1.get(0).getD1285()+"Mpa");
                        t8.setText(totallist1.get(0).getD1289()+"m");
                        t9.setText(totallist1.get(0).getD1283()+"Mpa");
                        t10.setText(totallist1.get(0).getD1287()+"Mpa");
                        t11.setText(totallist1.get(0).getD1277()+"m");*/
                    }
                }
            }

        }
    }
}
