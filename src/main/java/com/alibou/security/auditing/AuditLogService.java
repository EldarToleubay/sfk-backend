package com.alibou.security.auditing;

import com.alibou.security.config.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final JwtService jwtService;


    public List<AuditLog> getAll() {
        return auditLogRepository.findAll();
    }

    public void logRequest(HttpServletRequest request,
                           HttpServletResponse response,
                           String body,
                           Exception exception) {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = "anonymous";
        Long userId = null;
        String role = "NONE";

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                token = authHeader.substring(7);
                Claims claims = jwtService.extractAllClaims(token);
                username = claims.getSubject(); // "sub"
                userId = claims.get("id", Integer.class).longValue(); // "id"
                role = claims.get("role", String.class); // "role"
            } catch (Exception e) {
                // логируем как гость
            }
        }

        AuditLog log = AuditLog.builder()
                .userId(userId)
                .username(username)
                .role(role)
                .method(request.getMethod())
                .endpoint(request.getRequestURI())
                .ipAddress(request.getRemoteAddr())
                .requestBody(body)
                .statusCode(response.getStatus())
                .timestamp(LocalDateTime.now())
                .errorMessage(exception != null ? exception.getMessage() : null)
                .build();

        auditLogRepository.save(log);
    }
}
