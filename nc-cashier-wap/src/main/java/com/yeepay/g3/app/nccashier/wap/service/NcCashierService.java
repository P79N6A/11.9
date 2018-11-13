package com.yeepay.g3.app.nccashier.wap.service;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.app.nccashier.wap.vo.ActivityVo;
import com.yeepay.g3.app.nccashier.wap.vo.BankLimitAmountResponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.ResponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.SmsSendResponseVo;
import com.yeepay.g3.app.nccashier.wap.vo.clfeasy.CflEasyBankInfoVO;
import com.yeepay.g3.app.nccashier.wap.vo.clfeasy.CflEasyConfirmPayRequestVo;
import com.yeepay.g3.app.nccashier.wap.vo.clfeasy.CflEasyOrderReponseVO;
import com.yeepay.g3.app.nccashier.wap.vo.clfeasy.CflEasyOrderRequestVO;
import com.yeepay.g3.app.nccashier.wap.vo.clfeasy.CflEasyPreRouteRequestVO;
import com.yeepay.g3.app.nccashier.wap.vo.clfeasy.CflEasySmsSendRequestVo;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPaymentRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPaymentResponseDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPrePayRequestDTO;
import com.yeepay.g3.facade.nccashier.dto.guarantee.GuaranteeInstallmentPrePayResponseDTO;

import java.util.List;
import java.util.Map;

/**
 * NCCASHIER服务，封装对nccashier,redis的调用
 * @author zhen.tan
 *
 */
public interface NcCashierService {

	/**
	 * 创建支付订单
	 * @param requestDTO
	 * @return
	 */
	public CashierPaymentResponseDTO createPayment(CashierPaymentRequestDTO requestDTO);
	
	/**
	 * 发送短验
	 * @param requestDTO
	 */
	public void sendSMS(CashierSmsSendRequestDTO requestDTO);
	
	/**
	 * 查询支付结果
	 * @param requestDTO
	 * @return
	 */
	public TradeNoticeDTO queryPayResult(CashierQueryRequestDTO requestDTO);
	
	/**
	 * 获取必填项
	 * @param requestDTO
	 * @return
	 */
	public NeedValidatesDTO getCardValidates(CardValidateRequestDTO requestDTO);
	
	/**
	 * 获取支持银行列表
	 * @param requestId
	 * @return
	 */
	public List<BankSupportDTO> supportBankList(long requestId,String cusType);
	
	/**
	 * 获取基本用户信息
	 * @param tokenId
	 * @return
	 */
	public RequestInfoDTO requestBaseInfo(String tokenId);
	/**
	 * 保存用户信息
	 * @param userAccess
	 */
	public void saveUserAccess(UserAccessDTO userAccess);
	/**
	 * 获取支付业务类型
	 * @param parseLong
	 * @return 
	 */
	public BussinessTypeResponseDTO routerPayWay(long parseLong);
	/**
	 * 获取卡信息
	 * @param requestId
	 * @return
	 */
	public BankCardReponseDTO getBankCardInfo(long requestId,String cusType);

	/**
	 * 透传绑卡id校验
	 * @param requestId
	 * @return
     */
	public BasicResponseDTO validatePassBindId(long requestId);

	/**
	 * 是否是透传绑卡id
	 * @param requestId
	 * @return
     */
	public Boolean isPassBindId(long requestId);


	/**
	 * 清除UserAccount的RecordId
	 * @param tokenId
	 */
	public void clearRecordId(String tokenId);

	public CashierPayResponseDTO firstPay(CashierFirstPayRequestDTO firstPayRequest);

	public CashierPayResponseDTO bindPay(CashierBindPayRequestDTO bindPayRequest);

	public String getURL(String cashierVer, String merchantNo, long id);

	public String getSendSMSNo();
	
	public PayExtendInfo getPayExtendInfo(long requestId,String tokenId);

	public List<MerchantProductDTO> getNewMerchantInNetConfig(long requestId,String tokenId);

	public String WeChatPay(WeChatPayRequestDTO payRequest);

	public void setCardOwner(String bindId, String name, String idCardNo, long paymentRequestId);

	public void unbindCard(long paymentRequestId, String paymentOrderNo);

	public boolean queryQualification4Carnival(String merchantNo);

	public ActivityVo queryQualification4Activities();



	/**
	 * 根据卡号获取卡bin信息
	 * @param cardno
	 * @return
	 */
	public CardBinInfoDTO getCardBinInfo(String cardno);
	
	/**
	 * pc网银 获取商户支持的网银收银台模板
	 * @return
	 */
	EBankSupportBanksResponseDTO ebankSupportBankList(EBankSupportBanksRequestDTO request);


