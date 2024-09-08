package com.autofocus.pms.hq.domain.common.approvalline.service;

import com.autofocus.pms.common.config.response.error.exception.data.AlreadyExistsException;
import com.autofocus.pms.common.config.response.error.exception.data.ResourceNotFoundException;
import com.autofocus.pms.hq.config.database.typeconverter.LineGb;
import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.config.securityimpl.principal.AccessTokenUserInfo;
import com.autofocus.pms.hq.config.securityimpl.principal.AdditionalAccessTokenUserInfo;
import com.autofocus.pms.hq.domain.common.approvalline.dao.ApprovalLineRepository;
import com.autofocus.pms.hq.domain.common.approvalline.dao.ApprovalLineRepositorySupport;
import com.autofocus.pms.hq.domain.common.approvalline.dto.ApprovalLineReqDTO;
import com.autofocus.pms.hq.domain.common.approvalline.dto.ApprovalLineResDTO;
import com.autofocus.pms.hq.domain.common.approvalline.entity.ApprovalLine;
import com.autofocus.pms.hq.domain.common.dealer.dao.DealerRepository;
import com.autofocus.pms.hq.domain.common.dealer.dao.DealerRepositorySupport;
import com.autofocus.pms.hq.domain.common.dealer.entity.Dealer;
import com.autofocus.pms.hq.domain.common.dept.dao.DeptRepository;
import com.autofocus.pms.hq.domain.common.dept.entity.Dept;
import com.autofocus.pms.hq.domain.common.user.dao.UserRepository;
import com.autofocus.pms.hq.domain.common.user.dao.UserRepositorySupport;
import com.autofocus.pms.hq.domain.common.user.dto.UserCommonDTO;
import com.autofocus.pms.hq.domain.common.user.entity.User;
import com.autofocus.pms.security.oauth2.config.security.entity.CustomOauthAccessToken;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@RequiredArgsConstructor
public class ApprovalLineService {
    private final ApprovalLineRepository approvalLineRepository;
    private final ApprovalLineRepositorySupport approvalLineRepositorySupport;
    private final UserRepository userRepository;
    private final UserRepositorySupport userRepositorySupport;
    private final DealerRepositorySupport dealerRepositorySupport;

    public List<ApprovalLineResDTO.One> getApprovalLine(Integer dealerCd) throws JsonProcessingException {
        List<ApprovalLineResDTO.One> approvalLines = approvalLineRepositorySupport.findList(dealerCd);
        List<ApprovalLineResDTO.DeptAndLineGbs> showroomAndLineData = approvalLineRepositorySupport.findRegistrableLineGbs(dealerCd);

        for (ApprovalLineResDTO.DeptAndLineGbs showroom : showroomAndLineData) {
            int deptIdx = showroom.getDeptIdx();
            String deptNm = showroom.getDeptNm();

            if (showroom.getLineA() == 0) {
                ApprovalLineResDTO.One nullObject = createNullObject(deptIdx, deptNm, LineGb.A, "계약");
                approvalLines.add(nullObject);
            }
            if (showroom.getLineC() == 0) {
                ApprovalLineResDTO.One nullObject = createNullObject(deptIdx, deptNm, LineGb.C, "해약");
                approvalLines.add(nullObject);
            }

            if (showroom.getLineD() == 0) {
                ApprovalLineResDTO.One nullObject = createNullObject(deptIdx, deptNm, LineGb.D, "시승");
                approvalLines.add(nullObject);
            }
        }

        for (ApprovalLineResDTO.One dto : approvalLines) {
            if (dto.getLineDetails() == null) {
                continue;
            }
            Map<Integer, Long> userNodeMap = extractUserIds(dto.getLineDetails());
            List<Long> userIds = new ArrayList<>(userNodeMap.values());
            List<UserCommonDTO.OneWithDept> userInfoWithDept = userRepositorySupport.findUserInfosByUserIds(userIds);
            for (Integer index : userNodeMap.keySet()) {
                Long userId = userNodeMap.get(index);
                UserCommonDTO.OneWithDept userInfo = null;

                for (UserCommonDTO.OneWithDept info : userInfoWithDept) {
                    if (info.getUserIdx().equals(userId)) {
                        userInfo = info;
                        break;
                    }
                }

                if (userInfo != null) {
                    List<String> userDetails = Arrays.asList(
                            userInfo.getName(),
                            userInfo.getPosition(),
                            userInfo.getDeptNm()
                    );
                    dto.addUser(index, userDetails);
                }
            }
        }
        approvalLines.sort(Comparator.comparing(ApprovalLineResDTO.One::getDeptNm)
                .thenComparing(ApprovalLineResDTO.One::getLineGbStr));
        return approvalLines;
    }

