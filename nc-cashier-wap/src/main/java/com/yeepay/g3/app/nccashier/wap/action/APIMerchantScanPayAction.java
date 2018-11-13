package com.yeepay.g3.app.nccashier.wap.action;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.app.nccashier.wap.service.NcCashierService;
import com.yeepay.g3.app.nccashier.wap.service.impl.APIMerchantScanServiceImpl;
import com.yeepay.g3.app.nccashier.wap.utils.CommonUtil;
import com.yeepay.g3.app.nccashier.wap.service.APIMerchantScanService;
import com.yeepay.g3.app.nccashier.wap.vo.APIMerchantScanPayReponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.APIMerchantScanPayRequestVO;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

/**
 * 被扫支付api
 * 
 * @author duangduang
 * @since 2017-02-17
 */
@Controller
@RequestMapping(value = "/cashier", method = { RequestMethod.POST, RequestMethod.GET })
public class APIMerchantScanPayAction extends WapBaseAction{

	private static final Logger logger = LoggerFactory.getLogger(APIMerchantScanServiceImpl.class);
	
	@Resource
	private NcCashierService ncCashierService;

	@Resource
	private APIMerchantScanService apiMerchantScanService;

	@ResponseBody
	@RequestMapping(value = "/pqr")
	public Object payRequest(@Valid APIMerchantScanPayRequestVO param, BindingResult result, HttpServletRequest request) {
		APIMerchantScanPayReponseVO response = new APIMerchantScanPayReponseVO();
		try{
			// 参数校验
			if (result.hasErrors()) {
				response.setMessage(result.getFieldError().getDefaultMessage());
			} else {
				// 扫码类型校验
				String codeType = checkCodeType(param.getCodeType());
				// 时间戳校验
				CommonUtil.checkUrlOutOfExpDate(Long.parseLong(param.getTimestamp()));
				// 验签
				String appKey = CommonUtil.checkBizType(param.getBizType()) ? param.getMerchantNo():null;
				boolean isSuccess = ncCashierService.yopVerify(appKey, param.generateSignContent(), param.getSign());
				if (isSuccess) {
					String ypip = getUserIp(request);
					param.setCodeType(codeType);
					apiMerchantScanService.pay(param, ypip);
				} else {
					throw new CashierBusinessException(Errors.SECURITY_ERROR.getCode(), Errors.SECURITY_ERROR.getMsg());
				}

			}
		}catch (Throwable t) {
			logger.warn("商家扫码支付异常,param:{}, e:{}", param, t);
			CashierBusinessException e = (t instanceof CashierBusinessException)
					? (CashierBusinessException) t
					: new CashierBusinessException(Errors.SYSTEM_EXCEPTION.getCode(), Errors.SYSTEM_EXCEPTION.getMsg());
			response.setMessage(e.getMessage());
		}
		// 易宝私钥签名
		return writeResponse(response);

	}
	
	/**
	 * 检查扫码类型
	 * 
	 * @param codeType
	 * @return
	 */
	private String checkCodeType(String codeType) {
		String payType = CommonUtil.getPayTypeByScanType(codeType);
		if(StringUtils.isBlank(payType)){
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), "扫码类型错误");
		}
		return payType;
	}
	
	/**
	 * 对返回值进行签名，并返回
	 * 
	 * @param appKey
	 * @param response
	 * @return
	 */
	private String writeResponse(APIMerchantScanPayReponseVO response) {
		response.setCode(StringUtils.isNotBlank(response.getMessage()) ? "9999" : "0000");
		try {
			String signRes = ncCashierService.sign(response.generateSignContent());
			response.setSign(signRes);
		} catch (Throwable t) {
			logger.error("获取签名失败, t:{}", t);
		}
		return JSON.toJSONString(response);
	}


}
