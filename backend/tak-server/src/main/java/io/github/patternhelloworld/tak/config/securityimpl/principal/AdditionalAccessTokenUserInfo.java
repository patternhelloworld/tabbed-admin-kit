package io.github.patternhelloworld.tak.config.securityimpl.principal;

import io.github.patternhelloworld.tak.domain.common.user.dto.UserCommonDTO;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class AdditionalAccessTokenUserInfo implements Serializable {

    public enum UserType {
        USER("hq_pms_user");

        private final String value;

        UserType(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private UserType userType;

    private UserCommonDTO.OneWithDeptDealerMenus info;

    private Integer accessTokenRemainingSeconds;

    public AdditionalAccessTokenUserInfo(UserCommonDTO.OneWithDeptDealerMenus oneWithDeptDealerMenus) {

        this.userType = UserType.USER;
        this.info = oneWithDeptDealerMenus;

    }
}

