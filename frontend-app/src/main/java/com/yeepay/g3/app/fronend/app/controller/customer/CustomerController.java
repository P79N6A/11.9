package com.yeepay.g3.app.fronend.app.controller.customer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.yeepay.g3.app.fronend.app.controller.BaseController;
import com.yeepay.g3.app.fronend.app.dto.OperationResponse;
import com.yeepay.g3.facade.foundation.dto.MerchantPayConfigDto;
import com.yeepay.g3.facade.foundation.dto.MerchantPayConfigDto.PayProductDto;
import com.yeepay.g3.facade.foundation.facade.MerchantConfigFacade;
import com.yeepay.g3.facade.nctradeconfig.facade.AccessQueryFacade;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.rmi.RemoteServiceFactory;


/**
 * 
 * @author wangmeimei
 *
 */
@Controller
@RequestMapping("customer")
public class CustomerController extends BaseController {
	
	MerchantConfigFacade merchantConfigFacade = RemoteServiceFactory.getService(MerchantConfigFacade.class);
	
	AccessQueryFacade queryFacade = RemoteServiceFactory.getService(AccessQueryFacade.class);
	
	@RequestMapping("uploadCustomer")
    public String queryOrder() throws Exception {
        return "customer/uploadCustomer";
    }

	 @RequestMapping("uploadList")
	 @ResponseBody
	 public OperationResponse uploadList(@RequestParam(value = "excelFile") MultipartFile file) throws Exception {
		 InputStream inputStream = file.getInputStream();
		 Workbook rwb = Workbook.getWorkbook(inputStream);
		 Sheet rs = rwb.getSheet(0);
		 int rsRows = rs.getRows();  //获取Sheet表中所包含的总行数
			  
		 List<MerchantPayConfigDto> listMerchant = new ArrayList<MerchantPayConfigDto>();
		 for(int j = 0;j < rsRows;j++){
			 Cell[] cellRow = rs.getRow(j);
			 MerchantPayConfigDto merchantPayConfigDto = new MerchantPayConfigDto();
			 if(cellRow.length >= 1 && StringUtils.isNotBlank(cellRow[0].getContents())){
				 merchantPayConfigDto.setMerchantNo(cellRow[0].getContents().trim());
			 } else {
				 continue;
			 }
			 if(cellRow.length < 3 || StringUtils.isBlank(cellRow[2].getContents())){
				 merchantPayConfigDto.setMccCode("9999");    //如果不需要开通网银一键，则MCC默认9999
			 } 

			Map<String, List<PayProductDto>> payProductMap = new HashMap<String, List<PayProductDto>>();
			List<PayProductDto> listPay = new ArrayList<PayProductDto>();
			if(cellRow.length >= 2 && StringUtils.isNotBlank(cellRow[1].getContents())){
				PayProductDto payProductDto = new PayProductDto();
				payProductDto.setPayProductName("EANK");              //网银支付产品固定值EANK
				payProductDto.setPayScene(cellRow[1].getContents().trim());   //网银支付场景
				List<String> payWays = new ArrayList<String>();
				payWays.add("NET_BANK");                               //网银支付银行固定值NET_BANK
				payProductDto.setPayWays(payWays);
				listPay.add(payProductDto);
			}
			if(cellRow.length >= 3 && StringUtils.isNotBlank(cellRow[2].getContents())){
				PayProductDto payProductDto = new PayProductDto();
				payProductDto.setPayProductName("NCPAY");             //网银一键支付产品固定值NCPAY
				payProductDto.setPayScene(cellRow[2].getContents().trim());   //网银一键支付场景
				List<String> payWays = new ArrayList<String>();
				payWays.add("BANK_PAY_WAP");                           //网银一键支付银行固定值NCPAY
				payProductDto.setPayWays(payWays);                   
				listPay.add(payProductDto);
			}
			if(cellRow.length >= 4 && StringUtils.isNotBlank(cellRow[3].getContents())
					&& "SCCANPAY".equals(cellRow[3].getContents().trim())){
				PayProductDto payProductDto = new PayProductDto();
				payProductDto.setPayProductName("SCCANPAY");  //钱包支付支付产品固定值SCCANPAY
				List<String> payWays = new ArrayList<String>();             //钱包支付支付银行默认开通WECHAT_OPENID／WECHAT_ATIVE_SCAN／ALIPAY
				payWays.add("WECHAT_OPENID");                     
				payWays.add("WECHAT_ATIVE_SCAN");
				payWays.add("ALIPAY");
				payProductDto.setPayWays(payWays);
				listPay.add(payProductDto);
			}
			payProductMap.put("WEB", listPay);                     //二代迁移商户收款场景固定值WEB
			merchantPayConfigDto.setPayProductMap(payProductMap);
			listMerchant.add(merchantPayConfigDto);
		 }
		 
		 OperationResponse opResponse  = new OperationResponse();
		 List<String> errorList = new ArrayList<String>();
		 Integer success = new Integer(0);
		 for(MerchantPayConfigDto merchantPayConfigDto : listMerchant){
			 try{
				 if(StringUtils.isBlank(merchantPayConfigDto.getMccCode())){
					 List<String> merchantGoodsCode = queryFacade.queryMerchantGoodsCode(merchantPayConfigDto.getMerchantNo());
						if(merchantGoodsCode == null || merchantGoodsCode.size() <= 0){
							throw new Exception("mcc查询结果为空!");
						}
						logger.info(merchantPayConfigDto.getMerchantNo() + " mcc code: " + merchantGoodsCode);
					 	merchantPayConfigDto.setMccCode(merchantGoodsCode.get(0));   //如果配置多个MCC码，则选择第一个
				 }
				 logger.info(ToStringBuilder.reflectionToString(merchantPayConfigDto));
				 merchantConfigFacade.addPayConfig(merchantPayConfigDto);
				 success++;
			 } catch(Exception e){
				 errorList.add(merchantPayConfigDto.getMerchantNo() + " 配置错误: " + e.getMessage());
			 }
		 }
		 opResponse.setSuccess(success);
		 opResponse.setErrorList(errorList);
		 return opResponse;
	 }
	 
}
