package com.alibou.security.drug;

import lombok.Data;

import java.util.List;

@Data
public class DrugExcelExportRequestDTO {
    private DrugFilterRequest filter;
    private List<String> columns;
}
