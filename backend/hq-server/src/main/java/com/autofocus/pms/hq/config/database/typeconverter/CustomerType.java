package com.autofocus.pms.hq.config.database.typeconverter;

import com.autofocus.pms.hq.config.database.typeconverter.mybatis.YNCodeTypeHandler;
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
