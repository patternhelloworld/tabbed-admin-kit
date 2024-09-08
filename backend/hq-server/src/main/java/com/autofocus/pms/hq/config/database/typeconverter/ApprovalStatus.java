package com.autofocus.pms.hq.config.database.typeconverter;

import com.autofocus.pms.hq.config.database.typeconverter.mybatis.YNCodeTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.ibatis.type.MappedTypes;

@Getter
@AllArgsConstructor
public enum ApprovalStatus implements BaseEnumCode<Integer> {
    PENDING(0),   // 대기
    APPROVED(1),  // 승인
    REJECTED(2);  // 반려

    private final Integer value;

    @MappedTypes(ApprovalStatus.class)
    public static class TypeHandler extends YNCodeTypeHandler<ApprovalStatus> {
        public TypeHandler() {
            super(ApprovalStatus.class);
        }
    }
}
