package com.autofocus.pms.hq.domain.common.customergroup.service;

import com.autofocus.pms.common.config.response.error.exception.data.AlreadyExistsException;
import com.autofocus.pms.common.config.response.error.exception.data.ResourceNotFoundException;
import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.config.securityimpl.principal.AccessTokenUserInfo;
import com.autofocus.pms.hq.domain.common.customergroup.dao.CustomerGroupRepository;
import com.autofocus.pms.hq.domain.common.customergroup.dao.CustomerGroupRepositorySupport;
import com.autofocus.pms.hq.domain.common.customergroup.dto.CustomerGroupReqDTO;
import com.autofocus.pms.hq.domain.common.customergroup.dto.CustomerGroupResDTO;
import com.autofocus.pms.hq.domain.common.customergroup.entity.CustomerGroup;
import com.autofocus.pms.hq.domain.common.dealer.dao.DealerRepositorySupport;
import com.autofocus.pms.hq.domain.common.dealer.entity.Dealer;
import com.autofocus.pms.hq.domain.common.user.dao.UserRepository;
import com.autofocus.pms.hq.domain.common.user.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomerGroupService {
    private final CustomerGroupRepositorySupport customerGroupRepositorySupport;
    private final CustomerGroupRepository customerGroupRepository;
    private final UserRepository userRepository;
    private final DealerRepositorySupport dealerRepositorySupport;

    public Page<CustomerGroupResDTO.One> getCustomerGroupPage(Boolean skipPagination, Integer pageNum, Integer pageSize, String customerGroupSearchFilter, String sorterValueFilter, String dateRangeFilter) throws JsonProcessingException {
        return customerGroupRepositorySupport.findByPageAndFilter(skipPagination, pageNum, pageSize, customerGroupSearchFilter, sorterValueFilter, dateRangeFilter);
    }

    public CustomerGroupResDTO.Idx createCustomerGroup(CustomerGroupReqDTO.CreateUpdateOne dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo){
        User user = userRepository.findById(accessTokenUserInfo.getAdditionalAccessTokenUserInfo().getInfo().getUserIdx()).orElseThrow(() -> new ResourceNotFoundException("User not found."));
        Dealer dealer = dealerRepositorySupport.findById(accessTokenUserInfo.getAdditionalAccessTokenUserInfo().getInfo().getDealerCd());

        Optional<CustomerGroup> optionalCustomerGroup  = customerGroupRepository.findByGroupNmAndDelYn(dto.getGroupNm(), YNCode.N);
        if(optionalCustomerGroup.isPresent()) {
            throw new AlreadyExistsException("중복된 이름의 그룹이 있습니다.");
        }

        return new CustomerGroupResDTO.Idx(customerGroupRepository.save(dto.toEntity(dealer, user)));
    }

    @Transactional(value = "commonTransactionManager", rollbackFor=Exception.class)
    public CustomerGroupResDTO.Idx updateCustomerGroup(Integer customerGroupIdx, CustomerGroupReqDTO.CreateUpdateOne dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo){
        User user = userRepository.findById(accessTokenUserInfo.getAdditionalAccessTokenUserInfo().getInfo().getUserIdx()).orElseThrow(() -> new ResourceNotFoundException("User not found."));

        CustomerGroup customerGroup = customerGroupRepository.findById(customerGroupIdx).orElseThrow(() -> new ResourceNotFoundException("CustomerGroup for '" + customerGroupIdx + "' not found."));

        Optional<CustomerGroup> optionalCustomerGroup  = customerGroupRepository.findByGroupNmAndDelYn(dto.getGroupNm(), YNCode.N);
        if(optionalCustomerGroup.isPresent()) {
            if(!optionalCustomerGroup.get().getCustomerGroupIdx().equals(customerGroupIdx)){
                throw new AlreadyExistsException("중복된 이름의 그룹이 있습니다.");
            }
        }

        return customerGroupRepositorySupport.updateCustomerGroup(customerGroup, dto, user);
    }

    @Transactional(value = "commonTransactionManager", rollbackFor=Exception.class)
    public void deleteCustomerGroup(Integer customerGroupIdx, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo){
        CustomerGroup customerGroup = customerGroupRepository.findById(customerGroupIdx).orElseThrow(() -> new ResourceNotFoundException("CustomerGroup for '" + customerGroupIdx + "' not found."));
        customerGroupRepositorySupport.deleteCustomerGroup(customerGroup, accessTokenUserInfo.getAdditionalAccessTokenUserInfo().getInfo().getUserId());
    }

}
