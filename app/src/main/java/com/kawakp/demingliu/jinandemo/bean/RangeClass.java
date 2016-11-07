package com.kawakp.demingliu.jinandemo.bean;

import android.graphics.drawable.Drawable;
import android.widget.EditText;
import android.widget.TextView;

/**
 * Created by zuheng.lv on 2016/8/8.
 */
public class RangeClass {
    private Drawable color;//颜色的图片
    private Drawable icon;//图片
    private String title;//标题
    private String data;//采集数据
    private String meter;//仪表显示
    private String max;//量程上限
    private String min;//量程下限
    private String correct;//仪表校正
    private String maxAddressID;//量程上限地址
    private String minAddressID;//量程下限地址
    private String correctAddressID;//仪表校正地址
    private String ID;//设备ID


    public Drawable getColor() {
        return color;
    }

    public void setColor(Drawable color) {
        this.color = color;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getMeter() {
        return meter;
    }

    public void setMeter(String meter) {
        this.meter = meter;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getCorrect() {
        return correct;
    }

    public void setCorrect(String correct) {
        this.correct = correct;
    }


    public String getMaxAddressID() {
        return maxAddressID;
    }

    public void setMaxAddressID(String maxAddressID) {
        this.maxAddressID = maxAddressID;
    }


    public String getMinAddressID() {
        return minAddressID;
    }

    public void setMinAddressID(String minAddressID) {
        this.minAddressID = minAddressID;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCorrectAddressID() {
        return correctAddressID;
    }

    public void setCorrectAddressID(String correctAddressID) {
        this.correctAddressID = correctAddressID;
    }

}
