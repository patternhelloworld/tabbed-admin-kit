package com.autofocus.pms.hq.domain.common.customer.dto;

import lombok.Getter;

import java.util.Set;

public class CustomerReqDTO {

    @Getter
    public static class CustomersUserManager {

        private Set<Integer> customerIdxList;
        private Long userIdx;

    }
}
