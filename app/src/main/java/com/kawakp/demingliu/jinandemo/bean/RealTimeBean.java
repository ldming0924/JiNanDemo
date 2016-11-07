package com.kawakp.demingliu.jinandemo.bean;

import java.util.List;

/**
 * Created by deming.liu on 2016/10/13.
 */
public class RealTimeBean {

    /**
     * createBy :
     * createDate : 2016-08-08 20:18:13
     * delFlag : 0
     * deviceModelId : gxznhrjz
     * elements : [{"address":"D1081","categoryId":"gxznhrjz_c_01","css":null,"displayName":"一次供水温度","icon":"","id":"gxznhrjz_e_01","level":"","max":100,"min":0,"number":1,"processMode":"","showType":null,"tableId":"gxznhrjz_t_1","unit":"℃","valueType":"REAL"},{"address":"D1083","categoryId":"gxznhrjz_c_01","css":null,"displayName":"一次回水温度","icon":"","id":"gxznhrjz_e_02","level":"","max":100,"min":0,"number":2,"processMode":"","showType":null,"tableId":"gxznhrjz_t_1","unit":"℃","valueType":"REAL"},{"address":"D1093","categoryId":"gxznhrjz_c_01","css":null,"displayName":"二次供水温度","icon":"","id":"gxznhrjz_e_03","level":"","max":100,"min":0,"number":3,"processMode":"","showType":null,"tableId":"gxznhrjz_t_1","unit":"℃","valueType":"REAL"},{"address":"D1095","categoryId":"gxznhrjz_c_01","css":null,"displayName":"二次回水温度","icon":"","id":"gxznhrjz_e_04","level":"","max":100,"min":0,"number":4,"processMode":"","showType":null,"tableId":"gxznhrjz_t_1","unit":"℃","valueType":"REAL"}]
     * htmlUrl : null
     * id : gxznhrjz_c_01
     * name : 温度参数
     * number : 1
     * type : MONITOR
     * updateBy :
     * updateDate : 2016-08-08 20:18:13
     */

    private String createBy;
    private String createDate;
    private String delFlag;
    private String deviceModelId;
    private Object htmlUrl;
    private String id;
    private String name;
    private int number;
    private String type;
    private String updateBy;
    private String updateDate;
    /**
     * address : D1081
     * categoryId : gxznhrjz_c_01
     * css : null
     * displayName : 一次供水温度
     * icon :
     * id : gxznhrjz_e_01
     * level :
     * max : 100
     * min : 0
     * number : 1
     * processMode :
     * showType : null
     * tableId : gxznhrjz_t_1
     * unit : ℃
     * valueType : REAL
     */

    private List<ElementsBean> elements;

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDeviceModelId() {
        return deviceModelId;
    }

    public void setDeviceModelId(String deviceModelId) {
        this.deviceModelId = deviceModelId;
    }

    public Object getHtmlUrl() {
        return htmlUrl;
    }

    public void setHtmlUrl(Object htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public String getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(String updateDate) {
        this.updateDate = updateDate;
    }

    public List<ElementsBean> getElements() {
        return elements;
    }

    public void setElements(List<ElementsBean> elements) {
        this.elements = elements;
    }

    public static class ElementsBean {
        private String address;
        private String categoryId;
        private Object css;
        private String displayName;
        private String icon;
        private String id;
        private String level;
        private int max;
        private int min;
        private int number;
        private String processMode;
        private Object showType;
        private String tableId;
        private String unit;
        private String valueType;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public Object getCss() {
            return css;
        }

        public void setCss(Object css) {
            this.css = css;
        }

        public String getDisplayName() {
            return displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLevel() {
            return level;
        }

        public void setLevel(String level) {
            this.level = level;
        }

        public int getMax() {
            return max;
        }

        public void setMax(int max) {
            this.max = max;
        }

        public int getMin() {
            return min;
        }

        public void setMin(int min) {
            this.min = min;
        }

        public int getNumber() {
            return number;
        }

        public void setNumber(int number) {
            this.number = number;
        }

        public String getProcessMode() {
            return processMode;
        }

        public void setProcessMode(String processMode) {
            this.processMode = processMode;
        }

        public Object getShowType() {
            return showType;
        }

        public void setShowType(Object showType) {
            this.showType = showType;
        }

        public String getTableId() {
            return tableId;
        }

        public void setTableId(String tableId) {
            this.tableId = tableId;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public String getValueType() {
            return valueType;
        }

        public void setValueType(String valueType) {
            this.valueType = valueType;
        }
    }
}
