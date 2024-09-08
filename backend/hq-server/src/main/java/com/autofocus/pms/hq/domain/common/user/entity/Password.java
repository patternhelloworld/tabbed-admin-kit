package com.autofocus.pms.hq.domain.common.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.time.LocalDateTime;


@Embeddable
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Password {

    @Column(name = "password")
    private String value;

    @Column(name = "password_changed_at")
    private LocalDateTime changedDate;

    @Column(name = "password_failed_count")
    private Integer failedCount;

    @Builder
    public Password(final String value) {
        this.value = value;
        this.changedDate = LocalDateTime.now();
    }

    public static final int EXPIRED_AFTER = 180;

    public boolean isExpired() {
        if (this.changedDate == null) {
            // If changedDate is null, consider the password as expired.
            return true;
        }
        // 현재 날짜에서 180일을 뺀 날짜를 계산
        LocalDateTime expiryDate = LocalDateTime.now().minusDays(EXPIRED_AFTER);
        // 변경일자가 180일 전인지 확인
        return this.changedDate.isBefore(expiryDate);
    }

    public static final int FAILED_LIMIT = 5000;

    public boolean isPasswordFailedLimitOver() {
        if (this.failedCount == null) {
            // If failedCount is null, consider it as no failed attempts.
            return false;
        }
        return this.failedCount > FAILED_LIMIT;
    }
    public boolean isPasswordFailedCountOverZero() {
        if (this.failedCount == null) {
            // If failedCount is null, consider it as no failed attempts.
            return false;
        }
        return this.failedCount > 0;
    }
}
