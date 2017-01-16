package com.kawakp.demingliu.jinandemo.bean;

import java.util.List;

/**
 * Created by shuang.xiang on 2016/8/26.
 */
public class Bean {


    /**
     * createBy :
     * createDate : 2016-08-08 20:18:13
     * delFlag : 0
     * deviceModelId : gxznhrjz
     * elements : [{"address":"D1295","categoryId":"gxznhrjz_c_07","css":null,"displayName":"二次供温时段","icon":"","id":"gxznhrjz_e_15","level":"","max":0,"min":0,"number":15,"processMode":"","showType":"input","tableId":"gxznhrjz_t_1","unit":"℃","valueType":"REAL"},{"address":"D1399","categoryId":"gxznhrjz_c_07","css":null,"displayName":"二次供回压差","icon":"","id":"gxznhrjz_e_16","level":"","max":0,"min":0,"number":16,"processMode":"","showType":"input","tableId":"gxznhrjz_t_1","unit":"Mpa","valueType":"REAL"},{"address":"D1409","categoryId":"gxznhrjz_c_07","css":null,"displayName":"二次回压","icon":"","id":"gxznhrjz_e_17","level":"","max":0,"min":0,"number":17,"processMode":"","showType":"input","tableId":"gxznhrjz_t_1","unit":"Mpa","valueType":"REAL"},{"address":"D1411","categoryId":"gxznhrjz_c_07","css":null,"displayName":"二次回压偏差","icon":"","id":"gxznhrjz_e_18","level":"","max":0,"min":0,"number":18,"processMode":"","showType":"input","tableId":"gxznhrjz_t_1","unit":"Mpa","valueType":"REAL"}]
     * id : gxznhrjz_c_07
     * name : 控制参数
     * number : 7
     * type : PARAM
     * updateBy :
     * updateDate : 2016-08-08 20:18:13
     */

    private String createBy;
    private String createDate;
    private String delFlag;
    private String deviceModelId;
    private String id;
    private String name;
    private int number;
    private String type;
    private String updateBy;
    private String updateDate;
    /**
     * address : D1295
     * categoryId : gxznhrjz_c_07
     * css : null
     * displayName : 二次供温时段
     * icon :
     * id : gxznhrjz_e_15
     * level :
     * max : 0
     * min : 0
     * number : 15
     * processMode :
     * showType : input
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
        private String defaultAddress;
        private String categoryId;
        private String fieldName;
        private Object css;
        private String name;
        private String icon;
        private String id;
        private String level;
        private int max;
        private int min;
        private int number;
        private String processMode;
        private String showType;
        private String tableId;
        private String unit;
        private String valueType;

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getDefaultAddress() {
            return defaultAddress;
        }

        public void setDefaultAddress(String defaultAddress) {
            this.defaultAddress = defaultAddress;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public String getShowType() {
            return showType;
        }

        public void setShowType(String showType) {
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
