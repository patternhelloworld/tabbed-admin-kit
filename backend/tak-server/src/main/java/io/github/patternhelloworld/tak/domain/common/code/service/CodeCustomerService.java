package io.github.patternhelloworld.tak.domain.common.code.service;

import io.github.patternhelloworld.common.config.response.error.exception.data.AlreadyExistsException;
import io.github.patternhelloworld.common.config.response.error.exception.data.ResourceNotFoundException;
import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
import io.github.patternhelloworld.tak.config.securityimpl.principal.AccessTokenUserInfo;
import io.github.patternhelloworld.tak.domain.common.code.dao.CodeCustomerRepository;
import io.github.patternhelloworld.tak.domain.common.code.dao.CodeCustomerRepositorySupport;
import io.github.patternhelloworld.tak.domain.common.code.dto.CodeCustomerReqDTO;
import io.github.patternhelloworld.tak.domain.common.code.dto.CodeCustomerResDTO;
import io.github.patternhelloworld.tak.domain.common.code.entity.CodeCustomer;
import io.github.patternhelloworld.tak.domain.common.dealer.dao.DealerRepositorySupport;
import io.github.patternhelloworld.tak.domain.common.dealer.entity.Dealer;
import io.github.patternhelloworld.tak.domain.common.user.dao.UserRepository;
import io.github.patternhelloworld.tak.domain.common.user.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CodeCustomerService {
    private final CodeCustomerRepositorySupport codeCustomerRepositorySupport;
    private final CodeCustomerRepository codeCustomerRepository;
    private final DealerRepositorySupport dealerRepositorySupport;
    private final UserRepository userRepository;

    public Page<CodeCustomerResDTO.One> getCustomerCodePage(Boolean skipPagination, Integer pageNum, Integer pageSize, String codeCustomerSearchFilter, String sorterValueFilter, String dateRangeFilter) throws JsonProcessingException {
        return codeCustomerRepositorySupport.findByPageAndFilter(skipPagination, pageNum, pageSize, codeCustomerSearchFilter, sorterValueFilter, dateRangeFilter);
    }

    public List<CodeCustomer> getCustomerCodeMetas(Integer codeCustomerIdx) {
        CodeCustomer codeCustomer = codeCustomerRepository
                .findByCodeCustomerIdxAndCategoryYnAndDelYn(codeCustomerIdx, YNCode.Y, YNCode.N)
                .orElseThrow(() -> new NoSuchElementException("not found."));

        return codeCustomerRepository.findByParentAndCategoryYnAndDelYnOrderBySortAsc(codeCustomer.getCodeCustomerIdx(), YNCode.N, YNCode.N)
                .orElseGet(Collections::emptyList);
    }

    public CodeCustomerResDTO.Idx createCustomerCode(CodeCustomerReqDTO.CreateUpdateOne dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo){
        User user = userRepository.findById(accessTokenUserInfo.getAdditionalAccessTokenUserInfo().getInfo().getUserIdx()).orElseThrow(() -> new ResourceNotFoundException("User not found."));
        Dealer dealer = dealerRepositorySupport.findById(accessTokenUserInfo.getAdditionalAccessTokenUserInfo().getInfo().getDealerCd());

        Optional<CodeCustomer> optionalCodeCustomer = codeCustomerRepository.findByCategoryCdAndCategoryYnAndDelYn(dto.getCategoryCd(), YNCode.Y, YNCode.N);
        if(optionalCodeCustomer.isPresent()) {
            throw new AlreadyExistsException("중복된 카테고리 코드가 있습니다.");
        }

        return new CodeCustomerResDTO.Idx(codeCustomerRepository.save(dto.toEntity(dealer, user)));
    }

    @Transactional(value = "commonTransactionManager", rollbackFor=Exception.class)
    public CodeCustomerResDTO.Idx updateCustomerCode(Integer codeCustomerIdx, CodeCustomerReqDTO.CreateUpdateOne dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo){
        User user = userRepository.findById(accessTokenUserInfo.getAdditionalAccessTokenUserInfo().getInfo().getUserIdx()).orElseThrow(() -> new ResourceNotFoundException("User not found."));
        CodeCustomer codeCustomer = codeCustomerRepository.findById(codeCustomerIdx).orElseThrow(() -> new ResourceNotFoundException("CodeCustomer for '" + codeCustomerIdx + "' not found."));
        Dealer dealer = dealerRepositorySupport.findById(accessTokenUserInfo.getAdditionalAccessTokenUserInfo().getInfo().getDealerCd());

        Optional<CodeCustomer> optionalCodeCustomer = codeCustomerRepository.findByCategoryCdAndCategoryYnAndDelYn(dto.getCategoryCd(), YNCode.Y, YNCode.N);
        if(optionalCodeCustomer.isPresent()) {
            if(!optionalCodeCustomer.get().getCodeCustomerIdx().equals(codeCustomerIdx)){
                throw new AlreadyExistsException("중복된 카테고리 코드가 있습니다.");
            }
        }

        return codeCustomerRepositorySupport.updateCodeCustomer(codeCustomer, dto, dealer, user);
    }

    @Transactional(value = "commonTransactionManager", rollbackFor=Exception.class)
    public void deleteCodeCustomer(Integer codeCustomerIdx, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo){
        CodeCustomer codeCustomer = codeCustomerRepository.findById(codeCustomerIdx).orElseThrow(() -> new ResourceNotFoundException("CodeCustomer for '" + codeCustomerIdx + "' not found."));

        List<CodeCustomer> childCodeCustomers = Collections.emptyList();
        if(codeCustomer.getCategoryYn() == YNCode.Y) {
            childCodeCustomers = codeCustomerRepository.findByParentAndCategoryYn(codeCustomerIdx, YNCode.N);
        }
        codeCustomerRepositorySupport.deleteOne(codeCustomer, accessTokenUserInfo.getAdditionalAccessTokenUserInfo().getInfo().getUserId(), childCodeCustomers);
    }

    public CodeCustomerResDTO.Idx createMetaCustomerCode(Integer codeCustomerIdx, CodeCustomerReqDTO.MetaCreateUpdateOne dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo){
        User user = userRepository.findById(accessTokenUserInfo.getAdditionalAccessTokenUserInfo().getInfo().getUserIdx()).orElseThrow(() -> new ResourceNotFoundException("User not found."));
        CodeCustomer parent = codeCustomerRepository.findById(codeCustomerIdx).orElseThrow(() -> new ResourceNotFoundException("CodeCustomer for '" + codeCustomerIdx + "' not found."));

        return new CodeCustomerResDTO.Idx(codeCustomerRepository.save(dto.toEntity(parent.getCodeCustomerIdx(), user)));
    }

    @Transactional(value = "commonTransactionManager", rollbackFor=Exception.class)
    public CodeCustomerResDTO.Idx updateMetaCustomerCode(Integer codeCustomerIdx, CodeCustomerReqDTO.MetaCreateUpdateOne dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo){
        User user = userRepository.findById(accessTokenUserInfo.getAdditionalAccessTokenUserInfo().getInfo().getUserIdx()).orElseThrow(() -> new ResourceNotFoundException("User not found."));
        CodeCustomer codeCustomer = codeCustomerRepository.findById(codeCustomerIdx).orElseThrow(() -> new ResourceNotFoundException("CodeCustomer for '" + codeCustomerIdx + "' not found."));

        return codeCustomerRepositorySupport.updateMetaCustomerCode(codeCustomer, dto, user);
    }
}
