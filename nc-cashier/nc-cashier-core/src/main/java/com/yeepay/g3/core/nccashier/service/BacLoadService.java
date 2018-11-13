package com.yeepay.g3.core.nccashier.service;

import com.yeepay.g3.facade.nccashier.dto.MerchantProductDTO;

/**
 * @program: nc-cashier-parent
 * @description: ${description}
 * @author: jimin.zhou
 * @create: 2018-09-11 14:42
 **/
public interface BacLoadService {

    boolean isOpenNet(MerchantProductDTO config);

    boolean isOpenRemit(MerchantProductDTO config);

    boolean isOpenAccunt(MerchantProductDTO config);

    boolean isOpenLoad(MerchantProductDTO config);



}
