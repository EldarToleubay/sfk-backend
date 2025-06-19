package com.alibou.security.drug;

import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DrugRepositoryCustom {
    List<NameValueDto> findTopCompaniesWithFilters(DrugFilterRequest filter, String currency, Pageable pageable);
}

