package com.alibou.security.drug.reference.repository;

import com.alibou.security.drug.reference.Atc2;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface Atc2Repository extends JpaRepository<Atc2, Long> {
    @Query("SELECT i.name FROM Atc2 i")
    List<String> findAllNames();

    List<Atc2> findByIdIn(List<Long> ids);


}
