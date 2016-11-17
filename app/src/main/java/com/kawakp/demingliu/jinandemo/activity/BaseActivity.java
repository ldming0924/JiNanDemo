package com.kawakp.demingliu.jinandemo.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import butterknife.ButterKnife;

/**
 * Created by deming.liu on 2016/8/5.
 */
public abstract class BaseActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(setContentViewId());
        ButterKnife.bind(this);
        initViews(savedInstanceState);
    }

    public final <E extends View> E getView(int id) {
        return (E) findViewById(id);
    }
    public abstract int setContentViewId();

    public abstract void initViews(Bundle savedInstanceState);

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
