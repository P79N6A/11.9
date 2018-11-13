package com.yeepay.g3.app.nccashier.wap.utils;

import org.apache.commons.lang.StringUtils;

public class StringHelper {
	
	/**
	 * 校验objStr是否是sourceStr的子串
	 * 
	 * @param sourceStr
	 * @param objStr
	 * @return
	 */
	public static boolean contains(String sourceStr, String objStr) {
		if (StringUtils.isNotBlank(sourceStr) && StringUtils.isNotBlank(objStr)) {
			String sourceStrAfterFormat = sourceStr.replaceAll(" +", ""); // 把多个空格替换成空
			if (sourceStrAfterFormat.contains(objStr)) {
				return true;
			}
		}
		return false;
	}

}