    private ApprovalLineResDTO.One createNullObject(int deptIdx, String deptNm, LineGb lineGb, String lineGbStr) {
        ApprovalLineResDTO.One nullObject = new ApprovalLineResDTO.One();
        nullObject.setDeptIdx(deptIdx);
        nullObject.setDeptNm(deptNm);
        nullObject.setApprovalLineIdx(null);
        nullObject.setDealerCd(null);
        nullObject.setShowroomIdx(deptIdx);
        nullObject.setLineGb(lineGb);
        nullObject.setLineDetails(null);
        nullObject.setRegUserid(null);
        nullObject.setRegDt(null);
        nullObject.setModUserid(null);
        nullObject.setModDt(null);
        nullObject.setLineGbStr(lineGbStr);
        return nullObject;
    }

    private Map<Integer, Long> extractUserIds(String lineDetails) {
        Map<Integer, Long> userIdMap = new HashMap<>();
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode lineDetailsNode = objectMapper.readTree(lineDetails);
            Iterator<Map.Entry<String, JsonNode>> fields = lineDetailsNode.fields();

            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String key = field.getKey();
                JsonNode valueNode = field.getValue();
                String userIdString = valueNode.path("userid").asText();

                if (!userIdString.isEmpty()) {
                    try {
                        Long userId = Long.parseLong(userIdString);
                        userIdMap.put(Integer.parseInt(key), userId);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return userIdMap;
    }

    public List<ApprovalLineResDTO.DeptAndLineGbs> getShowroomAndLineGbs(Integer dealerCd) {
        return approvalLineRepositorySupport.findRegistrableLineGbs(dealerCd);
    }

    @Transactional(value = "commonTransactionManager", rollbackFor=Exception.class)
    public ApprovalLineResDTO.ApprovalLineIdx updateApprovalLine(Integer approvalLineIdx, ApprovalLineReqDTO.CreateUpdateOne dto, Long userid) {
        User user = userRepository.findById(userid).orElseThrow(() -> new ResourceNotFoundException("User for '" + userid + "' not found."));
        ApprovalLine approvalLine = approvalLineRepository.findById(approvalLineIdx).orElseThrow(() -> new ResourceNotFoundException("ApprovalLine for '" + approvalLineIdx + "' not found."));

        Optional<ApprovalLine> optionalApprovalLine = approvalLineRepository.findApprovalLineIdxByShowroomIdxAndLineGbAndDelYn(
                dto.getShowroomIdx(), dto.getLineGb(), YNCode.N);
        if (optionalApprovalLine.isPresent()) {
            if(!optionalApprovalLine.get().getApprovalLineIdx().equals(approvalLineIdx)){
                throw new AlreadyExistsException("전시장과 결재라인이 이미 존재합니다.");
            }
        }

        return approvalLineRepositorySupport.updateApprovalLine(approvalLine, dto, user);
    }

    public ApprovalLineResDTO.ApprovalLineIdx createApprovalLine(ApprovalLineReqDTO.CreateUpdateOne dto, @AuthenticationPrincipal AccessTokenUserInfo accessTokenUserInfo){
        Dealer dealer = dealerRepositorySupport.findById(Math.toIntExact(accessTokenUserInfo.getAdditionalAccessTokenUserInfo().getInfo().getDealerCd()));

        Optional<ApprovalLine> optionalApprovalLine = approvalLineRepository.findApprovalLineIdxByShowroomIdxAndLineGbAndDelYn(
                dto.getShowroomIdx(), dto.getLineGb(), YNCode.N);
        if(optionalApprovalLine.isPresent()) {
            throw new AlreadyExistsException("전시장과 결재라인이 이미 존재합니다.");
        }

        return new ApprovalLineResDTO.ApprovalLineIdx(approvalLineRepository.save(dto.toEntity(dealer)));
    }

    @Transactional(value = "commonTransactionManager", rollbackFor=Exception.class)
    public void deleteApprovalLine(Integer approvalLineIdx, String modifier){
        ApprovalLine approvalLine = approvalLineRepository.findById(approvalLineIdx).orElseThrow(() -> new ResourceNotFoundException("ApprovalLine for '" + approvalLineIdx + "' not found."));
        approvalLineRepositorySupport.deleteOne(approvalLine, modifier);
    }

}


