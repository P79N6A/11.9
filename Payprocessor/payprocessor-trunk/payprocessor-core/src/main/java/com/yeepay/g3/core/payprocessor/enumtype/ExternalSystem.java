package com.yeepay.g3.core.payprocessor.enumtype;

/**
 * 外部调用系统枚举
 */
public enum ExternalSystem {
	NCCWH("cwh-hessian"),
    NCAUTH("nc-auth-hessian"),
    NCPAY("nc-pay-hessian"),
    NCCONFIG("nc-config-hessian"),
    MPAY("mpay-web"),
    BANKINTERFACE("bankinterface-hessian"),
    G3MEMBER("g3-member"),
    REFUND("refund-hessian"),
    NOTIFY("notifier-hessian"),
	BANKROUTER("bankchannel-hessian"),
    RISK("riskcontrol-service"),
	FRONTEND("frontend-hessian"),
	NETPAY("frontend-netpay-hessian"),
	CFL("frontend-installment-hessian"),
	OPR("opr-hessian"),
    CS("cs-process-hessian"),
    ACCOUNTPAY("account-pay-hessian"),
    PP("payprocessor-hessian"),
    PERSONALMEMBER("member-hessian"),
    MKTG("mktg-hessian");
	
    /**
     * 获取系统名
     * @return
     */
    public String getSysName() {
        return sysName;
    }

    /**
     * 系统名
     */
    private  final String sysName;

    /**
     * 构造函数
     * @param sysName
     */
    ExternalSystem(String sysName) {
        this.sysName = sysName;
    }


}
