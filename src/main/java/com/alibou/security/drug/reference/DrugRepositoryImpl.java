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
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class DrugRepositoryImpl implements DrugRepositoryCustom {

    private final EntityManager em;

    @Override
    public BigDecimal getTotalMetricWithFilters(DrugFilterRequest filter, String metric) {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<BigDecimal> totalQuery = cb.createQuery(BigDecimal.class);
        Root<Drug> root = totalQuery.from(Drug.class);

        List<Predicate> predicates = buildPredicates(cb, root, filter);

        Expression<? extends Number> metricExpr = root.get(metric);
        Expression<BigDecimal> totalSum = cb.sum((Expression<BigDecimal>) metricExpr);

        totalQuery.select(cb.coalesce(totalSum, BigDecimal.ZERO))
                .where(predicates.toArray(new Predicate[0]));

        BigDecimal result = em.createQuery(totalQuery).getSingleResult();
        return result != null ? result : BigDecimal.ZERO;
    }


    @Override
    public List<NameValueDto> findTopByGroupFieldWithFilters(DrugFilterRequest filter, String metric, String groupField, Pageable pageable) {
        if (isFilterEmpty(filter)) {
            return List.of(new NameValueDto("UNKNOWN", BigDecimal.ZERO, BigDecimal.ZERO));
        }
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<NameValueDto> cq = cb.createQuery(NameValueDto.class);
        Root<Drug> root = cq.from(Drug.class);

        List<Predicate> predicates = buildPredicates(cb, root, filter);

        // Группировка по переданному полю (например, inn, tradeName, manufacturingCompany)
        Expression<String> groupByField = root.get(groupField);
        Expression<? extends Number> metricExpr = root.get(metric);
        Expression<Number> sumValue = cb.sum((Expression<Number>) metricExpr);

        cq.select(cb.construct(NameValueDto.class, groupByField, sumValue, cb.literal(BigDecimal.ZERO)))
                .where(predicates.toArray(new Predicate[0]))
                .groupBy(groupByField)
                .orderBy(cb.desc(sumValue));

        TypedQuery<NameValueDto> query = em.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<NameValueDto> topResults = query.getResultList();

        // ➕ Общая сумма по фильтру (без группировки)
        CriteriaQuery<BigDecimal> totalQuery = cb.createQuery(BigDecimal.class);
        Root<Drug> totalRoot = totalQuery.from(Drug.class);
        Expression<? extends Number> totalMetricExpr = totalRoot.get(metric);
        Expression<BigDecimal> totalSumValue = cb.sum((Expression<BigDecimal>) totalMetricExpr);
        totalQuery.select(cb.coalesce(totalSumValue, BigDecimal.ZERO))
                .where(buildPredicates(cb, totalRoot, filter).toArray(new Predicate[0]));

        BigDecimal total = em.createQuery(totalQuery).getSingleResult();
        if (total == null || BigDecimal.ZERO.compareTo(total) == 0) {
            total = BigDecimal.ONE; // чтобы не делить на 0
        }

        // ➕ Вычисляем проценты
        for (NameValueDto dto : topResults) {
            BigDecimal percent = dto.getTotalValue()
                    .multiply(BigDecimal.valueOf(100))
                    .divide(total, 2, RoundingMode.HALF_UP);
            dto.setTotalValueInPercent(percent);
        }

        return topResults;
    }


    private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<Drug> root, DrugFilterRequest filter) {
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

        if (filter.getPersonWithTradingLicense() != null && !filter.getPersonWithTradingLicense().isEmpty()) {
            predicates.add(root.get("personWithTradingLicense").in(filter.getPersonWithTradingLicense()));
        }
        if (filter.getPersonInterestedInRegistrationGeorgiaStand() != null && !filter.getPersonInterestedInRegistrationGeorgiaStand().isEmpty()) {
            predicates.add(root.get("personInterestedInRegistrationGeorgiaStand").in(filter.getPersonInterestedInRegistrationGeorgiaStand()));
        }
        if (filter.getInterestedParty() != null && !filter.getInterestedParty().isEmpty()) {
            predicates.add(root.get("interestedParty").in(filter.getInterestedParty()));
        }
        if (filter.getRxOtc() != null && !filter.getRxOtc().isEmpty()) {
            predicates.add(root.get("rxOtc").in(filter.getRxOtc()));
        }
        if (filter.getModeOfRegistration() != null && !filter.getModeOfRegistration().isEmpty()) {
            predicates.add(root.get("modeOfRegistration").in(filter.getModeOfRegistration()));
        }
        if (filter.getSku() != null && !filter.getSku().isEmpty()) {
            predicates.add(root.get("sku").in(filter.getSku()));
        }
        if (filter.getPriceSource() != null && !filter.getPriceSource().isEmpty()) {
            predicates.add(root.get("priceSource").in(filter.getPriceSource()));
        }

        if (filter.getDateFrom() != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("importDate"), filter.getDateFrom()));
        }
        if (filter.getDateTo() != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("importDate"), filter.getDateTo()));
        }

        return predicates;
    }


    private boolean isFilterEmpty(DrugFilterRequest filter) {
        return (filter.getInn().isEmpty() &&
                filter.getSegment().isEmpty() &&
                filter.getTradeName().isEmpty() &&
                filter.getManufacturingCompany().isEmpty() &&
                filter.getDrugForm().isEmpty() &&
                filter.getDosage().isEmpty() &&
                filter.getPackQuantity().isEmpty() &&
                filter.getAtc1().isEmpty() &&
                filter.getAtc2().isEmpty() &&
                filter.getAtc3().isEmpty() &&
                filter.getPersonWithTradingLicense().isEmpty() &&
                filter.getPersonInterestedInRegistrationGeorgiaStand().isEmpty() &&
                filter.getInterestedParty().isEmpty() &&
                filter.getRxOtc().isEmpty() &&
                filter.getModeOfRegistration().isEmpty() &&
                filter.getSku().isEmpty() &&
                filter.getPriceSource().isEmpty());
    }


}
