package com.autofocus.pms.hq.config.logger.common;


import com.autofocus.pms.common.util.PathUtils;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.util.ContentCachingRequestWrapper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class LoggingFilter implements Filter {

    public static final String[] NONE_SECURE_URLs = {
    };

    private static final List<String> EXCLUDE_URLs = Arrays.asList(NONE_SECURE_URLs);


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain chain)
            throws IOException, ServletException {
        if (servletRequest instanceof HttpServletRequest && servletResponse instanceof HttpServletResponse) {

             HttpServletRequest request = (HttpServletRequest) servletRequest;
             //HttpServletResponse response = (HttpServletResponse) servletResponse;

            if (PathUtils.matches(EXCLUDE_URLs, ((HttpServletRequest) servletRequest).getRequestURI())) {
                chain.doFilter(servletRequest, servletResponse);
                return;
            }

            // Logging 에서 기록되지 않은 경우를 방지 (참고 : https://devnm.tistory.com/30 )
            HttpServletRequest requestToCache = new ContentCachingRequestWrapper(request);
//          HttpServletResponse responseToCache = new ContentCachingResponseWrapper(response);

            chain.doFilter(requestToCache, servletResponse);
        } else {
            chain.doFilter(servletRequest, servletResponse);
        }


    }
}
