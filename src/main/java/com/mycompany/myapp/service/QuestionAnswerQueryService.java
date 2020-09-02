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

import com.mycompany.myapp.domain.QuestionAnswer;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.QuestionAnswerRepository;
import com.mycompany.myapp.service.dto.QuestionAnswerCriteria;

/**
 * Service for executing complex queries for {@link QuestionAnswer} entities in the database.
 * The main input is a {@link QuestionAnswerCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link QuestionAnswer} or a {@link Page} of {@link QuestionAnswer} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class QuestionAnswerQueryService extends QueryService<QuestionAnswer> {

    private final Logger log = LoggerFactory.getLogger(QuestionAnswerQueryService.class);

    private final QuestionAnswerRepository questionAnswerRepository;

    public QuestionAnswerQueryService(QuestionAnswerRepository questionAnswerRepository) {
        this.questionAnswerRepository = questionAnswerRepository;
    }

    /**
     * Return a {@link List} of {@link QuestionAnswer} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<QuestionAnswer> findByCriteria(QuestionAnswerCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<QuestionAnswer> specification = createSpecification(criteria);
        return questionAnswerRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link QuestionAnswer} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<QuestionAnswer> findByCriteria(QuestionAnswerCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<QuestionAnswer> specification = createSpecification(criteria);
        return questionAnswerRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(QuestionAnswerCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<QuestionAnswer> specification = createSpecification(criteria);
        return questionAnswerRepository.count(specification);
    }

    /**
     * Function to convert {@link QuestionAnswerCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<QuestionAnswer> createSpecification(QuestionAnswerCriteria criteria) {
        Specification<QuestionAnswer> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), QuestionAnswer_.id));
            }
            if (criteria.getLabel() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLabel(), QuestionAnswer_.label));
            }
            if (criteria.getPatientReponseId() != null) {
                specification = specification.and(buildSpecification(criteria.getPatientReponseId(),
                    root -> root.join(QuestionAnswer_.patientReponses, JoinType.LEFT).get(PatientReponse_.id)));
            }
            if (criteria.getQuestionId() != null) {
                specification = specification.and(buildSpecification(criteria.getQuestionId(),
                    root -> root.join(QuestionAnswer_.question, JoinType.LEFT).get(Question_.id)));
            }
        }
        return specification;
    }
}
