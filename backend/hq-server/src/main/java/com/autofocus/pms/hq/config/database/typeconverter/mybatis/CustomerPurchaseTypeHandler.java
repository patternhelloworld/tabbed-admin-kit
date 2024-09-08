package com.autofocus.pms.hq.config.database.typeconverter.mybatis;

import com.autofocus.pms.hq.config.database.typeconverter.CustomerPurchaseType;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.MappedTypes;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@MappedTypes(CustomerPurchaseType.class)
public class CustomerPurchaseTypeHandler<E extends Enum<E>> implements TypeHandler<CustomerPurchaseType> {
    private Class<E> type;

    public CustomerPurchaseTypeHandler(Class<E> type) {
        this.type = type;
    }

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, CustomerPurchaseType customerPurchaseType, JdbcType jdbcType) throws SQLException {
        if (customerPurchaseType == null) {
            preparedStatement.setNull(i, jdbcType.TYPE_CODE);
        } else {
            preparedStatement.setInt(i, customerPurchaseType.getValue());
        }
    }

    @Override
    public CustomerPurchaseType getResult(ResultSet resultSet, String s) throws SQLException {
        int customerPurchaseTypeValue = resultSet.getInt(s);
        return getCustomerPurchaseType(customerPurchaseTypeValue);
    }

    @Override
    public CustomerPurchaseType getResult(ResultSet resultSet, int i) throws SQLException {
        int customerPurchaseTypeValue = resultSet.getInt(i);
        return getCustomerPurchaseType(customerPurchaseTypeValue);
    }

    @Override
    public CustomerPurchaseType getResult(CallableStatement callableStatement, int i) throws SQLException {
        int customerPurchaseTypeValue = callableStatement.getInt(i);
        return getCustomerPurchaseType(customerPurchaseTypeValue);
    }

    private CustomerPurchaseType getCustomerPurchaseType(int customerPurchaseTypeInteger) {
        try {
            CustomerPurchaseType[] enumConstants = (CustomerPurchaseType[]) type.getEnumConstants();
            for (CustomerPurchaseType customerPurchaseType : enumConstants) {
                if (customerPurchaseType.getValue().equals(customerPurchaseTypeInteger)) {
                    return customerPurchaseType;
                }
            }
            return null;
        } catch (Exception exception) {
            throw new TypeException("Can't make enum object '" + type + "'", exception);
        }
    }
}
