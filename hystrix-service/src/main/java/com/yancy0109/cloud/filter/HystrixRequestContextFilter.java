package com.yancy0109.cloud.filter;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;

/**
 * HystrixRequestContext初始化
 * 通过过滤器在每个请求前后初始化和关闭HystrixRequestContext
 * 否则使用Cache会出现异常
 * @author yancy0109
 */
@Component
@WebFilter(value = "/*",asyncSupported = true)
public class HystrixRequestContextFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HystrixRequestContext context = HystrixRequestContext.initializeContext();
        try {
            filterChain.doFilter(servletRequest,servletResponse);
        } finally {
            context.close();
        }
    }
}
