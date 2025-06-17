package com.alibou.security.drug.reference.repository;

import com.alibou.security.drug.Inn;
import com.alibou.security.drug.reference.Atc1;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface Atc1Repository extends JpaRepository<Atc1, Long> {
    @Query("SELECT i.name FROM Atc1 i")
    List<String> findAllNames();

}
