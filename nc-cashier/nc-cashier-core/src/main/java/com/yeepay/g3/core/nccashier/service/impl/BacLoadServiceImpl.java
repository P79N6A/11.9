package com.yeepay.g3.core.nccashier.service.impl;/**
 * @program: nc-cashier-parent
 * @description:
 * @author: jimin.zhou
 * @create: 2018-09-11 14:44
 **/

import com.yeepay.g3.core.nccashier.service.BacLoadService;
import com.yeepay.g3.core.nccashier.utils.BacLoadUtils;
import com.yeepay.g3.facade.nccashier.dto.MerchantProductDTO;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import org.springframework.stereotype.Service;


/**
 *
 * @description:
 *
 * @author: jimin.zhou
 *
 * @create: 2018-09-11 14:44
 **/

@Service
public class BacLoadServiceImpl implements BacLoadService {
    @Override
    public boolean isOpenNet(MerchantProductDTO config) {
        if(isOpenLoad(config)){
            if(BacLoadUtils.BAC_LOAD_B2B_NET_PAY_TOOL.equals(config.getBase()) || BacLoadUtils.BAC_LOAD_B2C_NET_PAY_TOOL.equals(config.getBase()))
                return true;
        }
        return false;
    }

    @Override
    public boolean isOpenRemit(MerchantProductDTO config) {
        if(isOpenLoad(config)){
            if(BacLoadUtils.BAC_LOAD_B2B_REMIT_PAY_TOOL.equals(config.getBase()) || BacLoadUtils.BAC_LOAD_B2C_REMIT_PAY_TOOL.equals(config.getBase()))
                return true;
        }
        return false;
    }

    @Override
    public boolean isOpenAccunt(MerchantProductDTO config) {
        if(isOpenLoad(config)){
            if(BacLoadUtils.BAC_LOAD_ACCOUNT_PAY_TOOL.equals(config.getBase()) )
                return true;
        }
        return false;
    }

    @Override
    public boolean isOpenLoad(MerchantProductDTO config) {
        if(config!=null && BacLoadUtils.BAC_LOAD_BIZ_SYS.equals(config.getBiz()) && BacLoadUtils.BAC_LOAD_SALE_CODE.equals(config.getMar())){
            return true;
        }
        return false;
    }


}
