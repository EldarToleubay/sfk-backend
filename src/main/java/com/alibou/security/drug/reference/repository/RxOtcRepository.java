package com.alibou.security.drug.reference.repository;

import com.alibou.security.drug.reference.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RxOtcRepository extends JpaRepository<RxOtc, Long> {
    @Query("SELECT i.name FROM RxOtc i")
    List<String> findAllNames();

    List<RxOtc> findByIdIn(List<Long> ids);
}
