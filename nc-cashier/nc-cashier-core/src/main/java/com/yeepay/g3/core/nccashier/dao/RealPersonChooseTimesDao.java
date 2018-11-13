package com.yeepay.g3.core.nccashier.dao;

import java.util.List;
import java.util.Map;

import com.yeepay.g3.core.nccashier.entity.RealPersonChooseTimes;

public interface RealPersonChooseTimesDao {
	
	public RealPersonChooseTimes getShowTimesByUserInfo(Map map);
	public void updateShowTimes(RealPersonChooseTimes showTimes);
	public void create(RealPersonChooseTimes showTimes);
	public List<RealPersonChooseTimes> getUnChooseShowTimesInfo(Map map);

    int countRealPersonChooseTimes(Map pageParam);

    /**
	 * 批量查询identity_id_encrypt字段为空的记录
	 * @return ID和IDENTITY_ID 集合
	 */
	List<RealPersonChooseTimes> listRealPersonChooseTimesNotEnctypt(Map pageParam);

	/**
	 * 更新记录，将IDENTITY_ID的加密值写入IDENTITY_ID_ENCRYPT
	 * @param paymentRequest
	 * @return
	 */
	int updateForEncrypt(RealPersonChooseTimes paymentRequest);

	int updateForBatchEncrypt(List<RealPersonChooseTimes> paymentRequests);
}
