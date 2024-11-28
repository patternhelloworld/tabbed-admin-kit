package io.github.patternhelloworld.tak.config.database.typeconverter.mybatis;

import io.github.patternhelloworld.tak.config.database.typeconverter.CustomerGrade;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerGradeTypeHandler<E extends Enum<E>> implements TypeHandler<CustomerGrade> {
    private final Class<E> type;

    public CustomerGradeTypeHandler(Class<E> type) {
        this.type = type;
    }

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, CustomerGrade customerGrade, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, customerGrade.getValue());
    }

    @Override
    public CustomerGrade getResult(ResultSet resultSet, String s) throws SQLException {
        String customerGrade = resultSet.getString(s);
        return getCustomerGrade(customerGrade);
    }

    @Override
    public CustomerGrade getResult(ResultSet resultSet, int i) throws SQLException {
        String customerGrade = resultSet.getString(i);
        return getCustomerGrade(customerGrade);
    }

    @Override
    public CustomerGrade getResult(CallableStatement callableStatement, int i) throws SQLException {
        String customerGrade = callableStatement.getString(i);
        return getCustomerGrade(customerGrade);
    }

    private CustomerGrade getCustomerGrade(String customerGradeStr) {
        try {
            CustomerGrade[] enumConstants = (CustomerGrade[]) type.getEnumConstants();
            for (CustomerGrade customerGrade : enumConstants) {
                if (customerGrade.getValue().equals(customerGradeStr)) {
                    return customerGrade;
                }
            }
            return null;
        } catch (Exception exception) {
            throw new TypeException("Can't make enum object '" + type + "'", exception);
        }
    }
}
