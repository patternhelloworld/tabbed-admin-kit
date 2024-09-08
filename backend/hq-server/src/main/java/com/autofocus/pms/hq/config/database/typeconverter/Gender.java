package com.autofocus.pms.hq.config.database.typeconverter;

import com.autofocus.pms.hq.config.database.typeconverter.mybatis.GenderTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.ibatis.type.MappedTypes;

@Getter
@AllArgsConstructor
public enum Gender implements BaseEnumCode<String> {

    Male("Male"),
    Female("Female"),
    None("None");

    private final String value;

    @MappedTypes(Gender.class)
    public static class TypeHandler extends GenderTypeHandler<Gender> {
        public TypeHandler() {
            super(Gender.class);
        }
    }
}
