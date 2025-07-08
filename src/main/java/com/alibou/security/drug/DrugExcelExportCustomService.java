package com.alibou.security.drug;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DrugExcelExportCustomService {

    // Фиксированный порядок колонок, как в UI
    private static final List<String> DEFAULT_COLUMNS_ORDER = List.of(
            "year", "segment", "tradeName", "manufacturingCompany",
            "personWithTradingLicense", "personInterestedInRegistrationGeorgiaStand", "interestedParty", "rxOtc",
            "modeOfRegistration", "sku", "drugForm", "dosage", "packQuantity", "inn",
            "atc1", "atc2", "atc3", "pricePerUnitLari", "pricePerUnitUsd",
            "importDate", "priceSource", "volumeInUnits", "volumeInSU", "valueInGel", "valueInUsd"
    );

    // Отображение ключей в заголовки
    private static final Map<String, String> COLUMN_HEADERS = new LinkedHashMap<>() {{
        put("year", "Year");
        put("segment", "Segment");
        put("tradeName", "Trade Name");
        put("manufacturingCompany", "Manufacturing company");
        put("personWithTradingLicense", "Person with trading license");
        put("personInterestedInRegistrationGeorgiaStand", "Person interested in registration in Georgia");
        put("interestedParty", "Interested party (entity seeking registration in Georgia)");
        put("rxOtc", "Rx/OTC");
        put("modeOfRegistration", "Mode of registration");
        put("sku", "SKU");
        put("drugForm", "Drug Form");
        put("dosage", "Dosage");
        put("packQuantity", "Pack quantity");
        put("inn", "INN");
        put("atc1", "ATC1 WHO");
        put("atc2", "ATC2 WHO");
        put("atc3", "ATC3 WHO");
        put("pricePerUnitLari", "Price per Unit, in Lari");
        put("pricePerUnitUsd", "Price per unit, in USD");
        put("importDate", "Import date");
        put("priceSource", "Price Source");
        put("volumeInUnits", "Volume Units");
        put("volumeInSU", "Volume SU");
        put("valueInGel", "Value GEL");
        put("valueInUsd", "Value USD");
    }};

    public byte[] exportToExcel(List<DrugExportDto> drugs, List<String> selectedColumns) throws IOException {
        // Упорядочиваем выбранные колонки по дефолтному порядку
        List<String> columns = DEFAULT_COLUMNS_ORDER.stream()
                .filter(selectedColumns::contains)
                .collect(Collectors.toList());

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Drugs");

            createHeader(sheet, columns);

            int rowIdx = 1;
            for (DrugExportDto dto : drugs) {
                Row row = sheet.createRow(rowIdx++);
                fillRow(row, dto, columns);
            }

            for (int i = 0; i < columns.size(); i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        }
    }

    private void createHeader(Sheet sheet, List<String> columns) {
        Workbook workbook = sheet.getWorkbook();

        Font headerFont = workbook.createFont();
        headerFont.setBold(true);

        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFont(headerFont);

        Row header = sheet.createRow(0);
        for (int i = 0; i < columns.size(); i++) {
            String columnKey = columns.get(i);
            String displayName = COLUMN_HEADERS.getOrDefault(columnKey, columnKey);
            Cell cell = header.createCell(i);
            cell.setCellValue(displayName);
            cell.setCellStyle(headerStyle);
        }
    }

    private void fillRow(Row row, DrugExportDto d, List<String> columns) {
        int i = 0;
        for (String col : columns) {
            Cell cell = row.createCell(i++);
            switch (col) {
                case "segment" -> cell.setCellValue(d.getSegment());
                case "tradeName" -> cell.setCellValue(d.getTradeName());
                case "manufacturingCompany" -> cell.setCellValue(d.getManufacturingCompany());
                case "drugForm" -> cell.setCellValue(d.getDrugForm());
                case "dosage" -> cell.setCellValue(d.getDosage());
                case "packQuantity" -> cell.setCellValue(d.getPackQuantity());
                case "inn" -> cell.setCellValue(d.getInn());
                case "atc1" -> cell.setCellValue(d.getAtc1());
                case "atc2" -> cell.setCellValue(d.getAtc2());
                case "atc3" -> cell.setCellValue(d.getAtc3());
                case "importDate" -> cell.setCellValue(d.getImportDate() != null ? d.getImportDate().toString() : "");
                case "year" -> cell.setCellValue(d.getYear() != null ? d.getYear() : 0);
                case "personWithTradingLicense" -> cell.setCellValue(d.getPersonWithTradingLicense());
                case "personInterestedInRegistrationGeorgiaStand" -> cell.setCellValue(d.getPersonInterestedInRegistrationGeorgiaStand());
                case "interestedParty" -> cell.setCellValue(d.getInterestedParty());
                case "rxOtc" -> cell.setCellValue(d.getRxOtc());
                case "modeOfRegistration" -> cell.setCellValue(d.getModeOfRegistration());
                case "sku" -> cell.setCellValue(d.getSku());
                case "volumeInUnits" -> cell.setCellValue(toDouble(d.getVolumeInUnits()));
                case "pricePerUnitLari" -> cell.setCellValue(toDouble(d.getPricePerUnitLari()));
                case "pricePerUnitUsd" -> cell.setCellValue(toDouble(d.getPricePerUnitUsd()));
                case "valueInGel" -> cell.setCellValue(toDouble(d.getValueInGel()));
                case "valueInUsd" -> cell.setCellValue(toDouble(d.getValueInUsd()));
                case "volumeInSU" -> cell.setCellValue(toDouble(d.getVolumeInSU()));
                case "priceSource" -> cell.setCellValue(d.getPriceSource());
                default -> cell.setCellValue("N/A");
            }
        }
    }

    private double toDouble(BigDecimal val) {
        return val != null ? val.doubleValue() : 0.0;
    }
}
