package com.yeepay.g3.facade.payprocessor.facade;

import com.yeepay.g3.core.payprocessor.BaseTest;
import com.yeepay.g3.facade.payprocessor.dto.PayRecordQueryRequestDTO;
import com.yeepay.g3.facade.payprocessor.dto.PayRecordResponseDTO;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author chronos.
 * @createDate 2016/11/15.
 */
public class PayRecordQueryFacadeTest extends BaseTest {

    @Autowired
    private PayRecordQueryFacade queryFacade;
//    PayRecordQueryFacade queryFacade = getRemoteFacade(PayRecordQueryFacade.class, false);

    @Test
    public void query() throws Exception {
        PayRecordQueryRequestDTO requestDTO = new PayRecordQueryRequestDTO();
        requestDTO.setRecordNo("NET1807161518346414940");
        System.out.println(ToStringBuilder.reflectionToString(requestDTO));
        PayRecordResponseDTO responseDTO = queryFacade.query(requestDTO);
        System.out.println(responseDTO.toString());
    }

}