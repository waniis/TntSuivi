package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.TnTsuiviApp;
import com.mycompany.myapp.domain.Question;
import com.mycompany.myapp.domain.QuestionAnswer;
import com.mycompany.myapp.domain.PatientReponse;
import com.mycompany.myapp.domain.Medecin;
import com.mycompany.myapp.domain.Questionnaire;
import com.mycompany.myapp.repository.QuestionRepository;
import com.mycompany.myapp.service.QuestionService;
import com.mycompany.myapp.service.dto.QuestionCriteria;
import com.mycompany.myapp.service.QuestionQueryService;

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

import com.mycompany.myapp.domain.enumeration.TypeQuestion;
/**
 * Integration tests for the {@link QuestionResource} REST controller.
 */
@SpringBootTest(classes = TnTsuiviApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class QuestionResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final TypeQuestion DEFAULT_TYPE_QUESTION = TypeQuestion.Text;
    private static final TypeQuestion UPDATED_TYPE_QUESTION = TypeQuestion.CheckBox;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private QuestionQueryService questionQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionMockMvc;

    private Question question;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Question createEntity(EntityManager em) {
        Question question = new Question()
            .label(DEFAULT_LABEL)
            .typeQuestion(DEFAULT_TYPE_QUESTION);
        return question;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Question createUpdatedEntity(EntityManager em) {
        Question question = new Question()
            .label(UPDATED_LABEL)
            .typeQuestion(UPDATED_TYPE_QUESTION);
        return question;
    }

    @BeforeEach
    public void initTest() {
        question = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuestion() throws Exception {
        int databaseSizeBeforeCreate = questionRepository.findAll().size();

        // Create the Question
        restQuestionMockMvc.perform(post("/api/questions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(question)))
            .andExpect(status().isCreated());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeCreate + 1);
        Question testQuestion = questionList.get(questionList.size() - 1);
        assertThat(testQuestion.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testQuestion.getTypeQuestion()).isEqualTo(DEFAULT_TYPE_QUESTION);
    }

    @Test
    @Transactional
    public void createQuestionWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = questionRepository.findAll().size();

        // Create the Question with an existing ID
        question.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionMockMvc.perform(post("/api/questions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(question)))
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionRepository.findAll().size();
        // set the field null
        question.setLabel(null);

        // Create the Question, which fails.

        restQuestionMockMvc.perform(post("/api/questions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(question)))
            .andExpect(status().isBadRequest());

        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTypeQuestionIsRequired() throws Exception {
        int databaseSizeBeforeTest = questionRepository.findAll().size();
        // set the field null
        question.setTypeQuestion(null);

        // Create the Question, which fails.

        restQuestionMockMvc.perform(post("/api/questions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(question)))
            .andExpect(status().isBadRequest());

        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllQuestions() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList
        restQuestionMockMvc.perform(get("/api/questions?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(question.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].typeQuestion").value(hasItem(DEFAULT_TYPE_QUESTION.toString())));
    }
    
    @Test
    @Transactional
    public void getQuestion() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get the question
        restQuestionMockMvc.perform(get("/api/questions/{id}", question.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(question.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.typeQuestion").value(DEFAULT_TYPE_QUESTION.toString()));
    }


    @Test
    @Transactional
    public void getQuestionsByIdFiltering() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        Long id = question.getId();

        defaultQuestionShouldBeFound("id.equals=" + id);
        defaultQuestionShouldNotBeFound("id.notEquals=" + id);

        defaultQuestionShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultQuestionShouldNotBeFound("id.greaterThan=" + id);

        defaultQuestionShouldBeFound("id.lessThanOrEqual=" + id);
        defaultQuestionShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllQuestionsByLabelIsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where label equals to DEFAULT_LABEL
        defaultQuestionShouldBeFound("label.equals=" + DEFAULT_LABEL);

        // Get all the questionList where label equals to UPDATED_LABEL
        defaultQuestionShouldNotBeFound("label.equals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllQuestionsByLabelIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where label not equals to DEFAULT_LABEL
        defaultQuestionShouldNotBeFound("label.notEquals=" + DEFAULT_LABEL);

        // Get all the questionList where label not equals to UPDATED_LABEL
        defaultQuestionShouldBeFound("label.notEquals=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllQuestionsByLabelIsInShouldWork() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where label in DEFAULT_LABEL or UPDATED_LABEL
        defaultQuestionShouldBeFound("label.in=" + DEFAULT_LABEL + "," + UPDATED_LABEL);

        // Get all the questionList where label equals to UPDATED_LABEL
        defaultQuestionShouldNotBeFound("label.in=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllQuestionsByLabelIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where label is not null
        defaultQuestionShouldBeFound("label.specified=true");

        // Get all the questionList where label is null
        defaultQuestionShouldNotBeFound("label.specified=false");
    }
                @Test
    @Transactional
    public void getAllQuestionsByLabelContainsSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where label contains DEFAULT_LABEL
        defaultQuestionShouldBeFound("label.contains=" + DEFAULT_LABEL);

        // Get all the questionList where label contains UPDATED_LABEL
        defaultQuestionShouldNotBeFound("label.contains=" + UPDATED_LABEL);
    }

    @Test
    @Transactional
    public void getAllQuestionsByLabelNotContainsSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where label does not contain DEFAULT_LABEL
        defaultQuestionShouldNotBeFound("label.doesNotContain=" + DEFAULT_LABEL);

        // Get all the questionList where label does not contain UPDATED_LABEL
        defaultQuestionShouldBeFound("label.doesNotContain=" + UPDATED_LABEL);
    }


    @Test
    @Transactional
    public void getAllQuestionsByTypeQuestionIsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where typeQuestion equals to DEFAULT_TYPE_QUESTION
        defaultQuestionShouldBeFound("typeQuestion.equals=" + DEFAULT_TYPE_QUESTION);

        // Get all the questionList where typeQuestion equals to UPDATED_TYPE_QUESTION
        defaultQuestionShouldNotBeFound("typeQuestion.equals=" + UPDATED_TYPE_QUESTION);
    }

    @Test
    @Transactional
    public void getAllQuestionsByTypeQuestionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where typeQuestion not equals to DEFAULT_TYPE_QUESTION
        defaultQuestionShouldNotBeFound("typeQuestion.notEquals=" + DEFAULT_TYPE_QUESTION);

        // Get all the questionList where typeQuestion not equals to UPDATED_TYPE_QUESTION
        defaultQuestionShouldBeFound("typeQuestion.notEquals=" + UPDATED_TYPE_QUESTION);
    }

    @Test
    @Transactional
    public void getAllQuestionsByTypeQuestionIsInShouldWork() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where typeQuestion in DEFAULT_TYPE_QUESTION or UPDATED_TYPE_QUESTION
        defaultQuestionShouldBeFound("typeQuestion.in=" + DEFAULT_TYPE_QUESTION + "," + UPDATED_TYPE_QUESTION);

        // Get all the questionList where typeQuestion equals to UPDATED_TYPE_QUESTION
        defaultQuestionShouldNotBeFound("typeQuestion.in=" + UPDATED_TYPE_QUESTION);
    }

    @Test
    @Transactional
    public void getAllQuestionsByTypeQuestionIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);

        // Get all the questionList where typeQuestion is not null
        defaultQuestionShouldBeFound("typeQuestion.specified=true");

        // Get all the questionList where typeQuestion is null
        defaultQuestionShouldNotBeFound("typeQuestion.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuestionsByQuestionAnswerIsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);
        QuestionAnswer questionAnswer = QuestionAnswerResourceIT.createEntity(em);
        em.persist(questionAnswer);
        em.flush();
        question.addQuestionAnswer(questionAnswer);
        questionRepository.saveAndFlush(question);
        Long questionAnswerId = questionAnswer.getId();

        // Get all the questionList where questionAnswer equals to questionAnswerId
        defaultQuestionShouldBeFound("questionAnswerId.equals=" + questionAnswerId);

        // Get all the questionList where questionAnswer equals to questionAnswerId + 1
        defaultQuestionShouldNotBeFound("questionAnswerId.equals=" + (questionAnswerId + 1));
    }


    @Test
    @Transactional
    public void getAllQuestionsByPatientReponseIsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);
        PatientReponse patientReponse = PatientReponseResourceIT.createEntity(em);
        em.persist(patientReponse);
        em.flush();
        question.addPatientReponse(patientReponse);
        questionRepository.saveAndFlush(question);
        Long patientReponseId = patientReponse.getId();

        // Get all the questionList where patientReponse equals to patientReponseId
        defaultQuestionShouldBeFound("patientReponseId.equals=" + patientReponseId);

        // Get all the questionList where patientReponse equals to patientReponseId + 1
        defaultQuestionShouldNotBeFound("patientReponseId.equals=" + (patientReponseId + 1));
    }


    @Test
    @Transactional
    public void getAllQuestionsByMedecinIsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);
        Medecin medecin = MedecinResourceIT.createEntity(em);
        em.persist(medecin);
        em.flush();
        question.setMedecin(medecin);
        questionRepository.saveAndFlush(question);
        Long medecinId = medecin.getId();

        // Get all the questionList where medecin equals to medecinId
        defaultQuestionShouldBeFound("medecinId.equals=" + medecinId);

        // Get all the questionList where medecin equals to medecinId + 1
        defaultQuestionShouldNotBeFound("medecinId.equals=" + (medecinId + 1));
    }


    @Test
    @Transactional
    public void getAllQuestionsByQuestionnaireIsEqualToSomething() throws Exception {
        // Initialize the database
        questionRepository.saveAndFlush(question);
        Questionnaire questionnaire = QuestionnaireResourceIT.createEntity(em);
        em.persist(questionnaire);
        em.flush();
        question.addQuestionnaire(questionnaire);
        questionRepository.saveAndFlush(question);
        Long questionnaireId = questionnaire.getId();

        // Get all the questionList where questionnaire equals to questionnaireId
        defaultQuestionShouldBeFound("questionnaireId.equals=" + questionnaireId);

        // Get all the questionList where questionnaire equals to questionnaireId + 1
        defaultQuestionShouldNotBeFound("questionnaireId.equals=" + (questionnaireId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuestionShouldBeFound(String filter) throws Exception {
        restQuestionMockMvc.perform(get("/api/questions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(question.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].typeQuestion").value(hasItem(DEFAULT_TYPE_QUESTION.toString())));

        // Check, that the count call also returns 1
        restQuestionMockMvc.perform(get("/api/questions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuestionShouldNotBeFound(String filter) throws Exception {
        restQuestionMockMvc.perform(get("/api/questions?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuestionMockMvc.perform(get("/api/questions/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingQuestion() throws Exception {
        // Get the question
        restQuestionMockMvc.perform(get("/api/questions/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuestion() throws Exception {
        // Initialize the database
        questionService.save(question);

        int databaseSizeBeforeUpdate = questionRepository.findAll().size();

        // Update the question
        Question updatedQuestion = questionRepository.findById(question.getId()).get();
        // Disconnect from session so that the updates on updatedQuestion are not directly saved in db
        em.detach(updatedQuestion);
        updatedQuestion
            .label(UPDATED_LABEL)
            .typeQuestion(UPDATED_TYPE_QUESTION);

        restQuestionMockMvc.perform(put("/api/questions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedQuestion)))
            .andExpect(status().isOk());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
        Question testQuestion = questionList.get(questionList.size() - 1);
        assertThat(testQuestion.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testQuestion.getTypeQuestion()).isEqualTo(UPDATED_TYPE_QUESTION);
    }

    @Test
    @Transactional
    public void updateNonExistingQuestion() throws Exception {
        int databaseSizeBeforeUpdate = questionRepository.findAll().size();

        // Create the Question

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionMockMvc.perform(put("/api/questions")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(question)))
            .andExpect(status().isBadRequest());

        // Validate the Question in the database
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteQuestion() throws Exception {
        // Initialize the database
        questionService.save(question);

        int databaseSizeBeforeDelete = questionRepository.findAll().size();

        // Delete the question
        restQuestionMockMvc.perform(delete("/api/questions/{id}", question.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Question> questionList = questionRepository.findAll();
        assertThat(questionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
