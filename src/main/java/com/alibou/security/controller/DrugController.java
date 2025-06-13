package com.alibou.security.controller;

import com.alibou.security.entity.Drug;
import com.alibou.security.service.DrugService;
import com.alibou.security.service.ReferenceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/drugs")
public class DrugController {

    private final DrugService drugService;
    private final ReferenceService referenceService;

    public DrugController(DrugService drugService, ReferenceService referenceService) {
        this.drugService = drugService;
        this.referenceService = referenceService;
    }

    @GetMapping
    public List<Drug> getAllDrugs() {
        return drugService.getAll();
    }


    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            drugService.importExcel(file);
            referenceService.setAll();
            return ResponseEntity.ok("Файл успешно обработан и данные сохранены");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка при обработке файла: " + e.getMessage());
        }
    }

}

