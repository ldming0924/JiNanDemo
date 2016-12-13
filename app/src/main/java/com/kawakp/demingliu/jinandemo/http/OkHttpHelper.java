package com.kawakp.demingliu.jinandemo.http;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class OkHttpHelper {


    public static final int TOKEN_MISSING = 401;// token 丢失
    public static final int TOKEN_ERROR = 402; // token 错误
    public static final int TOKEN_EXPIRE = 403; // token 过期


    public static final String TAG = "OkHttpHelper";

    private static OkHttpHelper mInstance;
    private OkHttpClient mHttpClient;
    private Gson mGson;

    private Handler mHandler;

    private ClearableCookieJar cookieJar1;

    private volatile static OkHttpHelper httpHelper;

    public static final MediaType JSON = MediaType
            .parse("application/json; charset=utf-8");


    private OkHttpHelper(Context context) {

        cookieJar1 = new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(context));
        Log.d("TAG", cookieJar1.toString() + "-----------------");
        mHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10000L, TimeUnit.MILLISECONDS)
                .readTimeout(10000L, TimeUnit.MILLISECONDS)
                .cookieJar(cookieJar1)
                .build();

        mGson = new Gson();
        mHandler = new Handler(Looper.myLooper());
    }


    public static OkHttpHelper getInstance(Context context) {
        if (httpHelper == null) {
            synchronized (OkHttpHelper.class) {
                if (httpHelper == null) {
                    httpHelper = new OkHttpHelper(context);
                }
            }
        }
        return httpHelper;
    }


    public void get(String url, Map<String, Object> param, BaseCallback callback) {


        Request request = buildGetRequest(url, param);

        request(request, callback);

    }

    public void get(String url, BaseCallback callback) {

        get(url, null, callback);
    }

    //下载更新文件
    public void download(String url, final Download callback){
        Request.Builder builder = new Request.Builder()
                .url(url);

        builder.get();
        Request request = builder.build();
        mHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                InputStream is = response.body().byteStream();
                if (callback != null) {
                    callback.getInputStream(is, response.body().contentLength());
                }

            }
        });
    }

public interface Download{
    void getInputStream(InputStream is,long length);
}

    private Download download;

    public void setDownload(Download download) {
        this.download = download;
    }

    /**
     * put 方法
     *
     * @param url      url 地址
     * @param json     json 字符串
     * @param callBack
     */
    public void put(String url, String json, BaseCallback callBack) {
        Request request = buildRequest(url, HttpMethodType.PUT, null, json);
        request(request, callBack);
    }


    public void post(String url, Map<String, Object> param, BaseCallback callback) {
        Request request = buildPostRequest(url, param);
        request(request, callback);
    }


    public void request(final Request request, final BaseCallback callback) {
        callback.onBeforeRequest(request);
        mHttpClient.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                callbackFailure(callback, request, e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                callbackResponse(callback, response);

                if (response.isSuccessful()) {
                    String resultStr = response.body().string();
                    if (callback.mType == String.class) {
                        callbackSuccess(callback, response, resultStr);
                    } else {
                        try {
                            Object obj = mGson.fromJson(resultStr, callback.mType);
                            callbackSuccess(callback, response, obj);
                        } catch (com.google.gson.JsonParseException e) { // Json解析的错误
                            callback.onError(response, response.code(), e);
                        }
                    }
                } else if (response.code() == TOKEN_ERROR || response.code() == TOKEN_EXPIRE || response.code() == TOKEN_MISSING) {

                    callbackTokenError(callback, response);
                } else {
                    callbackError(callback, response, null);
                }
            }


        });


    }


    private void callbackTokenError(final BaseCallback callback, final Response response) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onTokenError(response, response.code());
            }
        });
    }

    private void callbackSuccess(final BaseCallback callback, final Response response, final Object obj) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(response, obj);
            }
        });
    }


    private void callbackError(final BaseCallback callback, final Response response, final Exception e) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(response, response.code(), e);
            }
        });
    }


    private void callbackFailure(final BaseCallback callback, final Request request, final IOException e) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onFailure(request, e);
            }
        });
    }


    private void callbackResponse(final BaseCallback callback, final Response response) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onResponse(response);
            }
        });
    }


    private Request buildPostRequest(String url, Map<String, Object> params) {

        return buildRequest(url, HttpMethodType.POST, params, null);
    }

    private Request buildGetRequest(String url, Map<String, Object> param) {

        return buildRequest(url, HttpMethodType.GET, param, null);
    }

    private Request buildRequest(String url, HttpMethodType methodType, Map<String, Object> params, String jsonString) {

        Request.Builder builder = new Request.Builder()
                .url(url);

        if (methodType == HttpMethodType.POST) {
            RequestBody body = builderFormData(params);
            builder.post(body);
        } else if (methodType == HttpMethodType.GET) {

            url = buildUrlParams(url, params);
            builder.url(url);

            builder.get();
        } else if (methodType == HttpMethodType.PUT) {
            RequestBody requestBody = buildPutData(jsonString);
            builder.put(requestBody);
        }

        return builder.build();
    }

    /**
     * put 一个json类型的字符串
     *
     * @param json
     * @return
     */
    private RequestBody buildPutData(String json) {
        RequestBody requestBody = RequestBody.create(JSON, json);
        return requestBody;
    }

    private String buildUrlParams(String url, Map<String, Object> params) {
        if (params == null)
            params = new HashMap<>(1);
      /*  String token = CniaoApplication.getInstance().getToken();
        if(!TextUtils.isEmpty(token))
            params.put("token",token);*/

        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            sb.append(entry.getKey() + "=" + (entry.getValue() == null ? "" : entry.getValue().toString()));
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0, s.length() - 1);
        }
        if (url.indexOf("?") > 0) {
            url = url + "&" + s;
        } else {
            url = url + "?" + s;
        }

        return url;
    }

    private RequestBody builderFormData(Map<String, Object> params) {
        //  FormEncodingBuilder builder = new FormEncodingBuilder();
        FormBody.Builder builder = new FormBody.Builder();   //表单构造器
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue() == null ? "" : entry.getValue().toString());
            }

               /* String token = CniaoApplication.getInstance().getToken();
                if(!TextUtils.isEmpty(token))
                    builder.add("token", token);*/
        }
        return builder.build();
    }

    enum HttpMethodType {

        GET,
        POST,
        PUT,

    }


}
