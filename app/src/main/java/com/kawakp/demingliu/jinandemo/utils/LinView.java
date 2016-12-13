package com.kawakp.demingliu.jinandemo.utils;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.kawakp.demingliu.jinandemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by deming.liu on 2016/11/21.
 */

public class LinView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder holder;
    private boolean threadFlag = true;//线程循环标记位
    private DrawThread thread;

    private int CHARTH = 400; //表格高
    private int CHARTW = 500; //表格宽
    private int OFFSET_LEFT = 100; // 距离左边位置
    private int OFFSET_TOP = 100;// 距离顶部位置
    private int TEXT_OFFSET = 20;
    private int X_INTERVAL = 20;//20个单位构成一个刻度
    private int XMAX = 200;//最大刻度
    int widthSize;
    int heightSize;
    int yz = 150;  // y轴的最大值



    private List<Point> plist;

    double mY;


    public void setmY(double mY) {
        this.mY = mY;
    }

    public LinView(Context context) {
        this(context,null);

    }

    public LinView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LinView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.AT_MOST || widthMode == MeasureSpec.UNSPECIFIED){
            widthSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, CHARTW, getResources().getDisplayMetrics()); //从XML中获取
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize,MeasureSpec.EXACTLY);
            CHARTW = widthSize;

        }else {
            CHARTW = MeasureSpec.getSize(widthMeasureSpec);

        }
        if (heightMode == MeasureSpec.AT_MOST || heightMode == MeasureSpec.UNSPECIFIED){
            heightSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,CHARTH,getResources().getDisplayMetrics());
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize,MeasureSpec.EXACTLY);
            CHARTH = heightSize;
        }else {
            CHARTH = MeasureSpec.getSize(heightMeasureSpec);
        }
        Log.d("LINVIEW",CHARTW+"　"+CHARTH);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    private void initData() {
        plist = new ArrayList<Point>();

        holder = this.getHolder();
        holder.addCallback(this);
        setZOrderOnTop(true);
        holder.setFormat(PixelFormat.TRANSLUCENT);
        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("TAG","22222222222");
        thread = new DrawThread();
        thread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    class DrawThread extends Thread{
        @Override
        public void run() {
            super.run();
            while (threadFlag){
                synchronized (holder) { //同位锁
                    Canvas canvas = holder.lockCanvas();
                    Log.d("LINVIEW","canvas = "+canvas);
                    if (canvas != null) {
                        drawTable(canvas); //表格
                        prepareLine(mY); //坐标点
                        drawCurve(canvas); //折线

                        holder.unlockCanvasAndPost(canvas);
                    }
                }
                SystemClock.sleep(1000);
            }
        }
    }

    public void start(){
        if(!thread.isAlive()){
            thread.start();
            threadFlag=true;
        }
    }
    public void stop(){
        threadFlag=false;
    }

    private void drawTable(Canvas canvas){

        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.black));

        //画外框
        paint.setStyle(Paint.Style.STROKE);
        //paint.setStrokeWidth(Pain);
        Rect chartRec = new Rect(OFFSET_LEFT, OFFSET_TOP,CHARTW-100, CHARTH-OFFSET_TOP);//左上角的坐标是（OFFSET_LEFT,OFFSET_TOP），右下角的坐标是（CHARTW+OFFSET_LEFT,CHARTH+OFFSET_TOP）
        Log.d("TAG",OFFSET_LEFT+"  "+OFFSET_TOP+"　"+CHARTW+OFFSET_LEFT+"  "+ CHARTH+OFFSET_TOP+"  "+widthSize+"  "+heightSize);
        canvas.drawRect(chartRec, paint);


        //画左侧数字
        canvas.drawText(yz+"", OFFSET_LEFT - TEXT_OFFSET -10, OFFSET_TOP+10, paint);
        for(int i = 1 ; i<10 ; i++){
            canvas.drawText(""+15*i, OFFSET_LEFT - TEXT_OFFSET-5,  CHARTH-100-( CHARTH-OFFSET_TOP-100)/10*(i), paint);
        }
        canvas.drawText("0", OFFSET_LEFT - TEXT_OFFSET -5,  CHARTH-100, paint);

        //画表格中的虚线
        Path path = new Path();
        //Path的线段虚线化。构造函数为DashPathEffect(float[] intervals, float offset)，
        // 其中intervals为虚线的ON和OFF数组，该数组的length必须大于等于2，phase为绘制时的偏移量。
        PathEffect effects = new DashPathEffect(new float[]{2,2,2,2},1);
        paint.setStyle(Paint.Style.STROKE);
        paint.setAntiAlias(false);
        paint.setPathEffect(effects);
        for(int i = 1 ; i<10 ; i++){
            path.moveTo(OFFSET_LEFT,  CHARTH-100-( CHARTH-OFFSET_TOP-100)/10*(i));  //moveTo 不会进行绘制，只用于移动移动画笔。把画笔移动(x,y)处开始绘制
            path.lineTo(CHARTW-100, CHARTH-100-( CHARTH-OFFSET_TOP-100)/10*(i));//lineTo 用于进行直线绘制。添加一条从一点到指定点(x,y)的线，如果没有移至，则自动设置为(0,0)。
            canvas.drawPath(path, paint);
            //canvas.drawLine(OFFSET_LEFT, OFFSET_TOP + CHARTH/10*i, OFFSET_LEFT+CHARTW, OFFSET_TOP + CHARTH/10*i, paint);

        }
    }

    private void drawCurve(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(getResources().getColor(R.color.blue));
        paint.setStrokeWidth(3);
        paint.setAntiAlias(true);
        //canvas.drawLines(line, paint);
        try {
            Thread.sleep(1000);
            prepareLine(mY);
            if(plist.size() >= 2){
                for(int i = 0; i<plist.size()-1; i++){
                    canvas.drawLine(plist.get(i).x, plist.get(i).y, plist.get(i+1).x, plist.get(i+1).y, paint);
                    postInvalidate();
                }

            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }



    private void prepareLine(double y){
        //int py = OFFSET_TOP + (int)(Math.random()*(CHARTH - OFFSET_TOP));
        //int py = ((1-(y/100)))*CHARTH+OFFSET_TOP;
        double f = ((double) y)/yz; //计算y占y轴最大值得比值，表示将表格分成200份，每份有多少
        double f1 = 1- f;
        double py = f1*(CHARTH-200) + OFFSET_TOP;//(CHARTH-200)控件总高度-距离上下的距离，f1*(CHARTH-200)表示占表格多少+距离底部距离就是y的坐标点
        //Log.d("TAG",py+"-----"+y+"===="+CHARTH+"   "+f1+"　　　"+f);
        Point p = new Point( CHARTW-100 , (int)py );////将坐标保存在Point类,从最右边的点开始
        int MaxDataSize=(CHARTW-200)/X_INTERVAL;   //横坐标  最多可绘制的点
        if(plist.size() > MaxDataSize){
            Log.d("TAG",plist.size()+"            dddddddddd        "+MaxDataSize+"===="+CHARTH);
            plist.remove(0);
            for(int i = 0; i<MaxDataSize; i++){
                if(i == 0) {
                    plist.get(i).x -= (X_INTERVAL - 2);
                }
                else {
                    plist.get(i).x -= X_INTERVAL;
                }
            }
            plist.add(p);
        } else{
            for(int i = 0; i<plist.size()-1; i++){
                plist.get(i).x -= X_INTERVAL;
            }
            plist.add(p);
        }

    }

}
