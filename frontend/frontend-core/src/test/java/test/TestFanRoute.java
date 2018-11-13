package test;

import com.yeepay.g3.core.frontend.biz.FanRouteBiz;
import com.yeepay.g3.core.frontend.service.PayRecordService;
import com.yeepay.g3.facade.frontend.dto.FanRouteAddRequestDTO;
import com.yeepay.g3.facade.frontend.dto.FanRouteAddResponseDTO;
import org.junit.Test;

import javax.annotation.Resource;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestFanRoute  extends BaseTest{

    @Resource
    FanRouteBiz fanRouteBiz;
    @Resource
    PayRecordService payRecordService;




    @Test
    public void add(){
        FanRouteAddRequestDTO requestDTO = new FanRouteAddRequestDTO();
        requestDTO.setCustomerNum("1111");
        requestDTO.setReportId("22222");
        requestDTO.setSubCustomerNum("33333");
        FanRouteAddResponseDTO fanRouteAddResponseDTO = fanRouteBiz.addFanRouteInfo(requestDTO);
        System.out.println("==========".concat(fanRouteAddResponseDTO.toString()));
    }

    private static ExecutorService executorService =  Executors.newFixedThreadPool(2);

    private  int i = 0;
    @Test
    public void testVolidate(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                i++;
                System.out.println("======i==="+i);
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                i++;
                System.out.println("----i-----"+i);
            }
        }).start();
        while (Thread.activeCount()>1){
            Thread.yield();
        }
    }

}
