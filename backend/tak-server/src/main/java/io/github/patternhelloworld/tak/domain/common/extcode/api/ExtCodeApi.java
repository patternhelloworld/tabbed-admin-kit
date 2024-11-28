package io.github.patternhelloworld.tak.domain.common.extcode.api;

import io.github.patternhelloworld.common.config.response.GlobalSuccessPayload;
import io.github.patternhelloworld.common.util.CommonConstant;
import io.github.patternhelloworld.tak.config.securityimpl.principal.AccessTokenUserInfo;
import io.github.patternhelloworld.tak.domain.common.extcode.dto.ExtCodeReqDTO;
import io.github.patternhelloworld.tak.domain.common.extcode.dto.ExtCodeResDTO;
import io.github.patternhelloworld.tak.domain.common.extcode.service.ExtCodeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@AllArgsConstructor
public class ExtCodeApi {
    private final ExtCodeService extCodeService;

   // @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @GetMapping("/codes/extcodes")
    public GlobalSuccessPayload<Page<ExtCodeResDTO.One>> getExtCodePage(@RequestParam(value = "skipPagination", required = false, defaultValue = "false") final Boolean skipPagination,
                                                                        @RequestParam(value = "pageNum", required = false, defaultValue = CommonConstant.COMMON_PAGE_NUM) final Integer pageNum,
                                                                        @RequestParam(value = "pageSize", required = false, defaultValue = CommonConstant.COMMON_PAGE_SIZE) final Integer pageSize,
                                                                        @RequestParam(value = "extCodeSearchFilter", required = false) final String extCodeSearchFilter,
                                                                        @RequestParam(value = "sorterValueFilter", required = false) final String sorterValueFilter,
                                                                        @RequestParam(value = "dateRangeFilter", required = false) String dateRangeFilter)
            throws JsonProcessingException {
        return new GlobalSuccessPayload<>(extCodeService.getExtCodePage(skipPagination, pageNum, pageSize, extCodeSearchFilter, sorterValueFilter, dateRangeFilter));
    }

 //   @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PostMapping("/codes/extcodes")
    public GlobalSuccessPayload<ExtCodeResDTO.Idx> createExtCode(@Valid @RequestBody final ExtCodeReqDTO.CreateUpdateOne dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {
        return new GlobalSuccessPayload<>(extCodeService.createExtCode(dto, accessTokenUserInfo));
    }


//    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PutMapping("/codes/extcodes/{ext_color_code_idx}")
    public GlobalSuccessPayload<ExtCodeResDTO.Idx> updateExtCode(@PathVariable("ext_color_code_idx") final Integer ext_color_code_idx, @Valid @RequestBody final ExtCodeReqDTO.CreateUpdateOne dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {
        return new GlobalSuccessPayload<>(extCodeService.updateExtCode(ext_color_code_idx, dto, accessTokenUserInfo));
    }


  //  @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PatchMapping("/codes/extcodes/{ext_color_code_idx}/delete")
    public GlobalSuccessPayload<ExtCodeResDTO.Idx> deleteExtCode(@PathVariable("ext_color_code_idx") final Integer ext_color_code_idx, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {
        extCodeService.deleteExtCode(ext_color_code_idx, accessTokenUserInfo);
        return new GlobalSuccessPayload<>(new ExtCodeResDTO.Idx(ext_color_code_idx));
    }

}
