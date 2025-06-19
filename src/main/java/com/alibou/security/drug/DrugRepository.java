package com.alibou.security.drug;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface DrugRepository extends JpaRepository<Drug, Long>, JpaSpecificationExecutor<Drug>, DrugRepositoryCustom {

    @Query("SELECT DISTINCT d.inn FROM Drug d WHERE d.inn IS NOT NULL ORDER BY d.inn")
    List<String> findDistinctInn();

    @Query("SELECT DISTINCT d.segment FROM Drug d WHERE d.segment IS NOT NULL ORDER BY d.segment")
    List<String> findDistinctSegment();

    @Query("SELECT DISTINCT d.manufacturingCompany FROM Drug d WHERE d.manufacturingCompany IS NOT NULL ORDER BY d.manufacturingCompany")
    List<String> findDistinctManufacturingCompany();

    @Query("SELECT DISTINCT d.tradeName FROM Drug d WHERE d.tradeName IS NOT NULL ORDER BY d.tradeName")
    List<String> findDistinctTradeName();

    @Query("SELECT DISTINCT d.dosage FROM Drug d WHERE d.dosage IS NOT NULL ORDER BY d.dosage")
    List<String> findDistinctDosage();

    @Query("SELECT DISTINCT d.drugForm FROM Drug d WHERE d.drugForm IS NOT NULL ORDER BY d.drugForm")
    List<String> findDistinctDrugForm();

    @Query("SELECT DISTINCT d.packQuantity FROM Drug d WHERE d.packQuantity IS NOT NULL ORDER BY d.packQuantity")
    List<String> findDistinctPackQuantity();

    @Query("SELECT DISTINCT d.atc1 FROM Drug d WHERE d.atc1 IS NOT NULL ORDER BY d.atc1")
    List<String> findDistinctAtc1();

    @Query("SELECT DISTINCT d.atc2 FROM Drug d WHERE d.atc2 IS NOT NULL ORDER BY d.atc2")
    List<String> findDistinctAtc2();

    @Query("SELECT DISTINCT d.atc3 FROM Drug d WHERE d.atc3 IS NOT NULL ORDER BY d.atc3")
    List<String> findDistinctAtc3();

    @Modifying
    @Query("DELETE FROM Drug")
    void deleteAllFast();


    List<Drug> findBySegment(String segment);

    @Query("""
    SELECT new com.alibou.security.drug.NameValueDto(
        d.manufacturingCompany,
        SUM(CASE WHEN :currency = 'usd' THEN d.valueInUsd ELSE d.valueInGel END)
    )
    FROM Drug d
    WHERE d.manufacturingCompany IS NOT NULL
      AND (:#{#filter.inn} IS NULL OR d.inn IN :#{#filter.inn})
      AND (:#{#filter.segment} IS NULL OR d.segment IN :#{#filter.segment})
      AND (:#{#filter.tradeName} IS NULL OR d.tradeName IN :#{#filter.tradeName})
      AND (:#{#filter.manufacturingCompany} IS NULL OR d.manufacturingCompany IN :#{#filter.manufacturingCompany})
      AND (:#{#filter.drugForm} IS NULL OR d.drugForm IN :#{#filter.drugForm})
      AND (:#{#filter.dosage} IS NULL OR d.dosage IN :#{#filter.dosage})
      AND (:#{#filter.packQuantity} IS NULL OR d.packQuantity IN :#{#filter.packQuantity})
      AND (:#{#filter.atc1} IS NULL OR d.atc1 IN :#{#filter.atc1})
      AND (:#{#filter.atc2} IS NULL OR d.atc2 IN :#{#filter.atc2})
      AND (:#{#filter.atc3} IS NULL OR d.atc3 IN :#{#filter.atc3})
      AND (:#{#filter.dateFrom} IS NULL OR d.importDate >= :#{#filter.dateFrom})
      AND (:#{#filter.dateTo} IS NULL OR d.importDate <= :#{#filter.dateTo})
    GROUP BY d.manufacturingCompany
    ORDER BY SUM(CASE WHEN :currency = 'usd' THEN d.valueInUsd ELSE d.valueInGel END) DESC
""")
    List<NameValueDto> findTopCompaniesByFilter(
            @Param("filter") DrugFilterRequest filter,
            @Param("currency") String currency,
            Pageable pageable
    );


}
