package com.alibou.security.useraccess;


import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder(toBuilder = true)
public class RefData {
    private String refType;
    private List<Long> refIds;
}
