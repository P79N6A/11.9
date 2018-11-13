package com.yeepay.g3.core.nccashier.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.dao.RealPersonChooseTimesDao;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.entity.RealPersonChooseTimes;
import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.core.nccashier.service.RealPersonChooseTimesService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.utils.common.DateUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.utils.lock.Lock;
import com.yeepay.utils.lock.impl.RedisLock;

@Service("realPersonChooseTimesService")
public class RealPersonChooseTimesServiceImpl implements
		RealPersonChooseTimesService {
	private static final Logger logger = NcCashierLoggerFactory.getLogger(RealPersonChooseTimesServiceImpl.class);
	@Resource
	private RealPersonChooseTimesDao realPersonChooseTimesDao;
	@Override
	public RealPersonChooseTimes getShowTimes(PaymentRequest payRequest) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("merchantNo", payRequest.getMerchantNo());
		map.put("identityId", payRequest.getIdentityId());
		map.put("identityType", payRequest.getIdentityType());
		
		RealPersonChooseTimes showTimes =  realPersonChooseTimesDao.getShowTimesByUserInfo(map);
		return showTimes;
	}

	@Override
	public void createShowTimes(RealPersonChooseTimes showTimes) {
		realPersonChooseTimesDao.create(showTimes);
	}

	@Override
	public void updateShowTimes(RealPersonChooseTimes showTimes) {
		Lock lock = new RedisLock("ol.nccashier.updateShowTime."+showTimes.getId(), 3);
		try{
			if(lock.lock()){
				int showCount = showTimes.getShowCount()+1;
				showTimes.setShowCount(showCount);
				showTimes.setUpdateTime(new Date());
				realPersonChooseTimesDao.updateShowTimes(showTimes);
			}
		
		}catch(Exception e){
			logger.error("更新支付身份页面展示次数异常", e);
		}finally{
			if(null !=lock){
				try{
					lock.unlock();
				}catch(Exception e){
					logger.error("更新支付身份页面展示次数释放锁时异常", e);
				}
			}
		}
		
	}

	@Override
	public List<RealPersonChooseTimes> getUnChooseShowTimesInfo() {
		Date now = new Date();
		int[] timeLength = CommonUtil.getAutoSetSamePersonSets();
		Date begin =DateUtils.addMinute(now, -timeLength[0]);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("updateTimeBegin", DateUtils.getTimeStampStr(begin));
		map.put("updateTimeEnd", DateUtils.getTimeStampStr(now));
		map.put("dealRecordsNo", timeLength[1]);
		return realPersonChooseTimesDao.getUnChooseShowTimesInfo(map);
	}

}
