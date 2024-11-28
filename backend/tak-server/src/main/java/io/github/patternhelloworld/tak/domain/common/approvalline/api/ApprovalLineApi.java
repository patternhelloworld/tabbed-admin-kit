package io.github.patternhelloworld.tak.domain.common.approvalline.api;

import io.github.patternhelloworld.common.config.response.GlobalSuccessPayload;
import io.github.patternhelloworld.tak.config.securityimpl.principal.AccessTokenUserInfo;
import io.github.patternhelloworld.tak.domain.common.approvalline.dto.ApprovalLineReqDTO;
import io.github.patternhelloworld.tak.domain.common.approvalline.dto.ApprovalLineResDTO;
import io.github.patternhelloworld.tak.domain.common.approvalline.service.ApprovalLineService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/settings")
@AllArgsConstructor
public class ApprovalLineApi {

    private final ApprovalLineService approvalLineService;

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @GetMapping("/approvallines")
    public GlobalSuccessPayload<List<ApprovalLineResDTO.One>> getApprovalLine(@AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo)
            throws JsonProcessingException {
        return new GlobalSuccessPayload<>(approvalLineService.getApprovalLine(accessTokenUserInfo.getAdditionalAccessTokenUserInfo().getInfo().getDealerCd()));
    }

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @GetMapping("/approvallines/showroommetas")
    public GlobalSuccessPayload<List<ApprovalLineResDTO.DeptAndLineGbs>> getShowroomAndLines(@AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {
        return new GlobalSuccessPayload<>(approvalLineService.getShowroomAndLineGbs(accessTokenUserInfo.getAdditionalAccessTokenUserInfo().getInfo().getDealerCd()));
    }

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PutMapping("/approvallines/{approvalLineIdx}")
    public GlobalSuccessPayload<ApprovalLineResDTO.ApprovalLineIdx> updateApprovalLine(@PathVariable("approvalLineIdx") final Integer approvalLineIdx, @Valid @RequestBody final ApprovalLineReqDTO.CreateUpdateOne dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {
        return new GlobalSuccessPayload<>(approvalLineService.updateApprovalLine(approvalLineIdx, dto, accessTokenUserInfo.getAdditionalAccessTokenUserInfo().getInfo().getUserIdx()));
    }

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PostMapping("/approvallines")
    public GlobalSuccessPayload<ApprovalLineResDTO.ApprovalLineIdx> createApprovalLine(@RequestBody final ApprovalLineReqDTO.CreateUpdateOne dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {
        return new GlobalSuccessPayload<>(approvalLineService.createApprovalLine(dto, accessTokenUserInfo));
    }

    @PreAuthorize("@customSecurityExpressionService.isAuthorized()")
    @PatchMapping("/approvallines/{approvalLineIdx}/delete")
    public GlobalSuccessPayload<ApprovalLineResDTO.ApprovalLineIdx> deleteApprovalLine(@PathVariable("approvalLineIdx") final Integer approvalLineIdx, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo) {
        approvalLineService.deleteApprovalLine(approvalLineIdx, accessTokenUserInfo.getUsername());
        return new GlobalSuccessPayload<>(new ApprovalLineResDTO.ApprovalLineIdx(approvalLineIdx));
    }
}
