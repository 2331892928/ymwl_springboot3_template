package cn.ymypay.team.config;

import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.reactor.context.SaReactorSyncHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

/**
 * @author 湮灭网络工作室——amen
 * @date 2025/9/19 上午12:55
 * @description: satoken前置过滤器，如果是用springboot拦截器，就需要这个。如果是satoken自带的拦截器（一般异步），就不需要这个
 */
@Component
public class SaTokenFilter implements WebFilter {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        try {
            // 先 set 上下文，再调用 Sa-Token 同步 API，并在 finally 里清除上下文
            SaReactorSyncHolder.setContext(exchange);
        }
        finally {
            SaReactorSyncHolder.clearContext();
        }
        return chain.filter(exchange);
    }
}
