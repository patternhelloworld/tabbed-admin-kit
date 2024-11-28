package io.github.patternhelloworld.tak.domain.common.carmodel.service;

import io.github.patternhelloworld.tak.domain.common.carmodel.dao.CarModelRepository;
import io.github.patternhelloworld.tak.domain.common.carmodel.dto.CarModelResDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarModelService {

    private final CarModelRepository carModelRepository;

    public List<CarModelResDTO.CarModelSearch> getCarModelsByCarMakerIdx(Integer carMakerIdx) {
        return carModelRepository.findCarModelsByCarMakerIdx(carMakerIdx);
    }
}
