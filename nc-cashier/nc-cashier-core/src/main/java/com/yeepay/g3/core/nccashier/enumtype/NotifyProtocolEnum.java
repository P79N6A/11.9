package com.yeepay.g3.core.nccashier.enumtype;

public enum NotifyProtocolEnum {
	
	HESSIAN("HESSIAN", "Hessian"),HTTPINVOKER("HTTPINVOKER", "HttpInvoker"),
	HTTP_GET("GET", "GET请求"),HTTP_POST("POST", "POST请求"),MQ("MQ", "MQ通知");
	private String value;
	private String desc;
	private NotifyProtocolEnum(String value, String desc)
	{
	   this.value = value;
	  this.desc = desc;
	}

	public String getValue() {
	  return value;
	 }
	  
	public String getDesc() {
	 return desc;
	 }
}
