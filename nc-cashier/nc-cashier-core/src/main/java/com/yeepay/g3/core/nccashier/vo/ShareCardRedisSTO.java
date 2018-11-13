package com.yeepay.g3.core.nccashier.vo;

import com.yeepay.g3.facade.cwh.param.BaseInfo;

import java.io.Serializable;
import java.util.List;

/**
 * Description
 * PackageName: com.yeepay.g3.core.nccashier.vo
 *
 * @author pengfei.chen
 * @since 17/1/9 15:24
 */
public class ShareCardRedisSTO implements Serializable {

    private List<BaseInfo> list;

    public List<BaseInfo> getList() {
        return list;
    }

    public void setList(List<BaseInfo> list) {
        this.list = list;
    }
}
