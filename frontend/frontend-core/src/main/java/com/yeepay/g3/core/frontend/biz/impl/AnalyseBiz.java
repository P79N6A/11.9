package com.yeepay.g3.core.frontend.biz.impl;

import com.yeepay.g3.core.frontend.entity.StatisticsDTO;
import com.yeepay.g3.core.frontend.util.ChartData;
import com.yeepay.g3.utils.common.StringUtils;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static com.yeepay.g3.core.frontend.util.ConstantUtils.*;

/**
 * 数据分析
 * @author chronos.
 * @createDate 2016/10/13.
 */
@Service("analyseBiz")
public class AnalyseBiz {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<ChartData> queryData(StatisticsDTO dto){
        if (StringUtils.isBlank(dto.getTable()) || StringUtils.isBlank(dto.getResultColumn())
                || dto.getResultType() == null) {
            throw new IllegalArgumentException("关键域不能为空");
        }
        List<ChartData> arrayList = new LinkedList<ChartData>();
        for (String dim : dto.getDimensions().keySet()){
            for (String conditionKey : dto.getConditions().keySet()) {
                StringBuilder sb = new StringBuilder("SELECT ");
                sb.append(dto.getResultType().name()).append("(").append(dto.getResultColumn()).append(") ")
                        .append(STATIC_RESULT).append(",").append(dto.getGroupBy()).append(" ").append(STATIC_DATE)
                        .append(", ").append(dim).append(" ").append(STATIC_DIM)
                        .append(" FROM ").append(dto.getTable());
                sb.append(" WHERE ").append(dto.getConditions().get(conditionKey));
                sb.append(" GROUP BY ").append(dto.getGroupBy()).append(",").append(dim);
                if (StringUtils.isNotBlank(dto.getOrderBy())) {
                    sb.append(" ORDER BY ").append(dto.getOrderBy());
                }
                sb.append(" WITH UR ");

                List<Map<String, Object>> queryResult = jdbcTemplate.queryForList(sb.toString());
                if (queryResult == null || queryResult.size() < 1)
                    continue;
                ChartData chartData = new ChartData();
                chartData.setCategoryAxisLabel((String) dto.getDimensions().get(dim));
                chartData.setValueAxisLabel(conditionKey);
                chartData.setTitle(conditionKey + "-" + dto.getDimensions().get(dim));
                chartData.setDataMap(parseListToMap(queryResult));
//                analysedData.put(dto.getDimensions().get(dim) + "-" + conditionKey, queryResult);
                arrayList.add(chartData);
            }
        }
        return arrayList;
    }

    private Map<String,Object> parseListToMap(List<Map<String,Object>> resultList){
        Map<String,Object> objectMap = new HashedMap();
        for (Map<String,Object> map : resultList){
            String key = (String) map.get(STATIC_DIM);
            if (StringUtils.isBlank(key))
                continue;
            Map<String,Object> tmp = (Map<String, Object>) objectMap.get(key);
            if (tmp == null){
                tmp = new LinkedHashMap();
            }
            tmp.put((String) map.get(STATIC_DATE),map.get(STATIC_RESULT));
            objectMap.put(key,tmp);
        }
        return objectMap;
    }
}
