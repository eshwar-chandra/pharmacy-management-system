package com.pharmacy.management.common.config; // Updated package

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.core.annotation.Order; // To ensure it runs early

import java.io.IOException;
import java.util.UUID;

@Component
@Order(1) // Ensure this filter runs early in the chain
public class MdcLoggingFilter implements Filter {

    private static final String REQUEST_ID_HEADER = "X-Request-ID";
    private static final String MDC_REQUEST_ID_KEY = "requestId";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        try {
            String requestId = null;
            if (request instanceof HttpServletRequest) {
                requestId = ((HttpServletRequest) request).getHeader(REQUEST_ID_HEADER);
            }
            if (requestId == null || requestId.isEmpty()) {
                requestId = UUID.randomUUID().toString().replace("-", "");
            }
            MDC.put(MDC_REQUEST_ID_KEY, requestId);
            chain.doFilter(request, response);
        } finally {
            MDC.remove(MDC_REQUEST_ID_KEY);
        }
    }
}
