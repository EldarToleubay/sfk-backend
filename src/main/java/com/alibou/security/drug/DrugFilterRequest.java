package com.alibou.security.drug;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;


@Data
public class DrugFilterRequest {
    private List<String> inn;
    private List<String> segment;
    private List<String> tradeName;
    private List<String> manufacturingCompany;
    private List<String> drugForm;
    private List<String> dosage;
    private List<String> packQuantity;
    private List<String> atc1;
    private List<String> atc2;
    private List<String> atc3;

    private LocalDate dateFrom;
    private LocalDate dateTo;
}
