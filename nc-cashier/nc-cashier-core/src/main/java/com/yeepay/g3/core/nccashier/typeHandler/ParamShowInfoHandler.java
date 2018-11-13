package com.yeepay.g3.core.nccashier.typeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import com.alibaba.fastjson.JSON;
import com.yeepay.g3.core.nccashier.entity.ParamShowInfo;
import com.yeepay.g3.utils.common.StringUtils;

/**
 * 
 * @author：peile.fan
 * @since：2016年5月26日 上午10:39:19
 * @version:
 */
public class ParamShowInfoHandler implements TypeHandler {
	@Override
	public void setParameter(PreparedStatement ps, int i, Object parameter, JdbcType jdbcType)
			throws SQLException {
		if (parameter != null) {
			if (parameter instanceof ParamShowInfo) {
				ps.setString(i, JSON.toJSONString(parameter));
			} else if (parameter instanceof String) {
				ps.setString(i, parameter.toString());
			}
		} else {
			ps.setString(i, "");
		}
	}

	@Override
	public Object getResult(ResultSet rs, String columnName) throws SQLException {
		String jsonStr = rs.getString(columnName);
		if (StringUtils.isBlank(jsonStr)) {
			return null;
		}
		return JSON.parseObject(jsonStr, ParamShowInfo.class);
	}
	@Override
	public Object getResult(CallableStatement cs, int columnIndex) throws SQLException {
		String jsonStr = cs.getString(columnIndex);
		if (StringUtils.isBlank(jsonStr)) {
			return null;
		}
		return JSON.parseObject(jsonStr, ParamShowInfo.class);
	}

}
