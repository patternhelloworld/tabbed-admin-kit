package io.github.patternhelloworld.tak.domain.common.carmodeldetail.dao;

import io.github.patternhelloworld.tak.domain.common.carmodeldetail.dto.CarModelDetailResDTO;
import io.github.patternhelloworld.tak.domain.common.carmodeldetail.entity.CarModelDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;


import java.util.List;

public interface CarModelDetailRepository extends JpaRepository<CarModelDetail, Integer>, QuerydslPredicateExecutor<CarModelDetail> {

    @Query("SELECT DISTINCT cmd.year FROM CarModelDetail cmd ORDER BY cmd.year")
    List<Integer> findDistinctYears();


    @Query("SELECT new io.github.patternhelloworld.tak.domain.common.carmodeldetail.dto.CarModelDetailResDTO$CarModelDetailSearch(cmd.carModelDetailIdx, cmd.name, cmd.code, cmd.motorType, cmd.carName) " +
            "FROM CarModelDetail cmd WHERE cmd.carModelIdx = :carModelIdx")
    List<CarModelDetailResDTO.CarModelDetailSearch> findDetailsByCarModelIdx(@Param("carModelIdx") Integer carModelIdx);
}
