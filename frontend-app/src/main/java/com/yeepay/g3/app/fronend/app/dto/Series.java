package com.yeepay.g3.app.fronend.app.dto;

/**
 * @author chronos.
 * @createDate 2016/10/18.
 */
public class Series {

    private String name;
    private Number[] data;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Number[] getData() {
        return data;
    }

    public void setData(Number[] data) {
        this.data = data;
    }

    public Series(String name, Number[] data) {
        this.name = name;
        this.data = data;
    }

    public Series() {

    }
}
