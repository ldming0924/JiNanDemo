package com.kawakp.demingliu.jinandemo.utils;

/**
 * Created by deming.liu on 2016/8/5.
 */
public class Path {
    public static final String LOGIN_PATH = "http://kawakp.chinclouds.com:60034/userconsle/login";
    //设备列表
    public final static String DEVICELIST_PATH = "http://kawakp.chinclouds.com:60034/userconsle/devices?";
   // public static final String REALTIME_DATA_PATH = "http://kawakp.chinclouds.com:60034/userconsle/devices/deviceId0022/elementTables/jngn_t_sjxs/datas?pageNum=1&pageSize=1";
  // public static final String REALTIME_DATA_PATH = "http://kawakp.chinclouds.com:60034/userconsle/devices/deviceId0037/elementTables/jngn_t_sjxs/datas?pageNum=1&pageSize=1";
    //获取报警历史数据
    public static final String WARN_DATA = "http://kawakp.chinclouds.com:60034/userconsle/deviceAlarms?";
    //获取元件列表
    public static final String PARAM_LIST ="http://kawakp.chinclouds.com:60034/userconsle/elementCategorys?";
    //控制设备
    public static String DEVICE_SET = "http://kawakp.chinclouds.com:60034/userconsle/devices/";
    //获取实时报警
    public static final String REAL_WARN = "http://kawakp.chinclouds.com:60034/userconsle/deviceAlarms?pageSize=0";

    //请求最新的一条报警信息
    public static final String NEW_WARM = "http://kawakp.chinclouds.com:60034/userconsle/deviceAlarms?pageSize=1";

}
