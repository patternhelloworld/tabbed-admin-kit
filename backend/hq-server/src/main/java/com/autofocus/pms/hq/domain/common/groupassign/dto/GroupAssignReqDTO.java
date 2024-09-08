package com.autofocus.pms.hq.domain.common.groupassign.dto;

import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.domain.common.customer.entity.Customer;
import com.autofocus.pms.hq.domain.common.customergroup.entity.CustomerGroup;
import com.autofocus.pms.hq.domain.common.groupassign.entity.GroupAssign;
import com.autofocus.pms.hq.domain.common.user.entity.User;

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
