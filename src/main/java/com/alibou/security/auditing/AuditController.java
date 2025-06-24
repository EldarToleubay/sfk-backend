package com.alibou.security.auditing;


import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/audit")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuditController {
    private final AuditLogService auditLogService;

    @GetMapping
    public Page<AuditLogDto> getAuditLogs(
            @RequestParam(required = false) String username,
            @RequestParam(required = false) String method,
            @RequestParam(required = false) String endpoint,
            @RequestParam(required = false) Integer statusCode,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime to,
            Pageable pageable
    ) {
        return auditLogService.search(username, method, endpoint, statusCode, from, to, pageable);
    }

}
