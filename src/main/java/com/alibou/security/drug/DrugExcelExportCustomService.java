package com.alibou.security.drug;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class DrugExcelExportCustomService {

    public byte[] exportToExcel(List<DrugExportDto> drugs, List<String> columns) throws IOException {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Drugs");

            createHeader(sheet, columns);
            int rowIdx = 1;
            for (DrugExportDto dto : drugs) {
                Row row = sheet.createRow(rowIdx++);
                fillRow(row, dto, columns);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);
            return out.toByteArray();
        }
    }

    private void createHeader(Sheet sheet, List<String> columns) {
        Row header = sheet.createRow(0);
        for (int i = 0; i < columns.size(); i++) {
            header.createCell(i).setCellValue(columns.get(i));
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
                case "personInterestedInRegistrationGeorgiaStand" ->
                        cell.setCellValue(d.getPersonInterestedInRegistrationGeorgiaStand());
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
