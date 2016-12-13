package com.kawakp.demingliu.jinandemo.http;

import android.content.Context;

import java.io.InputStream;

import okhttp3.Request;
import okhttp3.Response;


public abstract class SimpleCallback<T> extends BaseCallback<T> {

    protected Context mContext;

    public SimpleCallback(Context context){

        mContext = context;

    }

    @Override
    public void onBeforeRequest(Request request) {

    }

    @Override
    public void onFailure(Request request, Exception e) {

    }

    @Override
    public void onResponse(Response response) {

    }

    @Override
    public void onTokenError(Response response, int code) {


    }


}
