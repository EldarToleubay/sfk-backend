package com.alibou.security.drug;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DrugService {

    private final DrugRepository drugRepository;


    @Transactional
    public void removeAllDrugs() {
        drugRepository.deleteAllFast();
        log.info("Drugs removed");
    }


    private String getCellAsString(Cell cell) {
        if (cell == null) return null;

        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    yield cell.getDateCellValue().toString();
                }
                double val = cell.getNumericCellValue();
                yield (val == Math.floor(val)) ? String.valueOf((long) val) : String.valueOf(val);
            }
            case BOOLEAN -> String.valueOf(cell.getBooleanCellValue());
            case FORMULA -> cell.getCellFormula();
            case BLANK -> "";
            default -> "";
        };
    }

    private static final int BATCH_SIZE = 1000;

    //    public void importExcel(MultipartFile file) throws Exception {
    public void importExcel(InputStream inputStream) throws Exception {

        try (Workbook workbook = new XSSFWorkbook(inputStream)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            if (rows.hasNext()) rows.next(); // пропускаем заголовок

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            int rowNum = 1;

            List<Drug> batch = new ArrayList<>();
            List<String> errors = new ArrayList<>();

            while (rows.hasNext()) {
                Row row = rows.next();
                rowNum++;

                try {
                    Drug drug = new Drug();

                    drug.setYear(getIntCell(row, 0, rowNum, "year"));
                    drug.setSegment(getStringCell(row, 1, rowNum, "segment"));
                    drug.setTradeName(getStringCell(row, 2, rowNum, "tradeName"));
                    drug.setManufacturingCompany(getStringCell(row, 3, rowNum, "manufacturingCompany"));
                    drug.setPersonWithTradingLicense(getStringCell(row, 4, rowNum, "personWithTradingLicense"));
                    drug.setPersonInterestedInRegistrationGeorgiaStand(getStringCell(row, 5, rowNum, "personInterestedInRegistrationGeorgiaStand"));
                    drug.setInterestedParty(getStringCell(row, 6, rowNum, "interestedParty"));
                    drug.setRxOtc(getStringCell(row, 7, rowNum, "rxOtc"));
                    drug.setModeOfRegistration(getStringCell(row, 8, rowNum, "modeOfRegistration"));
                    drug.setSku(getStringCell(row, 9, rowNum, "sku"));
                    drug.setDrugForm(getStringCell(row, 10, rowNum, "drugForm"));
                    drug.setDosage(getStringCell(row, 11, rowNum, "dosage"));
                    drug.setPackQuantity(getStringCell(row, 12, rowNum, "packQuantity"));
                    drug.setInn(getStringCell(row, 13, rowNum, "inn"));
                    drug.setAtc1(getStringCell(row, 14, rowNum, "atc1"));
                    drug.setAtc2(getStringCell(row, 15, rowNum, "atc2"));
                    drug.setAtc3(getStringCell(row, 16, rowNum, "atc3"));
                    drug.setVolumeInUnits(getBigDecimalCell(row, 17, rowNum, "volumeInUnits"));
                    drug.setPricePerUnitLari(getBigDecimalCell(row, 18, rowNum, "pricePerUnitLari"));
                    drug.setPricePerUnitUsd(getBigDecimalCell(row, 19, rowNum, "pricePerUnitUsd"));
                    drug.setValueInGel(getBigDecimalCell(row, 20, rowNum, "valueInGel"));
                    drug.setValueInUsd(getBigDecimalCell(row, 21, rowNum, "valueInUsd"));

                    Cell dateCell = row.getCell(22);
                    if (dateCell != null) {
                        if (dateCell.getCellType() == CellType.NUMERIC) {
                            drug.setImportDate(dateCell.getLocalDateTimeCellValue().toLocalDate());
                        } else if (dateCell.getCellType() == CellType.STRING) {
                            drug.setImportDate(LocalDate.parse(dateCell.getStringCellValue(), formatter));
                        }
                    }

                    drug.setPriceSource(getStringCell(row, 23, rowNum, "priceSource"));

                    batch.add(drug);

                    if (batch.size() == BATCH_SIZE) {
                        drugRepository.saveAll(batch);
                        batch.clear();
                    }

                } catch (Exception e) {
                    errors.add("Ошибка в строке " + rowNum + ": " + e.getMessage());
                }
            }

            // Сохраняем оставшиеся записи
            if (!batch.isEmpty()) {
                drugRepository.saveAll(batch);
            }

            System.out.println("Импорт завершён. Ошибок: " + errors.size());
            errors.forEach(System.out::println);
        }
    }

    private int getIntCell(Row row, int index, int rowNum, String field) {
        try {
            Cell cell = row.getCell(index);
            return (int) cell.getNumericCellValue();
        } catch (Exception e) {
            throw new RuntimeException("Ошибка в поле [" + field + "] (строка " + rowNum + "): " + e.getMessage());
        }
    }

    private String getStringCell(Row row, int index, int rowNum, String field) {
        try {
            return getCellAsString(row.getCell(index));
        } catch (Exception e) {
            throw new RuntimeException("Ошибка в поле [" + field + "] (строка " + rowNum + "): " + e.getMessage());
        }
    }

    private BigDecimal getBigDecimalCell(Row row, int index, int rowNum, String field) {
        try {
            Cell cell = row.getCell(index);
            return BigDecimal.valueOf(cell.getNumericCellValue());
        } catch (Exception e) {
            throw new RuntimeException("Ошибка в поле [" + field + "] (строка " + rowNum + "): " + e.getMessage());
        }
    }

    public Page<Drug> fetchAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return drugRepository.findAll(pageRequest);
    }

    public Page<Drug> fetchAllWithFilters(
            List<String> inn,
            List<String> segment,
            List<String> tradeName,
            List<String> manufacturingCompany,
            List<String> drugForm,
            List<String> dosage,
            List<String> packQuantity,
            List<String> atc1,
            List<String> atc2,
            List<String> atc3,
            int page,
            int size
    ) {
        Specification<Drug> spec = Specification.where(null);

        spec = addListFilter(spec, inn, "inn");
        spec = addListFilter(spec, segment, "segment");
        spec = addListFilter(spec, tradeName, "tradeName");
        spec = addListFilter(spec, manufacturingCompany, "manufacturingCompany");
        spec = addListFilter(spec, drugForm, "drugForm");
        spec = addListFilter(spec, dosage, "dosage");
        spec = addListFilter(spec, packQuantity, "packQuantity");
        spec = addListFilter(spec, atc1, "atc1");
        spec = addListFilter(spec, atc2, "atc2");
        spec = addListFilter(spec, atc3, "atc3");

        Pageable pageable = PageRequest.of(Math.max(0, page), Math.max(1, size));
        return drugRepository.findAll(spec, pageable);
    }

    private Specification<Drug> addListFilter(Specification<Drug> spec, List<String> values, String field) {
        if (values != null && !values.isEmpty()) {
            return spec.and((root, query, cb) -> root.get(field).in(values));
        }
        return spec;
    }

}


