package com.alibou.security.auditing;

import java.time.LocalDateTime;

public record AuditLogDto(
        Long id,
        String username,
        String method,
        String endpoint,
        int statusCode,
        LocalDateTime timestamp
) {}
