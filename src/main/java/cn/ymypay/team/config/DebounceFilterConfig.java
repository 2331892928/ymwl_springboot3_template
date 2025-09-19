package cn.ymypay.team.config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DebounceFilterConfig {
    @Value("${debounce.enabled}")
    private Boolean debounceEnabled;
    @Value("${debounce.time}")
    private Integer debounceTime;
    @Bean
    public FilterRegistrationBean<DebounceFilter> debounceFilterRegistration() {
        FilterRegistrationBean<DebounceFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(new DebounceFilter(debounceEnabled, debounceTime));
        bean.addUrlPatterns("/*"); // 拦截所有请求
        bean.setOrder(1); // 设置优先级
        return bean;
    }
}
