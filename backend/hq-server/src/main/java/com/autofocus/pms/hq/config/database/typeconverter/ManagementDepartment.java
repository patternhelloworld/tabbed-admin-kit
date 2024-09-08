package com.autofocus.pms.hq.config.database.typeconverter;

import com.autofocus.pms.hq.config.database.typeconverter.mybatis.ManagementDepartmentTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.ibatis.type.MappedTypes;

@Getter
@AllArgsConstructor
public enum ManagementDepartment implements BaseEnumCode<String> {
    Management("Management"),
    Sales("Sales"),
    Support("Support"),
    None("None");

    private final String value;

    @MappedTypes(ManagementDepartment.class)
    public static class TypeHandler extends ManagementDepartmentTypeHandler<ManagementDepartment> {
        public TypeHandler() {
            super(ManagementDepartment.class);
        }
    }
}