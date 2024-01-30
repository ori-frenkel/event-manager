package com.eventmanager.eventmanagercrudapi.unit;

import com.eventmanager.eventmanagercrudapi.interceptors.RateLimitInterceptor;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RateLimitInterceptorTest {

    @Test
    public void testRateLimiting() throws Exception {
        // Set a low rate limit for testing purposes
        double rateLimit = 1;

        HandlerInterceptor rateLimitInterceptor = new RateLimitInterceptor(rateLimit);

        MockHttpServletRequest request = new MockHttpServletRequest();

        // First request should be allowed
        MockHttpServletResponse response1 = new MockHttpServletResponse();
        boolean firstRequestAllowed = rateLimitInterceptor.preHandle(request, response1, new Object());
        assertEquals(true, firstRequestAllowed);
        assertEquals(HttpStatus.OK.value(), response1.getStatus());

        // Second request within the same second should be blocked
        MockHttpServletResponse response2 = new MockHttpServletResponse();
        boolean secondRequestAllowed = rateLimitInterceptor.preHandle(request, response2, new Object());
        assertEquals(false, secondRequestAllowed);
        assertEquals(HttpStatus.TOO_MANY_REQUESTS.value(), response2.getStatus());

        // Sleep for 2 seconds to allow the rate limiter to reset

        Thread.sleep(2000);

        // Third request after the reset should be allowed
        MockHttpServletResponse response3 = new MockHttpServletResponse();
        boolean thirdRequestAllowed = rateLimitInterceptor.preHandle(request, response3, new Object());
        assertEquals(true, thirdRequestAllowed);
        assertEquals(HttpStatus.OK.value(), response3.getStatus());
    }
}


