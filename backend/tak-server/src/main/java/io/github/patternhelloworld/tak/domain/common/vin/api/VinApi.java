package io.github.patternhelloworld.tak.domain.common.vin.api;

import io.github.patternhelloworld.common.config.response.GlobalSuccessPayload;
import io.github.patternhelloworld.common.util.CommonConstant;
import io.github.patternhelloworld.tak.config.securityimpl.principal.AccessTokenUserInfo;
import io.github.patternhelloworld.tak.domain.common.vin.dto.VinCommonDTO;
import io.github.patternhelloworld.tak.domain.common.vin.service.VinService;
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
public class VinApi {
    private final VinService vinService;

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @GetMapping("/cars/vins")
    public GlobalSuccessPayload<Page<VinCommonDTO.One>> getVinPage(@RequestParam(value = "skipPagination", required = false, defaultValue = "false") final Boolean skipPagination,
                                                                                   @RequestParam(value = "pageNum", required = false, defaultValue = CommonConstant.COMMON_PAGE_NUM) final Integer pageNum,
                                                                                   @RequestParam(value = "pageSize", required = false, defaultValue = CommonConstant.COMMON_PAGE_SIZE) final Integer pageSize,
                                                                                   @RequestParam(value = "vinSearchFilter", required = false) final String vinSearchFilter,
                                                                                   @RequestParam(value = "sorterValueFilter", required = false) final String sorterValueFilter,
                                                                                   @RequestParam(value = "dateRangeFilter", required = false) String dateRangeFilter,
                                                                                   @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo)
            throws JsonProcessingException {
        return new GlobalSuccessPayload<>(vinService.getVinPage(skipPagination, pageNum, pageSize, vinSearchFilter, sorterValueFilter, dateRangeFilter));
    }


    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PutMapping("/cars/vins/{vinIdx}")
    public GlobalSuccessPayload<VinCommonDTO.One> updateVin(@PathVariable("vinIdx") final Long vinIdx,
                                                                      @Valid @RequestBody final VinCommonDTO.One dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo)  {
        dto.setVinIdx(vinIdx);
        return new GlobalSuccessPayload<>(vinService.updateVin(dto, accessTokenUserInfo));
    }

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PostMapping("/cars/vins")
    public GlobalSuccessPayload<VinCommonDTO.VinIdx> createVin(@RequestBody final VinCommonDTO.One dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {
        return new GlobalSuccessPayload<>(vinService.createVin(dto, accessTokenUserInfo));
    }

    /*
     *   복원 가능 한 삭제 (delete)
     * */
    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PatchMapping("/cars/vins/{vinIdx}/delete")
    public GlobalSuccessPayload<VinCommonDTO.VinIdx> deleteVin(@PathVariable("vinIdx") final Long vinIdx, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {

        vinService.deleteVin(vinIdx, accessTokenUserInfo.getUsername());

        return new GlobalSuccessPayload<>(new VinCommonDTO.VinIdx(vinIdx));
    }

    /*
     *   복원 가능 한 삭제 (restore)
     * */
    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PatchMapping("/cars/vins/{vinIdx}/restore")
    public GlobalSuccessPayload<VinCommonDTO.VinIdx> restoreVin(@PathVariable("vinIdx") final Long vinIdx, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {

        vinService.restoreVin(vinIdx, accessTokenUserInfo.getUsername());

        return new GlobalSuccessPayload<>(new VinCommonDTO.VinIdx(vinIdx));
    }

    /*
     *   복원 불가 한 삭제 (destroy)
     * */
    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @DeleteMapping("/cars/vins/{vinIdx}")
    public GlobalSuccessPayload<VinCommonDTO.VinIdx> destroyVin(@PathVariable("vinIdx") final Long vinIdx, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {

        vinService.destroyVin(vinIdx);

        return new GlobalSuccessPayload<>(new VinCommonDTO.VinIdx(vinIdx));
    }
}
