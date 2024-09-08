package com.autofocus.pms.hq.domain.common.carmaker.service;

import com.autofocus.pms.hq.domain.common.carmaker.dao.CarMakerRepository;
import com.autofocus.pms.hq.domain.common.carmaker.dto.CarMakerResDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CarMakerService {

    private final CarMakerRepository carMakerRepository;

    public List<CarMakerResDTO.CarMakerSearch> getCarMakerIdxAndName() {
        return carMakerRepository.findCarMakerIdxAndName();
    }
}
