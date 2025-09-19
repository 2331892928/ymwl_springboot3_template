package cn.ymypay.team.config;

/**
 * @author lvjinfa
 * @date 2025/5/24 下午1:21
 * @description: SQL分析与打印
 */
public class StdoutLogger extends com.p6spy.engine.spy.appender.StdoutLogger {
    public void logText(String text) {
        System.err.println(text);
    }
}
