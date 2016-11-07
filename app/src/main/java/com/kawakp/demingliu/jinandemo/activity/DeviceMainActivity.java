package com.kawakp.demingliu.jinandemo.activity;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;


import com.kawakp.demingliu.jinandemo.R;
import com.kawakp.demingliu.jinandemo.fragment.ControlSetFragment;
import com.kawakp.demingliu.jinandemo.fragment.ParameterSetFragment;
import com.kawakp.demingliu.jinandemo.fragment.HistoryWarnFragment;
import com.kawakp.demingliu.jinandemo.fragment.RealTimeDataFragment;
import com.kawakp.demingliu.jinandemo.widget.ChangeColorIconWithTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deming.liu on 2016/8/6.
 */
public class DeviceMainActivity extends BaseActivity implements View.OnClickListener, ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private List<Fragment> mTabs = new ArrayList<Fragment>();
    private MyAdapter mAdapter;

    private String[] mTitles = new String[]{"First Fragment!",
            "Second Fragment!", "Third Fragment!", "Fourth Fragment!"};

    private List<ChangeColorIconWithTextView> mTabIndicator = new ArrayList<ChangeColorIconWithTextView>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.testact);
        initView();
        initData();
        setListen();


    }

    @Override
    protected void initView() {
        mViewPager = (ViewPager) findViewById(R.id.id_viewpager);
    }

    @Override
    protected void initData() {

        RealTimeDataFragment tabFragment = new RealTimeDataFragment();
        mTabs.add(tabFragment);
        ControlSetFragment dataRecordFragment = new ControlSetFragment();
        mTabs.add(dataRecordFragment);
        ParameterSetFragment deviceManagerFragment = new ParameterSetFragment();
        mTabs.add(deviceManagerFragment);
        HistoryWarnFragment personalCenterFragment = new HistoryWarnFragment();
        mTabs.add(personalCenterFragment);

        mAdapter = new MyAdapter(getSupportFragmentManager());

        mViewPager.setAdapter(mAdapter);
        initTabIndicator();
    }

    @Override
    protected void setListen() {
        mViewPager.setOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        resetOtherTabs();

        switch (v.getId()) {
            case R.id.id_indicator_one:
                mTabIndicator.get(0).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(0, false);
                break;
            case R.id.id_indicator_two:
                mTabIndicator.get(1).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(1, false);
                break;
            case R.id.id_indicator_three:
                mTabIndicator.get(2).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(2, false);
                break;
            case R.id.id_indicator_four:
                mTabIndicator.get(3).setIconAlpha(1.0f);
                mViewPager.setCurrentItem(3, false);
                break;

        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (positionOffset > 0) {
            ChangeColorIconWithTextView left = mTabIndicator.get(position);
            ChangeColorIconWithTextView right = mTabIndicator.get(position + 1);

            left.setIconAlpha(1 - positionOffset);
            right.setIconAlpha(positionOffset);
        }
    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    /**
     * 重置其他的Tab
     */
    private void resetOtherTabs() {
        for (int i = 0; i < mTabIndicator.size(); i++) {
            mTabIndicator.get(i).setIconAlpha(0);
        }
    }

    private void initTabIndicator() {
        ChangeColorIconWithTextView one = (ChangeColorIconWithTextView) findViewById(R.id.id_indicator_one);
        ChangeColorIconWithTextView two = (ChangeColorIconWithTextView) findViewById(R.id.id_indicator_two);
        ChangeColorIconWithTextView three = (ChangeColorIconWithTextView) findViewById(R.id.id_indicator_three);
        ChangeColorIconWithTextView four = (ChangeColorIconWithTextView) findViewById(R.id.id_indicator_four);

        mTabIndicator.add(one);
        mTabIndicator.add(two);
        mTabIndicator.add(three);
        mTabIndicator.add(four);

        one.setOnClickListener(this);
        two.setOnClickListener(this);
        three.setOnClickListener(this);
        four.setOnClickListener(this);

        one.setIconAlpha(1.0f);
    }


    public class MyAdapter extends FragmentPagerAdapter {
        public MyAdapter(FragmentManager fm) {
            super(fm);
            // TODO Auto-generated constructor stub
        }

        @Override
        public Fragment getItem(int position) {
            return mTabs.get(position);
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }
    }

}
