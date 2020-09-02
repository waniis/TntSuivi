package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.PatientReponse;
import com.mycompany.myapp.repository.PatientReponseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link PatientReponse}.
 */
@Service
@Transactional
public class PatientReponseService {

    private final Logger log = LoggerFactory.getLogger(PatientReponseService.class);

    private final PatientReponseRepository patientReponseRepository;

    public PatientReponseService(PatientReponseRepository patientReponseRepository) {
        this.patientReponseRepository = patientReponseRepository;
    }

    /**
     * Save a patientReponse.
     *
     * @param patientReponse the entity to save.
     * @return the persisted entity.
     */
    public PatientReponse save(PatientReponse patientReponse) {
        log.debug("Request to save PatientReponse : {}", patientReponse);
        return patientReponseRepository.save(patientReponse);
    }

    /**
     * Get all the patientReponses.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<PatientReponse> findAll() {
        log.debug("Request to get all PatientReponses");
        return patientReponseRepository.findAll();
    }

    /**
     * Get one patientReponse by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PatientReponse> findOne(Long id) {
        log.debug("Request to get PatientReponse : {}", id);
        return patientReponseRepository.findById(id);
    }


    @Transactional(readOnly = true)
    public List<PatientReponse> FindByIdPatientQuestionnaire(Long id) {
        log.debug("Request to get PatientReponse : {}", id);
        return patientReponseRepository.FindByIdPatientQuestionnaire(id);
    }

    /**
     * Delete the patientReponse by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PatientReponse : {}", id);
        patientReponseRepository.deleteById(id);
    }
}
