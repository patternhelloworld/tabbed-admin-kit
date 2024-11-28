package io.github.patternhelloworld.tak.config.database.typeconverter.mybatis;

import io.github.patternhelloworld.tak.config.database.typeconverter.ViewPermission;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ViewPermissionTypeHandler<E extends Enum<E>> implements TypeHandler<ViewPermission> {
    private Class<E> type;

    public ViewPermissionTypeHandler(Class<E> type) {
        this.type = type;
    }

    public void setParameter(PreparedStatement preparedStatement, int i, ViewPermission viewPermission, JdbcType jdbcType) throws
            SQLException {
        preparedStatement.setString(i, viewPermission.getValue());
    }

    @Override
    public ViewPermission getResult(ResultSet resultSet, String s) throws SQLException {
        String viewPermission = resultSet.getString(s);
        return getViewPermission(viewPermission);
    }

    @Override
    public ViewPermission getResult(ResultSet resultSet, int i) throws SQLException {
        String viewPermission = resultSet.getString(i);
        return getViewPermission(viewPermission);
    }

    @Override
    public ViewPermission getResult(CallableStatement callableStatement, int i) throws SQLException {
        String viewPermission = callableStatement.getString(i);
        return getViewPermission(viewPermission);
    }

    private ViewPermission getViewPermission(String viewPermissionStr) {
        try {
            ViewPermission[] enumConstants = (ViewPermission[])type.getEnumConstants();
            for (ViewPermission viewPermission : enumConstants) {
                if (viewPermission.getValue().equals(viewPermissionStr)) {
                    return viewPermission;
                }
            }
            return null;
        } catch (Exception exception) {
            throw new TypeException("Can't make enum object '" + type + "'", exception);
        }
    }
}
