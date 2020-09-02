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

import com.mycompany.myapp.domain.Centre;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.CentreRepository;
import com.mycompany.myapp.service.dto.CentreCriteria;

/**
 * Service for executing complex queries for {@link Centre} entities in the database.
 * The main input is a {@link CentreCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Centre} or a {@link Page} of {@link Centre} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class CentreQueryService extends QueryService<Centre> {

    private final Logger log = LoggerFactory.getLogger(CentreQueryService.class);

    private final CentreRepository centreRepository;

    public CentreQueryService(CentreRepository centreRepository) {
        this.centreRepository = centreRepository;
    }

    /**
     * Return a {@link List} of {@link Centre} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Centre> findByCriteria(CentreCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Centre> specification = createSpecification(criteria);
        return centreRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Centre} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Centre> findByCriteria(CentreCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Centre> specification = createSpecification(criteria);
        return centreRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(CentreCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Centre> specification = createSpecification(criteria);
        return centreRepository.count(specification);
    }

    /**
     * Function to convert {@link CentreCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Centre> createSpecification(CentreCriteria criteria) {
        Specification<Centre> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Centre_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), Centre_.name));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), Centre_.address));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Centre_.phone));
            }
            if (criteria.getFax() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFax(), Centre_.fax));
            }
            if (criteria.getEmergency() != null) {
                specification = specification.and(buildStringSpecification(criteria.getEmergency(), Centre_.emergency));
            }
            if (criteria.getAdminDeCentreId() != null) {
                specification = specification.and(buildSpecification(criteria.getAdminDeCentreId(),
                    root -> root.join(Centre_.adminDeCentres, JoinType.LEFT).get(AdminDeCentre_.id)));
            }
            if (criteria.getMedecinId() != null) {
                specification = specification.and(buildSpecification(criteria.getMedecinId(),
                    root -> root.join(Centre_.medecins, JoinType.LEFT).get(Medecin_.id)));
            }
            if (criteria.getPatientId() != null) {
                specification = specification.and(buildSpecification(criteria.getPatientId(),
                    root -> root.join(Centre_.patients, JoinType.LEFT).get(Patient_.id)));
            }
            if (criteria.getRegionId() != null) {
                specification = specification.and(buildSpecification(criteria.getRegionId(),
                    root -> root.join(Centre_.region, JoinType.LEFT).get(Region_.id)));
            }
        }
        return specification;
    }
}
