package com.yeepay.g3.core.frontend.util;

import java.util.Map;

/**
 * @author chronos.
 * @createDate 2016/10/12.
 */
public class ChartData {

    private String title; //标题
    private String categoryAxisLabel;//目录说明
    private String valueAxisLabel;//值说明
    private Map<String, Object> dataMap;

    public ChartData(String title, String categoryAxisLabel, String valueAxisLabel, Map<String, Object> dataMap) {
        this.title = title;
        this.categoryAxisLabel = categoryAxisLabel;
        this.valueAxisLabel = valueAxisLabel;
        this.dataMap = dataMap;
    }

    public ChartData() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCategoryAxisLabel() {
        return categoryAxisLabel;
    }

    public void setCategoryAxisLabel(String categoryAxisLabel) {
        this.categoryAxisLabel = categoryAxisLabel;
    }

    public String getValueAxisLabel() {
        return valueAxisLabel;
    }

    public void setValueAxisLabel(String valueAxisLabel) {
        this.valueAxisLabel = valueAxisLabel;
    }

    public Map<String, Object> getDataMap() {
        return dataMap;
    }

    public void setDataMap(Map<String, Object> dataMap) {
        this.dataMap = dataMap;
    }
}
