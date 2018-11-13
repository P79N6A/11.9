/**
 * 
 */
package com.yeepay.g3.core.nccashier.gateway.service.impl;

import org.springframework.stereotype.Service;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.yeepay.g3.core.nccashier.gateway.service.YOPService;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.facade.yop.ca.dto.DigitalSignatureDTO;
import com.yeepay.g3.facade.yop.ca.enums.CertTypeEnum;
import com.yeepay.g3.facade.yop.ca.enums.DigestAlgEnum;
import com.yeepay.g3.facade.yop.ca.exceptions.EncryptFailedException;
import com.yeepay.g3.facade.yop.ca.exceptions.SignFailedException;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

/**
 * @author zhen.tan
 *
 */
@Service
public class YOPServiceImpl extends NcCashierBaseService implements YOPService {
	
	private static final Logger logger = LoggerFactory.getLogger(YOPServiceImpl.class);

	private static final String YEEPAY_APP_KEY = "_YOP";
	private static final String MERCHANT_APP_KEY_PREFIX = "OPR:";
	
	/* (non-Javadoc)
	 * @see com.yeepay.g3.core.nccashier.gateway.service.YOPService#sign(java.lang.String)
	 */
	@Override
	public String sign(String plaintext) {
		String signature = "";
		DigitalSignatureDTO digitalEnvelopeDTO = new DigitalSignatureDTO();
		digitalEnvelopeDTO.setAppKey(YEEPAY_APP_KEY);
		digitalEnvelopeDTO.setCertType(CertTypeEnum.RSA2048);
		digitalEnvelopeDTO.setDigestAlg(DigestAlgEnum.SHA256);
		digitalEnvelopeDTO.setPlainText(plaintext);
		try {
			digitalEnvelopeDTO = digitalSecurityFacade.sign(digitalEnvelopeDTO);
			signature = digitalEnvelopeDTO.getSignature();
		}catch(SignFailedException se){
			logger.error("调用yop签名异常 plaintext:{}, e:{}", plaintext, se);
		}catch (EncryptFailedException ee) {
			logger.error("调用yop签名异常 plaintext:{}, e:{}", plaintext, ee);
		}catch (Throwable te) {
			try {
				digitalEnvelopeDTO = digitalSecurityFacade.sign(digitalEnvelopeDTO);
				signature = digitalEnvelopeDTO.getSignature();
			}catch(Throwable t){
				logger.error("调用yop签名异常 plaintext:{}, e:{}", plaintext, t);
			}
			
		}
		return signature;
	}
	
	

	/* (non-Javadoc)
	 * @see com.yeepay.g3.core.nccashier.gateway.service.YOPService#verify(java.lang.String, java.lang.String)
	 */
	@Override
	public boolean verify(String appKey, String plaintext, String signature) {
		boolean isSuccess = false;
		appKey = StringUtils.isBlank(appKey)?YEEPAY_APP_KEY:(MERCHANT_APP_KEY_PREFIX+appKey);
		DigitalSignatureDTO digitalEnvelopeDTO = new DigitalSignatureDTO();
		digitalEnvelopeDTO.setAppKey(appKey);
		digitalEnvelopeDTO.setCertType(CertTypeEnum.RSA2048);
		digitalEnvelopeDTO.setDigestAlg(DigestAlgEnum.SHA256);
		digitalEnvelopeDTO.setPlainText(plaintext);
		digitalEnvelopeDTO.setSignature(signature);
		try {
			digitalSecurityFacade.verify(digitalEnvelopeDTO);
			isSuccess = true;
		} catch (Throwable e) {
			logger.error("verifySign appKey:{}, plaintext:{},signature:{},e:{}",appKey, plaintext, signature, e);
		}
		return isSuccess;
	}

}
