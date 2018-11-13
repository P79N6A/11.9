/**
 * 
 */
package com.yeepay.g3.core.payprocessor.enumtype;

import com.yeepay.g3.utils.common.StringUtils;

/**
 * Description:
 * @author peile.fan
 * @since:2017年2月6日 上午11:47:13
 */
public enum CancelStatusEnum {


	DOING("处理中"), SUCCESS("成功"), FAILURE("失败");

	private String desc;

	public String getDesc() {
		return desc;
	}

	CancelStatusEnum(String desc) {
		this.desc = desc;
	}

	public static CancelStatusEnum of(String status) {
		if (StringUtils.isBlank(status)) {
			return null;
		}
		try {
			return CancelStatusEnum.valueOf(status);
		} catch (Throwable th) {
			return null;
		}
	}


}
