package com.yeepay.g3.app.fronend.app.enumtype;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.utils.common.StringUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * 订单来源枚举
 * @author haifeng.lu
 *
 */
public enum Source  {
	/**
	 * 线下pos支付
	 */
	POS("线下POS-终端",1,false),

	/**
	 * 线上支付
	 */
	ONLINE("线下POS-扫码支付",2,false),

	/**
	 * 线上网关支付
	 */
	GATEWAY("PC综合收银台",4,false),

	/**
	 * 公众号支付
	 */
	MPPAY("线下POS-公众号支付",8,false), 
	/**
	 * 收款宝
	 */
	SKB("收款宝",16,false),
	/**
	 * 掌柜通
	 */
	ZGT("掌柜通",32,false),
	/**
	 * 线上直销商户
	 */
	GENERAL("线上零部件",64,false);
	
	private String desc;
	private int value;
	// 是否页面选中
	private boolean select;
	
	Source(String desc,int value,boolean select){
		this.desc=desc;
		this.value=value;
		this.select = select;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}
	

	public boolean getSelect() {
		return select;
	}

	public void setSelect(boolean select) {
		this.select = select;
	}

	/**
	 * 位于运算转换成数字
	 * @param source
	 * @return
	 */

	public static Long getSource(String source) {
		JSONObject jsonObject=JSONObject.parseObject(source);
		SourceMode sourceMode=new SourceMode(0);
		for (Source s : Source.values()) {
			if(jsonObject.containsKey(s.name())){
				if(jsonObject.getBoolean(s.name())){
					sourceMode.allow(s.getValue());
				}
			}
		}
		return (long) sourceMode.getValue();
	}
	
	/**
	 * 位于运算转换成数字
	 * @param source
	 * @return
	 */
	public static Long getSourceArr(String sourceStr) {
		if(StringUtils.isBlank(sourceStr)){
			return null;
		}
		SourceMode sourceMode=new SourceMode(0);
		String[] sourceArray = sourceStr.split(",");
		for (int i = 0; i < sourceArray.length; i++) {
			if (StringUtils.isNotBlank(sourceArray[i])) {
				sourceMode.allow(Source.valueOf(sourceArray[i]).getValue());
			}
		}
		return (long) sourceMode.getValue();
	}
	
	/**
	 * 将int值转换为String
	 */
	public static List<String> getSourceList(int value) {
		List<String> list = new ArrayList<String>();
		for(Source source : Source.values()) {
			if(isAllow(value,source.getValue())){
				list.add(source.name());
			}
		}
		return list;
	}
	
	/**
	 * 将int值转换为desc
	 */
	public static List<String> getSourceDescList(int value) {
		List<String> list = new ArrayList<String>();
		for(Source source : Source.values()) {
			if(isAllow(value,source.getValue())){
				list.add(source.getDesc());
			}
		}
		return list;
	}
	
	public static final boolean isAllow(int value,int func){
		if(func < 0 || value < 0){
			throw new IllegalArgumentException();
		}
		return (value & func)==func;
	}
	
}
