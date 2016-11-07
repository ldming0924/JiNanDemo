package com.kawakp.demingliu.jinandemo.utils;

import android.content.Context;
import android.widget.Toast;

public class IToast {
	private static Toast toast;
	private static Object toastMessage = ""; // toast内容。如果内容和上次相同并且同步显示则关闭掉上次的toast
	
	public static void showToast(Context context,String toasttext ) {
		//如果toast不为Null 并且内容和上次相同则取消掉上次的toast
		if(toastMessage == null||toasttext == null)
			return ;
		if(toast!=null&&toastMessage.equals(toasttext))
			toast.cancel();
		if (context != null){
			if(!toasttext.equals(toastMessage)){
				toastMessage = toasttext;
			}
			toast = Toast.makeText(context, toasttext, Toast.LENGTH_SHORT);
			 toast.show();
		}
	}

	public static void showToast(int resid, Context context) {
		if(toastMessage == null)
			return ;
		if(toast!=null&&toastMessage.equals(resid))
			toast.cancel();
		if (context != null){
			if(!toastMessage.equals(resid)){
				toastMessage = resid;
			}
			toast = Toast.makeText(context, resid, Toast.LENGTH_SHORT);
			toast.show();
		}
	}
}
