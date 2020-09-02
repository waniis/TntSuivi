package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Medicament;
import com.mycompany.myapp.repository.MedicamentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing {@link Medicament}.
 */
@Service
@Transactional
public class MedicamentService {

    private final Logger log = LoggerFactory.getLogger(MedicamentService.class);

    private final MedicamentRepository medicamentRepository;

    public MedicamentService(MedicamentRepository medicamentRepository) {
        this.medicamentRepository = medicamentRepository;
    }

    /**
     * Save a medicament.
     *
     * @param medicament the entity to save.
     * @return the persisted entity.
     */
    public Medicament save(Medicament medicament) {
        log.debug("Request to save Medicament : {}", medicament);
        return medicamentRepository.save(medicament);
    }

    /**
     * Get all the medicaments.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<Medicament> findAll() {
        log.debug("Request to get all Medicaments");
        return medicamentRepository.findAll();
    }

    /**
     * Get one medicament by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Medicament> findOne(Long id) {
        log.debug("Request to get Medicament : {}", id);
        return medicamentRepository.findById(id);
    }

    /**
     * Delete the medicament by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Medicament : {}", id);
        medicamentRepository.deleteById(id);
    }
}
