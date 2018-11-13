package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class ActivityInfoOfPayProductDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<String> copyWrites;

	private List<String> activityIds;

	public ActivityInfoOfPayProductDTO() {

	}

	public void init() {
		this.activityIds = new ArrayList<String>();
		this.copyWrites = new ArrayList<String>();
	}

	public List<String> getCopyWrites() {
		return copyWrites;
	}

	public void setCopyWrites(List<String> copyWrites) {
		this.copyWrites = copyWrites;
	}

	public List<String> getActivityIds() {
		return activityIds;
	}

	public void setActivityIds(List<String> activityIds) {
		this.activityIds = activityIds;
	}

	public void addActivityId(String activityId) {
		activityIds.add(activityId);
	}

	public void addCopyWrites(String copyWrite) {
		copyWrites.add(copyWrite);
	}

	public String toString(){
		return JSON.toJSONString(this);
	}
}
