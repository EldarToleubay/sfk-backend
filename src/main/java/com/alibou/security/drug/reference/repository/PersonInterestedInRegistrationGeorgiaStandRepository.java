package com.alibou.security.drug.reference.repository;

import com.alibou.security.drug.reference.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PersonInterestedInRegistrationGeorgiaStandRepository extends JpaRepository<PersonInterestedInRegistrationGeorgiaStand, Long> {
    @Query("SELECT i.name FROM PersonInterestedInRegistrationGeorgiaStand i")
    List<String> findAllNames();

    List<PersonInterestedInRegistrationGeorgiaStand> findByIdIn(List<Long> ids);
}
