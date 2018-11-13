package com.yeepay.g3.facade.nccashier.enumtype;


public enum BankEnum {
	ABC("农业银行"),
	BCCB("北京银行"),
	BJRCB("北京农商银行"),
	BOC("中国银行"),
	BOCO("交通银行"),
	BSB("包商银行"),
	CBHB("渤海银行"),
	CCB("建设银行"),
	CEB("光大银行"),
	CIB("兴业银行"),
	CMBC("民生银行"),
	CMBCHINA("招商银行"),
	ECITIC("中信银行"),
	GDB("广发银行"),
	HXB("华夏银行"),
	HX("华夏银行"),
	ICBC("工商银行"),
	PSBC("邮储银行"),
	POST("邮储银行"),
	SDB("深发银行"),
	SHB("上海银行"),
	SPDB("浦发银行"),
	SZPA("平安银行"),
	PINGAN("平安银行"),
	
	NBCB("宁波银行"),
	NCBC("宁波银行"),
	GZCB("广州银行"),
	JSBC("江苏银行"),
	SRCB("上海农商银行"),
	HZB("杭州银行"),
	NJCB("南京银行"),
	HSB("徽商银行"),
	GZRCB("广州农商银行"),
	HRB("哈尔滨银行"),//HRBCB("哈尔滨银行"),
	TJB("天津银行"),//TCCB("天津银行"),
	CQRCB("重庆农村商业银行"),
	LJB("龙江银行"),
	GYCCB("贵阳银行"),
	TZB("台州银行"),
	DLB("大连银行"),
	HEBBANK("河北银行"),//HEBB("河北银行"),
	SJB("盛京银行"),
	CDRCB("成都农商银行"),
	HKBC("汉口银行"),
	UCCB("乌鲁木齐市商业银行"),
	WZB	("温州银行"),//WZCB("温州银行"),
	CQCB("重庆银行"),
	NCCB("南昌银行"),
	
	BOCD("成都商业银行"),
	CRBC("华润银行"),
	CZ("浙商银行"),
	DGB("东莞银行"),
	GDNYB("南粤银行"),
	HKBEA("东亚银行"),
	KMRCU("昆明农联社"),
	JSHB("晋商银行"),
	JZBANK("锦州银行"),//JZB("锦州银行"),
	NCB("南洋商业银行"),
	SZRCB("深圳农商银行"),
	UPOP("银联在线"),
	ZJTLCB("浙江泰隆商业银行"),
	
	SDRCU("山东省农村信用社"),
	SCRCU("四川省农村信用社");



	/**
     *描述
     */
    private String name;
    @Deprecated
    public static BankEnum valueOfString(String bankCode){
    	try {
    		//由于账账户中的平安银行编码是"PINGAN",需要进行转化
        	if("PINGAN".equalsIgnoreCase(bankCode)){
        		return BankEnum.SZPA;
        	}
        	else if("HX".equalsIgnoreCase(bankCode)){
        		return BankEnum.HXB;
        	}
        	return BankEnum.valueOf(bankCode);
		} catch (Exception e) {
			
		}
    	return null;
    }
    
    
    private BankEnum(){
    }
    
    private BankEnum(String name){
    	this.setName(name);
    }
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
