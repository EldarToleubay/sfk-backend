package com.alibou.security.drug.reference.repository;

import com.alibou.security.drug.reference.PersonWithTradingLicense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PersonWithTradingLicenseRepository extends JpaRepository<PersonWithTradingLicense, Long> {
    @Query("SELECT i.name FROM PersonWithTradingLicense i")
    List<String> findAllNames();

    List<PersonWithTradingLicense> findByIdIn(List<Long> ids);
}
