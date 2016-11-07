package com.kawakp.demingliu.jinandemo.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


import com.kawakp.demingliu.jinandemo.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zuheng.lv on 2016/8/5.
 */
public class Animatiom  extends SurfaceView implements SurfaceHolder.Callback {

    private double mWeight;//全屏宽
    private double mHeight;//全屏长
    private SurfaceHolder holder;
    private Circle circle1;
    private Circle circle2;
    private Circle circle3;
    private Circle circle4;
    private Circle circle5;
    private List<Circle> circleList1;
    private List<Circle> circleList2;
    private List<Circle> circleList3;
    private List<Circle> circleList4;
    private List<Circle> circleList5;
    private Paint mPaint1;
    private Paint mPaint2;
    private Paint mPaint3;
    private int COLOR_RED =0;
    private int COLOR_BLUE=1;
    private int COLOR_ORANGE =2;
    private boolean STATUS_CIRCLE1=false;//运行标记位
    private boolean STATUS_CIRCLE2=false;
    private boolean STATUS_CIRCLE3=false;
    private boolean STATUS_CIRCLE4=false;
    private boolean STATUS_CIRCLE5=false;
    private boolean threadFlag = true;//线程循环标记位
    private int timesleep = 90;//线程休眠时间

    private CircleThread1 thread1;//运行线程
    private CircleThread2 thread2;
    private CircleThread3 thread3;
    private CircleThread4 thread4;
    private CircleThread5 thread5;
    private DrawThread drawThread;///画图线程

    private Bitmap bitmap;
    private  Rect rect;

    public Animatiom(Context context) {
        super(context);
        initData();
    }

    public Animatiom(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initData();
    }

    public Animatiom(Context context, AttributeSet attributeSet, int defStyle) {
        super(context, attributeSet, defStyle);
        initData();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //在创建时激发，一般在这里调用画图的线程。

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //在surface的大小发生改变时激发
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //销毁时激发，一般在这里将画图的线程停止、释放。
    }

    public void initData() {
        drawThread = new DrawThread();
        thread1 = new CircleThread1();
        thread2 = new CircleThread2();
        thread3 = new CircleThread3();
        thread4 = new CircleThread4();
        thread5 = new CircleThread5();
        DisplayMetrics dm2 = getResources().getDisplayMetrics();//获取全屏长款尺寸
        mHeight = dm2.heightPixels;
        mWeight = dm2.widthPixels;
//        setZOrderOnTop(true);//控件置顶
//        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        holder = this.getHolder();
        holder.addCallback(this);
        //bitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.animotion_back)).getBitmap();
        bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.animotion_back);
        rect = new Rect(0,0,(int)mWeight,(int)mHeight);

        circleList1 = new ArrayList<>();
        circleList2 = new ArrayList<>();
        circleList3 = new ArrayList<>();
        circleList4 = new ArrayList<>();
        circleList5 = new ArrayList<>();


        mPaint1 = new Paint();
        mPaint1.setColor(Color.RED);
        mPaint1.setAlpha(90);//透明度
        mPaint1.setAntiAlias(true);//抗锯齿

        mPaint2 = new Paint();
        mPaint2.setColor(Color.BLUE);
        mPaint2.setARGB(90,26,20,249);
        mPaint2.setAlpha(90);
        mPaint2.setAntiAlias(true);

        mPaint3 = new Paint();
        mPaint3.setARGB(90,232,101,31);
        mPaint3.setAlpha(90);
        mPaint3.setAntiAlias(true);
    }


