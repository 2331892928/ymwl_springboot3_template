package cn.ymypay.team.utils;

import java.sql.Timestamp;

public class TimeUtils {
    public static Timestamp now() {
        return new Timestamp(System.currentTimeMillis());
    }

    public static Timestamp convertToTimestamp(long timestampMillis) {
        // 方法 1: 使用 Timestamp 的构造函数 (推荐)
        Timestamp timestamp = new Timestamp(timestampMillis);

        // 方法 2: 使用 Instant (Java 8+)
        // Instant instant = Instant.ofEpochMilli(timestampMillis);
        // Timestamp timestamp = Timestamp.from(instant);

        return timestamp;
    }
}
