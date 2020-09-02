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

import com.mycompany.myapp.domain.Questionnaire;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.QuestionnaireRepository;
import com.mycompany.myapp.service.dto.QuestionnaireCriteria;

/**
 * Service for executing complex queries for {@link Questionnaire} entities in the database.
 * The main input is a {@link QuestionnaireCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Questionnaire} or a {@link Page} of {@link Questionnaire} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuestionnaireQueryService extends QueryService<Questionnaire> {

    private final Logger log = LoggerFactory.getLogger(QuestionnaireQueryService.class);

    private final QuestionnaireRepository questionnaireRepository;

    public QuestionnaireQueryService(QuestionnaireRepository questionnaireRepository) {
        this.questionnaireRepository = questionnaireRepository;
    }

    /**
     * Return a {@link List} of {@link Questionnaire} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Questionnaire> findByCriteria(QuestionnaireCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Questionnaire> specification = createSpecification(criteria);
        return questionnaireRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Questionnaire} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Questionnaire> findByCriteria(QuestionnaireCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Questionnaire> specification = createSpecification(criteria);
        return questionnaireRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QuestionnaireCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Questionnaire> specification = createSpecification(criteria);
        return questionnaireRepository.count(specification);
    }

    /**
     * Function to convert {@link QuestionnaireCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Questionnaire> createSpecification(QuestionnaireCriteria criteria) {
        Specification<Questionnaire> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Questionnaire_.id));
            }
            if (criteria.getSubject() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSubject(), Questionnaire_.subject));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), Questionnaire_.startDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), Questionnaire_.endDate));
            }
            if (criteria.getPatientQuestionnaireId() != null) {
                specification = specification.and(buildSpecification(criteria.getPatientQuestionnaireId(),
                    root -> root.join(Questionnaire_.patientQuestionnaires, JoinType.LEFT).get(PatientQuestionnaire_.id)));
            }
            if (criteria.getQuestionId() != null) {
                specification = specification.and(buildSpecification(criteria.getQuestionId(),
                    root -> root.join(Questionnaire_.questions, JoinType.LEFT).get(Question_.id)));
            }
            if (criteria.getMedecinId() != null) {
                specification = specification.and(buildSpecification(criteria.getMedecinId(),
                    root -> root.join(Questionnaire_.medecin, JoinType.LEFT).get(Medecin_.id)));
            }
        }
        return specification;
    }
}
