package com.yeepay.g3.facade.nccashier.exception;

import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.utils.common.exception.YeepayBizException;

/**
 * 业务异常
 * 
 * @author cheng.li
 * @date 2014-3-6
 */
public class CashierBusinessException extends YeepayBizException {

	private static final long serialVersionUID = -3067823464703563324L;


	public CashierBusinessException(String defineCode, String message) {
		super(defineCode);
		try {
			this.defineCode = defineCode;
			setMessage(message);
		} catch (Exception e) {
		}
	}
	
	/**
	 * 根据Errors获取错误信息
	 * 
	 * @param errors
	 */
	public CashierBusinessException(Errors errors) {
		super(errors.getCode());
		try {
			this.defineCode = errors.getCode();
			setMessage(errors.getMsg());
		} catch (Exception e) {
		}
	}

	public static final CashierBusinessException SYSTEM_EXCEPTION = new CashierBusinessException(
			Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
	
	public static final CashierBusinessException SYSTEM_INPUT_EXCEPTION = new CashierBusinessException(
			Errors.SYSTEM_INPUT_EXCEPTION.getCode(), Errors.SYSTEM_INPUT_EXCEPTION.getMsg());
	
	public static final CashierBusinessException INPUT_PARAM_NULL = new CashierBusinessException(
			Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg());
	
	public static final CashierBusinessException PAY_REQUEST_NULL = new CashierBusinessException(
			Errors.PAY_REQUEST_NULL.getCode(), Errors.PAY_REQUEST_NULL.getMsg());
	
	public static final CashierBusinessException PAY_RECORD_NULL = new CashierBusinessException(
			Errors.PAY_RECORD_NULL.getCode(), Errors.PAY_RECORD_NULL.getMsg());
	
	public static final CashierBusinessException CARD_INFO_ERROR = new CashierBusinessException(
			Errors.CARD_INFO_ERROR.getCode(), Errors.CARD_INFO_ERROR.getMsg());
	
	public static final CashierBusinessException SUPPORT_BANK_FAILED = new CashierBusinessException(
			Errors.SUPPORT_BANK_FAILED.getCode(), Errors.SUPPORT_BANK_FAILED.getMsg());
	
	private static final String E3000117 = "3000117";

	public static final CashierBusinessException GET_SMS_FIRST = new CashierBusinessException(
			Errors.GET_SMS_FIRST.getCode(), Errors.GET_SMS_FIRST.getMsg());

	public static final CashierBusinessException GET_SMS_AGAIN = new CashierBusinessException(
			Errors.GET_SMS_AGAIN.getCode(), Errors.GET_SMS_AGAIN.getMsg());

	public static final CashierBusinessException THRANS_FINISHED = new CashierBusinessException(
			Errors.THRANS_FINISHED.getCode(), Errors.THRANS_FINISHED.getMsg());

	public static final CashierBusinessException INVALID_BANK_CARD_NO =
			new CashierBusinessException(Errors.INVALID_BANK_CARD_NO.getCode(),
					Errors.INVALID_BANK_CARD_NO.getMsg());

	public static final CashierBusinessException THRANS_EXP_DATE = new CashierBusinessException(
			Errors.THRANS_EXP_DATE.getCode(), Errors.THRANS_EXP_DATE.getMsg());

	public static final CashierBusinessException PLZ_DEBIT_PAY = new CashierBusinessException(
			Errors.PLZ_DEBIT_PAY.getCode(), Errors.PLZ_DEBIT_PAY.getMsg());

	public static final CashierBusinessException PLZ_CREDIT_PAY = new CashierBusinessException(
			Errors.PLZ_CREDIT_PAY.getCode(), Errors.PLZ_CREDIT_PAY.getMsg());
	public static final CashierBusinessException AMOUNT_OUT_RANGE = new CashierBusinessException(
			Errors.AMOUNT_OUT_RANGE.getCode(), Errors.AMOUNT_OUT_RANGE.getMsg());

	public static final CashierBusinessException SUPPORT_BANKLIST_FAILED =
			new CashierBusinessException(Errors.SUPPORT_BANKLIST_FAILED.getCode(),
					Errors.SUPPORT_BANKLIST_FAILED.getMsg());

	public static final CashierBusinessException SYSTEM_BINDID_NULL = new CashierBusinessException(
			Errors.SYSTEM_BINDID_NULL.getCode(), Errors.SYSTEM_BINDID_NULL.getMsg());

	public static final CashierBusinessException SMS_SEND_FRILED = new CashierBusinessException(
			Errors.SMS_SEND_FRILED.getCode(), Errors.SMS_SEND_FRILED.getMsg());

	public static final CashierBusinessException CANT_REVERSE = new CashierBusinessException(
			Errors.CANT_REVERSE.getCode(), Errors.CANT_REVERSE.getMsg());

	public static final CashierBusinessException REPEAT_ORDER = new CashierBusinessException(
			Errors.REPEAT_ORDER.getCode(), Errors.REPEAT_ORDER.getMsg());

	public static final CashierBusinessException BUSINESS_NOT_SUPPORT =
			new CashierBusinessException(Errors.BUSINESS_NOT_SUPPORT.getCode(),
					Errors.BUSINESS_NOT_SUPPORT.getMsg());
	public static final CashierBusinessException SYSTEM_TOKEN_MISS = new CashierBusinessException(
			Errors.SYSTEM_TOKEN_MISS.getCode(), Errors.SYSTEM_TOKEN_MISS.getMsg());

	public static final CashierBusinessException NAME_NOT_NULL = new CashierBusinessException(
			Errors.NAME_NOT_NULL.getCode(), Errors.NAME_NOT_NULL.getMsg());

	public static final CashierBusinessException ID_NOT_NULL =
			new CashierBusinessException(Errors.ID_NOT_NULL.getCode(), Errors.ID_NOT_NULL.getMsg());

	public static final CashierBusinessException PASSWORD_NOT_NULL = new CashierBusinessException(
			Errors.PASSWORD_NOT_NULL.getCode(), Errors.PASSWORD_NOT_NULL.getMsg());

	public static final CashierBusinessException CVV2_NOT_NULL = new CashierBusinessException(
			Errors.CVV2_NOT_NULL.getCode(), Errors.CVV2_NOT_NULL.getMsg());

	public static final CashierBusinessException VALIDATE_NOT_NULL = new CashierBusinessException(
			Errors.VALIDATE_NOT_NULL.getCode(), Errors.VALIDATE_NOT_NULL.getMsg());

	public static final CashierBusinessException PHONE_NOT_NULL = new CashierBusinessException(
			Errors.PHONE_NOT_NULL.getCode(), Errors.PHONE_NOT_NULL.getMsg());

	public static final CashierBusinessException CARD_NOT_NULL = new CashierBusinessException(
			Errors.CARD_NOT_NULL.getCode(), Errors.CARD_NOT_NULL.getMsg());

	public static final CashierBusinessException VERIFYCODE_MISS = new CashierBusinessException(
			Errors.VERIFYCODE_MISS.getCode(), Errors.VERIFYCODE_MISS.getMsg());

	public static final CashierBusinessException INVALID_NAME = new CashierBusinessException(
			Errors.INVALID_NAME.getCode(), Errors.INVALID_NAME.getMsg());

	public static final CashierBusinessException INVALID_IDNO = new CashierBusinessException(
			Errors.INVALID_IDNO.getCode(), Errors.INVALID_IDNO.getMsg());

	public static final CashierBusinessException INVALID_PASSEORD = new CashierBusinessException(
			Errors.INVALID_PASSEORD.getCode(), Errors.INVALID_PASSEORD.getMsg());

	public static final CashierBusinessException INVALID_CVV2 = new CashierBusinessException(
			Errors.INVALID_CVV2.getCode(), Errors.INVALID_CVV2.getMsg());

	public static final CashierBusinessException INVALID_VALIDATE = new CashierBusinessException(
			Errors.INVALID_VALIDATE.getCode(), Errors.INVALID_VALIDATE.getMsg());

	public static final CashierBusinessException INVALID_PHONE = new CashierBusinessException(
			Errors.INVALID_PHONE.getCode(), Errors.INVALID_PHONE.getMsg());

	public static final CashierBusinessException GET_SMS_FREQUENT = new CashierBusinessException(
			Errors.GET_SMS_FREQUENT.getCode(), Errors.GET_SMS_FREQUENT.getMsg());

	public static final CashierBusinessException ENCODE_ERROR = new CashierBusinessException(
			Errors.ENCODE_ERROR.getCode(), Errors.ENCODE_ERROR.getMsg());

	public static final CashierBusinessException DECODE_ERROR = new CashierBusinessException(
			Errors.DECODE_ERROR.getCode(), Errors.DECODE_ERROR.getMsg());

	public static final CashierBusinessException SECURITY_ERROR = new CashierBusinessException(
			Errors.SECURITY_ERROR.getCode(), Errors.SECURITY_ERROR.getMsg());

	public static final CashierBusinessException CASHIER_CONFIG_NULL = new CashierBusinessException(
			Errors.CASHIER_CONFIG_NULL.getCode(), Errors.CASHIER_CONFIG_NULL.getMsg());
	
	public static final CashierBusinessException G3_BANK_CONVERT_MAP_NULL = new CashierBusinessException(
			Errors.G3_BANK_CONVERT_MAP_NULL.getCode(), Errors.G3_BANK_CONVERT_MAP_NULL.getMsg());

	public static final CashierBusinessException CASHIER_CONFIG_BANKS_NULL = new CashierBusinessException(
			Errors.CASHIER_CONFIG_BANKS_NULL.getCode(), Errors.CASHIER_CONFIG_BANKS_NULL.getMsg());

	public static final CashierBusinessException REQUEST_NOT_SAME = new CashierBusinessException(
			Errors.REQUEST_NOT_SAME.getCode(), Errors.REQUEST_NOT_SAME.getMsg());;
	
	public static final CashierBusinessException SYSTEM_EXCEPTION_REPAY = new CashierBusinessException(E3000117,"系统异常，请重新支付");

	public static final CashierBusinessException CARD_INFO_NOT_SAME = new CashierBusinessException(
			Errors.CARD_INFO_NOT_SAME.getCode(), Errors.CARD_INFO_NOT_SAME.getMsg());

	public static final CashierBusinessException OUT_BIND_CARD_LIMIT = new CashierBusinessException(
			Errors.OUT_BIND_CARD_LIMIT.getCode(), Errors.OUT_BIND_CARD_LIMIT.getMsg());
	
	/**
	 * 实例化CashierBusinessException
	 * @return
	 */
	public CashierBusinessException newInstance(){
		CashierBusinessException copy = new CashierBusinessException(this.defineCode, this.message);
		return copy;
	}

	@Override
	public String toString() {
		return super.toString()+" errorCode:"+this.getDefineCode()+" errorMsg:"+this.getMessage();
	}
	
	
}
