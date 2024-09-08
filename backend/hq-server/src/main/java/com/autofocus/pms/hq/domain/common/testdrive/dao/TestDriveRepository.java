package com.autofocus.pms.hq.domain.common.testdrive.dao;


import com.autofocus.pms.hq.domain.common.testdrive.entity.TestDrive;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;

public interface TestDriveRepository extends JpaRepository<TestDrive, Long>, QuerydslPredicateExecutor<TestDrive> {
}