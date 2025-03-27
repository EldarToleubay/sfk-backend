package com.alibou.security.documents;

import com.alibou.security.dto.response.DocumentResponse;
import com.alibou.security.minio.MinioService;
import com.google.common.net.HttpHeaders;
import io.minio.StatObjectResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final MinioService minioService;
    private static final Set<String> ALLOWED_TYPES = Set.of("application/pdf", "image/png", "image/jpeg");


    public DocumentEntity saveDocument(String title, DocumentType documentType, MultipartFile file) throws Exception {
        validateFile(file);

        String filePath = minioService.uploadFile(title, file); // Загружаем в MinIO

        DocumentEntity document = new DocumentEntity();
        document.setTitle(title);
        document.setFileName(file.getOriginalFilename());
        document.setDocumentType(documentType);
        document.setFilePath(filePath);
        document.setFileType(file.getContentType());

        return documentRepository.save(document);
    }


    public ResponseEntity<byte[]> getDocument(Long id) throws Exception {
        DocumentEntity document = documentRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Документ с id {} не найден", id);
                    return new RuntimeException("Документ не найден");
                });

        // Получаем метаданные файла
        StatObjectResponse stat = minioService.getFileMetadata(document.getFilePath());
        String contentType = stat.contentType(); // Получаем Content-Type

        // Читаем сам файл
        byte[] fileBytes;
        try (InputStream inputStream = minioService.getFile(document.getFilePath())) {
            fileBytes = inputStream.readAllBytes();
        }

        // Возвращаем файл с Content-Type
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + document.getFileName() + "\"")
                .body(fileBytes);
    }



    public void deleteDocument(Long id) throws Exception {
        DocumentEntity document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Документ не найден"));

        minioService.deleteFile(document.getFilePath()); // Удаляем из MinIO
        documentRepository.deleteById(id);
    }

    private void validateFile(MultipartFile file) {
        if (!ALLOWED_TYPES.contains(file.getContentType())) {
            throw new IllegalArgumentException("Разрешены только файлы: " + ALLOWED_TYPES);
        }
    }

    public List<DocumentEntity> getDocumentsByType(DocumentType documentType) {
        return documentRepository.findByDocumentType(documentType);
    }

    public DocumentEntity updateDocumentTitle(Long id, String newTitle) {
        DocumentEntity document = documentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Документ не найден"));

        document.setTitle(newTitle);
        return documentRepository.save(document);
    }

    public List<DocumentEntity> getDocuments() {
        return documentRepository.findAll();
    }
}
