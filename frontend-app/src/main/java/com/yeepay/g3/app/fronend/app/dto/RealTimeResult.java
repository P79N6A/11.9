package com.yeepay.g3.app.fronend.app.dto;

import com.yeepay.g3.utils.common.json.JSONObject;
import org.apache.commons.collections.map.HashedMap;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author chronos.
 * @createDate 2016/10/19.
 */
public class RealTimeResult {

    private String name;

    private List<Map<String,Object>> series;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Map<String, Object>> getSeries() {
        return series;
    }

    public void setSeries(List<Map<String, Object>> series) {
        this.series = series;
    }

    public RealTimeResult() {
    }

    public RealTimeResult(String name, List<Map<String, Object>> series) {
        this.name = name;
        this.series = series;
    }

    public static void main(String args[]){
        Map<String,Object> map = new HashedMap();
        map.put("trx_amount",10);
        map.put("date","2016-10-19 19:10");
        map.put("key","key");
        List<Map<String,Object>> list = new ArrayList<Map<String, Object>>();
        list.add(map);
        RealTimeResult result = new RealTimeResult("type", list);
        JSONObject jsonObject = (JSONObject) JSONObject.wrap(result);
        System.out.println(jsonObject.toString());
    }
}
