package io.github.patternhelloworld.tak.domain.common.carmaker.service;

import io.github.patternhelloworld.tak.domain.common.carmaker.dao.CarMakerRepository;
import io.github.patternhelloworld.tak.domain.common.carmaker.dto.CarMakerResDTO;
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
