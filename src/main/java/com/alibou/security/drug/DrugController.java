package com.alibou.security.drug;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/drugs")
@CrossOrigin(origins = "*")
public class DrugController {

    private final DrugService drugService;
    private final DrugRepository drugRepository;
    private final ReferenceService referenceService;
    private final ImportProgressTracker progressTracker;


    public DrugController(DrugService drugService, DrugRepository drugRepository, ReferenceService referenceService, ImportProgressTracker progressTracker) {
        this.drugService = drugService;
        this.drugRepository = drugRepository;
        this.referenceService = referenceService;
        this.progressTracker = progressTracker;
    }
//
//    @GetMapping
//    public List<Drug> getAllDrugs() {
//        return drugService.getAll();
//    }


    @GetMapping("/count")
    public long count() {
        return drugRepository.count();
    }


    @PostMapping("/upload-async")
    public ResponseEntity<String> uploadFileAsync(@RequestParam("file") MultipartFile file) {
        CompletableFuture.runAsync(() -> {
            try {
                drugService.importExcel(file);
                referenceService.setAll();
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

    @PostMapping("/async-upolad2")
    public ResponseEntity<String> importExcel(@RequestParam("file") MultipartFile file) {
        drugService.importExcelAsync(file); // Асинхронно
        return ResponseEntity.accepted().body("Импорт запущен.");
    }

    @GetMapping("/progress")
    public ResponseEntity<ImportProgressResponse> getProgress() {
        return ResponseEntity.ok(new ImportProgressResponse(
                progressTracker.getProgress(),
                progressTracker.isCompleted()
        ));
    }

    record ImportProgressResponse(int progress, boolean completed) {}


}

