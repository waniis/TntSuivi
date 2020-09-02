package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.AdminDeCentre;
import com.mycompany.myapp.domain.Medecin;
import com.mycompany.myapp.repository.AdminDeCentreRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.service.MedecinService;
import com.mycompany.myapp.service.UserService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.rest.errors.LoginAlreadyUsedException;
import com.mycompany.myapp.service.dto.MedecinCriteria;
import com.mycompany.myapp.service.EmailAlreadyUsedException;
import com.mycompany.myapp.service.MedecinQueryService;

import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Medecin}.
 */
@RestController
@RequestMapping("/api")
public class MedecinResource {

    private final Logger log = LoggerFactory.getLogger(MedecinResource.class);

    private static final String ENTITY_NAME = "medecin";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MedecinService medecinService;

    private  final UserRepository userRepository;

    private final MedecinQueryService medecinQueryService;

    private final UserService userService;
    
    private final AdminDeCentreRepository adminDeCentreRepository;

    public MedecinResource(AdminDeCentreRepository adminDeCentreRepository, MedecinService medecinService, MedecinQueryService medecinQueryService ,UserService userService ,UserRepository userRepository ) {
        this.medecinService = medecinService;
        this.medecinQueryService = medecinQueryService;
        this.userService=userService;
        this.userRepository=userRepository;
        this.adminDeCentreRepository=adminDeCentreRepository;
    }

    /**
     * {@code POST  /medecins} : Create a new medecin.
     *
     * @param medecin the medecin to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new medecin, or with status {@code 400 (Bad Request)} if the medecin has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/medecins")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\") or hasAuthority(\"" + AuthoritiesConstants.ADMIN_DE_CENTRE + "\")")
    public ResponseEntity<Medecin> createMedecin(@Valid @RequestBody Medecin medecin ) throws URISyntaxException {
        log.debug("REST request to save Medecin : {}", medecin);
        boolean isAdminDeCentre = SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN_DE_CENTRE);
        medecin.getUser().setId(null);
        if (medecin.getId() != null) {
            throw new BadRequestAlertException("A new medecin cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (medecin.getUser().getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
            // Lowercase the user login before comparing with database
        } else if (userRepository.findOneByLogin(medecin.getUser().getLogin().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        } else if (userRepository.findOneByEmailIgnoreCase(medecin.getUser().getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        } else {  
            if (isAdminDeCentre) {
                AdminDeCentre adc = adminDeCentreRepository.findOneByUserIsCurrentUser();
                medecin.setCentre(adc.getCentre());
            }
        Medecin result = userService.createMedecin(medecin.getUser() , medecin.getPhone() , medecin.getPhone2() ,medecin.getAdress(), medecin.getSexe(), medecin.getCentre() , medecin.getSpecialty());
        return ResponseEntity.created(new URI("/api/medecins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }}

    /**
     * {@code PUT  /medecins} : Updates an existing medecin.
     *
     * @param medecin the medecin to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated medecin,
     * or with status {@code 400 (Bad Request)} if the medecin is not valid,
     * or with status {@code 500 (Internal Server Error)} if the medecin couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/medecins")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\") or hasAuthority(\"" + AuthoritiesConstants.ADMIN_DE_CENTRE + "\")")
    public ResponseEntity<Medecin> updateMedecin(@Valid @RequestBody Medecin medecin) throws URISyntaxException {
        log.debug("REST request to update Medecin : {}", medecin);
        if (medecin.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Medecin result = medecinService.save(medecin);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, medecin.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /medecins} : get all the medecins.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of medecins in body.
     */
    @GetMapping("/medecins")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\") or hasAuthority(\"" + AuthoritiesConstants.ADMIN_DE_CENTRE + "\")")
    public ResponseEntity<List<Medecin>> getAllMedecins(MedecinCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Medecins by criteria: {}", criteria);
        boolean isAdminDeCentre = SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN_DE_CENTRE);
        LongFilter longFilter = new LongFilter();
        
         if (isAdminDeCentre) {
            AdminDeCentre adc = adminDeCentreRepository.findOneByUserIsCurrentUser();
            longFilter.setEquals(adc.getCentre().getId());
            criteria.setCentreId(longFilter);
           }

        Page<Medecin> page = medecinQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /medecins/count} : count all the medecins.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/medecins/count")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\") or hasAuthority(\"" + AuthoritiesConstants.ADMIN_DE_CENTRE + "\")")
    public ResponseEntity<Long> countMedecins(MedecinCriteria criteria) {
        log.debug("REST request to count Medecins by criteria: {}", criteria);
        return ResponseEntity.ok().body(medecinQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /medecins/:id} : get the "id" medecin.
     *
     * @param id the id of the medecin to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the medecin, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/medecins/{id}")
   
    public ResponseEntity<Medecin> getMedecin(@PathVariable Long id) {
        log.debug("REST request to get Medecin : {}", id);
        Optional<Medecin> medecin = medecinService.findOne(id);
        return ResponseUtil.wrapOrNotFound(medecin);
    }

    /**
     * {@code DELETE  /medecins/:id} : delete the "id" medecin.
     *
     * @param id the id of the medecin to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/medecins/{id}")
    //@PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteMedecin(@PathVariable Long id) {
        log.debug("REST request to delete Medecin : {}", id);
        medecinService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
