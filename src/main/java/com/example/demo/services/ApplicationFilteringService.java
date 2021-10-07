package com.example.demo.services;

import com.example.demo.dto.FilteringCriteria;
import com.example.demo.entities.Application;
import com.example.demo.entities.Application_;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationFilteringService {
    private final EntityManager entityManager;

    public List<Application> getFiltered(FilteringCriteria criteria) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Application> query = builder.createQuery(Application.class);
        Root<Application> application_ = query.from(Application.class);
        query.select(application_);

        List<Predicate> searchCriteria = new ArrayList<>();
        if(criteria.getName() != null && !criteria.getName().isEmpty()) {
            searchCriteria.add(likePredicate(builder, application_.get(Application_.name), criteria.getName()));
        }
        if(criteria.getState() != null) {
            searchCriteria.add(builder.equal(application_.get(Application_.state), criteria.getState()));
        }

        query.where(searchCriteria.toArray(Predicate[]::new));

        TypedQuery<Application> resultQuery = entityManager.createQuery(query);
        resultQuery.setFirstResult(getOffset(criteria.getPage(), criteria.getSize()));
        resultQuery.setMaxResults(criteria.getSize());

        return resultQuery.getResultList();
    }

    private Predicate likePredicate(CriteriaBuilder builder, Path<String> path, String firstName) {
        return builder.like(builder.upper(path), "%" + firstName.toUpperCase() + "%");
    }

    private int getOffset(int pageNumber, int pageSize) {
        return (pageNumber - 1) * pageSize;
    }
}
