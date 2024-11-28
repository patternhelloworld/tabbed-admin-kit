package io.github.patternhelloworld.tak.config.database.typeconverter;

import io.github.patternhelloworld.tak.config.database.typeconverter.mybatis.YNCodeTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.ibatis.type.MappedTypes;

@Getter
@AllArgsConstructor
public enum DealerStockUseType implements BaseEnumCode<Integer> {
    UNDEFINED(0),   // 미정
    PENDING(1),     // 대기
    DISPLAY(2),     // 전시
    TEST_DRIVE(3),  // 시승
    TEST_CAR_SALE(4); // 시승차 판매

    private final Integer value;

    @MappedTypes(DealerStockUseType.class)
    public static class TypeHandler extends YNCodeTypeHandler<DealerStockUseType> {
        public TypeHandler() {
            super(DealerStockUseType.class);
        }
    }
}