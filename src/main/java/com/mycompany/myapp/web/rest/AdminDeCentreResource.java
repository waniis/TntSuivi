package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.AdminDeCentre;
import com.mycompany.myapp.repository.AdminDeCentreRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.service.EmailAlreadyUsedException;
import com.mycompany.myapp.service.UserService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.rest.errors.LoginAlreadyUsedException;

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
 * REST controller for managing {@link com.mycompany.myapp.domain.AdminDeCentre}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class AdminDeCentreResource {

    private final Logger log = LoggerFactory.getLogger(AdminDeCentreResource.class);

    private static final String ENTITY_NAME = "adminDeCentre";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdminDeCentreRepository adminDeCentreRepository;

    private final UserService userService; 
    
    private UserRepository userRepository;

    public AdminDeCentreResource(UserService userService , UserRepository userRepository, AdminDeCentreRepository adminDeCentreRepository) {
        this.adminDeCentreRepository = adminDeCentreRepository;
        this.userService=userService;
        this.userRepository=userRepository;

    }

    /**
     * {@code POST  /admin-de-centres} : Create a new adminDeCentre.
     *
     * @param adminDeCentre the adminDeCentre to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new adminDeCentre, or with status {@code 400 (Bad Request)} if the adminDeCentre has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/admin-de-centres")
    public ResponseEntity<AdminDeCentre> createAdminDeCentre(@Valid @RequestBody AdminDeCentre adminDeCentre) throws URISyntaxException {
        log.debug("REST request to save Patient : {}", adminDeCentre);
        adminDeCentre.getUser().setId(null);
        if (adminDeCentre.getId() != null) {
            throw new BadRequestAlertException("A new patient cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (adminDeCentre.getUser().getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
            // Lowercase the user login before comparing with database
        } else if (userRepository.findOneByLogin(adminDeCentre.getUser().getLogin().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        } else if (userRepository.findOneByEmailIgnoreCase(adminDeCentre.getUser().getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        } else {
            AdminDeCentre  result = userService.createAdminDeCentre(adminDeCentre.getUser() , adminDeCentre.getPhone()  ,  adminDeCentre.getCentre() );
        return ResponseEntity.created(new URI("/api/medecins/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }}


    /**
     * {@code PUT  /admin-de-centres} : Updates an existing adminDeCentre.
     *
     * @param adminDeCentre the adminDeCentre to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated adminDeCentre,
     * or with status {@code 400 (Bad Request)} if the adminDeCentre is not valid,
     * or with status {@code 500 (Internal Server Error)} if the adminDeCentre couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/admin-de-centres")
    public ResponseEntity<AdminDeCentre> updateAdminDeCentre(@Valid @RequestBody AdminDeCentre adminDeCentre) throws URISyntaxException {
        log.debug("REST request to update AdminDeCentre : {}", adminDeCentre);
        if (adminDeCentre.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        AdminDeCentre result = adminDeCentreRepository.save(adminDeCentre);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, adminDeCentre.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /admin-de-centres} : get all the adminDeCentres.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of adminDeCentres in body.
     */
    @GetMapping("/admin-de-centres")
    public List<AdminDeCentre> getAllAdminDeCentres() {
        log.debug("REST request to get all AdminDeCentres");
        return adminDeCentreRepository.findAll();
    }

    /**
     * {@code GET  /admin-de-centres/:id} : get the "id" adminDeCentre.
     *
     * @param id the id of the adminDeCentre to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the adminDeCentre, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/admin-de-centres/{id}")
    public ResponseEntity<AdminDeCentre> getAdminDeCentre(@PathVariable Long id) {
        log.debug("REST request to get AdminDeCentre : {}", id);
        Optional<AdminDeCentre> adminDeCentre = adminDeCentreRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(adminDeCentre);
    }

    /**
     * {@code DELETE  /admin-de-centres/:id} : delete the "id" adminDeCentre.
     *
     * @param id the id of the adminDeCentre to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/admin-de-centres/{id}")
    public ResponseEntity<Void> deleteAdminDeCentre(@PathVariable Long id) {
        log.debug("REST request to delete AdminDeCentre : {}", id);
        adminDeCentreRepository.deleteById(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
