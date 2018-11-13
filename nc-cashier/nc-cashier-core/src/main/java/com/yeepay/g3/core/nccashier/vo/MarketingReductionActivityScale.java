package com.yeepay.g3.core.nccashier.vo;

import java.util.List;

import com.yeepay.g3.facade.mktg.dto.scene.PaymentType;

public class MarketingReductionActivityScale {
	
	  /**
     * 支付产品 YJZF/EBANK等等二级产品
     */
    private String paymentProduct;

    /**
     * 三级：包含借贷、对公对私、银行编码
     */
    private List<PaymentType> paymentType;


}
