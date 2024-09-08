package com.autofocus.pms.hq.config.database.typeconverter.mybatis;

import com.autofocus.pms.hq.config.database.typeconverter.ApprovalStatus;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeException;
import org.apache.ibatis.type.TypeHandler;

import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

// MyBatis Handler for ApprovalStatus Enum
public class ApprovalStatusHandler<E extends Enum<E>> implements TypeHandler<ApprovalStatus> {
    private Class<E> type;

    public ApprovalStatusHandler(Class<E> type) {
        this.type = type;
    }

    @Override
    public void setParameter(PreparedStatement preparedStatement, int i, ApprovalStatus approvalStatus, JdbcType jdbcType) throws SQLException {
        if (approvalStatus == null) {
            preparedStatement.setNull(i, jdbcType.TYPE_CODE);
        } else {
            preparedStatement.setInt(i, approvalStatus.getValue());
        }
    }

    @Override
    public ApprovalStatus getResult(ResultSet resultSet, String s) throws SQLException {
        int approvalStatusValue = resultSet.getInt(s);
        return getApprovalStatus(approvalStatusValue);
    }

    @Override
    public ApprovalStatus getResult(ResultSet resultSet, int i) throws SQLException {
        int approvalStatusValue = resultSet.getInt(i);
        return getApprovalStatus(approvalStatusValue);
    }

    @Override
    public ApprovalStatus getResult(CallableStatement callableStatement, int i) throws SQLException {
        int approvalStatusValue = callableStatement.getInt(i);
        return getApprovalStatus(approvalStatusValue);
    }

    private ApprovalStatus getApprovalStatus(int approvalStatusValue) {
        try {
            ApprovalStatus[] enumConstants = (ApprovalStatus[]) type.getEnumConstants();
            for (ApprovalStatus approvalStatus : enumConstants) {
                if (approvalStatus.getValue() == approvalStatusValue) {
                    return approvalStatus;
                }
            }
            return null;
        } catch (Exception exception) {
            throw new TypeException("Can't make enum object '" + type + "'", exception);
        }
    }
}