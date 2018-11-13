/**
 * 
 */
package com.yeepay.g3.facade.frontend.dto;


import org.apache.commons.lang.builder.ToStringBuilder;
import com.yeepay.g3.facade.frontend.enumtype.PayLimitType;
import java.io.Serializable;

/**
 * app支付请求dto
 * @author TML
 *
 */
@Deprecated
public class AppPayRequestDTO extends BasicRequestDTO{

	private static final long serialVersionUID = 7188103173662580115L;

	/**
	 * 前端回调地址
	 */
	private String pageCallBack;
    
	/**
     * 支付限制类型
     */
    private PayLimitType payLimitType;
    
	public String getPageCallBack() {
		return pageCallBack;
	}

	public void setPageCallBack(String pageCallBack) {
		this.pageCallBack = pageCallBack;
	}

	public PayLimitType getPayLimitType() {
		return payLimitType;
	}

	public void setPayLimitType(PayLimitType payLimitType) {
		this.payLimitType = payLimitType;
	}

	@Override
    public String toString(){
    	return ToStringBuilder.reflectionToString(this);
    }

}
