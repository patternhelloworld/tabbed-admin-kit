package com.autofocus.pms.hq.config.database.typeconverter;

import com.autofocus.pms.hq.config.database.typeconverter.mybatis.ViewPermissionTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.ibatis.type.MappedTypes;

@Getter
@AllArgsConstructor
public enum ViewPermission implements BaseEnumCode<String> {
    All("All"),
    Dept("Dept"),
    None("None");

    private final String value;

    @MappedTypes(ViewPermission.class)
    public static class TypeHandler extends ViewPermissionTypeHandler<ViewPermission> {
        public TypeHandler() {
            super(ViewPermission.class);
        }
    }
}