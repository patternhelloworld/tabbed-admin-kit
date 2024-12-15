package io.github.patternhelloworld.tak.config.securityimpl.message;

import io.github.patternknife.securityhelper.oauth2.api.config.security.message.DefaultSecurityUserExceptionMessage;
import io.github.patternknife.securityhelper.oauth2.api.config.security.message.ISecurityUserExceptionMessageService;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CustomSecurityMessageServiceImpl implements ISecurityUserExceptionMessageService {

    @Override
    public String getUserMessage(DefaultSecurityUserExceptionMessage defaultSecurityUserExceptionMessage) {
        try {
            CustomSecurityUserExceptionMessage customMessage = CustomSecurityUserExceptionMessage.valueOf(defaultSecurityUserExceptionMessage.name());
            return customMessage.getMessage();
        } catch (IllegalArgumentException e) {
            return defaultSecurityUserExceptionMessage.getMessage();
        }
    }

}
