package com.tenpo.interceptor;

import com.tenpo.repositoryredis.RedisRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@Order(1)
public class RateLimitInterceptor implements HandlerInterceptor {

    @Value("${requestLimit}")
    private int requestLimit;

    @Autowired
    private RedisRepository redisRepository;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String clientIp = request.getRemoteAddr();
        if (!request.getRequestURI().contains("/v1") || redisRepository.isAllowed(clientIp, requestLimit)) {
            return true;
        } else {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("Too many requests - Try again later");
            return false;
        }
    }
}
