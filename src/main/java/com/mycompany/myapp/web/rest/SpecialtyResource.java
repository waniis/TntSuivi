package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Specialty;
import com.mycompany.myapp.repository.SpecialtyRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Specialty}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class SpecialtyResource {

    private final Logger log = LoggerFactory.getLogger(SpecialtyResource.class);

    private static final String ENTITY_NAME = "specialty";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SpecialtyRepository specialtyRepository;

    public SpecialtyResource(SpecialtyRepository specialtyRepository) {
        this.specialtyRepository = specialtyRepository;
    }

    /**
     * {@code POST  /specialties} : Create a new specialty.
     *
     * @param specialty the specialty to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new specialty, or with status {@code 400 (Bad Request)} if the specialty has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/specialties")
    public ResponseEntity<Specialty> createSpecialty(@Valid @RequestBody Specialty specialty) throws URISyntaxException {
        log.debug("REST request to save Specialty : {}", specialty);
        if (specialty.getId() != null) {
            throw new BadRequestAlertException("A new specialty cannot already have an ID", ENTITY_NAME, "idexists");
        }

        specialty.setName(specialty.getName().substring(0, 1).toUpperCase() + specialty.getName().substring(1).toLowerCase());
        if (specialtyRepository.existsByName(specialty.getName())) {
        // if ( (centreRepository.existsByRegionId(centre.getRegion().getId())) && ( centreRepository.existsByNameIgnoreCase(centre.getName()))) {
            throw new BadRequestAlertException("A new Specialty cannot already exist", ENTITY_NAME, " A new Specialty Cannot  already exist");
        }
        else {

        Specialty result = specialtyRepository.save(specialty);
        return ResponseEntity.created(new URI("/api/specialties/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }}

    /**
     * {@code PUT  /specialties} : Updates an existing specialty.
     *
     * @param specialty the specialty to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated specialty,
     * or with status {@code 400 (Bad Request)} if the specialty is not valid,
     * or with status {@code 500 (Internal Server Error)} if the specialty couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/specialties")
    public ResponseEntity<Specialty> updateSpecialty(@Valid @RequestBody Specialty specialty) throws URISyntaxException {
        log.debug("REST request to update Specialty : {}", specialty);
        if (specialty.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        specialty.setName(specialty.getName().substring(0, 1).toUpperCase() + specialty.getName().substring(1).toLowerCase());
        if (specialtyRepository.existsByName(specialty.getName())) {
        // if ( (centreRepository.existsByRegionId(centre.getRegion().getId())) && ( centreRepository.existsByNameIgnoreCase(centre.getName()))) {
            throw new BadRequestAlertException("A new Specialty cannot already exist", ENTITY_NAME, " Cannot Update Specialty , already exist");
        }
        else {

        Specialty result = specialtyRepository.save(specialty);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, specialty.getId().toString()))
            .body(result);
    }}

    /**
     * {@code GET  /specialties} : get all the specialties.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of specialties in body.
     */
    @GetMapping("/specialties")
    public List<Specialty> getAllSpecialties() {
        log.debug("REST request to get all Specialties");
        return specialtyRepository.findAll();
    }

    /**
     * {@code GET  /specialties/:id} : get the "id" specialty.
     *
     * @param id the id of the specialty to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the specialty, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/specialties/{id}")
    public ResponseEntity<Specialty> getSpecialty(@PathVariable Long id) {
        log.debug("REST request to get Specialty : {}", id);
        Optional<Specialty> specialty = specialtyRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(specialty);
    }

    /**
     * {@code DELETE  /specialties/:id} : delete the "id" specialty.
     *
     * @param id the id of the specialty to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/specialties/{id}")
    public ResponseEntity<Void> deleteSpecialty(@PathVariable Long id) {
        log.debug("REST request to delete Specialty : {}", id);
        specialtyRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
