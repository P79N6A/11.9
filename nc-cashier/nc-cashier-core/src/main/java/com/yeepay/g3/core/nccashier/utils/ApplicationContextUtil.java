package com.yeepay.g3.core.nccashier.utils;

import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

/**
 * 维持一个applicationContext
 *
 * Created by ruiyang.du on 2017/6/28.
 */
@Service
public class ApplicationContextUtil implements ApplicationContextAware {

    Logger logger = NcCashierLoggerFactory.getLogger(ApplicationContextUtil.class);

    private ApplicationContext applicationContext;

    public <T> T getBean(String name, Class<T> type) {
        if (StringUtils.isEmpty(name) || type == null) {
            return null;
        }
        try {
            T bean = getApplicationContext().getBean(name, type);
            return bean;
        } catch (Exception e) {
            logger.warn("getBean() 失败，name = " + name + ",type = " + type.getName() + "异常为：", e);
            return null;
        }
    }

    public ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
