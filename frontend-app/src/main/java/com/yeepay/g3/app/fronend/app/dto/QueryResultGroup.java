package com.yeepay.g3.app.fronend.app.dto;

import com.yeepay.g3.utils.common.json.JSONObject;

/**
 * 查询结果集
 * @author chronos.
 * @createDate 2016/10/18.
 */
public class QueryResultGroup {

    private String key;

    private String[] categories;

    private Series[] series;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Series[] getSeries() {
        return series;
    }

    public void setSeries(Series[] series) {
        this.series = series;
    }

    public String[] getCategories() {
        return categories;
    }

    public void setCategories(String[] categories) {
        this.categories = categories;
    }

    public QueryResultGroup() {
    }

    public QueryResultGroup(String[] categories, Series[] series) {
        this.categories = categories;
        this.series = series;
    }

    public static void main(String[] args){
        String[] categories = {"10:09","10:10","10.11"};

        Long[] data1 = {15l,20l,100l};
        Series series1 = new Series("W",data1);
        Long[] data2 = {100l,59l,37l};
        Series series2 = new Series("Z",data2);
        Series[] series = {series1,series2};
        QueryResultGroup group = new QueryResultGroup(categories, series);
        JSONObject jsonObject = (JSONObject) JSONObject.wrap(group);
        System.out.println(jsonObject.toString());
    }
}
