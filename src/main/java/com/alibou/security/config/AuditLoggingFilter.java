package com.alibou.security.config;

import com.alibou.security.auditing.AuditLogService;
import com.alibou.security.util.CachedBodyHttpServletRequest;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.BufferedReader;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class AuditLoggingFilter extends OncePerRequestFilter {

    private final AuditLogService auditLogService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        StringBuilder bodyBuilder = new StringBuilder();
        String contentType = request.getContentType();

        boolean isMultipart = contentType != null && contentType.toLowerCase().startsWith("multipart/");

        if (!isMultipart && ("POST".equalsIgnoreCase(request.getMethod()) || "PUT".equalsIgnoreCase(request.getMethod()))) {
            request = new CachedBodyHttpServletRequest(request);
            try (BufferedReader reader = request.getReader()) {
                String line;
                while ((line = reader.readLine()) != null) {
                    bodyBuilder.append(line);
                }
            }
        }

        Exception exception = null;

        try {
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            exception = ex;
            throw ex;
        } finally {
            auditLogService.logRequest(request, response, bodyBuilder.toString(), exception);
        }
    }

}
