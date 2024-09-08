package com.autofocus.pms.hq.domain.common.approvalline.entity;

import com.autofocus.pms.hq.config.database.typeconverter.LineGb;
import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.config.database.typeconverter.jpa.YNCodeConverter;
import com.autofocus.pms.hq.domain.common.approvalline.dto.ApprovalLineReqDTO;
import com.autofocus.pms.hq.domain.common.dealer.dto.DealerReqDTO;
import com.autofocus.pms.hq.domain.common.dealer.entity.Dealer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.io.IOException;
import java.time.LocalDateTime;

@Entity
@Table(name = "approval_line")
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ApprovalLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "approval_line_idx")
    private Integer approvalLineIdx;

    @Column(name = "dealer_cd", insertable = false, updatable = false)
    private Integer dealerCd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dealer_cd")
    private Dealer dealer;

    @Column(name = "showroom_idx")
    private Integer showroomIdx;

    @Enumerated(EnumType.STRING)
    @Column(name = "line_gb")
    private LineGb lineGb;

    @Column(name = "line_details")
    private String lineDetails;

    @Column(name = "reg_userid")
    private String regUserid;

    @Column(name = "reg_dt")
    @CreationTimestamp
    private LocalDateTime regDt;

    @Column(name = "reg_ip")
    private String regIp;

    @Column(name = "mod_userid")
    private String modUserid;

    @Column(name = "mod_dt")
    @UpdateTimestamp
    private LocalDateTime modDt;

    @Column(name = "mod_ip")
    private String modIp;

    @Setter
    @Column(name = "del_userid")
    private String delUserid;

    @Setter
    @Column(name = "del_dt")
    private LocalDateTime delDt;

    @Setter
    @Column(name = "del_ip")
    private String delIp;

    @Setter
    @Convert(converter = YNCodeConverter.class)
    @Column(name = "del_yn")
    private YNCode delYn;

    public void updateApprovalLine(ApprovalLineReqDTO.CreateUpdateOne dto, String userid) {
        this.lineGb = dto.getLineGb();
        this.lineDetails = dto.getLineDetails();
        this.modUserid = userid.toString();
        this.modIp = "modIp";
    }
}
