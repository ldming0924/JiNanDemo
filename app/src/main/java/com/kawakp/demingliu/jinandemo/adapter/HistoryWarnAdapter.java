package com.kawakp.demingliu.jinandemo.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kawakp.demingliu.jinandemo.R;
import com.kawakp.demingliu.jinandemo.bean.HistoryWarnBean;
import com.kawakp.demingliu.jinandemo.utils.ViewHodle;

import java.util.List;

/**
 * Created by deming.liu on 2016/8/26.
 */
public class HistoryWarnAdapter extends BaseAdapter {
    private List<HistoryWarnBean> list;
    private Context context;

    public HistoryWarnAdapter(List<HistoryWarnBean> list, Context context) {
        this.list = list;
        this.context = context;

    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {
            return 0;
        } else {
            return 1;
        }

    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.historywarn_item, null);
            TextView tv_time = ViewHodle.getView(view, R.id.textView_time);
            TextView tv_name = ViewHodle.getView(view, R.id.textView_name);//名称
            TextView tv_type = ViewHodle.getView(view, R.id.textView_type);
            TextView tv_shijian = ViewHodle.getView(view, R.id.textView_shijian);
            TextView tv_new = ViewHodle.getView(view, R.id.textView_new);
            TextView tv_limit = ViewHodle.getView(view, R.id.textView_limit);
            LinearLayout linearLayout = ViewHodle.getView(view, R.id.lin_detail);

            int type = getItemViewType(i);
            switch (type) {
                case 0:
                    linearLayout.setBackgroundColor(context.getResources().getColor(R.color.gray2));
                    break;
                case 1:
                    linearLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
                    break;

            }

            tv_time.setText(list.get(i).getCreateDate());
            tv_shijian.setText(list.get(i).getDisplayName());
        }
        return view;
    }
}
