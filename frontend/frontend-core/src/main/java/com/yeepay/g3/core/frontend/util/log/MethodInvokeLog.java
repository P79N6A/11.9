package com.yeepay.g3.core.frontend.util.log;

import java.lang.reflect.Field;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.stereotype.Component;

import com.yeepay.g3.core.frontend.util.HiddenCodeUtil;

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

	// 敏感信息判断暂时在此添加 added by zhijun.wang 2017-06-29
	public String getObjectStringforBankNotify(Object object) {
		if (null == object) {
			return null;
		}
//		if (isOverrideToString(object)) {// 原DTO中如果重写了toString方法，则用该方法
//			return object.toString();
//		}
		Field[] fields = object.getClass().getDeclaredFields();
		StringBuffer sb = new StringBuffer();
		try {
			for(Field field : fields) {
				field.setAccessible(true);
				if("serialVersionUID".equals(field.getName())) {
					continue;
				}
				if("payerBankAccountNo".equals(field.getName())) {
					sb.append(field.getName() + "=" + HiddenCodeUtil.hiddenBankCardNO(String.valueOf(field.get(object))) + ",");
					continue;
				}
				sb.append(field.getName() + "=" + String.valueOf(field.get(object)) + ",");
			}
		}catch (Exception e) {
			return super.toString();
		}
		return sb.substring(0, sb.length() - 1);
	}


}
