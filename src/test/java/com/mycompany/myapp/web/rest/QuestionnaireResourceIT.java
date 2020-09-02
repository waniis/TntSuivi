package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.TnTsuiviApp;
import com.mycompany.myapp.domain.Questionnaire;
import com.mycompany.myapp.domain.PatientQuestionnaire;
import com.mycompany.myapp.domain.Question;
import com.mycompany.myapp.domain.Medecin;
import com.mycompany.myapp.repository.QuestionnaireRepository;
import com.mycompany.myapp.service.QuestionnaireService;
import com.mycompany.myapp.service.dto.QuestionnaireCriteria;
import com.mycompany.myapp.service.QuestionnaireQueryService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link QuestionnaireResource} REST controller.
 */
@SpringBootTest(classes = TnTsuiviApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class QuestionnaireResourceIT {

    private static final String DEFAULT_SUBJECT = "AAAAAAAAAA";
    private static final String UPDATED_SUBJECT = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    @Autowired
    private QuestionnaireRepository questionnaireRepository;

    @Mock
    private QuestionnaireRepository questionnaireRepositoryMock;

    @Mock
    private QuestionnaireService questionnaireServiceMock;

    @Autowired
    private QuestionnaireService questionnaireService;

    @Autowired
    private QuestionnaireQueryService questionnaireQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restQuestionnaireMockMvc;

    private Questionnaire questionnaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Questionnaire createEntity(EntityManager em) {
        Questionnaire questionnaire = new Questionnaire()
            .subject(DEFAULT_SUBJECT)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE);
        return questionnaire;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Questionnaire createUpdatedEntity(EntityManager em) {
        Questionnaire questionnaire = new Questionnaire()
            .subject(UPDATED_SUBJECT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);
        return questionnaire;
    }

    @BeforeEach
    public void initTest() {
        questionnaire = createEntity(em);
    }

    @Test
    @Transactional
    public void createQuestionnaire() throws Exception {
        int databaseSizeBeforeCreate = questionnaireRepository.findAll().size();

        // Create the Questionnaire
        restQuestionnaireMockMvc.perform(post("/api/questionnaires")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(questionnaire)))
            .andExpect(status().isCreated());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeCreate + 1);
        Questionnaire testQuestionnaire = questionnaireList.get(questionnaireList.size() - 1);
        assertThat(testQuestionnaire.getSubject()).isEqualTo(DEFAULT_SUBJECT);
        assertThat(testQuestionnaire.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testQuestionnaire.getEndDate()).isEqualTo(DEFAULT_END_DATE);
    }

    @Test
    @Transactional
    public void createQuestionnaireWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = questionnaireRepository.findAll().size();

        // Create the Questionnaire with an existing ID
        questionnaire.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restQuestionnaireMockMvc.perform(post("/api/questionnaires")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(questionnaire)))
            .andExpect(status().isBadRequest());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllQuestionnaires() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList
        restQuestionnaireMockMvc.perform(get("/api/questionnaires?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionnaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllQuestionnairesWithEagerRelationshipsIsEnabled() throws Exception {
        when(questionnaireServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuestionnaireMockMvc.perform(get("/api/questionnaires?eagerload=true"))
            .andExpect(status().isOk());

        verify(questionnaireServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllQuestionnairesWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(questionnaireServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restQuestionnaireMockMvc.perform(get("/api/questionnaires?eagerload=true"))
            .andExpect(status().isOk());

        verify(questionnaireServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getQuestionnaire() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get the questionnaire
        restQuestionnaireMockMvc.perform(get("/api/questionnaires/{id}", questionnaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(questionnaire.getId().intValue()))
            .andExpect(jsonPath("$.subject").value(DEFAULT_SUBJECT))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()));
    }


    @Test
    @Transactional
    public void getQuestionnairesByIdFiltering() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        Long id = questionnaire.getId();

        defaultQuestionnaireShouldBeFound("id.equals=" + id);
        defaultQuestionnaireShouldNotBeFound("id.notEquals=" + id);

        defaultQuestionnaireShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultQuestionnaireShouldNotBeFound("id.greaterThan=" + id);

        defaultQuestionnaireShouldBeFound("id.lessThanOrEqual=" + id);
        defaultQuestionnaireShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllQuestionnairesBySubjectIsEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where subject equals to DEFAULT_SUBJECT
        defaultQuestionnaireShouldBeFound("subject.equals=" + DEFAULT_SUBJECT);

        // Get all the questionnaireList where subject equals to UPDATED_SUBJECT
        defaultQuestionnaireShouldNotBeFound("subject.equals=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    public void getAllQuestionnairesBySubjectIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where subject not equals to DEFAULT_SUBJECT
        defaultQuestionnaireShouldNotBeFound("subject.notEquals=" + DEFAULT_SUBJECT);

        // Get all the questionnaireList where subject not equals to UPDATED_SUBJECT
        defaultQuestionnaireShouldBeFound("subject.notEquals=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    public void getAllQuestionnairesBySubjectIsInShouldWork() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where subject in DEFAULT_SUBJECT or UPDATED_SUBJECT
        defaultQuestionnaireShouldBeFound("subject.in=" + DEFAULT_SUBJECT + "," + UPDATED_SUBJECT);

        // Get all the questionnaireList where subject equals to UPDATED_SUBJECT
        defaultQuestionnaireShouldNotBeFound("subject.in=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    public void getAllQuestionnairesBySubjectIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where subject is not null
        defaultQuestionnaireShouldBeFound("subject.specified=true");

        // Get all the questionnaireList where subject is null
        defaultQuestionnaireShouldNotBeFound("subject.specified=false");
    }
                @Test
    @Transactional
    public void getAllQuestionnairesBySubjectContainsSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where subject contains DEFAULT_SUBJECT
        defaultQuestionnaireShouldBeFound("subject.contains=" + DEFAULT_SUBJECT);

        // Get all the questionnaireList where subject contains UPDATED_SUBJECT
        defaultQuestionnaireShouldNotBeFound("subject.contains=" + UPDATED_SUBJECT);
    }

    @Test
    @Transactional
    public void getAllQuestionnairesBySubjectNotContainsSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where subject does not contain DEFAULT_SUBJECT
        defaultQuestionnaireShouldNotBeFound("subject.doesNotContain=" + DEFAULT_SUBJECT);

        // Get all the questionnaireList where subject does not contain UPDATED_SUBJECT
        defaultQuestionnaireShouldBeFound("subject.doesNotContain=" + UPDATED_SUBJECT);
    }


    @Test
    @Transactional
    public void getAllQuestionnairesByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where startDate equals to DEFAULT_START_DATE
        defaultQuestionnaireShouldBeFound("startDate.equals=" + DEFAULT_START_DATE);

        // Get all the questionnaireList where startDate equals to UPDATED_START_DATE
        defaultQuestionnaireShouldNotBeFound("startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllQuestionnairesByStartDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where startDate not equals to DEFAULT_START_DATE
        defaultQuestionnaireShouldNotBeFound("startDate.notEquals=" + DEFAULT_START_DATE);

        // Get all the questionnaireList where startDate not equals to UPDATED_START_DATE
        defaultQuestionnaireShouldBeFound("startDate.notEquals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllQuestionnairesByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where startDate in DEFAULT_START_DATE or UPDATED_START_DATE
        defaultQuestionnaireShouldBeFound("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE);

        // Get all the questionnaireList where startDate equals to UPDATED_START_DATE
        defaultQuestionnaireShouldNotBeFound("startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    public void getAllQuestionnairesByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where startDate is not null
        defaultQuestionnaireShouldBeFound("startDate.specified=true");

        // Get all the questionnaireList where startDate is null
        defaultQuestionnaireShouldNotBeFound("startDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuestionnairesByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where endDate equals to DEFAULT_END_DATE
        defaultQuestionnaireShouldBeFound("endDate.equals=" + DEFAULT_END_DATE);

        // Get all the questionnaireList where endDate equals to UPDATED_END_DATE
        defaultQuestionnaireShouldNotBeFound("endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllQuestionnairesByEndDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where endDate not equals to DEFAULT_END_DATE
        defaultQuestionnaireShouldNotBeFound("endDate.notEquals=" + DEFAULT_END_DATE);

        // Get all the questionnaireList where endDate not equals to UPDATED_END_DATE
        defaultQuestionnaireShouldBeFound("endDate.notEquals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllQuestionnairesByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where endDate in DEFAULT_END_DATE or UPDATED_END_DATE
        defaultQuestionnaireShouldBeFound("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE);

        // Get all the questionnaireList where endDate equals to UPDATED_END_DATE
        defaultQuestionnaireShouldNotBeFound("endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void getAllQuestionnairesByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);

        // Get all the questionnaireList where endDate is not null
        defaultQuestionnaireShouldBeFound("endDate.specified=true");

        // Get all the questionnaireList where endDate is null
        defaultQuestionnaireShouldNotBeFound("endDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllQuestionnairesByPatientQuestionnaireIsEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);
        PatientQuestionnaire patientQuestionnaire = PatientQuestionnaireResourceIT.createEntity(em);
        em.persist(patientQuestionnaire);
        em.flush();
        questionnaire.addPatientQuestionnaire(patientQuestionnaire);
        questionnaireRepository.saveAndFlush(questionnaire);
        Long patientQuestionnaireId = patientQuestionnaire.getId();

        // Get all the questionnaireList where patientQuestionnaire equals to patientQuestionnaireId
        defaultQuestionnaireShouldBeFound("patientQuestionnaireId.equals=" + patientQuestionnaireId);

        // Get all the questionnaireList where patientQuestionnaire equals to patientQuestionnaireId + 1
        defaultQuestionnaireShouldNotBeFound("patientQuestionnaireId.equals=" + (patientQuestionnaireId + 1));
    }


    @Test
    @Transactional
    public void getAllQuestionnairesByQuestionIsEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);
        Question question = QuestionResourceIT.createEntity(em);
        em.persist(question);
        em.flush();
        questionnaire.addQuestion(question);
        questionnaireRepository.saveAndFlush(questionnaire);
        Long questionId = question.getId();

        // Get all the questionnaireList where question equals to questionId
        defaultQuestionnaireShouldBeFound("questionId.equals=" + questionId);

        // Get all the questionnaireList where question equals to questionId + 1
        defaultQuestionnaireShouldNotBeFound("questionId.equals=" + (questionId + 1));
    }


    @Test
    @Transactional
    public void getAllQuestionnairesByMedecinIsEqualToSomething() throws Exception {
        // Initialize the database
        questionnaireRepository.saveAndFlush(questionnaire);
        Medecin medecin = MedecinResourceIT.createEntity(em);
        em.persist(medecin);
        em.flush();
        questionnaire.setMedecin(medecin);
        questionnaireRepository.saveAndFlush(questionnaire);
        Long medecinId = medecin.getId();

        // Get all the questionnaireList where medecin equals to medecinId
        defaultQuestionnaireShouldBeFound("medecinId.equals=" + medecinId);

        // Get all the questionnaireList where medecin equals to medecinId + 1
        defaultQuestionnaireShouldNotBeFound("medecinId.equals=" + (medecinId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultQuestionnaireShouldBeFound(String filter) throws Exception {
        restQuestionnaireMockMvc.perform(get("/api/questionnaires?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(questionnaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].subject").value(hasItem(DEFAULT_SUBJECT)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())));

        // Check, that the count call also returns 1
        restQuestionnaireMockMvc.perform(get("/api/questionnaires/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultQuestionnaireShouldNotBeFound(String filter) throws Exception {
        restQuestionnaireMockMvc.perform(get("/api/questionnaires?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restQuestionnaireMockMvc.perform(get("/api/questionnaires/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingQuestionnaire() throws Exception {
        // Get the questionnaire
        restQuestionnaireMockMvc.perform(get("/api/questionnaires/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateQuestionnaire() throws Exception {
        // Initialize the database
        questionnaireService.save(questionnaire);

        int databaseSizeBeforeUpdate = questionnaireRepository.findAll().size();

        // Update the questionnaire
        Questionnaire updatedQuestionnaire = questionnaireRepository.findById(questionnaire.getId()).get();
        // Disconnect from session so that the updates on updatedQuestionnaire are not directly saved in db
        em.detach(updatedQuestionnaire);
        updatedQuestionnaire
            .subject(UPDATED_SUBJECT)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE);

        restQuestionnaireMockMvc.perform(put("/api/questionnaires")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedQuestionnaire)))
            .andExpect(status().isOk());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeUpdate);
        Questionnaire testQuestionnaire = questionnaireList.get(questionnaireList.size() - 1);
        assertThat(testQuestionnaire.getSubject()).isEqualTo(UPDATED_SUBJECT);
        assertThat(testQuestionnaire.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testQuestionnaire.getEndDate()).isEqualTo(UPDATED_END_DATE);
    }

    @Test
    @Transactional
    public void updateNonExistingQuestionnaire() throws Exception {
        int databaseSizeBeforeUpdate = questionnaireRepository.findAll().size();

        // Create the Questionnaire

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restQuestionnaireMockMvc.perform(put("/api/questionnaires")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(questionnaire)))
            .andExpect(status().isBadRequest());

        // Validate the Questionnaire in the database
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteQuestionnaire() throws Exception {
        // Initialize the database
        questionnaireService.save(questionnaire);

        int databaseSizeBeforeDelete = questionnaireRepository.findAll().size();

        // Delete the questionnaire
        restQuestionnaireMockMvc.perform(delete("/api/questionnaires/{id}", questionnaire.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Questionnaire> questionnaireList = questionnaireRepository.findAll();
        assertThat(questionnaireList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
