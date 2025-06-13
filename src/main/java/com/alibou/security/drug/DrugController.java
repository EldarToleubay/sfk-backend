package com.alibou.security.drug;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/drugs")
@CrossOrigin(origins = "*")
public class DrugController {

    private final DrugService drugService;
    private final ReferenceService referenceService;

    public DrugController(DrugService drugService, ReferenceService referenceService) {
        this.drugService = drugService;
        this.referenceService = referenceService;
    }
//
//    @GetMapping
//    public List<Drug> getAllDrugs() {
//        return drugService.getAll();
//    }



    @PostMapping("/upload-async")
    public ResponseEntity<String> uploadFileAsync(@RequestParam("file") MultipartFile file) {
        CompletableFuture.runAsync(() -> {
            try {
                drugService.importExcel(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return ResponseEntity.accepted().body("Файл принят. Импорт выполняется в фоне.");
    }


    @PostMapping("/upload")
    @Operation(summary = "Загрузка Excel-файла", description = "Загружает Excel-файл и сохраняет данные")
    public ResponseEntity<String> uploadFile(
            @Parameter(description = "Excel файл", required = true)
            @RequestParam("file") MultipartFile file) {
        try {
            drugService.importExcel(file);
            referenceService.setAll();
            return ResponseEntity.ok("Файл успешно обработан и данные сохранены");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Ошибка при обработке файла: " + e.getMessage());
        }
    }


}

