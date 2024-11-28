package io.github.patternhelloworld.tak.config.database.typeconverter.mybatis;

import io.github.patternhelloworld.tak.config.database.typeconverter.PurchasePlan;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PurchasePlanTypeHandler<E extends Enum<E>> implements TypeHandler<PurchasePlan> {
    private final Class<E> type;

    public PurchasePlanTypeHandler(Class<E> type) {
        this.type = type;
    }

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, PurchasePlan purchasePlan, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, purchasePlan.getValue());
    }

    @Override
    public PurchasePlan getResult(ResultSet resultSet, String s) throws SQLException {
        String purchasePlan = resultSet.getString(s);
        return getPurchasePlan(purchasePlan);
    }

    @Override
    public PurchasePlan getResult(ResultSet resultSet, int i) throws SQLException {
        String purchasePlan = resultSet.getString(i);
        return getPurchasePlan(purchasePlan);
    }

    @Override
    public PurchasePlan getResult(CallableStatement callableStatement, int i) throws SQLException {
        String purchasePlan = callableStatement.getString(i);
        return getPurchasePlan(purchasePlan);
    }

    private PurchasePlan getPurchasePlan(String purchasePlanStr) {
        try {
            PurchasePlan[] enumConstants = (PurchasePlan[]) type.getEnumConstants();
            for (PurchasePlan purchasePlan : enumConstants) {
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