package com.eventmanager.eventmanagercrudapi.interceptors;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RateLimitInterceptor implements HandlerInterceptor {
    private final double DEFAULT_NUMBER_OF_REQUESTS_PER_SECOND = 10.0;
    private final RateLimiter rateLimiter;

    public RateLimitInterceptor() {
        this.rateLimiter = RateLimiter.create(DEFAULT_NUMBER_OF_REQUESTS_PER_SECOND);
    }

    public RateLimitInterceptor(double requestsPerSecond) {
        this.rateLimiter = RateLimiter.create(requestsPerSecond);
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (rateLimiter.tryAcquire()) {
            return true; // Allow the request to proceed
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.getWriter().write("Too many requests. Please try again later.");
            return false; // Block the request
        }
    }
}
