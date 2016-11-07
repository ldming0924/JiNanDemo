package com.kawakp.demingliu.jinandemo.listener;

/**
 * 网络请求完成时的回调接口
 * @author hele
 *
 */
public interface IOnNetResultListener {
	/**
	 *  网络访问获取到Json串,该方法在子线程当中回调
	 * @param flag
	 * @param jsonResult
	 */
	public void onNetResult(int flag, String jsonResult);
	
	/**
	 * 网络访问、数据解析完成,该方法在主线程中回调
	 * @param flag
	 */
	public void onNetComplete(int flag);
}
