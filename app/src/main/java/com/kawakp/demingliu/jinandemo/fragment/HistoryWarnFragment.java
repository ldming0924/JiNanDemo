package com.kawakp.demingliu.jinandemo.fragment;

import android.app.ProgressDialog;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cjj.MaterialRefreshLayout;
import com.cjj.MaterialRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.kawakp.demingliu.jinandemo.R;
import com.kawakp.demingliu.jinandemo.adapter.HistoryWarnAdapter;
import com.kawakp.demingliu.jinandemo.bean.HistoryWarnBean;
import com.kawakp.demingliu.jinandemo.bean.MateralBean;
import com.kawakp.demingliu.jinandemo.bean.WarmBean;
import com.kawakp.demingliu.jinandemo.constant.Config;
import com.kawakp.demingliu.jinandemo.http.OkHttpHelper;
import com.kawakp.demingliu.jinandemo.http.SimpleCallback;
import com.kawakp.demingliu.jinandemo.http.SpotsCallBack;
import com.kawakp.demingliu.jinandemo.listener.IOnNetResultListener;
import com.kawakp.demingliu.jinandemo.net.NetController;
import com.kawakp.demingliu.jinandemo.utils.MyListView;
import com.kawakp.demingliu.jinandemo.utils.Path;
import com.kawakp.demingliu.jinandemo.utils.PopUtils;
import com.kawakp.demingliu.jinandemo.utils.SharedPerferenceHelper;
import com.kawakp.demingliu.jinandemo.utils.wheelviewutlis.NumericWheelAdapter;
import com.kawakp.demingliu.jinandemo.utils.wheelviewutlis.OnWheelScrollListener;
import com.kawakp.demingliu.jinandemo.utils.wheelviewutlis.WheelView;
import com.kawakp.demingliu.jinandemo.widget.DividerItemDecoration;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import dmax.dialog.SpotsDialog;
import okhttp3.Response;


/**
 * Created by deming.liu on 2016/7/5.
 */
