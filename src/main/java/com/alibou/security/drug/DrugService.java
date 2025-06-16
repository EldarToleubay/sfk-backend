package com.alibou.security.drug;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DrugService {

    private final DrugRepository drugRepository;


    public String removeAllDrugs() {
        drugRepository.deleteAll();
        return "Drugs removed";
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
}


