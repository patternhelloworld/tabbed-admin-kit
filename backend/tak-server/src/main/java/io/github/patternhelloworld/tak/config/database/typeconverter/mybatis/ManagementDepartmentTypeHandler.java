package io.github.patternhelloworld.tak.config.database.typeconverter.mybatis;

import io.github.patternhelloworld.tak.config.database.typeconverter.ManagementDepartment;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ManagementDepartmentTypeHandler<E extends Enum<E>> implements TypeHandler<ManagementDepartment> {
    private Class<E> type;

    public ManagementDepartmentTypeHandler(Class<E> type) {
        this.type = type;
    }

    public void setParameter(PreparedStatement preparedStatement, int i, ManagementDepartment managementDepartment, JdbcType jdbcType) throws
            SQLException {
        preparedStatement.setString(i, managementDepartment.getValue());
    }

    @Override
    public ManagementDepartment getResult(ResultSet resultSet, String s) throws SQLException {
        String managementDepartment = resultSet.getString(s);
        return getManagementDepartment(managementDepartment);
    }

    @Override
    public ManagementDepartment getResult(ResultSet resultSet, int i) throws SQLException {
        String managementDepartment = resultSet.getString(i);
        return getManagementDepartment(managementDepartment);
    }

    @Override
    public ManagementDepartment getResult(CallableStatement callableStatement, int i) throws SQLException {
        String managementDepartment = callableStatement.getString(i);
        return getManagementDepartment(managementDepartment);
    }

    private ManagementDepartment getManagementDepartment(String managementDepartmentStr) {
        try {
            ManagementDepartment[] enumConstants = (ManagementDepartment[])type.getEnumConstants();
            for (ManagementDepartment managementDepartment : enumConstants) {
                if (managementDepartment.getValue().equals(managementDepartmentStr)) {
                    return managementDepartment;
                }
            }
            return null;
        } catch (Exception exception) {
            throw new TypeException("Can't make enum object '" + type + "'", exception);
        }
    }
}
