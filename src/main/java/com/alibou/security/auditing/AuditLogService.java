package com.alibou.security.auditing;

import com.alibou.security.config.JwtService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final JwtService jwtService;



    public Page<AuditLogDto> search(String username,
                                    String method,
                                    String endpoint,
                                    Integer statusCode,
                                    LocalDateTime from,
                                    LocalDateTime to,
                                    Pageable pageable) {
        return auditLogRepository.search(username, method, endpoint, statusCode, from, to, pageable)
                .map(this::toDto);
    }

    private AuditLogDto toDto(AuditLog log) {
        return new AuditLogDto(
                log.getId(),
                log.getUsername(),
                log.getMethod(),
                log.getEndpoint(),
                log.getStatusCode(),
                log.getTimestamp()
        );
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
                .statusCode(response.getStatus())
                .timestamp(LocalDateTime.now())
                .build();

        auditLogRepository.save(log);
    }
}
