package test;

import com.yeepay.g3.facade.frontend.facade.novalidate.FrontendRefundDaemonFacade;
import com.yeepay.g3.utils.rmi.RemoteServiceFactory;
import com.yeepay.g3.utils.rmi.RemotingProtocol;

public class testDaemonRefund {
	
	public static void main(String[] args) {
		RemoteServiceFactory.init();
		FrontendRefundDaemonFacade frontendRefundDaemonFacade = RemoteServiceFactory.getService("http://10.151.30.43:8001/frontend-hessian/hessian/FrontendRefundDaemonFacade",
				RemotingProtocol.HESSIAN, FrontendRefundDaemonFacade.class);
		frontendRefundDaemonFacade.errorRefundWX(10);
	}

}
