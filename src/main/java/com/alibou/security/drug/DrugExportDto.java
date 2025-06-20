package com.alibou.security.drug;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DrugExportDto {
    private String segment;
    private String tradeName;
    private String manufacturingCompany;
    private String drugForm;
    private String dosage;
    private String packQuantity;
    private String inn;
    private String atc1;
    private String atc2;
    private String atc3;
    private LocalDate importDate;
    private Integer year;
    private String personWithTradingLicense;
    private String personInterestedInRegistrationGeorgiaStand;
    private String interestedParty;
    private String rxOtc;
    private String modeOfRegistration;
    private String sku;
    private BigDecimal volumeInUnits;
    private BigDecimal pricePerUnitLari;
    private BigDecimal pricePerUnitUsd;
    private BigDecimal valueInGel;
    private BigDecimal valueInUsd;
    private BigDecimal volumeInSU;
    private String priceSource;
}
