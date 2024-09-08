package com.autofocus.pms.hq.config.database.typeconverter;

import com.autofocus.pms.hq.config.database.typeconverter.mybatis.NationalityTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.ibatis.type.MappedTypes;

@Getter
@AllArgsConstructor
public enum Nationality implements BaseEnumCode<String> {

    Domestic("Domestic"),
    Foreigner("Foreigner"),
    None("None");

    private final String value;

    @MappedTypes(Nationality.class)
    public static class TypeHandler extends NationalityTypeHandler<Nationality> {
        public TypeHandler() {
            super(Nationality.class);
        }
    }
}
