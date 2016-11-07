package com.kawakp.demingliu.jinandemo.utils;

import android.util.SparseArray;
import android.view.View;

public class ViewHodle {
	/**
	 * 封装ViewHodle
	 * 
	 * @param convertView
	 * @param id
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends View> T getView(View convertView, int id) {
		SparseArray<View> viewHodle = (SparseArray<View>) convertView.getTag();
		if (viewHodle == null) {
			viewHodle = new SparseArray<View>();
			convertView.setTag(viewHodle);
		}
		View childView = viewHodle.get(id);
		if (childView == null) {
			childView = convertView.findViewById(id);
			viewHodle.put(id, childView);
		}
		return (T) childView;
	}
}
