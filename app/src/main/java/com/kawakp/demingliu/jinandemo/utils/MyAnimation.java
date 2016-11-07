package com.kawakp.demingliu.jinandemo.utils;

import android.content.Context;
import android.graphics.Bitmap;
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
 * Created by deming.liu on 2016/9/2.
 */
public class MyAnimation extends SurfaceView implements SurfaceHolder.Callback {
    private double mWeight;//全屏宽
    private double mHeight;//全屏长
    private SurfaceHolder holder;

    private Bitmap bitmap; //背景图
    private  Rect rect;

    private boolean STATUS_CIRCLE1=false;//运行标记位
    private boolean STATUS_CIRCLE2=false;
    private boolean STATUS_CIRCLE3=false;
    private boolean STATUS_CIRCLE4=false;
    private boolean STATUS_CIRCLE5=false;
    private boolean STATUS_CIRCLE6=false;
    private boolean STATUS_CIRCLE7=false;
    private boolean STATUS_CIRCLE8=false;

    private boolean b1 = false;//表示1号循环泵是否打开
    private boolean b2 = false;//表示2号循环泵是否打开
    private boolean b3 = false;//表示1号补水泵是否打开
    private boolean b4 = false;//表示1号补水泵是否打开

    private boolean threadFlag = true;//线程循环标记位
    private int timesleep = 50;//线程休眠时间

    private DrawThread drawThread;///画图线程
    private CircleThread1 thread1;//运行线程
    private CircleThread2 thread2;
    private CircleThread3 thread3;
    private CircleThread4 thread4; //2号补水泵
    private CircleThread5 thread5; //1.2号一起
    private CircleThread6 thread6;
    private CircleThread7 thread7; // 2号循环泵开
    private CircleThread8 thread8;

    private List<Circle> circleList1;
    private List<Circle> circleList2;
    private List<Circle> circleList3;
    private List<Circle> circleList4;
    private List<Circle> circleList5;
    private List<Circle> circleList6;
    private List<Circle> circleList7;
    private List<Circle> circleList8;

    //画笔
    private Paint mPaint1;
    private Paint mPaint2;
    private Paint mPaint3;
    //圆
    private Circle circle1;
    private Circle circle2;
    private Circle circle3;
    private Circle circle4;
    private Circle circle5;
    private Circle circle6;
    private Circle circle7;
    private Circle circle8;

    public MyAnimation(Context context) {
        this(context,null);
    }

