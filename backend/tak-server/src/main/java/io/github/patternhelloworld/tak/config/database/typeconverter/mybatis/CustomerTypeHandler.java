package io.github.patternhelloworld.tak.config.database.typeconverter.mybatis;

import io.github.patternhelloworld.tak.config.database.typeconverter.CustomerType;
import org.apache.ibatis.type.*;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerTypeHandler<E extends Enum<E>> implements TypeHandler<CustomerType> {
    private Class<E> type;

    public CustomerTypeHandler(Class<E> type) {
        this.type = type;
    }

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, CustomerType customerType, JdbcType jdbcType) throws SQLException {
        if (customerType == null) {
            preparedStatement.setNull(i, jdbcType.TYPE_CODE);
        } else {
            preparedStatement.setInt(i, customerType.getValue());
        }
    }

    @Override
    public CustomerType getResult(ResultSet resultSet, String s) throws SQLException {
        int customerTypeValue = resultSet.getInt(s);
        return getCustomerType(customerTypeValue);
    }

    @Override
    public CustomerType getResult(ResultSet resultSet, int i) throws SQLException {
        int customerTypeValue = resultSet.getInt(i);
        return getCustomerType(customerTypeValue);
    }

    @Override
    public CustomerType getResult(CallableStatement callableStatement, int i) throws SQLException {
        int customerTypeValue = callableStatement.getInt(i);
        return getCustomerType(customerTypeValue);
    }

    private CustomerType getCustomerType(int customerTypeInteger) {
        try {
            CustomerType[] enumConstants = (CustomerType[]) type.getEnumConstants();
            for (CustomerType customerType : enumConstants) {
                if (customerType.getValue() == customerTypeInteger) {
                    return customerType;
                }
            }
            return null;
        } catch (Exception exception) {
            throw new TypeException("Can't make enum object '" + type + "'", exception);
        }
    }
}
