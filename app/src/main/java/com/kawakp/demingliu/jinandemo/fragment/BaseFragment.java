package com.kawakp.demingliu.jinandemo.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class BaseFragment extends Fragment {
    private View view;
    private LayoutInflater inflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (this.inflater == null) {
            this.inflater = inflater;
        }

        initView(view);
        initData();
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    protected abstract void initView(View view);

    protected abstract void initData();

    protected abstract void setListen();

    @SuppressWarnings("unchecked")
    public final <E extends View> E getView(View view, int id) {
        return (E) view.findViewById(id);
    }

    protected void setContentView(int layoutResID) {
        view = inflater.inflate(layoutResID, null);
    }

    protected void setContextView(View view) {
        this.view = view;
    }
}
