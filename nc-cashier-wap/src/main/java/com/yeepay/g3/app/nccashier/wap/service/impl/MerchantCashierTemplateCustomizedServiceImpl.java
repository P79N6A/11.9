package com.yeepay.g3.app.nccashier.wap.service.impl;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.app.nccashier.wap.service.MerchantCashierTemplateCustomizedService;
import com.yeepay.g3.app.nccashier.wap.service.NcCashierService;
import com.yeepay.g3.app.nccashier.wap.utils.CommonUtil;
import com.yeepay.g3.app.nccashier.wap.utils.RedisTemplate;
import com.yeepay.g3.app.nccashier.wap.vo.MerchantCashierCustomizedLayoutSelectVO;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import com.yeepay.g3.facade.nccashier.dto.MerchantCashierCustomizedFileDTO;
import com.yeepay.g3.facade.nccashier.dto.MerchantCashierCustomizedLayoutSelectDTO;
import com.yeepay.g3.facade.nccashier.enumtype.PayTool;
import com.yeepay.g3.utils.common.StringUtils;
import com.yeepay.g3.utils.common.log.Logger;
import com.yeepay.g3.utils.common.log.LoggerFactory;

/**
 * 
 * @Description 收银台定制化service
 * @author yangmin.peng
 * @since 2017年6月16日上午11:20:50
 */
@Service("merchantCashierTemplateCustomizedService")
public class MerchantCashierTemplateCustomizedServiceImpl implements MerchantCashierTemplateCustomizedService {
	private static Logger logger = LoggerFactory.getLogger(MerchantCashierTemplateCustomizedServiceImpl.class);

	/** 服务器本地布局或logo文件的版本号*/
	private Map<String, String> localFileFlag = new ConcurrentHashMap<String, String>();
	@Resource
	private NcCashierService ncCashierService;

	@Override
	public MerchantCashierCustomizedLayoutSelectVO queryMerchantCashierCustomizedLayoutSelectInfo(String merchantNo) {
		MerchantCashierCustomizedLayoutSelectVO merchantCashierCustomizedLayoutSelectVO = new MerchantCashierCustomizedLayoutSelectVO();
		if(StringUtils.isNotBlank(merchantNo)){
			MerchantCashierCustomizedLayoutSelectDTO merchantCashierCustomizedLayoutSelectDTO = ncCashierService
					.queryMerchantCashierCustomizedLayoutSelectInfo(merchantNo);
			if(null != merchantCashierCustomizedLayoutSelectDTO){
				merchantCashierCustomizedLayoutSelectVO.setFrontColor(merchantCashierCustomizedLayoutSelectDTO.getFrontColor());
				merchantCashierCustomizedLayoutSelectVO.setBackColor(merchantCashierCustomizedLayoutSelectDTO.getBackColor());
				merchantCashierCustomizedLayoutSelectVO.setLayoutNo(merchantCashierCustomizedLayoutSelectDTO.getLayoutNo());
				merchantCashierCustomizedLayoutSelectVO.setLogoNo(merchantCashierCustomizedLayoutSelectDTO.getLogoNo());
				merchantCashierCustomizedLayoutSelectVO.setNeedCustomService(merchantCashierCustomizedLayoutSelectDTO.isNeedCustomService());
				merchantCashierCustomizedLayoutSelectVO.setPayToolOrder(merchantCashierCustomizedLayoutSelectDTO.getPayToolOrder());
				merchantCashierCustomizedLayoutSelectVO.setServicePhone(merchantCashierCustomizedLayoutSelectDTO.getServicePhone());
				merchantCashierCustomizedLayoutSelectVO.setLayoutUpdateVersion(merchantCashierCustomizedLayoutSelectDTO.getLayoutUpdateVersion());
				merchantCashierCustomizedLayoutSelectVO.setLogoUpdateVersion(merchantCashierCustomizedLayoutSelectDTO.getLogoUpdateVersion());
			}
		}
		return merchantCashierCustomizedLayoutSelectVO;
	}

	@Override
	public void queryMerchantCashierCustomizedFile(MerchantCashierCustomizedLayoutSelectVO merchantCashierCustomizedLayoutSelect) {		
		// 生成模版文件逻辑
		String layoutNo = merchantCashierCustomizedLayoutSelect.getLayoutNo();
		if (StringUtils.isNotBlank(layoutNo)) {
			String[] lessFileTypes = { Constant.MERCHANT_CASHIER_CUSTOMIZED_LESS_FILE_TYPE,
					Constant.MERCHANT_CASHIER_CUSTOMIZED_JS_FILE_TYPE };
			handleAndGenerateFile(layoutNo, lessFileTypes, Constant.MERCHANT_CASHIER_CUSTOMIZED_LESS_FILE+layoutNo,merchantCashierCustomizedLayoutSelect.getLayoutUpdateVersion());
		}
		// 生成logo文件逻辑
		String logoNo = merchantCashierCustomizedLayoutSelect.getLogoNo();
		if (StringUtils.isNotBlank(logoNo)) {
			String[] logoFileTypes = { Constant.MERCHANT_CASHIER_CUSTOMIZED_LOGO_FILE_TYPE};
			handleAndGenerateFile(logoNo, logoFileTypes, Constant.MERCHANT_CASHIER_CUSTOMIZED_LOGO_FILE+logoNo, merchantCashierCustomizedLayoutSelect.getLogoUpdateVersion());
		}
	}

