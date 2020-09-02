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

import com.mycompany.myapp.domain.Patient;
import com.mycompany.myapp.domain.*; // for static metamodels
import com.mycompany.myapp.repository.PatientRepository;
import com.mycompany.myapp.service.dto.PatientCriteria;

/**
 * Service for executing complex queries for {@link Patient} entities in the database.
 * The main input is a {@link PatientCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link List} of {@link Patient} or a {@link Page} of {@link Patient} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class PatientQueryService extends QueryService<Patient> {

    private final Logger log = LoggerFactory.getLogger(PatientQueryService.class);

    private final PatientRepository patientRepository;

    public PatientQueryService(PatientRepository patientRepository) {
        this.patientRepository = patientRepository;
    }

    /**
     * Return a {@link List} of {@link Patient} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public List<Patient> findByCriteria(PatientCriteria criteria) {
        log.debug("find by criteria : {}", criteria);
        final Specification<Patient> specification = createSpecification(criteria);
        return patientRepository.findAll(specification);
    }

    /**
     * Return a {@link Page} of {@link Patient} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<Patient> findByCriteria(PatientCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<Patient> specification = createSpecification(criteria);
        return patientRepository.findAll(specification, page);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(PatientCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<Patient> specification = createSpecification(criteria);
        return patientRepository.count(specification);
    }

    /**
     * Function to convert {@link PatientCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<Patient> createSpecification(PatientCriteria criteria) {
        Specification<Patient> specification = Specification.where(null);
        if (criteria != null) {
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), Patient_.id));
            }
            if (criteria.getFullName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getFullName(), Patient_.fullName));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), Patient_.phone));
            }
            if (criteria.getAdress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAdress(), Patient_.adress));
            }
            if (criteria.getSexe() != null) {
                specification = specification.and(buildSpecification(criteria.getSexe(), Patient_.sexe));
            }
            if (criteria.getAlcool() != null) {
                specification = specification.and(buildSpecification(criteria.getAlcool(), Patient_.alcool));
            }
            if (criteria.getStartDateAlcool() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDateAlcool(), Patient_.startDateAlcool));
            }
            if (criteria.getEndDateAlcool() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDateAlcool(), Patient_.endDateAlcool));
            }
            if (criteria.getTobacoo() != null) {
                specification = specification.and(buildSpecification(criteria.getTobacoo(), Patient_.tobacoo));
            }
            if (criteria.getStartDateTobacco() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDateTobacco(), Patient_.startDateTobacco));
            }
            if (criteria.getEndDateTobacco() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDateTobacco(), Patient_.endDateTobacco));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(buildSpecification(criteria.getUserId(),
                    root -> root.join(Patient_.user, JoinType.LEFT).get(User_.id)));
            }
            if (criteria.getPatientQuestionnaireId() != null) {
                specification = specification.and(buildSpecification(criteria.getPatientQuestionnaireId(),
                    root -> root.join(Patient_.patientQuestionnaires, JoinType.LEFT).get(PatientQuestionnaire_.id)));
            }
            if (criteria.getMedecinId() != null) {
                specification = specification.and(buildSpecification(criteria.getMedecinId(),
                    root -> root.join(Patient_.medecin, JoinType.LEFT).get(Medecin_.id)));
            }
            if (criteria.getCentreId() != null) {
                specification = specification.and(buildSpecification(criteria.getCentreId(),
                    root -> root.join(Patient_.centre, JoinType.LEFT).get(Centre_.id)));
            }
            if (criteria.getGroupeDePatientId() != null) {
                specification = specification.and(buildSpecification(criteria.getGroupeDePatientId(),
                    root -> root.join(Patient_.groupeDePatients, JoinType.LEFT).get(GroupeDePatient_.id)));
            }
        }
        return specification;
    }
}
