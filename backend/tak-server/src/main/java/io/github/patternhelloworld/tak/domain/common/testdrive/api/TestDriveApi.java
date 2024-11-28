package io.github.patternhelloworld.tak.domain.common.testdrive.api;

import io.github.patternhelloworld.common.config.response.GlobalSuccessPayload;
import io.github.patternhelloworld.common.util.CommonConstant;
import io.github.patternhelloworld.tak.config.securityimpl.principal.AccessTokenUserInfo;
import io.github.patternhelloworld.tak.domain.common.testdrive.dto.TestDriveCommonDTO;
import io.github.patternhelloworld.tak.domain.common.testdrive.service.TestDriveService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class TestDriveApi {

    private final TestDriveService testDriveService;

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @GetMapping("/cars/test-drives")
    public GlobalSuccessPayload<Page<TestDriveCommonDTO.One>> getTestDrivePage(
            @RequestParam(value = "skipPagination", required = false, defaultValue = "false") final Boolean skipPagination,
            @RequestParam(value = "pageNum", required = false, defaultValue = CommonConstant.COMMON_PAGE_NUM) final Integer pageNum,
            @RequestParam(value = "pageSize", required = false, defaultValue = CommonConstant.COMMON_PAGE_SIZE) final Integer pageSize,
            @RequestParam(value = "testDriveSearchFilter", required = false) final String testDriveSearchFilter,
            @RequestParam(value = "sorterValueFilter", required = false) final String sorterValueFilter,
            @RequestParam(value = "dateRangeFilter", required = false) String dateRangeFilter,
            @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) throws JsonProcessingException {
        return new GlobalSuccessPayload<>(testDriveService.getTestDrivePage(skipPagination, pageNum, pageSize, testDriveSearchFilter, sorterValueFilter, dateRangeFilter));
    }

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PutMapping("/cars/test-drives/{testDriveIdx}")
    public GlobalSuccessPayload<TestDriveCommonDTO.One> updateTestDrive(@PathVariable("testDriveIdx") final Long testDriveIdx,
                                                                        @Valid @RequestBody final TestDriveCommonDTO.One dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {
        dto.setTestDriveIdx(testDriveIdx);
        return new GlobalSuccessPayload<>(testDriveService.updateTestDrive(dto, accessTokenUserInfo));
    }

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PostMapping("/cars/test-drives")
    public GlobalSuccessPayload<TestDriveCommonDTO.TestDriveIdx> createTestDrive(@RequestBody final TestDriveCommonDTO.One dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {
        return new GlobalSuccessPayload<>(testDriveService.createTestDrive(dto, accessTokenUserInfo));
    }

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PatchMapping("/cars/test-drives/{testDriveIdx}/delete")
    public GlobalSuccessPayload<TestDriveCommonDTO.TestDriveIdx> deleteTestDrive(@PathVariable("testDriveIdx") final Long testDriveIdx, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {
        testDriveService.deleteTestDrive(testDriveIdx, accessTokenUserInfo.getUsername());
        return new GlobalSuccessPayload<>(new TestDriveCommonDTO.TestDriveIdx(testDriveIdx));
    }

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PatchMapping("/cars/test-drives/{testDriveIdx}/restore")
    public GlobalSuccessPayload<TestDriveCommonDTO.TestDriveIdx> restoreTestDrive(@PathVariable("testDriveIdx") final Long testDriveIdx, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {
        testDriveService.restoreTestDrive(testDriveIdx, accessTokenUserInfo.getUsername());
        return new GlobalSuccessPayload<>(new TestDriveCommonDTO.TestDriveIdx(testDriveIdx));
    }

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @DeleteMapping("/cars/test-drives/{testDriveIdx}")
    public GlobalSuccessPayload<TestDriveCommonDTO.TestDriveIdx> destroyTestDrive(@PathVariable("testDriveIdx") final Long testDriveIdx, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {
        testDriveService.destroyTestDrive(testDriveIdx);
        return new GlobalSuccessPayload<>(new TestDriveCommonDTO.TestDriveIdx(testDriveIdx));
    }
}
