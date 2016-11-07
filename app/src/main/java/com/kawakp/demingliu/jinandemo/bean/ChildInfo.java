package com.kawakp.demingliu.jinandemo.bean;

/**
 * Created by shuang.xiang on 2016/8/26.
 */
public class ChildInfo {
    private String name;
    private String value;
    private String address;
    private String unit;


    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ChildInfo(String name, String value,String unit) {
        this.name = name;
        this.value = value;
        this.unit = unit;
    }
}
