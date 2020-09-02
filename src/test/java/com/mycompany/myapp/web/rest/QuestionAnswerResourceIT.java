package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.TnTsuiviApp;
import com.mycompany.myapp.domain.QuestionAnswer;
import com.mycompany.myapp.domain.PatientReponse;
import com.mycompany.myapp.domain.Question;
import com.mycompany.myapp.repository.QuestionAnswerRepository;
import com.mycompany.myapp.service.QuestionAnswerService;
import com.mycompany.myapp.service.dto.QuestionAnswerCriteria;
import com.mycompany.myapp.service.QuestionAnswerQueryService;

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
 * Integration tests for the {@link QuestionAnswerResource} REST controller.
 */
@SpringBootTest(classes = TnTsuiviApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class QuestionAnswerResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    @Autowired
    private QuestionAnswerRepository questionAnswerRepository;

    @Autowired
    private QuestionAnswerService questionAnswerService;

    @Autowired
    private QuestionAnswerQueryService questionAnswerQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionAnswerMockMvc;

    private QuestionAnswer questionAnswer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionAnswer createEntity(EntityManager em) {
        QuestionAnswer questionAnswer = new QuestionAnswer()
            .label(DEFAULT_LABEL);
        return questionAnswer;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static QuestionAnswer createUpdatedEntity(EntityManager em) {
        QuestionAnswer questionAnswer = new QuestionAnswer()
            .label(UPDATED_LABEL);
        return questionAnswer;
    }

    @BeforeEach
    public void initTest() {
        questionAnswer = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuestionAnswer() throws Exception {
        int databaseSizeBeforeCreate = questionAnswerRepository.findAll().size();

        // Create the QuestionAnswer
        restQuestionAnswerMockMvc.perform(post("/api/question-answers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(questionAnswer)))
            .andExpect(status().isCreated());

        // Validate the QuestionAnswer in the database
        List<QuestionAnswer> questionAnswerList = questionAnswerRepository.findAll();
        assertThat(questionAnswerList).hasSize(databaseSizeBeforeCreate + 1);
        QuestionAnswer testQuestionAnswer = questionAnswerList.get(questionAnswerList.size() - 1);
        assertThat(testQuestionAnswer.getLabel()).isEqualTo(DEFAULT_LABEL);
    }

    @Test
    @Transactional
    public void createQuestionAnswerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = questionAnswerRepository.findAll().size();

        // Create the QuestionAnswer with an existing ID
        questionAnswer.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionAnswerMockMvc.perform(post("/api/question-answers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(questionAnswer)))
            .andExpect(status().isBadRequest());

        // Validate the QuestionAnswer in the database
        List<QuestionAnswer> questionAnswerList = questionAnswerRepository.findAll();
        assertThat(questionAnswerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllQuestionAnswers() throws Exception {
        // Initialize the database
        questionAnswerRepository.saveAndFlush(questionAnswer);

        // Get all the questionAnswerList
        restQuestionAnswerMockMvc.perform(get("/api/question-answers?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionAnswer.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)));
    }
    
    @Test
    @Transactional
    public void getQuestionAnswer() throws Exception {
        // Initialize the database
        questionAnswerRepository.saveAndFlush(questionAnswer);

        // Get the questionAnswer
        restQuestionAnswerMockMvc.perform(get("/api/question-answers/{id}", questionAnswer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(questionAnswer.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL));
    }


    @Test
    @Transactional
    public void getQuestionAnswersByIdFiltering() throws Exception {
        // Initialize the database
        questionAnswerRepository.saveAndFlush(questionAnswer);

        Long id = questionAnswer.getId();

        defaultQuestionAnswerShouldBeFound("id.equals=" + id);
        defaultQuestionAnswerShouldNotBeFound("id.notEquals=" + id);

        defaultQuestionAnswerShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultQuestionAnswerShouldNotBeFound("id.greaterThan=" + id);

        defaultQuestionAnswerShouldBeFound("id.lessThanOrEqual=" + id);
        defaultQuestionAnswerShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllQuestionAnswersByLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        questionAnswerRepository.saveAndFlush(questionAnswer);

        // Get all the questionAnswerList where label equals to DEFAULT_LABEL
        defaultQuestionAnswerShouldBeFound("label.equals=" + DEFAULT_LABEL);

        // Get all the questionAnswerList where label equals to UPDATED_LABEL
        defaultQuestionAnswerShouldNotBeFound("label.equals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllQuestionAnswersByLabelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionAnswerRepository.saveAndFlush(questionAnswer);

        // Get all the questionAnswerList where label not equals to DEFAULT_LABEL
        defaultQuestionAnswerShouldNotBeFound("label.notEquals=" + DEFAULT_LABEL);

        // Get all the questionAnswerList where label not equals to UPDATED_LABEL
        defaultQuestionAnswerShouldBeFound("label.notEquals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllQuestionAnswersByLabelIsInShouldWork() throws Exception {
        // Initialize the database
        questionAnswerRepository.saveAndFlush(questionAnswer);

        // Get all the questionAnswerList where label in DEFAULT_LABEL or UPDATED_LABEL
        defaultQuestionAnswerShouldBeFound("label.in=" + DEFAULT_LABEL + "," + UPDATED_LABEL);

        // Get all the questionAnswerList where label equals to UPDATED_LABEL
        defaultQuestionAnswerShouldNotBeFound("label.in=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllQuestionAnswersByLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionAnswerRepository.saveAndFlush(questionAnswer);

        // Get all the questionAnswerList where label is not null
        defaultQuestionAnswerShouldBeFound("label.specified=true");

        // Get all the questionAnswerList where label is null
        defaultQuestionAnswerShouldNotBeFound("label.specified=false");
    }
                @Test
    @Transactional
    public void getAllQuestionAnswersByLabelContainsSomething() throws Exception {
        // Initialize the database
        questionAnswerRepository.saveAndFlush(questionAnswer);

        // Get all the questionAnswerList where label contains DEFAULT_LABEL
        defaultQuestionAnswerShouldBeFound("label.contains=" + DEFAULT_LABEL);

        // Get all the questionAnswerList where label contains UPDATED_LABEL
        defaultQuestionAnswerShouldNotBeFound("label.contains=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllQuestionAnswersByLabelNotContainsSomething() throws Exception {
        // Initialize the database
        questionAnswerRepository.saveAndFlush(questionAnswer);

        // Get all the questionAnswerList where label does not contain DEFAULT_LABEL
        defaultQuestionAnswerShouldNotBeFound("label.doesNotContain=" + DEFAULT_LABEL);

        // Get all the questionAnswerList where label does not contain UPDATED_LABEL
        defaultQuestionAnswerShouldBeFound("label.doesNotContain=" + UPDATED_LABEL);
    }


    @Test
    @Transactional
    public void getAllQuestionAnswersByPatientReponseIsEqualToSomething() throws Exception {
        // Initialize the database
        questionAnswerRepository.saveAndFlush(questionAnswer);
        PatientReponse patientReponse = PatientReponseResourceIT.createEntity(em);
        em.persist(patientReponse);
        em.flush();
        questionAnswer.addPatientReponse(patientReponse);
        questionAnswerRepository.saveAndFlush(questionAnswer);
        Long patientReponseId = patientReponse.getId();

        // Get all the questionAnswerList where patientReponse equals to patientReponseId
        defaultQuestionAnswerShouldBeFound("patientReponseId.equals=" + patientReponseId);

        // Get all the questionAnswerList where patientReponse equals to patientReponseId + 1
        defaultQuestionAnswerShouldNotBeFound("patientReponseId.equals=" + (patientReponseId + 1));
    }


    @Test
    @Transactional
    public void getAllQuestionAnswersByQuestionIsEqualToSomething() throws Exception {
        // Initialize the database
        questionAnswerRepository.saveAndFlush(questionAnswer);
        Question question = QuestionResourceIT.createEntity(em);
        em.persist(question);
        em.flush();
        questionAnswer.setQuestion(question);
        questionAnswerRepository.saveAndFlush(questionAnswer);
        Long questionId = question.getId();

        // Get all the questionAnswerList where question equals to questionId
        defaultQuestionAnswerShouldBeFound("questionId.equals=" + questionId);

        // Get all the questionAnswerList where question equals to questionId + 1
        defaultQuestionAnswerShouldNotBeFound("questionId.equals=" + (questionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuestionAnswerShouldBeFound(String filter) throws Exception {
        restQuestionAnswerMockMvc.perform(get("/api/question-answers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionAnswer.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)));

        // Check, that the count call also returns 1
        restQuestionAnswerMockMvc.perform(get("/api/question-answers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuestionAnswerShouldNotBeFound(String filter) throws Exception {
        restQuestionAnswerMockMvc.perform(get("/api/question-answers?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuestionAnswerMockMvc.perform(get("/api/question-answers/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingQuestionAnswer() throws Exception {
        // Get the questionAnswer
        restQuestionAnswerMockMvc.perform(get("/api/question-answers/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuestionAnswer() throws Exception {
        // Initialize the database
        questionAnswerService.save(questionAnswer);

        int databaseSizeBeforeUpdate = questionAnswerRepository.findAll().size();

        // Update the questionAnswer
        QuestionAnswer updatedQuestionAnswer = questionAnswerRepository.findById(questionAnswer.getId()).get();
        // Disconnect from session so that the updates on updatedQuestionAnswer are not directly saved in db
        em.detach(updatedQuestionAnswer);
        updatedQuestionAnswer
            .label(UPDATED_LABEL);

        restQuestionAnswerMockMvc.perform(put("/api/question-answers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedQuestionAnswer)))
            .andExpect(status().isOk());

        // Validate the QuestionAnswer in the database
        List<QuestionAnswer> questionAnswerList = questionAnswerRepository.findAll();
        assertThat(questionAnswerList).hasSize(databaseSizeBeforeUpdate);
        QuestionAnswer testQuestionAnswer = questionAnswerList.get(questionAnswerList.size() - 1);
        assertThat(testQuestionAnswer.getLabel()).isEqualTo(UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void updateNonExistingQuestionAnswer() throws Exception {
        int databaseSizeBeforeUpdate = questionAnswerRepository.findAll().size();

        // Create the QuestionAnswer

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionAnswerMockMvc.perform(put("/api/question-answers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(questionAnswer)))
            .andExpect(status().isBadRequest());

        // Validate the QuestionAnswer in the database
        List<QuestionAnswer> questionAnswerList = questionAnswerRepository.findAll();
        assertThat(questionAnswerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteQuestionAnswer() throws Exception {
        // Initialize the database
        questionAnswerService.save(questionAnswer);

        int databaseSizeBeforeDelete = questionAnswerRepository.findAll().size();

        // Delete the questionAnswer
        restQuestionAnswerMockMvc.perform(delete("/api/question-answers/{id}", questionAnswer.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<QuestionAnswer> questionAnswerList = questionAnswerRepository.findAll();
        assertThat(questionAnswerList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
