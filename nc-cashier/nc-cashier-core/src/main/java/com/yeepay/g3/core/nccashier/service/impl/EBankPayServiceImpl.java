package com.yeepay.g3.core.nccashier.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.yeepay.g3.core.nccashier.constant.PayProductCode;
import com.yeepay.g3.core.nccashier.constant.PaymentSysCode;
import com.yeepay.g3.core.nccashier.entity.PaymentRecord;
import com.yeepay.g3.core.nccashier.entity.PaymentRequest;
import com.yeepay.g3.core.nccashier.gateway.service.MerchantInfoService;
import com.yeepay.g3.core.nccashier.gateway.service.NcConfigService;
import com.yeepay.g3.core.nccashier.gateway.service.PayProcessorService;
import com.yeepay.g3.core.nccashier.service.*;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.core.nccashier.utils.RedisTemplate;
import com.yeepay.g3.facade.frontend.enumtype.PayBusinessType;
import com.yeepay.g3.facade.merchant_platform.dto.LevelRespDTO;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.*;
import com.yeepay.g3.facade.nccashier.enumtype.*;
import com.yeepay.g3.facade.nccashier.error.Errors;
import com.yeepay.g3.facade.nccashier.exception.CashierBusinessException;
import com.yeepay.g3.facade.ncconfig.enumtype.OnlineChannelType;
import com.yeepay.g3.facade.ncconfig.param.QueryOnlineBankRulesByTemplateParam;
import com.yeepay.g3.facade.ncconfig.result.OnlineBankRuleDTO;
import com.yeepay.g3.facade.ncconfig.result.OnlineBankTemplateInfo;
import com.yeepay.g3.facade.payprocessor.dto.NetPayRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.NetPayResponseDTO;
import com.yeepay.g3.facade.payprocessor.enumtype.CashierVersion;
import com.yeepay.g3.facade.payprocessor.enumtype.PayOrderType;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.yeepay.g3.core.nccashier.utils.ConfigCenterUtils.isLoadSystem;

/**
 * 网银支付相关业务 service层实现
 * 
 * @author duangduang
 * @since 2016-11-08
 */
@Service("eBankPayService")
public class EBankPayServiceImpl extends NcCashierBaseService implements EBankPayService {

	private static final Logger logger = LoggerFactory.getLogger(EBankPayServiceImpl.class);

	@Resource
	private NcConfigService ncConfigService;

	@Resource
	private PayProcessorService payProcessorService;

	@Resource
	private PaymentRequestService paymentRequestService;

	@Resource
	private PaymentProcessService paymentProcessService;

	@Resource
	private UserProceeService userProceeService;

	@Resource
	private MerchantInfoService merchantInfoService;


	@Resource
	private MerchantVerificationService merchantVerificationService;


	@Override
	public void querySupportBankList(EBankSupportBanksRequestDTO request, EBankSupportBanksResponseDTO response) {
		// 1 查询paymentRequest
		PaymentRequest paymentRequest = paymentRequestService
				.findPaymentRequestByRequestId(request.getPaymentRequestId());
		// 2 获取支持的网银银行列表
		OnlineBankTemplateInfo ebankTempate = getEbankSupportBanks(paymentRequest.getEbankPayScene(), paymentRequest.getMerchantNo(),paymentRequest.getCardType());
		// 3 返回值设置
		buildEBankSupportBanksResponseDTO(ebankTempate, response);
	}

	@Override
	public void getBacLoadSuportBanks(EBankSupportBanksRequestDTO request, EBankSupportBanksResponseDTO response) {
		// 1 查询paymentRequest
		PaymentRequest paymentRequest = paymentRequestService.findPaymentRequestByRequestId(request.getPaymentRequestId());
		List<MerchantProductDTO> configList = merchantVerificationService.getNewMerchantInNetConfig(request.getToken().toString(),paymentRequest);

		String b2cPayScene = "",b2bPayScene = "";
		for(MerchantProductDTO dto : configList){
			if(dto!=null && dto.getBase().contains(request.getBacNetType())){
				if(dto.getBase().contains("B2C"))
					b2cPayScene = dto.getSce();
				if(dto.getBase().contains("B2B"))
					b2bPayScene = dto.getSce();
			}
		}
		if(StringUtils.isNotBlank(b2cPayScene)){
			try{
				OnlineBankTemplateInfo ebankTempate = getEbankSupportBanks(b2cPayScene, request.getMerchantNo(),"");
				buildBanks(response, ebankTempate.getBankRules().getB2cRules(), false);
				response.setB2cPayScene(b2cPayScene);
			}
			catch (Throwable e){
				logger.info("[getBacLoadSuportBanks]token:{},获取B2C模板失败,支付场景:{},商遍:{}",request.getToken(),b2cPayScene,request.getMerchantNo());
			}
		}
		if(StringUtils.isNotBlank(b2bPayScene)){
			try{
				OnlineBankTemplateInfo ebankTempate = getEbankSupportBanks(b2bPayScene, request.getMerchantNo(),"");
				buildBanks(response, ebankTempate.getBankRules().getB2bRules(), true);
				response.setB2bPayScene(b2bPayScene);
			}
			catch (Throwable e){
				logger.info("[getBacLoadSuportBanks]token:{},获取B2B模板失败,支付场景:{},商遍:{}",request.getToken(),b2cPayScene,request.getMerchantNo());
			}
		}
		if(response.getB2bBanks()==null && response.getB2cBanks()==null){
			throw new CashierBusinessException(Errors.CASHIER_CONFIG_NULL.getCode(), Errors.CASHIER_CONFIG_NULL.getMsg());
		}
	}






