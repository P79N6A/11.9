package com.yeepay.g3.app.nccashier.wap.vo;

import java.io.Serializable;

public class UserIpVO implements Serializable{

	private static final long serialVersionUID = 1L;

	private String remoteAddrIp;

	private String xForwardedForIp;
	
	private String ProxyClientIp;
	
	private String WLProxyClientIp;

	public UserIpVO() {

	}

	public String getRemoteAddrIp() {
		return remoteAddrIp;
	}

	public void setRemoteAddrIp(String remoteAddrIp) {
		this.remoteAddrIp = remoteAddrIp;
	}

	public String getxForwardedForIp() {
		return xForwardedForIp;
	}

	public void setxForwardedForIp(String xForwardedForIp) {
		this.xForwardedForIp = xForwardedForIp;
	}
	
	public String getProxyClientIp() {
		return ProxyClientIp;
	}

	public void setProxyClientIp(String proxyClientIp) {
		ProxyClientIp = proxyClientIp;
	}

	public String getWLProxyClientIp() {
		return WLProxyClientIp;
	}

	public void setWLProxyClientIp(String wLProxyClientIp) {
		WLProxyClientIp = wLProxyClientIp;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder("UserIpVO[");
		builder.append("xForwardedForIp=").append(xForwardedForIp).append(",")
		.append("remoteAddrIp=").append(remoteAddrIp);
		return builder.toString();
	}

}
