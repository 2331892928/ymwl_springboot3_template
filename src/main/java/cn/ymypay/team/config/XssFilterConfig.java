package cn.ymypay.team.config;

import jakarta.servlet.DispatcherType;
import lombok.Data;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;

@Data
@Configuration
public class XssFilterConfig {
    @ConfigurationProperties(prefix = "xss")
    @Bean
    public XssFilterProperties xssFilterProperties() {
        return new XssFilterProperties();
    }
    /**
     * 注册XSS过滤器。
     * @return FilterRegistrationBean 用于注册过滤器的bean。
     */
    @Bean
    public FilterRegistrationBean<XssFilter> xssFilterRegistration(XssFilterProperties xssFilterProperties) {
        FilterRegistrationBean<XssFilter> registrationBean = new FilterRegistrationBean<>();
        // 设置过滤器的分发类型为请求类型
        registrationBean.setDispatcherTypes(DispatcherType.REQUEST);
        // 创建XssFilter的实例
        registrationBean.setFilter(new XssFilter(xssFilterProperties));
        // 添加过滤器需要拦截的URL模式，这里拦截所有请求
        registrationBean.addUrlPatterns("/*");
        // 设置过滤器的名称
        registrationBean.setName("XssFilter");
        // 设置过滤器的执行顺序，数值越小，优先级越高
        registrationBean.setOrder(3);
        return registrationBean;
    }
    @Bean
    public HttpMessageConverters xssHttpMessageConverters() {
        XSSMappingJackson2HttpMessageConverter xssMappingJackson2HttpMessageConverter = new XSSMappingJackson2HttpMessageConverter();
        HttpMessageConverter converter = xssMappingJackson2HttpMessageConverter;
        return new HttpMessageConverters(converter);
    }
}

