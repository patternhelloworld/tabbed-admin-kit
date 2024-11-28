package io.github.patternhelloworld.tak.config.database.typeconverter.mybatis;

import io.github.patternhelloworld.tak.config.database.typeconverter.Gender;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class GenderTypeHandler<E extends Enum<E>> implements TypeHandler<Gender> {
    private final Class<E> type;

    public GenderTypeHandler(Class<E> type) {
        this.type = type;
    }

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, Gender gender, JdbcType jdbcType) throws SQLException {
        preparedStatement.setString(i, gender.getValue());
    }

    @Override
    public Gender getResult(ResultSet resultSet, String s) throws SQLException {
        String gender = resultSet.getString(s);
        return getGender(gender);
    }

    @Override
    public Gender getResult(ResultSet resultSet, int i) throws SQLException {
        String gender = resultSet.getString(i);
        return getGender(gender);
    }

    @Override
    public Gender getResult(CallableStatement callableStatement, int i) throws SQLException {
        String gender = callableStatement.getString(i);
        return getGender(gender);
    }

    private Gender getGender(String genderStr) {
        try {
            Gender[] enumConstants = (Gender[]) type.getEnumConstants();
            for (Gender gender : enumConstants) {
                if (gender.getValue().equals(genderStr)) {
                    return gender;
                }
            }
            return null;
        } catch (Exception exception) {
            throw new TypeException("Can't make enum object '" + type + "'", exception);
        }
    }
}
