package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.GroupeDePatient;
import com.mycompany.myapp.domain.Notification;
import com.mycompany.myapp.domain.Patient;
import com.mycompany.myapp.domain.PatientQuestionnaire;
import com.mycompany.myapp.domain.PatientQuestionnaireRequest;
import com.mycompany.myapp.domain.PatientReponse;
import com.mycompany.myapp.domain.Question;
import com.mycompany.myapp.domain.QuestionAnswer;
import com.mycompany.myapp.domain.Questionnaire;
import com.mycompany.myapp.domain.QuestionnaireGroupeDepatientRequest;
import com.mycompany.myapp.repository.GroupeDePatientRepository;
import com.mycompany.myapp.repository.PatientReponseRepository;
import com.mycompany.myapp.repository.QuestionAnswerRepository;
import com.mycompany.myapp.repository.QuestionRepository;
import com.mycompany.myapp.repository.QuestionnaireRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.service.PatientQuestionnaireService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.PatientQuestionnaireCriteria;
import com.mycompany.myapp.service.NotificationService;
import com.mycompany.myapp.service.PatientQuestionnaireQueryService;

import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;

import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;
import javax.validation.Valid;

/**
 * REST controller for managing
 * {@link com.mycompany.myapp.domain.PatientQuestionnaire}.
 */
@RestController
@RequestMapping("/api")
public class PatientQuestionnaireResource {

    private final Logger log = LoggerFactory.getLogger(PatientQuestionnaireResource.class);

    private static final String ENTITY_NAME = "patientQuestionnaire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final GroupeDePatientRepository groupeDePatientRepository;

    private final QuestionnaireRepository questionnaireRepository;
    private final QuestionRepository questionRepository;

    private final PatientQuestionnaireService patientQuestionnaireService;
    private final NotificationService notificationService;

    private final PatientReponseRepository patientReponseRepository;

    private final PatientQuestionnaireQueryService patientQuestionnaireQueryService;

    private final QuestionAnswerRepository  questionAnswerRepository;

    public PatientQuestionnaireResource(QuestionAnswerRepository questionAnswerRepository ,PatientReponseRepository  patientReponseRepository,QuestionRepository questionRepository, QuestionnaireRepository questionnaireRepository ,GroupeDePatientRepository groupeDePatientRepository,
            NotificationService notificationService, PatientQuestionnaireService patientQuestionnaireService,
            PatientQuestionnaireQueryService patientQuestionnaireQueryService) {
        this.patientQuestionnaireService = patientQuestionnaireService;
        this.patientQuestionnaireQueryService = patientQuestionnaireQueryService;
        this.notificationService = notificationService;
        this.questionRepository=questionRepository;
        this.groupeDePatientRepository = groupeDePatientRepository;
        this.questionnaireRepository=questionnaireRepository;
        this.patientReponseRepository=patientReponseRepository;
        this.questionAnswerRepository=questionAnswerRepository;
    }

