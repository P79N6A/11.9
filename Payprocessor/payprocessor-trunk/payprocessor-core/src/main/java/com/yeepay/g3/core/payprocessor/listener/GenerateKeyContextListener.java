package com.yeepay.g3.core.payprocessor.listener;

import com.yeepay.g3.core.payprocessor.util.ConstantUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

public class GenerateKeyContextListener implements ServletContextListener{
    
    private static final Logger log = LoggerFactory.getLogger(GenerateKeyContextListener.class);

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        log.info("contextDestroyed");
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        log.info("contextInitialized");
        
        ConstantUtils.mergeSensitiveKey();
    }

}
