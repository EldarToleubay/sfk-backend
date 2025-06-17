package com.alibou.security.drug.reference.repository;

import com.alibou.security.drug.reference.Dosage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DosageRepository extends JpaRepository<Dosage, Long> {
    @Query("SELECT i.name FROM Dosage i")
    List<String> findAllNames();

}
