package com.yeepay.g3.core.frontend.service.impl;

import com.yeepay.g3.core.frontend.dao.FanRouteDao;
import com.yeepay.g3.core.frontend.entity.FanRoute;
import com.yeepay.g3.core.frontend.service.FanRouteService;
import com.yeepay.g3.core.frontend.util.ConstantUtils;
import com.yeepay.g3.core.frontend.util.log.FeLogger;
import com.yeepay.g3.core.frontend.util.log.FeLoggerFactory;
import com.yeepay.g3.utils.common.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class FanRouteServiceImpl  implements FanRouteService {

    private static final FeLogger logger = (FeLogger) FeLoggerFactory.getLogger(FanRouteServiceImpl.class);


    @Resource
    protected FanRouteDao fanRouteDao;

    @Override
    public FanRoute queryFanRouteByReportId(String reportId) {
        return fanRouteDao.queryFanRouteByReportId(reportId);
    }

    @Override
    public void addFanRoute(FanRoute fanRoute) {
        fanRoute.setCreateTime(new Date());
        fanRoute.setEnabled(0);
        fanRouteDao.add(fanRoute);
    }

    /**
     *
     * added by zengzhi.han 20181016
     *
     * 设置交易的商户号
     */
    @Override
    public String getFanRouteTradeCustomerNum(Map<String, String> map) {
        //哆啦宝粉丝路由根据 reportId 查找子商户传给路由,目前只有哆啦宝有这个需求
        if (map != null) {
            List<String> fanRouteParentMerchantNos =  ConstantUtils.getFanRouteParentMerchantNos();
            String reportId = map.get(ConstantUtils.WX_REPORT_ID);
            String parentMerchantNo = map.get(ConstantUtils.PARENT_MERCHANT_NO);
            if (StringUtils.isNotBlank(reportId) &&
                    StringUtils.isNotBlank(parentMerchantNo) &&
                    fanRouteParentMerchantNos!=null &&
                    fanRouteParentMerchantNos.size()>0 &&
                    fanRouteParentMerchantNos.contains(parentMerchantNo)) {
                FanRoute fanRoute = queryFanRouteByReportId(reportId);
                if (fanRoute == null || StringUtils.isBlank(fanRoute.getSubCustomerNum())) {
                    logger.warn("Fe系统***哆啦宝粉丝路由,根据reportId=".concat(reportId).concat("*****查找不到子商编"));
                } else {
                    return fanRoute.getSubCustomerNum();
                }
            }
        }
        return null;
    }
}
