package com.kawakp.demingliu.jinandemo.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;

import com.kawakp.demingliu.jinandemo.R;
import com.kawakp.demingliu.jinandemo.bean.ChildInfo;
import com.kawakp.demingliu.jinandemo.bean.ControlChildInfo;
import com.kawakp.demingliu.jinandemo.constant.Config;
import com.kawakp.demingliu.jinandemo.listener.IOnNetResultListener;
import com.kawakp.demingliu.jinandemo.net.NetController;
import com.kawakp.demingliu.jinandemo.utils.IToast;
import com.kawakp.demingliu.jinandemo.utils.Path;
import com.kawakp.demingliu.jinandemo.utils.SharedPerferenceHelper;

import org.json.JSONException;

import java.util.List;
import java.util.Map;

/**
 * Created by deming.liu on 2016/10/15.
 */
public class ControlAdapter extends BaseExpandableListAdapter implements IOnNetResultListener {
    private Map<String, List<ControlChildInfo>> map;
    private List<String> parent;
    private Context context;
    private String deviceID;
    private String cookie;

    public ControlAdapter(Map<String, List<ControlChildInfo>> map, List<String> parent, Context context) {
        this.context = context;
        this.map = map;
        this.parent = parent;
        deviceID = SharedPerferenceHelper.getDeviceId(context);
        cookie = SharedPerferenceHelper.getCookie(context);
    }


    @Override
    public int getGroupCount() {
        return parent.size();
    }

    @Override
    public int getChildrenCount(int i) {
        String key = parent.get(i);
        int size = map.get(key).size();
        return size;
    }

