package com.yeepay.g3.core.nccashier.utils;

import java.util.HashMap;
import java.util.Map;

import com.yeepay.g3.core.nccashier.log.NcCashierLoggerFactory;
import com.yeepay.g3.facade.nccashier.dto.UnifiedAPICashierRequestDTO;
import com.yeepay.g3.facade.nccashier.enumtype.PayTool;
import com.yeepay.g3.facade.nccashier.enumtype.PayTypeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;

/**
 * API收银台，请求支付类型路由
 * Created by ruiyang.du on 2017/7/14.
 */
public class ApiCashierRouterUtil {

	public static final String ALIPAY_LIFE_NO_PREPAY_CODE = "tradeNo";
	
    static Logger logger = NcCashierLoggerFactory.getLogger(ApiCashierRouterUtil.class);
    
    private static final Map<String, String> serviceNameMaps = new HashMap<String, String>();
    
    static {
        serviceNameMaps.put(PayTool.SCCANPAY.name(), "SCCANPAY_OPENID");
        serviceNameMaps.put(PayTool.MSCANPAY.name(), PayTool.MSCANPAY.name());
        serviceNameMaps.put(PayTool.WECHAT_OPENID.name(), "SCCANPAY_OPENID");
        serviceNameMaps.put(PayTool.ZFB_SHH.name(), "SCCANPAY_OPENID");
        serviceNameMaps.put(PayTool.EWALLET.name(), "SCCANPAY_OPENID");
        serviceNameMaps.put(PayTool.XCX_OFFLINE_ZF.name(), "SCCANPAY_OPENID");
    }
   
    
	public static String getServiceName(String payTool) {
		String serviceName = null;
		if (StringUtils.isNotBlank(payTool)) {
			serviceName = serviceNameMaps.get(payTool);
		}
		if (StringUtils.isNotBlank(serviceName)) {
			return serviceName;
		}
		throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
				Errors.INPUT_PARAM_NULL.getMsg() + "，payTool非法");
	}

	/**
	 * 校验payTool入参
	 * 
	 * @param payTool
	 * @return
	 */
	private static void checkPayTool(String payTool) {
		if (!PayTool.SCCANPAY.name().equals(payTool) && !PayTool.MSCANPAY.name().equals(payTool)
				&& !PayTool.WECHAT_OPENID.name().equals(payTool) && !PayTool.ZFB_SHH.name().equals(payTool)
                && !PayTool.EWALLET.name().equals(payTool) && !PayTool.XCX_OFFLINE_ZF.name().equals(payTool)) {
			throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(),
					Errors.INPUT_PARAM_NULL.getMsg() + "，payTool非法");
		}
	}
	
	
    /**
     * 根据对外的payTool和payTypeOut，映射到收银台内部的payTool和payType枚举值，并覆盖到入参
     * <p>同时处理yop透传来的商编格式（去除‘opr:’）
     *
     * @param apiCashierRequestDTO
     * @return
     */
    public static void payTypeRequestRoute(UnifiedAPICashierRequestDTO apiCashierRequestDTO) {
        if (apiCashierRequestDTO == null || StringUtils.isEmpty(apiCashierRequestDTO.getPayTool()) || StringUtils.isEmpty(apiCashierRequestDTO.getPayType())) {
             // 这句根本走不到
        		logger.error("【API收银台支付接口】payTypeRequestRoute() 失败，入参payTool或payType为空");
            throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
        }
        PayToolOut payToolOut = PayToolOut.getByName(apiCashierRequestDTO.getPayTool());
        if(payToolOut==null){
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + "，payTool非法");
        }
        PayTypeOut payTypeOuter = PayTypeOut.getByName(apiCashierRequestDTO.getPayType());
        if(payTypeOuter==null){
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + "，payType非法");
        }
        String payToolInner = payToolOut.name();
        String payTypeInner = null;
        switch (payToolOut) {
            case SCCANPAY: {
                switch (payTypeOuter) {
                    case WECHAT:
                        payTypeInner = PayTypeEnum.WECHAT_ATIVE_SCAN.name();
                        break;
                    case ALIPAY:
                        payTypeInner = PayTypeEnum.ALIPAY.name();
                        break;
                    case JD:
                        payTypeInner = PayTypeEnum.JD_ATIVE_SCAN.name();
                        break;
                    case UPOP:
                        payTypeInner = PayTypeEnum.UPOP_ATIVE_SCAN.name();
                        break;
                    case QQ:
                        payTypeInner = PayTypeEnum.QQ_ATIVE_SCAN.name();
                        break;
                    default:
                        break;
                }
                break;
            }
            case MSCANPAY: {
                switch (payTypeOuter) {
                    case WECHAT:
                        payTypeInner = PayTypeEnum.WECHAT_SCAN.name();
                        break;
                    case ALIPAY:
                        payTypeInner = PayTypeEnum.ALIPAY_SCAN.name();
                        break;
                    case JD:
                        payTypeInner = PayTypeEnum.JD_PASSIVE_SCAN.name();
                        break;
                    case UPOP:
                        payTypeInner = PayTypeEnum.UPOP_PASSIVE_SCAN.name();
                        break;
                    case QQ:
                        payTypeInner = PayTypeEnum.QQ_PASSIVE_SCAN.name();
                        break;
                    default:
                        break;
                }
                break;
            }
            case WECHAT_OPENID: {
                switch (payTypeOuter) {
                    case WECHAT:
                        payTypeInner = PayTypeEnum.WECHAT_OPENID.name();
                        break;
                    default:
                        break;
                }
                break;
            }
			case ZFB_SHH: {
				switch (payTypeOuter) {
				case ALIPAY:
					payTypeInner = PayTypeEnum.ZFB_SHH.name();
					break;
				default:
					break;
				}
				break;
			}
            case EWALLET: {
                switch (payTypeOuter) {
                    case ALIPAY:
                        payTypeInner = PayTypeEnum.ALIPAY_SDK.name();
                        break;
                    case WECHAT:
                        payTypeInner = PayTypeEnum.WECHAT_SDK.name();
                        break;
                    default:
                        break;
                }
                break;
            }
            case MINI_PROGRAM: {
                payToolInner = PayTool.XCX_OFFLINE_ZF.name();
                switch (payTypeOuter) {
                    case WECHAT:
                        payTypeInner = PayTypeEnum.XCX_OFFLINE_ZF.name();
                        break;
                    default:
                        break;
                }
                break;
            }
        }
        if (StringUtils.isEmpty(payTypeInner)) {
            throw new CashierBusinessException(Errors.INPUT_PARAM_NULL.getCode(), Errors.INPUT_PARAM_NULL.getMsg() + "，payType非法");
        }
        if (StringUtils.isNotEmpty(apiCashierRequestDTO.getMerchantNo()) && apiCashierRequestDTO.getMerchantNo().startsWith("OPR:")) {
            String[] merchantNoArray = apiCashierRequestDTO.getMerchantNo().split("OPR:");
            if (merchantNoArray != null && merchantNoArray.length >= 2) {
                apiCashierRequestDTO.setMerchantNo(merchantNoArray[1]);
            }
        }
        apiCashierRequestDTO.setPayTool(payToolInner);
        apiCashierRequestDTO.setPayType(payTypeInner);
    }

    /**
     * API接口返回时，将收银台内部payType映射到API对外payType，以保持返回与入参一致
     *
     * @param payTypeInner
     * @return
     */
    public static String payTypeResponseRoute(String payTypeInner) {
        if (PayTypeEnum.ALIPAY.name().equals(payTypeInner) || PayTypeEnum.ALIPAY_SCAN.name().equals(payTypeInner) || PayTypeEnum.ZFB_SHH.name().equals(payTypeInner)) {
            return PayTypeOut.ALIPAY.name();
        }
        if (PayTypeEnum.WECHAT_ATIVE_SCAN.name().equals(payTypeInner) || PayTypeEnum.WECHAT_SCAN.name().equals(payTypeInner) || PayTypeEnum.WECHAT_OPENID.name().equals(payTypeInner)
                || PayTypeEnum.XCX_OFFLINE_ZF.name().equals(payTypeInner)) {
            return PayTypeOut.WECHAT.name();
        }
        if (PayTypeEnum.JD_ATIVE_SCAN.name().equals(payTypeInner) || PayTypeEnum.JD_PASSIVE_SCAN.name().equals(payTypeInner)) {
            return PayTypeOut.JD.name();
        }
        if(PayTypeEnum.UPOP_ATIVE_SCAN.name().equals(payTypeInner) || PayTypeEnum.UPOP_PASSIVE_SCAN.name().equals(payTypeInner)){
            return PayTypeOut.UPOP.name();
        }
        if(PayTypeEnum.QQ_ATIVE_SCAN.name().equals(payTypeInner) || PayTypeEnum.QQ_PASSIVE_SCAN.name().equals(payTypeInner)){
            return PayTypeOut.QQ.name();
        }
        return payTypeInner;
    }

    /**
     * API接口返回时，将收银台内部payTool映射到API对外payTool，以保持返回与入参一致
     *
     * @param payToolInner
     * @return
     */
    public static String payToolResponseRoute(String payToolInner) {
        if (PayTool.XCX_OFFLINE_ZF.name().equals(payToolInner)) {
            return PayToolOut.MINI_PROGRAM.name();
        }
        return payToolInner;
    }

    /**
     * 【聚合支付】API收银台对外简化版payType
     */
    private enum PayTypeOut {
        WECHAT("1", "微信"),
        ALIPAY("2", "支付宝"),
        JD("3", "京东"),
        UPOP("4", "银联"),
        QQ("5","QQ");
        private String value;
        private String description;
        private PayTypeOut(String value, String description) {
            this.value = value;
            this.description = description;
        }
        /**
         * 根据名称获取支付类型
         * @param name
         * @return
         */
		private static PayTypeOut getByName(String name) {
			try {
				return PayTypeOut.valueOf(name);
			} catch (Throwable t) {
				logger.warn("转化paytype异常, payType=" + name, t);
			}
			return null;
		}
    }

    /**
     * 【聚合支付】API收银台对外简化版payTool
     */
    private enum PayToolOut {
        SCCANPAY("2", "扫码支付"),
        EWALLET("4", "钱包支付"),
        MSCANPAY("6","商家扫码"),
        WECHAT_OPENID("7","公众号支付"),
        ZFB_SHH("11", "支付宝生活号"),
        MINI_PROGRAM("17","微信小程序支付");

        private String value;
        private String description;
        private PayToolOut(String value, String description) {
            this.value = value;
            this.description = description;
        }
        /**
         * 根据名称获取对外支付根据
         * @param name
         * @return
         */
        private static PayToolOut getByName(String name) {
            try {
                return PayToolOut.valueOf(name);
            } catch (Throwable t) {
                logger.warn("转化payTool异常, payTool=" + name, t);
            }
            return null;
        }
    }
}
