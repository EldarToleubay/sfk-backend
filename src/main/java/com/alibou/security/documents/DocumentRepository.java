package com.alibou.security.documents;

import com.alibou.security.dto.response.DocumentResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentRepository extends JpaRepository<DocumentEntity, Long> {

    List<DocumentResponse> findByDocumentType(DocumentType documentType);

    List<DocumentResponse> findAllBy();

}

