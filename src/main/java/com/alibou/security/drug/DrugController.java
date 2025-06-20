package com.alibou.security.drug;

import com.google.common.net.HttpHeaders;
import jakarta.transaction.Transactional;
import org.springframework.http.MediaType;
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
    private final DrugExcelExportService excelExportService;
    private final DrugExcelExportCustomService drugExcelExportCustomService;


    public DrugController(DrugService drugService, ImportService importService, DrugRepository drugRepository, DrugExcelExportService excelExportService, DrugExcelExportCustomService drugExcelExportCustomService) {
        this.drugService = drugService;
        this.importService = importService;
        this.drugRepository = drugRepository;
        this.excelExportService = excelExportService;
        this.drugExcelExportCustomService = drugExcelExportCustomService;
    }

    @PostMapping("/export-custom")
    public ResponseEntity<byte[]> exportCustom(@RequestBody DrugExcelExportRequestDTO request) throws IOException {
        List<DrugExportDto> data = drugService.fetchAllWithFilters(request.getFilter());
        byte[] file = drugExcelExportCustomService.exportToExcel(data, request.getColumns());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=filtered_drugs.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
    }


    @DeleteMapping("/remove")
    @Transactional
    public String removeAllDrugs() {
        drugRepository.deleteAllFast();
        return "Drugs removed";
    }


    @PostMapping("/filter")
    public List<DrugExportDto> getFilteredDrugs(@RequestBody DrugFilterRequest request) {
        return drugService.fetchAllWithFilters(request);
    }


    @PostMapping("/export")
    public ResponseEntity<byte[]> export(@RequestBody DrugFilterRequest request) throws IOException {
        List<DrugExportDto> drugs = drugService.fetchAllWithFilters(request);
        byte[] file = excelExportService.exportToExcel(drugs);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=drugs.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);
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
    @PostMapping("/top-molecules")
    public List<NameValueDto> topMolecules(@RequestBody DrugFilterRequest request, @RequestParam String metric) {
        return drugService.getTopMolecules(request, metric);
    }

    @PostMapping("/top-products")
    public List<NameValueDto> topProducts(@RequestBody DrugFilterRequest request, @RequestParam String metric) {
        return drugService.getTopProducts(request, metric);
    }

    @PostMapping("/top-companies")
    public List<NameValueDto> topCompanies(@RequestBody DrugFilterRequest request, @RequestParam String metric) {
        return drugService.getTopCompanies(request, metric);
    }

    @PostMapping("/top-atc1")
    public List<NameValueDto> topAtc1(@RequestBody DrugFilterRequest request, @RequestParam String metric) {
        return drugService.getTopAtc1(request, metric);
    }


    @PostMapping("/top-segment")
    public List<NameValueDto> topSegment(@RequestBody DrugFilterRequest request, @RequestParam String metric) {
        return drugService.getTopSegment(request, metric);
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

