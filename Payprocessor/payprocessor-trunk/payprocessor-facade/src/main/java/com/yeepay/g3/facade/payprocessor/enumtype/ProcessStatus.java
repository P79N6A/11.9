
package com.yeepay.g3.facade.payprocessor.enumtype;

/**
 * @author peile.fan
 *
 */
public enum ProcessStatus {

	SUCCESS("处理成功"), FAILED("处理失败");

	private final String value;

	public String getValue() {
		return value;
	}

	ProcessStatus(String value) {
		this.value = value;
	}

}
