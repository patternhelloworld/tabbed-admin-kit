package io.github.patternhelloworld.tak.config.database.typeconverter;

import io.github.patternhelloworld.tak.config.database.typeconverter.mybatis.YNCodeTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.ibatis.type.MappedTypes;

@Getter
@AllArgsConstructor
public enum CustomerType implements BaseEnumCode<Integer> {
    NONE(0),
    PERSONAL(1),
    CORPORATE(2);

    private final Integer value;

    @MappedTypes(CustomerType.class)
    public static class TypeHandler extends YNCodeTypeHandler<CustomerType> {
        public TypeHandler() {
            super(CustomerType.class);
        }
    }

}
