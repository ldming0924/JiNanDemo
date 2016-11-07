package com.kawakp.demingliu.jinandemo.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


import com.kawakp.demingliu.jinandemo.bean.OrgBean;

import java.util.List;

/**
 * Created by deming.liu on 2016/8/10.
 */
public class DeviceListAdapter extends BaseAdapter {
    private List<OrgBean> list;
    private Context context;

    public DeviceListAdapter(List<OrgBean> list, Context context) {
        this.list = list;
        this.context = context;

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
    public View getView(int position, View convertView, ViewGroup parent) {

      /*  if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.devicelistada_item, null);
            TextView device_name = ViewHodle.getView(convertView, R.id.textView_device_name);
            TextView device_status = ViewHodle.getView(convertView, R.id.textView_status);
            ImageView img = ViewHodle.getView(convertView, R.id.imageView_device);
            device_name.setText(list.get(position).getName());
            if (list.get(position).getOnlineStatus().equals("ONLINE")) {
                device_status.setText("在线...");
                device_status.setTextColor(Color.parseColor("#3EAA48"));
            } else {
                device_status.setText("离线...");
            }
            if (position % 4 == 1) {
                img.setImageResource(R.drawable.huanre);
            } else if (position % 4 == 2) {
                img.setImageResource(R.drawable.shebei2);
            } else if (position % 4 == 3) {
                img.setImageResource(R.drawable.shebei3);
            } else {
                img.setImageResource(R.drawable.shebei4);
            }

        }*/

        return convertView;
    }


}
