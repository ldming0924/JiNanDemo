package com.kawakp.demingliu.jinandemo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

/**
 * Created by deming.liu on 2016/8/5.
 */
public abstract class BaseActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public final <E extends View> E getView(int id) {
        return (E) findViewById(id);
    }

    protected abstract void initView();
    protected abstract void initData();
    protected abstract void setListen();
}
