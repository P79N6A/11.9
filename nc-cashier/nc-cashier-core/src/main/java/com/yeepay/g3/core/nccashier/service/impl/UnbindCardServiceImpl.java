package com.yeepay.g3.core.nccashier.service.impl;

import java.util.Date;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.dao.UnbindCardDao;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.entity.UnbindRecord;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.service.UnbindCardService;
@Service("unbindCardService")
public class UnbindCardServiceImpl extends NcCashierBaseService implements
		UnbindCardService {
	@Resource
	private UnbindCardDao unbindCardDao;
	@Override
	public UnbindRecord create(Long bindId, PaymentRequest paymentRequest,
			String cause) {
		UnbindRecord unbindRecord = unbindCardDao.getUnbindRecordByBindId(bindId);
		if(null == unbindRecord){
			unbindRecord = new UnbindRecord();
			unbindRecord.setBindId(bindId);
			unbindRecord.setMerchantNo(paymentRequest.getMerchantNo());
			unbindRecord.setCause(cause);
			unbindRecord.setIdentityId(paymentRequest.getIdentityId());
			unbindRecord.setIdentityType(paymentRequest.getIdentityType());
			unbindRecord.setStatus("INIT");
			unbindRecord.setCreateTime(new Date());
			unbindRecord.setUpdateTime(new Date());
			unbindCardDao.create(unbindRecord);
		}
		return unbindRecord;
	}

	@Override
	public void update(UnbindRecord unbindRecord) {
		unbindCardDao.update(unbindRecord);
	}

}
