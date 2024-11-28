package io.github.patternhelloworld.tak.domain.common.testdrive.service;

import io.github.patternhelloworld.tak.config.securityimpl.principal.AccessTokenUserInfo;
import io.github.patternhelloworld.tak.domain.common.customer.dao.CustomerRepositorySupport;
import io.github.patternhelloworld.tak.domain.common.customer.entity.Customer;
import io.github.patternhelloworld.tak.domain.common.testdrive.dao.TestDriveRepository;
import io.github.patternhelloworld.tak.domain.common.testdrive.dao.TestDriveRepositorySupport;
import io.github.patternhelloworld.tak.domain.common.testdrive.dto.TestDriveCommonDTO;
import io.github.patternhelloworld.tak.domain.common.testdrive.entity.TestDrive;
import io.github.patternhelloworld.tak.domain.common.user.dao.UserRepositorySupport;
import io.github.patternhelloworld.tak.domain.common.user.entity.User;
import io.github.patternhelloworld.tak.domain.common.vin.dao.VinRepository;
import io.github.patternhelloworld.tak.domain.common.vin.entity.Vin;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TestDriveService {

    private final TestDriveRepositorySupport testDriveRepositorySupport;
    private final TestDriveRepository testDriveRepository;

    private final VinRepository vinRepository;
    private final UserRepositorySupport userRepositorySupport;
    private final CustomerRepositorySupport customerRepositorySupport;

    public Page<TestDriveCommonDTO.One> getTestDrivePage(Boolean skipPagination, Integer pageNum, Integer pageSize,
                                                         String testDriveSearchFilter, String sorterValueFilter,
                                                         String dateRangeFilter) throws JsonProcessingException {
        return testDriveRepositorySupport.findByPageAndFilter(skipPagination, pageNum, pageSize, testDriveSearchFilter, sorterValueFilter, dateRangeFilter);
    }

    public TestDriveCommonDTO.TestDriveIdx createTestDrive(TestDriveCommonDTO.One dto, AccessTokenUserInfo accessTokenUserInfo) {
        Vin vin = vinRepository.findById(dto.getVinIdx()).orElse(null);
        User user = userRepositorySupport.findByUserId(accessTokenUserInfo.getUsername());
        Customer customer = customerRepositorySupport.findById(dto.getCustomerIdx());

        TestDrive testDrive = testDriveRepository.save(dto.toEntity(vin, user, customer));
        return new TestDriveCommonDTO.TestDriveIdx(testDrive.getTestDriveIdx());
    }

    @Transactional
    public TestDriveCommonDTO.One updateTestDrive(TestDriveCommonDTO.One dto, AccessTokenUserInfo accessTokenUserInfo) {
        TestDrive testDrive = testDriveRepositorySupport.findById(dto.getTestDriveIdx());
        Vin vin = vinRepository.findById(dto.getVinIdx()).orElse(null);
        User user = userRepositorySupport.findById(dto.getUserIdx());
        Customer customer = customerRepositorySupport.findById(dto.getCustomerIdx());

        testDrive.updateTestDrive(dto, vin, user, customer, accessTokenUserInfo.getUsername());
        return dto;
    }

    @Transactional
    public void deleteTestDrive(Long testDriveIdx, String modifier) {
        testDriveRepositorySupport.deleteOne(testDriveIdx, modifier);
    }

    @Transactional
    public void restoreTestDrive(Long testDriveIdx, String modifier) {
        testDriveRepositorySupport.restoreOne(testDriveIdx, modifier);
    }

    @Transactional
    public void destroyTestDrive(Long testDriveIdx) {
        testDriveRepositorySupport.destroyOne(testDriveIdx);
    }
}
