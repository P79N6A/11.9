package com.yeepay.g3.core.frontend.dao.impl;

import com.yeepay.g3.core.frontend.dao.PromotionDao;
import com.yeepay.g3.core.frontend.entity.Promotion;
import com.yeepay.g3.utils.persistence.mybatis.GenericDaoDefault;
import org.springframework.stereotype.Repository;

@Repository
public class PromotionDaoImpl extends GenericDaoDefault<Promotion> implements PromotionDao {
}