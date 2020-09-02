package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.TnTsuiviApp;
import com.mycompany.myapp.domain.PatientReponse;
import com.mycompany.myapp.domain.PatientQuestionnaire;
import com.mycompany.myapp.domain.Question;
import com.mycompany.myapp.domain.QuestionAnswer;
import com.mycompany.myapp.repository.PatientReponseRepository;
import com.mycompany.myapp.service.PatientReponseService;
import com.mycompany.myapp.service.dto.PatientReponseCriteria;
import com.mycompany.myapp.service.PatientReponseQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PatientReponseResource} REST controller.
 */
@SpringBootTest(classes = TnTsuiviApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class PatientReponseResourceIT {

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    @Autowired
    private PatientReponseRepository patientReponseRepository;

    @Autowired
    private PatientReponseService patientReponseService;

    @Autowired
    private PatientReponseQueryService patientReponseQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPatientReponseMockMvc;

    private PatientReponse patientReponse;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PatientReponse createEntity(EntityManager em) {
        PatientReponse patientReponse = new PatientReponse()
            .content(DEFAULT_CONTENT);
        // Add required entity
        Question question;
        if (TestUtil.findAll(em, Question.class).isEmpty()) {
            question = QuestionResourceIT.createEntity(em);
            em.persist(question);
            em.flush();
        } else {
            question = TestUtil.findAll(em, Question.class).get(0);
        }
        patientReponse.setQuestion(question);
        return patientReponse;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PatientReponse createUpdatedEntity(EntityManager em) {
        PatientReponse patientReponse = new PatientReponse()
            .content(UPDATED_CONTENT);
        // Add required entity
        Question question;
        if (TestUtil.findAll(em, Question.class).isEmpty()) {
            question = QuestionResourceIT.createUpdatedEntity(em);
            em.persist(question);
            em.flush();
        } else {
            question = TestUtil.findAll(em, Question.class).get(0);
        }
        patientReponse.setQuestion(question);
        return patientReponse;
    }

    @BeforeEach
    public void initTest() {
        patientReponse = createEntity(em);
    }

    @Test
    @Transactional
    public void createPatientReponse() throws Exception {
        int databaseSizeBeforeCreate = patientReponseRepository.findAll().size();

        // Create the PatientReponse
        restPatientReponseMockMvc.perform(post("/api/patient-reponses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patientReponse)))
            .andExpect(status().isCreated());

        // Validate the PatientReponse in the database
        List<PatientReponse> patientReponseList = patientReponseRepository.findAll();
        assertThat(patientReponseList).hasSize(databaseSizeBeforeCreate + 1);
        PatientReponse testPatientReponse = patientReponseList.get(patientReponseList.size() - 1);
        assertThat(testPatientReponse.getContent()).isEqualTo(DEFAULT_CONTENT);
    }

    @Test
    @Transactional
    public void createPatientReponseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = patientReponseRepository.findAll().size();

        // Create the PatientReponse with an existing ID
        patientReponse.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPatientReponseMockMvc.perform(post("/api/patient-reponses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patientReponse)))
            .andExpect(status().isBadRequest());

        // Validate the PatientReponse in the database
        List<PatientReponse> patientReponseList = patientReponseRepository.findAll();
        assertThat(patientReponseList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPatientReponses() throws Exception {
        // Initialize the database
        patientReponseRepository.saveAndFlush(patientReponse);

        // Get all the patientReponseList
        restPatientReponseMockMvc.perform(get("/api/patient-reponses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patientReponse.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)));
    }
    
    @Test
    @Transactional
    public void getPatientReponse() throws Exception {
        // Initialize the database
        patientReponseRepository.saveAndFlush(patientReponse);

        // Get the patientReponse
        restPatientReponseMockMvc.perform(get("/api/patient-reponses/{id}", patientReponse.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(patientReponse.getId().intValue()))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT));
    }


    @Test
    @Transactional
    public void getPatientReponsesByIdFiltering() throws Exception {
        // Initialize the database
        patientReponseRepository.saveAndFlush(patientReponse);

        Long id = patientReponse.getId();

        defaultPatientReponseShouldBeFound("id.equals=" + id);
        defaultPatientReponseShouldNotBeFound("id.notEquals=" + id);

        defaultPatientReponseShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPatientReponseShouldNotBeFound("id.greaterThan=" + id);

        defaultPatientReponseShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPatientReponseShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPatientReponsesByContentIsEqualToSomething() throws Exception {
        // Initialize the database
        patientReponseRepository.saveAndFlush(patientReponse);

        // Get all the patientReponseList where content equals to DEFAULT_CONTENT
        defaultPatientReponseShouldBeFound("content.equals=" + DEFAULT_CONTENT);

        // Get all the patientReponseList where content equals to UPDATED_CONTENT
        defaultPatientReponseShouldNotBeFound("content.equals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void getAllPatientReponsesByContentIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientReponseRepository.saveAndFlush(patientReponse);

        // Get all the patientReponseList where content not equals to DEFAULT_CONTENT
        defaultPatientReponseShouldNotBeFound("content.notEquals=" + DEFAULT_CONTENT);

        // Get all the patientReponseList where content not equals to UPDATED_CONTENT
        defaultPatientReponseShouldBeFound("content.notEquals=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void getAllPatientReponsesByContentIsInShouldWork() throws Exception {
        // Initialize the database
        patientReponseRepository.saveAndFlush(patientReponse);

        // Get all the patientReponseList where content in DEFAULT_CONTENT or UPDATED_CONTENT
        defaultPatientReponseShouldBeFound("content.in=" + DEFAULT_CONTENT + "," + UPDATED_CONTENT);

        // Get all the patientReponseList where content equals to UPDATED_CONTENT
        defaultPatientReponseShouldNotBeFound("content.in=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void getAllPatientReponsesByContentIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientReponseRepository.saveAndFlush(patientReponse);

        // Get all the patientReponseList where content is not null
        defaultPatientReponseShouldBeFound("content.specified=true");

        // Get all the patientReponseList where content is null
        defaultPatientReponseShouldNotBeFound("content.specified=false");
    }
                @Test
    @Transactional
    public void getAllPatientReponsesByContentContainsSomething() throws Exception {
        // Initialize the database
        patientReponseRepository.saveAndFlush(patientReponse);

        // Get all the patientReponseList where content contains DEFAULT_CONTENT
        defaultPatientReponseShouldBeFound("content.contains=" + DEFAULT_CONTENT);

        // Get all the patientReponseList where content contains UPDATED_CONTENT
        defaultPatientReponseShouldNotBeFound("content.contains=" + UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void getAllPatientReponsesByContentNotContainsSomething() throws Exception {
        // Initialize the database
        patientReponseRepository.saveAndFlush(patientReponse);

        // Get all the patientReponseList where content does not contain DEFAULT_CONTENT
        defaultPatientReponseShouldNotBeFound("content.doesNotContain=" + DEFAULT_CONTENT);

        // Get all the patientReponseList where content does not contain UPDATED_CONTENT
        defaultPatientReponseShouldBeFound("content.doesNotContain=" + UPDATED_CONTENT);
    }


    @Test
    @Transactional
    public void getAllPatientReponsesByPatientQuestionnaireIsEqualToSomething() throws Exception {
        // Initialize the database
        patientReponseRepository.saveAndFlush(patientReponse);
        PatientQuestionnaire patientQuestionnaire = PatientQuestionnaireResourceIT.createEntity(em);
        em.persist(patientQuestionnaire);
        em.flush();
        patientReponse.setPatientQuestionnaire(patientQuestionnaire);
        patientReponseRepository.saveAndFlush(patientReponse);
        Long patientQuestionnaireId = patientQuestionnaire.getId();

        // Get all the patientReponseList where patientQuestionnaire equals to patientQuestionnaireId
        defaultPatientReponseShouldBeFound("patientQuestionnaireId.equals=" + patientQuestionnaireId);

        // Get all the patientReponseList where patientQuestionnaire equals to patientQuestionnaireId + 1
        defaultPatientReponseShouldNotBeFound("patientQuestionnaireId.equals=" + (patientQuestionnaireId + 1));
    }


    @Test
    @Transactional
    public void getAllPatientReponsesByQuestionIsEqualToSomething() throws Exception {
        // Get already existing entity
        Question question = patientReponse.getQuestion();
        patientReponseRepository.saveAndFlush(patientReponse);
        Long questionId = question.getId();

        // Get all the patientReponseList where question equals to questionId
        defaultPatientReponseShouldBeFound("questionId.equals=" + questionId);

        // Get all the patientReponseList where question equals to questionId + 1
        defaultPatientReponseShouldNotBeFound("questionId.equals=" + (questionId + 1));
    }


    @Test
    @Transactional
    public void getAllPatientReponsesByQuestionAnswerIsEqualToSomething() throws Exception {
        // Initialize the database
        patientReponseRepository.saveAndFlush(patientReponse);
        QuestionAnswer questionAnswer = QuestionAnswerResourceIT.createEntity(em);
        em.persist(questionAnswer);
        em.flush();
        patientReponse.setQuestionAnswer(questionAnswer);
        patientReponseRepository.saveAndFlush(patientReponse);
        Long questionAnswerId = questionAnswer.getId();

        // Get all the patientReponseList where questionAnswer equals to questionAnswerId
        defaultPatientReponseShouldBeFound("questionAnswerId.equals=" + questionAnswerId);

        // Get all the patientReponseList where questionAnswer equals to questionAnswerId + 1
        defaultPatientReponseShouldNotBeFound("questionAnswerId.equals=" + (questionAnswerId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPatientReponseShouldBeFound(String filter) throws Exception {
        restPatientReponseMockMvc.perform(get("/api/patient-reponses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patientReponse.getId().intValue())))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT)));

        // Check, that the count call also returns 1
        restPatientReponseMockMvc.perform(get("/api/patient-reponses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPatientReponseShouldNotBeFound(String filter) throws Exception {
        restPatientReponseMockMvc.perform(get("/api/patient-reponses?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPatientReponseMockMvc.perform(get("/api/patient-reponses/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPatientReponse() throws Exception {
        // Get the patientReponse
        restPatientReponseMockMvc.perform(get("/api/patient-reponses/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePatientReponse() throws Exception {
        // Initialize the database
        patientReponseService.save(patientReponse);

        int databaseSizeBeforeUpdate = patientReponseRepository.findAll().size();

        // Update the patientReponse
        PatientReponse updatedPatientReponse = patientReponseRepository.findById(patientReponse.getId()).get();
        // Disconnect from session so that the updates on updatedPatientReponse are not directly saved in db
        em.detach(updatedPatientReponse);
        updatedPatientReponse
            .content(UPDATED_CONTENT);

        restPatientReponseMockMvc.perform(put("/api/patient-reponses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPatientReponse)))
            .andExpect(status().isOk());

        // Validate the PatientReponse in the database
        List<PatientReponse> patientReponseList = patientReponseRepository.findAll();
        assertThat(patientReponseList).hasSize(databaseSizeBeforeUpdate);
        PatientReponse testPatientReponse = patientReponseList.get(patientReponseList.size() - 1);
        assertThat(testPatientReponse.getContent()).isEqualTo(UPDATED_CONTENT);
    }

    @Test
    @Transactional
    public void updateNonExistingPatientReponse() throws Exception {
        int databaseSizeBeforeUpdate = patientReponseRepository.findAll().size();

        // Create the PatientReponse

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientReponseMockMvc.perform(put("/api/patient-reponses")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patientReponse)))
            .andExpect(status().isBadRequest());

        // Validate the PatientReponse in the database
        List<PatientReponse> patientReponseList = patientReponseRepository.findAll();
        assertThat(patientReponseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePatientReponse() throws Exception {
        // Initialize the database
        patientReponseService.save(patientReponse);

        int databaseSizeBeforeDelete = patientReponseRepository.findAll().size();

        // Delete the patientReponse
        restPatientReponseMockMvc.perform(delete("/api/patient-reponses/{id}", patientReponse.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PatientReponse> patientReponseList = patientReponseRepository.findAll();
        assertThat(patientReponseList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
