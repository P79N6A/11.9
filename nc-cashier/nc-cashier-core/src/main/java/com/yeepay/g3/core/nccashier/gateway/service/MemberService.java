package com.yeepay.g3.core.nccashier.gateway.service;

import com.yeepay.g3.component.open.dto.QueryAccountRequestDTO;
import com.yeepay.g3.component.open.dto.QueryAccountResponseDTO;

/**
 * 三代会员子系统
 */
public interface MemberService {

    /**
     * 查询三代会员编号
     * @param queryAccountRequestDTO
     * @return
     */
    QueryAccountResponseDTO queryMemberNo(QueryAccountRequestDTO queryAccountRequestDTO);
}
