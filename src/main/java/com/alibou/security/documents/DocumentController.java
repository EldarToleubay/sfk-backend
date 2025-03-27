package com.alibou.security.documents;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("api/v1/documents")
@CrossOrigin(origins = "https://skffront.netlify.app")
public class DocumentController {

    private final DocumentService documentService;

    public DocumentController(DocumentService documentService) {
        this.documentService = documentService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<DocumentEntity> uploadDocument(
            @RequestParam String title,
            @RequestParam DocumentType documentType,
            @RequestParam MultipartFile file) throws Exception {
        return ResponseEntity.ok(documentService.saveDocument(title, documentType, file));
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id) throws Exception {
        byte[] fileData = documentService.getDocument(id);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"document\"")
                .body(fileData);
    }

    @GetMapping("/all")
    public List<DocumentEntity> getDocuments(@RequestParam(required = false) DocumentType documentType) {
        if (documentType == null) {
            return documentService.getDocumentsByType(documentType);
        }
        return documentService.getDocumentsByType(documentType);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) throws Exception {
        documentService.deleteDocument(id);
        return ResponseEntity.noContent().build();
    }
}
