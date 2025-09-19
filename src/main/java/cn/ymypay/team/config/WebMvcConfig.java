package cn.ymypay.team.config;

import cn.dev33.satoken.SaManager;
import cn.dev33.satoken.context.SaHolder;
import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.filter.SaServletFilter;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.util.SaResult;
import cn.ymypay.team.common.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lvjinfa
 * @date 2025/6/1 下午7:14
 * @description: web配置类
 */
@Configuration
@Slf4j
public class WebMvcConfig implements WebMvcConfigurer {
    // 注册拦截器
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册 Sa-Token 拦截器，校验规则为 StpUtil.checkLogin() 登录校验。
        log.info("注册鉴权拦截器");
        registry.addInterceptor(new SaInterceptor(handle -> StpUtil.checkLogin()))
                .addPathPatterns("/**")
                .excludePathPatterns("/api/user/getWxQrcode")
                .excludePathPatterns("/api/user/checkWxLogin/**")
                .excludePathPatterns("/api/user/getUserInfoForCode/**")
                .excludePathPatterns("/api/user/pay/**")
                .excludePathPatterns("/api/test1");
    }
//    /**
//     * 注册 [Sa-Token 全局过滤器]
//     */
//    @Bean
//    public SaServletFilter getSaServletFilter() {
//        return new SaServletFilter()
//
//                // 指定 [拦截路由] 与 [放行路由]
//                .addInclude("/**")
//                .addExclude("/api/test1")
//
//                // 认证函数: 每次请求执行
//                .setAuth(obj -> {
//                    // 输出 API 请求日志，方便调试代码
////                     SaManager.getLog().debug("----- 请求path={}  提交token={}", SaHolder.getRequest().getRequestPath(), StpUtil.getTokenValue());
//                    StpUtil.checkLogin();
//                })
//
//                // 异常处理函数：每次认证函数发生异常时执行此函数
//                .setError(e -> {
//                    return Result.fail(401, "未登录");
//                });
//    }
}
