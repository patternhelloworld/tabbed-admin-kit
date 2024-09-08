package com.autofocus.pms.hq.domain.crm.mainmenu.api;

import com.autofocus.pms.common.config.response.GlobalSuccessPayload;
import com.autofocus.pms.hq.domain.crm.mainmenu.dto.MainMenuReqDTO;
import com.autofocus.pms.hq.domain.crm.mainmenu.dto.MainMenuResDTO;
import com.autofocus.pms.hq.domain.crm.mainmenu.service.MainMenuService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class MainMenuApi {

    private final MainMenuService mainMenuService;

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @GetMapping("/settings/menus/mains")
    public GlobalSuccessPayload<List<MainMenuResDTO.One>> getMainMenuListAll(@RequestParam(value = "mainMenuSearchFilter", required = false) String mainMenuSearchFilter) throws JsonProcessingException {
        return new GlobalSuccessPayload<>(mainMenuService.getMainMenuListAll(mainMenuSearchFilter));
    }

    /*
    *
    *   isAuthorized() : Menu 들을 권한을 확인하므로, isAuthenticated() 에서 이거 사용으로 바뀜, customSecurityExpressionService 함수 참고 바랍니다.
    * */
    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PutMapping("/settings/menus/mains/{mainMenuIdx}")
    public GlobalSuccessPayload<MainMenuResDTO.One> updateMainMenu(@PathVariable(name = "mainMenuIdx") final Integer mainMenuIdx, @Valid @RequestBody final MainMenuReqDTO.UpdateOne dto) {
        return new GlobalSuccessPayload<>(mainMenuService.updateMainMenu(mainMenuIdx, dto));
    }
}
