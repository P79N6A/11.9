package com.yeepay.g3.core.nccashier.typeHandler;

import com.yeepay.g3.core.nccashier.utils.AESUtil;
import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 敏感信息identityId加密存储typeHandler
 * Created by ruiyang.du on 2017/8/4.
 */
public class IdentityIdEncryptTypeHandler extends BaseTypeHandler {


    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Object parameter, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, AESUtil.aesEncrypt((String) parameter));
    }

    @Override
    public Object getNullableResult(ResultSet resultSet, String s) throws SQLException {
//      String readEncryptColumnSwitch = CommonUtil.getNccashierSensitiveInfoConfig(Constant.ENCRYPT_DB_COLUMN_QUERY_SWITCH);
//      注：敏感信息改造已完成，开关强制取on值，不再读取统一配置
        String readEncryptColumnSwitch = "on";
        if ("off".equals(readEncryptColumnSwitch)) {
            String columnValue = resultSet.getString(s);
            return columnValue;
        } else {
            String columnValue = resultSet.getString(s + Constant.ENCRYPT_DATABASE_COLUMU_SUFFIX);
            return AESUtil.aesDecrypt(columnValue);
        }
    }

    @Override
    public Object getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        String columnValue = callableStatement.getString(i);
        return columnValue;
    }

}
