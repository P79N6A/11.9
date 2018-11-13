package com.yeepay.g3.core.nccashier.beanExtension;

import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;

/**
 * 用于应用启动时初始化加解密key
 * Created by ruiyang.du on 2017/8/8.
 */
@Service
public class EncryptKeyInitProcessor implements InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(EncryptKeyInitProcessor.class);

    @Override
    public void afterPropertiesSet() throws Exception {
        String nccashierEncryptKey = CommonUtil.getNccashierEncryptKey();
        if(StringUtils.isBlank(nccashierEncryptKey)){
            logger.info("初始化加解密key失败，请检查统一配置");
            throw new Error("初始化加解密key失败，请检查统一配置");
        }
        logger.info("初始化加解密key成功");
    }
}
