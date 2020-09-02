package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.QuestionAnswer;
import com.mycompany.myapp.repository.QuestionAnswerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link QuestionAnswer}.
 */
@Service
@Transactional
public class QuestionAnswerService {

    private final Logger log = LoggerFactory.getLogger(QuestionAnswerService.class);

    private final QuestionAnswerRepository questionAnswerRepository;

    public QuestionAnswerService(QuestionAnswerRepository questionAnswerRepository) {
        this.questionAnswerRepository = questionAnswerRepository;
    }

    /**
     * Save a questionAnswer.
     *
     * @param questionAnswer the entity to save.
     * @return the persisted entity.
     */
    public QuestionAnswer save(QuestionAnswer questionAnswer) {
        log.debug("Request to save QuestionAnswer : {}", questionAnswer);
        return questionAnswerRepository.save(questionAnswer);
    }

    /**
     * Get all the questionAnswers.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<QuestionAnswer> findAll() {
        log.debug("Request to get all QuestionAnswers");
        return questionAnswerRepository.findAll();
    }

    /**
     * Get one questionAnswer by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<QuestionAnswer> findOne(Long id) {
        log.debug("Request to get QuestionAnswer : {}", id);
        return questionAnswerRepository.findById(id);
    }

    /**
     * Delete the questionAnswer by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete QuestionAnswer : {}", id);
        questionAnswerRepository.deleteById(id);
    }
}
