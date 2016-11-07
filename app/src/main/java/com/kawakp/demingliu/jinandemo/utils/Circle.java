package com.kawakp.demingliu.jinandemo.utils;

/**
 * Created by zuheng.lv on 2016/6/9.
 */
public class Circle {
    private double x;//X坐标
    private double y;//Y坐标
    private int flag;//结点标记位
    private Boolean LINE;
    private int dric;//抖动标记位
    private int Color;//颜色

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public int getFlag() {
        return flag;
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public Boolean getLINE() {
        return LINE;
    }

    public void setLINE(Boolean LINE) {
        this.LINE = LINE;
    }

    public int getDric() {
        return dric;
    }

    public void setDric(int dric) {
        this.dric = dric;
    }

    public int getColor() {
        return Color;
    }

    public void setColor(int color) {
        Color = color;
    }
}
