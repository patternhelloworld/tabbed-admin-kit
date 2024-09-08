package com.autofocus.pms.hq.domain.common.carmodeldetail.api;


import com.autofocus.pms.common.config.response.GlobalSuccessPayload;
import com.autofocus.pms.hq.domain.common.carmodeldetail.dto.CarModelDetailResDTO;
import com.autofocus.pms.hq.domain.common.carmodeldetail.service.CarModelDetailService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class CarModelDetailApi {

    private final CarModelDetailService carModelDetailService;

    @GetMapping("/cars/models/details/search")
    public GlobalSuccessPayload<List<CarModelDetailResDTO.CarModelDetailSearch>> getCarModelDetailsByCarModelIdx(@RequestParam(value = "carModelIdx") Integer carModelIdx) {
        return new GlobalSuccessPayload<>(carModelDetailService.getCarModelDetailsByCarModelIdx(carModelIdx));
    }
    @GetMapping("/cars/models/details/search/distinct-years")
    public GlobalSuccessPayload<List<Integer>> getDistinctYears() {
        return new GlobalSuccessPayload<>(carModelDetailService.getDistinctYears());
    }

}
