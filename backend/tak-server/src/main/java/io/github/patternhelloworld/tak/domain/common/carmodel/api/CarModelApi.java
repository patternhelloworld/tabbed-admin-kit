package io.github.patternhelloworld.tak.domain.common.carmodel.api;


import io.github.patternhelloworld.common.config.response.GlobalSuccessPayload;
import io.github.patternhelloworld.tak.domain.common.carmodel.dto.CarModelResDTO;
import io.github.patternhelloworld.tak.domain.common.carmodel.service.CarModelService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class CarModelApi {

    private final CarModelService carModelService;


    @GetMapping("/cars/models/search")
    public GlobalSuccessPayload<List<CarModelResDTO.CarModelSearch>> getCarModelsByCarMakerIdx(@RequestParam(value = "carMakerIdx") Integer carMakerIdx) {
        return new GlobalSuccessPayload<>(carModelService.getCarModelsByCarMakerIdx(carMakerIdx));
    }
}
