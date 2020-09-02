package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Medecin;
import com.mycompany.myapp.repository.MedecinRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Medecin}.
 */
@Service
@Transactional
public class MedecinService {

    private final Logger log = LoggerFactory.getLogger(MedecinService.class);

    private final MedecinRepository medecinRepository;

    public MedecinService(MedecinRepository medecinRepository) {
        this.medecinRepository = medecinRepository;
    }

    /**
     * Save a medecin.
     *
     * @param medecin the entity to save.
     * @return the persisted entity.
     */
    public Medecin save(Medecin medecin) {
        log.debug("Request to save Medecin : {}", medecin);
        return medecinRepository.save(medecin);
    }

    /**
     * Get all the medecins.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Medecin> findAll(Pageable pageable) {
        log.debug("Request to get all Medecins");
        return medecinRepository.findAll(pageable);
    }

    /**
     * Get one medecin by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Medecin> findOne(Long id) {
        log.debug("Request to get Medecin : {}", id);
        return medecinRepository.findById(id);
    }

    /**
     * Delete the medecin by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Medecin : {}", id);
        medecinRepository.deleteById(id);
    }
}
