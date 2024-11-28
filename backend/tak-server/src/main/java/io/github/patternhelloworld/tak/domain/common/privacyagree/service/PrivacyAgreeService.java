package io.github.patternhelloworld.tak.domain.common.privacyagree.service;

import io.github.patternhelloworld.common.config.response.error.exception.data.ResourceNotFoundException;
import io.github.patternhelloworld.tak.config.securityimpl.principal.AccessTokenUserInfo;
import io.github.patternhelloworld.tak.domain.common.customer.dao.CustomerRepository;
import io.github.patternhelloworld.tak.domain.common.customer.entity.Customer;
import io.github.patternhelloworld.tak.domain.common.privacyagree.dao.PrivacyAgreeRepository;
import io.github.patternhelloworld.tak.domain.common.privacyagree.dao.PrivacyAgreeRepositorySupport;
import io.github.patternhelloworld.tak.domain.common.privacyagree.dto.PrivacyAgreeReqDTO;
import io.github.patternhelloworld.tak.domain.common.privacyagree.dto.PrivacyAgreeResDTO;
import io.github.patternhelloworld.tak.domain.common.user.dao.UserRepository;
import io.github.patternhelloworld.tak.domain.common.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrivacyAgreeService {
    private final PrivacyAgreeRepository privacyAgreeRepository;
    private final PrivacyAgreeRepositorySupport privacyAgreeRepositorySupport;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    public PrivacyAgreeResDTO.Idx createPrivacyAgree(PrivacyAgreeReqDTO.CreateOne dto, AccessTokenUserInfo accessTokenUserInfo){
        User user = userRepository.findById(accessTokenUserInfo.getAdditionalAccessTokenUserInfo().getInfo().getUserIdx()).orElseThrow(() -> new ResourceNotFoundException("User not found."));
        Customer customer = customerRepository.findById(dto.getCustomerIdx()).orElse(null);
        return new PrivacyAgreeResDTO.Idx(privacyAgreeRepository.save(dto.toEntity(customer, user)));
    }

    public PrivacyAgreeResDTO.One getOne(Integer customerIdx) {
        return privacyAgreeRepositorySupport.findOneByCustomerIdx(customerIdx);
    }
}
