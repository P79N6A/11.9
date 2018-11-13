package com.yeepay.g3.facade.nccashier.util;

public class NullObject {
	
	public static boolean objectIsNull(Object t){
		boolean isNull = true;
		for (java.lang.reflect.Field f : t.getClass().getDeclaredFields()) {
	    f.setAccessible(true);
			   try {
				   if(f.getName().equals("serialVersionUID")){
					   continue;
				   }
				   if(f.get(t) !=null&&!"".equals(f.get(t).toString())){
						   isNull = false;
							break;
				}
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
	}
		return isNull;
	}
}
