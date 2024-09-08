package com.autofocus.pms.hq.domain.common.privacyagree.service;

import com.autofocus.pms.common.config.response.error.exception.data.ResourceNotFoundException;
import com.autofocus.pms.hq.config.securityimpl.principal.AccessTokenUserInfo;
import com.autofocus.pms.hq.domain.common.customer.dao.CustomerRepository;
import com.autofocus.pms.hq.domain.common.customer.entity.Customer;
import com.autofocus.pms.hq.domain.common.privacyagree.dao.PrivacyAgreeRepository;
import com.autofocus.pms.hq.domain.common.privacyagree.dao.PrivacyAgreeRepositorySupport;
import com.autofocus.pms.hq.domain.common.privacyagree.dto.PrivacyAgreeReqDTO;
import com.autofocus.pms.hq.domain.common.privacyagree.dto.PrivacyAgreeResDTO;
import com.autofocus.pms.hq.domain.common.privacyagree.entity.PrivacyAgree;
import com.autofocus.pms.hq.domain.common.user.dao.UserRepository;
import com.autofocus.pms.hq.domain.common.user.entity.User;
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
