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

import com.mycompany.myapp.domain.PatientQuestionnaire;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.PatientQuestionnaireRepository;
import com.mycompany.myapp.service.dto.PatientQuestionnaireCriteria;

/**
 * Service for executing complex queries for {@link PatientQuestionnaire} entities in the database.
 * The main input is a {@link PatientQuestionnaireCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link PatientQuestionnaire} or a {@link Page} of {@link PatientQuestionnaire} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PatientQuestionnaireQueryService extends QueryService<PatientQuestionnaire> {

    private final Logger log = LoggerFactory.getLogger(PatientQuestionnaireQueryService.class);

    private final PatientQuestionnaireRepository patientQuestionnaireRepository;

    public PatientQuestionnaireQueryService(PatientQuestionnaireRepository patientQuestionnaireRepository) {
        this.patientQuestionnaireRepository = patientQuestionnaireRepository;
    }

    /**
     * Return a {@link List} of {@link PatientQuestionnaire} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<PatientQuestionnaire> findByCriteria(PatientQuestionnaireCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<PatientQuestionnaire> specification = createSpecification(criteria);
        return patientQuestionnaireRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link PatientQuestionnaire} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<PatientQuestionnaire> findByCriteria(PatientQuestionnaireCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<PatientQuestionnaire> specification = createSpecification(criteria);
        return patientQuestionnaireRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PatientQuestionnaireCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<PatientQuestionnaire> specification = createSpecification(criteria);
        return patientQuestionnaireRepository.count(specification);
    }

    /**
     * Function to convert {@link PatientQuestionnaireCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<PatientQuestionnaire> createSpecification(PatientQuestionnaireCriteria criteria) {
        Specification<PatientQuestionnaire> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), PatientQuestionnaire_.id));
            }
            if (criteria.getDoneTimeDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDoneTimeDate(), PatientQuestionnaire_.doneTimeDate));
            }
            if (criteria.getDone() != null) {
                specification = specification.and(buildSpecification(criteria.getDone(), PatientQuestionnaire_.done));
            }
            if (criteria.getPatientReponseId() != null) {
                specification = specification.and(buildSpecification(criteria.getPatientReponseId(),
                    root -> root.join(PatientQuestionnaire_.patientReponses, JoinType.LEFT).get(PatientReponse_.id)));
            }
            if (criteria.getPatientId() != null) {
                specification = specification.and(buildSpecification(criteria.getPatientId(),
                    root -> root.join(PatientQuestionnaire_.patient, JoinType.LEFT).get(Patient_.id)));
            }
            if (criteria.getQuestionnaireId() != null) {
                specification = specification.and(buildSpecification(criteria.getQuestionnaireId(),
                    root -> root.join(PatientQuestionnaire_.questionnaire, JoinType.LEFT).get(Questionnaire_.id)));
            }
        }
        return specification;
    }
}
