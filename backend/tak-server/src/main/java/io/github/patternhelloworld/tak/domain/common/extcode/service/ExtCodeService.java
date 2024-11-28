package io.github.patternhelloworld.tak.domain.common.extcode.service;

import io.github.patternhelloworld.common.config.response.error.exception.data.AlreadyExistsException;
import io.github.patternhelloworld.common.config.response.error.exception.data.ResourceNotFoundException;
import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
import io.github.patternhelloworld.tak.config.securityimpl.principal.AccessTokenUserInfo;
import io.github.patternhelloworld.tak.domain.common.extcode.dao.ExtCodeRepository;
import io.github.patternhelloworld.tak.domain.common.extcode.dao.ExtCodeRepositorySupport;
import io.github.patternhelloworld.tak.domain.common.extcode.dto.ExtCodeReqDTO;
import io.github.patternhelloworld.tak.domain.common.extcode.dto.ExtCodeResDTO;
import io.github.patternhelloworld.tak.domain.common.extcode.entity.ExtCode;
import io.github.patternhelloworld.tak.domain.common.user.dao.UserRepository;
import io.github.patternhelloworld.tak.domain.common.user.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class ExtCodeService {
    private final ExtCodeRepositorySupport extCodeRepositorySupport;
    private final ExtCodeRepository extCodeRepository;
    private final UserRepository userRepository;

    public Page<ExtCodeResDTO.One> getExtCodePage(Boolean skipPagination, Integer pageNum, Integer pageSize, String extCodeSearchFilter, String sorterValueFilter, String dateRangeFilter) throws JsonProcessingException {
        return extCodeRepositorySupport.findByPageAndFilter(skipPagination, pageNum, pageSize, extCodeSearchFilter, sorterValueFilter, dateRangeFilter);
    }


    public ExtCodeResDTO.Idx createExtCode(ExtCodeReqDTO.CreateUpdateOne dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo){

        User user = userRepository.findById(accessTokenUserInfo.getAdditionalAccessTokenUserInfo().getInfo().getUserIdx()).orElseThrow(() -> new ResourceNotFoundException("User not found."));

        Optional<ExtCode> optionalExtCode  = extCodeRepository.findByCodeAndDelYn(dto.getCode(), YNCode.N);
        if(optionalExtCode.isPresent()) {
            throw new AlreadyExistsException("중복된 이름의 코드가 있습니다.");
        }

        return new ExtCodeResDTO.Idx(extCodeRepository.save(dto.toEntity(user)));
    }

    @Transactional(value = "commonTransactionManager", rollbackFor=Exception.class)
    public ExtCodeResDTO.Idx updateExtCode(Integer extColorCodeIdx, ExtCodeReqDTO.CreateUpdateOne dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo){
        User user = userRepository.findById(accessTokenUserInfo.getAdditionalAccessTokenUserInfo().getInfo().getUserIdx()).orElseThrow(() -> new ResourceNotFoundException("User not found."));
        ExtCode extCode = extCodeRepository.findById(extColorCodeIdx).orElseThrow(() -> new ResourceNotFoundException("ExtCode for '" + extColorCodeIdx +"' not found."));

        Optional<ExtCode> optionalExtCode  = extCodeRepository.findByCodeAndDelYn(dto.getCode(), YNCode.N);
        if(optionalExtCode.isPresent()) {
            if(!optionalExtCode.get().getExtColorCodeIdx().equals(extColorCodeIdx)){
                throw new AlreadyExistsException("중복된 이름의 코드가 있습니다.");
            }
        }

        return extCodeRepositorySupport.updateExtCode(extCode, dto, user);
    }

    @Transactional(value = "commonTransactionManager", rollbackFor=Exception.class)
    public void deleteExtCode(Integer extColorCodeIdx, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo){
        ExtCode extCode = extCodeRepository.findById(extColorCodeIdx).orElseThrow(() -> new ResourceNotFoundException("ExtCode for '" + extColorCodeIdx + "' not found."));
        extCodeRepositorySupport.deleteExtCode(extCode, accessTokenUserInfo.getAdditionalAccessTokenUserInfo().getInfo().getUserId());
    }
}