	/**
	 * 根据支付场景获取 网银收银台模板
	 * @param request
	 * @return
     */
	EBankSupportBanksResponseDTO getBacLoadSuportBanks(EBankSupportBanksRequestDTO request);


		/**
         * 新request下单处理
         * @param orderNo
         * @return
         */
	NewOrderRequestResponseDTO newOrderRequest(String orderNo,int orderSysNo);
	
	/**
	 * 网银确认支付下单
	 * @param request
	 * @return
	 */
	EBankCreatePaymentResponseDTO ebankCreatePayment(EBankCreatePaymentRequestDTO request);
	
	/**
	 * 支付处理器是否可查标识监听处理
	 * @param request
	 * @return
	 */
	PayResultQuerySignListenResponseDTO listenCanPayResultQuery(PayResultQuerySignListenRequestDTO request); 

	/**
	 *
	 * @param requestId 获取requestPayment的参数
	 * @param merchantNo 商户号
	 * @param cashierVersion  收银台，PC/WAP
	 * @return
	 */
	Map<String,List<BankLimitAmountResponseVO>> querySupportBankListFromMerchantConfig(String requestId, String merchantNo, String cashierVersion);

	/**
	 * 查询是否所有的银行全部超出限额
	 * @param amount
	 * @param requestId
	 * @param merchantNo
	 * @return
	 */
	Boolean queryAllBankOverLimit(double amount,String requestId,String merchantNo);

	/**
	 * 对绑卡列表进行排序，最先展示没有超出限额的
	 * @param list
	 * @param amount
	 * @param requestId
	 * @param merchantNo
	 * @return
	 */
	List<JSONObject> bindCradLimitSort(List<BankCardDTO> list,double amount,String requestId,String merchantNo);

	/**
	 * 调取预路由接口，获取appid
	 * @return
	 */
	public JsapiRouteResponseDTO getAppid(String requestId, String payType);
	
	/**
	 * 风控reffer校验
	 * @param requestDTO
	 * @return
	 */
	public void checkReffer(CheckRefferRequestDTO requestDTO);
	/**
	 * 获取支付身份信息（新）
	 * @param paymentRequestId
	 * @param paymentRecordId
	 * @return
	 */
	public CardOwnerConfirmResDTO getOwnersInfo(long paymentRequestId,
			long paymentRecordId);

	public void unbindCard(long paymentRequestId, long paymentRecordId);

	public MerchantAuthorityResponseDTO merchantAuthorityRequest(MerchantAuthorityRequestDTO merchantAuthorityRequestDTO);

	public ShareCardAuthoritySendSmsResponseDTO shareCardAuthoritySendSms(ShareCardAuthoritySendSmsRequestDTO shareCardAuthoritySendSmsRequestDTO);

	public ShareCardAuthoritySmsConfirmResponseDTO shareCardAuthoritySmsConfirm(ShareCardAuthoritySmsConfirmRequestDTO shareCardAuthoritySmsConfirmRequestDTO);
	
	/**
	 * YOP验签
	 * @param plaintext
	 * @param signature
	 * @return
	 */
	public boolean yopVerify(String appKey, String plaintext, String signature);

	/**
	 *  订单处理器下单
	 * @param orderProcessorRequestDTO
	 * @return
	 */
	public OrderProcessResponseDTO orderProcessorRequest(OrderProcessorRequestDTO orderProcessorRequestDTO);
	
	/**
	 * yop签名
	 * 
	 * @param plaintext
	 * @return
	 */
	String sign(String plaintext);
	
	/**
	 * 商家扫码支付下单
	 * 
	 * @param request
	 * @return
	 */
	void apiMerchantScanPay(APIMerchantScanPayDTO request);
	
	/**
	 * 获取商户定制收银台信息
	 * 
	 * @param merchantNo
	 * @return
	 */
	MerchantCashierCustomizedLayoutSelectDTO queryMerchantCashierCustomizedLayoutSelectInfo(String merchantNo);

	/**
	 * 获取收银台定制化模版文件内容
	 * 
	 * @param fileId
	 * @param fileType
	 * @return
	 */
	MerchantCashierCustomizedFileDTO queryMerchantCashierCustomizedFile(String fileId, String fileType);


	/**
	 * 商户信息查询
	 * @param merchantAccountCode  商户编号
	 * @return
	 */
//	MerchantResDTO queryMerchantInfo(String merchantAccountCode);
 

	/**
	 * 账户支付账户信息校验，包括账户名和交易密码
	 * 
	 * @param validateDTO
	 * @return
	 */
	AccountPayValidateResponseDTO accountPayValidate(AccountPayValidateRequestDTO validateDTO);
	
	/**
	 * 账户支付支付接口
	 * 
	 * @param payDTO
	 */
	void accountPay(CashierAccountPayRequestDTO payDTO);

