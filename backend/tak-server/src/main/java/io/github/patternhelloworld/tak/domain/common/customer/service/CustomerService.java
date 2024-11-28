package io.github.patternhelloworld.tak.domain.common.customer.service;

import io.github.patternhelloworld.tak.config.securityimpl.principal.AccessTokenUserInfo;
import io.github.patternhelloworld.tak.domain.common.code.dao.CodeCustomerRepositorySupport;
import io.github.patternhelloworld.tak.domain.common.customer.dao.CustomerRepository;
import io.github.patternhelloworld.tak.domain.common.customer.dao.CustomerRepositorySupport;
import io.github.patternhelloworld.tak.domain.common.customer.dto.CustomerReqDTO;
import io.github.patternhelloworld.tak.domain.common.customer.dto.CustomerCommonDTO;
import io.github.patternhelloworld.tak.domain.common.customer.entity.Customer;
import io.github.patternhelloworld.tak.domain.common.customergroup.dao.CustomerGroupRepositorySupport;
import io.github.patternhelloworld.tak.domain.common.relocatelog.dao.RelocateLogRepositorySupport;
import io.github.patternhelloworld.tak.domain.common.user.dao.UserRepositorySupport;
import io.github.patternhelloworld.tak.domain.common.user.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;


@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepositorySupport customerRepositorySupport;
    private final CustomerRepository customerRepository;

    private final CustomerGroupRepositorySupport customerGroupRepositorySupport;
    private final CodeCustomerRepositorySupport codeCustomerRepositorySupport;

    private final UserRepositorySupport userRepositorySupport;
    private final RelocateLogRepositorySupport relocateLogRepositorySupport;

    public Page<CustomerCommonDTO.One> getCustomerPage(Boolean skipPagination, Integer pageNum, Integer pageSize, String customerSearchFilter, String sorterValueFilter, String dateRangeFilter) throws JsonProcessingException {
        return customerRepositorySupport.findByPageAndFilter(skipPagination, pageNum, pageSize, customerSearchFilter, sorterValueFilter, dateRangeFilter);
    }


    public CustomerCommonDTO.CustomerIdx createCustomer(CustomerCommonDTO.One dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo){

       User user = userRepositorySupport.findByUserId(accessTokenUserInfo.getUsername());

        Customer customer = customerRepository.save(dto.toEntity(customerGroupRepositorySupport.findByIdWithoutThrow(dto.getCustomerGroupIdx()),
                codeCustomerRepositorySupport.findByIdWithoutThrow(dto.getCodeGeneralPositionIdx()),
                codeCustomerRepositorySupport.findByIdWithoutThrow(dto.getCodeGeneralJobIdx()),
                codeCustomerRepositorySupport.findByIdWithoutThrow(dto.getCodeGeneralContactMethodIdx()),
                codeCustomerRepositorySupport.findByIdWithoutThrow(dto.getCodeGeneralPurchaseDecisionFactorIdx()),
                codeCustomerRepositorySupport.findByIdWithoutThrow(dto.getCodeGeneralInterestFieldIdx()), user, user
        ));

        return new CustomerCommonDTO.CustomerIdx(customer.getCustomerIdx());
    }
    @Transactional(value = "commonTransactionManager", rollbackFor=Exception.class)
    public CustomerCommonDTO.One updateCustomer(CustomerCommonDTO.One dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {

        // Customer 엔티티를 가져옵니다.
        Customer customer = customerRepositorySupport.findById(dto.getCustomerIdx());
        customer.updateCustomer(dto,customerGroupRepositorySupport.findByIdWithoutThrow(dto.getCustomerGroupIdx()),
                codeCustomerRepositorySupport.findByIdWithoutThrow(dto.getCodeGeneralPositionIdx()),
                codeCustomerRepositorySupport.findByIdWithoutThrow(dto.getCodeGeneralJobIdx()),
                codeCustomerRepositorySupport.findByIdWithoutThrow(dto.getCodeGeneralContactMethodIdx()),
                codeCustomerRepositorySupport.findByIdWithoutThrow(dto.getCodeGeneralPurchaseDecisionFactorIdx()),
                codeCustomerRepositorySupport.findByIdWithoutThrow(dto.getCodeGeneralInterestFieldIdx()),accessTokenUserInfo.getUsername());

        return dto;
    }

    @Transactional(value = "commonTransactionManager", rollbackFor=Exception.class)
    public CustomerCommonDTO.Updated updateCustomersUserManager(CustomerReqDTO.CustomersUserManager dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {
        Long userIdx = dto.getUserIdx();
        Set<Integer> customerIdxList = dto.getCustomerIdxList();

        List<Object[]> fromUserManagers = customerRepository.findFromUserManagers(customerIdxList);

        // UserManager 업데이트
        int updatedCount = customerRepository.updateCustomersUserManager(userIdx, LocalDateTime.now(), accessTokenUserInfo.getUsername(), customerIdxList);

        int result = relocateLogRepositorySupport.createRelocateLog(userIdx, accessTokenUserInfo.getUsername(), fromUserManagers);

        return new CustomerCommonDTO.Updated(updatedCount);
    }

    @Transactional(value = "commonTransactionManager", rollbackFor=Exception.class)
    public void deleteCustomer(Integer customerIdx, String modifier){
        customerRepositorySupport.deleteOne(customerIdx, modifier);
    }

    @Transactional(value = "commonTransactionManager", rollbackFor=Exception.class)
    public void restoreCustomer(Integer customerIdx, String modifier){
        customerRepositorySupport.restoreOne(customerIdx, modifier);
    }

    @Transactional(value = "commonTransactionManager", rollbackFor=Exception.class)
    public void destroyCustomer(Integer customerIdx){
        customerRepositorySupport.destroyOne(customerIdx);
    }

}
