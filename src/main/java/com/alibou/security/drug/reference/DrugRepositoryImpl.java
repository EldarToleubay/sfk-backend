package com.alibou.security.drug.reference;

import com.alibou.security.drug.Drug;
import com.alibou.security.drug.DrugFilterRequest;
import com.alibou.security.drug.DrugRepositoryCustom;
import com.alibou.security.drug.NameValueDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DrugRepositoryImpl implements DrugRepositoryCustom {

    private final EntityManager em;

    @Override
    public List<NameValueDto> findTopCompaniesWithFilters(DrugFilterRequest filter, String currency, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<NameValueDto> cq = cb.createQuery(NameValueDto.class);
        Root<Drug> root = cq.from(Drug.class);

        List<Predicate> predicates = new ArrayList<>();

        if (filter.getInn() != null && !filter.getInn().isEmpty()) {
            predicates.add(root.get("inn").in(filter.getInn()));
        }
        if (filter.getSegment() != null && !filter.getSegment().isEmpty()) {
            predicates.add(root.get("segment").in(filter.getSegment()));
        }
        if (filter.getTradeName() != null && !filter.getTradeName().isEmpty()) {
            predicates.add(root.get("tradeName").in(filter.getTradeName()));
        }
        if (filter.getManufacturingCompany() != null && !filter.getManufacturingCompany().isEmpty()) {
            predicates.add(root.get("manufacturingCompany").in(filter.getManufacturingCompany()));
        }
        if (filter.getDrugForm() != null && !filter.getDrugForm().isEmpty()) {
            predicates.add(root.get("drugForm").in(filter.getDrugForm()));
        }
        if (filter.getDosage() != null && !filter.getDosage().isEmpty()) {
            predicates.add(root.get("dosage").in(filter.getDosage()));
        }
        if (filter.getPackQuantity() != null && !filter.getPackQuantity().isEmpty()) {
            predicates.add(root.get("packQuantity").in(filter.getPackQuantity()));
        }
        if (filter.getAtc1() != null && !filter.getAtc1().isEmpty()) {
            predicates.add(root.get("atc1").in(filter.getAtc1()));
        }
        if (filter.getAtc2() != null && !filter.getAtc2().isEmpty()) {
            predicates.add(root.get("atc2").in(filter.getAtc2()));
        }
        if (filter.getAtc3() != null && !filter.getAtc3().isEmpty()) {
            predicates.add(root.get("atc3").in(filter.getAtc3()));
        }
        if (filter.getDateFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("importDate"), filter.getDateFrom()));
        }
        if (filter.getDateTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("importDate"), filter.getDateTo()));
        }

        // Группировка
        Expression<String> companyGroup = root.get("manufacturingCompany");

        // Выбор поля по валюте
        Expression<BigDecimal> valueExpression = currency.equalsIgnoreCase("usd")
                ? root.get("valueInUsd")
                : root.get("valueInGel");

        Expression<BigDecimal> sumValue = cb.sum(valueExpression);

        cq.select(cb.construct(
                        NameValueDto.class,
                        companyGroup,
                        sumValue
                ))
                .where(predicates.toArray(new Predicate[0]))
                .groupBy(companyGroup)
                .orderBy(cb.desc(sumValue));

        TypedQuery<NameValueDto> query = em.createQuery(cq);

        // пагинация
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        return query.getResultList();
    }
}
