package io.github.patternhelloworld.tak.domain.common.customer.dto;

import io.github.patternhelloworld.tak.config.database.typeconverter.CustomerInfo;
import io.github.patternhelloworld.tak.config.database.typeconverter.CustomerType;
import io.github.patternhelloworld.tak.config.database.typeconverter.Gender;
import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
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
