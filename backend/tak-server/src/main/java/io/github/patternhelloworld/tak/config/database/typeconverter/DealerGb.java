package io.github.patternhelloworld.tak.config.database.typeconverter;

import io.github.patternhelloworld.tak.config.database.typeconverter.mybatis.ManagementDepartmentTypeHandler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.ibatis.type.MappedTypes;

@Getter
@AllArgsConstructor
public enum DealerGb implements BaseEnumCode<String> {
    D("D"),
    P("P"),
    S("S"),
    A("A"),
    C("C"),
    H("H");

    private final String value;

    @MappedTypes(DealerGb.class)
    public static class TypeHandler extends ManagementDepartmentTypeHandler<DealerGb> {
        public TypeHandler() {
            super(DealerGb.class);
        }
    }
}