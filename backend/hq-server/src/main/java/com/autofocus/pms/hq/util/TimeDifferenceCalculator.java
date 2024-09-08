package com.autofocus.pms.hq.util;

import jakarta.annotation.Nullable;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TimeDifferenceCalculator {

    public static LocalDateTime getFutureTime(Long hours) {
        LocalDateTime now = LocalDateTime.now();
        return now.plusHours(hours);
    }

    public static LocalDateTime getFutureTime(Timestamp startTimestamp, Long hours) {
        LocalDateTime startTimestampAsLocalDateTime = startTimestamp.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        return startTimestampAsLocalDateTime.plusHours(hours);
    }

    public static @Nullable Long calculateHoursDifference(Timestamp startTimestamp, LocalDateTime endLocalDateTime) {
        if(startTimestamp == null || endLocalDateTime == null) {
            return null;
        }
        // Timestamp를 LocalDateTime으로 변환
        LocalDateTime startTimestampAsLocalDateTime = startTimestamp.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        // 두 LocalDateTime 간의 Duration 계산
        Duration duration = Duration.between(startTimestampAsLocalDateTime, endLocalDateTime);

        // Duration을 시간 단위로 변환
        return duration.toHours();
    }

    public static @Nullable Long calculateCurrentTimeDifference(Timestamp startTimestamp) {
        if(startTimestamp == null) {
            return null;
        }

        // 현재 시간 가져오기
        LocalDateTime currentTime = LocalDateTime.now();

        // Timestamp를 LocalDateTime으로 변환
        LocalDateTime startTimestampAsLocalDateTime = startTimestamp.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        // 두 LocalDateTime 간의 Duration 계산
        Duration duration = Duration.between(startTimestampAsLocalDateTime, currentTime);

        // Duration을 시간 단위로 변환
        return duration.toHours();
    }

}
