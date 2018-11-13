package com.yeepay.g3.core.nccashier.gatewayservice;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Assert;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.base.BaseTest;
import com.yeepay.g3.core.nccashier.gateway.service.MerchantInfoService;
import com.yeepay.g3.core.nccashier.gateway.service.YOPService;
import com.yeepay.g3.core.nccashier.utils.RedisTemplate;
import com.yeepay.g3.core.nccashier.vo.SimpleRecodeInfoModel;
import com.yeepay.g3.facade.merchant_platform.dto.LevelRespDTO;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.utils.common.CollectionUtils;

public class GateServiceTest extends BaseTest {
		
	@Resource
	private MerchantInfoService merchantInfoService;
	
	@Resource
	private YOPService yopService;

	@Test
	public void testGetLevel(){
		LevelRespDTO response = merchantInfoService.getMerchantLevel("10040007800");
		Assert.assertTrue(response != null);
	}
	
	@Test
	public void testRedis(){
		SimpleRecodeInfoModel recordInfo = new SimpleRecodeInfoModel(1111l,"payorderno1");
//		JSONObject json = (JSONObject) JSON.toJSON(recordInfo);  
//		List<SimpleRecodeInfoModel> models= new ArrayList<SimpleRecodeInfoModel>();
		
//		List<SimpleRecodeInfoModel> result= (List<SimpleRecodeInfoModel>)RedisTemplate.getTargetFromRedis(
//				Constant.NCCASHIER_RESULT_QUERY_RECORD_INFO_REDIS_KEY + "token1111", List.class);
		
		String resultJson = RedisTemplate.getTargetFromRedisToString(Constant.NCCASHIER_RESULT_QUERY_RECORD_INFO_REDIS_KEY + "token1111");
		List<SimpleRecodeInfoModel> models = JSONObject.parseArray(resultJson, SimpleRecodeInfoModel.class);
		if(CollectionUtils.isEmpty(models)){
			models = new ArrayList<SimpleRecodeInfoModel>();
			models.add(recordInfo);
			RedisTemplate.setCacheObjectSumValue(Constant.NCCASHIER_RESULT_QUERY_RECORD_INFO_REDIS_KEY + "token1111", models,
					Constant.NCCASHIER_RESULT_QUERY_RECORD_INFO_REDIS_KEY_TIMEOUT);
		} else if(models.contains(recordInfo)){
//			models.add(recordInfo);
//			RedisTemplate.setCacheObjectSumValue(Constant.NCCASHIER_RESULT_QUERY_RECORD_INFO_REDIS_KEY + "token1111", models,
//					Constant.NCCASHIER_RESULT_QUERY_RECORD_INFO_REDIS_KEY_TIMEOUT);
			System.out.println(JSON.toJSONString(recordInfo));
		
		}
	}
	
