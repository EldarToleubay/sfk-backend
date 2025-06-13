package com.alibou.security.drug;

import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DrugService {

    private final DrugRepository drugRepository;


    private final EntityManager entityManager;

    private static final int BATCH_SIZE = 1000;

    @Transactional
    public void importExcel(MultipartFile file) throws Exception {
        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            if (rows.hasNext()) rows.next(); // Пропустить заголовок

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
            int rowNum = 1;

            List<Drug> batch = new ArrayList<>();

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                rowNum++;

                try {
                    Drug drug = new Drug();
                    drug.setYear(getIntCell(currentRow, 0, rowNum, "year"));
                    drug.setSegment(getStringCell(currentRow, 1, rowNum, "segment"));
                    drug.setTradeName(getStringCell(currentRow, 2, rowNum, "tradeName"));
                    drug.setManufacturingCompany(getStringCell(currentRow, 3, rowNum, "manufacturingCompany"));
                    drug.setPersonWithTradingLicense(getStringCell(currentRow, 4, rowNum, "personWithTradingLicense"));
                    drug.setPersonInterestedInRegistrationGeorgiaStand(getStringCell(currentRow, 5, rowNum, "personInterestedInRegistrationGeorgiaStand"));
                    drug.setInterestedParty(getStringCell(currentRow, 6, rowNum, "interestedParty"));
                    drug.setRxOtc(getStringCell(currentRow, 7, rowNum, "rxOtc"));
                    drug.setModeOfRegistration(getStringCell(currentRow, 8, rowNum, "modeOfRegistration"));
                    drug.setSku(getStringCell(currentRow, 9, rowNum, "sku"));
                    drug.setDrugForm(getStringCell(currentRow, 10, rowNum, "drugForm"));
                    drug.setDosage(getStringCell(currentRow, 11, rowNum, "dosage"));
                    drug.setPackQuantity(getStringCell(currentRow, 12, rowNum, "packQuantity"));
                    drug.setInn(getStringCell(currentRow, 13, rowNum, "inn"));
                    drug.setAtc1(getStringCell(currentRow, 14, rowNum, "atc1"));
                    drug.setAtc2(getStringCell(currentRow, 15, rowNum, "atc2"));
                    drug.setAtc3(getStringCell(currentRow, 16, rowNum, "atc3"));
                    drug.setVolumeInUnits(getBigDecimalCell(currentRow, 17, rowNum, "volumeInUnits"));
                    drug.setPricePerUnitLari(getBigDecimalCell(currentRow, 18, rowNum, "pricePerUnitLari"));
                    drug.setPricePerUnitUsd(getBigDecimalCell(currentRow, 19, rowNum, "pricePerUnitUsd"));
                    drug.setValueInGel(getBigDecimalCell(currentRow, 20, rowNum, "valueInGel"));
                    drug.setValueInUsd(getBigDecimalCell(currentRow, 21, rowNum, "valueInUsd"));

                    Cell dateCell = currentRow.getCell(22);
                    if (dateCell != null) {
                        if (dateCell.getCellType() == CellType.NUMERIC) {
                            drug.setImportDate(dateCell.getLocalDateTimeCellValue().toLocalDate());
                        } else if (dateCell.getCellType() == CellType.STRING) {
                            drug.setImportDate(LocalDate.parse(dateCell.getStringCellValue(), formatter));
                        }
                    }

                    drug.setPriceSource(getStringCell(currentRow, 23, rowNum, "priceSource"));

                    batch.add(drug);

                    if (batch.size() == BATCH_SIZE) {
                        saveBatch(batch);
                        batch.clear();
                    }

                } catch (Exception e) {
                    System.err.printf("Ошибка в строке %d: %s%n", rowNum, e.getMessage());
                    e.printStackTrace();
                }
            }

            // финальная пачка
            if (!batch.isEmpty()) {
                saveBatch(batch);
            }
        }
    }

    private void saveBatch(List<Drug> batch) {
        drugRepository.saveAll(batch);
        entityManager.flush();
        entityManager.clear(); // освобождаем память
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
            Cell cell = row.getCell(index);
            return getCellAsString(cell);
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

    private String getCellAsString(Cell cell) {
        if (cell == null) return null;

        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(cell)) {
                    return cell.getDateCellValue().toString();
                } else {
                    double val = cell.getNumericCellValue();
                    return (val == Math.floor(val)) ? String.valueOf((long) val) : String.valueOf(val);
                }
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            case FORMULA:
                return cell.getCellFormula(); // или cell.getStringCellValue() для значения
            case BLANK:
                return "";
            default:
                return "";
        }
    }

    public List<Drug> getAll() {
        return drugRepository.findAll();
    }
}

