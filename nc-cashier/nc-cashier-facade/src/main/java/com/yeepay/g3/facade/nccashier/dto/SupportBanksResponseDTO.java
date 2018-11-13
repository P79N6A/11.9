package com.yeepay.g3.facade.nccashier.dto;

import java.io.Serializable;
import java.util.List;

import com.yeepay.g3.utils.common.CollectionUtils;

/**
 * 银行卡必填项结果
* @author zhen.tan
* @since：2016年5月27日 上午10:27:39
*/
public class SupportBanksResponseDTO extends BasicResponseDTO implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 731147649612278786L;

	/**
	 * 支付请求ID
	 */
	private long requestId;

	/**
	 * 
	 * 支持银行
	 */
	private List<BankSupportDTO> supportBankList;

	public long getRequestId() {
		return requestId;
	}

	public void setRequestId(long requestId) {
		this.requestId = requestId;
	}

	public List<BankSupportDTO> getSupportBankList() {
		return supportBankList;
	}

	public void setSupportBankList(List<BankSupportDTO> supportBankList) {
		this.supportBankList = supportBankList;
	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("SupportBanksResponseDTO{");
		sb.append("requestId='").append(this.requestId).append('\'');
		
		if(CollectionUtils.isNotEmpty(this.supportBankList)){
			sb.append(", supportBankList='{");
			for(BankSupportDTO dto : this.supportBankList){
				sb.append(dto.toString()+",");
			}
			sb.append("}");
		}else{
			sb.append(", supportBankList='null'");
		}
		sb.append(","+super.toString());
		sb.append('}');
		return sb.toString();
	}
}
