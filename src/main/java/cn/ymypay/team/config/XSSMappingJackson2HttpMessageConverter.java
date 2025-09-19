package cn.ymypay.team.config;

import com.fasterxml.jackson.databind.JavaType;
import cn.ymypay.team.utils.XssUtil;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.converter.HttpMessageNotWritableException;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * 在读取和写入JSON数据时特殊字符避免xss攻击的消息解析器
 *
 */
public class XSSMappingJackson2HttpMessageConverter extends MappingJackson2HttpMessageConverter {
    /**
     * 从HTTP输入消息中读取对象，同时应用XSS防护。
     *
     * @param type        类型令牌，表示要读取的对象类型。
     * @param contextClass    上下文类，提供类型解析的上下文信息。
     * @param inputMessage HTTP输入消息，包含要读取的JSON数据。
     * @return 从输入消息中解析出的对象，经过XSS防护处理。
     * @throws IOException 如果发生I/O错误。
     * @throws HttpMessageNotReadableException 如果消息无法读取。
     */
    @Override
    public Object read(Type type, Class contextClass,
                       HttpInputMessage inputMessage) throws IOException,
            HttpMessageNotReadableException {
        JavaType javaType = getJavaType(type, contextClass);
        Object obj = readJavaType(javaType, inputMessage);
        //得到请求json
        String json = super.getObjectMapper().writeValueAsString(obj);
        //过滤特殊字符
        String result = XssUtil.clean(json);
        Object resultObj = super.getObjectMapper().readValue(result, javaType);
        return resultObj;
    }
    /**
     * 从HTTP输入消息中读取指定Java类型的对象，内部使用。
     *
     * @param javaType    要读取的对象的Java类型。
     * @param inputMessage HTTP输入消息，包含要读取的JSON数据。
     * @return 从输入消息中解析出的对象。
     * @throws IOException 如果发生I/O错误。
     * @throws HttpMessageNotReadableException 如果消息无法读取。
     */
    private Object readJavaType(JavaType javaType, HttpInputMessage inputMessage) {
        try {
            return super.getObjectMapper().readValue(inputMessage.getBody(), javaType);
        } catch (IOException ex) {
            throw new HttpMessageNotReadableException("Could not read JSON: " + ex.getMessage(), ex);
        }
    }
    /**
     * 将对象写入HTTP输出消息，同时应用XSS防护。
     *
     * @param object 要写入的对象。
     * @param outputMessage HTTP输出消息，对象将被序列化为JSON并写入此消息。
     * @throws IOException 如果发生I/O错误。
     * @throws HttpMessageNotWritableException 如果消息无法写入。
     */
    @Override
    protected void writeInternal(Object object, HttpOutputMessage outputMessage)
            throws IOException, HttpMessageNotWritableException {
        //得到要输出的json
        String json = super.getObjectMapper().writeValueAsString(object);
        //过滤特殊字符
        String result = XssUtil.clean(json);
        // 输出
        outputMessage.getBody().write(result.getBytes());
    }
}

