package com.yeepay.g3.core.nccashier.vo;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

public class ActivityInfoOfPayProduct implements Serializable{

	private static final long serialVersionUID = -9113330504905889699L;

	private ArrayList<String> copyWrites;

	private ArrayList<String> activityIds;

	public ActivityInfoOfPayProduct() {

	}

	public void init() {
		this.activityIds = new ArrayList<String>();
		this.copyWrites = new ArrayList<String>();
	}

	public ArrayList<String> getCopyWrites() {
		return copyWrites;
	}

	public void setCopyWrites(ArrayList<String> copyWrites) {
		this.copyWrites = copyWrites;
	}

	public ArrayList<String> getActivityIds() {
		return activityIds;
	}

	public void setActivityIds(ArrayList<String> activityIds) {
		this.activityIds = activityIds;
	}

	public void addActivityId(String activityId) {
		if (!activityIds.contains(activityId)) {
			activityIds.add(activityId);
		}
	}

	public void addCopyWrites(String copyWrite) {
		if (!copyWrites.contains(copyWrite)) {
			copyWrites.add(copyWrite);
		}

	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	
	public static void main(String[] args){
		String text = "{\"NCPAY_CREDIT\":{\"activityIds\":[\"MKN1001201806250000000196\",\"MKN1001201807030000000364\"],\"copyWrites\":[\"促销\",\"张婷测试文案超过十二\"]},\"NCPAY_DEBIT\":{\"activityIds\":[\"MKN1001201806250000000196\",\"MKN1001201807030000000364\"],\"copyWrites\":[\"促销\",\"张婷测试文案超过十二\"]}}";
		try{
			Map<String, ActivityInfoOfPayProduct> activities = (Map<String, ActivityInfoOfPayProduct>) JSON.parseObject(text, new TypeReference<HashMap<String,ActivityInfoOfPayProduct>>(){});
			System.out.println(activities);
		}catch(Throwable t){
			t.printStackTrace();
		}

	}
}
