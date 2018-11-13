package com.yeepay.g3.core.nccashier.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.dao.UserRequestInfoDao;
import com.yeepay.g3.core.nccashier.entity.UserRequestInfo;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.UserRequestInfoService;
import com.yeepay.g3.core.nccashier.utils.RedisTemplate;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.OrderProcessorRequestDTO;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Description
 * PackageName: com.yeepay.g3.core.nccashier.service.impl
 *
 * @author pengfei.chen
 * @since 17/3/15 17:58
 */
@Service("userRequestInfoService")
public class UserRequestInfoServiceImpl implements UserRequestInfoService {

    private static Logger logger = NcCashierLoggerFactory.getLogger(UserRequestInfoServiceImpl.class);

    @Autowired
    private UserRequestInfoDao userRequestInfoDao;
    @Override
    public UserRequestInfo getUserRequestInfoByTokenId(String tokenId) {
        UserRequestInfo userRequestInfo = getUserRequestInfoFromRedis(tokenId);
        if(userRequestInfo == null){
            userRequestInfo = userRequestInfoDao.getUserRequestInfoBytoken(tokenId);
            setUserRequestInfoRedis(tokenId,userRequestInfo);
        }
        return userRequestInfo;
    }

    @Override
    public void insertUserRequestInfo(UserRequestInfo userRequestInfo) {
        userRequestInfoDao.saveUserRequestInfo(userRequestInfo);
    }

    @Override
    public OrderProcessorRequestDTO getUrlParamFromUserRequestInfo(UserRequestInfo userRequestInfo) {
        OrderProcessorRequestDTO urlParamInfo = null;
        try {
            if (userRequestInfo == null || StringUtils.isEmpty(userRequestInfo.getUrlParamInfo())) {
                return urlParamInfo;
            }
            urlParamInfo = JSONObject.parseObject(userRequestInfo.getUrlParamInfo(),OrderProcessorRequestDTO.class);
            return urlParamInfo;
        } catch (Exception e) {
            logger.warn("getUrlParamFromUserRequestInfo() 从UserRequestInfo中解析urlParamInfo失败，userRequestInfo="+JSONObject.toJSONString(userRequestInfo)+"，error=", e);
            return null;
        }
    }

    private UserRequestInfo getUserRequestInfoFromRedis(String tokenId){
        try {
            UserRequestInfo userRequestInfo = RedisTemplate.getTargetFromRedis(
                    Constant.NCCASHIER_USERREQUEST_REDIS_KEY + tokenId, UserRequestInfo.class);
            return userRequestInfo;
        } catch (Exception e) {
            logger.error("获取redis用户缓存信息失败,tokenId:{},错误信息:{}", tokenId, e);
            return null;
        }
    }

    private void setUserRequestInfoRedis(String tokenId, UserRequestInfo userRequestInfo) {
        try {
            RedisTemplate.setCacheObjectSumValue(Constant.NCCASHIER_USERREQUEST_REDIS_KEY + tokenId,
                    userRequestInfo, Constant.NCCASHIER_USERACCOUNT_REDIS_TIME_LIMIT);
        } catch (Exception e) {
            logger.error("更新redis用户缓存信息失败,tokenId:" + tokenId, e);
        }
    }

}
