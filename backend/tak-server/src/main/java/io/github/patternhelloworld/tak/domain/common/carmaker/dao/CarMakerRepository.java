package io.github.patternhelloworld.tak.domain.common.carmaker.dao;

import io.github.patternhelloworld.tak.domain.common.carmaker.dto.CarMakerResDTO;
import io.github.patternhelloworld.tak.domain.common.carmaker.entity.CarMaker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;

public interface CarMakerRepository extends JpaRepository<CarMaker, Integer>, QuerydslPredicateExecutor<CarMaker> {

    @Query("SELECT new io.github.patternhelloworld.tak.domain.common.carmaker.dto.CarMakerResDTO$CarMakerSearch(cm.carMakerIdx, cm.carMakerNm) FROM CarMaker cm")
    List<CarMakerResDTO.CarMakerSearch> findCarMakerIdxAndName();
}
