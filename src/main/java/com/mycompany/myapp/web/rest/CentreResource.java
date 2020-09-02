package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.AdminDeCentre;
import com.mycompany.myapp.domain.Centre;
import com.mycompany.myapp.repository.AdminDeCentreRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.service.CentreService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.CentreCriteria;
import com.mycompany.myapp.service.CentreQueryService;

import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.transaction.Transactional;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Centre}.
 */
@RestController
@RequestMapping("/api")
public class CentreResource {

    private final Logger log = LoggerFactory.getLogger(CentreResource.class);

    private static final String ENTITY_NAME = "centre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CentreService centreService;

    private final AdminDeCentreRepository adminDeCentreRepository;
    private final CentreQueryService centreQueryService;

    public CentreResource(AdminDeCentreRepository adminDeCentreRepository, CentreService centreService, CentreQueryService centreQueryService) {
        this.centreService = centreService;
        this.adminDeCentreRepository=adminDeCentreRepository;
        this.centreQueryService = centreQueryService;
    }

    /**
     * {@code POST  /centres} : Create a new centre.
     *
     * @param centre the centre to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new centre, or with status {@code 400 (Bad Request)} if the centre has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/centres")
   
    public ResponseEntity<Centre> createCentre(@Valid @RequestBody Centre centre) throws URISyntaxException {
        log.debug("REST request to save Centre : {}", centre);
        if (centre.getId() != null) {
            throw new BadRequestAlertException("A new centre cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Centre result = centreService.save(centre);
        return ResponseEntity.created(new URI("/api/centres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /centres} : Updates an existing centre.
     *
     * @param centre the centre to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated centre,
     * or with status {@code 400 (Bad Request)} if the centre is not valid,
     * or with status {@code 500 (Internal Server Error)} if the centre couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/centres")
    public ResponseEntity<Centre> updateCentre(@Valid @RequestBody Centre centre) throws URISyntaxException {
        log.debug("REST request to update Centre : {}", centre);
        if (centre.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Centre result = centreService.save(centre);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, centre.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /centres} : get all the centres.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of centres in body.
     */
    @GetMapping("/centres")
    @Transactional
    public ResponseEntity<List<Centre>> getAllCentres(CentreCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Centres by criteria: {}", criteria);
        boolean isAdminDeCentre = SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN_DE_CENTRE);
        LongFilter longFilter = new LongFilter();
        if  (isAdminDeCentre) {

         AdminDeCentre adc = adminDeCentreRepository.findOneByUserIsCurrentUser();
         longFilter.setEquals(adc.getId());
         criteria.setAdminDeCentreId(longFilter);
        }
        Page<Centre> page = centreQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /centres/count} : count all the centres.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/centres/count")
    public ResponseEntity<Long> countCentres(CentreCriteria criteria) {
        log.debug("REST request to count Centres by criteria: {}", criteria);
        return ResponseEntity.ok().body(centreQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /centres/:id} : get the "id" centre.
     *
     * @param id the id of the centre to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the centre, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/centres/{id}")
    public ResponseEntity<Centre> getCentre(@PathVariable Long id) {
        log.debug("REST request to get Centre : {}", id);
        Optional<Centre> centre = centreService.findOne(id);
        return ResponseUtil.wrapOrNotFound(centre);
    }

    /**
     * {@code DELETE  /centres/:id} : delete the "id" centre.
     *
     * @param id the id of the centre to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/centres/{id}")
    public ResponseEntity<Void> deleteCentre(@PathVariable Long id) {
        log.debug("REST request to delete Centre : {}", id);
        centreService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
