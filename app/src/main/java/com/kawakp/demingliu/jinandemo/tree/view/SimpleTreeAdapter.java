package com.kawakp.demingliu.jinandemo.tree.view;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;


import com.kawakp.demingliu.jinandemo.R;
import com.kawakp.demingliu.jinandemo.tree.bean.Node;
import com.kawakp.demingliu.jinandemo.tree.bean.TreeListViewAdapter;

import java.util.List;

public class SimpleTreeAdapter<T> extends TreeListViewAdapter<T>
{

	public SimpleTreeAdapter(ListView mTree, Context context, List<T> datas,
							 int defaultExpandLevel) throws IllegalArgumentException,
			IllegalAccessException
	{
		super(mTree, context, datas, defaultExpandLevel);
	}

	@Override
	public View getConvertView(Node node , int position, View convertView, ViewGroup parent)
	{
		
		ViewHolder viewHolder = null;
		if (convertView == null)
		{
			convertView = mInflater.inflate(R.layout.list_item, parent, false);
			viewHolder = new ViewHolder();
			viewHolder.icon = (ImageView) convertView
					.findViewById(R.id.id_treenode_icon);
			viewHolder.label = (TextView) convertView
					.findViewById(R.id.id_treenode_label);
			viewHolder.status = (TextView) convertView
					.findViewById(R.id.textView_treenode_status);
			convertView.setTag(viewHolder);

		} else
		{
			viewHolder = (ViewHolder) convertView.getTag();
		}

		if (node.getIcon() == -1)
		{
			viewHolder.icon.setVisibility(View.INVISIBLE);
		} else
		{
			viewHolder.icon.setVisibility(View.VISIBLE);
			viewHolder.icon.setImageResource(node.getIcon());
		}

		viewHolder.label.setText(node.getName());
		if (node.getStatus() != null){
			if (node.getStatus().equals("ONLINE")) {
				viewHolder.status.setText("(" + "在线" + ")");
			}else {
				viewHolder.status.setText("(" + "离线" + ")");
			}
		}else {
			viewHolder.status.setText("");
		}
		
		
		return convertView;
	}

	private final class ViewHolder
	{
		ImageView icon;
		TextView label;
		TextView status;
	}

}
