package io.github.patternhelloworld.tak.domain.crm.mainmenu.service;

import io.github.patternhelloworld.tak.domain.crm.mainmenu.dao.MainMenuRepositorySupport;
import io.github.patternhelloworld.tak.domain.crm.mainmenu.dto.MainMenuReqDTO;
import io.github.patternhelloworld.tak.domain.crm.mainmenu.dto.MainMenuResDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MainMenuService {

    private final MainMenuRepositorySupport mainMenuRepositorySupport;

    public List<MainMenuResDTO.One> getMainMenuListAll(String mainMenuSearchFilter) throws JsonProcessingException {
        return mainMenuRepositorySupport.findByAll(mainMenuSearchFilter);
    }

    public MainMenuResDTO.One updateMainMenu(Integer mainMenuIdx, MainMenuReqDTO.UpdateOne dto) {
        return mainMenuRepositorySupport.updateByMainMenuIdx(mainMenuIdx, dto);
    }

}
