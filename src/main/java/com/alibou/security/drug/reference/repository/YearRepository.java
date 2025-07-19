package com.alibou.security.drug.reference.repository;

import com.alibou.security.drug.reference.Year;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface YearRepository extends JpaRepository<Year, Long> {
    @Query("SELECT i.name FROM Year i")
    List<String> findAllNames();

    List<Year> findByIdIn(List<Long> ids);


}
