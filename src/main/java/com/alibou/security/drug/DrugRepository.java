package com.alibou.security.drug;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DrugRepository extends JpaRepository<Drug, Long> {

    @Query("SELECT DISTINCT d.inn FROM Drug d WHERE d.inn IS NOT NULL ORDER BY d.inn")
    List<String> findDistinctInn();




}
