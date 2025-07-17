package com.alibou.security.drug.reference.repository;

import com.alibou.security.drug.reference.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PriceSourceRepository extends JpaRepository<PriceSource, Long> {
    @Query("SELECT i.name FROM PriceSource i")
    List<String> findAllNames();

    List<PriceSource> findByIdIn(List<Long> ids);
}
