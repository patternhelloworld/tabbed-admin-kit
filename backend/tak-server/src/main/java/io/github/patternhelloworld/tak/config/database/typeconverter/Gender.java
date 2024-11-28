package io.github.patternhelloworld.tak.config.database.typeconverter;

import io.github.patternhelloworld.tak.config.database.typeconverter.mybatis.GenderTypeHandler;
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
