package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.PatientQuestionnaire;
import com.mycompany.myapp.repository.PatientQuestionnaireRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link PatientQuestionnaire}.
 */
@Service
@Transactional
public class PatientQuestionnaireService {

    private final Logger log = LoggerFactory.getLogger(PatientQuestionnaireService.class);

    private final PatientQuestionnaireRepository patientQuestionnaireRepository;

    public PatientQuestionnaireService(PatientQuestionnaireRepository patientQuestionnaireRepository) {
        this.patientQuestionnaireRepository = patientQuestionnaireRepository;
    }

    /**
     * Save a patientQuestionnaire.
     *
     * @param patientQuestionnaire the entity to save.
     * @return the persisted entity.
     */
    public PatientQuestionnaire save(PatientQuestionnaire patientQuestionnaire) {
        log.debug("Request to save PatientQuestionnaire : {}", patientQuestionnaire);
        return patientQuestionnaireRepository.save(patientQuestionnaire);
    }

    /**
     * Get all the patientQuestionnaires.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PatientQuestionnaire> findAll() {
        log.debug("Request to get all PatientQuestionnaires");
        return patientQuestionnaireRepository.findAll();
    }

    /**
     * Get one patientQuestionnaire by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PatientQuestionnaire> findOne(Long id) {
        log.debug("Request to get PatientQuestionnaire : {}", id);
        return patientQuestionnaireRepository.findById(id);
    }


    @Transactional(readOnly = true)
    public PatientQuestionnaire findOneById(Long id) {
        log.debug("Request to get PatientQuestionnaire : {}", id);
        return patientQuestionnaireRepository.findOneById(id);
    }


    @Transactional(readOnly = true)
    public PatientQuestionnaire findOneByIdAndIdPatient(Long id) {
        log.debug("Request to get PatientQuestionnaire : {}", id);
        return patientQuestionnaireRepository.findOneByIdAndIdPatient(id);
    }

    @Transactional(readOnly = true)
    public Long  countPatient(Long id) {
        log.debug("Request to get PatientQuestionnaire : {}", id);
        return patientQuestionnaireRepository.countPatient(id);
    }


    /**
     * Delete the patientQuestionnaire by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PatientQuestionnaire : {}", id);
        patientQuestionnaireRepository.deleteById(id);
    }



    @Transactional(readOnly = true)
    public Optional<PatientQuestionnaire> findOneWithQuestionsAndQuestionsAnswers(Long id) {
        log.debug("Request to get PatientQuestionnaire : {}", id);
        return patientQuestionnaireRepository.findByIdWithQuestions(id);
    }



    @Transactional(readOnly = true)
    public Optional<PatientQuestionnaire> findOneWithQuestionsAndQuestionsAnswersAndReponse(Long id) {
        log.debug("Request to get PatientQuestionnaire : {}", id);
        return patientQuestionnaireRepository.findByIdWithQuestionsAndReponse(id);
    }

}
