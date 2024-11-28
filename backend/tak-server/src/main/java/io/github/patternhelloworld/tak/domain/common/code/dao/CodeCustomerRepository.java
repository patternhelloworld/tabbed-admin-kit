package io.github.patternhelloworld.tak.domain.common.code.dao;

import io.github.patternhelloworld.tak.config.database.typeconverter.YNCode;
import io.github.patternhelloworld.tak.domain.common.code.entity.CodeCustomer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

import java.util.List;
import java.util.Optional;

public interface CodeCustomerRepository extends JpaRepository<CodeCustomer, Integer>, QuerydslPredicateExecutor<CodeCustomer> {
    Optional<CodeCustomer> findByCodeCustomerIdxAndCategoryYnAndDelYn(Integer codeCustomerIdx, YNCode categoryYn, YNCode delYn);
    Optional<List<CodeCustomer>> findByParentAndCategoryYnAndDelYnOrderBySortAsc(Integer parent, YNCode categoryYn, YNCode delYn);
    Optional<CodeCustomer> findByCategoryCdAndCategoryYnAndDelYn(String categoryCd, YNCode categoryYn, YNCode delYn);
    List<CodeCustomer> findByParentAndCategoryYn(Integer parent, YNCode categoryYn);

}