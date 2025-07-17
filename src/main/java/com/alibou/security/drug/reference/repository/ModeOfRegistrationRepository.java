package com.alibou.security.drug.reference.repository;

import com.alibou.security.drug.reference.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ModeOfRegistrationRepository extends JpaRepository<ModeOfRegistration, Long> {
    @Query("SELECT i.name FROM ModeOfRegistration i")
    List<String> findAllNames();

    List<ModeOfRegistration> findByIdIn(List<Long> ids);
}
