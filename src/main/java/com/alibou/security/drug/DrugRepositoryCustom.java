package com.alibou.security.drug;

import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface DrugRepositoryCustom {
//    List<NameValueDto> findTopCompaniesWithFilters(DrugFilterRequest filter, String currency, Pageable pageable);

    BigDecimal getTotalMetricWithFilters(DrugFilterRequest filter, String metric);

    List<NameValueDto> findTopByGroupFieldWithFilters(DrugFilterRequest filter, String metric, String groupField, Pageable pageable);

}

