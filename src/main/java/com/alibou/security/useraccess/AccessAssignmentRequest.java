package com.alibou.security.useraccess;

import java.util.List;


public record AccessAssignmentRequest(
        Long userId,
        List<AccessEntry> accesses
) {
    public record AccessEntry(String refType, List<Long> refIds) {
    }
}
