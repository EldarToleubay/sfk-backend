package com.alibou.security.drug;

import lombok.Data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@Data
public class DrugFilterRequest {
    private List<String> inn = new ArrayList<>();
    private List<String> segment = new ArrayList<>();
    private List<String> tradeName= new ArrayList<>();
    private List<String> manufacturingCompany= new ArrayList<>();
    private List<String> drugForm= new ArrayList<>();
    private List<String> dosage= new ArrayList<>();
    private List<String> packQuantity= new ArrayList<>();
    private List<String> atc1= new ArrayList<>();
    private List<String> atc2= new ArrayList<>();
    private List<String> atc3= new ArrayList<>();

    private LocalDate dateFrom;
    private LocalDate dateTo;
}
