package com.yeepay.g3.app.nccashier.wap.vo;

/**
 * pc二维码已扫标识监听返回
 * @author duangduang
 * @since  2016-11-11
 */
public class ScanListenResponseVO extends ResponseVO{

	private static final long serialVersionUID = 1L;
	
	private boolean scan;
	
	public ScanListenResponseVO(){
		
	}

	public boolean isScan() {
		return scan;
	}

	public void setScan(boolean scan) {
		this.scan = scan;
	}
	
}
