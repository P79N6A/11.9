package com.yeepay.g3.core.nccashier.service;

import com.yeepay.g3.core.nccashier.entity.UserRequestInfo;
import com.yeepay.g3.facade.nccashier.dto.OrderProcessorRequestDTO;

/**
 * Description
 * PackageName: com.yeepay.g3.core.nccashier.service
 *
 * @author pengfei.chen
 * @since 17/3/15 17:55
 */
public interface UserRequestInfoService {

    /**
     * 获取用户请求信息
     * @param string
     * @return
     */
    public UserRequestInfo getUserRequestInfoByTokenId(String string);

    /**
     * 插入用户请求信息
     * @param userRequestInfo
     */
    public void insertUserRequestInfo(UserRequestInfo userRequestInfo);

    /**
     * 将userRequestInfo中的urlParamInfo字符串，反序列化为OrderProcessorRequestDTO
     * @param userRequestInfo
     * @return
     */
    OrderProcessorRequestDTO getUrlParamFromUserRequestInfo(UserRequestInfo userRequestInfo);
}
