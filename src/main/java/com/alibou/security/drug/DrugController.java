package com.alibou.security.drug;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/drugs")
@CrossOrigin(origins = "*")
public class DrugController {

    private final DrugService drugService;
    private final DrugRepository drugRepository;
    private final ReferenceService referenceService;


    public DrugController(DrugService drugService, DrugRepository drugRepository, ReferenceService referenceService) {
        this.drugService = drugService;
        this.drugRepository = drugRepository;
        this.referenceService = referenceService;
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
    public ResponseEntity<String> uploadFileAsync(@RequestParam("file") MultipartFile file) throws IOException {

        byte[] fileBytes = file.getBytes(); // читаем в память

        CompletableFuture.runAsync(() -> {
            try (InputStream inputStream = new ByteArrayInputStream(fileBytes)) {
                drugService.removeAllDrugs();
                drugService.importExcel(inputStream); // передаём стрим
                referenceService.setAll();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        return ResponseEntity.accepted().body("Файл принят. Импорт выполняется в фоне.");
    }

//    @PostMapping("/upload")
//    @Operation(summary = "Загрузка Excel-файла", description = "Загружает Excel-файл и сохраняет данные")
//    public ResponseEntity<String> uploadFile(
//            @Parameter(description = "Excel файл", required = true)
//            @RequestParam("file") MultipartFile file) {
//        try {
//            drugService.importExcel(file);
//            referenceService.setAll();
//            return ResponseEntity.ok("Файл успешно обработан и данные сохранены");
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().body("Ошибка при обработке файла: " + e.getMessage());
//        }
//    }

}

