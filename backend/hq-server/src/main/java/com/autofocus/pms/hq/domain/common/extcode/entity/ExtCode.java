package com.autofocus.pms.hq.domain.common.extcode.entity;

import com.autofocus.pms.hq.config.database.typeconverter.YNCode;
import com.autofocus.pms.hq.config.database.typeconverter.jpa.YNCodeConverter;
import com.autofocus.pms.hq.domain.common.extcode.dto.ExtCodeReqDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "ext_color_code")
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ExtCode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ext_color_code_idx")
    private Integer extColorCodeIdx;

    @Column(name = "code", nullable = false)
    private String code;

    @Column(name = "description")
    private String description;

    @Column(name = "reg_user_id")
    private String regUserId;

    @CreationTimestamp
    @Column(name = "reg_dt")
    private LocalDateTime regDt;

    @Column(name = "mod_user_id")
    private String modUserId;

    @UpdateTimestamp
    @Column(name = "mod_dt")
    private LocalDateTime modDt;

    @Setter
    @Convert(converter = YNCodeConverter.class)
    @Column(name = "del_yn", nullable = false)
    private YNCode delYn = YNCode.N; // Default value set to 'N'

    @Setter
    @Column(name = "del_user_id")
    private String delUserId;

    @Setter
    @Column(name = "del_dt")
    private LocalDateTime delDt;

    public void updateExtCode(ExtCodeReqDTO.CreateUpdateOne dto, String userId) {
        this.code = dto.getCode();
        this.description = dto.getDescription();
        this.modUserId = userId;
    }

}
