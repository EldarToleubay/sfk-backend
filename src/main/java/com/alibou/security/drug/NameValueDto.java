package com.alibou.security.drug;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class NameValueDto {

    private String companyName;
    private BigDecimal totalValue;

    public NameValueDto(String companyName, BigDecimal totalValue){
        this.companyName = companyName;
        this.totalValue = totalValue;
    }

}
