package io.github.patternhelloworld.tak.config.database.typeconverter.mybatis;

import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class YNCodeTypeHandler<E extends Enum<E>> implements TypeHandler<YNCode> {
    private Class<E> type;

    public YNCodeTypeHandler(Class<E> type) {
        this.type = type;
    }

    public void setParameter(PreparedStatement preparedStatement, int i, YNCode yNCode, JdbcType jdbcType) throws
            SQLException {
        preparedStatement.setString(i, yNCode.getValue());
    }

    @Override
    public YNCode getResult(ResultSet resultSet, String s) throws SQLException {
        String yNCode = resultSet.getString(s);
        return getYNCode(yNCode);
    }

    @Override
    public YNCode getResult(ResultSet resultSet, int i) throws SQLException {
        String yNCode = resultSet.getString(i);
        return getYNCode(yNCode);
    }

    @Override
    public YNCode getResult(CallableStatement callableStatement, int i) throws SQLException {
        String yNCode = callableStatement.getString(i);
        return getYNCode(yNCode);
    }

    private YNCode getYNCode(String yNCodeStr) {
        try {
            YNCode[] enumConstants = (YNCode[])type.getEnumConstants();
            for (YNCode yNCode : enumConstants) {
                if(yNCode.getValue() == null){
                    return YNCode.N;
                }else{
                    if (yNCode.getValue().equals(yNCodeStr)) {
                        return yNCode;
                    }
                }
            }
            return null;
        } catch (Exception exception) {
            throw new TypeException("Can't make enum object '" + type + "'", exception);
        }
    }
}
