package com.alibou.security.drug.reference.repository;

import com.alibou.security.drug.reference.ManufacturingCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ManufacturingCompanyRepository extends JpaRepository<ManufacturingCompany, Long> {
    @Query("SELECT i.name FROM ManufacturingCompany i")
    List<String> findAllNames();

}
