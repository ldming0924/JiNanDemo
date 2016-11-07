package com.kawakp.demingliu.jinandemo.net;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;


import com.kawakp.demingliu.jinandemo.constant.Config;
import com.kawakp.demingliu.jinandemo.listener.IOnNetResultListener;
import com.kawakp.demingliu.jinandemo.utils.CommonUtil;
import com.kawakp.demingliu.jinandemo.utils.SharedPerferenceHelper;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class NetController {
    private static final int TIME_OUT = 8 * 1000;
    private NetTask mNetTask;

    /**
     *
     * @param url 接口地址
     * @param method
     * @param flag 一个页面多个请求，用于区分是哪个请求
     * @param onNetResultListener
     * @param cookie
     * @param json JSON字符串
     * @param context 上下文
     */

    public void requestNet(Context context, String url, HttpMethod method, int flag, IOnNetResultListener onNetResultListener, String cookie, String json, Map<String,String> map) {
        if (mNetTask != null && mNetTask.getStatus() == AsyncTask.Status.RUNNING) {
            mNetTask.cancel(true);
            mNetTask = null;
        }
        if (NetHelper.isHaveNet(context)) {
            mNetTask = new NetTask(url, onNetResultListener, flag, method, cookie, json, map, context);
            mNetTask.execute();
        }else {
            if (onNetResultListener != null){
                onNetResultListener.onNetResult(flag,"-1");
            }
        }
    }

    public void cancelTask() {
        if (mNetTask != null && mNetTask.getStatus() == AsyncTask.Status.RUNNING) {
            mNetTask.cancel(true);
            mNetTask = null;
        }
    }

    static class NetTask extends AsyncTask<Void, Void, String> {
        private String url;
        private IOnNetResultListener onNetResultListener;
        private int flag;
        private HttpMethod method;
        private String cookie;
        private Context context;
        private String json;
        private Map<String,String> map;

        /**
         *
         * @param url
         * @param onNetResultListener
         * @param flag 用于判断一个页面多个请求，判断是哪一个请求
         * @param method
         * @param map 表单
         * @param cookie
         * @param json 传入的JSON字符串
         * @param context
         */

        public NetTask(String url, IOnNetResultListener onNetResultListener, int flag, HttpMethod method,
                        String cookie,String json,Map<String,String> map, Context context) {
            this.url = url;
            this.flag = flag;
            this.onNetResultListener = onNetResultListener;
            this.method = method;
            this.cookie = cookie;
            this.context = context;
            this.json = json;
            this.map = map;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            if (CommonUtil.isEmpty(url)) {
                if (onNetResultListener != null)
                    onNetResultListener.onNetResult(flag, null);
                return null;
            }
            String sb = new String();
            switch (method) {
                case GET:
                    sb = requestGetUtl(url, cookie);
                    if (Config.DEBUG_MODE) {
                       // Log.d("LDM" ,sb);
                    }
                    if (onNetResultListener != null) {
                        onNetResultListener.onNetResult(flag, sb);
                        return (url + "\n" + sb);
                    }
                    break;

                case POST:
                    sb = requestPostUrl(url,map,cookie, context);
                    if (Config.DEBUG_MODE) {
                       // Log.d("LDM" ,sb);
                    }
                    if (onNetResultListener != null) {
                        onNetResultListener.onNetResult(flag, sb);
                        return (url + "\n" + sb);
                    }
                    break;
                case PUT:
                    sb = requestPutUrl(url,json,cookie);
                    if (Config.DEBUG_MODE) {
                       //Log.d("LDM" ,sb+"");
                    }
                    if (onNetResultListener != null) {
                        onNetResultListener.onNetResult(flag, sb);
                        return (url + "\n" + sb);
                    }
                    break;
            }

            return null;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            if (onNetResultListener != null)
                onNetResultListener.onNetComplete(flag);
        }
    }


    private static String requestGetUtl(String url, String cookie) {
        HttpURLConnection httpConn = null;
        BufferedInputStream bis = null;
        try {
            URL urlObj = new URL(url);
            httpConn = (HttpURLConnection) urlObj.openConnection();
            httpConn.setRequestMethod("GET");
            httpConn.setDoInput(true);
            httpConn.setConnectTimeout(5000);
            httpConn.setReadTimeout(5000);
            httpConn.setRequestProperty("Cookie", cookie); //设置cookie
            httpConn.connect();

            if (httpConn.getResponseCode() == 200) {
                bis = new BufferedInputStream(httpConn.getInputStream());
                return dealResponseResult(bis);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("TAG", "err:" + e.toString());
            // return "err"+e.getMessage().toString();

        } finally {
            try {
                if (bis != null) {
                    bis.close();
                }
                httpConn.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;


    }

    /*
    * Function  :   发送Post请求到服务器
    * Param     :   params请求体内容，encode编码格式
    *
    */
    public static String requestPostUrl(String strUrlPath,Map<String,String> map,String cookie, Context context) {

        byte[] data = getRequestData(map).toString().getBytes();//获得请求体
        try {
            URL url = new URL(strUrlPath);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(5000);     //设置连接超时时间
            httpURLConnection.setReadTimeout(5000);
            httpURLConnection.setDoInput(true);                  //打开输入流，以便从服务器获取数据
            httpURLConnection.setDoOutput(true);                 //打开输出流，以便向服务器提交数据
            httpURLConnection.setRequestMethod("POST");     //设置以Post方式提交数据
            httpURLConnection.setUseCaches(false);               //使用Post方式不能使用缓存
            if (cookie != null){
                httpURLConnection.setRequestProperty("Cookie", cookie); //设置cookie
            }
            //设置请求体的类型是文本类型
            httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            //设置请求体的长度
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));
            //获得输出流，向服务器写入数据
            OutputStream outputStream = httpURLConnection.getOutputStream();
            outputStream.write(data);
          /*  if (cookie == null) {
                //获取cookie
                String sessionID = "";
                String cookieVal = "";
                String key = null;
                for (int i = 1; (key = httpURLConnection.getHeaderFieldKey(i)) != null; i++) {
                    if (key.equalsIgnoreCase("set-cookie")) {
                        cookieVal = httpURLConnection.getHeaderField(i);
                        cookieVal = cookieVal.substring(0, cookieVal.indexOf(";"));
                        sessionID = sessionID + cookieVal;

                    }
                }
                //保持cookie到本地
                boolean b = SharedPerferenceHelper.saveCookie(context, sessionID);
                Log.d("cookie",b+"　　aaaaaaaaaaaaaa　　"+sessionID);
            }*/


            int response = httpURLConnection.getResponseCode();            //获得服务器的响应码
            if (response == HttpURLConnection.HTTP_OK) {


                String cookieskey = "Set-Cookie";
                Map<String, List<String>> maps = httpURLConnection.getHeaderFields();
                List<String> coolist = maps.get(cookieskey);
                Iterator<String> it = coolist.iterator();
                StringBuffer sbu = new StringBuffer();
               // sbu.append("eos_style_cookie=default; ");
                while(it.hasNext()){
                    sbu.append(it.next());
                }
                Log.d("cookie","　　bbbbbbbbbbbbb　　"+sbu.toString().split(";")[0]);
                //保持cookie到本地
                boolean b = SharedPerferenceHelper.saveCookie(context, sbu.toString().split(";")[0]);

                InputStream inptStream = httpURLConnection.getInputStream();
                return dealResponseResult(inptStream);                     //处理服务器的响应结果
            }else {

            }
        } catch (IOException e) {
            //e.printStackTrace();
            return "err: " + e.getMessage().toString();
        }
        return null;
    }

    public static String requestPutUrl(String url,String json,String cookie){
        String result="";
        try{
            HttpClient client=new DefaultHttpClient();
            HttpPut put=new HttpPut(url);
            put.addHeader("Cookie",cookie);
            put.addHeader("Content-Type","application/json");
            put.setEntity(new StringEntity(json.toString()));
            HttpResponse response=client.execute(put);
            int code = response.getStatusLine().getStatusCode();
            if(code== HttpStatus.SC_OK){
                result= EntityUtils.toString(response.getEntity());
            }else if (code == 400){
                result= EntityUtils.toString(response.getEntity());
            }else {
                Log.d("TAG","ERR返回："+EntityUtils.toString(response.getEntity())) ;
                return null;
            }
        }catch(Exception e){
            e.printStackTrace();
            Log.d("TAG","ERR:"+e.toString());
        }
        return result;
    }


    public static String dealResponseResult(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int c = 0;
        byte[] buffer = new byte[8 * 1024];
        try {
            while ((c = is.read(buffer)) != -1) {
                baos.write(buffer, 0, c);
                baos.flush();
            }
            return new String(baos.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (baos != null) {
                    baos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 获取参数
     *
     * @param keys
     * @param values
     * @return
     * @throws
     * @throws IOException
     */
    private static String getParams(String[] keys, String[] values) {
        if (keys != null && values != null && keys.length != values.length) {

        }
        StringBuffer sb = new StringBuffer();
        if (keys != null && values != null)
            for (int i = 0; i < keys.length; i++) {
                if (i != 0)
                    sb.append("&");
                sb.append(keys[i]);
                sb.append("=");
                sb.append(values[i]);
            }
        return sb.toString();
    }

    /*
     * Function  :   封装请求体信息
     * Param     :   params请求体内容，encode编码格式
     *
     *
     */
    public static StringBuffer getRequestData(Map<String, String> params) {
        StringBuffer stringBuffer = new StringBuffer();        //存储封装好的请求体信息
        try {
            for(Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey())
                        .append("=")
                        .append(URLEncoder.encode(entry.getValue(),"utf-8"))
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1);    //删除最后的一个"&"
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }

    /**
     * 网络请求类型枚举
     *
     * @author hele
     */
    public static enum HttpMethod {
        GET, POST, PUT
    }

}

