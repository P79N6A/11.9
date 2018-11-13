package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;

public class CashierQueryResponseDTO extends BasicResponseDTO implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5610046569180415743L;
	
	/**
	 * 支付记录ID
	 */
	private long recordId;

	private TradeNoticeDTO tradeNoticeDTO;

	public long getRecordId() {
		return recordId;
	}

	public void setRecordId(long recordId) {
		this.recordId = recordId;
	}
	
	public TradeNoticeDTO getTradeNoticeDTO() {
		return tradeNoticeDTO;
	}

	public void setTradeNoticeDTO(TradeNoticeDTO tradeNoticeDTO) {
		this.tradeNoticeDTO = tradeNoticeDTO;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("CashierQueryResponseDTO{");
		sb.append("recordId='").append(this.recordId).append('\'');
		sb.append(", tradeNoticeDTO='").append(this.tradeNoticeDTO==null?"null":this.tradeNoticeDTO.toString()).append('\'');
		sb.append(","+super.toString());
		sb.append('}');
		return sb.toString();
	}
}
