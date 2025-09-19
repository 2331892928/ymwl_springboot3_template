package cn.ymypay.team.config;

import cn.ymypay.team.common.Result;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.security.MessageDigest;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import static org.springframework.web.multipart.support.MultipartResolutionDelegate.isMultipartRequest;

public class DebounceFilter implements Filter {
    // 防抖动时间窗口（单位：毫秒，默认1秒）
    private static long DEBOUNCE_WINDOW_MS = 500;
    // 请求缓存：记录请求唯一键的最后处理时间
    private final ConcurrentHashMap<String, Long> requestCache = new ConcurrentHashMap<>();
    // URL白名单模式
    private final Set<Pattern> urlWhitelistPatterns = new HashSet<>();
    // 是否开启防抖
    private final Boolean debounceEnabled;

    public DebounceFilter(Boolean debounceEnabled,Integer debounceTime) {
        this.debounceEnabled = debounceEnabled;
        DEBOUNCE_WINDOW_MS = debounceTime;
        // 初始化白名单模式
        urlWhitelistPatterns.add(Pattern.compile("^/static/Preview/.*"));
        // 可以从配置文件或其他地方加载白名单模式
    }
    /**
     * 检查URL是否在白名单中
     */
    private boolean isUrlInWhitelist(String requestURI) {
        for (Pattern pattern : urlWhitelistPatterns) {
            if (pattern.matcher(requestURI).matches()) {
                return true;
            }
        }
        return false;
    }
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (!this.debounceEnabled) {
            chain.doFilter(request, response); // 直接放行
            return;
        }
        // 是否在白名单
        String requestURI = httpRequest.getRequestURI();
        if (isUrlInWhitelist(requestURI)) {
            chain.doFilter(request, response); // 直接放行
            return;
        }
        // 如果是表单
        if (isMultipartRequest(httpRequest)) {
            chain.doFilter(request, response); // 直接放行
            return;
        }
        // 设置响应的编码为UTF-8
        httpResponse.setCharacterEncoding("UTF-8");
        httpResponse.setContentType("application/json; charset=UTF-8");
        // 包装请求，支持多次读取请求体
        CachedBodyHttpServletRequest wrappedRequest = new CachedBodyHttpServletRequest(httpRequest);
        // 生成请求唯一标识
        String requestKey = generateRequestKey(wrappedRequest);
        // 清理过期缓存
        cleanExpiredCache();
        // 检查是否为重复请求
        if (isDuplicateRequest(requestKey)) {
            sendErrorResponse(httpResponse, "请求频繁，请稍后再试");
            return;
        }
        // 记录当前请求时间
        requestCache.put(requestKey, System.currentTimeMillis());
        // 继续处理请求
        chain.doFilter(wrappedRequest, response);
    }
    /**
     * 生成请求唯一标识（方法 + URI + 参数 + 请求体哈希）
     */
    private String generateRequestKey(HttpServletRequest request) throws IOException {
        StringBuilder keyBuilder = new StringBuilder();
        keyBuilder.append(request.getMethod())
                .append(":")
                .append(request.getRequestURI());
        // 添加排序后的查询参数
        String queryString = request.getQueryString();
        if (queryString != null) {
            keyBuilder.append("?").append(queryString);
        }
        // 添加请求体哈希
        keyBuilder.append(":").append(calculateBodyHash(request));
        return keyBuilder.toString();
    }
    /**
     * 计算请求体哈希（MD5）
     */
    private String calculateBodyHash(HttpServletRequest request) {
        try {
            byte[] body = ((CachedBodyHttpServletRequest) request).getCachedBody();
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] hashBytes = md.digest(body);
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (Exception e) {
            return "invalid_hash";
        }
    }
    /**
     * 清理过期缓存（超过时间窗口）
     */
    private void cleanExpiredCache() {
        long currentTime = System.currentTimeMillis();
        requestCache.entrySet().removeIf(entry ->
                (currentTime - entry.getValue()) > DEBOUNCE_WINDOW_MS
        );
    }
    /**
     * 检查是否为重复请求
     */
    private boolean isDuplicateRequest(String requestKey) {
        Long lastRequestTime = requestCache.get(requestKey);
        return lastRequestTime != null &&
                (System.currentTimeMillis() - lastRequestTime) < DEBOUNCE_WINDOW_MS;
    }
    /**
     * 返回错误响应
     */
    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(429); // HTTP 429 Too Many Requests
        response.setContentType("application/json");
        response.getWriter().write(Result.fail(429,message).toString());
    }

    @Override
    public void destroy() {}
}
