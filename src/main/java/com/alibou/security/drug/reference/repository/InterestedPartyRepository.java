package com.alibou.security.drug.reference.repository;

import com.alibou.security.drug.reference.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InterestedPartyRepository extends JpaRepository<InterestedParty, Long> {
    @Query("SELECT i.name FROM InterestedParty i")
    List<String> findAllNames();

    List<InterestedParty> findByIdIn(List<Long> ids);
}
