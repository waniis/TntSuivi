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

import com.mycompany.myapp.domain.PatientReponse;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.PatientReponseRepository;
import com.mycompany.myapp.service.dto.PatientReponseCriteria;

/**
 * Service for executing complex queries for {@link PatientReponse} entities in the database.
 * The main input is a {@link PatientReponseCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PatientReponse} or a {@link Page} of {@link PatientReponse} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PatientReponseQueryService extends QueryService<PatientReponse> {

    private final Logger log = LoggerFactory.getLogger(PatientReponseQueryService.class);

    private final PatientReponseRepository patientReponseRepository;

    public PatientReponseQueryService(PatientReponseRepository patientReponseRepository) {
        this.patientReponseRepository = patientReponseRepository;
    }

    /**
     * Return a {@link List} of {@link PatientReponse} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PatientReponse> findByCriteria(PatientReponseCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PatientReponse> specification = createSpecification(criteria);
        return patientReponseRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PatientReponse} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PatientReponse> findByCriteria(PatientReponseCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PatientReponse> specification = createSpecification(criteria);
        return patientReponseRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PatientReponseCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PatientReponse> specification = createSpecification(criteria);
        return patientReponseRepository.count(specification);
    }

    /**
     * Function to convert {@link PatientReponseCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PatientReponse> createSpecification(PatientReponseCriteria criteria) {
        Specification<PatientReponse> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PatientReponse_.id));
            }
            if (criteria.getContent() != null) {
                specification = specification.and(buildStringSpecification(criteria.getContent(), PatientReponse_.content));
            }
            if (criteria.getPatientQuestionnaireId() != null) {
                specification = specification.and(buildSpecification(criteria.getPatientQuestionnaireId(),
                    root -> root.join(PatientReponse_.patientQuestionnaire, JoinType.LEFT).get(PatientQuestionnaire_.id)));
            }
            if (criteria.getQuestionId() != null) {
                specification = specification.and(buildSpecification(criteria.getQuestionId(),
                    root -> root.join(PatientReponse_.question, JoinType.LEFT).get(Question_.id)));
            }
            if (criteria.getQuestionAnswerId() != null) {
                specification = specification.and(buildSpecification(criteria.getQuestionAnswerId(),
                    root -> root.join(PatientReponse_.questionAnswer, JoinType.LEFT).get(QuestionAnswer_.id)));
            }
        }
        return specification;
    }
}
