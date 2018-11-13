package com.yeepay.g3.core.nccashier.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.yeepay.g3.core.nccashier.vo.MerchantInNetConfigModel;
import com.yeepay.g3.core.nccashier.vo.MerchantInNetConfigModel.Product;
import com.yeepay.g3.facade.nccashier.enumtype.CashierVersionEnum;
import com.yeepay.g3.facade.nccashier.enumtype.PayTool;

public class CashierPayProductUtil {

	/**
	 * 根据三级支付银行构造对象Map<CashierVersionEnum, List<Product>> TODO
	 * 简化，version=WAP，PAY_TOOL=EWALLETH5
	 * 
	 * @param payTypeList
	 * @return
	 */
	public static Map<CashierVersionEnum, List<Product>> constructPayProductHierarchy(List<String> payTypeList) {
		Product product = new Product();
		product.setPayTool(PayTool.EWALLETH5);
		product.setPayTypes(payTypeList);
		List<Product> products = new ArrayList<MerchantInNetConfigModel.Product>();
		products.add(product);
		Map<CashierVersionEnum, List<Product>> productsOfVersion = new HashMap<CashierVersionEnum, List<Product>>();
		productsOfVersion.put(CashierVersionEnum.WAP, products);
		return productsOfVersion;
	}

}
