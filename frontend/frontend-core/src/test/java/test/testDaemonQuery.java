package test;


import com.yeepay.g3.facade.frontend.facade.novalidate.FrontendQueryDaemonFacade;
import com.yeepay.g3.utils.rmi.RemoteServiceFactory;
import com.yeepay.g3.utils.rmi.RemotingProtocol;

public class testDaemonQuery {
	public static void main(String[] args) {
		RemoteServiceFactory.init();
		FrontendQueryDaemonFacade frontendQueryDaemonFacade = RemoteServiceFactory.getService("http://10.151.30.43:8001/frontend-hessian/hessian/FrontendQueryDaemonFacade",
				RemotingProtocol.HESSIAN, FrontendQueryDaemonFacade.class);
		frontendQueryDaemonFacade.queryBankOrderWX(10);
	}
}
