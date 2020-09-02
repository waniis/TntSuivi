package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Questionnaire;
import com.mycompany.myapp.repository.MedecinRepository;
import com.mycompany.myapp.repository.QuestionnaireRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.service.QuestionnaireService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.QuestionnaireCriteria;
import com.mycompany.myapp.service.QuestionnaireQueryService;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Questionnaire}.
 */
@RestController
@RequestMapping("/api")
public class QuestionnaireResource {

    private final Logger log = LoggerFactory.getLogger(QuestionnaireResource.class);

    private static final String ENTITY_NAME = "questionnaire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuestionnaireService questionnaireService;

    private final QuestionnaireRepository questionnaireRepository;

    private final QuestionnaireQueryService questionnaireQueryService;
    private final  MedecinRepository medecinRepository;

    public QuestionnaireResource(MedecinRepository medecinRepository, QuestionnaireRepository questionnaireRepository,  QuestionnaireService questionnaireService, QuestionnaireQueryService questionnaireQueryService) {
        this.questionnaireService = questionnaireService;
        this.questionnaireRepository=questionnaireRepository;
        this.questionnaireQueryService = questionnaireQueryService;
        this.medecinRepository=medecinRepository;
    }

    /**
     * {@code POST  /questionnaires} : Create a new questionnaire.
     *
     * @param questionnaire the questionnaire to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new questionnaire, or with status {@code 400 (Bad Request)} if the questionnaire has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/questionnaires")
    public ResponseEntity<Questionnaire> createQuestionnaire(@RequestBody Questionnaire questionnaire) throws URISyntaxException {
        log.debug("REST request to save Questionnaire : {}", questionnaire);
        if (questionnaire.getId() != null) {
            throw new BadRequestAlertException("A new questionnaire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        questionnaire.setMedecin(medecinRepository.findOneByUserIsCurrentUser());
        Questionnaire result = questionnaireService.save(questionnaire);
        return ResponseEntity.created(new URI("/api/questionnaires/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /questionnaires} : Updates an existing questionnaire.
     *
     * @param questionnaire the questionnaire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated questionnaire,
     * or with status {@code 400 (Bad Request)} if the questionnaire is not valid,
     * or with status {@code 500 (Internal Server Error)} if the questionnaire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/questionnaires")
    public ResponseEntity<Questionnaire> updateQuestionnaire(@RequestBody Questionnaire questionnaire) throws URISyntaxException {
        log.debug("REST request to update Questionnaire : {}", questionnaire);
        if (questionnaire.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        Questionnaire result = questionnaireService.save(questionnaire);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, questionnaire.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /questionnaires} : get all the questionnaires.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questionnaires in body.
     */
    @GetMapping("/questionnaires")
    public ResponseEntity<List<Questionnaire>> getAllQuestionnaires(QuestionnaireCriteria criteria) {
    
        log.debug("REST request to get Questionnaires by criteria: {}", criteria);
        boolean isMedecin =  SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.MEDECIN);
        boolean isPatient =  SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.PATIENT);

       // boolean isAdmin =  SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.ADMIN);
        if (isMedecin) {
        List<Questionnaire> entityList = questionnaireRepository.findByUserIsCurrentUser();
        return ResponseEntity.ok().body(entityList); }
        else if (isPatient) {
            List<Questionnaire> entityList = questionnaireRepository.findAllByPatient();
            return ResponseEntity.ok().body(entityList);
        }
        else
        {
            throw new BadRequestAlertException("Bad request ", "Bad request", "Bad request");}
    }

    /**
     * {@code GET  /questionnaires/count} : count all the questionnaires.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/questionnaires/count")
    public ResponseEntity<Long> countQuestionnaires(QuestionnaireCriteria criteria) {
        log.debug("REST request to count Questionnaires by criteria: {}", criteria);
        return ResponseEntity.ok().body(questionnaireQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /questionnaires/:id} : get the "id" questionnaire.
     *
     * @param id the id of the questionnaire to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the questionnaire, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/questionnaires/{id}")
    public ResponseEntity<Questionnaire> getQuestionnaire(@PathVariable Long id) {
        log.debug("REST request to get Questionnaire : {}", id);
        boolean isMedecin =  SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.MEDECIN);
        boolean isPatient =  SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.PATIENT);
        if (isMedecin) {
        Optional<Questionnaire> questionnaire = questionnaireRepository.findByIdAndUserIsCurrentUser(id);
        return ResponseUtil.wrapOrNotFound(questionnaire);
    } 
    else  if ( isPatient){

       // throw new BadRequestAlertException("Invalid Request", "Invalid Request", "Invalid Request");
         Long ispatientq = questionnaireRepository.countp(id); // Si le patient participe a cet questionnaire return > 0
         if  (ispatientq <1) 
         throw new BadRequestAlertException("Bad request ", "Bad request", "Bad request");
          else {
       Questionnaire questionnaire = questionnaireRepository.findoneBypatientAndid(id);
        int value = questionnaire.getEndDate().compareTo(Instant.now()); 
        if ( value <0 ) 
        throw new BadRequestAlertException("Questionnaire termineé", "Questionnaire termineé", "Questionnaire termineé : "+questionnaire.getEndDate());
        else 
       return ResponseEntity.ok(questionnaire);
    } }
    else    throw new BadRequestAlertException("Bad request ", "Bad request", "Bad request");
    }
    /**
     * {@code DELETE  /questionnaires/:id} : delete the "id" questionnaire.
     *
     * @param id the id of the questionnaire to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/questionnaires/{id}")
    public ResponseEntity<Void> deleteQuestionnaire(@PathVariable Long id) {
        log.debug("REST request to delete Questionnaire : {}", id);
        questionnaireService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
