package com.mycompany.myapp.service;

import java.util.List;

import javax.persistence.criteria.JoinType;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import io.github.jhipster.service.QueryService;

import com.mycompany.myapp.domain.Medicament;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.MedicamentRepository;
import com.mycompany.myapp.service.dto.MedicamentCriteria;

/**
 * Service for executing complex queries for {@link Medicament} entities in the database.
 * The main input is a {@link MedicamentCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Medicament} or a {@link Page} of {@link Medicament} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MedicamentQueryService extends QueryService<Medicament> {

    private final Logger log = LoggerFactory.getLogger(MedicamentQueryService.class);

    private final MedicamentRepository medicamentRepository;

    public MedicamentQueryService(MedicamentRepository medicamentRepository) {
        this.medicamentRepository = medicamentRepository;
    }

    /**
     * Return a {@link List} of {@link Medicament} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Medicament> findByCriteria(MedicamentCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Medicament> specification = createSpecification(criteria);
        return medicamentRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Medicament} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Medicament> findByCriteria(MedicamentCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Medicament> specification = createSpecification(criteria);
        return medicamentRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MedicamentCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Medicament> specification = createSpecification(criteria);
        return medicamentRepository.count(specification);
    }

    /**
     * Function to convert {@link MedicamentCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Medicament> createSpecification(MedicamentCriteria criteria) {
        Specification<Medicament> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Medicament_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Medicament_.name));
            }
            if (criteria.getForme() != null) {
                specification = specification.and(buildSpecification(criteria.getForme(), Medicament_.forme));
            }
            if (criteria.getDescrpition() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescrpition(), Medicament_.descrpition));
            }
        }
        return specification;
    }
}
