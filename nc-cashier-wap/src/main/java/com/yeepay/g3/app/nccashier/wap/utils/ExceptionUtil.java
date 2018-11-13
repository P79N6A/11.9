package com.yeepay.g3.app.nccashier.wap.utils;

import com.yeepay.g3.facade.foundation.dto.DefaultErrorCode;
import com.yeepay.g3.facade.foundation.dto.ErrorMeta;
import com.yeepay.g3.facade.nccashier.dto.ErrorCodeDTO;
import com.yeepay.g3.facade.nccashier.enumtype.SysCodeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import com.yeepay.g3.utils.foundation.util.ErrorCodeUtility;

public class ExceptionUtil {

	private static final Logger logger = LoggerFactory.getLogger(ExceptionUtil.class);

	/**
	 * controller层异常处理，主要处理catchException的逻辑
	 * NCCASHIER、PC、WAP的错误码不变，相当于直接取NCCASHIER_PC、 NCCASHIER_WAP的错误描述信息即可
	 * 
	 * @param e
	 *            被调用系统(NCCASHIER)抛出的异常
	 * @param sysCode
	 *            调用系统的系统编码 (NCCASHIER_PC 或 NCCASHIER_WAP)
	 * @return
	 */
	public static CashierBusinessException handleException(Throwable e, SysCodeEnum sysCode) {
		// e为空或者非CashierBusinessException类型时，都会置为系统异常
		CashierBusinessException calledSysException = (e instanceof CashierBusinessException)
				? (CashierBusinessException) e
				: new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
		return getSelfErrorMsg(calledSysException, sysCode);
	}
	
	/**
	 * controller层异常处理，主要处理直接抛出Errors的地方
	 * NCCASHIER、PC、WAP的错误码不变，相当于直接取NCCASHIER_PC、 NCCASHIER_WAP的错误描述信息即可
	 * @param e
	 *            被调用系统(NCCASHIER)返回的错误码枚举值；为空时，置为Errors.SYSTEM_EXCEPTION
	 * @param sysCode
	 *            调用系统的系统编码
	 * @return
	 */
	public static CashierBusinessException handleException(Errors e, SysCodeEnum sysCode) {
		e = (e == null) ? Errors.SYSTEM_EXCEPTION : e;
		return getSelfErrorMsg(new CashierBusinessException(e.getCode(), e.getMsg()), sysCode);
	}
	
	/**
	 * controller层异常处理，主要处理从某个实体中（如DTO）取出errorCode和errorMsg的地方
	 * NCCASHIER、PC、WAP的错误码不变，相当于直接取NCCASHIER_PC、 NCCASHIER_WAP的错误描述信息即可
	 * 
	 * @param errorCode
	 *            被调用方抛出的错误码
	 * @param errorMsg
	 *            被调用方抛出的错误描述信息
	 * @param sysCode
	 * @return 调用方的错误码和错误描述信息
	 */
	public static ErrorCodeDTO handleException(String errorCode, String errorMsg, SysCodeEnum sysCode) {
		logger.info("handleException errorCode:{}, errorMsg:{}, sysCode:{}", errorCode, errorMsg, sysCode);
		ErrorCodeDTO response = mapSelfErrorMsg(errorCode, errorMsg, sysCode);
		logger.info("转换后 errorCode:{}, errorMsg:{}", response.getExternalErrorCode(), response.getExternalErrorMsg());
		return response;
	}
	
	/**
	 * 
	 * @param oriException
	 * @param sysCode
	 * @return
	 */
	private static CashierBusinessException getSelfErrorMsg(CashierBusinessException oriException, SysCodeEnum sysCode) {
		String oriErrorCode = (oriException == null || StringUtils.isBlank(oriException.getDefineCode()))
				? Errors.SYSTEM_EXCEPTION.getCode() : oriException.getDefineCode();
		String oriErrorMsg = (oriException == null) ? Errors.SYSTEM_EXCEPTION.getMsg() : oriException.getMessage();
		ErrorCodeDTO response = mapSelfErrorMsg(oriErrorCode, oriErrorMsg, sysCode);
		logger.info("转换后 errorCode:{}, errorMsg:{}", response.getExternalErrorCode(), response.getExternalErrorMsg());
		return new CashierBusinessException(response.getExternalErrorCode(), response.getExternalErrorMsg());
	}
	
