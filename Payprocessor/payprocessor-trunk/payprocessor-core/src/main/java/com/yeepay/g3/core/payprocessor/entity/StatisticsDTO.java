package com.yeepay.g3.core.payprocessor.entity;

import com.yeepay.g3.core.payprocessor.enumtype.ResultType;
import org.apache.commons.collections.map.HashedMap;

import java.util.Map;

/**
 * 数据统计
 *
 * @author chronos.
 * @createDate 2016/10/13.
 */
public class StatisticsDTO {

    /**
     * 表名
     */
    private String table;
    /**
     * 结果列
     */
    private String resultColumn;
    /**
     * 结果类型
     */
    private ResultType resultType;
    /**
     * 分组字段
     */
    private String groupBy;
    /**
     * 排序字段
     */
    private String orderBy;
    /**
     * 查询维度列表
     */
    private Map<String,Object> dimensions = new HashedMap();
    /**
     * 查询条件:
     */
    private Map<String,Object> conditions = new HashedMap();

    public StatisticsDTO() {
    }

    public String getTable() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String getResultColumn() {
        return resultColumn;
    }

    public void setResultColumn(String resultColumn) {
        this.resultColumn = resultColumn;
    }

    public ResultType getResultType() {
        return resultType;
    }

    public void setResultType(ResultType resultType) {
        this.resultType = resultType;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getGroupBy() {
        return groupBy;
    }

    public void setGroupBy(String groupBy) {
        this.groupBy = groupBy;
    }

    public Map<String, Object> getDimensions() {
        return dimensions;
    }

    public void setDimensions(Map<String, Object> dimensions) {
        this.dimensions = dimensions;
    }

    public Map<String, Object> getConditions() {
        return conditions;
    }

    public void setConditions(Map<String, Object> conditions) {
        this.conditions = conditions;
    }
}
