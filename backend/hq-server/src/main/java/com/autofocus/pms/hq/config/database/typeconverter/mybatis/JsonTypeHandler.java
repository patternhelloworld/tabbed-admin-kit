package com.autofocus.pms.hq.config.database.typeconverter.mybatis;
import com.autofocus.pms.hq.domain.common.user.dto.UserCommonDTO;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;

import java.sql.*;
import java.util.List;

public class JsonTypeHandler extends BaseTypeHandler<List<UserCommonDTO.OneWithDeptDealerMenus.Permission>> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, List<UserCommonDTO.OneWithDeptDealerMenus.Permission> parameter, JdbcType jdbcType) throws SQLException {
        try {
            ps.setString(i, objectMapper.writeValueAsString(parameter));
        } catch (Exception e) {
            throw new SQLException("Failed to convert object to JSON string.", e);
        }
    }

    @Override
    public List<UserCommonDTO.OneWithDeptDealerMenus.Permission> getNullableResult(ResultSet rs, String columnName) throws SQLException {
        String json = rs.getString(columnName);
        return jsonToList(json);
    }

    @Override
    public List<UserCommonDTO.OneWithDeptDealerMenus.Permission> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        String json = rs.getString(columnIndex);
        return jsonToList(json);
    }

    @Override
    public List<UserCommonDTO.OneWithDeptDealerMenus.Permission> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        String json = cs.getString(columnIndex);
        return jsonToList(json);
    }

    private List<UserCommonDTO.OneWithDeptDealerMenus.Permission> jsonToList(String json) throws SQLException {
        try {
            if (json != null) {
                return objectMapper.readValue(json, new TypeReference<List<UserCommonDTO.OneWithDeptDealerMenus.Permission>>() {});
            }
            return null;
        } catch (Exception e) {
            throw new SQLException("Failed to convert JSON string to list.", e);
        }
    }
}
