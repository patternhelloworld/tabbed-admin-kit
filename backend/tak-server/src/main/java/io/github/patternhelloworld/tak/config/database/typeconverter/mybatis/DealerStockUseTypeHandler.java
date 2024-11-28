package io.github.patternhelloworld.tak.config.database.typeconverter.mybatis;

import io.github.patternhelloworld.tak.config.database.typeconverter.DealerStockUseType;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DealerStockUseTypeHandler<E extends Enum<E>> implements TypeHandler<DealerStockUseType> {
    private Class<E> type;

    public DealerStockUseTypeHandler(Class<E> type) {
        this.type = type;
    }

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, DealerStockUseType dealerStockUseType, JdbcType jdbcType) throws SQLException {
        if (dealerStockUseType == null) {
            preparedStatement.setNull(i, jdbcType.TYPE_CODE);
        } else {
            preparedStatement.setInt(i, dealerStockUseType.getValue());
        }
    }

    @Override
    public DealerStockUseType getResult(ResultSet resultSet, String s) throws SQLException {
        int dealerStockUseTypeValue = resultSet.getInt(s);
        return getDealerStockUseType(dealerStockUseTypeValue);
    }

    @Override
    public DealerStockUseType getResult(ResultSet resultSet, int i) throws SQLException {
        int dealerStockUseTypeValue = resultSet.getInt(i);
        return getDealerStockUseType(dealerStockUseTypeValue);
    }

    @Override
    public DealerStockUseType getResult(CallableStatement callableStatement, int i) throws SQLException {
        int dealerStockUseTypeValue = callableStatement.getInt(i);
        return getDealerStockUseType(dealerStockUseTypeValue);
    }

    private DealerStockUseType getDealerStockUseType(int dealerStockUseTypeInteger) {
        try {
            DealerStockUseType[] enumConstants = (DealerStockUseType[]) type.getEnumConstants();
            for (DealerStockUseType dealerStockUseType : enumConstants) {
                if (dealerStockUseType.getValue() == dealerStockUseTypeInteger) {
                    return dealerStockUseType;
                }
            }
            return null;
        } catch (Exception exception) {
            throw new TypeException("Can't make enum object '" + type + "'", exception);
        }
    }
}