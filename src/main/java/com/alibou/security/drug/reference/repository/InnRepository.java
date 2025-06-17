package com.alibou.security.drug.reference.repository;

import com.alibou.security.drug.Inn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface InnRepository extends JpaRepository<Inn, Long> {
    @Query("SELECT i.name FROM Inn i")
    List<String> findAllNames();

}
