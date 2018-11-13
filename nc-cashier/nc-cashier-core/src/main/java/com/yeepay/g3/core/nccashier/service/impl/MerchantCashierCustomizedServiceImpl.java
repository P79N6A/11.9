package com.yeepay.g3.core.nccashier.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.yeepay.g3.core.nccashier.gateway.service.NcConfigService;
import com.yeepay.g3.core.nccashier.service.MerchantCashierCustomizedService;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.facade.nccashier.dto.MerchantCashierCustomizedFileDTO;
import com.yeepay.g3.facade.nccashier.dto.MerchantCashierCustomizedLayoutSelectDTO;
import com.yeepay.g3.facade.ncconfig.param.BasicMerchantLayoutParam;
import com.yeepay.g3.facade.ncconfig.result.MerchantCashierTemplateFileDTO;
import com.yeepay.g3.facade.ncconfig.result.MerchantLayoutSelectDTO;

/**
 * 
 * @Description 收银台定制化service
 * @author yangmin.peng
 * @since 2017年6月16日下午2:37:26
 */
@Service("merchantCashierCustomizedService")
public class MerchantCashierCustomizedServiceImpl extends NcCashierBaseService
		implements MerchantCashierCustomizedService {

	@Resource
	private NcConfigService ncConfigService;


	@Override
	public MerchantCashierCustomizedLayoutSelectDTO queryMerchantCashierCustomizedLayoutSelectInfo(String merchantNo) {
		
		BasicMerchantLayoutParam merchantLayoutQueryParam = buildMerchantLayoutQueryParm(merchantNo);
		MerchantLayoutSelectDTO merchantLayoutSelectInfo = ncConfigService
				.queryMerchantCashierSelectLayout(merchantLayoutQueryParam);
		MerchantCashierCustomizedLayoutSelectDTO merchantLayoutQueryResult = buildMerchantLayoutQueryResult(
				merchantLayoutSelectInfo);
		return merchantLayoutQueryResult;
	}

	@Override
	public MerchantCashierCustomizedFileDTO queryMerchantCashierCustomizedFile(String fileId, String fileType) {
		
		MerchantCashierTemplateFileDTO merchantCashierTemplateFile = ncConfigService
				.queryMerchantCashierTemplateFileInfo(fileId,fileType);
		MerchantCashierCustomizedFileDTO merchantCashierCustomizedFileResult = buildMerchantCashierCustomizedFileResult(
				merchantCashierTemplateFile);
		return merchantCashierCustomizedFileResult;
	}

	private MerchantCashierCustomizedFileDTO buildMerchantCashierCustomizedFileResult(
			MerchantCashierTemplateFileDTO merchantCashierTemplateFile) {
		MerchantCashierCustomizedFileDTO merchantCashierCustomizedFile = new MerchantCashierCustomizedFileDTO();
		merchantCashierCustomizedFile.setFileContent(merchantCashierTemplateFile.getFileContent());
		merchantCashierCustomizedFile.setFileName(merchantCashierTemplateFile.getFileName());
		return merchantCashierCustomizedFile;
	}

	private BasicMerchantLayoutParam buildMerchantLayoutQueryParm(String merchantNo) {
		BasicMerchantLayoutParam merchantLayoutQueryParm = new BasicMerchantLayoutParam();
		merchantLayoutQueryParm.setMerchantNo(merchantNo);
		return merchantLayoutQueryParm;
	}

	private MerchantCashierCustomizedLayoutSelectDTO buildMerchantLayoutQueryResult(
			MerchantLayoutSelectDTO merchantLayoutSelectInfo) {
		MerchantCashierCustomizedLayoutSelectDTO merchantCashierCustomizedLayoutSelect = new MerchantCashierCustomizedLayoutSelectDTO();
		merchantCashierCustomizedLayoutSelect.setFrontColor(merchantLayoutSelectInfo.getFrontColor());
		merchantCashierCustomizedLayoutSelect.setBackColor(merchantLayoutSelectInfo.getBackColor());
		merchantCashierCustomizedLayoutSelect.setMerchantLayoutId(String.valueOf(merchantLayoutSelectInfo.getMerchantLayoutId()));
		merchantCashierCustomizedLayoutSelect.setLayoutNo(merchantLayoutSelectInfo.getLayoutFileId());
		merchantCashierCustomizedLayoutSelect.setLogoNo(merchantLayoutSelectInfo.getLogoFileId());
		merchantCashierCustomizedLayoutSelect.setNeedCustomService(merchantLayoutSelectInfo.isNeedCustomService());
		merchantCashierCustomizedLayoutSelect.setPayToolOrder(merchantLayoutSelectInfo.getPayTools());
		merchantCashierCustomizedLayoutSelect.setServicePhone(merchantLayoutSelectInfo.getServicePhone());
		merchantCashierCustomizedLayoutSelect.setLayoutUpdateVersion(merchantLayoutSelectInfo.getLayoutUpdateVersion());
		merchantCashierCustomizedLayoutSelect.setLogoUpdateVersion(merchantLayoutSelectInfo.getLogoUpdateVersion());
		return merchantCashierCustomizedLayoutSelect;
	}

}
