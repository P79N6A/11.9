package test;

import com.yeepay.g3.utils.config.ConfigurationUtils;
import com.yeepay.g3.utils.rmi.RemoteServiceFactory;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

/**
 * @author chronos.
 * @createDate 16/8/9.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"/test-appContext.xml"})
@TransactionConfiguration(defaultRollback = false)
public class BaseTest {

    @BeforeClass
    public static void init(){
        System.out.println("init RemoteServiceFactory and ConfigurationUtils before class:");
        RemoteServiceFactory.init();
        ConfigurationUtils.init();
        System.out.println("init environment success.");
    }
}
