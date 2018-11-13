import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.lang.builder.ToStringBuilder;

import com.yeepay.g3.facade.foundation.dto.MerchantPayConfigDto;
import com.yeepay.g3.facade.foundation.dto.MerchantPayConfigDto.PayProductDto;
import com.yeepay.g3.facade.foundation.facade.MerchantConfigFacade;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.rmi.RemoteServiceFactory;
import com.yeepay.g3.utils.rmi.RemotingProtocol;


public class testPayConfig {
	
	public static void main(String[] args) {
		try {
			InputStream is = new FileInputStream("/Users/wangmeimei/Downloads/test.xls");
			Workbook rwb = Workbook.getWorkbook(is);
//			System.out.println("Sheet的个数："+rwb.getNumberOfSheets());
			
			Sheet rs = rwb.getSheet(0);
			  
//			String sheetName = rs.getName();//获取Sheet的名称
//			System.out.println("Sheet的名称："+sheetName);
			  
//			int rsColumns = rs.getColumns();//获取Sheet表中所包含的总列数
//			System.out.println("列数："+rsColumns);
			  
			int rsRows = rs.getRows();  //获取Sheet表中所包含的总行数
			System.out.println("行数："+rsRows);
			  
			List<MerchantPayConfigDto> listMerchant = new ArrayList<MerchantPayConfigDto>();
			
			for(int j = 0;j < rsRows;j++){
				Cell[] cellRow = rs.getRow(j);
				
				MerchantPayConfigDto merchantPayConfigDto = new MerchantPayConfigDto();
				if(StringUtils.isBlank(cellRow[2].getContents())){
					merchantPayConfigDto.setMccCode("9999");
				} else {
					merchantPayConfigDto.setMccCode("2");
				}
				
				merchantPayConfigDto.setMerchantNo(cellRow[0].getContents());
				
				Map<String, List<PayProductDto>> payProductMap = new HashMap<String, List<PayProductDto>>();
				List<PayProductDto> listPay = new ArrayList<PayProductDto>();
				if(StringUtils.isNotBlank(cellRow[1].getContents())){
					PayProductDto payProductDto = new PayProductDto();
					payProductDto.setPayProductName("EANK");
					payProductDto.setPayScene(cellRow[1].getContents());
					List<String> payWays = new ArrayList<String>();
					payWays.add("NET_BANK");
					payProductDto.setPayWays(payWays);
					System.out.println(ToStringBuilder.reflectionToString(payProductDto));
					listPay.add(payProductDto);
				}
				if(StringUtils.isNotBlank(cellRow[2].getContents())){
					PayProductDto payProductDto = new PayProductDto();
					payProductDto.setPayProductName("NCPAY");
					payProductDto.setPayScene(cellRow[2].getContents());
					List<String> payWays = new ArrayList<String>();
					payWays.add("BANK_PAY_WAP");
					payProductDto.setPayWays(payWays);
					System.out.println(ToStringBuilder.reflectionToString(payProductDto));
					listPay.add(payProductDto);
				}
				if(StringUtils.isNotBlank(cellRow[3].getContents())){
					PayProductDto payProductDto = new PayProductDto();
					payProductDto.setPayProductName("SCCANPAY");
					List<String> payWays = new ArrayList<String>();
					payWays.add("WECHAT_OPENID");
					payWays.add("WECHAT_ATIVE_SCAN");
					payWays.add("ALIPAY");
					payProductDto.setPayWays(payWays);
					System.out.println(ToStringBuilder.reflectionToString(payProductDto));
					listPay.add(payProductDto);
				}
				
				payProductMap.put("WEB", listPay);
				merchantPayConfigDto.setPayProductMap(payProductMap);
				
				System.out.println(ToStringBuilder.reflectionToString(merchantPayConfigDto));
				
				listMerchant.add(merchantPayConfigDto);
			}
			
//			MerchantConfigFacade merchantConfigFacade = RemoteServiceFactory.getService("", 
//					RemotingProtocol.HESSIAN, MerchantConfigFacade.class);
//					
//			 for(MerchantPayConfigDto merchantPayConfigDto : listMerchant){
//				 try{
//					 System.out.println(ToStringBuilder.reflectionToString(merchantPayConfigDto));
//					 merchantConfigFacade.addPayConfig(merchantPayConfigDto);
//				 } catch(Exception e){
//					 e.printStackTrace();
//				 }
//			 }

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}
