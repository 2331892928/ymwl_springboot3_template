package cn.ymypay.team.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class XssFilter implements Filter {

    private final XssFilterProperties xssFilterProperties;
    public XssFilter(XssFilterProperties xssFilterProperties) {
        this.xssFilterProperties = xssFilterProperties;
    }
    /**
     * 执行过滤逻辑，如果当前请求不在排除列表中，则通过XSS过滤器包装请求。
     * @param request  HTTP请求对象。
     * @param response HTTP响应对象。
     * @param chain    过滤器链对象，用于继续或中断请求处理。
     * @throws IOException      如果处理过程中出现I/O错误。
     * @throws ServletException 如果处理过程中出现Servlet相关错误。
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse resp = (HttpServletResponse) response;
        //如果该访问接口在排除列表里面则不拦截
        if (isExcludeUrl(req.getServletPath())) {
            chain.doFilter(request, response);
            return;
        }
//        log.info("uri:{}", req.getRequestURI());
        // xss 过滤
        chain.doFilter(new XssWrapper(req), resp);
    }
    /**
     * 判断当前请求的URL是否应该被排除在XSS过滤之外。
     *
     * @param urlPath 请求的URL路径。
     * @return 如果请求应该被排除，则返回true；否则返回false。
     */
    private boolean isExcludeUrl(String urlPath) {
        if (!xssFilterProperties.isEnabled()) {
            //如果xss开关关闭了，则所有url都不拦截
            return true;
        }
        // 白名单是空的 放行
        if(CollectionUtils.isEmpty(xssFilterProperties.getExcludeUrlList())) {
            return true;
        }
        for (String pattern : xssFilterProperties.getExcludeUrlList()) {
            Pattern p = Pattern.compile("^" + pattern);
            Matcher m = p.matcher(urlPath);
            if (m.find()) {
                return true;
            }
        }
        return false;
    }
}

