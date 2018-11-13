package com.yeepay.g3.app.nccashier.wap.service.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.yeepay.g3.app.nccashier.wap.service.NcCashierService;
import com.yeepay.g3.app.nccashier.wap.service.NotBankCardPayService;
import com.yeepay.g3.app.nccashier.wap.utils.CommonUtil;
import com.yeepay.g3.facade.nccashier.dto.RequestInfoDTO;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

@Service("notBankCardPayService")
public class NotBankCardPayServiceImpl implements NotBankCardPayService {
	
	private static Logger LOGGER = LoggerFactory.getLogger(NotBankCardPayServiceImpl.class);
	
	@Autowired
	private NcCashierService ncCashierService;

	@Override
	public String getNotBankCardPayUrl(RequestInfoDTO requestInfo) {
		Map<String, String> notBankCardPayReqData = new HashMap<String, String>();
		String notBankCardPayUrl = "";
		try {

			// 签名顺序：p0_Cmd,p1_MerId,p2_Order,p3_Amt,p4_Cur,p8_Url,pd_FrpId,clientIp
			// 接口类型，固定Buy
			notBankCardPayReqData.put("p0_Cmd", "Buy");
			// 商户号
			notBankCardPayReqData.put("p1_MerId", requestInfo.getMerchantNo());
			// 订单号
			notBankCardPayReqData.put("p2_Order", requestInfo.getOrderid());
			// 订单金额
			notBankCardPayReqData.put("p3_Amt", requestInfo.getAmount().toString());
			// 交易币种
			notBankCardPayReqData.put("p4_Cur", "CNY");
			// 商品名称 goodsName---productname
			notBankCardPayReqData.put("p5_Pid", StringUtils.isNotBlank(requestInfo.getProductname())?requestInfo.getProductname():"");
			// 商品类别 goodsKind---
			notBankCardPayReqData.put("p6_Pcat", StringUtils.isNotBlank(requestInfo.getGoodsKind())?requestInfo.getGoodsKind():"");
			// 商品描述 goodsDesc
			notBankCardPayReqData.put("p7_Pdesc", StringUtils.isNotBlank(requestInfo.getGoodsDesc())?requestInfo.getGoodsDesc():"");
			// 回调地址 callBackUrl
			notBankCardPayReqData.put("p8_Url", StringUtils.isNotBlank(requestInfo.getCallBackUrl())?requestInfo.getCallBackUrl():"");
			// 是否需要填送货地址，默认上送0
			notBankCardPayReqData.put("p9_SAF", "0");
			// 支付卡种，默认送空
			notBankCardPayReqData.put("pd_FrpId", "");
			// 是否需要应答，默认上送1
			notBankCardPayReqData.put("pr_NeedResponse", "1");
			// 扩展信息 goodsExt
			notBankCardPayReqData.put("pa_MP", StringUtils.isNotBlank(requestInfo.getGoodsExt())?requestInfo.getGoodsExt():"");
			// 用户请求ip地址
			notBankCardPayReqData.put("clientIp", StringUtils.isNotBlank(requestInfo.getUrlParamInfo().getUserIp())?requestInfo.getUrlParamInfo().getUserIp():"");
			String source = getSource(notBankCardPayReqData);

			String notBankCardPayUrlPre = CommonUtil.getNotBankCardPayUrl();
			String hmac = ncCashierService.getNotBankCardPayHmac(requestInfo.getMerchantNo(), source);
			StringBuilder sb = new StringBuilder();
			// 签名顺序：p0_Cmd,p1_MerId,p2_Order,p3_Amt,p4_Cur,p8_Url,pd_FrpId,clientIp
			notBankCardPayUrl = sb.append(notBankCardPayUrlPre).append("?p0_Cmd=" + notBankCardPayReqData.get("p0_Cmd"))
					.append("&p1_MerId=" + notBankCardPayReqData.get("p1_MerId"))
					.append("&p2_Order=" + notBankCardPayReqData.get("p2_Order"))
					.append("&p3_Amt=" + notBankCardPayReqData.get("p3_Amt"))
					.append("&p4_Cur=" + notBankCardPayReqData.get("p4_Cur"))
					.append("&p5_Pid=" + notBankCardPayReqData.get("p5_Pid"))
					.append("&p6_Pcat=" + notBankCardPayReqData.get("p6_Pcat"))
					.append("&p7_Pdesc=" + notBankCardPayReqData.get("p7_Pdesc"))
					.append("&p8_Url=" + notBankCardPayReqData.get("p8_Url"))
					.append("&p9_SAF=" + notBankCardPayReqData.get("p9_SAF"))
					.append("&pd_FrpId=" + notBankCardPayReqData.get("pd_FrpId"))
					.append("&pr_NeedResponse=" + notBankCardPayReqData.get("pr_NeedResponse"))
					.append("&pa_MP=" + notBankCardPayReqData.get("pa_MP"))
					.append("&clientIp=" + notBankCardPayReqData.get("clientIp"))
					.append("&hmac=" + hmac).toString();
		} catch (Exception e) {
			LOGGER.warn("非银行卡支付获取支付链接异常",e);
		}
		return notBankCardPayUrl;
	}

	private String getSource(Map<String, String> map) {
		String include = "p0_Cmd,p1_MerId,p2_Order,p3_Amt,p4_Cur,p8_Url,pd_FrpId,clientIp";
		String[] includes = include.split(",");
		StringBuffer source = new StringBuffer();
		for (String str : includes) {
			Object value = map.get(str);
			if (value != null)
				source.append(value.toString());
		}
		return source.toString();
	}
}
