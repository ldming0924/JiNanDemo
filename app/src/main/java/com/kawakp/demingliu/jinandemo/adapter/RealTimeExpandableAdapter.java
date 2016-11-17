package com.kawakp.demingliu.jinandemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.kawakp.demingliu.jinandemo.R;
import com.kawakp.demingliu.jinandemo.bean.ChildInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by deming.liu on 2016/10/13.
 */
public class RealTimeExpandableAdapter extends BaseExpandableListAdapter {
    private Map<String, List<ChildInfo>> map;
    private List<String> parent;
    private Context context;

    public RealTimeExpandableAdapter(Map<String, List<ChildInfo>> map, List<String> parent, Context context) {
        this.context = context;
        this.map = map;
        this.parent = parent;
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
    public View getGroupView(int i, boolean b, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.adapter_parameter_parent,viewGroup,false);
        }
        TextView tv = (TextView) view
                .findViewById(R.id.tv_parameter_parent);
        tv.setText(this.parent.get(i));
        return view;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View view, ViewGroup viewGroup) {
        String key = this.parent.get(i);
        ChildInfo info = map.get(key).get(i1);
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.adapter_reltime_child_item, viewGroup,false);
        }
        TextView name = (TextView) view
                .findViewById(R.id.textView_leiming);
        TextView value = (TextView) view
                .findViewById(R.id.textView_data);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView22);
        if (key.equals("温度参数")){
            imageView.setImageResource(R.drawable.temperature);
        }else if (key.equals("压力参数")){
            imageView.setImageResource(R.drawable.pressure);
        }else if (key.equals("液位参数")){
            imageView.setImageResource(R.drawable.water_position);
        }else {
            imageView.setImageResource(R.drawable.temperature);
        }
        name.setText(info.getName().toString());
        value.setText(info.getValue().toString()+info.getUnit());

        return view;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return true;
    }
}
