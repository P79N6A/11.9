package com.yeepay.g3.app.nccashier.wap.base;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.test.context.web.ServletTestExecutionListener;

import com.yeepay.g3.utils.config.ConfigurationUtils;
import com.yeepay.g3.utils.rmi.RemoteServiceFactory;

@RunWith(SpringJUnit4ClassRunner.class)
// 使用junit4进行测试
@ContextConfiguration(locations ={"classpath*:/test-nc-cashier-appContext.xml","classpath*:/test-nc-cashier-servlet.xml"})
@TransactionConfiguration(transactionManager = "txManager", defaultRollback = true)
// 加载配置文件
@TestExecutionListeners({TransactionalTestExecutionListener.class,
		ServletTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
		DirtiesContextTestExecutionListener.class, NcCashierExecutionListener.class})
public class ActionBaseTest extends AbstractJUnit4SpringContextTests {
	
	@BeforeClass
	public static void initconfig() {
		RemoteServiceFactory.init();
		ConfigurationUtils.init();
	}
	
//	public BaseTest() {
//		ConfigurationUtils.init();
//	}
	
}
