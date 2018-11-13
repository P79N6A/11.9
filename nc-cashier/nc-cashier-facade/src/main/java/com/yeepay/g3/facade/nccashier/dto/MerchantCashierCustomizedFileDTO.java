package com.yeepay.g3.facade.nccashier.dto;

import java.util.Date;

/**
 * 
 * @Description 收银台定制化模版文件实体
 * @author yangmin.peng
 * @since 2017年6月16日上午11:42:10
 */
public class MerchantCashierCustomizedFileDTO extends BasicResponseDTO {

	private static final long serialVersionUID = 1L;
	/**
	 * 文件编号
	 */
	private String fileId;
	/**
	 * 模版名称/logo文件名称
	 */
	private String fileName;
	/**
	 * 文件内容
	 */
	private byte[] fileContent;
	/**
	 * 文件类型(LESS，JS，LOGO)
	 */
	private String fileType;
	/**
	 * 创建时间
	 */
	private Date createTime;
	/**
	 * 最近更新时间
	 */
	private Date updateTime;
	/**
	 * 操作员
	 */
	private String operator;

	public String getFileId() {
		return fileId;
	}

	public void setFileId(String fileId) {
		this.fileId = fileId;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public byte[] getFileContent() {
		return fileContent;
	}

	public void setFileContent(byte[] fileContent) {
		this.fileContent = fileContent;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer("MerchantCashierCustomizedFileDTO{");
		sb.append("fileId='").append(fileId).append('\'');
		sb.append("fileName='").append(fileName).append('\'');
		sb.append("fileContent='").append(fileContent).append('\'');
		sb.append("fileType='").append(fileType).append('\'');
		sb.append("operator='").append(operator).append('\'');
		sb.append("," + super.toString());
		sb.append('}');
		return sb.toString();

	}

}
