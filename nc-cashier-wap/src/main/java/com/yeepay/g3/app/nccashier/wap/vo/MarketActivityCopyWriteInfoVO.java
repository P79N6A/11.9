package com.yeepay.g3.app.nccashier.wap.vo;

import java.io.Serializable;

import com.alibaba.fastjson.JSON;

public class MarketActivityCopyWriteInfoVO implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 当前的描述文案
	 */
	private String copyWrite;
	
	private String key;

	
	private MarketActivityCopyWriteInfoVO childCopyWriteInfo;

	public MarketActivityCopyWriteInfoVO() {

	}

	public String getCopyWrite() {
		return copyWrite;
	}

	public void setCopyWrite(String copyWrite) {
		this.copyWrite = copyWrite;
	}
	

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public MarketActivityCopyWriteInfoVO getChildCopyWriteInfo() {
		return childCopyWriteInfo;
	}

	public void setChildCopyWriteInfo(MarketActivityCopyWriteInfoVO childCopyWriteInfo) {
		this.childCopyWriteInfo = childCopyWriteInfo;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
