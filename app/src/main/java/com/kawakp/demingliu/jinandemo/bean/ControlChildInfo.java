package com.kawakp.demingliu.jinandemo.bean;

/**
 * Created by deming.liu on 2016/10/14.
 */
public class ControlChildInfo {
    private String name;
    private String value;
    private String address;
    private String unit;
    private String id;

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public ControlChildInfo(String id,String name, String value,String address) {
        this.id = id;
        this.name = name;
        this.value = value;
        this.address = address;
    }
}
