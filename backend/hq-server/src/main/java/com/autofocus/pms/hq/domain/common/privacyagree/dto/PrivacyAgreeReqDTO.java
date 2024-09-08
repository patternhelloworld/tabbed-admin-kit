package com.autofocus.pms.hq.domain.common.privacyagree.dto;

import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.domain.common.customer.entity.Customer;
import com.autofocus.pms.hq.domain.common.privacyagree.entity.PrivacyAgree;
import com.autofocus.pms.hq.domain.common.user.entity.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

public class PrivacyAgreeReqDTO {
    @Getter
    @Setter
    public static class CreateOne {
        private Integer customerIdx;
        private YNCode isAgree;
        private Date agreeDate;
        private String fname;
        private String sname;

        public PrivacyAgree toEntity(Customer customer, User user) {
            PrivacyAgree.PrivacyAgreeBuilder builder = PrivacyAgree.builder()
                    .customer(customer)
                    .isAgree(this.isAgree)
                    .fname(this.fname)
                    .sname(this.sname)
                    .agreeDate(this.agreeDate)
                    .regUserId(user.getUserId())
                    .delYn(YNCode.N);

            return builder.build();
        }
    }
}
