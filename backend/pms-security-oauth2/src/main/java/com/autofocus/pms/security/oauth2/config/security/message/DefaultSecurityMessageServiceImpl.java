package com.autofocus.pms.security.oauth2.config.security.message;


public class DefaultSecurityMessageServiceImpl implements ISecurityUserExceptionMessageService {

    @Override
    public String getUserMessage(DefaultSecurityUserExceptionMessage defaultSecurityUserExceptionMessage) {
        return defaultSecurityUserExceptionMessage.getMessage();
    }

}
