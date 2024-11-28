package io.github.patternhelloworld.tak.domain.common.carmodeldetail.service;

import io.github.patternhelloworld.tak.domain.common.carmodeldetail.dao.CarModelDetailRepository;
import io.github.patternhelloworld.tak.domain.common.carmodeldetail.dto.CarModelDetailResDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarModelDetailService {

    private final CarModelDetailRepository carModelDetailRepository;

    public List<CarModelDetailResDTO.CarModelDetailSearch> getCarModelDetailsByCarModelIdx(Integer carModelIdx) {
        return carModelDetailRepository.findDetailsByCarModelIdx(carModelIdx);
    }

    public List<Integer> getDistinctYears() {
        return carModelDetailRepository.findDistinctYears();
    }

}
