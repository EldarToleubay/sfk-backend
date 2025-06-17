package com.alibou.security.drug;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/drugs")
@CrossOrigin(origins = "*")
public class DrugController {

    private final DrugService drugService;
    private final ImportService importService;
    private final DrugRepository drugRepository;
    private final ReferenceService referenceService;


    public DrugController(DrugService drugService, ImportService importService, DrugRepository drugRepository, ReferenceService referenceService) {
        this.drugService = drugService;
        this.importService = importService;
        this.drugRepository = drugRepository;
        this.referenceService = referenceService;
    }


    @DeleteMapping("/remove")
    @Transactional
    public String removeAllDrugs() {
        drugRepository.deleteAllFast();
        return "Drugs removed";
    }


    @GetMapping
    public Page<Drug> getAllDrugs(
            @RequestParam(required = false) String inn,
            @RequestParam(required = false) String segment,
            @RequestParam(required = false) String tradeName,
            @RequestParam(required = false) String manufacturingCompany,
            @RequestParam(required = false) String drugForm,
            @RequestParam(required = false) String dosage,
            @RequestParam(required = false) String packQuantity,
            @RequestParam(required = false) String atc1,
            @RequestParam(required = false) String atc2,
            @RequestParam(required = false) String atc3,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return drugService.fetchAllWithFilters(
                inn, segment, tradeName, manufacturingCompany,
                drugForm, dosage, packQuantity, atc1, atc2, atc3,
                page, size
        );
    }



    @GetMapping("/count")
    public long count() {
        return drugRepository.count();
    }


    @PostMapping("/upload-async")
    public ResponseEntity<String> uploadFileAsync(@RequestParam("file") MultipartFile file) throws IOException {

        byte[] fileBytes = file.getBytes(); // читаем в память

        CompletableFuture.runAsync(() -> {
            try (InputStream inputStream = new ByteArrayInputStream(fileBytes)) {
                importService.importDrugsAsync(inputStream);
            } catch (Exception e) {
                e.printStackTrace(); // или логгировать
            }
        });

        return ResponseEntity.accepted().body("Файл принят. Импорт выполняется в фоне.");
    }


}

