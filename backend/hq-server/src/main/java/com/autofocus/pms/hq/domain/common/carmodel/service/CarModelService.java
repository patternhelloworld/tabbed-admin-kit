package com.autofocus.pms.hq.domain.common.carmodel.service;

import com.autofocus.pms.hq.domain.common.carmodel.dao.CarModelRepository;
import com.autofocus.pms.hq.domain.common.carmodel.dto.CarModelResDTO;
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
