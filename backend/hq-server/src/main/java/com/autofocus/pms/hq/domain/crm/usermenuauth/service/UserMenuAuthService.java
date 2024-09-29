package com.autofocus.pms.hq.domain.crm.usermenuauth.service;

import com.autofocus.pms.hq.config.securityimpl.principal.AccessTokenUserInfo;
import com.autofocus.pms.hq.domain.crm.usermenuauth.dao.UserMenuAuthRepositorySupport;
import com.autofocus.pms.hq.domain.crm.usermenuauth.dto.UserMenuAuthCommonDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserMenuAuthService {

    private final UserMenuAuthRepositorySupport userMenuAuthRepositorySupport;

    public List<UserMenuAuthCommonDTO.OneWithSubMenu> findUserMenuAuthByUserIdx(Long userIdx) throws JsonProcessingException {
        return userMenuAuthRepositorySupport.findListByUserIdxAndSyncEmptyPermissions(userIdx);
    }


    @Transactional(value = "crmTransactionManager", rollbackFor=Exception.class)
    public List<UserMenuAuthCommonDTO.OneWithSubMenu> updateUserMenuAuths(UserMenuAuthCommonDTO.PermissionSet permissionSet, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) throws JsonProcessingException {

        // 권한을 복사할 대상이 있다면
        if(permissionSet.getCopyToUserIdx() != null){
            List<UserMenuAuthCommonDTO.OneWithSubMenu> copyToPermissions = userMenuAuthRepositorySupport.findListByUserIdxAndSyncEmptyPermissions(permissionSet.getCopyToUserIdx());
            List<UserMenuAuthCommonDTO.OneWithSubMenu> copiedCopyToPermissions = copyToPermissions.stream().peek(copyToPermission ->  {

                // sub_menu_idx 가 일치하는 것을 찾음
                UserMenuAuthCommonDTO.OneWithSubMenu copiedPermission = permissionSet.getPermissions().stream().filter(y -> y.getSubMenuIdx().equals(copyToPermission.getSubMenuIdx())).findAny().orElse(null);
                if(copiedPermission != null) {
                    copyToPermission.setReason("권한 복사 from 사용자 IDX : " + copiedPermission.getUserIdx());
                    copyToPermission.setYnLst(copiedPermission.getYnLst());
                    copyToPermission.setYnInt(copiedPermission.getYnInt());
                    copyToPermission.setYnMod(copiedPermission.getYnMod());
                    copyToPermission.setYnDel(copiedPermission.getYnDel());
                    copyToPermission.setYnXls(copiedPermission.getYnXls());
                }
            }).toList();
            userMenuAuthRepositorySupport.updateOneWithSubMenuList(copiedCopyToPermissions, accessTokenUserInfo.getUsername());
        }
        // 나의 권한만을 업데이트 합니다.
        return userMenuAuthRepositorySupport.updateOneWithSubMenuList(permissionSet.getPermissions(), accessTokenUserInfo.getUsername());
    }
}