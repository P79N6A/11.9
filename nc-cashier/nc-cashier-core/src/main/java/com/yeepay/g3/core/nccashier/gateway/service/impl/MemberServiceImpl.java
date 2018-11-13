package com.yeepay.g3.core.nccashier.gateway.service.impl;

import com.yeepay.g3.component.open.dto.QueryAccountRequestDTO;
import com.yeepay.g3.component.open.dto.QueryAccountResponseDTO;
import com.yeepay.g3.component.platform.exception.PlatformBasicsException;
import com.yeepay.g3.core.nccashier.enumtype.SystemEnum;
import com.yeepay.g3.core.nccashier.gateway.service.MemberService;
import com.yeepay.g3.core.nccashier.service.NcCashierBaseService;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.facade.member.param.MemberBaseResponse;
import com.yeepay.g3.facade.nccashier.enumtype.SysCodeEnum;
import com.yeepay.g3.facade.nccashier.error.Errors;
import org.springframework.stereotype.Service;

@Service
public class MemberServiceImpl extends NcCashierBaseService implements MemberService {

    @Override
    public QueryAccountResponseDTO queryMemberNo(QueryAccountRequestDTO queryAccountRequestDTO) {
        QueryAccountResponseDTO responseDTO = null;
        try {
            responseDTO = openMemberQueryAccountFacade.queryAccount(queryAccountRequestDTO);
        } catch (PlatformBasicsException e) {
            throw CommonUtil.handleException(SysCodeEnum.MB.name(), e.getDefineCode(), Errors.SYSTEM_EXCEPTION.getMsg());
        } catch (Throwable t) {
            throw CommonUtil.handleException(Errors.SYSTEM_EXCEPTION);
        }
        return responseDTO;
    }

}
