package com.yeepay.g3.facade.nccashier.dto;

import com.yeepay.g3.facade.cwh.enumtype.IdentityType;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.nccashier.util.HiddenCode;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

/**
 * 银行卡分期下单接口入参实体
 * 
 * @author duangduang
 *
 */
public class APIInstallmentRequestDTO extends APINoCardBasicRequestDTO {

	private static Logger logger = LoggerFactory.getLogger(APIInstallmentRequestDTO.class);

	private static final long serialVersionUID = 1L;

	/**
	 * 签约关系ID（来源：签约关系列表）
	 */
	private String signRelationId;

	/**
	 * 卡号
	 */
	protected String cardNo;

	/**
	 * 分期期数
	 */
	protected String number;

	public APIInstallmentRequestDTO() {

	}

	public String getSignRelationId() {
		return signRelationId;
	}

	public void setSignRelationId(String signRelationId) {
		this.signRelationId = signRelationId;
	}

	public String getCardNo() {
		return cardNo;
	}

	public void setCardNo(String cardNo) {
		this.cardNo = cardNo;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	@Override
	public void validate() {
		super.validate();
		
		if(StringUtils.isBlank(getToken())){
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ",token不能为空");
		}
		
		if (StringUtils.isBlank(cardNo) && StringUtils.isBlank(signRelationId)) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ",cardNo和signRelationId不能同时为空");
		}
		if (StringUtils.isNotBlank(signRelationId)
				&& (StringUtils.isBlank(getUserNo()) || StringUtils.isBlank(getUserType()))) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ",传signRelationId，userNo和userType必须不为空");
		}

		// 校验userType的格式
		if (StringUtils.isNotBlank(getUserType())) {
			try {
				IdentityType.valueOf(getUserType());
			} catch (Throwable t) {
				logger.warn("token=" + getToken() + ",转化userType" + getUserType() + "异常");
				throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
						Errors.INPUT_PARAM_NULL.getMsg() + ",userType格式不合法");
			}
		}

		// 校验分期期数
		if (StringUtils.isBlank(number)) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ",number不能为空");
		}
		if (!StringUtils.isNumeric(number) || number.trim().equals("0")) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + ",number格式不合法");
		}
	}

	@Override
	public String toString() {
		return "APIInstallmentRequestDTO " +
				super.toString()+
				"[signRelationId=" + signRelationId + ", cardNo=" + HiddenCode.hiddenBankCardNO(cardNo) + ", number="
				+ number + "]";
	}
	
}
