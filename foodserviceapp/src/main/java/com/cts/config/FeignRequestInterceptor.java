package com.cts.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;


@Component
public class FeignRequestInterceptor implements RequestInterceptor {

    private static final String HEADER_USER_EMAIL = "X-User-Email";
    private static final String HEADER_USER_ID = "X-User-Id";
    private static final String HEADER_USER_ROLE = "X-User-Role";

    @Override
    public void apply(RequestTemplate template) {
        HttpServletRequest request = getCurrentHttpRequest();

        if (request != null) {
          
            String userEmail = request.getHeader(HEADER_USER_EMAIL);
            String userId = request.getHeader(HEADER_USER_ID);
            String userRole = request.getHeader(HEADER_USER_ROLE);
            
            if (userEmail != null) {
                template.header(HEADER_USER_EMAIL, userEmail);
            }
            if (userId != null) {
                template.header(HEADER_USER_ID, userId);
            }
            if (userRole != null) {
                template.header(HEADER_USER_ROLE, userRole);
            }
        }
    }

    private HttpServletRequest getCurrentHttpRequest() {
        if (RequestContextHolder.getRequestAttributes() != null) {
            return ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        }
        return null;
    }
}