package com.alibou.security.useraccess;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserAccessRepository extends JpaRepository<UserAccess, Long> {

    @Query("SELECT ua.refId FROM UserAccess ua WHERE ua.user.id = :userId AND ua.refType = :refType")
    List<Long> findRefIdsByUserIdAndRefType(@Param("userId") Long userId, @Param("refType") String refType); // ✅


    List<UserAccess> findByUserIdAndRefType(Long userId, String refType);

    @Modifying
    @Query("DELETE FROM UserAccess ua WHERE ua.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);



}