    @Override
    public Object getGroup(int i) {
        return parent.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        String key = parent.get(i);
        return (map.get(key).get(i1));
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public int getChildType(int i, int i1) {
        String key = this.parent.get(i);
        ControlChildInfo info = map.get(key).get(i1);
        if (info.getAddress().equals("XH_MA")) {
            return 0;
        } else if (info.getAddress().equals("BS_MA")) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public int getChildTypeCount() {
// TODO Auto-generated method stub
        return 3;
    }

    @Override
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.parameter_parent, viewGroup, false);
        }
        TextView tv = (TextView) view
                .findViewById(R.id.tv_parameter_parent);
        tv.setText(this.parent.get(i));
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        String key = this.parent.get(i);
        final ControlChildInfo info = map.get(key).get(i1);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int type = getChildType(i, i1);
        switch (type) {
            case 0:
                if (view == null) {
                    view = inflater.inflate(R.layout.control_adapter_child_item, viewGroup, false);
                    RadioButton rb_manul = (RadioButton) view.findViewById(R.id.loop_manul);
                    RadioButton rb_auto = (RadioButton) view.findViewById(R.id.loop_auto);
                    if (info.getValue().equals("0")) {
                        rb_manul.setChecked(false);
                        rb_auto.setChecked(true);
                    } else {
                        rb_manul.setChecked(true);
                        rb_auto.setChecked(false);
                    }
                    rb_manul.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String url_manul = Path.DEVICE_SET + deviceID + "/elements/" + info.getId();
                            String json_manul = "{\"value\":" + 1 + "}";
                            NetController netController_manul = new NetController();
                            netController_manul.requestNet(context, url_manul, NetController.HttpMethod.PUT, Config.FLAG_ONE, ControlAdapter.this, cookie, json_manul, null);
                            Log.d("ada", url_manul + "　" + json_manul);
                        }
                    });

                    rb_auto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String url_auto = Path.DEVICE_SET + deviceID + "/elements/" + info.getId();
                            String json_auto = "{\"value\":" + 0 + "}";
                            NetController netController_auto = new NetController();
                            netController_auto.requestNet(context, url_auto, NetController.HttpMethod.PUT, Config.FLAG_ONE, ControlAdapter.this, cookie, json_auto, null);
                            Log.d("ada", url_auto + "　" + json_auto);
                        }
                    });

                }
                break;
            case 1:
                if (view == null) {
                    view = inflater.inflate(R.layout.control_adapter_child_item, viewGroup, false);
                    RadioButton rb_manul = (RadioButton) view.findViewById(R.id.loop_manul);
                    RadioButton rb_auto = (RadioButton) view.findViewById(R.id.loop_auto);
                    if (info.getValue().equals("0")) {
                        rb_manul.setChecked(false);
                        rb_auto.setChecked(true);
                    } else {
                        rb_manul.setChecked(true);
                        rb_auto.setChecked(false);
                    }
                    rb_manul.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String url_manul = Path.DEVICE_SET + deviceID + "/elements/" + info.getId();
                            String json_manul = "{\"value\":" + 1 + "}";
                            NetController netController_manul = new NetController();
                            netController_manul.requestNet(context, url_manul, NetController.HttpMethod.PUT, Config.FLAG_ONE, ControlAdapter.this, cookie, json_manul, null);
                            Log.d("ada", url_manul + "　" + json_manul);
                        }
                    });

                    rb_auto.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String url_auto = Path.DEVICE_SET + deviceID + "/elements/" + info.getId();
                            String json_auto = "{\"value\":" + 0 + "}";
                            NetController netController_auto = new NetController();
                            netController_auto.requestNet(context, url_auto, NetController.HttpMethod.PUT, Config.FLAG_ONE, ControlAdapter.this, cookie, json_auto, null);
                            Log.d("ada", url_auto + "　" + json_auto);
                        }
                    });

                }
                break;
            case 2:

                if (view == null) {
                    view = inflater.inflate(R.layout.control_adapter_item2, viewGroup, false);
                }
                TextView name = (TextView) view
                        .findViewById(R.id.tv_parameter_childName);
                final CheckBox checkBox = (CheckBox) view.findViewById(R.id.csb1);
                if (info.getValue().equals("0")) {
                    checkBox.setChecked(false);
                } else {
                    checkBox.setChecked(true);
                }

                checkBox.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (checkBox.isChecked()) {  //点击后的状态
                            String url1 = Path.DEVICE_SET + deviceID + "/elements/" + info.getId();
                            String json1 = "{\"value\":" + 1 + "}";
                            // Log.d("FLAG_TWO",url1+"-----------"+json1);
                            NetController netController1 = new NetController();
                            netController1.requestNet(context, url1, NetController.HttpMethod.PUT, Config.FLAG_ONE, ControlAdapter.this, cookie, json1, null);
                        } else {
                            String url1 = Path.DEVICE_SET + deviceID + "/elements/" + info.getId();
                            String json1 = "{\"value\":" + 0 + "}";
                            // Log.d("FLAG_TWO",url1+"-----------"+json1+"     "+csb1.isChecked());
                            checkBox.setChecked(false);
                            NetController netController1 = new NetController();
                            netController1.requestNet(context, url1, NetController.HttpMethod.PUT, Config.FLAG_ONE, ControlAdapter.this, cookie, json1, null);
                        }
                    }
                });


                name.setText(info.getName().toString());

                break;

        }
        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }


    private String jsonString;
    private ProgressDialog progressDialog2;

    @Override
    public void onNetResult(int flag, String jsonResult) {
        jsonString = jsonResult;
    }

    @Override
    public void onNetComplete(int flag) {
        Log.d("ada", jsonString);
        if (jsonString.contains("'error'")) {
            IToast.showToast(context, "设置失败");

        } else {
            try {
                org.json.JSONObject object1 = new org.json.JSONObject(jsonString);
                IToast.showToast(context, object1.getString("success"));
                //重新请求一下数据
                if (context != null) {
                    progressDialog2 = new ProgressDialog(context);
                    progressDialog2.setMessage("请稍候...");
                    progressDialog2.setCanceledOnTouchOutside(false);
                    progressDialog2.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                   /* //传递一个告诉成功
                        if (callBack != null){
                            callBack.callback(true);
                        }*/
                            progressDialog2.dismiss();
                        }
                    }, 8000);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /*private MyCallBack callBack;



    public void setCallBack(MyCallBack callBack) {
        this.callBack = callBack;
    }

    private interface MyCallBack{
        void callback(boolean flag);
    }*/
}