/**
 * 初始化起始点元素
 * */

    public void born(int i) {
        switch (i) {
            case 0:
                circle1 = new Circle();
                circle1.setX(mWeight * 0.025);
                circle1.setY(mHeight * 0.208);
                circle1.setFlag(0);
                circle1.setLINE(true);
                circle1.setColor(0);
                circleList1.add(circle1);
                break;
            case 1:
                circle2 = new Circle();
                circle2.setX(mWeight * 0.970);
                circle2.setY(mHeight * 0.365);
                circle2.setFlag(0);
                circle2.setLINE(true);
                circle2.setColor(1);
                circleList2.add(circle2);
                break;
            case 2:
                circle3 = new Circle();
                circle3.setX(mWeight * 0.025);
                circle3.setY(mHeight * 0.505);
                circle3.setFlag(0);
                circle3.setLINE(true);
                circle3.setColor(1);
                circleList3.add(circle3);
                break;
            case 3:
                circle4 = new Circle();
                circle4.setX(mWeight * 0.555);
                circle4.setY(mHeight * 0.68);
                circle4.setFlag(0);
                circle4.setLINE(true);
                circle4.setColor(1);
                circleList4.add(circle4);
                break;
            case 4:
                circle5 = new Circle();
                circle5.setX(mWeight * 0.77);
                circle5.setY(mHeight * 0.51);
                circle5.setFlag(0);
                circle5.setLINE(true);
                circle5.setColor(1);
                circleList5.add(circle5);
                break;
        }
    }
    /**
     * 绘图函数
     *
     * */
    private void drawUiCircle1(Canvas canvas) {
        for (int i = 0; i < circleList1.size(); i++) {
            if(!(circleList1.get(i).getX()>=0.27*mWeight && circleList1.get(i).getX()<=0.315*mWeight&&circleList1.get(i).getY()>=0.2 && circleList1.get(i).getY()<=0.24*mHeight)){
                if(circleList1.get(i).getColor()==0){
                    canvas.drawCircle((float) circleList1.get(i).getX(), (float) circleList1.get(i).getY(), (float) (mWeight * 0.004), mPaint1);
                }else if(circleList1.get(i).getColor()==1){
                    canvas.drawCircle((float) circleList1.get(i).getX(), (float) circleList1.get(i).getY(), (float) (mWeight * 0.004), mPaint2);
                }
            }

        }
    }
    private void drawUiCircle2(Canvas canvas) {
        for (int i = 0; i < circleList2.size(); i++) {
            if(!(( (circleList2.get(i).getX()<=0.71*mWeight && circleList2.get(i).getX()>=0.635*mWeight)) && circleList2.get(i).getY()<=0.5*mHeight && circleList2.get(i).getY()>=0.3*mHeight )){
                if(circleList2.get(i).getColor()==1) {
                    canvas.drawCircle((float) circleList2.get(i).getX(), (float) circleList2.get(i).getY(), (float) (mWeight * 0.004), mPaint2);
                }else if(circleList2.get(i).getColor()==2){
                    canvas.drawCircle((float) circleList2.get(i).getX(), (float) circleList2.get(i).getY(), (float) (mWeight * 0.004), mPaint3);
                }
            }
        }
    }
    private void drawUiCircle3(Canvas canvas) {
        for (int i = 0; i < circleList3.size(); i++) {
            if(!((circleList3.get(i).getX()<=0.71*mWeight && circleList3.get(i).getX()>=0.635*mWeight) || (circleList3.get(i).getX()<=0.28*mWeight && circleList3.get(i).getX()>=0.22*mWeight && circleList3.get(i).getY()>=0.59*mHeight &&circleList3.get(i).getY()<=0.8*mHeight))){
                canvas.drawCircle((float) circleList3.get(i).getX(), (float) circleList3.get(i).getY(), (float) (mWeight * 0.004), mPaint2);
            }

        }
    }
    private void drawUiCircle4(Canvas canvas) {
        for (int i = 0; i < circleList4.size(); i++) {
            if(!(circleList4.get(i).getX()<=0.71*mWeight && circleList4.get(i).getX()>=0.635*mWeight)) {
                canvas.drawCircle((float) circleList4.get(i).getX(), (float) circleList4.get(i).getY(), (float) (mWeight * 0.004), mPaint2);
            }
        }
    }
    private void drawUiCircle5(Canvas canvas) {
        for (int i = 0; i < circleList5.size(); i++) {
            if(!(circleList5.get(i).getX()<=0.71*mWeight && circleList5.get(i).getX()>=0.635*mWeight)){
                canvas.drawCircle((float) circleList5.get(i).getX(), (float) circleList5.get(i).getY(), (float) (mWeight * 0.004), mPaint2);
            }
        }
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
/**
 *绘图线程
 * */
    class DrawThread extends Thread{
    @Override
    public void run() {
        super.run();
        while (threadFlag){
            synchronized (holder) { //同位锁
                Canvas canvas = holder.lockCanvas();
                if (canvas != null) {

                    canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//刷新画布
                    canvas.drawBitmap(bitmap,rect,rect,null);
                    drawUiCircle1(canvas);
                    drawUiCircle2(canvas);
                    drawUiCircle3(canvas);
                    drawUiCircle4(canvas);
                    drawUiCircle5(canvas);
                    holder.unlockCanvasAndPost(canvas);
                }
            }
            SystemClock.sleep(90);
        }
    }
    }
    /**
     *轨迹计算线程
     * */

        class CircleThread1 extends Thread {
            @Override
            public void run() {
                super.run();
                while (threadFlag){
                    synchronized (holder) {
                        if (STATUS_CIRCLE1) {
                            //当list里最后一个元素达到一个位置时，初始化一个元素，并加入到list中
                            if (circleList1.get(circleList1.size() - 1).getX() >= mWeight * 0.04) {
                                born(0);
                            }
                        }
                        loopCircle1();
                    }
                    SystemClock.sleep(timesleep);
                }

            }
        }
    class CircleThread2 extends Thread{
        @Override
        public void run() {
            super.run();
            while (threadFlag){
                synchronized (holder) {
                    if (STATUS_CIRCLE2) {
                        if (circleList2.get(circleList2.size() - 1).getX() <= mWeight * 0.958) {
                            born(1);
                        }
                    }
                    loopCircle2();
                }
                SystemClock.sleep(timesleep);
            }
        }
    }
    class CircleThread3 extends Thread{
        @Override
        public void run() {
            super.run();
            while (threadFlag){
                synchronized (holder) {
                    if (STATUS_CIRCLE3 || STATUS_CIRCLE4) {
                        if (circleList3.get(circleList3.size() - 1).getX() >= mWeight * 0.04) {
                            born(2);
                        }
                    }
                    loopCircle3();
                }
                SystemClock.sleep(timesleep);
            }
        }
    }
    class CircleThread4 extends Thread{
        @Override
        public void run() {
            super.run();
            while (threadFlag) {
                synchronized (holder) {
                    if (STATUS_CIRCLE4) {
                        if (circleList4.get(circleList4.size() - 1).getY() >= mHeight * 0.7) {
                            born(3);
                        }
                    }
                    loopCircle4();
                }
                SystemClock.sleep(timesleep);
            }
        }
    }
    class CircleThread5 extends Thread{
        @Override
        public void run() {
            super.run();
            while (threadFlag){
                synchronized (holder) {
                    if (STATUS_CIRCLE4) {
                        if (circleList5.get(circleList5.size() - 1).getX() <= mWeight * 0.76) {
                            born(4);
                        }
                    }
                    loopCircle5();
                }
                SystemClock.sleep(timesleep);
            }
        }
    }
    /////////////////////////////////////////////////////////////////////////////////////////
    /**
     *
     *轨迹计算函数
     *每一个case都是一个结点
     * 每到一个节点都改变方向标记位
     * */
    private void loopCircle1(){
        for(int i = 0 ;i<circleList1.size();i++){
            switch (circleList1.get(i).getFlag()){
                case 0:
                if(circleList1.get(i).getX()>=0.37*mWeight){
                    circleList1.get(i).setFlag(1);
                }else {
                    if(circleList1.get(i).getDric()==0){//抖动函数，上下或者左右跳动
                        circleList1.get(i).setY(circleList1.get(i).getY()+mWeight*0.002);
                        circleList1.get(i).setDric(1);
                    }else {
                        circleList1.get(i).setY(circleList1.get(i).getY()-mWeight*0.002);
                        circleList1.get(i).setDric(0);
                    }
                    circleList1.get(i).setX(circleList1.get(i).getX()+mWeight*0.005);
                }
                    break;
                case 1:
                    if(circleList1.get(i).getY()>=0.36*mHeight){
                        circleList1.get(i).setFlag(2);
                        circleList1.get(i).setColor(1);
                    }else {
                        if(circleList1.get(i).getDric()==0){
                            circleList1.get(i).setX(circleList1.get(i).getX()+mWeight*0.002);
                            circleList1.get(i).setDric(1);
                        }else {
                            circleList1.get(i).setX(circleList1.get(i).getX()-mWeight*0.002);
                            circleList1.get(i).setDric(0);
                        }
                        circleList1.get(i).setY(circleList1.get(i).getY()+mWeight*0.005);
                    }
                    break;
                case 2:
                    if(circleList1.get(i).getX()<=mWeight * 0.025){
                        circleList1.remove(i);
                    }else {
                        if(circleList1.get(i).getDric()==0){
                            circleList1.get(i).setY(circleList1.get(i).getY()+mWeight*0.002);
                            circleList1.get(i).setDric(1);
                        }else {
                            circleList1.get(i).setY(circleList1.get(i).getY()-mWeight*0.002);
                            circleList1.get(i).setDric(0);
                        }
                        circleList1.get(i).setX(circleList1.get(i).getX()-mWeight*0.005);
                    }
                    break;
            }
        }
    }


    private void loopCircle2(){
        for(int i = 0 ;i<circleList2.size();i++){
            switch (circleList2.get(i).getFlag()){
                case 0:
                    if(circleList2.get(i).getX()<=0.39*mWeight){
                        circleList2.get(i).setFlag(1);
                    }else {
                        if(circleList2.get(i).getDric()==0){
                            circleList2.get(i).setY(circleList2.get(i).getY()-mWeight*0.002);
                            circleList2.get(i).setDric(1);
                        }else {

                            circleList2.get(i).setY(circleList2.get(i).getY()+mWeight*0.002);
                            circleList2.get(i).setDric(0);
                        }
                        circleList2.get(i).setX(circleList2.get(i).getX()-mWeight*0.005);
                    }
                    break;
                case 1:
                    if(circleList2.get(i).getY()<=0.212*mHeight){
                        circleList2.get(i).setFlag(2);
                        circleList2.get(i).setColor(2);
                    }else {
                        if(circleList2.get(i).getDric()==0){
                            circleList2.get(i).setX(circleList2.get(i).getX()+mWeight*0.002);
                            circleList2.get(i).setDric(1);
                        }else {
                            circleList2.get(i).setX(circleList2.get(i).getX()-mWeight*0.002);
                            circleList2.get(i).setDric(0);
                        }
                        circleList2.get(i).setY(circleList2.get(i).getY()-mWeight*0.005);
                    }
                    break;
                case 2:
                    if(circleList2.get(i).getX()>=0.97*mWeight){
                        circleList2.remove(i);
                    }else {
                        if(circleList2.get(i).getDric()==0){
                            circleList2.get(i).setY(circleList2.get(i).getY()+mWeight*0.002);
                            circleList2.get(i).setDric(1);
                        }else {
                            circleList2.get(i).setY(circleList2.get(i).getY()-mWeight*0.002);
                            circleList2.get(i).setDric(0);
                        }
                        circleList2.get(i).setX(circleList2.get(i).getX()+mWeight*0.005);
                    }
                    break;
            }
        }
    }
    private void loopCircle3(){
        for(int i = 0 ;i<circleList3.size();i++) {
            switch (circleList3.get(i).getFlag()){
                case 0:
                    if(circleList3.get(i).getX()>=0.224*mWeight){
                        circleList3.get(i).setFlag(1);
                    }else {
                        if(circleList3.get(i).getDric()==0){
                            circleList3.get(i).setY(circleList3.get(i).getY()+mWeight*0.002);
                            circleList3.get(i).setDric(1);
                        }else {
                            circleList3.get(i).setY(circleList3.get(i).getY()-mWeight*0.002);
                            circleList3.get(i).setDric(0);
                        }
                        circleList3.get(i).setX(circleList3.get(i).getX()+mWeight*0.005);
                    }
                    break;
                case 1:
                    if(circleList3.get(i).getY()>=0.67 *mHeight){
                        circleList3.get(i).setFlag(2);
                        circleList3.get(i).setColor(1);
                    }else {
                        if(circleList3.get(i).getDric()==0){
                            circleList3.get(i).setX(circleList3.get(i).getX()+mWeight*0.002);
                            circleList3.get(i).setDric(1);
                        }else {
                            circleList3.get(i).setX(circleList3.get(i).getX()-mWeight*0.002);
                            circleList3.get(i).setDric(0);
                        }
                        circleList3.get(i).setY(circleList3.get(i).getY()+mWeight*0.005);
                    }
                    break;
                case 2:
                    if(circleList3.get(i).getX()>=0.555*mWeight){
                        if (isOpne(3) && !isOpne(4)) {
                            circleList3.get(i).setFlag(3);
                        }else if(isOpne(4) && !isOpne(3)){
                            circleList3.remove(i);
                            openCircle(10);
                        }else if(isOpne(4) && isOpne(3)){
                            circleList3.get(i).setFlag(3);
                            openCircle(10);
                        }
                    }else {
                        if(circleList3.get(i).getDric()==0){
                            circleList3.get(i).setY(circleList3.get(i).getY()+mWeight*0.002);
                            circleList3.get(i).setDric(1);
                        }else {
                            circleList3.get(i).setY(circleList3.get(i).getY()-mWeight*0.002);
                            circleList3.get(i).setDric(0);
                        }
                        circleList3.get(i).setX(circleList3.get(i).getX()+mWeight*0.005);
                    }
                    break;
                case 3:
                        if(circleList3.get(i).getX()>=0.77*mWeight){
                            circleList3.get(i).setFlag(4);
                        }else {
                            if(circleList3.get(i).getDric()==0){
                                circleList3.get(i).setY(circleList3.get(i).getY()+mWeight*0.002);
                                circleList3.get(i).setDric(1);
                            }else {
                                circleList3.get(i).setY(circleList3.get(i).getY()-mWeight*0.002);
                                circleList3.get(i).setDric(0);
                            }
                            circleList3.get(i).setX(circleList3.get(i).getX()+mWeight*0.005);
                        }
                    break;
                case 4:
                    if(circleList3.get(i).getY()<=0.512 *mHeight){
                        if(isOpne(2) && !isOpne(5)){
                            circleList3.get(i).setFlag(5);
                        }else if(isOpne(5) && !isOpne(2)){
//                            circleList3.remove(i);
                            openCircle(6);
                        }else if(isOpne(2) && isOpne(5)){
                            circleList3.get(i).setFlag(5);
                            openCircle(6);
                        }
                    }else {
                        if(circleList3.get(i).getDric()==0){
                            circleList3.get(i).setX(circleList3.get(i).getX()+mWeight*0.002);
                            circleList3.get(i).setDric(1);
                        }else {
                            circleList3.get(i).setX(circleList3.get(i).getX()-mWeight*0.002);
                            circleList3.get(i).setDric(0);
                        }
                        circleList3.get(i).setY(circleList3.get(i).getY()-mWeight*0.005);
                    }
                    break;
                case 5:
                    if(circleList3.get(i).getY()<=0.38 *mHeight){
                        circleList3.remove(i);
                    }else {
                        if(circleList3.get(i).getDric()==0){
                            circleList3.get(i).setX(circleList3.get(i).getX()+mWeight*0.002);
                            circleList3.get(i).setDric(1);
                        }else {
                            circleList3.get(i).setX(circleList3.get(i).getX()-mWeight*0.002);
                            circleList3.get(i).setDric(0);
                        }
                        circleList3.get(i).setY(circleList3.get(i).getY()-mWeight*0.005);
                    }
                    break;
            }
        }
    }

    private void loopCircle4(){
        for(int i = 0 ;i<circleList4.size();i++){
            switch (circleList4.get(i).getFlag()){
                case 0:
                    if(circleList4.get(i).getY()>=0.812 *mHeight){
                        circleList4.get(i).setFlag(1);
//                        circleList4.get(i).setColor(1);
                    }else {
                        if(circleList4.get(i).getDric()==0){
                            circleList4.get(i).setX(circleList4.get(i).getX()+mWeight*0.002);
                            circleList4.get(i).setDric(1);
                        }else {
                            circleList4.get(i).setX(circleList4.get(i).getX()-mWeight*0.002);
                            circleList4.get(i).setDric(0);
                        }
                        circleList4.get(i).setY(circleList4.get(i).getY()+mWeight*0.005);
                    }
                    break;
                case 1:
                    if(circleList4.get(i).getX()>=0.77*mWeight){
                        if(isOpne(3)){
                            circleList4.get(i).setFlag(2);
                        }else {
                            circleList4.get(i).setFlag(3);
                        }

                    }else {
                        if(circleList4.get(i).getDric()==0){
                            circleList4.get(i).setY(circleList4.get(i).getY()+mWeight*0.002);
                            circleList4.get(i).setDric(1);
                        }else {
                            circleList4.get(i).setY(circleList4.get(i).getY()-mWeight*0.002);
                            circleList4.get(i).setDric(0);
                        }
                        circleList4.get(i).setX(circleList4.get(i).getX()+mWeight*0.005);
                    }
                    break;
                case 2:
                    if(circleList4.get(i).getY()<=0.7 *mHeight){
                        circleList4.remove(i);
                    }else {
                        if(circleList4.get(i).getDric()==0){
                            circleList4.get(i).setX(circleList4.get(i).getX()+mWeight*0.002);
                            circleList4.get(i).setDric(1);
                        }else {
                            circleList4.get(i).setX(circleList4.get(i).getX()-mWeight*0.002);
                            circleList4.get(i).setDric(0);
                        }
                        circleList4.get(i).setY(circleList4.get(i).getY()-mWeight*0.005);
                    }
                    break;
                case 3:
                    if(circleList4.get(i).getY()<=0.512 *mHeight){
                        if(isOpne(2) && !isOpne(5)){//假如管道2打开，5关闭
                            circleList4.get(i).setFlag(4);
                        }else if(isOpne(5) && !isOpne(2)){//假如管道5打开，2关闭
                            circleList4.remove(i);
                            openCircle(6);
                        }else if(isOpne(2) && isOpne(5)){//假如管道2、5都打开了
                            circleList4.get(i).setFlag(4);
                            openCircle(6);
                        }
                    }else {
                        if(circleList4.get(i).getDric()==0){
                            circleList4.get(i).setX(circleList4.get(i).getX()+mWeight*0.002);
                            circleList4.get(i).setDric(1);
                        }else {
                            circleList4.get(i).setX(circleList4.get(i).getX()-mWeight*0.002);
                            circleList4.get(i).setDric(0);
                        }
                        circleList4.get(i).setY(circleList4.get(i).getY()-mWeight*0.005);
                    }
                    break;

                case 4:

                    if(circleList4.get(i).getY()<=0.38 *mHeight){
                        circleList4.remove(i);
                    }else {
                        if(circleList4.get(i).getDric()==0){
                            circleList4.get(i).setX(circleList4.get(i).getX()+mWeight*0.002);
                            circleList4.get(i).setDric(1);
                        }else {
                            circleList4.get(i).setX(circleList4.get(i).getX()-mWeight*0.002);
                            circleList4.get(i).setDric(0);
                        }
                        circleList4.get(i).setY(circleList4.get(i).getY()-mWeight*0.005);
                    }
                    break;
            }
        }
    }

    private void loopCircle5(){
        for(int i = 0 ;i<circleList5.size();i++){
            switch (circleList5.get(i).getFlag()){
                case 0:
                    if(circleList5.get(i).getX()<=0.565*mWeight){
                            circleList5.get(i).setFlag(1);
                    }else {
                        if(circleList5.get(i).getDric()==0){
                            circleList5.get(i).setY(circleList5.get(i).getY()+mWeight*0.002);
                            circleList5.get(i).setDric(1);
                        }else {
                            circleList5.get(i).setY(circleList5.get(i).getY()-mWeight*0.002);
                            circleList5.get(i).setDric(0);
                        }
                        circleList5.get(i).setX(circleList5.get(i).getX()-mWeight*0.005);
                    }
                    break;
                case 1:
                    if(circleList5.get(i).getY()<=0.38 *mHeight){
                        circleList5.remove(i);
                    }else {
                        if(circleList5.get(i).getDric()==0){
                            circleList5.get(i).setX(circleList5.get(i).getX()+mWeight*0.002);
                            circleList5.get(i).setDric(1);
                        }else {
                            circleList5.get(i).setX(circleList5.get(i).getX()-mWeight*0.002);
                            circleList5.get(i).setDric(0);
                        }
                        circleList5.get(i).setY(circleList5.get(i).getY()-mWeight*0.005);
                    }
                    break;
            }
        }
    }
///////////////////////////////////////////////////////////////////////////////////////////
    /**
     *获取运行状态
     * true为运行，一直产生元素
     * false为停止，不在初始化起始点，但是线程仍然执行
     * */

    public boolean isOpne(int circleNumber){
        switch (circleNumber){
            case 1:
                return STATUS_CIRCLE1;
            case 2:
                return STATUS_CIRCLE2;
            case 3:
                return STATUS_CIRCLE3;
            case 4:
                return STATUS_CIRCLE4;
            case 5:
                return STATUS_CIRCLE5;
            case 6:
                return STATUS_CIRCLE5;
            case 7:
                return STATUS_CIRCLE5;
            case 8:
                return STATUS_CIRCLE5;
            case 9:
                return STATUS_CIRCLE5;
            default:
                return false;
        }
    }

    /**
     *运行开启
     * 负责开启线程，初始化元素，打开运行标记位
     *
     * */
    public void openCircle(int circleNumber){
        switch (circleNumber){
            case 1:
                if (!thread1.isAlive()){
                    thread1.start();
                }

                if (!STATUS_CIRCLE1) {
                    born(0);
                    STATUS_CIRCLE1 = true;
                }
                break;
            case 2:
                if(!thread2.isAlive()){

                    thread2.start();
                }
                if (!STATUS_CIRCLE2) {
                    born(1);
                    STATUS_CIRCLE2=true;
                }

                break;
            case 3:
                if(!thread3.isAlive()){

                    thread3.start();
                }
                if (!STATUS_CIRCLE3) {
                    born(2);
                    STATUS_CIRCLE3=true;
                }
                break;
            case 4:
                if(!thread3.isAlive()){
                    born(2);
                    thread3.start();
                }
                STATUS_CIRCLE4=true;
                break;
            case 5:
                    STATUS_CIRCLE5=true;
                break;
            case 6:
                if(!thread5.isAlive()){
                    born(4);
                    thread5.start();
                }
                break;
            case 10:
                if(!thread4.isAlive()){
                    born(3);
                    thread4.start();
                }
        }

    }

    /**
     *停止
     * 负责关闭运行标记位，使元素不再产生
     *
     * */
    public void  stopCircle(int circleNumber){
        switch (circleNumber){
            case 1:
                STATUS_CIRCLE1=false;
                break;
            case 2:
                STATUS_CIRCLE2=false;
                break;
            case 3:
                STATUS_CIRCLE3=false;
                break;
            case 4:
                STATUS_CIRCLE4=false;
                break;
            case 5:
                STATUS_CIRCLE5=false;
                break;
        }
    }

    /**
     *总线程运行开启
     * 负责开启线程，必须开启才能启动所有线程
     *
     * */
    public void start(){
        if(!drawThread.isAlive()){
            drawThread.start();
            threadFlag=true;
        }

    }
    /**
     *总线程运行关闭
     * 负责关闭线程，关闭以后线程全部停止
     *
     * */
    public void end(){
        threadFlag=false;
    }


}