	/**
	 * 获取支持的网银银行列表
	 * 
	 *
	 * @param paymentSceneNo
	 * @param merchantNo
	 * @return
	 */
	private OnlineBankTemplateInfo getEbankSupportBanks(String paymentSceneNo, String merchantNo,String cardType) {
		if (StringUtils.isBlank(paymentSceneNo)) {
			throw CommonUtil.handleException(Errors.CASHIER_CONFIG_NULL);
		}
		// 缓存
		OnlineBankTemplateInfo ebankTempate = RedisTemplate.getTargetFromRedis(
				Constant.NCCASHIER_EBANK_SUPPORT_BANKS_REDIS_KEY + (paymentSceneNo + "_" + merchantNo + "_" + cardType), OnlineBankTemplateInfo.class);
		if (ebankTempate != null) {
			logger.info("从缓存中获取网银收银台模板, ebankTempateId:{}" , ebankTempate.getTemplateId());
			return ebankTempate;
		}
		QueryOnlineBankRulesByTemplateParam param = buildQueryOnlineBankRulesByTemplateParam(paymentSceneNo, merchantNo,cardType);
		ebankTempate = ncConfigService.getEbankSupportBanks(param);
		// 放入缓存 - 设置超时时间
		try {
			RedisTemplate.setCacheObjectSumValue(Constant.NCCASHIER_EBANK_SUPPORT_BANKS_REDIS_KEY + (param.getTemplateCode() + "_" + param.getMerchantNo()+"_" + param.getCardType()),
					ebankTempate, Constant.NCCASHIER_EBANK_SUPPORT_BANKS_REDIS_TIME_LIMIT);
		} catch (Exception e) {
			logger.error("更新支持网银收银台模板缓存失败, ebankTempate:" + ebankTempate, e);
		}
		return ebankTempate;
	}
	
	private QueryOnlineBankRulesByTemplateParam buildQueryOnlineBankRulesByTemplateParam(String paymentSceneNo, String merchantNo,String cardType){
		QueryOnlineBankRulesByTemplateParam param = new QueryOnlineBankRulesByTemplateParam();
		param.setTemplateCode(paymentSceneNo);
		param.setMerchantNo(merchantNo);
		param.setCardType(cardType);
		return param;
		
	}

	/**
	 * 判断某个银行是否被支持 判断依据： 银行编码一致
	 * 
	 * @param request
	 * @param paymentSceneNo
	 * @return
	 */
	private OnlineBankRuleDTO isBankSupported(EBankCreatePaymentRequestDTO request, String paymentSceneNo, String merchantNo,String cardType) {
		// 根据收银台模板编码获取支持的收银台模板
		OnlineBankTemplateInfo ebankTempate = getEbankSupportBanks(paymentSceneNo, merchantNo,cardType);
		if (ebankTempate == null || ebankTempate.getBankRules() == null) {
			return null;
		}
		// 该收银台模板每一种业务类型下的银行规则列表，银行是唯一的
		if (request.geteBankAccountType().contains(BankAccountTypeEnum.B2B.name())
				&& CollectionUtils.isNotEmpty(ebankTempate.getBankRules().getB2bRules())) {
			for (OnlineBankRuleDTO item : ebankTempate.getBankRules().getB2bRules()) {
				if (item.getBank() != null && item.getBank().name().equals(request.getBankId())) {
					return item;
				}
			}
		} else if (request.geteBankAccountType().contains(BankAccountTypeEnum.B2C.name())
				&& CollectionUtils.isNotEmpty(ebankTempate.getBankRules().getB2cRules())) {
			for (OnlineBankRuleDTO item : ebankTempate.getBankRules().getB2cRules()) {
				if (item.getBank() != null && item.getBank().name().equals(request.getBankId())) {
					return item;
				}
			}
		}
		return null;
	}

