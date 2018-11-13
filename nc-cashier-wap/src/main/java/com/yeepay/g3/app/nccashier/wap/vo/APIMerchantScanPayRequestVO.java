package com.yeepay.g3.app.nccashier.wap.vo;

import com.yeepay.g3.app.nccashier.wap.utils.CommonUtil;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 被扫支付下单的请求入参
 * 
 * @author duangduang
 * @since 2017-02-17
 */
public class APIMerchantScanPayRequestVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/** 商户编号 **/
	@NotBlank(message="商户编号不能为空")
	private String merchantNo;
	
	/** 业务方类型 **/
	private String bizType;
	
	/** 订单处理器订单号 **/
	@NotBlank(message = "订单token不能为空")
	private String token;

	/** 订单时间戳 秒为单位**/
	@NotBlank(message = "时间戳不能为空")
	@Size(max = 20, message = "时间戳长度最长不能超过20")
	@Pattern(regexp = "^[0-9]{1,20}$", message = "时间戳必须由数字组成")
	private String timestamp;

	/** 扫码类型 **/
	@NotBlank(message = "扫码类型不能为空")
	private String codeType;

	/** 授权码 **/
	@NotBlank(message = "授权码不能为空")
	private String code;

	/** 门店编码 **/
	@NotBlank(message = "门店编码不能为空")
	@Size(max = 64, message = "门店编码长度最长不能超过64")
	private String storeCode;

	/** 设备号 **/
	@NotBlank(message = "设备号不能为空")
	@Size(max = 32, message = "设备号长度最长不能超过32")
	private String deviceSn;

	/** 签名 **/
	@NotBlank(message = "签名不能为空")
	private String sign;

	public APIMerchantScanPayRequestVO() {

	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(String timestamp) {
		this.timestamp = timestamp;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getStoreCode() {
		return storeCode;
	}

	public void setStoreCode(String storeCode) {
		this.storeCode = storeCode;
	}

	public String getDeviceSn() {
		return deviceSn;
	}

	public void setDeviceSn(String deviceSn) {
		this.deviceSn = deviceSn;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}
	
	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getBizType() {
		return bizType;
	}

	public void setBizType(String bizType) {
		this.bizType = bizType;
	}

	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
	
	/**
	 * 获取签名入参
	 * 
	 * @return
	 */
	public String generateSignContent() {
		StringBuilder str = new StringBuilder("merchantNo=").append(this.merchantNo)
				.append("&token=").append(this.token)
				.append("&timestamp=").append(this.timestamp)
				.append("&codeType=").append(codeType)
				.append("&code=").append(code)
				.append("&storeCode=").append(storeCode)
				.append("&deviceSn=").append(deviceSn);
		if (!CommonUtil.checkBizType(bizType)) {
			str.append("&bizType=").append(bizType);
		}
		return str.toString();
	}

}