	@Test
	public void testMerchantSignAndVerify(){
//		String appKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCrVHL17lfZ+2nZ3eHuO0soTOHU a4X7wuVPrl1hy9DLQuLqdbwfg10gltRfBnPx3ZkrRdR4m5LY7T6yxdFY5Kej3hNwbrch/pvZwoQo 8F2eHigvrCRjxudmHvQHLdtFRPtimPs4VG3GYl11/e1lPDcnOoOLlPF4lyKmsEKPwK/DK+I+jeCP o2jB+pAxFPVWXY6mWrs91sgpdoOmSCijPDsxfhfmVvFQvLUdXr/SuKjvH75keAZ9oUVNjPC7QJHR o3JYoU9iUfiu5TJwwx7XKCjqMuXvb+iV34/L1MHfZdjo1QU+hvQSTonP1+DSVTOTE3uP6ufiC4nw wyXpGAPMY8YRAgMBAAECggEAMGhy9uO59MAhf0o+7MXaDW/zmsYqnCDME8BraBdjThr+7EoJtkmy hWO4a4TyO5NmFKDtUIp8akhWH8LezKQGbblweqL9oWBD/roEB2EqwmM47YdQ3NQ1S1hRkLm3K5I7 CPe6e4b3YUcnqw/tBF6IItBYnJafx3fEdZ51oBJMVvNW10Eu/ZCdVn+wpMIlGzH8qn3pNrkpP2EW 19OSissnjVJ1E7v5TDGZBQVSHkcbQ7xr9oAjOscZcm6TtTPWKlsNvXayyZBVSnsszRPgmVaIZL+L CJGbcQV6HtOwqIFqmUfWNg0Adf4++K5SJWnEzdMP+SaaJyJnYGjklEkf9mQNxQKBgQDvc8YHreju RfGLNmaCUdr0G5iTSi7KQCymMmSRqOd1upD1v3zGCM4a+zVlGu1aQCmba+qWxXIeuygcwn01g01G ukrq4dP85/mnqisI4OG607NCRILbuupphk/tdYWbdI7o/Qave2slaczLlRpAgc/gYqcwCitljmnf KPvX/KU7gwKBgQC3K4A+UfrrMwKvgYxtcP6lCuG8mswBuDRkPC6LZrJc22S3HWw7LXVWwUi8SZdG oCUwDPFzfrHZsl7965SLKB7zk/1+VmSWt8ZueOMwOg7UPOfbMBg52PC1rOtu5JY4Bm1aZsHAIYDK tKKYUaazFyC23ZidNLQhMXINGN8aIscf2wKBgQCEh86WV4IxxxKem5h3DrkiHNgAxbFKDeTog8G4 AQVC2uT6r2Zu8VaqBloSQKoYJqUgucUYd+Xm7m2QJXFJmge+WsO2ZxF+zCIY042IF3e4gQ2ZYvQO i9DMYSOB6WbumL+0Yr89hxDRn1JTZ44lH/QfXFrusuI8Dmu4sSVa8SG+4wKBgAiVSkIhV0+0KTkO KgVq2RPkyaUr38lo11OnGks/+bWuNi76evre63OwRPdFv4f4syVoRdwyoKTh3d+qLWDD9YdWdPd5 lucVH4BHu+WjotRBMmAsBcaYKtdojfO5VGy1qGQnEoctSrq08jWPBe+4crj+80rSkGpJxd1lP/ca kBgnAoGBAMTq2u3BJ3tpT8YSlOA1ojtFOpKVzdYrKi+1piFjyNgEcTpf1f37x8SxYzVYz9gGor5A oAp8DzyJz/9RsSLJJmAV0lliiNbgBDjFOJvJaUy2Gx8KWD9AHW6vtMNnsssCyrrRHHk1HcojT6Uk fw/rSIouLd9rSkID/5AYUAwnNUai"; 
		String plainText = "10040018749order_processor_token1487659807329000ZFBasd";
//		String sign = yopService.sign(appKey, plainText);
//		System.out.println("得到签名:" + sign);
		boolean verify = yopService.verify("10040007800", plainText, "ILw1f5hMG-tJ4sHcuHk3yFIiXb-W3_rzj2InCDj_VloFzi-zZQglyQQBNrpWhYn9rbP1oeGT9WDGUlvPC-1ryvAzR8XyN1_LjqPsU8Hzr7o0DdYeqXBRd6Hw1xO2VzpO8lYzY2kpXnCrKzYQ4sritU-Md3vPw002ChN-8x0XIDNLikT25NNgP7yAtW2bMxjlT0HavMH4UwP2XYvFR-JZdTWFY_jKy-5Wp84o3ukFVM3q6ZVoozSNcZOhpLTkscwAf5eAqTX7KV8Ml9uNHCRrxq46DCdl9L8gov3AcYyIUQMwoCjyMiN4vp6aT09PLh7l0mfMF7l-YTsdEKRKIn4TDA$SHA256");
		System.out.println("签名验证:" + verify);
	}

}