	/**
	 * 构造 获取支持的网银银行列表 返回值对象
	 * 
	 * @param ebankTempate
	 * @param response
	 */
	private void buildEBankSupportBanksResponseDTO(OnlineBankTemplateInfo ebankTempate,
			EBankSupportBanksResponseDTO response) {
		if (ebankTempate.getBankRules() != null) {
			buildBanks(response, ebankTempate.getBankRules().getB2bRules(), true);
			buildBanks(response, ebankTempate.getBankRules().getB2cRules(), false);
		}
		response.setProcessStatusEnum(ProcessStatusEnum.SUCCESS);
	}

	/**
	 * 构建支持的银行List列表
	 * 
	 *
	 * @param response
	 * @param banksSource
	 * @param isB2B
	 */
	private void buildBanks(EBankSupportBanksResponseDTO response, List<OnlineBankRuleDTO> banksSource, boolean isB2B) {
		if (CollectionUtils.isEmpty(banksSource)) {
			return;
		}
		if (isB2B) {
			response.setB2bBanks(buildBankList(banksSource));
		} else {
			response.setB2cBanks(buildBankList(banksSource));
		}
	}


	private List<BaseBankInfo> buildBankList(List<OnlineBankRuleDTO> banksSource){
		List<BaseBankInfo> banksDes = new ArrayList<BaseBankInfo>();
		for (OnlineBankRuleDTO item : banksSource) {
			if(item.getBank()==null){
				continue;
			}
			BaseBankInfo bank = new BaseBankInfo();
			// 银行账户类型
			BankAccountTypeEnum bankAccountType = BankAccountTypeEnum.getBankAccountType(item.getBusinessType());
			bank.setBankAccountType(bankAccountType);
			// 银行编码&银行名称
			bank.setBankCode(item.getBank().name());
			bank.setBankName(item.getBank().getName());
			// 通道类型（仅借=借记卡、仅贷=贷记卡、借贷=借记卡|信用卡）
			bank.setCardType(item.getChannelType());
			banksDes.add(bank);
		}
		return banksDes;
	}



	/**
	 * 判断是否新增PaymentRecord
	 * 
	 * @param request
	 * @return
	 */
	private boolean needNewRecord(EBankCreatePaymentRequestDTO request) {
		// 新下单
		if (request.getPaymentRecordId() == null || request.getPaymentRecordId().longValue() <= 0) {
			return true;
		}
		PaymentRecord oldRecord = paymentProcessService
				.findRecordByPaymentRecordId(String.valueOf(request.getPaymentRecordId()));
		// 数据库无对应记录
		if (oldRecord == null) {
			return true;
		}
		// 该token下过单，但是之前该笔之前不是网银的单子
		if (!PayTypeEnum.NET_BANK.name().equals(oldRecord.getPayType())) {
			return true;
		}
		// 该token下过单，虽然是网银的单子，但是不是同一张银行卡
		if (!request.getBankId().equals(oldRecord.getBankCode())) {
			return true;
		}
		// 该token下过单，虽然是网银的单子且是同一张银行卡，但是支付处理器返回的子单号为空 - 未到支付处理器下过单
		if (StringUtils.isBlank(oldRecord.getPaymentOrderNo())) {
			return true;
		}
		return false;
	}

	@Override
	public void createPayment(EBankCreatePaymentRequestDTO request, EBankCreatePaymentResponseDTO response) {
		// 1 获取支付请求订单
		PaymentRequest paymentRequest = paymentRequestService
				.findPaymentRequestByRequestId(request.getPaymentRequestId());
		response.setMerchantOrderId(paymentRequest.getMerchantOrderId());


		// 2 使用的银行是否在收银台模板范围内
		OnlineBankRuleDTO bankRuleDTO = null;
		if(isLoadSystem(paymentRequest.getTradeSysNo())){
			bankRuleDTO = isBankSupported(request, request.getPayScene(), paymentRequest.getMerchantNo(),"");
		}
		else{
			bankRuleDTO = isBankSupported(request, paymentRequest.getEbankPayScene(), paymentRequest.getMerchantNo(),paymentRequest.getCardType());
		}

		if (bankRuleDTO == null) {
			throw CommonUtil.handleException(Errors.SUPPORT_BANK_FAILED);
		}
		// 3 是否需要重新创建订单
		boolean isNeed = needNewRecord(request);
		if (isNeed) {
			PaymentRecord newRecord = buildPaymentRecord(request, paymentRequest);
			paymentProcessService.savePaymentRecord(newRecord);
			userProceeService.getAndUpdatePaymentRecordId(request.getToken(), String.valueOf(newRecord.getId()));
			request.setPaymentRecordId(newRecord.getId());
		}
		// 4 调用支付处理器进行下单
		callPaymentProccessor(request, paymentRequest, response, bankRuleDTO);
	}



