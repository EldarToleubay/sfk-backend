package com.alibou.security.minio;

import com.google.common.net.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequestMapping("/files")
public class MinioController {

    private final MinioService minioService;

    public MinioController(MinioService minioService) {
        this.minioService = minioService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            minioService.uploadFile(file.getOriginalFilename(), file);
            return ResponseEntity.ok("Файл загружен: " + file.getOriginalFilename());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Ошибка загрузки файла: " + e.getMessage());
        }
    }

    @GetMapping("/download/{filename}")
    public ResponseEntity<InputStream> downloadFile(@PathVariable String filename) {
        try {
            return ResponseEntity.ok(minioService.getFile(filename));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(null);
        }
    }

    @DeleteMapping("/delete/{filename}")
    public ResponseEntity<String> deleteFile(@PathVariable String filename) {
        try {
            minioService.deleteFile(filename);
            return ResponseEntity.ok("Файл удален: " + filename);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Ошибка удаления файла: " + e.getMessage());
        }
    }

    @GetMapping("/download-file/{fileName}")
    public ResponseEntity<byte[]> downloadFileMinio(@PathVariable String fileName) {
        try {
            InputStream stream = minioService.getFile(fileName);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(stream.readAllBytes());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(("Ошибка скачивания: " + e.getMessage()).getBytes());
        }
    }
}
