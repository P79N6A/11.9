package com.yeepay.g3.core.nccashier.facade.impl;

import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.dataEncrypt.NccashierDataEncryptService;
import com.yeepay.g3.facade.nccashier.service.NccashierDataEncryptFacade;
import com.yeepay.g3.utils.common.log.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * nc-cashier 数据库敏感信息清洗
 * Created by ruiyang.du on 2017/8/4.
 */
@Service
public class NccashierDataEncryptFacadeImpl implements NccashierDataEncryptFacade {

    private static final Logger logger = NcCashierLoggerFactory.getLogger(NccashierDataEncryptFacadeImpl.class);


    @Autowired
    private NccashierDataEncryptService nccashierDataEncryptService;


    /**
     * 终止任务
     *
     */
    @Override
    public String stopWorkingTask() {
        return nccashierDataEncryptService.stopWorkingTask();
    }


    @Override
    public String batchEncryptData(int corePoolSize, int maxPoolSize, int queueSize,long fromIndex, long toIndex, int queryPageSize, int partTaskSize, int threadTaskSize,String className) {
        logger.info("【nc-cashier敏感数据加密】batchEncryptData 入参 corePoolSize={}，maxPoolSize={}，queueSize={}，fromIndex={},toIndex={},queryPageSize={},partTaskSize={},threadTaskSize={}",
                corePoolSize, maxPoolSize, queueSize,fromIndex, toIndex, queryPageSize, partTaskSize, threadTaskSize);
        if (fromIndex < 0 || fromIndex > toIndex) {
            return "fromIndex或toIndex参数错误";
        }
        if (queryPageSize < 0) {
            return "queryPageSize参数错误";
        }
        if (partTaskSize < 0 || threadTaskSize<0) {
            return "partTaskSize或threadTaskSize参数错误";
        }
        return nccashierDataEncryptService.batchEncryptData(corePoolSize,maxPoolSize,queueSize,fromIndex,toIndex,queryPageSize,partTaskSize,threadTaskSize,className);
    }
}