	/**
	 * 调用支付处理器进行网银支付下单
	 * 
	 * @param request
	 * @param paymentRequest
	 * @param response
	 */
	private void callPaymentProccessor(EBankCreatePaymentRequestDTO request, PaymentRequest paymentRequest,
			EBankCreatePaymentResponseDTO response, OnlineBankRuleDTO bankRuleDTO) {
		NetPayRequestDTO payProcessorParam = buildNetPayRequestDTO(paymentRequest, request, bankRuleDTO);
		NetPayResponseDTO netPayResponseDTO = payProcessorService.onlinePayRequest(payProcessorParam);
		// 把支付处理器的支付子订单号更新到无卡收银台的支付记录表中 
		paymentProcessService.updateRecordNo(request.getPaymentRecordId(), null, netPayResponseDTO.getRecordNo(), PayRecordStatusEnum.ORDERED);
		buildEBankCreatePaymentResponseDTO(netPayResponseDTO, response);
	}

	/**
	 * 构造支付记录
	 * 
	 *
	 * @param request
	 * @param paymentRequest
	 * @return
	 */
	private PaymentRecord buildPaymentRecord(EBankCreatePaymentRequestDTO request, PaymentRequest paymentRequest) {
		PaymentRecord paymentRecord = new PaymentRecord();
		// 地区信息
		paymentRecord.setAreaInfo(paymentRequest.getAreaInfo());
		// 网银的支付场景编码
		paymentRecord.setBizModeCode(paymentRequest.getEbankPayScene());
		// 行业类别码
		paymentRecord.setMcc(paymentRequest.getIndustryCatalog());
		// 商户名称
		paymentRecord.setMerchantName(paymentRequest.getMerchantName());
		// 商户编号
		paymentRecord.setMerchantNo(paymentRequest.getMerchantNo());
		// 商户订单号
		paymentRecord.setMerchantOrderId(paymentRequest.getMerchantOrderId());
		// 订单金额
		paymentRecord.setPaymentAmount(paymentRequest.getOrderAmount());
		// 银行编码
		paymentRecord.setBankCode(request.getBankId());
		// 订单请求号
		paymentRecord.setPaymentRequestId(paymentRequest.getId());
		// 支付方编码
		paymentRecord.setPaymentSysNo(PaymentSysCode.PAY_PROCCESOR);
		// 支付产品编码
		paymentRecord.setPayProductCode(PayProductCode.NCCASHIER);
		// 支付类型 
		paymentRecord.setPayType(PayTypeEnum.NET_BANK.name());
		// 商品名称
		paymentRecord.setProductName(paymentRequest.getProductName());
		// 状态
		paymentRecord.setState(PayRecordStatusEnum.INIT);
		// 
		paymentRecord.setTokenId(request.getToken());
		// 交易系统编码
		paymentRecord.setTradeSysNo(paymentRequest.getTradeSysNo());
		// 交易系统订单号
		paymentRecord.setTradeSysOrderId(paymentRequest.getTradeSysOrderId());
		// 订单方订单号
		paymentRecord.setOrderOrderId(paymentRequest.getOrderOrderId());
		// 订单方系统编码
		paymentRecord.setOrderSysNo(paymentRequest.getOrderSysNo());
		// 会员编码
		paymentRecord.setMemberNo("");
		// 会员类型
		paymentRecord.setMemberType(StringUtils.isNotBlank(paymentRequest.getMemberType())
				? paymentRequest.getMemberType() : Constant.JOINLY);
		// 时间和版本
		paymentRecord.setCreateTime(new Date());
		paymentRecord.setUpdateTime(new Date());
		paymentRecord.setVersion(1);
		// 支付工具
		paymentRecord.setPayTool(PayTool.EANK.name());
		return paymentRecord;
	}

