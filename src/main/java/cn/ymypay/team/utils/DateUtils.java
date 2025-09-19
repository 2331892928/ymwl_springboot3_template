package cn.ymypay.team.utils;

import java.sql.Timestamp;

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
}
