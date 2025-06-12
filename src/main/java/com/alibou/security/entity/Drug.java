package com.alibou.security.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "drug_registration")
@Getter
@Setter
public class Drug {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //1 Год регистрации
    @Column(name = "year")
    private Integer year;

    //2 Сегмент
    @Column(name = "segment", length = 255)
    private String segment;

    //3 Торговое наименование препарата
    @Column(name = "trade_name", length = 1000)
    private String tradeName;

    //4 Производящая компания
    @Column(name = "manufacturing_company", length = 1000)
    private String manufacturingCompany;

    //5 Лицо с торговой лицензией STANDARD
    @Column(name = "person_with_trading_license", length = 1000)
    private String personWithTradingLicense;

    //6 Лицо, заинтересованное в регистрации в Грузии STANDARD
    @Column(name = "person_interested_in_registration_georgia_stand", length = 1000)
    private String personInterestedInRegistrationGeorgiaStand;

    //7 Заинтересованная сторона (юридическое лицо, ищущее регистрацию в Грузии) STANDARD
    @Column(name = "interested_party", length = 1000)
    private String interestedParty;

    //8 Рецептурный или безрецептурный препарат (RX/OTC)
    @Column(name = "rx_otc", length = 255)
    private String rxOtc;

    //9 Режим регистрации
    @Column(name = "mode_of_registration", length = 50)
    private String modeOfRegistration;

    //10 SKU
    @Column(name = "sku", length = 1000)
    private String sku;

    //11 Форма выпуска препарата
    @Column(name = "drug_form", length = 255)
    private String drugForm;

    //12 Дозировка (текущий формат)
    @Column(name = "dosage", length = 255)
    private String dosage;

    //13 Количество в упаковке
    @Column(name = "pack_quantity")
    private String packQuantity;

    //14 МНН
    @Column(name = "inn", length = 1000)
    private String inn;

    //15 ATC1 WHO
    @Column(name = "ATC1", length = 255)
    private String atc1;

    //16 ATC2 WHO
    @Column(name = "ATC2", length = 255)
    private String atc2;

    //17 ATC3 WHO
    @Column(name = "ATC3", length = 255)
    private String atc3;

    //18 Объем
    @Column(name = "volumeInUnits")
    private BigDecimal volumeInUnits;

    //19 Цена за единицу в локальной валюте
    @Column(name = "price_per_unit_gel")
    private BigDecimal pricePerUnitLari;

    //20 Цена за единицу в долларах США
    @Column(name = "price_per_unit_usd")
    private BigDecimal pricePerUnitUsd;

    //21 Общая стоимость в локальной валюте
    @Column(name = "value_in_gel")
    private BigDecimal valueInGel;

    //22 Общая стоимость в USD
    @Column(name = "value_in_usd")
    private BigDecimal valueInUsd;

    //23 Дата регистрации
    @Column(name = "import_date")
    private LocalDate importDate;

    //24 Price Source
    @Column(name = "price_source", length = 255)
    private String priceSource;

}

