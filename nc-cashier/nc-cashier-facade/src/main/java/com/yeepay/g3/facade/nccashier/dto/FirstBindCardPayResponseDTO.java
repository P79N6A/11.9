package com.yeepay.g3.facade.nccashier.dto;

/**
 * 先绑卡后支付时，调用支付接口的返回值
 * 
 * @author duangduang
 *
 */
public class FirstBindCardPayResponseDTO extends CashierPaymentResponseDTO {

	private static final long serialVersionUID = 1L;

	private boolean loseNeedItem;

	private Long recordId;

	private boolean finishPay;

	public FirstBindCardPayResponseDTO() {

	}

	public boolean isLoseNeedItem() {
		return loseNeedItem;
	}

	public void setLoseNeedItem(boolean loseNeedItem) {
		this.loseNeedItem = loseNeedItem;
	}

	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public boolean isFinishPay() {
		return finishPay;
	}

	public void setFinishPay(boolean finishPay) {
		this.finishPay = finishPay;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("FirstBindCardPayResponseDTO{");
		sb.append("finishPay='").append(finishPay).append('\'');
		sb.append(", recordId='").append(recordId).append('\'');
		sb.append(", loseNeedItem='").append(loseNeedItem).append('\'');
		sb.append(",").append(super.toString());
		sb.append('}');
		return sb.toString();
	}
}
