package io.github.patternhelloworld.tak.config.database.typeconverter;


import io.github.patternhelloworld.tak.config.database.typeconverter.mybatis.CustomerInfoTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.ibatis.type.MappedTypes;

@Getter
@AllArgsConstructor
public enum CustomerInfo implements BaseEnumCode<Integer> {
    NONE(0),
    CONTRACT(1),
    POTENTIAL(2);

    private final Integer value;

    @MappedTypes(CustomerInfo.class)
    public static class TypeHandler extends CustomerInfoTypeHandler<CustomerInfo> {
        public TypeHandler() {
            super(CustomerInfo.class);
        }
    }
}