    /**
     * {@code POST  /patient-questionnaires} : Create a new patientQuestionnaire.
     *
     * @param patientQuestionnaire the patientQuestionnaire to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with
     *         body the new patientQuestionnaire, or with status
     *         {@code 400 (Bad Request)} if the patientQuestionnaire has already an
     *         ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
  
    @PostMapping("/patient-questionnaires")
    @Transactional
    public ResponseEntity<PatientQuestionnaire> createPatientQuestionnaire(
        @Valid @RequestBody QuestionnaireGroupeDepatientRequest groupeDePatientQuestionnaire) throws URISyntaxException {
        log.debug("REST request to save PatientQuestionnaire : {}", groupeDePatientQuestionnaire);
        PatientQuestionnaire result = null;
    
        Questionnaire questionnaire = questionnaireRepository.findOneById(Long.parseLong(groupeDePatientQuestionnaire.getQuestionnaire()));
        GroupeDePatient groupeDePatient = groupeDePatientRepository.findOneById(Long. parseLong(groupeDePatientQuestionnaire.getGroupeDePatient()));
        Hibernate.initialize(groupeDePatient.getPatients());
        groupeDePatient.getPatients().forEach(patient -> {
            PatientQuestionnaire patientQuestionnaire = new PatientQuestionnaire();
            patientQuestionnaire.setDone(false);
            patientQuestionnaire.setDoneTimeDate(null);
            patientQuestionnaire.setPatient(patient);
            patientQuestionnaire.setQuestionnaire(questionnaire);
            patientQuestionnaireService.save(patientQuestionnaire);
            Notification notification = new Notification();
            notification.setDateTime(Instant.now());
            notification.setUser(patient.getUser());
            notification.setLink("patient-questionnaire/"+patientQuestionnaire.getId()+"reponse");
            notification.setDescription("Vous Avez un Questinnaire a faire");
            notificationService.save(notification); 
        });
        
    
		return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,"")).body(result);
            
    }

    /**
     * {@code PUT  /patient-questionnaires} : Updates an existing patientQuestionnaire.
     *
     * @param patientQuestionnaire the patientQuestionnaire to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patientQuestionnaire,
     * or with status {@code 400 (Bad Request)} if the patientQuestionnaire is not valid,
     * or with status {@code 500 (Internal Server Error)} if the patientQuestionnaire couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/patient-questionnaires")
    @Transactional
    public ResponseEntity<String> ReponsePatientQuestionnaire(
            @RequestBody PatientQuestionnaireRequest patientQuestionnaireRequest) throws URISyntaxException {
        log.debug("REST request to update PatientQuestionnaire : {}", patientQuestionnaireRequest.getId());
        log.debug("REST request to update PatientQuestionnaire : {}", patientQuestionnaireRequest.getListReponse());
        PatientQuestionnaire patientQuestionnaire = patientQuestionnaireService.findOneById(patientQuestionnaireRequest.getId());
            patientQuestionnaire.setDone(true);
            patientQuestionnaire.setDoneTimeDate(Instant.now());
            patientQuestionnaireService.save(patientQuestionnaire);
            Notification notification = new Notification();
            notification.setDateTime(Instant.now());
            notification.setUser(patientQuestionnaire.getQuestionnaire().getMedecin().getUser());
            notification.setLink("patient-reponse/"+patientQuestionnaire.getId());
            notification.setDescription("Le patient "+patientQuestionnaire.getPatient().getFullName()+" a repondu a votre questionnaire ");
            notificationService.save(notification);
            patientQuestionnaireRequest.getListReponse().forEach(reponse -> {
            Question question = questionRepository.getOne(Long.parseLong(reponse.getQuestionId()));
            
            if (question.getTypeQuestion().toString().equals("Text")){
                PatientReponse patientReponse = new PatientReponse();
             patientReponse.setContent(reponse.getContenu());
             patientReponse.setPatientQuestionnaire(patientQuestionnaire);
             patientReponse.setQuestion(question);
             patientReponse.setQuestionAnswer(null);
             patientReponseRepository.save(patientReponse);
            }
            else {
               
                reponse.getChoixId().forEach(choix ->{
                    PatientReponse patientReponse = new PatientReponse();
                    QuestionAnswer questionAnswer = questionAnswerRepository.findOneById(Long.parseLong(choix));
                    patientReponse.setPatientQuestionnaire(patientQuestionnaire);
                    patientReponse.setQuestion(question);
                    patientReponse.setQuestionAnswer(questionAnswer);
                    patientReponseRepository.save(patientReponse);
                   });
                   

            }
         
            
         });
        
    
		return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME,"")).body("ok");
            
    }

    /**
     * {@code GET  /patient-questionnaires} : get all the patientQuestionnaires.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of patientQuestionnaires in body.
     */
    @GetMapping("/patient-questionnaires")
    public ResponseEntity<List<PatientQuestionnaire>> getAllPatientQuestionnaires(PatientQuestionnaireCriteria criteria) {
        log.debug("REST request to get PatientQuestionnaires by criteria: {}", criteria);
        boolean isPatient =  SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.PATIENT);
        boolean isMedecin = SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.MEDECIN);
        if (isPatient) {  Patient patient = patientReponseRepository.findOneByUserIsCurrentUser();
            LongFilter longFilter = new LongFilter();
            longFilter.setEquals(patient.getId());
            criteria.setPatientId(longFilter);
            List<PatientQuestionnaire> entityList = patientQuestionnaireQueryService.findByCriteria(criteria);
            return ResponseEntity.ok().body(entityList);
        
        }

  else {
        List<PatientQuestionnaire> entityList = patientQuestionnaireQueryService.findByCriteria(criteria);
        return ResponseEntity.ok().body(entityList);}
    }

    /**
     * {@code GET  /patient-questionnaires/count} : count all the patientQuestionnaires.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/patient-questionnaires/count")
    public ResponseEntity<Long> countPatientQuestionnaires(PatientQuestionnaireCriteria criteria) {
        log.debug("REST request to count PatientQuestionnaires by criteria: {}", criteria);
        return ResponseEntity.ok().body(patientQuestionnaireQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /patient-questionnaires/:id} : get the "id" patientQuestionnaire.
     *
     * @param id the id of the patientQuestionnaire to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the patientQuestionnaire, or with status {@code 404 (Not Found)}.
     */
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.PATIENT + "\")")
    @GetMapping("/patient-questionnaires/{id}")
  
    public ResponseEntity<PatientQuestionnaire> getPatientQuestionnaire(@PathVariable Long id) {
        log.debug("REST request to get PatientQuestionnaire : {}", id);

        PatientQuestionnaire patientQuestionnaire = patientQuestionnaireService.findOneByIdAndIdPatient(id);
        Long ispatientq = patientQuestionnaireService.countPatient(id); // Si le patient participe a cet questionnaire return > 0
        if  (ispatientq <1) 
        throw new BadRequestAlertException("Bad request ", "Bad request", "Bad request");
         
         else {
       
        if ( patientQuestionnaire.getQuestionnaire().getEndDate().compareTo(Instant.now()) <0 ) 
        throw new BadRequestAlertException("Questionnaire termineé", "Questionnaire termineé", "Questionnaire termineé : "+patientQuestionnaire.getQuestionnaire().getEndDate());
        else {

            if ( ( patientQuestionnaire.getQuestionnaire().getStartDate().compareTo(Instant.now()) >0 ) )
            throw new BadRequestAlertException("not yet", "not yet", "not yet");
            else {
                if (patientQuestionnaire.isDone()) {
                    throw new BadRequestAlertException("c bon", "c bon", "c bon");   
                }
                else  return ResponseEntity.ok(patientQuestionnaire);
            }

        }
    }}
    

    /**
     * {@code DELETE  /patient-questionnaires/:id} : delete the "id" patientQuestionnaire.
     *
     * @param id the id of the patientQuestionnaire to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/patient-questionnaires/{id}")
    public ResponseEntity<Void> deletePatientQuestionnaire(@PathVariable Long id) {
        log.debug("REST request to delete PatientQuestionnaire : {}", id);
        patientQuestionnaireService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
