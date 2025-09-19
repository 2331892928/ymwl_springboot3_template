package cn.ymypay.team.config;

import cn.ymypay.team.common.Result;
import cn.ymypay.team.utils.AESDateUtil;
import cn.ymypay.team.utils.TimeUtils;
import com.alibaba.fastjson2.JSONObject;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.util.DigestUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * 加解密过滤器
 * @author amen
 */
public class EncryptionFilter implements Filter {

    private String aesKey;
    private boolean aesEnabled;
    private String iv;
    private String signKey;
    private Integer controllerTime;


    public EncryptionFilter(String aesKey, boolean aesEnabled, String iv, String signKey, Integer controllerTime) {
        this.aesKey = aesKey;
        this.aesEnabled = aesEnabled;
        this.iv = iv;
        this.signKey = signKey;
        this.controllerTime = controllerTime;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        if (!aesEnabled || "GET".equalsIgnoreCase(httpRequest.getMethod())) {
            chain.doFilter(request, response);
            return;
        }
//        System.out.println("aesEnabled========"+aesEnabled);
        ContentCachingRequestWrapper requestWrapper1 = new ContentCachingRequestWrapper(httpRequest);

        if (isEncryptedRequest(httpRequest)) {
            try {
                // 读取加密的请求体
                String requestBody = IOUtils.toString(requestWrapper1.getInputStream(), StandardCharsets.UTF_8);
                if (requestBody==null|| requestBody.isEmpty()) {
                    chain.doFilter(request, response);
                    return;
                }
//                System.out.println(requestBody);
                JSONObject json = JSONObject.parseObject(requestBody);
                String encryptedData = json.getString("data");
                String clientSign = json.getString("sign");
                // 解密
                String decryptedData = AESDateUtil.decrypt(encryptedData, aesKey, iv);
                System.out.println("解密成功======" + decryptedData);
                JSONObject decryptedJson = JSONObject.parseObject(decryptedData);
                String t = decryptedJson.getString("t");
                // 签名校验
                if (!DigestUtils.md5DigestAsHex((t + signKey).getBytes()).equals(clientSign)) {
                    httpResponse.getWriter().write(JSONObject.toJSONString(Result.fail(407,"参数无效")));
                    return;
                }
                if ((Long.parseLong(t) + controllerTime * 1000) < TimeUtils.now().getTime()) {
                    httpResponse.getWriter().write(JSONObject.toJSONString(Result.fail(407,"参数已过期")));
                    return;
                }
                DecryptRequestWrapper requestWrapper = new DecryptRequestWrapper(httpRequest, decryptedData);
                chain.doFilter(requestWrapper, response);
            } catch (Exception e) {
                httpResponse.getWriter().write(JSONObject.toJSONString(Result.fail(407,"请求失败")));
            }
        } else {
            chain.doFilter(request, response);
        }
    }

    private String readRequestBody(HttpServletRequest request) throws IOException {
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        }
        return sb.toString();
    }

    private boolean isEncryptedRequest(HttpServletRequest request) {
        // Post和Put才进来
        if ("POST".equalsIgnoreCase(request.getMethod()) || "PUT".equalsIgnoreCase(request.getMethod())) {
            // 除了表单以外都进来
            String contentType = request.getContentType();
            if (contentType != null && (contentType.contains("application/x-www-form-urlencoded") || contentType.contains("multipart/form-data"))) {
                // 是表单提交，排除
                // 不是加密请求，因为是表单
                return false;
            } else {
                // 不是表单提交，认为是加密请求
                // 是加密请求
                return true;
            }
        }
        // 不是 POST 或 PUT 请求，也不是加密请求
        return false;
    }

}
