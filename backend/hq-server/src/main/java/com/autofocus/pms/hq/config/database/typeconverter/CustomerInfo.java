package com.autofocus.pms.hq.config.database.typeconverter;


import com.autofocus.pms.hq.config.database.typeconverter.mybatis.CustomerInfoTypeHandler;
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