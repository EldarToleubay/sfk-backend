package com.alibou.security.repository;

import com.alibou.security.entity.Drug;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrugRepository extends JpaRepository<Drug, Long> {
}
