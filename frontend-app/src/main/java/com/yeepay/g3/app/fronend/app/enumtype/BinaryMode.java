package com.yeepay.g3.app.fronend.app.enumtype;

import java.io.Serializable;

public abstract class BinaryMode implements Serializable{
	private static final long serialVersionUID = 1L;
	protected int value = 0;
	
	public BinaryMode(int aValue) {
		this.value = aValue<0 ? 0 : aValue;
	}
	
	public int getValue(){
		return value;
	}

	/**
	 * 
	 * <p>开通某业务，func为此业务值</p>
	 * @Title allow
	 * @param func
	 * @return
	 * @return int 
	 * @author SHJ
	 * @Create 2015年2月11日
	 */
	public final int allow(int func){
		if(func < 0 || value < 0){
			throw new IllegalArgumentException();
		}
		return value |= func;
	}

	/**
	 * 
	 * <p>关闭某业务，func为此业务值</p>
	 * @Title disAllow
	 * @param func
	 * @return void 
	 * @author SHJ
	 * @Create 2015年2月11日
	 */
	public final int disAllow(int func){
		if(func < 0 || value < 0){
			throw new IllegalArgumentException();
		}
		
		if(this.isAllow(func)) return value ^= func;
		
		return 0;
	}
	
	/**
	 * 
	 * <p>判断某业务是否开通，func为业务值</p>
	 * @Title isAllow
	 * @param func
	 * @return
	 * @return boolean 
	 * @author SHJ
	 * @Create 2015年2月11日
	 */
	public final boolean isAllow(int func){
		if(func < 0 || value < 0){
			throw new IllegalArgumentException();
		}
		return (value & func)==func;
	}
}
