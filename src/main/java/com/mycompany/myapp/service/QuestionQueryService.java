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

import com.mycompany.myapp.domain.Question;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.QuestionRepository;
import com.mycompany.myapp.service.dto.QuestionCriteria;

/**
 * Service for executing complex queries for {@link Question} entities in the database.
 * The main input is a {@link QuestionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Question} or a {@link Page} of {@link Question} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuestionQueryService extends QueryService<Question> {

    private final Logger log = LoggerFactory.getLogger(QuestionQueryService.class);

    private final QuestionRepository questionRepository;

    public QuestionQueryService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    /**
     * Return a {@link List} of {@link Question} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Question> findByCriteria(QuestionCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Question> specification = createSpecification(criteria);
        return questionRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Question} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Question> findByCriteria(QuestionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Question> specification = createSpecification(criteria);
        return questionRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QuestionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Question> specification = createSpecification(criteria);
        return questionRepository.count(specification);
    }

    /**
     * Function to convert {@link QuestionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Question> createSpecification(QuestionCriteria criteria) {
        Specification<Question> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Question_.id));
            }
            if (criteria.getLabel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLabel(), Question_.label));
            }
            if (criteria.getTypeQuestion() != null) {
                specification = specification.and(buildSpecification(criteria.getTypeQuestion(), Question_.typeQuestion));
            }
            if (criteria.getQuestionAnswerId() != null) {
                specification = specification.and(buildSpecification(criteria.getQuestionAnswerId(),
                    root -> root.join(Question_.questionAnswers, JoinType.LEFT).get(QuestionAnswer_.id)));
            }
            if (criteria.getPatientReponseId() != null) {
                specification = specification.and(buildSpecification(criteria.getPatientReponseId(),
                    root -> root.join(Question_.patientReponses, JoinType.LEFT).get(PatientReponse_.id)));
            }
            if (criteria.getMedecinId() != null) {
                specification = specification.and(buildSpecification(criteria.getMedecinId(),
                    root -> root.join(Question_.medecin, JoinType.LEFT).get(Medecin_.id)));
            }
            if (criteria.getQuestionnaireId() != null) {
                specification = specification.and(buildSpecification(criteria.getQuestionnaireId(),
                    root -> root.join(Question_.questionnaires, JoinType.LEFT).get(Questionnaire_.id)));
            }
        }
        return specification;
    }
}
