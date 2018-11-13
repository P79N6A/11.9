package com.yeepay.g3.core.payprocessor.dao;

import com.yeepay.g3.core.payprocessor.BaseTest;
import com.yeepay.g3.core.payprocessor.entity.ReverseRecord;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author chronos.
 * @createDate 2016/11/14.
 */
public class ReverseRecordDaoTest extends BaseTest {

    @Autowired
    private ReverseRecordDao reverseRecordDao;

    @Test
    public void deleteByPrimaryKey() throws Exception {

    }

    @Test
    public void insert() throws Exception {
    }

    @Test
    public void insertSelective() throws Exception {

    }

    @Test
    public void selectByPrimaryKey() throws Exception {
        ReverseRecord record = reverseRecordDao.selectByPrimaryKey(1l);
        System.out.println(ToStringBuilder.reflectionToString(record));
    }

    @Test
    public void updateByPrimaryKeySelective() throws Exception {

    }

    @Test
    public void updateByPrimaryKey() throws Exception {

    }

    @Test
    public void queryRecordByDate() throws Exception {

    }

    @Test
    public void queryByRecordNo() throws Exception {

    }

}