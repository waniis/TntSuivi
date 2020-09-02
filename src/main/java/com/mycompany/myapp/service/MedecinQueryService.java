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

import com.mycompany.myapp.domain.Medecin;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.MedecinRepository;
import com.mycompany.myapp.service.dto.MedecinCriteria;

/**
 * Service for executing complex queries for {@link Medecin} entities in the database.
 * The main input is a {@link MedecinCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Medecin} or a {@link Page} of {@link Medecin} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class MedecinQueryService extends QueryService<Medecin> {

    private final Logger log = LoggerFactory.getLogger(MedecinQueryService.class);

    private final MedecinRepository medecinRepository;

    public MedecinQueryService(MedecinRepository medecinRepository) {
        this.medecinRepository = medecinRepository;
    }

    /**
     * Return a {@link List} of {@link Medecin} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Medecin> findByCriteria(MedecinCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Medecin> specification = createSpecification(criteria);
        return medecinRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Medecin} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Medecin> findByCriteria(MedecinCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Medecin> specification = createSpecification(criteria);
        return medecinRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(MedecinCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Medecin> specification = createSpecification(criteria);
        return medecinRepository.count(specification);
    }

    /**
     * Function to convert {@link MedecinCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Medecin> createSpecification(MedecinCriteria criteria) {
        Specification<Medecin> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Medecin_.id));
            }
            if (criteria.getFullName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFullName(), Medecin_.fullName));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Medecin_.phone));
            }
            if (criteria.getPhone2() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone2(), Medecin_.phone2));
            }
            if (criteria.getAdress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAdress(), Medecin_.adress));
            }
            if (criteria.getSexe() != null) {
                specification = specification.and(buildSpecification(criteria.getSexe(), Medecin_.sexe));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(Medecin_.user, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getQuestionId() != null) {
                specification = specification.and(buildSpecification(criteria.getQuestionId(),
                    root -> root.join(Medecin_.questions, JoinType.LEFT).get(Question_.id)));
            }
            if (criteria.getPatientId() != null) {
                specification = specification.and(buildSpecification(criteria.getPatientId(),
                    root -> root.join(Medecin_.patients, JoinType.LEFT).get(Patient_.id)));
            }
            if (criteria.getQuestionnaireId() != null) {
                specification = specification.and(buildSpecification(criteria.getQuestionnaireId(),
                    root -> root.join(Medecin_.questionnaires, JoinType.LEFT).get(Questionnaire_.id)));
            }
            if (criteria.getGroupeDePatientId() != null) {
                specification = specification.and(buildSpecification(criteria.getGroupeDePatientId(),
                    root -> root.join(Medecin_.groupeDePatients, JoinType.LEFT).get(GroupeDePatient_.id)));
            }
            if (criteria.getCentreId() != null) {
                specification = specification.and(buildSpecification(criteria.getCentreId(),
                    root -> root.join(Medecin_.centre, JoinType.LEFT).get(Centre_.id)));
            }
            if (criteria.getSpecialtyId() != null) {
                specification = specification.and(buildSpecification(criteria.getSpecialtyId(),
                    root -> root.join(Medecin_.specialty, JoinType.LEFT).get(Specialty_.id)));
            }
        }
        return specification;
    }
}
