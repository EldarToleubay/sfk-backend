package com.alibou.security.dto.response;

import com.alibou.security.documents.DocumentType;

import java.time.LocalDateTime;

public interface  DocumentResponse {
    Long getId();
    String getTitle();
    String getFileName();
    LocalDateTime getUploadDate();
    DocumentType getDocumentType();
}
