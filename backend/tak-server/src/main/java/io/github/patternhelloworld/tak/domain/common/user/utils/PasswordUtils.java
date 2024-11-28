package io.github.patternhelloworld.tak.domain.common.user.utils;

import io.github.patternhelloworld.tak.domain.common.user.entity.Password;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

public class PasswordUtils {

    private static final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public static String encrypt(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    public static boolean isExpired(Password password) {
        if (password.getChangedDate() == null) {
            // If changedDate is null, consider the password as expired.
            return true;
        }
        // 현재 날짜에서 180일을 뺀 날짜를 계산
        LocalDateTime expiryDate = LocalDateTime.now().minusDays(Password.EXPIRED_AFTER);
        // 변경일자가 180일 전인지 확인
        return password.getChangedDate().isBefore(expiryDate);
    }

    public static boolean isPasswordFailedLimitOver(Password password) {
        if (password.getFailedCount() == null) {
            // If failedCount is null, consider it as no failed attempts.
            return false;
        }
        return password.getFailedCount() > Password.FAILED_LIMIT;
    }
}
