package io.github.patternhelloworld.tak.domain.common.carmaker.api;


import io.github.patternhelloworld.common.config.response.GlobalSuccessPayload;
import io.github.patternhelloworld.tak.domain.common.carmaker.dto.CarMakerResDTO;
import io.github.patternhelloworld.tak.domain.common.carmaker.service.CarMakerService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class CarMakerApi {

    private final CarMakerService carMakerService;

    @GetMapping("/cars/makers/search")
    public GlobalSuccessPayload<List<CarMakerResDTO.CarMakerSearch>> getCarMakerIdxAndName() {
        return new GlobalSuccessPayload<>(carMakerService.getCarMakerIdxAndName());
    }
}
