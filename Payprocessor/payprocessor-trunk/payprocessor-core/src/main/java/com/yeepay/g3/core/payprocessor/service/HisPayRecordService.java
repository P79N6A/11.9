
package com.yeepay.g3.core.payprocessor.service;

import com.yeepay.g3.core.payprocessor.entity.PayRecord;

import java.util.Date;
import java.util.List;

/**
 * @author peile.fan
 *
 */
public interface HisPayRecordService {

	/**
	 * @param recordNo
	 */
	PayRecord queryRecordById(String recordNo);


}