    public MyAnimation(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyAnimation(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initData();
    }

    public void initData() {
        drawThread = new DrawThread();
        thread1 = new CircleThread1();
        thread2 = new CircleThread2();
        thread3 = new CircleThread3();
        thread4 = new CircleThread4();
        thread5 = new CircleThread5();
        thread6 = new CircleThread6();
        thread7= new CircleThread7();
        thread8= new CircleThread8();

        DisplayMetrics dm2 = getResources().getDisplayMetrics();//获取全屏长款尺寸
        mHeight = dm2.heightPixels;
        mWeight = dm2.widthPixels;
//        setZOrderOnTop(true);//控件置顶
//        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        holder = this.getHolder();
        holder.addCallback(this);
        bitmap = ((BitmapDrawable)getResources().getDrawable(R.drawable.animotion_back)).getBitmap();
        rect = new Rect(0,0,(int)mWeight,(int)mHeight);

        circleList1 = new ArrayList<>();
        circleList2 = new ArrayList<>();
        circleList3 = new ArrayList<>();
        circleList4 = new ArrayList<>();
        circleList5 = new ArrayList<>();
        circleList6 = new ArrayList<>();
        circleList7 = new ArrayList<>();
        circleList8 = new ArrayList<>();

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



    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }

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
                        drawUiCircle6(canvas);
                        drawUiCircle7(canvas);
                        drawUiCircle8(canvas);

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
                            born(1);
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
                            born(2);
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
            while (threadFlag) {
                synchronized (holder) {
                    if (STATUS_CIRCLE3) {
                        if (circleList3.get(circleList3.size() - 1).getX() >= mWeight * 0.04) {
                            born(3);
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
                        if (circleList4.get(circleList4.size() - 1).getX() >= mWeight * 0.04) {
                            born(4);
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
            while (threadFlag) {
                synchronized (holder) {
                    if (STATUS_CIRCLE5) {
                        if (circleList5.get(circleList5.size() - 1).getX() >= mWeight * 0.04) {
                            born(5);
                        }
                    }
                    loopCircle5();
                }
                SystemClock.sleep(timesleep);
            }
        }
    }

    class CircleThread6 extends Thread{
        @Override
        public void run() {
            super.run();
            while (threadFlag) {
                synchronized (holder) {
                    if (STATUS_CIRCLE6) {
                        if (circleList6.get(circleList6.size() - 1).getY() >= mHeight * 0.7) {
                            born(6);
                        }
                    }
                    loopCircle6();
                }
                SystemClock.sleep(timesleep);
            }
        }
    }
    class CircleThread7 extends Thread{
        @Override
        public void run() {
            super.run();
            while (threadFlag) {
                synchronized (holder) {
                    if (STATUS_CIRCLE7) {
                        if (circleList7.get(circleList7.size() - 1).getY() >= mHeight * 0.4) {
                            born(7);
                        }
                    }
                    loopCircle7();
                }
                SystemClock.sleep(timesleep);
            }
        }
    }

    class CircleThread8 extends Thread{
        @Override
        public void run() {
            super.run();
            while (threadFlag) {
                synchronized (holder) {
                    if (STATUS_CIRCLE8) {

                        if (circleList8.get(circleList8.size() - 1).getX() <= mWeight * 0.770) {
                            born(8);
                        }
                    }
                    loopCircle8();
                }
                SystemClock.sleep(timesleep);
            }
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

    //2号补水泵
    private void drawUiCircle4(Canvas canvas) {
        for (int i = 0; i < circleList4.size(); i++) {
            if(!((circleList4.get(i).getX()<=0.71*mWeight && circleList4.get(i).getX()>=0.635*mWeight) || (circleList4.get(i).getX()<=0.28*mWeight && circleList4.get(i).getX()>=0.22*mWeight && circleList4.get(i).getY()>=0.59*mHeight &&circleList4.get(i).getY()<=0.8*mHeight))){
                canvas.drawCircle((float) circleList4.get(i).getX(), (float) circleList4.get(i).getY(), (float) (mWeight * 0.004), mPaint2);
            }
        }
    }

    private void drawUiCircle5(Canvas canvas) {
        for (int i = 0; i < circleList5.size(); i++) {
            if(!((circleList5.get(i).getX()<=0.71*mWeight && circleList5.get(i).getX()>=0.635*mWeight) || (circleList5.get(i).getX()<=0.28*mWeight && circleList5.get(i).getX()>=0.22*mWeight && circleList5.get(i).getY()>=0.59*mHeight &&circleList5.get(i).getY()<=0.8*mHeight))){
                canvas.drawCircle((float) circleList5.get(i).getX(), (float) circleList5.get(i).getY(), (float) (mWeight * 0.004), mPaint2);
            }
        }
    }

    private void drawUiCircle6(Canvas canvas) {
        for (int i = 0; i < circleList6.size(); i++) {
            if(!(circleList6.get(i).getX()<=0.71*mWeight && circleList6.get(i).getX()>=0.635*mWeight)) {
                canvas.drawCircle((float) circleList6.get(i).getX(), (float) circleList6.get(i).getY(), (float) (mWeight * 0.004), mPaint2);
            }
        }
    }
    private void drawUiCircle7(Canvas canvas) {
        for (int i = 0; i < circleList7.size(); i++) {
            if(!(circleList7.get(i).getX()<=0.71*mWeight && circleList7.get(i).getX()>=0.635*mWeight)) {
                canvas.drawCircle((float) circleList7.get(i).getX(), (float) circleList7.get(i).getY(), (float) (mWeight * 0.004), mPaint2);
            }
        }
    }

    private void drawUiCircle8(Canvas canvas) {
        for (int i = 0; i < circleList8.size(); i++) {
            if(!(( (circleList8.get(i).getX()<=0.71*mWeight && circleList8.get(i).getX()>=0.635*mWeight)) && circleList8.get(i).getY()<=0.5*mHeight && circleList8.get(i).getY()>=0.3*mHeight )){
                if(circleList8.get(i).getColor()==1) {
                    canvas.drawCircle((float) circleList8.get(i).getX(), (float) circleList8.get(i).getY(), (float) (mWeight * 0.004), mPaint2);
                }else if(circleList8.get(i).getColor()==2){
                    canvas.drawCircle((float) circleList8.get(i).getX(), (float) circleList8.get(i).getY(), (float) (mWeight * 0.004), mPaint3);
                }
            }
        }
    }



    /**
     * 初始化起始点元素
     * */

    public void born(int i) {
        switch (i) {
            case 1:
                circle1 = new Circle();
                circle1.setX(mWeight * 0.025);
                circle1.setY(mHeight * 0.208);
                circle1.setFlag(0);
                circle1.setLINE(true);
                circle1.setColor(0);
                circleList1.add(circle1);
                break;
            case 2:
                circle2 = new Circle();
                circle2.setX(mWeight * 0.970);
                circle2.setY(mHeight * 0.365);
                circle2.setFlag(0);
                circle2.setLINE(true);
                circle2.setColor(1);
                circleList2.add(circle2);
                break;
            case 3:
                circle3 = new Circle();
                circle3.setX(mWeight * 0.025);
                circle3.setY(mHeight * 0.505);
                circle3.setFlag(0);
                circle3.setLINE(true);
                circle3.setColor(1);
                circleList3.add(circle3);
                break;
            case 4:
                circle4 = new Circle();
                circle4.setX(mWeight * 0.025);
                circle4.setY(mHeight * 0.505);
                circle4.setFlag(0);
                circle4.setLINE(true);
                circle4.setColor(1);
                circleList4.add(circle4);
                break;
            case 5:
                circle5 = new Circle();
                circle5.setX(mWeight * 0.025);
                circle5.setY(mHeight * 0.505);
                circle5.setFlag(0);
                circle5.setLINE(true);
                circle5.setColor(1);
                circleList5.add(circle5);
                break;
            case 6:
                circle6 = new Circle();
                circle6.setX(mWeight * 0.555);
                circle6.setY(mHeight * 0.68);
                circle6.setFlag(0);
                circle6.setLINE(true);
                circle6.setColor(1);
                circleList6.add(circle6);
                break;
            case 7:
                circle7 = new Circle();
                circle7.setX(mWeight * 0.775);
                circle7.setY(mHeight * 0.365);
                circle7.setFlag(0);
                circle7.setLINE(true);
                circle7.setColor(1);
                circleList7.add(circle7);
                break;
            case 8:
                circle8 = new Circle();
                circle8.setX(mWeight * 0.775);
                circle8.setY(mHeight * 0.5);
                circle8.setFlag(0);
                circle8.setLINE(true);
                circle8.setColor(1);
                circleList8.add(circle8);
                break;
        }
    }


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
                    if(circleList2.get(i).getX()<=0.775*mWeight){
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
                    if (b1 && !b2){  //1号循环泵开
                        if(circleList2.get(i).getX()<=0.39*mWeight){
                            circleList2.get(i).setFlag(2);
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
                    }else if (!b1 && b2){  //二号循环泵开

                        openCircle(7);


                      /*  if (circleList2.get(i).getY() >= 0.5*mHeight){
                            circleList2.get(i).setFlag(4);
                        }else {
                            circleList2.get(i).setY(circleList2.get(i).getY() + mWeight * 0.005);
                        }*/
                    }
                    else {  //1号2号循环泵都开
                        if(circleList2.get(i).getX()<=0.39*mWeight){
                            circleList2.get(i).setFlag(2);
                            openCircle(7);
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
                    }
                    break;
                case 2:
                    if(circleList2.get(i).getY()<=0.212*mHeight){
                        circleList2.get(i).setFlag(3);
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
                case 3:
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
                case 4:
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
                    if (circleList3.get(i).getX() >= 0.555*mWeight){

                        circleList3.get(i).setFlag(3); // 通过1号补水泵

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

                        circleList3.get(i).setFlag(5);

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

                    if (b1 && !b2){  //1号循环泵开
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
                    }else if (!b1 && b2){  //2号循环泵开
                        if (circleList3.get(i).getY() <= 0.515*mHeight){
                            circleList3.get(i).setFlag(6);

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
                    }else {  //1 2 号同时开

                    }
                    break;
                case 6:
                    if (circleList3.get(i).getX() <=0.555*mWeight ){
                        circleList3.get(i).setFlag(7);
                    }else {
                        if(circleList3.get(i).getDric()==0){
                            circleList3.get(i).setX(circleList3.get(i).getX()+mWeight*0.002);
                            circleList3.get(i).setDric(1);
                        }else {
                            circleList3.get(i).setX(circleList3.get(i).getX()-mWeight*0.002);
                            circleList3.get(i).setDric(0);
                        }
                        circleList3.get(i).setX(circleList3.get(i).getX()-mWeight*0.005);
                    }
                    break;
                case 7:
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
                    if(circleList4.get(i).getX()>=0.224*mWeight){
                        circleList4.get(i).setFlag(1);
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
                case 1:
                    if(circleList4.get(i).getY()>=0.67 *mHeight){
                        circleList4.get(i).setFlag(2);
                        circleList4.get(i).setColor(1);
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
                case 2:
                    if (circleList4.get(i).getX() >= 0.555*mWeight){

                        circleList4.get(i).setFlag(3); // 通过1号补水泵

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
                case 3:
                    if(circleList4.get(i).getY()>=0.812 *mHeight){
                        circleList4.get(i).setFlag(4);
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
                case 4:
                    if(circleList4.get(i).getX()>=0.77*mWeight){
                        circleList4.get(i).setFlag(5);

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
                case 5:

                    if (b1 && !b2){  //1号循环泵开
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
                    }else if (!b1 && b2){  //2号循环泵开
                        if (circleList4.get(i).getY() <= 0.515*mHeight){
                            circleList4.get(i).setFlag(6);
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
                    }else {  //1 2 号同时开

                    }
                    break;
                case 6:
                    if (circleList4.get(i).getX() <=0.555*mWeight ){
                        circleList4.get(i).setFlag(7);
                    }else {
                        if(circleList4.get(i).getDric()==0){
                            circleList4.get(i).setX(circleList4.get(i).getX()+mWeight*0.002);
                            circleList4.get(i).setDric(1);
                        }else {
                            circleList4.get(i).setX(circleList4.get(i).getX()-mWeight*0.002);
                            circleList4.get(i).setDric(0);
                        }
                        circleList4.get(i).setX(circleList4.get(i).getX()-mWeight*0.005);
                    }
                    break;
                case 7:
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
                    if(circleList5.get(i).getX()>=0.224*mWeight){
                        circleList5.get(i).setFlag(1);
                    }else {
                        if(circleList5.get(i).getDric()==0){
                            circleList5.get(i).setY(circleList5.get(i).getY()+mWeight*0.002);
                            circleList5.get(i).setDric(1);
                        }else {
                            circleList5.get(i).setY(circleList5.get(i).getY()-mWeight*0.002);
                            circleList5.get(i).setDric(0);
                        }
                        circleList5.get(i).setX(circleList5.get(i).getX()+mWeight*0.005);
                    }
                    break;
                case 1:
                    if(circleList5.get(i).getY()>=0.67 *mHeight){
                        circleList5.get(i).setFlag(2);
                        circleList5.get(i).setColor(1);
                    }else {
                        if(circleList5.get(i).getDric()==0){
                            circleList5.get(i).setX(circleList5.get(i).getX()+mWeight*0.002);
                            circleList5.get(i).setDric(1);
                        }else {
                            circleList5.get(i).setX(circleList5.get(i).getX()-mWeight*0.002);
                            circleList5.get(i).setDric(0);
                        }
                        circleList5.get(i).setY(circleList5.get(i).getY()+mWeight*0.005);
                    }
                    break;
                case 2:
                    if (circleList5.get(i).getX() >= 0.555*mWeight){
                        circleList5.get(i).setFlag(3); // 通过1号补水泵
                        openCircle(6);
                    }else {
                        if(circleList5.get(i).getDric()==0){
                            circleList5.get(i).setY(circleList5.get(i).getY()+mWeight*0.002);
                            circleList5.get(i).setDric(1);
                        }else {
                            circleList5.get(i).setY(circleList5.get(i).getY()-mWeight*0.002);
                            circleList5.get(i).setDric(0);
                        }
                        circleList5.get(i).setX(circleList5.get(i).getX()+mWeight*0.005);
                    }
                    break;
                case 3:
                    if(circleList5.get(i).getX()>=0.77*mWeight){
                        circleList5.get(i).setFlag(4);
                    }else {
                        if(circleList5.get(i).getDric()==0){
                            circleList5.get(i).setY(circleList5.get(i).getY()+mWeight*0.002);
                            circleList5.get(i).setDric(1);
                        }else {
                            circleList5.get(i).setY(circleList5.get(i).getY()-mWeight*0.002);
                            circleList5.get(i).setDric(0);
                        }
                        circleList5.get(i).setX(circleList5.get(i).getX()+mWeight*0.005);
                    }
                    break;
                case 4:
                    if(circleList5.get(i).getY()<=0.512 *mHeight){
                        circleList5.get(i).setFlag(5);

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
                case 5:

                    if (b1 && !b2){  //1号循环泵开
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
                    }else if (!b1 && b2){  //2号循环泵开
                        if (circleList5.get(i).getY() <= 0.515*mHeight){
                            circleList5.get(i).setFlag(6);
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
                    }else {  //1 2 号同时开

                    }
                    break;
                case 6:
                    if (circleList5.get(i).getX() <=0.555*mWeight ){
                        circleList5.get(i).setFlag(7);
                    }else {

                        if(circleList5.get(i).getDric()==0){
                            circleList5.get(i).setX(circleList5.get(i).getX()+mWeight*0.002);
                            circleList5.get(i).setDric(1);
                        }else {
                            circleList5.get(i).setX(circleList5.get(i).getX()-mWeight*0.002);
                            circleList5.get(i).setDric(0);
                        }
                        circleList5.get(i).setX(circleList5.get(i).getX()-mWeight*0.005);
                    }
                    break;
                case 7:
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

    private void loopCircle6(){
        for(int i = 0 ;i<circleList6.size();i++){
            switch (circleList6.get(i).getFlag()){
                case 0:
                    if(circleList6.get(i).getY()>=0.812 *mHeight){
                        circleList6.get(i).setFlag(1);
//                        circleList4.get(i).setColor(1);
                    }else {
                        if(circleList6.get(i).getDric()==0){
                            circleList6.get(i).setX(circleList6.get(i).getX()+mWeight*0.002);
                            circleList6.get(i).setDric(1);
                        }else {
                            circleList6.get(i).setX(circleList6.get(i).getX()-mWeight*0.002);
                            circleList6.get(i).setDric(0);
                        }
                        circleList6.get(i).setY(circleList6.get(i).getY()+mWeight*0.005);
                    }
                    break;
                case 1:
                    if(circleList6.get(i).getX()>=0.77*mWeight){

                        circleList6.get(i).setFlag(2);


                    }else {
                        if(circleList6.get(i).getDric()==0){
                            circleList6.get(i).setY(circleList6.get(i).getY()+mWeight*0.002);
                            circleList6.get(i).setDric(1);
                        }else {
                            circleList6.get(i).setY(circleList6.get(i).getY()-mWeight*0.002);
                            circleList6.get(i).setDric(0);
                        }
                        circleList6.get(i).setX(circleList6.get(i).getX()+mWeight*0.005);
                    }
                    break;
                case 2:
                    if(circleList6.get(i).getY()<=0.7 *mHeight){
                        circleList6.remove(i);
                    }else {
                        if(circleList6.get(i).getDric()==0){
                            circleList6.get(i).setX(circleList6.get(i).getX()+mWeight*0.002);
                            circleList6.get(i).setDric(1);
                        }else {
                            circleList6.get(i).setX(circleList6.get(i).getX()-mWeight*0.002);
                            circleList6.get(i).setDric(0);
                        }
                        circleList6.get(i).setY(circleList6.get(i).getY()-mWeight*0.005);
                    }
                    break;
                case 3:
                    if(circleList6.get(i).getY()<=0.512 *mHeight){

                        circleList6.get(i).setFlag(4);

                    }else {
                        if(circleList6.get(i).getDric()==0){
                            circleList6.get(i).setX(circleList4.get(i).getX()+mWeight*0.002);
                            circleList6.get(i).setDric(1);
                        }else {
                            circleList6.get(i).setX(circleList4.get(i).getX()-mWeight*0.002);
                            circleList6.get(i).setDric(0);
                        }
                        circleList6.get(i).setY(circleList6.get(i).getY()-mWeight*0.005);
                    }
                    break;

                case 4:

                    if(circleList6.get(i).getY()<=0.38 *mHeight){
                        circleList6.remove(i);
                    }else {
                        if(circleList6.get(i).getDric()==0){
                            circleList6.get(i).setX(circleList6.get(i).getX()+mWeight*0.002);
                            circleList6.get(i).setDric(1);
                        }else {
                            circleList6.get(i).setX(circleList6.get(i).getX()-mWeight*0.002);
                            circleList6.get(i).setDric(0);
                        }
                        circleList6.get(i).setY(circleList6.get(i).getY()-mWeight*0.005);
                    }
                    break;
            }
        }



    }
    private void loopCircle7(){
        for(int i = 0 ;i<circleList7.size();i++){
            switch (circleList7.get(i).getFlag()){
                case 0:
                    if (circleList7.get(i).getY() >= 0.5*mHeight){
                        circleList7.get(i).setFlag(1);

                    }else {
                        circleList7.get(i).setY(circleList7.get(i).getY() + mWeight * 0.005);
                    }
                    break;
                case 1:
                    if (b2){
                        if (circleList7.get(i).getY() >= 0.49*mHeight) {
                            openCircle(8);
                            circleList7.remove(i);
                        }
                    }
                    break;
            }
        }
    }

    private void loopCircle8(){
        for(int i = 0 ;i<circleList8.size();i++){
            switch (circleList8.get(i).getFlag()){
                case 0:
                    if (circleList8.get(i).getY() <= 0.515*mHeight){
                        circleList8.get(i).setFlag(1);
                    }else {
                        if(circleList8.get(i).getDric()==0){
                            circleList8.get(i).setX(circleList8.get(i).getX()+mWeight*0.002);
                            circleList8.get(i).setDric(1);
                        }else {
                            circleList8.get(i).setX(circleList8.get(i).getX()-mWeight*0.002);
                            circleList8.get(i).setDric(0);
                        }
                        circleList8.get(i).setY(circleList8.get(i).getY()-mWeight*0.005);
                    }
                    break;
                case 1:
                    if (circleList8.get(i).getX() <=0.555*mWeight ){
                        circleList8.get(i).setFlag(2);
                    }else {
                        if(circleList8.get(i).getDric()==0){
                            circleList8.get(i).setX(circleList8.get(i).getX()+mWeight*0.002);
                            circleList8.get(i).setDric(1);
                        }else {
                            circleList8.get(i).setX(circleList8.get(i).getX()-mWeight*0.002);
                            circleList8.get(i).setDric(0);
                        }
                        circleList8.get(i).setX(circleList8.get(i).getX()-mWeight*0.005);
                    }
                    break;
                case 2:
                    if(circleList8.get(i).getY()<=0.38 *mHeight){
                        circleList8.get(i).setFlag(3);
                    }else {
                        if(circleList8.get(i).getDric()==0){
                            circleList8.get(i).setX(circleList8.get(i).getX()+mWeight*0.002);
                            circleList8.get(i).setDric(1);
                        }else {
                            circleList8.get(i).setX(circleList8.get(i).getX()-mWeight*0.002);
                            circleList8.get(i).setDric(0);
                        }
                        circleList8.get(i).setY(circleList8.get(i).getY()-mWeight*0.005);
                    }
                    break;
                case 3:
                    if (b1){
                        circleList8.remove(i);
                    }else {
                        if(circleList8.get(i).getX()<=0.39*mWeight){
                            circleList8.get(i).setFlag(4);
                        }else {
                            if(circleList8.get(i).getDric()==0){
                                circleList8.get(i).setY(circleList2.get(i).getY()-mWeight*0.002);
                                circleList8.get(i).setDric(1);
                            }else {

                                circleList8.get(i).setY(circleList8.get(i).getY()+mWeight*0.002);
                                circleList8.get(i).setDric(0);
                            }
                            circleList8.get(i).setX(circleList8.get(i).getX()-mWeight*0.005);
                        }
                    }
                    break;
                case 4:
                    if(circleList8.get(i).getY()<=0.212*mHeight){
                        circleList8.get(i).setFlag(5);
                        circleList8.get(i).setColor(2);
                    }else {
                        if(circleList8.get(i).getDric()==0){
                            circleList8.get(i).setX(circleList8.get(i).getX()+mWeight*0.002);
                            circleList8.get(i).setDric(1);
                        }else {
                            circleList8.get(i).setX(circleList8.get(i).getX()-mWeight*0.002);
                            circleList8.get(i).setDric(0);
                        }
                        circleList8.get(i).setY(circleList8.get(i).getY()-mWeight*0.005);
                    }
                    break;
                case 5:
                    if(circleList8.get(i).getX()>=0.97*mWeight){
                        circleList8.remove(i);
                    }else {
                        if(circleList8.get(i).getDric()==0){
                            circleList8.get(i).setY(circleList8.get(i).getY()+mWeight*0.002);
                            circleList8.get(i).setDric(1);
                        }else {
                            circleList8.get(i).setY(circleList8.get(i).getY()-mWeight*0.002);
                            circleList8.get(i).setDric(0);
                        }
                        circleList8.get(i).setX(circleList8.get(i).getX()+mWeight*0.005);
                    }
                    break;
            }
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
     *运行开启
     * 负责开启线程，初始化元素，打开运行标记位
     *我觉得有点没搞清楚，我不是压了半个月工资吗，那半个月的工资应该是转正了的工资，那这个月不应该是按照一个月来发吗，
     * */
    public void openCircle(int circleNumber){
        switch (circleNumber){
            case 1:  // 1号循环泵开
                if (!thread1.isAlive()){
                    thread1.start();
                }

                if (!STATUS_CIRCLE1) {
                    born(1);
                    STATUS_CIRCLE1 = true;
                }
                break;
            case 2: // 2号循环泵开
                if(!thread2.isAlive()){
                    thread2.start();
                }
                if (!STATUS_CIRCLE2) {
                    born(2);
                    STATUS_CIRCLE2=true;
                }

                break;
            case 3: // 1号补水泵开
                if(!thread3.isAlive()){

                    thread3.start();
                }
                if (!STATUS_CIRCLE3) {
                    born(3);
                    STATUS_CIRCLE3=true;
                }
                break;
            case 4:  // 2号补水泵开
                if(!thread4.isAlive()){
                    thread4.start();
                }
                if (!STATUS_CIRCLE4) {
                    born(4);
                    STATUS_CIRCLE4=true;
                }

                break;
            case 5:  // 1、2号补水泵泵开
                if(!thread5.isAlive()){
                    thread5.start();
                }
                if (!STATUS_CIRCLE5) {
                    born(5);
                    STATUS_CIRCLE5=true;
                }
                break;
            case 6:  // 1、2号补水泵开，补充2号补水泵
                if(!thread6.isAlive()){
                    thread6.start();
                }
                if (!STATUS_CIRCLE6) {
                    born(6);
                    STATUS_CIRCLE6=true;
                }
                break;
            case 7:
                if(!thread7.isAlive()){
                    thread7.start();
                }
                if (!STATUS_CIRCLE7) {
                    born(7);
                    STATUS_CIRCLE7=true;
                }
                break;
            case 8:
                if(!thread8.isAlive()){
                    thread8.start();
                }
                if (!STATUS_CIRCLE8) {
                    born(8);
                    STATUS_CIRCLE8=true;
                }
                break;


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
                circleList1.clear();
                break;
            case 2:
                STATUS_CIRCLE2=false;
                circleList2.clear();
                break;
            case 3:
                STATUS_CIRCLE3=false;
                circleList3.clear();
                break;
            case 4:
                STATUS_CIRCLE4=false;
                circleList4.clear();
                break;
            case 5:
                STATUS_CIRCLE5=false;
                circleList5.clear();
                break;
            case 6:
                STATUS_CIRCLE6=false;
                circleList6.clear();
                break;
            case 7:
                STATUS_CIRCLE7=false;
                circleList7.clear();
                break;
            case 8:
                STATUS_CIRCLE8=false;
                circleList8.clear();
                break;
        }
    }

    public void stopBeng(int i){
        switch (i){
            case 1:
                b1 = false;
                break;
            case 2:
                b2 = false;
                break;
            case 3:
                b3 = false;
                break;
            case 4:
                b4 = false;
                break;
        }

    }

    public void startBeng(int i){
        switch (i){
            case 1:
                b1 = true;
                break;
            case 2:
                b2 = true;
                break;
            case 3:
                b3 = true;
                break;
            case 4:
                b4 = true;
                break;
        }

    }

}
