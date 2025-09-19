package cn.ymypay.team.utils;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.util.LinkedMultiValueMap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lvjinfa
 * @date 2025/6/2 下午6:05
 * @description: 工具包
 */
public class YmwlUtils {
    public static HashMap<String, String> ConvertMap(LinkedMultiValueMap<String, String> multiMap) {

        HashMap<String, String> hashMap = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : multiMap.entrySet()) {
            String key = entry.getKey();
            List<String> values = entry.getValue();
            // 这里选择将所有值合并为一个字符串，例如使用逗号分隔，或者选择其他策略
            String value = String.join(",", values); // 或者选择其他合并方式
            hashMap.put(key, value);
        }
        return hashMap;
    }
    public static String getRealIP(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress;
    }
}
