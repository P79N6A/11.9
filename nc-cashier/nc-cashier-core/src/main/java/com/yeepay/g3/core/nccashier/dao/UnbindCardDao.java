package com.yeepay.g3.core.nccashier.dao;

import com.yeepay.g3.core.nccashier.entity.UnbindRecord;

import java.util.List;
import java.util.Map;

public interface UnbindCardDao {

	UnbindRecord getUnbindRecordByBindId(Long bindId);

	void create(UnbindRecord unbindRecord);

	void update(UnbindRecord unbindRecord);

    int countUnbindRecord(Map pageParam);

    /**
	 * 批量查询identity_id_encrypt字段为空的记录
	 * @return ID和IDENTITY_ID 集合
	 */
	List<UnbindRecord> listUnbindRecordNotEnctypt(Map pageParam);

	/**
	 * 更新记录，将IDENTITY_ID的加密值写入IDENTITY_ID_ENCRYPT
	 * @param paymentRequest
	 * @return
	 */
	int updateForEncrypt(UnbindRecord paymentRequest);
	int updateForBatchEncrypt(List<UnbindRecord> paymentRequests);
}
