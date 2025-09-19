package cn.ymypay.team.common.advice;

import cn.ymypay.team.common.Result ;
import cn.ymypay.team.utils.AESDateUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * @author AMEN
 */
@ControllerAdvice
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    @Value("${aes.key}")
    private String aesKey;
    @Value("${aes.enabled}")
    private boolean aesEnabled;
    @Value("${iv}")
    private String iv;
    @Value("${sign.key}")
    private String signKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                 Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                 ServerHttpRequest request, ServerHttpResponse response) {
        if (!aesEnabled) {
            return body;
        }
//        // 白名单
//        String requestUri = request.getURI().getPath();
//        if (requestUri.equals("/static/uploadImage")) {
//            return body;
//        }
        if (body instanceof Result<?>) {
            Result<?> result = (Result<?>) body;
            Object data = result.getData();
            if (data != null) {
                try {
                    // 将data对象转换为JSON字符串
                    String jsonData = objectMapper.writeValueAsString(data);
                    // 加密data字段
                    String encryptedData = AESDateUtil.encrypt(jsonData,aesKey, iv);
                    // 设置加密后的数据
                    ((Result<String>) result).setData(encryptedData);
                } catch (Exception e) {
                    throw new RuntimeException("数据加密失败");
                }
            }
        }
        return body;
    }
}