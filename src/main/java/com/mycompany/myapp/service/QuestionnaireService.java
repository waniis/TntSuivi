package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Questionnaire;
import com.mycompany.myapp.repository.QuestionnaireRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Questionnaire}.
 */
@Service
@Transactional
public class QuestionnaireService {

    private final Logger log = LoggerFactory.getLogger(QuestionnaireService.class);

    private final QuestionnaireRepository questionnaireRepository;

    public QuestionnaireService(QuestionnaireRepository questionnaireRepository) {
        this.questionnaireRepository = questionnaireRepository;
    }

    /**
     * Save a questionnaire.
     *
     * @param questionnaire the entity to save.
     * @return the persisted entity.
     */
    public Questionnaire save(Questionnaire questionnaire) {
        log.debug("Request to save Questionnaire : {}", questionnaire);
        return questionnaireRepository.save(questionnaire);
    }

    /**
     * Get all the questionnaires.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Questionnaire> findAll() {
        log.debug("Request to get all Questionnaires");
        return questionnaireRepository.findAllWithEagerRelationships();
    }

    /**
     * Get all the questionnaires with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<Questionnaire> findAllWithEagerRelationships(Pageable pageable) {
        return questionnaireRepository.findAllWithEagerRelationships(pageable);
    }

    /**
     * Get one questionnaire by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Questionnaire> findOne(Long id) {
        log.debug("Request to get Questionnaire : {}", id);
        return questionnaireRepository.findOneWithEagerRelationships(id);
    }

    /**
     * Delete the questionnaire by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Questionnaire : {}", id);
        questionnaireRepository.deleteById(id);
    }
}