	/**
	 * 拼接并返回商户回调地址
	 * @param requestInfoDTO
	 * @return
	 */
	String getMerchantPageCallBack(RequestInfoDTO requestInfoDTO);
	
	/**
	 * 非银行卡支付获取hmac
	 * 
	 * @return
	 */
	String getNotBankCardPayHmac(String merchantNo, String source);

	/**
	 * 获取微信公众号二维码地址，用于展示在成功、查询及失败页
	 * @param requestId
	 * @return
	 */
	String getYeepayWechatQRCode(String requestId);
	/**
	 * 获取微信公众号二维码地址，用于展示在成功、查询及失败页
	 * @param merchantNo
	 * @param merchantOrderId
	 * @return
	 */
	String getYeepayWechatQRCode(String merchantNo,String merchantOrderId);

	/**
	 * 绑卡支付，在已绑卡列表，主动解绑选中的卡
	 * @param paymentRequestId
	 * @param bindId
	 */
	void unbindCardActive(String paymentRequestId, String bindId);
	
	NOPCardBinResponseDTO getNopCardBinInfo(NOPCardBinRequestDTO cardBinRequestDTO);

	NOPAuthBindResponseDTO authBindCardRequest(NOPAuthBindRequestDTO authBindRequestDTO);

	NOPAuthBindConfirmResponseDTO authBindCardConfirm(NOPAuthBindConfirmRequestDTO authBindConfirmRequestDTO);
	
	NOPAuthBindSMSResponseDTO authBindCardSMS(NOPAuthBindSMSRequestDTO authBindSMSRequest);
	
	NOPQueryOrderResponseDTO queryNopOrderStatus(NOPQueryOrderRequestDTO queryOrderRequest);
	
	NOPQueryBindCardOpenStatusResponseDTO getNopBindCardOpenStatus(String merchantNo);
	
	/**
	 * 获取银行卡分期支持银行列表及期数信息
	 * 
	 * @param requestId
	 * @return
	 */
	InstallmentBanksResponseDTO getInstallmentBanks(long requestId);
	
	InstallmentRouteResponseDTO installmentRoutePayWay(long requestId);
	
	InstallmentFeeInfoResponseDTO getInstallmentFeeInfo(InstallmentFeeInfoRequestDTO requestDTO);
	
	CardNoOrderResponseDTO orderByCardNo(CardNoOrderRequestDTO requestDTO);
	
	BasicResponseDTO orderBySignRelationId(SignRelationIdOrderRequestDTO requestDTO);
	
	BasicResponseDTO bankInstallmentSmsSend(Long requestId, Long recordId);
	
	BasicResponseDTO bankInstallmentConfirmPay(Long requestId, Long recordId, String verifyCode);




	/**
	 * 预授权首次下单
	 * @param requestDTO
	 * @return
	 */
	CashierPaymentResponseDTO preauthOrderRequest(CashierPaymentRequestDTO requestDTO);
	/**
	 * 预授权首次确认
	 * @param requestDTO
	 */
	void preauthOrderConfirm(PayConfirmBaseRequestDTO requestDTO);
	/**
	 * 预授权绑卡确认
	 * @param requestDTO
	 */
	void preauthBindOrderConfirm(PreauthBindConfirmRequestDTO requestDTO);
	/**
	 * 预授权绑卡下单
	 * @param requestDTO
	 * @return
	 */
	CashierPaymentResponseDTO preauthBindOrderRequest(CashierPaymentRequestDTO requestDTO);
	/**
	 * 预授权发短验
	 * @param requestDTO
	 */
	void preAuthSendSms(CashierSmsSendRequestDTO requestDTO);
	/**
	 * 预授权：获取绑卡信息，不处理透传项
	 * @param requestId
	 * @param cusType
	 * @return
	 */
	BankCardReponseDTO getBankCardInfo4Preauth(long requestId, String cusType);

	/**
	 * 担保分期-预下单
	 * @param requestDTO
	 * @return
	 */
	GuaranteeInstallmentPrePayResponseDTO guaranteeInstallmentPrePay(GuaranteeInstallmentPrePayRequestDTO requestDTO);

	/**
	 * 担保分期-请求支付
	 * @param requestDTO
	 * @return
	 */
	GuaranteeInstallmentPaymentResponseDTO guaranteeInstallmentRequestPayment(GuaranteeInstallmentPaymentRequestDTO requestDTO);


	/**
	 * 担保分期获取支持银行列表和期数
	 * @param requestId
	 * @return
     */
	InstallmentRouteResponseDTO guaranteeInstallmentgetSupportBankAndPeriods(long requestId);

	/**
	 * 获取营销活动
	 * 
	 * @param requestDTO
	 * @return
	 */
	MarketActivityResponseDTO getMarketActivityInfo(MarketActivityRequestDTO requestDTO);
}
