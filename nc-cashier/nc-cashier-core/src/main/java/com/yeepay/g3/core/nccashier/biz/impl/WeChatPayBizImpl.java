/**
 * 
 */
package com.yeepay.g3.core.nccashier.biz.impl;

import com.yeepay.g3.core.nccashier.biz.WeChatPayBiz;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.WeChatOrderPaymentService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.utils.RedisTemplate;
import com.yeepay.g3.core.nccashier.validator.BeanValidator;
import com.yeepay.g3.core.nccashier.vo.CombinedPaymentDTO;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.JsapiRouteRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.JsapiRouteResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.WeChatPayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.WeChatPayResponseDTO;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author zhen.tan
 *
 */
@Service
public class WeChatPayBizImpl extends NcCashierBaseBizImpl implements WeChatPayBiz {
	
	private static Logger log = LoggerFactory.getLogger(WeChatPayBiz.class);
	
	@Resource
	private WeChatOrderPaymentService weChatOrderPaymentService;


	public WeChatPayResponseDTO pay(WeChatPayRequestDTO requestDto) {
		WeChatPayResponseDTO response = new WeChatPayResponseDTO();
		try {
			basicValidatePaymentRequest(requestDto,response);
			
			CombinedPaymentDTO combinedPaymentDto = weChatOrderPaymentService.validatePayBusinInfo(requestDto);
			String result = null;
			if (combinedPaymentDto.isNeedOrderRecord()) {
				weChatOrderPaymentService.weChatCreateRecord(requestDto, combinedPaymentDto);
			}else{
				//当不需创建record时，先尝试从redis取url，如有直接返回；如无调用FE接口
				result = getQrCodeFromCache(requestDto.getRecordId());
				if(StringUtils.isNotEmpty(result)){
					response.setResult(result);
					return response;
				}
			}
			result = weChatOrderPaymentService.callFEPay(requestDto, combinedPaymentDto);
			response.setResult(result);
			saveQrCodeIntoCache(requestDto.getRecordId(),result);
			return response;
		} catch (Throwable e) {
			handleException(response, e);
			return response;
		}
	}

	@Override
	public JsapiRouteResponseDTO weChatRoute(JsapiRouteRequestDTO jsapiRouteRequestDTO) {
		JsapiRouteResponseDTO jsapiRouteResponseDTO = new JsapiRouteResponseDTO();
		try{
			if(jsapiRouteRequestDTO==null || StringUtils.isBlank(jsapiRouteRequestDTO.getRequestId())){
				throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
			}
			NcCashierLoggerFactory.TAG_LOCAL.set("[微信预路由请求|weChatRoute] - [requestId=" + jsapiRouteRequestDTO.getRequestId() + "]");
			PaymentRequest paymentRequest = paymentRequestService.findPayRequestById(Long.parseLong(jsapiRouteRequestDTO.getRequestId()));
			return weChatOrderPaymentService.weChatPreRoute(paymentRequest, jsapiRouteRequestDTO.getPayType());
		}catch (Throwable e){
			handleException(jsapiRouteResponseDTO,e);
		}
		return jsapiRouteResponseDTO;
	}

	/**
	 * 下单入参基本校验
	 * MerchantInNetConfigModel
	 * @param requestDto
	 * @param response
	 */
	private void basicValidatePaymentRequest(WeChatPayRequestDTO requestDto,
			WeChatPayResponseDTO response) {
		BeanValidator.validate(requestDto);
		response.setTokenId(requestDto.getTokenId());
		NcCashierLoggerFactory.TAG_LOCAL.set("[WeChatcreatePayment],支付请求ID=" + requestDto.getRequestId() + ",支付记录ID="
						+ requestDto.getRecordId() + "");
	}

	/**
	 * 优先尝试从缓存中取二维码url
	 * 
	 * @param recordId
	 * @return
	 */
	private String getQrCodeFromCache(long recordId) {
		try {
			if (recordId <= 0) {
				// 这个分支不可能走到
				return null;
			}
			return RedisTemplate.getTargetFromRedis(Constant.NCCASHIER_QRCODE_REDIS_KEY + recordId,
					String.class);
		} catch (Exception e) {
			// 这个日志没有必要，因为getTargetFromRedis方法内已经打了
			return null;
		}

	}

	/**
	 * 保存二维码url到缓存
	 * @param recordId
	 * @param url
	 */
	private void saveQrCodeIntoCache(long recordId,String url) {
		try {
			if (recordId <= 0 || StringUtils.isEmpty(url)) {
				return;
			}
			int expireTime = CommonUtil.getQRCodeCacheExpireTime();
			RedisTemplate.setCacheObjectSumValue(Constant.NCCASHIER_QRCODE_REDIS_KEY + recordId, url, expireTime * 1000);
		} catch (Exception e) {
			// 基本上只可能是因为redis挂了，或获取不到redis连接才会报错
			log.error("尝试往redis写入二维码缓存失败，异常为：",e);
			return;
		}
	}
}
