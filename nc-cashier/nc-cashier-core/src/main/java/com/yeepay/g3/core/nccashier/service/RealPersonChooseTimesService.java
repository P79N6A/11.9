package com.yeepay.g3.core.nccashier.service;

import java.util.List;

import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.entity.RealPersonChooseTimes;
/**
 * 持卡人信息选择页展示次数服务
 * @since 2016-11-22
 * @author xueping.ni
 *
 */
public interface RealPersonChooseTimesService {

	public RealPersonChooseTimes getShowTimes(PaymentRequest payRequest);

	public void createShowTimes(RealPersonChooseTimes showTimes);
	
	public void updateShowTimes(RealPersonChooseTimes showTimes);
	
	public List<RealPersonChooseTimes> getUnChooseShowTimesInfo();


}
