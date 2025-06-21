package com.alibou.security.auditing;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface AuditLogRepositoryCustom {
    Page<AuditLog> search(String username, String method, String endpoint, Integer statusCode,
                          LocalDateTime from, LocalDateTime to, Pageable pageable);
}
