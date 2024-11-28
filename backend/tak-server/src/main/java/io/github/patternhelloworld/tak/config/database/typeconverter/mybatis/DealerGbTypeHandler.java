package io.github.patternhelloworld.tak.config.database.typeconverter.mybatis;

import io.github.patternhelloworld.tak.config.database.typeconverter.DealerGb;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DealerGbTypeHandler<E extends Enum<E>> implements TypeHandler<DealerGb> {
    private Class<E> type;

    public DealerGbTypeHandler(Class<E> type) {
        this.type = type;
    }

    public void setParameter(PreparedStatement preparedStatement, int i, DealerGb dealerGb, JdbcType jdbcType) throws
            SQLException {
        preparedStatement.setString(i, dealerGb.getValue());
    }

    @Override
    public DealerGb getResult(ResultSet resultSet, String s) throws SQLException {
        String dealerGb = resultSet.getString(s);
        return getDealerGb(dealerGb);
    }

    @Override
    public DealerGb getResult(ResultSet resultSet, int i) throws SQLException {
        String dealerGb = resultSet.getString(i);
        return getDealerGb(dealerGb);
    }

    @Override
    public DealerGb getResult(CallableStatement callableStatement, int i) throws SQLException {
        String dealerGb = callableStatement.getString(i);
        return getDealerGb(dealerGb);
    }

    private DealerGb getDealerGb(String dealerGbStr) {
        try {
            DealerGb[] enumConstants = (DealerGb[])type.getEnumConstants();
            for (DealerGb dealerGb : enumConstants) {
                if (dealerGb.getValue().equals(dealerGbStr)) {
                    return dealerGb;
                }
            }
            return null;
        } catch (Exception exception) {
            throw new TypeException("Can't make enum object '" + type + "'", exception);
        }
    }
}
