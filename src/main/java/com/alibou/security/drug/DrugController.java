package com.alibou.security.drug;

import jakarta.transaction.Transactional;
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


    @PostMapping("/filter")
    public List<Drug> getFilteredDrugs(@RequestBody DrugFilterRequest request) {
        return drugService.fetchAllWithFilters(request);
    }


    @GetMapping("/count")
    public long count() {
        return drugRepository.count();
    }

//    @GetMapping("/top-companies")
//    public List<NameValueDto> getTopCompanies(
//            @RequestParam(defaultValue = "usd") String currency
//    ) {
//        return drugService.getTopCompanies(currency);
//    }

    @PostMapping("/top-companies")
    public List<NameValueDto> getTopCompanies(@RequestBody DrugFilterRequest request,
                                              @RequestParam(defaultValue = "usd") String currency
    ) {
        return drugService.getTopCompaniesFiltered(request, currency);
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