	/**
	 * 支付处理器网银下单入参实体构造
	 * 
	 * @param request
	 * @return
	 */
	private NetPayRequestDTO buildNetPayRequestDTO(PaymentRequest paymentRequest, EBankCreatePaymentRequestDTO request,
			OnlineBankRuleDTO bankRuleDTO) {
		NetPayRequestDTO param = new NetPayRequestDTO();
		buildBasicRequestDTO(paymentRequest, param);
		param.setPayScene(paymentRequest.getEbankPayScene());
		JSONObject jsonObject = CommonUtil.parseJson(paymentRequest.getExtendInfo());
		param.setRetailProductCode(jsonObject.getString("saleProductCode"));//零售产品码
		//基础产品码需要区分B2B 和B2C
		param.setBasicProductCode(CommonUtil.getBasicProductCode(request.geteBankAccountType().trim(),paymentRequest.getTradeSysNo()));
		//判断网银是否直连，处理回调地址
		setUpPageCallBack(request, param);
		param.setCashierVersion(CashierVersion.WEB);
		param.setPayOrderType(PayOrderType.NET);
		param.setPayProduct(PayTool.EANK.name());
		param.setCashierType(CommonUtil.transformToOPRVersion(paymentRequest.getCashierVersion()));

		if (bankRuleDTO.isLargeAmount()) {
			param.setPayBusinessType(PayBusinessType.DE);
		} else if (OnlineChannelType.ONLOAN.name().equals(bankRuleDTO.getChannelType())) {
			param.setPayBusinessType(PayBusinessType.DC);
		} else if (OnlineChannelType.ONLYBY.name().equals(bankRuleDTO.getChannelType())) {
			param.setPayBusinessType(PayBusinessType.OD);
		} else {
			param.setPayBusinessType(PayBusinessType.OC);
		}
		// 支付银行编码
		param.setBankId(request.getBankId());
		// 支付账户类型(对公／对私) B2B/B2C
		param.setBankAccountType(
				CommonUtil.getBankAccountType(request.geteBankAccountType()));
		param.setPubBankCusNo(request.getClientId());
		LevelRespDTO respDTO =merchantInfoService.getMerchantLevel(paymentRequest.getMerchantNo());
		param.setCustomerLevel(respDTO.getCheckLevel());
		//支持营销立减
		param.setCombRequestDTO(buildCompPayInfo(request.getToken(), paymentRequest, param.getPayProduct(),
					param.getBankAccountType().name(), request.getBankId()));
		// modify by meiling.zhuang：传用户IP，按照银行定制
		if(StringUtils.isNotBlank(request.getNetPayerIp())){
			param.setPayerIp(request.getNetPayerIp());
		}
		return param;
	}

	/**
	 * 构造 获取支持的网银银行列表 返回值对象
	 * 
	 * @param netPayResponseDTO
	 * @param response
	 */
	private void buildEBankCreatePaymentResponseDTO(NetPayResponseDTO netPayResponseDTO,
			EBankCreatePaymentResponseDTO response) {
		// 跳转pccashier-pay（跳板机）的链接地址 TODO 等到这次上线 response.bankPayUrl可以下线
		response.setBankPayUrl(netPayResponseDTO.getPayUrl());
		response.setPayUrl(netPayResponseDTO.getPayUrl());
		// 直接跳转银行所需的报文
		UrlInfoDTO urlInfoDTO = new UrlInfoDTO();
		urlInfoDTO.setCharset(netPayResponseDTO.getCharset());
		urlInfoDTO.setMethod(netPayResponseDTO.getMethod());
		urlInfoDTO.setParams(netPayResponseDTO.getParam());
		urlInfoDTO.setUrl(netPayResponseDTO.getBankPayUrl());
		response.setUrlInfoDTO(urlInfoDTO);
		response.setProcessStatusEnum(ProcessStatusEnum.SUCCESS);
	}

	/**
	 * 判断网银是否直连，处理回调地址
	 * @param request
	 * @param param
	 */
	private void setUpPageCallBack(EBankCreatePaymentRequestDTO request, NetPayRequestDTO param){
		String urlPrefix = CommonUtil.getCashierUrlPrefix(param.getCustomerNumber());
		if(request.isDirectEbankPay()){
			//如果是直连，传入收银台重定向接口，从PP回调后再直接重定向到商户地址（解决商户回调地址超长问题）
			param.setPageCallBack(urlPrefix+"newpc/ebank/merchantredirect/" + request.getToken());
		}else {
			param.setPageCallBack(urlPrefix+"newpc/ebank/result/" + request.getToken());

		}
	}
}
