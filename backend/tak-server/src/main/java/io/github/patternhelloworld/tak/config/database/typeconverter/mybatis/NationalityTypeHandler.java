package io.github.patternhelloworld.tak.config.database.typeconverter.mybatis;

import io.github.patternhelloworld.tak.config.database.typeconverter.Nationality;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NationalityTypeHandler<E extends Enum<E>> implements TypeHandler<Nationality> {
    private final Class<E> type;

    public NationalityTypeHandler(Class<E> type) {
        this.type = type;
    }

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, Nationality purchasePlan, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, purchasePlan.getValue());
    }

    @Override
    public Nationality getResult(ResultSet resultSet, String s) throws SQLException {
        String purchasePlan = resultSet.getString(s);
        return getNationality(purchasePlan);
    }

    @Override
    public Nationality getResult(ResultSet resultSet, int i) throws SQLException {
        String purchasePlan = resultSet.getString(i);
        return getNationality(purchasePlan);
    }

    @Override
    public Nationality getResult(CallableStatement callableStatement, int i) throws SQLException {
        String purchasePlan = callableStatement.getString(i);
        return getNationality(purchasePlan);
    }

    private Nationality getNationality(String purchasePlanStr) {
        try {
            Nationality[] enumConstants = (Nationality[]) type.getEnumConstants();
            for (Nationality purchasePlan : enumConstants) {
                if (purchasePlan.getValue().equals(purchasePlanStr)) {
                    return purchasePlan;
                }
            }
            return null;
        } catch (Exception exception) {
            throw new TypeException("Can't make enum object '" + type + "'", exception);
        }
    }
}
