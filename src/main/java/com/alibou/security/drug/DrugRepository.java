package com.alibou.security.drug;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DrugRepository extends JpaRepository<Drug, Long> {

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


}
