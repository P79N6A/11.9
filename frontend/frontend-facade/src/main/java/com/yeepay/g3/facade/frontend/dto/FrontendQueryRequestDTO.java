package com.yeepay.g3.facade.frontend.dto;

import com.yeepay.g3.facade.frontend.enumtype.PlatformType;
import org.apache.commons.lang.builder.ToStringBuilder;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 查单请求DTO
 * @author TML
 *
 */
public class FrontendQueryRequestDTO implements Serializable{

	private static final long serialVersionUID = 8228245046657134261L;

	/**
	 * 业务方
	 */
	@NotNull(message = "requestSystem不能为空")
	private String requestSystem;

	/**
	 * 业务方订单号
	 */
	@NotNull(message = "requestId不能为空")
	private String requestId;
	

    /**
     * 支付平台类型
     */
	@NotNull(message = "platformType不能为空")
    private PlatformType platformType;
	
	
	public String getRequestSystem() {
		return requestSystem;
	}

	public void setRequestSystem(String requestSystem) {
		this.requestSystem = requestSystem;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public PlatformType getPlatformType() {
		return platformType;
	}


	public void setPlatformType(PlatformType platformType) {
		this.platformType = platformType;
	}


	@Override
	public String toString(){
		return ToStringBuilder.reflectionToString(this);
	}
	
}
