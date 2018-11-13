
package com.yeepay.g3.facade.payprocessor.facade;

import com.yeepay.g3.core.payprocessor.BaseTest;
import com.yeepay.g3.facade.payprocessor.dto.OperationRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.OperationResponseDTO;
import com.yeepay.g3.utils.rmi.RemoteServiceFactory;
import com.yeepay.g3.utils.rmi.RemotingProtocol;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * 
 * @author peile.fan
 * @since:2017年1月11日 下午2:35:48
 */
public class PayProcessorOperationFacadeTest extends BaseTest {

//	@Autowired
//	private PayProcessorOperationFacade payProcessorOperationFacade;

	private PayProcessorOperationFacade payProcessorOperationFacade = RemoteServiceFactory.getService(
			"http://10.132.2.22:30013/payprocessor-hessian/" ,
			RemotingProtocol.HESSIAN , PayProcessorOperationFacade.class);

//	PayProcessorOperationFacade payProcessorOperationFacade = RemoteServiceFactory.getService(
//            "http://10.151.32.27:30013/payprocessor-hessian/" ,
//            RemotingProtocol.HESSIAN , PayProcessorOperationFacade.class);


//	protected  PayProcessorOperationFacade payProcessorOperationFacade = RemoteFacadeProxyFactory.getService(PayProcessorOperationFacade.class, ExternalSystem.PP);

	@Test
	public void testbatchRepair() {
		OperationRequestDTO request = new OperationRequestDTO();
		List<String> recordList = new ArrayList<String>();
		recordList.add("ACCOUNT1706191128154032080");
		request.setRecordList(recordList);
		OperationResponseDTO response = payProcessorOperationFacade.batchRepair(request);
		System.out.println(response);
	}

	@Test
	public void testbatchReNotify() {
		OperationRequestDTO request = new OperationRequestDTO();
		List<String> recordList = new ArrayList<String>();
		recordList.add("SALE1807260804251191674");
		recordList.add("SALE1807301015092473806");
		recordList.add("SALE1807310926543185062");
		recordList.add("SALE1808071004223976944");
		recordList.add("SALE1809022051232675235");
		recordList.add("SALE1809040238547587092");
		recordList.add("SALE1809040805090405609");
		recordList.add("SALE1809050859365717835");
		recordList.add("SALE1809071557287220401");
		recordList.add("SALE1809071619558661222");
		recordList.add("SALE1809071627328148483");
		recordList.add("SALE1809081217404486568");
		recordList.add("SALE1809090843544945788");
		recordList.add("SALE1809101108079715391");
		recordList.add("SALE1809101129045652677");
		recordList.add("SALE1809101145139233648");
		recordList.add("SALE1809101521194942169");
		request.setRecordList(recordList);
		OperationResponseDTO response = payProcessorOperationFacade.batchReNotify(request);
		System.out.println(response);
	}



}
