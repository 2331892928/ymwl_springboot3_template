package cn.ymypay.team.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {
    @Value("${aes.key}")
    private String aesKey;
    @Value("${aes.enabled}")
    private boolean aesEnabled;
    @Value("${iv}")
    private String iv;
    @Value("${sign.key}")
    private String signKey;
    @Value("${controller.time}")
    private Integer controllerTime;
    @Bean
    public FilterRegistrationBean<EncryptionFilter> encryptionFilterRegistration() {
        FilterRegistrationBean<EncryptionFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new EncryptionFilter(aesKey,aesEnabled,iv,signKey,controllerTime));
        bean.addUrlPatterns("/*"); // 拦截所有请求
        bean.setOrder(2);
        return bean;
    }
}
