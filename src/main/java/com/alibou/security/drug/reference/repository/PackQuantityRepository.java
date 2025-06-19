package com.alibou.security.drug.reference.repository;

import com.alibou.security.drug.reference.PackQuantity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PackQuantityRepository extends JpaRepository<PackQuantity, Long> {
    @Query("SELECT i.name FROM PackQuantity i")
    List<String> findAllNames();

    List<PackQuantity> findByIdIn(List<Long> ids);


}
