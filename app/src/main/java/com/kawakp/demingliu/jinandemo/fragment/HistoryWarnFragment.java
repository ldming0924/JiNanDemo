package com.kawakp.demingliu.jinandemo.fragment;

import android.app.ProgressDialog;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
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
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.kawakp.demingliu.jinandemo.R;
import com.kawakp.demingliu.jinandemo.adapter.HistoryWarnAdapter;
import com.kawakp.demingliu.jinandemo.bean.HistoryWarnBean;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Response;


/**
 * Created by deming.liu on 2016/7/5.
 */
public class HistoryWarnFragment extends BaseFragment implements View.OnClickListener, PullToRefreshScrollView.OnScrollListener{
    private LinearLayout lin_start_time;
    private LinearLayout lin_end_time;
    private TextView tv_start_time;
    private TextView tv_end_time;
    private Button btn_search;
    private boolean flag = false;
    private PopupWindow menuWindow;
    private LayoutInflater inflater = null;
    private WheelView year;
    private WheelView month;
    private WheelView day;
    private WheelView hour;
    private WheelView mins;
    private MyListView listView;

    private int page = 1;
    private int pageSize = 10;
    private int status = 0;//0-未处理，1-人工处理，2-远程处理，3-已忽略
    private OkHttpHelper okHttpHelper;
    private int FLAG_ZERO = 0;
    private int FLAG_ONE = 1;
    private int FLAG_TWO = 2;

    private List<HistoryWarnBean> totallist = new ArrayList<HistoryWarnBean>();
    private HistoryWarnAdapter adapter;

    private ProgressDialog progressDialog;

    /**
     * 在MyScrollView里面的布局
     */
    private LinearLayout mLayout;
    /**
     * 位于顶部的布局
     */
    private LinearLayout mTopLayout;

    private PullToRefreshScrollView myScrollView;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (view == null) {
            view = inflater.inflate(R.layout.search_his_date_act, null);
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
        lin_start_time = getView(view, R.id.linearLayout_start);
        lin_end_time = getView(view, R.id.linearLayout_end);
        mLayout = getView(view, R.id.title);
        mTopLayout = getView(view, R.id.title2);
        myScrollView = getView(view, R.id.myScrollView);
        tv_start_time = getView(view, R.id.textView_start_time);
        tv_end_time = getView(view, R.id.textView_end_time);
        listView = getView(view, R.id.listView_search);
        btn_search = getView(view, R.id.button_search);
        okHttpHelper = OkHttpHelper.getInstance(getActivity());

        myScrollView.setOnScrollListener(this);
        //上拉、下拉设定
        myScrollView.setMode(PullToRefreshBase.Mode.BOTH);

        //当布局的状态或者控件的可见性发生改变回调的接口
        view.findViewById(R.id.parent_layout).getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {
                //这一步很重要，使得上面的布局和下面的布局重合
                onScroll(myScrollView.getScrollY());

            }
        });

        myScrollView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                //TODO 下拉刷新
                page = 1;
                String url = Path.WARN_DATA + "pageNum=" + page + "&pageSize=" + pageSize + "&status=" + status;

                okHttpHelper.get(url, new SimpleCallback<String>(getActivity()) {

                    @Override
                    public void onSuccess(Response response, String s) {
                        JSONObject object1 = JSON.parseObject(s);
                        JSONArray array1 = object1.getJSONArray("list");
                        List<HistoryWarnBean> list1 = JSON.parseArray(array1.toString(), HistoryWarnBean.class);
                        totallist.clear();
                        totallist.addAll(list1);
                        adapter.notifyDataSetChanged();
                        myScrollView.onRefreshComplete();//刷新完成
                    }

                    @Override
                    public void onError(Response response, int code, Exception e) {

                    }
                });

            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                //TODO 上啦加载
                page++;
                String url = Path.WARN_DATA + "pageNum=" + page + "&pageSize=" + pageSize + "&status=" + status;
                okHttpHelper.get(url, new SimpleCallback<String>(getActivity()) {

                    @Override
                    public void onSuccess(Response response, String s) {
                        JSONObject object2 = JSON.parseObject(s);
                        JSONArray array2 = object2.getJSONArray("list");
                        List<HistoryWarnBean> list2 = JSON.parseArray(array2.toString(), HistoryWarnBean.class);
                        totallist.addAll(list2);
                        //adapter.notifyDataSetChanged();
                        listView.setAdapter(adapter);
                        myScrollView.onRefreshComplete();//刷新完成
                    }

                    @Override
                    public void onError(Response response, int code, Exception e) {

                    }
                });

            }
        });
    }

    @Override
    protected void initData() {
        getCurrentTime();

        String url = Path.WARN_DATA + "pageNum=" + page + "&pageSize=" + pageSize + "&status=" + status;
        if (getActivity() != null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("正在加载,请稍候...");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            //NetController netController = new NetController();
            //netController.requestNet(getActivity(), url, NetController.HttpMethod.GET, FLAG_ZERO, this, cookie, null, null);
            okHttpHelper.get(url, new SimpleCallback<String>(getActivity()) {

                @Override
                public void onSuccess(Response response, String s) {
                    JSONObject jsonObject = JSON.parseObject(s);
                    JSONArray array = jsonObject.getJSONArray("list");
                    List<HistoryWarnBean> list = JSON.parseArray(array.toString(), HistoryWarnBean.class);
                    totallist.clear();
                    totallist.addAll(list);
                    //构建适配器
                    adapter = new HistoryWarnAdapter(totallist, getActivity());
                    listView.setAdapter(adapter);
                    if (progressDialog != null) {
                        progressDialog.dismiss();
                    }
                }

                @Override
                public void onError(Response response, int code, Exception e) {

                }
            });
        }

    }

    @Override
    protected void setListen() {
        lin_start_time.setOnClickListener(this);
        lin_end_time.setOnClickListener(this);
        btn_search.setOnClickListener(this);
    }

    private void getCurrentTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        tv_start_time.setText(str);
        tv_end_time.setText(str);
    }

    @Override
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
                    //NetController netController = new NetController();
                   // netController.requestNet(getActivity(), url, NetController.HttpMethod.GET, 3, this, cookie, null, null);
                    okHttpHelper.get(url, new SpotsCallBack<String>(getActivity()) {

                        @Override
                        public void onSuccess(Response response, String s) {
                            JSONObject jsonObject3 = JSON.parseObject(s);
                            JSONArray array3 = jsonObject3.getJSONArray("list");
                            List<HistoryWarnBean> list3 = JSON.parseArray(array3.toString(), HistoryWarnBean.class);
                            totallist.clear();
                            totallist.addAll(list3);
                            listView.setAdapter(adapter);
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
    public void onScroll(int scrollY) {
        int mBuyLayout2ParentTop = Math.max(scrollY, mLayout.getTop());
        mTopLayout.layout(0, mBuyLayout2ParentTop, mTopLayout.getWidth(), mBuyLayout2ParentTop + mTopLayout.getHeight());
    }





    @Override
    public void onPause() {
        super.onPause();
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }
}
