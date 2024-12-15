package io.github.patternhelloworld.tak.domain.common.user.service;


import io.github.patternhelloworld.common.util.CommonConstant;
import io.github.patternhelloworld.tak.config.securityimpl.principal.AccessTokenUserInfo;
import io.github.patternhelloworld.tak.config.securityimpl.principal.AdditionalAccessTokenUserInfo;
import io.github.patternhelloworld.tak.domain.common.dealer.dao.DealerRepositorySupport;
import io.github.patternhelloworld.tak.domain.common.dept.dao.DeptRepositorySupport;
import io.github.patternhelloworld.tak.domain.common.user.dao.UserRepository;
import io.github.patternhelloworld.tak.domain.common.user.dao.UserRepositorySupport;
import io.github.patternhelloworld.tak.domain.common.user.dto.UserCommonDTO;
import io.github.patternhelloworld.tak.domain.common.user.dto.UserSearchFilter;
import io.github.patternhelloworld.tak.domain.common.user.entity.User;

import io.github.patternknife.securityhelper.oauth2.api.config.security.dao.KnifeAuthorizationRepository;
import io.github.patternknife.securityhelper.oauth2.api.config.security.entity.KnifeAuthorization;
import io.github.patternknife.securityhelper.oauth2.api.config.security.serivce.persistence.authorization.OAuth2AuthorizationServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserRepositorySupport userRepositorySupport;
    private final DeptRepositorySupport deptRepositorySupport;
    private final DealerRepositorySupport dealerRepositorySupport;
    private final RegisteredClientRepository registeredClientRepository;

    private final OAuth2AuthorizationServiceImpl authorizationService;
    private final KnifeAuthorizationRepository knifeAuthorizationRepository;
    /*
    *
    *   1. 사용자 조회
    *
    * */


    @Transactional(value = "commonTransactionManager", readOnly = true)
    public Page<UserCommonDTO.OneWithDeptDealer> findUsersPage(Boolean skipPagination,
                                                               Integer pageNum,
                                                               Integer pageSize,
                                                               String userSearchFilter,
                                                               String sorterValueFilter,
                                                               String dateRangeFilter, AccessTokenUserInfo accessTokenUserInfo) throws JsonProcessingException {
        if(skipPagination){
            pageNum = Integer.valueOf(CommonConstant.COMMON_PAGE_NUM);
            pageSize = Integer.valueOf(CommonConstant.COMMON_PAGE_SIZE_DEFAULT_MAX);
        }

        ObjectMapper objectMapper = new ObjectMapper();

        UserSearchFilter deserializedUserSearchFilter;
        if(userSearchFilter != null) {
            deserializedUserSearchFilter = objectMapper.readValue(userSearchFilter, UserSearchFilter.class);
        }else{
            deserializedUserSearchFilter = new UserSearchFilter();
        }
        deserializedUserSearchFilter.setDealerCd(accessTokenUserInfo.getAdditionalAccessTokenUserInfo().getInfo().getDealerCd());

        return userRepositorySupport.getUsersPage(pageNum,pageSize,objectMapper.writeValueAsString(deserializedUserSearchFilter),sorterValueFilter,dateRangeFilter);

    }


    // 자기 자신의 정보를 Access Token 으로 조회
    @Transactional(value = "commonTransactionManager")
    public void resetPasswordFailedCountByAccessToken(String token){

        OAuth2Authorization oAuth2Authorization = authorizationService.findByToken(token, OAuth2TokenType.ACCESS_TOKEN);

        if(oAuth2Authorization != null) {
            User user = userRepository.findByUserId(oAuth2Authorization.getPrincipalName()).orElse(null);
            if(user != null){
                if(user.getPassword().isPasswordFailedCountOverZero()) {
                    user.getPassword().setFailedCount(0);
                }
            }else{
                // ignore
            }

        }else{
            // Ignore
        }
    }

    /*
        2. 사용자 생성
    * */
    public UserCommonDTO.UserIdx createUser(UserCommonDTO.OneWithDeptDealer dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo){

       User user = userRepository.save(dto.toEntity(deptRepositorySupport.findByIdWithoutThrow(dto.getDeptIdx()), accessTokenUserInfo.getUsername()));
       return new UserCommonDTO.UserIdx(user.getUserIdx());
    }
    @Transactional(value = "commonTransactionManager")
    public UserCommonDTO.OneWithDeptDealer updateUser(UserCommonDTO.OneWithDeptDealer dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo){

        User user = userRepositorySupport.findById(dto.getUserIdx());
        user.updateUser(dto, deptRepositorySupport.findByIdWithoutThrow(dto.getDeptIdx()), accessTokenUserInfo.getUsername());

        return dto;
    }


    @Transactional(value = "commonTransactionManager", rollbackFor=Exception.class)
    public void deleteUser(Long userIdx, String modifier){

        RegisteredClient registeredClient = registeredClientRepository.findByClientId(AdditionalAccessTokenUserInfo.UserType.USER.getValue());

        User user = userRepositorySupport.findById(userIdx);

        List<KnifeAuthorization> knifeAuthorizations = knifeAuthorizationRepository.findListByPrincipalNameAndRegisteredClientIdAndAccessTokenAppToken(user.getUserId(), registeredClient.getClientId(), null).orElse(new ArrayList<>());

        for (KnifeAuthorization knifeAuthorization : knifeAuthorizations) {
            knifeAuthorizationRepository.deleteById(knifeAuthorization.getId());
        }

        userRepositorySupport.deleteOne(userIdx, modifier);
    }

    @Transactional(value = "commonTransactionManager", rollbackFor=Exception.class)
    public void restoreUser(Long userIdx, String modifier){
        userRepositorySupport.restoreOne(userIdx, modifier);
    }

    @Transactional(value = "commonTransactionManager", rollbackFor=Exception.class)
    public void destroyUser(Long userIdx){
        userRepositorySupport.destroyOne(userIdx);
    }
}