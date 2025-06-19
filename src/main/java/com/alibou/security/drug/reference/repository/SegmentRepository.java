package com.alibou.security.drug.reference.repository;

import com.alibou.security.drug.reference.Segment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface SegmentRepository extends JpaRepository<Segment, Long> {
    @Query("SELECT i.name FROM Segment i")
    List<String> findAllNames();

    List<Segment> findByIdIn(List<Long> ids);

}
