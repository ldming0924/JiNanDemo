package com.kawakp.demingliu.jinandemo.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.kawakp.demingliu.jinandemo.R;
import com.kawakp.demingliu.jinandemo.bean.RangeClass;
import com.kawakp.demingliu.jinandemo.constant.Config;
import com.kawakp.demingliu.jinandemo.listener.IOnNetResultListener;
import com.kawakp.demingliu.jinandemo.net.NetController;
import com.kawakp.demingliu.jinandemo.utils.SharedPerferenceHelper;

import java.util.LinkedHashSet;
import java.util.List;

/**
 * Created by zuheng.lv on 2016/8/8.
 */
public class RangeAdapter extends BaseAdapter {

    private Context context;
    private List<RangeClass> list;
    private NetController netController;
    private String cookie;
    private String json;
    private ViewHolder holder;
    private String PATH = "http://kawakp.chinclouds.com:58010/userconsle/devices/deviceId0022/elements/gxznhrjz_t_1/datas?pageNum=1&pageSize=1";
    private String PATH1 = "http://kawakp.chinclouds.com:58010/userconsle/devices/";
    private String PATH2 = "/gxznhrjz_t_1/datas?pageNum=1&pageSize=1";

    public RangeAdapter(Context context, List<RangeClass> list) {
        super();
        this.context = context;
        this.list = list;
        cookie = SharedPerferenceHelper.getCookie(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.range_adapter, null);
            holder = new ViewHolder();
            holder.color = (ImageView) convertView.findViewById(R.id.range_iv_color);
            holder.icon = (ImageView) convertView.findViewById(R.id.range_iv_icon);
            holder.title = (TextView) convertView.findViewById(R.id.range_tv_title);
            holder.data = (TextView) convertView.findViewById(R.id.range_tv_data);
            holder.meter = (TextView) convertView.findViewById(R.id.range_tv_meter);
            holder.max = (EditText) convertView.findViewById(R.id.range_et_max);
            holder.min = (EditText) convertView.findViewById(R.id.range_et_min);
            holder.correct = (EditText) convertView.findViewById(R.id.range_et_correct);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.color.setImageDrawable(list.get(position).getColor());
        holder.title.setText(list.get(position).getTitle().toString());
        holder.data.setText(list.get(position).getData().toString());
        holder.meter.setText(list.get(position).getMeter().toString());
        holder.max.setText(list.get(position).getMax().toString());
        holder.min.setText(list.get(position).getMin().toString());
        holder.correct.setText(list.get(position).getCorrect().toString());
        holder.max.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                json = "{\"value\":" + holder.max.getText() + "}";
                netController = new NetController();
                netController.requestNet(context, PATH1 + list.get(position).getMaxAddressID() + "/elements/" + list.get(position).getID(), NetController.HttpMethod.PUT, Config.FLAG_ZERO, new IOnNetResultListener() {
                    @Override
                    public void onNetResult(int flag, String jsonResult) {

                    }

                    @Override
                    public void onNetComplete(int flag) {

                    }
                }, cookie, json, null);
            }
        });
        holder.min.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                json = "{\"value\":" + holder.min.getText() + "}";
                netController = new NetController();
                netController.requestNet(context, PATH1 + list.get(position).getMinAddressID() + "/elements/" + list.get(position).getID(), NetController.HttpMethod.PUT, Config.FLAG_ZERO, new IOnNetResultListener() {
                    @Override
                    public void onNetResult(int flag, String jsonResult) {

                    }

                    @Override
                    public void onNetComplete(int flag) {

                    }
                }, cookie, json, null);

            }
        });
        holder.correct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                json = "{\"value\":" + holder.correct.getText() + "}";
                netController = new NetController();
                netController.requestNet(context, PATH1 + list.get(position).getCorrectAddressID() + "/elements/" + list.get(position).getID(), NetController.HttpMethod.PUT, Config.FLAG_ZERO, new IOnNetResultListener() {
                    @Override
                    public void onNetResult(int flag, String jsonResult) {

                    }

                    @Override
                    public void onNetComplete(int flag) {

                    }
                }, cookie, json, null);
            }
        });
        if (list.get(position).getIcon() != null) {
            holder.icon.setImageDrawable(list.get(position).getIcon());
        }
        return convertView;
    }


    class ViewHolder {
        ImageView color;//颜色图片
        ImageView icon;//图片
        TextView title;//标题
        TextView data;//数据采集
        TextView meter;//仪表显示
        EditText max;//量程上限
        EditText min;//量程下限
        EditText correct;//仪表校正
    }
}
