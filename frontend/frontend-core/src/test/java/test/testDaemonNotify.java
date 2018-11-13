package test;

import com.yeepay.g3.facade.frontend.facade.novalidate.FrontendReNotifyDaemonFacade;
import com.yeepay.g3.utils.rmi.RemoteServiceFactory;
import com.yeepay.g3.utils.rmi.RemotingProtocol;

public class testDaemonNotify {
	
	public static void main(String[] args) {
		RemoteServiceFactory.init();
		FrontendReNotifyDaemonFacade frontendReNotifyDaemonFacade = RemoteServiceFactory.getService("http://10.151.30.43:8001/frontend-hessian/hessian/FrontendReNotifyDaemonFacade",
				RemotingProtocol.HESSIAN, FrontendReNotifyDaemonFacade.class);
		frontendReNotifyDaemonFacade.reNotifyWX(10);
	}
		

}
