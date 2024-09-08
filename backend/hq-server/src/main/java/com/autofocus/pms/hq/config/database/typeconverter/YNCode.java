package com.autofocus.pms.hq.config.database.typeconverter;

import com.autofocus.pms.hq.config.database.typeconverter.mybatis.YNCodeTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.ibatis.type.MappedTypes;

@Getter
@AllArgsConstructor
public enum YNCode implements BaseEnumCode<String> {

    Y("Y"),
    N("N");

    private final String value;

    @MappedTypes(YNCode.class)
    public static class TypeHandler extends YNCodeTypeHandler<YNCode> {
        public TypeHandler() {
            super(YNCode.class);
        }
    }
}