	/**
	 * 根据错误码获取描述信息主逻辑
	 * 
	 * @param oriErrorCode
	 * @param oriErrorMsg
	 * @param objSystemCode
	 * @return externalErrorCode&externalErrorMsg都可能为空
	 */
	private static ErrorCodeDTO mapSelfErrorMsg(String oriErrorCode, String oriErrorMsg, SysCodeEnum objSystemCode) {
		objSystemCode = (objSystemCode == null) ? SysCodeEnum.NCCASHIER_WAP : objSystemCode;
		boolean isOpen = CommonUtil.getErrorCodeManageSwitch();
		if (isOpen && StringUtils.isNotBlank(oriErrorCode)) {
			try {
				String objErrorMsg = ErrorCodeUtility.retrieveErrorCodeMsg(objSystemCode.name(), oriErrorCode);
				logger.info("mapSelfErrorMsg 错误码管理系统返回值objErrorMsg:{}", objErrorMsg);
				objErrorMsg = StringUtils.isBlank(objErrorMsg) ? oriErrorMsg : objErrorMsg;
				return buildErrorCodeDTO(oriErrorCode, objErrorMsg);
			} catch (Throwable t) {
				logger.error("[monitor],event:nccashierwap_mapSelfErrorMsg,oriErrorCode:{},oriErrorMsg:{},e:{}",
						oriErrorCode, oriErrorMsg, t);
			}
		}
		return buildErrorCodeDTO(oriErrorCode, oriErrorMsg);
	}
	
	private static ErrorCodeDTO buildErrorCodeDTO(String objErrorCode, String objErrorMsg){
		ErrorCodeDTO errorCodeDTO = new ErrorCodeDTO();
		errorCodeDTO.setExternalErrorCode(objErrorCode);
		errorCodeDTO.setExternalErrorMsg(objErrorMsg);
		return errorCodeDTO;
	}

	/**
	 * wap系统间异常通用处理
	 * 
	 * @param callerSysCode
	 *            调用系统编码 (SysCodeEnum.NCCASHIER_WAP || SysCodeEnum.NCCASHIER_PC)
	 * @param calledSysException
	 *            被调用系统抛出的异常（这里被调用系统恒为NCCASHIER） 被调用方的错误码和错误描述信息都要求不能为空
	 * @return
	 */
	public static CashierBusinessException transferException(SysCodeEnum callerSysCode,
			CashierBusinessException calledSysException) {

		logger.info("transferException callerSysCode:{}, calledSysException:{}", callerSysCode, calledSysException);
		// NCCASHIER转NCCASHIER，直接返回
		if (SysCodeEnum.NCCASHIER.equals(callerSysCode)) {
			return calledSysException;
		}
		// 入参校验
		validateTransferExptParam(callerSysCode, calledSysException);
		if (StringUtils.isBlank(calledSysException.getDefineCode())) {
			return calledSysException;
		}
		// 映射系统间错误码
		return mapException(callerSysCode, calledSysException);
	}

	/**
	 * 系统间错误码映射
	 * 
	 * @param callerSysCode
	 * @param calledSysException
	 * @return
	 */
	private static CashierBusinessException mapException(SysCodeEnum callerSysCode,
			CashierBusinessException calledSysException) {
		// 获取错误码开关
		boolean isOpen = CommonUtil.getErrorCodeManageSwitch();
		if (isOpen) {
			try {
				DefaultErrorCode defaultErrorCode = new DefaultErrorCode(callerSysCode.name(),
						Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
				ErrorMeta objErrorCode = ErrorCodeUtility.mapErrorMeta(SysCodeEnum.NCCASHIER.name(),
						calledSysException.getDefineCode(), calledSysException.getMessage(), defaultErrorCode);
				logger.info("mapException-错误码系统返回值objErrorCode:{}", objErrorCode);
				if (objErrorCode != null) {
					return new CashierBusinessException(objErrorCode.getErrorCode(), objErrorCode.getErrorMsg());
				}
			} catch (Throwable t) {
				logger.error("[monitor],event:nccashierwap_mapException,callerSysCode:{}, calledSysException:{},e:{}",
						callerSysCode, calledSysException, t);
			}
		}

		// 开关关 || 错误码管理系统返回值为空/异常 —— 原始异常抛出
		return new CashierBusinessException(calledSysException.getDefineCode(), calledSysException.getMessage());
	}

	/**
	 * wap系统间异常通用处理方法入参校验
	 * 
	 * @param callerSysCode
	 *            默认为SysCodeEnum.NCCASHIER_WAP
	 * @param calledSysException
	 *            为空时，置为Errors.SYSTEM_EXCEPTION
	 */
	private static void validateTransferExptParam(SysCodeEnum callerSysCode,
			CashierBusinessException calledSysException) {

		// 调用系统编码为空，默认为WAP
		callerSysCode = (callerSysCode == null) ? SysCodeEnum.NCCASHIER_WAP : callerSysCode;
		// 被调用系统抛出的异常为空时，置为系统异常
		calledSysException = (calledSysException == null)
				? new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg())
				: calledSysException;

	}
}
