package com.alibou.security.auditing;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AuditLogRepositoryCustomImpl implements AuditLogRepositoryCustom {

    private final EntityManager em;

    @Override
    public Page<AuditLog> search(String username, String method, String endpoint, Integer statusCode,
                                 LocalDateTime from, LocalDateTime to, Pageable pageable) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        // === MAIN QUERY ===
        CriteriaQuery<AuditLog> cq = cb.createQuery(AuditLog.class);
        Root<AuditLog> root = cq.from(AuditLog.class);

        List<Predicate> predicates = buildPredicates(cb, root, username, method, endpoint, statusCode, from, to);
        cq.where(cb.and(predicates.toArray(new Predicate[0])));
        cq.orderBy(cb.desc(root.get("timestamp")));

        TypedQuery<AuditLog> query = em.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<AuditLog> results = query.getResultList();

        // === COUNT QUERY ===
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<AuditLog> countRoot = countQuery.from(AuditLog.class);
        countQuery.select(cb.count(countRoot));

        List<Predicate> countPredicates = buildPredicates(cb, countRoot, username, method, endpoint, statusCode, from, to);
        countQuery.where(cb.and(countPredicates.toArray(new Predicate[0])));

        Long total = em.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(results, pageable, total);
    }

    private List<Predicate> buildPredicates(CriteriaBuilder cb,
                                            Root<AuditLog> root,
                                            String username,
                                            String method,
                                            String endpoint,
                                            Integer statusCode,
                                            LocalDateTime from,
                                            LocalDateTime to) {
        List<Predicate> predicates = new ArrayList<>();

        if (username != null && !username.isBlank()) {
            predicates.add(cb.equal(root.get("username"), username));
        }
        if (method != null && !method.isBlank()) {
            predicates.add(cb.equal(root.get("method"), method));
        }
        if (endpoint != null && !endpoint.isBlank()) {
            predicates.add(cb.like(root.get("endpoint"), "%" + endpoint + "%"));
        }
        if (statusCode != null) {
            predicates.add(cb.equal(root.get("statusCode"), statusCode));
        }
        if (from != null) {
            predicates.add(cb.greaterThanOrEqualTo(root.get("timestamp"), from));
        }
        if (to != null) {
            predicates.add(cb.lessThanOrEqualTo(root.get("timestamp"), to));
        }

        return predicates;
    }
}
