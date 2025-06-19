package com.alibou.security.drug.reference.repository;

import com.alibou.security.drug.reference.DrugForm;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DrugFormRepository extends JpaRepository<DrugForm, Long> {
    @Query("SELECT i.name FROM DrugForm i")
    List<String> findAllNames();

    List<DrugForm> findByIdIn(List<Long> ids);


}