	/**
	 * @param customizedFileId 定制化文件编号
	 * @param customizedFileTypes 定制化文件类型
	 * @param localFileUpdateVersionKey 定制化文件本地更新版本号缓存key
	 * @param remoteFileUpdateVersion 定制化文件远处更新版本号
	 */
	private void handleAndGenerateFile(String customizedFileId, String[] customizedFileTypes, String localFileUpdateVersionKey, String remoteFileUpdateVersion) {
		MerchantCashierCustomizedFileDTO merchantCashierCustomizedFile = null;
		Map<String,MerchantCashierCustomizedFileDTO> merchantCashierCustomizedFileMap = new HashMap<String,MerchantCashierCustomizedFileDTO>();

		String localFileMapValueNew = "";
		// 文件远程版本号存在且与本地版本号相等，或者远程版本号不存在且本地版本号有值
		if ((StringUtils.isNotBlank(remoteFileUpdateVersion) && remoteFileUpdateVersion.equals(localFileFlag.get(localFileUpdateVersionKey)))
				|| (StringUtils.isBlank(remoteFileUpdateVersion) && StringUtils.isNotBlank(localFileFlag.get(localFileUpdateVersionKey)))) {
			return;
		}
		if (StringUtils.isNotBlank(remoteFileUpdateVersion) && !remoteFileUpdateVersion.equals(localFileFlag.get(localFileUpdateVersionKey))) {
			// 将远程版本号写入本地版本号key
			localFileMapValueNew = remoteFileUpdateVersion;
		}
		if (StringUtils.isBlank(remoteFileUpdateVersion) && StringUtils.isBlank(localFileFlag.get(localFileUpdateVersionKey))) {
			// 生成UUID写入本地版本号
			localFileMapValueNew = UUID.randomUUID().toString();
		}
		for (String customizedFileType : customizedFileTypes) {
			// 获取文件
			merchantCashierCustomizedFile = ncCashierService.queryMerchantCashierCustomizedFile(customizedFileId, customizedFileType);
			merchantCashierCustomizedFileMap.put(customizedFileType, merchantCashierCustomizedFile);
		}
		//生成文件
		generateLayoutFile(merchantCashierCustomizedFileMap, customizedFileId, customizedFileTypes, remoteFileUpdateVersion, localFileUpdateVersionKey, localFileMapValueNew);

	}

	// 生成模版文件或者logo图片
	private synchronized void generateLayoutFile(Map<String, MerchantCashierCustomizedFileDTO> merchantCashierCustomizedFileMap, String customizedFileId,
			String[] customizedFileTypes, String remoteFileUpdateVersion, String localFileUpdateVersionKey, String localFileMapValueNew) {
		if (StringUtils.isNotBlank(remoteFileUpdateVersion) && remoteFileUpdateVersion.equals(localFileFlag.get(localFileUpdateVersionKey))) {
			return;
		}
		String dirPath = Constant.MERCHANT_CASHIER_CUSTOMIZED_FILE_UPLOAD_ROOT_PATH;
		boolean flag = true;
		File dir = new File(dirPath);
		// 如果目录不存在，则创建新的目录
		if (!dir.exists()) {
			dir.mkdirs();
		}
		for (String customizedFileType : customizedFileTypes) {
			StringBuilder fileNameSb = new StringBuilder();
			if (Constant.MERCHANT_CASHIER_CUSTOMIZED_LOGO_FILE_TYPE.equals(customizedFileType)) {
				fileNameSb.append("logo-").append(customizedFileId).append(".jpg").toString();
				// fileName = "logo-" + customizedFileId + ".jpg";
			} else if (Constant.MERCHANT_CASHIER_CUSTOMIZED_JS_FILE_TYPE.equals(customizedFileType)) {
				// fileName = "template-" + customizedFileId + ".js";
				fileNameSb.append("template-").append(customizedFileId).append(".js").toString();
			} else {
				fileNameSb.append("layout-").append(customizedFileId).append(".less").toString();
				// fileName = "layout-" + customizedFileId + ".less";
			}
			byte[] fileContent = merchantCashierCustomizedFileMap.get(customizedFileType).getFileContent();
			File file = new File(dirPath + File.separator + fileNameSb);
			FileOutputStream os = null;
			try {
				// 如果文件不存在，则创建新的文件
				if (!file.exists()) {
					file.createNewFile();
				}
				// 创建文件成功后，生成文件
				os = new FileOutputStream(file);
				if(null != fileContent && fileContent.length > 0){
					os.write(fileContent, 0, fileContent.length);
				}
			} catch (Exception e) {
				flag = false;
				logger.warn("生成文件异常", e);
				break;
			} finally {
				if (os != null) {
					try {
						os.close();
					} catch (Exception e) {
						logger.warn("生成文件异常", e);
						flag = false;
						break;
					}
				}
			}
		}
		// 生成文件成功。如果文件本地版本号为空，或者本地版本号与远程版本号不相等，修改本地版本号
		if (flag && (null == localFileFlag.get(localFileUpdateVersionKey)
				|| !localFileFlag.get(localFileUpdateVersionKey).equals(remoteFileUpdateVersion))) {
			localFileFlag.put(localFileUpdateVersionKey, localFileMapValueNew);
		}

	}
}
