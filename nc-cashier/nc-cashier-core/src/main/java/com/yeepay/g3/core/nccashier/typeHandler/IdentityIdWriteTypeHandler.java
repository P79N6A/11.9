package com.yeepay.g3.core.nccashier.typeHandler;

import com.yeepay.g3.core.nccashier.utils.CommonUtil;
import com.yeepay.g3.facade.nccashier.constant.Constant;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 敏感信息identityId加密typeHandler
 * <p>注：用于判断旧字段是否写入
 * Created by ruiyang.du on 2017/8/9.
 */
public class IdentityIdWriteTypeHandler extends BaseTypeHandler {

    /**
     * @param preparedStatement
     * @param i
     * @param o
     * @param jdbcType
     * @throws SQLException
     */
    @Override
    public void setNonNullParameter(PreparedStatement preparedStatement, int i, Object o, JdbcType jdbcType) throws SQLException {
//      String writeOldColumnSwitch = CommonUtil.getNccashierSensitiveInfoConfig(Constant.ENCRYPT_DB_COLUMN_WRITE_SWITCH);
//      注：敏感信息改造已完成，开关强制取on值，不再读取统一配置
        String writeOldColumnSwitch = "on";
        if ("off".equals(writeOldColumnSwitch) && o!=null) {
            preparedStatement.setString(i, o.toString());
        } else {
            //开关值非off，表明不再往明文字段写数据
            preparedStatement.setString(i, "");
        }
    }

    @Override
    @Deprecated
    public Object getNullableResult(ResultSet resultSet, String s) throws SQLException {
        return null;
    }

    @Override
    @Deprecated
    public Object getNullableResult(CallableStatement callableStatement, int i) throws SQLException {
        return null;
    }
}
