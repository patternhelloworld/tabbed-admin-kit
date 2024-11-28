package io.github.patternhelloworld.tak.config.database.typeconverter.mybatis;

import io.github.patternhelloworld.tak.config.database.typeconverter.CustomerInfo;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(CustomerInfo.class)
public class CustomerInfoTypeHandler<E extends Enum<E>> implements TypeHandler<CustomerInfo> {
    private Class<E> type;

    public CustomerInfoTypeHandler(Class<E> type) {
        this.type = type;
    }

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, CustomerInfo customerInfo, JdbcType jdbcType) throws SQLException {
        if (customerInfo == null) {
            preparedStatement.setNull(i, jdbcType.TYPE_CODE);
        } else {
            preparedStatement.setInt(i, customerInfo.getValue());
        }
    }

    @Override
    public CustomerInfo getResult(ResultSet resultSet, String s) throws SQLException {
        int customerInfoValue = resultSet.getInt(s);
        return getCustomerInfo(customerInfoValue);
    }

    @Override
    public CustomerInfo getResult(ResultSet resultSet, int i) throws SQLException {
        int customerInfoValue = resultSet.getInt(i);
        return getCustomerInfo(customerInfoValue);
    }

    @Override
    public CustomerInfo getResult(CallableStatement callableStatement, int i) throws SQLException {
        int customerInfoValue = callableStatement.getInt(i);
        return getCustomerInfo(customerInfoValue);
    }

    private CustomerInfo getCustomerInfo(int customerInfoInteger) {
        try {
            CustomerInfo[] enumConstants = (CustomerInfo[]) type.getEnumConstants();
            for (CustomerInfo customerInfo : enumConstants) {
                if (customerInfo.getValue().equals(customerInfoInteger)) {
                    return customerInfo;
                }
            }
            return null;
        } catch (Exception exception) {
            throw new TypeException("Can't make enum object '" + type + "'", exception);
        }
    }
}
