package io.github.patternhelloworld.tak.config.database.typeconverter;

import io.github.patternhelloworld.tak.config.database.typeconverter.mybatis.PurchasePlanTypeHandler;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.ibatis.type.MappedTypes;

@Getter
@AllArgsConstructor
public enum PurchasePlan implements BaseEnumCode<String> {

    IMMEDIATE("Immediate"),
    ONE_MONTH("1M"),
    THREE_MONTHS("3M"),
    SIX_MONTHS("6M"),
    TWELVE_MONTHS("12M"),
    Undecided("Undecided");

    private final String value;

    @JsonValue
    public String getValue() {
        return value;
    }

    @MappedTypes(PurchasePlan.class)
    public static class TypeHandler extends PurchasePlanTypeHandler<PurchasePlan> {
        public TypeHandler() {
            super(PurchasePlan.class);
        }
    }
}