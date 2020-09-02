package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Question;
import com.mycompany.myapp.domain.QuestionRequest;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.QuestionAnswer;
import com.mycompany.myapp.repository.MedecinRepository;
import com.mycompany.myapp.repository.QuestionAnswerRepository;
import com.mycompany.myapp.repository.QuestionRepository;
import com.mycompany.myapp.repository.UserRepository;
import com.mycompany.myapp.security.AuthoritiesConstants;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.service.QuestionService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import com.mycompany.myapp.service.dto.QuestionCriteria;
import com.mycompany.myapp.service.QuestionQueryService;
import org.springframework.security.access.prepost.PreAuthorize;

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
 * REST controller for managing {@link com.mycompany.myapp.domain.Question}.
 */
@RestController
@RequestMapping("/api")
public class QuestionResource {

    private final Logger log = LoggerFactory.getLogger(QuestionResource.class);

    private static final String ENTITY_NAME = "question";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final QuestionService questionService;

    private final QuestionQueryService questionQueryService;

    private final QuestionRepository questionRepository;

    private final QuestionAnswerRepository questionAnswerRepository;
    private final  MedecinRepository medecinRepository;

    public QuestionResource(QuestionAnswerRepository questionAnswerRepository, MedecinRepository medecinRepository , QuestionService questionService, QuestionQueryService questionQueryService, QuestionRepository questionRepository) {
        this.questionService = questionService;
        this.questionQueryService = questionQueryService;
        this.questionRepository = questionRepository;
        this.medecinRepository = medecinRepository;
        this.questionAnswerRepository=questionAnswerRepository;
    }

    /**
     * {@code POST  /questions} : Create a new question.
     *
     * @param question the question to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new question, or with status {@code 400 (Bad Request)} if the question has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/questions")
    //@PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MEDECIN + "\")")
    public ResponseEntity<Question> createQuestion(@Valid @RequestBody QuestionRequest question    ) throws URISyntaxException {
        log.debug("REST request to save Question : {}", question);
        
        Question q = new Question ();
        q.setLabel(question.getLabel());
        q.setTypeQuestion(question.getTypeQuestion());
        q.setMedecin(medecinRepository.findOneByUserIsCurrentUser());
        Question result = questionRepository.save(q);
        if (q.getTypeQuestion().toString().equals("Text"))
         question.setQuestionAnswers(null);
        if (question.getQuestionAnswers() !=null) { 
        question.getQuestionAnswers().forEach(questionanswer -> {
            QuestionAnswer qa = new QuestionAnswer();
            qa.setLabel(questionanswer);
            qa.setQuestion(q);
            questionAnswerRepository.save(qa);
        });
    }
      
        return ResponseEntity.created(new URI("/api/questions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }
        

    /**
     * {@code PUT  /questions} : Updates an existing question.
     *
     * @param question the question to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated question,
     * or with status {@code 400 (Bad Request)} if the question is not valid,
     * or with status {@code 500 (Internal Server Error)} if the question couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/questions")
    //@PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MEDECIN + "\")")
    public ResponseEntity<Question> updateQuestion(@Valid @RequestBody Question question) throws URISyntaxException {
        log.debug("REST request to update Question : {}", question);
        if (question.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        question.setMedecin(medecinRepository.findOneByUserIsCurrentUser());
        Question result = questionService.save(question);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, question.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /questions} : get all the questions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of questions in body.
     */
    @GetMapping("/questions")
    //@PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MEDECIN + "\")")
    public ResponseEntity<List<Question>> getAllQuestions() {
        log.debug("REST request to get Questions ");

        boolean isMedecin =  SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.MEDECIN);
        boolean isPatient =  SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.PATIENT);
        if (isMedecin) {
        List<Question> entityList = questionRepository.findByUserIsCurrentUser();
        return ResponseEntity.ok().body(entityList); }
        else if (isPatient) 
        {
            List<Question> entityList = questionRepository.findAll();
            return ResponseEntity.ok().body(entityList);
        } 
        else  throw new BadRequestAlertException("Invalid Request", ENTITY_NAME, "Invalid Request");
    }

    /**
     * {@code GET  /questions/count} : count all the questions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/questions/count")
    //@PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MEDECIN + "\")")
    public ResponseEntity<Long> countQuestions() {
        log.debug("REST request to count Questions ");
        return ResponseEntity.ok().body(questionRepository.CountByUserIsCurrentUser());
    }

    /**
     * {@code GET  /questions/:id} : get the "id" question.
     *
     * @param id the id of the question to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the question, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/questions/{id}")
    //@PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MEDECIN + "\")")
    public ResponseEntity<Question> getQuestion(@PathVariable Long id) {
        log.debug("REST request to get Question : {}", id);
        Optional<Question> question = questionRepository.findByIdAndUserIsCurrentUser(id);
        return ResponseUtil.wrapOrNotFound(question);
    }

    /**
     * {@code DELETE  /questions/:id} : delete the "id" question.
     *
     * @param id the id of the question to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/questions/{id}")
    @PreAuthorize("hasAuthority(\"" + AuthoritiesConstants.MEDECIN + "\")")
    public ResponseEntity<Void> deleteQuestion(@PathVariable Long id) {
        log.debug("REST request to delete Question : {}", id);
        questionService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
