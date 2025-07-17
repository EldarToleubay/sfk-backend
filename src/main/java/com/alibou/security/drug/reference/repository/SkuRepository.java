package com.alibou.security.drug.reference.repository;

import com.alibou.security.drug.reference.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SkuRepository extends JpaRepository<Sku, Long> {
    @Query("SELECT i.name FROM Sku i")
    List<String> findAllNames();

    List<Sku> findByIdIn(List<Long> ids);
}
