package com.alibou.security.useraccess;

import java.util.List;


public record AccessAssignmentRequest(
        Long userId,
        String refType,
        List<Long> refIds
) {}