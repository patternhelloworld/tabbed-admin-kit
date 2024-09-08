package com.autofocus.pms.hq.config.database.typeconverter;

import com.autofocus.pms.hq.config.database.typeconverter.mybatis.CustomerGradeTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.ibatis.type.MappedTypes;

@Getter
@AllArgsConstructor
public enum CustomerGrade implements BaseEnumCode<String> {

    A("A"),
    B("B"),
    C("C"),
    D("D"),
    None("None");

    private final String value;

    @MappedTypes(CustomerGrade.class)
    public static class TypeHandler extends CustomerGradeTypeHandler<CustomerGrade> {
        public TypeHandler() {
            super(CustomerGrade.class);
        }
    }
}
