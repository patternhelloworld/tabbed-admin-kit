package com.autofocus.pms.hq.domain.common.carmodel.dao;

import com.autofocus.pms.hq.domain.common.carmodel.dto.CarModelResDTO;
import com.autofocus.pms.hq.domain.common.carmodel.entity.CarModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CarModelRepository extends JpaRepository<CarModel, Integer>, QuerydslPredicateExecutor<CarModel> {

    @Query("SELECT new com.autofocus.pms.hq.domain.common.carmodel.dto.CarModelResDTO$CarModelSearch(cm.carModelIdx, cm.modelCode, cm.modelName, cm.svcCode, cm.svcName, cm.modelSvcName) " +
            "FROM CarModel cm WHERE cm.carMakerIdx = :carMakerIdx")
    List<CarModelResDTO.CarModelSearch> findCarModelsByCarMakerIdx(@Param("carMakerIdx") Integer carMakerIdx);
}
