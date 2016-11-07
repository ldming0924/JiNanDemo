package com.kawakp.demingliu.jinandemo.utils;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathEffect;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;


import com.kawakp.demingliu.jinandemo.R;

import java.io.CharArrayWriter;
import java.util.ArrayList;
import java.util.List;

public class DrawChart extends View {

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

	double y;

	public void setY(double y) {
		this.y = y;
	}

	public DrawChart(Context context) {
		this(context,null);
	}

	public DrawChart(Context context, AttributeSet attrs) {
		this(context, attrs,0);
	}

	public DrawChart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		plist = new ArrayList<Point>();
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
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		drawTable(canvas); //表格
		prepareLine(y); //坐标点
		drawCurve(canvas); //折线
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
			prepareLine(y);
			if(plist.size() >= 2){
				for(int i = 0; i<plist.size()-1; i++){
					canvas.drawLine(plist.get(i).x, plist.get(i).y, plist.get(i+1).x, plist.get(i+1).y, paint);
					invalidate();
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

	private void initPlist(){
		int py = OFFSET_TOP + (int)(Math.random()*(CHARTH - OFFSET_TOP));
		Point p = new Point(OFFSET_LEFT + CHARTW , py );
		plist.add(p);

	}


}
