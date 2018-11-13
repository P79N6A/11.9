package com.yeepay.g3.core.frontend.errorcode;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.yeepay.g3.core.frontend.common.RemoteFacadeProxyFactory;
import com.yeepay.g3.core.frontend.enumtype.ExternalSystem;
import com.yeepay.g3.core.frontend.util.log.FeLoggerFactory;
import com.yeepay.g3.facade.ncconfig.facade.ConfigErrorCodeFacade;
import com.yeepay.g3.facade.ncconfig.param.CatchErrorCodeParam;
import com.yeepay.g3.facade.ncconfig.result.ErrorCodeDTO;
import com.yeepay.g3.utils.common.CheckUtils;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;

/**
 * 通过错误码系统获取错误码系统<br/>
 */
public class SystemErrorCodeTranslator implements ErrorCodeTranslator {


	public static final String FRONTEND_SYS_CODE = "FRONTEND";
	
	private ConfigErrorCodeFacade configErrorCodeFacade = RemoteFacadeProxyFactory.getService(
			ConfigErrorCodeFacade.class, ExternalSystem.NCCONFIG);

	private static final Logger logger = FeLoggerFactory.getLogger(SystemErrorCodeTranslator.class);

	private static final String DEFAULT_ERROR_SYS_CODE = "00000";


	/**
	 * 单例模式
	 */
	private static class Instance {
		private static SystemErrorCodeTranslator instance = new SystemErrorCodeTranslator();
	}

	private SystemErrorCodeTranslator() {}

	public static SystemErrorCodeTranslator getInstance() {
		return Instance.instance;
	}

	@Override
	public String getMessage(String errorCode) {

		if (StringUtils.isNotEmpty(errorCode)) {
			try {
				ErrorCodeDTO errorCodeDTO =
						configErrorCodeFacade.catchSelfErrorCode(FRONTEND_SYS_CODE, errorCode);
				if (errorCodeDTO != null && !StringUtils.startsWith(errorCodeDTO.getErrorCode(), DEFAULT_ERROR_SYS_CODE)) {
					return errorCodeDTO.getErrorCodeInfo();
				} else {
					logger.error("[Outer] - [系统异常] - [ConfigErrorCodeFacade.catchSelfErrorCode] - 未能从错误码配置系统中找到对应错误信息： errorCode:"
							+ errorCode);
				}
			} catch (Exception e) {
				logger.error(
						"[Outer] - [系统异常] - [ConfigErrorCodeFacade.catchSelfErrorCode] - 调用错误码配置系统出现未知异常：errorCode:"
								+ errorCode, e);
			}
		}
		return "";
	}

	@Override
	public String translateCode(ErrorCodeSource source, String errorCode, String errorMessage) {
		CheckUtils.notNull(source, "source");
		CheckUtils.notEmpty(errorCode, "errorCode");
		if (StringUtils.isNotEmpty(errorCode)) {
			CatchErrorCodeParam param = getErrorCodeParam(source.getSysCode(), errorCode, errorMessage);
			try {
				ErrorCodeDTO errorCodeDTO = configErrorCodeFacade.catchErrorCode(param);
				if (errorCodeDTO != null
						&& !DEFAULT_ERROR_SYS_CODE.equals(errorCodeDTO.getErrorCode())) {
					return errorCodeDTO.getErrorCode();
				}
			} catch (Exception e) {
				logger.error("[系统异常]调用错误码配置系统出现未知异常" + ToStringBuilder.reflectionToString((param)),
						e);
			}
		}
		return DEFAULT_ERROR_CODE;
	}

	private CatchErrorCodeParam getErrorCodeParam(String sysCode, String errorCode, String errMsg) {
		CatchErrorCodeParam param = new CatchErrorCodeParam();
		param.setSystemCode(FRONTEND_SYS_CODE);
		param.setSystemDefaultErrorCode(DEFAULT_ERROR_CODE);
		param.setBottomSystemCode(sysCode);
		param.setBottomSystemErrorCode(errorCode);
		param.setBottomSystemErrorCodeInfo(errMsg);
		return param;
	}


}
