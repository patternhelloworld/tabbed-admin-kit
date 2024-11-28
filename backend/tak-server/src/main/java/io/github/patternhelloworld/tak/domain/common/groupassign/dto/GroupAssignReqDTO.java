package io.github.patternhelloworld.tak.domain.common.groupassign.dto;

import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
import io.github.patternhelloworld.tak.domain.common.customer.entity.Customer;
import io.github.patternhelloworld.tak.domain.common.customergroup.entity.CustomerGroup;
import io.github.patternhelloworld.tak.domain.common.groupassign.entity.GroupAssign;
import io.github.patternhelloworld.tak.domain.common.user.entity.User;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class GroupAssignReqDTO {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class CreateOne {
        public GroupAssign toEntity(Customer customer, CustomerGroup customerGroup, User user) {
            GroupAssign.GroupAssignBuilder builder = GroupAssign.builder()
                    .customer(customer)
                    .customerGroup(customerGroup)
                    .regUserid(user.getUserId())
                    .delYn(YNCode.N);
            return builder.build();
        }
    }
}
