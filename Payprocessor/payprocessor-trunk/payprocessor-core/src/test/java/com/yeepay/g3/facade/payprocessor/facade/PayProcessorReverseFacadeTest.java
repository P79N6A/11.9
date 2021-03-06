package com.yeepay.g3.facade.payprocessor.facade;

import com.yeepay.g3.core.payprocessor.BaseTest;
import com.yeepay.g3.facade.payprocessor.dto.ReverseRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.ReverseResponseDTO;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author chronos.
 * @createDate 2016/11/15.
 */
public class PayProcessorReverseFacadeTest extends BaseTest {

    @Autowired
    private PayProcessorReverseFacade reverseFacade ;

    @Test
    public void reverseRequest() throws Exception {
        ReverseRequestDTO requestDTO = new ReverseRequestDTO();
        requestDTO.setRemark("任性冲正");
        //requestDTO.setOrderNo("order176709660");
        requestDTO.setRecordNo("H5APP147919218483515");
        requestDTO.setRequestSystem("NCCASHIER");
        System.out.println(ToStringBuilder.reflectionToString(requestDTO));
        ReverseResponseDTO responseDTO = reverseFacade.reverseRequest(requestDTO);
        System.out.println(responseDTO.toString());
    }

}