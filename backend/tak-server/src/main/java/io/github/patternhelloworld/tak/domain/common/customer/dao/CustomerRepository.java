package io.github.patternhelloworld.tak.domain.common.customer.dao;

import io.github.patternhelloworld.tak.domain.common.customer.entity.Customer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;


public interface CustomerRepository extends JpaRepository<Customer, Integer>, QuerydslPredicateExecutor<Customer> {
    @Modifying
    @Transactional
    @Query("UPDATE Customer c SET c.userManagerIdx = :userIdx, " +
            "c.modDt = :modDt, " +
            "c.modUserid = :modUserid " +
            "WHERE c.customerIdx IN :customerIdxList")
    int updateCustomersUserManager(@Param("userIdx") Long userIdx, @Param("modDt") LocalDateTime modDt, @Param("modUserid") String modUserid, @Param("customerIdxList") Set<Integer> customerIdxList);

    @Query("SELECT c.customerIdx, c.userManagerIdx FROM Customer c WHERE c.customerIdx IN :customerIdxList")
    List<Object[]> findFromUserManagers(@Param("customerIdxList") Set<Integer> customerIdxList);
}