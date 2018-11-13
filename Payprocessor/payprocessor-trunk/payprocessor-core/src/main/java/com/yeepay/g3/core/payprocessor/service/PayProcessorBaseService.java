package com.yeepay.g3.core.payprocessor.service;

import com.yeepay.g3.core.payprocessor.common.RemoteFacadeProxyFactory;
import com.yeepay.g3.core.payprocessor.enumtype.ExternalSystem;
import com.yeepay.g3.facade.frontend.facade.FrontendPayFacade;
import com.yeepay.g3.facade.frontend.facade.FrontendQueryFacade;
import com.yeepay.g3.facade.frontend.facade.FrontendRefundFacade;
import com.yeepay.g3.facade.ncpay.facade.NcPayResultTaskFacade;
import com.yeepay.g3.facade.ncpay.facade.PaymentManageFacade;
import com.yeepay.g3.facade.ncpay.facade.PaymentManagerWrapperFacade;

public class PayProcessorBaseService {

	protected PaymentManagerWrapperFacade paymentFacade = RemoteFacadeProxyFactory
			.getService(PaymentManagerWrapperFacade.class, ExternalSystem.NCPAY);

	protected PaymentManageFacade paymentManageFacade = RemoteFacadeProxyFactory.getService(PaymentManageFacade.class,
			ExternalSystem.NCPAY);

	protected NcPayResultTaskFacade ncPayResultTaskFacade = RemoteFacadeProxyFactory
			.getService(NcPayResultTaskFacade.class, ExternalSystem.NCPAY);
	
	
	protected FrontendPayFacade frontendPayFacade = RemoteFacadeProxyFactory
			.getService(FrontendPayFacade.class,ExternalSystem.FRONTEND);

	protected FrontendRefundFacade frontendRefundFacade = RemoteFacadeProxyFactory
			.getService(FrontendRefundFacade.class, ExternalSystem.FRONTEND);

	protected FrontendQueryFacade frontendQueryFacade = RemoteFacadeProxyFactory.getService(FrontendQueryFacade.class,
			ExternalSystem.FRONTEND);
	
}
