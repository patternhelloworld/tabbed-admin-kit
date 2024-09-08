package com.autofocus.pms.hq.config.securityimpl.service.userdetail;


import com.autofocus.pms.hq.config.database.CommonQuerydslRepositorySupport;
import com.autofocus.pms.hq.config.securityimpl.principal.AccessTokenUserInfo;
import com.autofocus.pms.hq.config.securityimpl.principal.AdditionalAccessTokenUserInfo;
import com.autofocus.pms.hq.domain.common.user.dao.UserRepository;
import com.autofocus.pms.hq.domain.common.user.dto.UserCommonDTO;
import com.autofocus.pms.hq.domain.common.user.entity.Password;
import com.autofocus.pms.hq.domain.common.user.entity.User;

import com.autofocus.pms.hq.mapper.UserMapper;
import com.autofocus.pms.security.oauth2.config.security.dao.OauthClientDetailRepository;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;


@Service
public class UserDetailsServiceImpl extends CommonQuerydslRepositorySupport implements UserDetailsService {
    private final JPAQueryFactory jpaQueryFactory;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserDetailsServiceImpl(UserRepository userRepository,
                                  UserMapper userMapper,
                                  @Qualifier("commonJpaQueryFactory") JPAQueryFactory jpaQueryFactory, OauthClientDetailRepository oauthClientDetailRepository) {
        super(User.class);
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.jpaQueryFactory = jpaQueryFactory;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        UserCommonDTO.OneWithDeptDealerMenus userInfo = userMapper.findOneWithDeptDealerMenus(username);
        if(userInfo == null){
            throw new UsernameNotFoundException("ID : \"" + username + "\" 을 찾을 수 없습니다.");
        }
        if(userInfo.getDelDt() != null){
            throw new UsernameNotFoundException(userInfo.getUserId() + " 님의 계정은 현재 " + userInfo.getDelDt() + " 시점부터 비활성화 되어 있습니다.");
        }

        Password password = Password.builder().value(userInfo.getPassword()).changedDate(userInfo.getPasswordChangedAt()).failedCount(userInfo.getPasswordFailedCount()).build();


        if(password.isExpired()){
            throw new UsernameNotFoundException("비밀번호를 변경한지 " + Password.EXPIRED_AFTER +  " 일 지났습니다.");
        }
        if(password.isPasswordFailedLimitOver()){
            throw new UsernameNotFoundException(Password.FAILED_LIMIT + " 회 이상 패스워드 입력에 실패 하였습니다.");
        }

        return buildUserForAuthentication(userInfo, new ArrayList<GrantedAuthority>());
    }

    private AccessTokenUserInfo buildUserForAuthentication(UserCommonDTO.OneWithDeptDealerMenus userInfo, Collection<? extends GrantedAuthority> authorities) {

        String username = userInfo.getUserId();
        String password = userInfo.getPassword();

        boolean enabled = true;
        boolean accountNonExpired = true;
        boolean credentialsNonExpired = true;
        boolean accountNonLocked = true;

        AccessTokenUserInfo authUser = new AccessTokenUserInfo(username, password, enabled, accountNonExpired, credentialsNonExpired,
                accountNonLocked, authorities);

        // Spring Security 로그인 사용자 정보에 DB의 추가적인 컬럼들도 저장하기 위함.
        authUser.setAdditionalAccessTokenUserInfo(new AdditionalAccessTokenUserInfo(userInfo));

        return authUser;
    }



}