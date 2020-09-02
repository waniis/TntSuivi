package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.PatientReponse;
import com.mycompany.myapp.service.PatientReponseService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.PatientReponseCriteria;
import com.mycompany.myapp.service.PatientReponseQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.PatientReponse}.
 */
@RestController
@RequestMapping("/api")
public class PatientReponseResource {

    private final Logger log = LoggerFactory.getLogger(PatientReponseResource.class);

    private static final String ENTITY_NAME = "patientReponse";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PatientReponseService patientReponseService;

    private final PatientReponseQueryService patientReponseQueryService;

    public PatientReponseResource(PatientReponseService patientReponseService, PatientReponseQueryService patientReponseQueryService) {
        this.patientReponseService = patientReponseService;
        this.patientReponseQueryService = patientReponseQueryService;
    }

    /**
     * {@code POST  /patient-reponses} : Create a new patientReponse.
     *
     * @param patientReponse the patientReponse to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new patientReponse, or with status {@code 400 (Bad Request)} if the patientReponse has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/patient-reponses")
    public ResponseEntity<PatientReponse> createPatientReponse(@Valid @RequestBody PatientReponse patientReponse) throws URISyntaxException {
        log.debug("REST request to save PatientReponse : {}", patientReponse);
        if (patientReponse.getId() != null) {
            throw new BadRequestAlertException("A new patientReponse cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PatientReponse result = patientReponseService.save(patientReponse);
        return ResponseEntity.created(new URI("/api/patient-reponses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /patient-reponses} : Updates an existing patientReponse.
     *
     * @param patientReponse the patientReponse to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patientReponse,
     * or with status {@code 400 (Bad Request)} if the patientReponse is not valid,
     * or with status {@code 500 (Internal Server Error)} if the patientReponse couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/patient-reponses")
    public ResponseEntity<PatientReponse> updatePatientReponse(@Valid @RequestBody PatientReponse patientReponse) throws URISyntaxException {
        log.debug("REST request to update PatientReponse : {}", patientReponse);
        if (patientReponse.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        PatientReponse result = patientReponseService.save(patientReponse);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, patientReponse.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /patient-reponses} : get all the patientReponses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of patientReponses in body.
     */
    @GetMapping("/patient-reponses")
    public ResponseEntity<List<PatientReponse>> getAllPatientReponses(PatientReponseCriteria criteria) {
        log.debug("REST request to get PatientReponses by criteria: {}", criteria);
        List<PatientReponse> entityList = patientReponseQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);
    }

    /**
     * {@code GET  /patient-reponses/count} : count all the patientReponses.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/patient-reponses/count")
    public ResponseEntity<Long> countPatientReponses(PatientReponseCriteria criteria) {
        log.debug("REST request to count PatientReponses by criteria: {}", criteria);
        return ResponseEntity.ok().body(patientReponseQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /patient-reponses/:id} : get the "id" patientReponse.
     *
     * @param id the id of the patientReponse to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the patientReponse, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/patient-reponses/{id}")
    public ResponseEntity< List<PatientReponse> > getPatientReponse(@PathVariable Long id) {
        log.debug("REST request to get PatientReponse : {}", id);
        List<PatientReponse> patientReponse = patientReponseService.FindByIdPatientQuestionnaire(id);
        return ResponseEntity.ok().body(patientReponse);
        
    }
    

    /**
     * {@code DELETE  /patient-reponses/:id} : delete the "id" patientReponse.
     *
     * @param id the id of the patientReponse to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/patient-reponses/{id}")
    public ResponseEntity<Void> deletePatientReponse(@PathVariable Long id) {
        log.debug("REST request to delete PatientReponse : {}", id);
        patientReponseService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
