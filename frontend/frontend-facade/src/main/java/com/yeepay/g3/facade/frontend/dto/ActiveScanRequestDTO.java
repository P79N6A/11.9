/**
 * 
 */
package com.yeepay.g3.facade.frontend.dto;


import org.apache.commons.lang.builder.ToStringBuilder;
import java.io.Serializable;

/**
 * 用户主扫请求DTO
 * @author TML
 */
@Deprecated
public class ActiveScanRequestDTO extends BasicRequestDTO implements Serializable{

	private static final long serialVersionUID = -1113397503659648043L;

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this);
	}
	
}
