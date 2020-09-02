package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.AdminDeCentre;
import com.mycompany.myapp.domain.Medecin;
import com.mycompany.myapp.domain.Patient;
import com.mycompany.myapp.repository.AdminDeCentreRepository;
import com.mycompany.myapp.repository.MedecinRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.service.PatientService;
import com.mycompany.myapp.service.UserService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.web.rest.errors.LoginAlreadyUsedException;
import com.mycompany.myapp.service.dto.PatientCriteria;
import com.mycompany.myapp.service.EmailAlreadyUsedException;
import com.mycompany.myapp.service.PatientQueryService;

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
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Patient}.
 */
@RestController
@RequestMapping("/api")
public class PatientResource {

    private final Logger log = LoggerFactory.getLogger(PatientResource.class);

    private static final String ENTITY_NAME = "patient";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;

    private UserRepository userRepository;

    private final PatientService patientService;

    private final AdminDeCentreRepository adminDeCentreRepository;

    private final MedecinRepository medecinRepository;

    private final PatientQueryService patientQueryService;

    public PatientResource(AdminDeCentreRepository adminDeCentreRepository, MedecinRepository medecinRepository, UserService userService, UserRepository userRepository, PatientService patientService,
            PatientQueryService patientQueryService) {
this.medecinRepository=medecinRepository;
        this.userRepository = userRepository;
        this.patientService = patientService;
        this.patientQueryService = patientQueryService;
        this.userService = userService;
        this.adminDeCentreRepository = adminDeCentreRepository;
    }

    /**
     * {@code POST  /patients} : Create a new patient.
     *
     * @param patient the patient to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new patient, or with status {@code 400 (Bad Request)} if the
     *         patient has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/patients")
    public ResponseEntity<Patient> createPatient(@Valid @RequestBody Patient patient) throws URISyntaxException {
        log.debug("REST request to save Patient : {}", patient);
        boolean isAdminDeCentre = SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN_DE_CENTRE);
        patient.getUser().setId(null);
        if (patient.getId() != null) {
            throw new BadRequestAlertException("A new patient cannot already have an ID", ENTITY_NAME, "idexists");
        }
        if (patient.getUser().getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
            // Lowercase the user login before comparing with database
        } else if (userRepository.findOneByLogin(patient.getUser().getLogin().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        } else if (userRepository.findOneByEmailIgnoreCase(patient.getUser().getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        } else {

            if (isAdminDeCentre) {
                AdminDeCentre adc = adminDeCentreRepository.findOneByUserIsCurrentUser();
                patient.setCentre(adc.getCentre());
            }
            Patient result = userService.createPatient(patient.getUser(), patient.getPhone(), patient.getAdress(),
                    patient.getSexe(), patient.getAlcool(), patient.getStartDateAlcool(), patient.getEndDateAlcool(),
                    patient.getTobacoo(), patient.getStartDateTobacco(), patient.getEndDateTobacco(),
                    patient.getCentre(), patient.getMedecin());
            return ResponseEntity
                    .created(new URI("/api/medecins/" + result.getId())).headers(HeaderUtil
                            .createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
                    .body(result);
        }
    }

    /**
     * {@code PUT  /patients} : Updates an existing patient.
     *
     * @param patient the patient to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body
     *         the updated patient, or with status {@code 400 (Bad Request)} if the
     *         patient is not valid, or with status
     *         {@code 500 (Internal Server Error)} if the patient couldn't be
     *         updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/patients")
    public ResponseEntity<Patient> updatePatient(@Valid @RequestBody Patient patient) throws URISyntaxException {
        log.debug("REST request to update Patient : {}", patient);
        if (patient.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Patient result = patientService.save(patient);
        return ResponseEntity.ok().headers(
                HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, patient.getId().toString()))
                .body(result);
    }

    /**
     * {@code GET  /patients} : get all the patients.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list
     *         of patients in body.
     */
    @GetMapping("/patients")
    public ResponseEntity<List<Patient>> getAllPatients(PatientCriteria criteria, Pageable pageable) {
        log.debug("REST request to get Patients by criteria: {}", criteria);
        boolean isMedecin = SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.MEDECIN);
        boolean isAdminDeCentre = SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN_DE_CENTRE);
        
        LongFilter longFilter = new LongFilter();
        if (isMedecin) {
        Medecin m =  medecinRepository.findOneByUserIsCurrentUser() ;
        longFilter.setEquals(m.getId());
        criteria.setMedecinId(longFilter); }
        else if (isAdminDeCentre) {

         AdminDeCentre adc = adminDeCentreRepository.findOneByUserIsCurrentUser();
         longFilter.setEquals(adc.getCentre().getId());
         criteria.setCentreId(longFilter);
        }
        

        Page<Patient> page = patientQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /patients/count} : count all the patients.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/patients/count")
    public ResponseEntity<Long> countPatients(PatientCriteria criteria) {
        log.debug("REST request to count Patients by criteria: {}", criteria);
        return ResponseEntity.ok().body(patientQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /patients/:id} : get the "id" patient.
     *
     * @param id the id of the patient to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the patient, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/patients/{id}")
    public ResponseEntity<Patient> getPatient(@PathVariable Long id) {
        log.debug("REST request to get Patient : {}", id);
        Optional<Patient> patient = patientService.findOne(id);
        return ResponseUtil.wrapOrNotFound(patient);
    }

    /**
     * {@code DELETE  /patients/:id} : delete the "id" patient.
     *
     * @param id the id of the patient to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/patients/{id}")
    public ResponseEntity<Void> deletePatient(@PathVariable Long id) {
        log.debug("REST request to delete Patient : {}", id);
        patientService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
