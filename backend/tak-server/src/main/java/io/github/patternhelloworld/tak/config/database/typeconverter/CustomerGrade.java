package io.github.patternhelloworld.tak.config.database.typeconverter;

import io.github.patternhelloworld.tak.config.database.typeconverter.mybatis.CustomerGradeTypeHandler;
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
