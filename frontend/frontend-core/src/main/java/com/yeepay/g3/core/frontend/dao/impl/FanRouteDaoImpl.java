package com.yeepay.g3.core.frontend.dao.impl;

import com.yeepay.g3.core.frontend.dao.FanRouteDao;
import com.yeepay.g3.core.frontend.entity.FanRoute;
import com.yeepay.g3.utils.persistence.mybatis.GenericDaoDefault;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class FanRouteDaoImpl extends GenericDaoDefault<FanRoute> implements FanRouteDao {

    @Override
    public FanRoute queryFanRouteByReportId(String reportId) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("reportId", reportId);
        return (FanRoute)this.queryOne("queryFanRouteByReportId",params);
    }

}
