package com.yeepay.g3.app.nccashier.wap.vo;

/**
 * 
 * @Description 商户选择收银台模版实体
 * @author yangmin.peng
 * @since 2017年6月16日上午11:39:42
 */
public class MerchantCashierCustomizedLayoutSelectVO extends ResponseVO {

	private static final long serialVersionUID = 1L;

	/**
	 * 商编
	 */
	private String merchantNo;

	/**
	 * 模板编码（实际上存储的是商编/易宝默认）
	 */
	private String layoutNo;

	/**
	 * 模板主色
	 */
	private String frontColor;
	/**
	 * 模版配色
	 */
	private String backColor;
	/**
	 * 支付产品排序
	 */
	private String[] payToolOrder;

	/**
	 * 是否展示在线客服
	 */
	private boolean needCustomService;
	/**
	 * 在线客服
	 */
	private String servicePhone;
	/**
	 * logo编码（实际上存储的是商编/易宝默认）
	 */
	private String logoNo;

	/**
	 * 模板文件更新版本号（用于收银台判断是否需要重新获取文件）
	 */
	private String layoutUpdateVersion;

	/**
	 * logo文件更新版本号（用于收银台判断是否需要重新获取文件）
	 */
	private String logoUpdateVersion;

	public String getMerchantNo() {
		return merchantNo;
	}

	public void setMerchantNo(String merchantNo) {
		this.merchantNo = merchantNo;
	}

	public String getLayoutNo() {
		return layoutNo;
	}

	public void setLayoutNo(String layoutNo) {
		this.layoutNo = layoutNo;
	}

	public String[] getPayToolOrder() {
		return payToolOrder;
	}

	public void setPayToolOrder(String[] payToolOrder) {
		this.payToolOrder = payToolOrder;
	}

	public boolean isNeedCustomService() {
		return needCustomService;
	}

	public void setNeedCustomService(boolean needCustomService) {
		this.needCustomService = needCustomService;
	}

	public String getLogoNo() {
		return logoNo;
	}

	public void setLogoNo(String logoNo) {
		this.logoNo = logoNo;
	}

	public String getFrontColor() {
		return frontColor;
	}

	public void setFrontColor(String frontColor) {
		this.frontColor = frontColor;
	}

	public String getBackColor() {
		return backColor;
	}

	public void setBackColor(String backColor) {
		this.backColor = backColor;
	}

	public String getServicePhone() {
		return servicePhone;
	}

	public void setServicePhone(String servicePhone) {
		this.servicePhone = servicePhone;
	}

	public String getLayoutUpdateVersion() {
		return layoutUpdateVersion;
	}

	public void setLayoutUpdateVersion(String layoutUpdateVersion) {
		this.layoutUpdateVersion = layoutUpdateVersion;
	}

	public String getLogoUpdateVersion() {
		return logoUpdateVersion;
	}

	public void setLogoUpdateVersion(String logoUpdateVersion) {
		this.logoUpdateVersion = logoUpdateVersion;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("MerchantCashierCustomizedLayoutSelectDTO{");
		sb.append("merchantNo='").append(merchantNo).append('\'');
		sb.append("frontColor='").append(frontColor).append('\'');
		sb.append("backColor='").append(backColor).append('\'');
		sb.append("payToolOrder='").append(payToolOrder).append('\'');
		sb.append("needCustomService='").append(needCustomService).append('\'');
		sb.append("servicePhone='").append(servicePhone).append('\'');
		sb.append("layoutUpdateVersion='").append(layoutUpdateVersion).append('\'');
		sb.append("logoUpdateVersion='").append(logoUpdateVersion).append('\'');
		sb.append("," + super.toString());
		sb.append('}');
		return sb.toString();

	}
}
