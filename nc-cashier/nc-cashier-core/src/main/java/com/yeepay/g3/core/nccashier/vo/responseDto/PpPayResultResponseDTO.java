package com.yeepay.g3.core.nccashier.vo.responseDto;


import java.math.BigDecimal;

import com.yeepay.g3.facade.payprocessor.dto.CombResponseDTO;

/**
 * Created by jimin.zhou on 18/1/3.
 *
 * PP支付确认后发送的支付结果
 */
public class PpPayResultResponseDTO extends ResponseStatusDTO {

    private static final long serialVersionUID = 3751563662742836656L;
    /**
     * 这些字段和PayRecordResponseDTO字段一致
     */
    private String trxStatus;
    private String orderSystemStatus;
    private String frpCode;
    private String bankOrderNO;
    private String bankTrxId;
    private BigDecimal cost;
    private String recordNo;
    private String cardId;
    private Integer cflCount;
    private BigDecimal cflRate = new BigDecimal(0);
    private BigDecimal merchantFeeSubsidy = new BigDecimal(0);
    private BigDecimal merchantAmountSubsidy = new BigDecimal(0);

    /**
     * 商户编号
     */
    private String customerNo;

    /**
     * 商户订单号
     */
    private String outTradeNo;
    
    /**
	 * 第二支付实体
	 */
	private CombResponseDTO combResponseDTO;
    
    /**
	 * 如果是组合支付，有此参数，第一支付金额
	 */
	private BigDecimal firstPayAmount;
    

    public String getFrpCode() {
        return frpCode;
    }

    public void setFrpCode(String frpCode) {
        this.frpCode = frpCode;
    }

    public String getBankOrderNO() {
        return bankOrderNO;
    }

    public void setBankOrderNO(String bankOrderNO) {
        this.bankOrderNO = bankOrderNO;
    }

    public String getBankTrxId() {
        return bankTrxId;
    }

    public void setBankTrxId(String bankTrxId) {
        this.bankTrxId = bankTrxId;
    }

    public BigDecimal getCost() {
        return cost;
    }

    public void setCost(BigDecimal cost) {
        this.cost = cost;
    }

    public String getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(String recordNo) {
        this.recordNo = recordNo;
    }

    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public Integer getCflCount() {
        return cflCount;
    }

    public void setCflCount(Integer cflCount) {
        this.cflCount = cflCount;
    }

    public BigDecimal getCflRate() {
        return cflRate;
    }

    public void setCflRate(BigDecimal cflRate) {
        this.cflRate = cflRate;
    }

    public BigDecimal getMerchantFeeSubsidy() {
        return merchantFeeSubsidy;
    }

    public void setMerchantFeeSubsidy(BigDecimal merchantFeeSubsidy) {
        this.merchantFeeSubsidy = merchantFeeSubsidy;
    }

    public BigDecimal getMerchantAmountSubsidy() {
        return merchantAmountSubsidy;
    }

    public void setMerchantAmountSubsidy(BigDecimal merchantAmountSubsidy) {
        this.merchantAmountSubsidy = merchantAmountSubsidy;
    }

    public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getOutTradeNo() {
        return outTradeNo;
    }

    public void setOutTradeNo(String outTradeNo) {
        this.outTradeNo = outTradeNo;
    }

	public String getTrxStatus() {
		return trxStatus;
	}

	public void setTrxStatus(String trxStatus) {
		this.trxStatus = trxStatus;
	}

	public String getOrderSystemStatus() {
		return orderSystemStatus;
	}

	public void setOrderSystemStatus(String orderSystemStatus) {
		this.orderSystemStatus = orderSystemStatus;
	}

	public BigDecimal getFirstPayAmount() {
		return firstPayAmount;
	}

	public void setFirstPayAmount(BigDecimal firstPayAmount) {
		this.firstPayAmount = firstPayAmount;
	}

	public CombResponseDTO getCombResponseDTO() {
		return combResponseDTO;
	}

	public void setCombResponseDTO(CombResponseDTO combResponseDTO) {
		this.combResponseDTO = combResponseDTO;
	}
	
}