public class HistoryWarnFragment extends BaseFragment {
    @Bind(R.id.linearLayout_start)
    LinearLayout lin_start_time;
    @Bind(R.id.linearLayout_end)
    LinearLayout lin_end_time;
    @Bind(R.id.textView_start_time)
    TextView tv_start_time;
    @Bind(R.id.textView_end_time)
    TextView tv_end_time;
    @Bind(R.id.button_search)
    Button btn_search;
    @Bind(R.id.materialRefreshLayout)
    MaterialRefreshLayout mRefreshLaout;
    @Bind(R.id.recyclerView)
    RecyclerView recyclerView;
    private boolean flag = false;
    private PopupWindow menuWindow;
    private LayoutInflater inflater = null;
    private WheelView year;
    private WheelView month;
    private WheelView day;
    private WheelView hour;
    private WheelView mins;
    private OkHttpHelper okHttpHelper;
    private HistoryWarnAdapter adapter;
    private SpotsDialog mDialog;
    private View view;
    private int page = 1;
    private int pageSize = 10;
    private int status = 1;
    private int totalPage = 1;
    private static final int STATE_NOMAL = 0; //正常加载
    private static final int STATE_REFRESH = 1; //下拉刷新
    private static final int STATE_MORE = 2; //上拉加载
    private int state = STATE_NOMAL;
    private String url = "http://kawakp.chinclouds.com:60034/userconsle/deviceAlarms?";
    private List<WarmBean> totallist = new ArrayList<WarmBean>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_his_data, null);
            ButterKnife.bind(this,view);
            initView(view);
            initData();
            setListen();
        } else {
            ViewGroup parent = (ViewGroup) view.getParent();
            if (parent != null) {
                parent.removeView(view);
            }
        }
        return view;
    }

    @Override
    protected void initView(View view) {
        getCurrentTime();
        okHttpHelper = OkHttpHelper.getInstance(getActivity());
        initRefreshLayout();
        if (getActivity() != null) {
            mDialog = new SpotsDialog(getActivity(),"拼命加载中...");
            mDialog.show();
            requestWares();
        }
    }

    private  void initRefreshLayout(){
        mRefreshLaout.setLoadMore(true);
        mRefreshLaout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(MaterialRefreshLayout materialRefreshLayout) {
                refreshData();
            }

            @Override
            public void onRefreshLoadMore(MaterialRefreshLayout materialRefreshLayout) {
                if(page <=totalPage)
                    loadMoreData();
                else{
                    mRefreshLaout.finishRefreshLoadMore();
                }
            }
        });
    }
    private void requestWares(){
        String s = url + "pageNum=" + page + "&pageSize=" + pageSize + "&status=" + status;
        okHttpHelper.get(s, new SimpleCallback<MateralBean<WarmBean>>(getActivity()) {

            @Override
            public void onSuccess(Response response, MateralBean<WarmBean> warmBeanMateralBean) {
                totalPage =warmBeanMateralBean.getPages();
                totallist = warmBeanMateralBean.getList();
                showWaresData(totallist);
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

        });

    }
    private  void refreshData(){
        page =1;
        state=STATE_REFRESH;
        requestWares();
    }

    private void loadMoreData(){

        page = ++page;
        state = STATE_MORE;
        requestWares();

    }
    private  void showWaresData(List<WarmBean> wares){
        switch (state){
            case  STATE_NOMAL:
                    mDialog.dismiss();
                if(adapter ==null) {
                    adapter = new HistoryWarnAdapter(getActivity(), wares);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                }
                else{
                    adapter.clear();
                    adapter.addData(wares);
                }
                break;
            case STATE_REFRESH:
                adapter.clear();
                adapter.refreshData(wares);
                recyclerView.scrollToPosition(0);
                mRefreshLaout.finishRefresh();
                break;
            case STATE_MORE:
                adapter.loadMoreData(wares);
                recyclerView.scrollToPosition(adapter.getDatas().size());
                mRefreshLaout.finishRefreshLoadMore();
                break;
        }

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void setListen() {

    }

    private void getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        tv_start_time.setText(str);
        tv_end_time.setText(str);
    }

    @OnClick({R.id.linearLayout_start,R.id.linearLayout_end,R.id.button_search,})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.linearLayout_start:
                flag = false;
                showPopwindow(getDataPick());//弹出日期选择器
                break;
            case R.id.linearLayout_end:
                flag = true;
                showPopwindow(getDataPick());//弹出日期选择器
                break;
            case R.id.button_search:
                try {
                    String startTime = URLEncoder.encode(tv_start_time.getText().toString() + ":00", "utf-8");
                    String endTime = URLEncoder.encode(tv_end_time.getText().toString() + ":00", "utf-8");
                    String url = Path.WARN_DATA + "pageNum=" + page + "&pageSize=" + 100 + "&fromDate=" + startTime + "&toDate=" + endTime + "&status=" + status;
                    Log.d("TAG", url);
                    state = STATE_NOMAL;
                    okHttpHelper.get(url, new SpotsCallBack<MateralBean<WarmBean>>(getActivity()) {
                        @Override
                        public void onSuccess(Response response, MateralBean<WarmBean> warmBeanMateralBean) {
                            totallist = warmBeanMateralBean.getList();
                            showWaresData(totallist);
                        }

                        @Override
                        public void onError(Response response, int code, Exception e) {

                        }

                    });
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

                break;
        }
    }

    private long getLongTime(String date) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dt2 = null;
        long lTime = 0;
        try {
            dt2 = sdf.parse(date);
            //继续转换得到秒数的long型
            lTime = dt2.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return lTime;
    }

    /**
     * 初始化popupWindow
     *
     * @param view
     */
    private void showPopwindow(View view) {
        menuWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, false);
        menuWindow.setFocusable(true);
        menuWindow.setOutsideTouchable(true);
        menuWindow.setBackgroundDrawable(new BitmapDrawable());
        menuWindow.setAnimationStyle(R.style.myanimation);
        menuWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
        menuWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                menuWindow = null;
                PopUtils.setBackgroundAlpha(1.0f, getActivity());//设置Popw消失背景为透明
            }
        });
        PopUtils.setBackgroundAlpha(0.5f, getActivity());//设置popw出现时背景透明度

    }


    /**
     * 日期选择
     *
     * @return
     */
    private View getDataPick() {
        Calendar c = Calendar.getInstance();
        int curYear = c.get(Calendar.YEAR);
        int curMonth = c.get(Calendar.MONTH) + 1;//通过Calendar算出的月数要+1
        int curDate = c.get(Calendar.DATE);
        final View view = LayoutInflater.from(getActivity()).inflate(R.layout.datapick, null);

        year = (WheelView) view.findViewById(R.id.year);
        year.setAdapter(new NumericWheelAdapter(1950, curYear));
        //year.setLabel("年");
        year.setCyclic(true);
        year.addScrollingListener(scrollListener);

        month = (WheelView) view.findViewById(R.id.month);
        month.setAdapter(new NumericWheelAdapter(1, 12));
        // month.setLabel("月");
        month.setCyclic(true);
        month.addScrollingListener(scrollListener);

        day = (WheelView) view.findViewById(R.id.day);
        initDay(curYear, curMonth);
        //day.setLabel("日");
        day.setCyclic(true);

        year.setCurrentItem(curYear - 1950);
        month.setCurrentItem(curMonth - 1);
        day.setCurrentItem(curDate - 1);

        hour = (WheelView) view.findViewById(R.id.hour);
        hour.setAdapter(new NumericWheelAdapter(0, 23));
        // hour.setLabel("时");
        hour.setCyclic(true);
        mins = (WheelView) view.findViewById(R.id.mins);
        mins.setAdapter(new NumericWheelAdapter(0, 59));
        //mins.setLabel("分");
        mins.setCyclic(true);

        hour.setCurrentItem(8);
        mins.setCurrentItem(30);

        TextView bt = (TextView) view.findViewById(R.id.set);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = (year.getCurrentItem() + 1950) + "-" + (month.getCurrentItem() + 1) + "-" + (day.getCurrentItem() + 1 + " " + hour.getCurrentItem() + ":" + mins.getCurrentItem());
                if (flag) {
                    tv_end_time.setText(str);
                } else {
                    tv_start_time.setText(str);
                }
                menuWindow.dismiss();
            }
        });
        TextView cancel = (TextView) view.findViewById(R.id.cancel);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuWindow.dismiss();
            }
        });
        return view;
    }

    OnWheelScrollListener scrollListener = new OnWheelScrollListener() {

        @Override
        public void onScrollingStarted(WheelView wheel) {

        }

        @Override
        public void onScrollingFinished(WheelView wheel) {
            // TODO Auto-generated method stub
            int n_year = year.getCurrentItem() + 1950;// 楠烇拷
            int n_month = month.getCurrentItem() + 1;// 閺堬拷
            initDay(n_year, n_month);
        }
    };

    private void initDay(int arg1, int arg2) {
        day.setAdapter(new NumericWheelAdapter(1, getDay(arg1, arg2), "%02d"));
    }

    /**
     * @param year
     * @param month
     * @return
     */
    private int getDay(int year, int month) {
        int day = 30;
        boolean flag = false;
        switch (year % 4) {
            case 0:
                flag = true;
                break;
            default:
                flag = false;
                break;
        }
        switch (month) {
            case 1:
            case 3:
            case 5:
            case 7:
            case 8:
            case 10:
            case 12:
                day = 31;
                break;
            case 2:
                day = flag ? 29 : 28;
                break;
            default:
                day = 30;
                break;
        }
        return day;
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mDialog != null) {
            mDialog.dismiss();
        }
    }
}
