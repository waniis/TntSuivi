package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.GroupeDePatient;
import com.mycompany.myapp.domain.Medecin;
import com.mycompany.myapp.repository.GroupeDePatientRepository;
import com.mycompany.myapp.repository.MedecinRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.GroupeDePatient}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class GroupeDePatientResource {

    private final Logger log = LoggerFactory.getLogger(GroupeDePatientResource.class);

    private static final String ENTITY_NAME = "groupeDePatient";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GroupeDePatientRepository groupeDePatientRepository;
    private final  MedecinRepository medecinRepository;

    public GroupeDePatientResource( MedecinRepository medecinRepository, GroupeDePatientRepository groupeDePatientRepository) {
        this.groupeDePatientRepository = groupeDePatientRepository;
        this.medecinRepository=medecinRepository;
    }

    /**
     * {@code POST  /groupe-de-patients} : Create a new groupeDePatient.
     *
     * @param groupeDePatient the groupeDePatient to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new groupeDePatient, or with status {@code 400 (Bad Request)} if the groupeDePatient has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/groupe-de-patients")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MEDECIN + "\") or hasAuthority(\"" + AuthoritiesConstants.ADMIN_DE_CENTRE + "\")")
    public ResponseEntity<GroupeDePatient> createGroupeDePatient(@RequestBody GroupeDePatient groupeDePatient) throws URISyntaxException {
        log.debug("REST request to save GroupeDePatient : {}", groupeDePatient);
        boolean isMedecin = SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.MEDECIN);
        if (groupeDePatient.getId() != null) {
            throw new BadRequestAlertException("A new groupeDePatient cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (isMedecin)
        {
            Medecin medecin = medecinRepository.findOneByUserIsCurrentUser();
            groupeDePatient.setMedecin(medecin);
        }
        GroupeDePatient result = groupeDePatientRepository.save(groupeDePatient);
        return ResponseEntity.created(new URI("/api/groupe-de-patients/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /groupe-de-patients} : Updates an existing groupeDePatient.
     *
     * @param groupeDePatient the groupeDePatient to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated groupeDePatient,
     * or with status {@code 400 (Bad Request)} if the groupeDePatient is not valid,
     * or with status {@code 500 (Internal Server Error)} if the groupeDePatient couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/groupe-de-patients")
    public ResponseEntity<GroupeDePatient> updateGroupeDePatient(@RequestBody GroupeDePatient groupeDePatient) throws URISyntaxException {
        log.debug("REST request to update GroupeDePatient : {}", groupeDePatient);
        if (groupeDePatient.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        GroupeDePatient result = groupeDePatientRepository.save(groupeDePatient);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, groupeDePatient.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /groupe-de-patients} : get all the groupeDePatients.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of groupeDePatients in body.
     */
    @GetMapping("/groupe-de-patients")
    public List<GroupeDePatient> getAllGroupeDePatients(@RequestParam(required = false, defaultValue = "false") boolean eagerload) {
        log.debug("REST request to get all GroupeDePatients");
        return groupeDePatientRepository.findAllWithEagerRelationships();
    }

    /**
     * {@code GET  /groupe-de-patients/:id} : get the "id" groupeDePatient.
     *
     * @param id the id of the groupeDePatient to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the groupeDePatient, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/groupe-de-patients/{id}")
    public ResponseEntity<GroupeDePatient> getGroupeDePatient(@PathVariable Long id) {
        log.debug("REST request to get GroupeDePatient : {}", id);
        Optional<GroupeDePatient> groupeDePatient = groupeDePatientRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(groupeDePatient);
    }

    /**
     * {@code DELETE  /groupe-de-patients/:id} : delete the "id" groupeDePatient.
     *
     * @param id the id of the groupeDePatient to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/groupe-de-patients/{id}")
    public ResponseEntity<Void> deleteGroupeDePatient(@PathVariable Long id) {
        log.debug("REST request to delete GroupeDePatient : {}", id);
        groupeDePatientRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
