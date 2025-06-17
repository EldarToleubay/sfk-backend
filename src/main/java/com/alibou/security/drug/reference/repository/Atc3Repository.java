package com.alibou.security.drug.reference.repository;

import com.alibou.security.drug.reference.Atc3;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface Atc3Repository extends JpaRepository<Atc3, Long> {
    @Query("SELECT i.name FROM Atc3 i")
    List<String> findAllNames();

}
