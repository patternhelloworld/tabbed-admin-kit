package com.autofocus.pms.hq.domain.common.customer.dto;

import com.autofocus.pms.hq.config.database.typeconverter.CustomerInfo;
import com.autofocus.pms.hq.config.database.typeconverter.CustomerType;
import com.autofocus.pms.hq.config.database.typeconverter.Gender;
import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerSearchFilter {
    private String customerName;

    private String regUserId;

    private String deptWithUser;

    private YNCode personalDataConsent;

    private CustomerType customerType;
    private CustomerInfo customerInfo;

    private Integer codeGeneralContactMethodIdx;

    private String hp;
    private String tel;
    private Gender gender;

    private YNCode userOutYn;

    private Integer customerIdx;
}
