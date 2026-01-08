package cn.ymypay.team.utils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

/**
 * @author lvjinfa
 * @date 2025/6/1 下午6:22
 * @description: 时间工具包
 */
public class DateUtils {
    public static Timestamp nowMillis() {
        return new Timestamp(System.currentTimeMillis());
    }
    public static Timestamp nowSeconds() {
        return new Timestamp(System.currentTimeMillis() / 1000);
    }
    public static Date newDate(Integer year, Integer month, Integer day) {
        LocalDate localDate = LocalDate.of(year, month, day);
        return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }
    public static Date newDate(Integer year, Integer month) {
        return newDate(year, month, 1);
    }
    public static Date newDateForLastDay(Integer year, Integer month) {
        LocalDate localDate = LocalDate.of(year, month, 1);
        int lastDay = localDate.lengthOfMonth();
        // 创建包含时分秒的LocalDateTime
        LocalDateTime localDateTime = LocalDateTime.of(year, month, lastDay, 23, 59, 59);
        // 转换为Date对象
        return Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
    public static long getDay(Date date1, Date date2) {
        // 1. 获取两个 Date 的毫秒数
        long time1 = date1.getTime();
        long time2 = date2.getTime();

        // 2. 计算毫秒差 → 转换为天数（注意取绝对值，避免负数）
        long daysDiff = Math.abs((time2 - time1) / (1000 * 60 * 60 * 24));
        return daysDiff;
    }
}
