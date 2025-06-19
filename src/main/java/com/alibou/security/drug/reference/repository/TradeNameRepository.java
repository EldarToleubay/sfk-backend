package com.alibou.security.drug.reference.repository;

import com.alibou.security.drug.reference.TradeName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TradeNameRepository extends JpaRepository<TradeName, Long> {
    @Query("SELECT i.name FROM TradeName i")
    List<String> findAllNames();

    List<TradeName> findByName(String name);

    List<TradeName> findByIdIn(List<Long> ids);


}
