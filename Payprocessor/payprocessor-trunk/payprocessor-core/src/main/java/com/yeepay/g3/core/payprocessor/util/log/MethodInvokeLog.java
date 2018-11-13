package com.yeepay.g3.core.payprocessor.util.log;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.stereotype.Component;

import com.yeepay.g3.core.payprocessor.util.HiddenCodeUtil;

@Component
public class MethodInvokeLog {


	/**
	 * 获取对象的显示信息.（对敏感信息进行掩码)
	 * @param o
	 * @return
     */
	public String getObjectString(Object o) {
		if (null == o)
			return null;
		if (HiddenCodeUtil.isMobileNo(o)) {
			return HiddenCodeUtil.hiddenMobile(o.toString());
		}
		if (HiddenCodeUtil.isBankCardNo(o)) {
			return HiddenCodeUtil.hiddenBankCardNO(o.toString());
		}
		if (isOverrideToString(o)) {
			return o.toString();
		}
		return ToStringBuilder.reflectionToString(o);

	}

	private boolean isOverrideToString(Object o) {
		if (o == null) {
			return false;
		}
		try {
			o.getClass().getDeclaredMethod("toString", new Class[] {});
		} catch (NoSuchMethodException e) {
			return false;
		}
		return true;
	}



}
