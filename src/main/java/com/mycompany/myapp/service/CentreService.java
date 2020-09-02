package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Centre;
import com.mycompany.myapp.repository.CentreRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Centre}.
 */
@Service
@Transactional
public class CentreService {

    private final Logger log = LoggerFactory.getLogger(CentreService.class);

    private final CentreRepository centreRepository;

    public CentreService(CentreRepository centreRepository) {
        this.centreRepository = centreRepository;
    }

    /**
     * Save a centre.
     *
     * @param centre the entity to save.
     * @return the persisted entity.
     */
    public Centre save(Centre centre) {
        log.debug("Request to save Centre : {}", centre);
        return centreRepository.save(centre);
    }

    /**
     * Get all the centres.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<Centre> findAll(Pageable pageable) {
        log.debug("Request to get all Centres");
        return centreRepository.findAll(pageable);
    }

    /**
     * Get one centre by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<Centre> findOne(Long id) {
        log.debug("Request to get Centre : {}", id);
        return centreRepository.findById(id);
    }

    /**
     * Delete the centre by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Centre : {}", id);
        centreRepository.deleteById(id);
    }
}
