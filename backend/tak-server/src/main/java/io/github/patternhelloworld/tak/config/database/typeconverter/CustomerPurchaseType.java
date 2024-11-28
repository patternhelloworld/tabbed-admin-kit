package io.github.patternhelloworld.tak.config.database.typeconverter;

import io.github.patternhelloworld.tak.config.database.typeconverter.mybatis.CustomerPurchaseTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.ibatis.type.MappedTypes;

@Getter
@AllArgsConstructor
public enum CustomerPurchaseType implements BaseEnumCode<Integer> {
    NONE(0),
    NEW_PURCHASE(1),
    REPEAT_PURCHASE_2(2),
    REPEAT_PURCHASE_3_OR_MORE(3),
    USED_PURCHASE(4),
    DEMO_PURCHASE(5);

    private final Integer value;

    @MappedTypes(CustomerPurchaseType.class)
    public static class TypeHandler extends CustomerPurchaseTypeHandler<CustomerPurchaseType> {
        public TypeHandler() {
            super(CustomerPurchaseType.class);
        }
    }
}
