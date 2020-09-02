package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.TnTsuiviApp;
import com.mycompany.myapp.domain.PatientQuestionnaire;
import com.mycompany.myapp.domain.PatientReponse;
import com.mycompany.myapp.domain.Patient;
import com.mycompany.myapp.domain.Questionnaire;
import com.mycompany.myapp.repository.PatientQuestionnaireRepository;
import com.mycompany.myapp.service.PatientQuestionnaireService;
import com.mycompany.myapp.service.dto.PatientQuestionnaireCriteria;
import com.mycompany.myapp.service.PatientQuestionnaireQueryService;

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
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link PatientQuestionnaireResource} REST controller.
 */
@SpringBootTest(classes = TnTsuiviApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class PatientQuestionnaireResourceIT {

    private static final Instant DEFAULT_DONE_TIME_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DONE_TIME_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_DONE = false;
    private static final Boolean UPDATED_DONE = true;

    @Autowired
    private PatientQuestionnaireRepository patientQuestionnaireRepository;

    @Autowired
    private PatientQuestionnaireService patientQuestionnaireService;

    @Autowired
    private PatientQuestionnaireQueryService patientQuestionnaireQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPatientQuestionnaireMockMvc;

    private PatientQuestionnaire patientQuestionnaire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PatientQuestionnaire createEntity(EntityManager em) {
        PatientQuestionnaire patientQuestionnaire = new PatientQuestionnaire()
            .doneTimeDate(DEFAULT_DONE_TIME_DATE)
            .done(DEFAULT_DONE);
        return patientQuestionnaire;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PatientQuestionnaire createUpdatedEntity(EntityManager em) {
        PatientQuestionnaire patientQuestionnaire = new PatientQuestionnaire()
            .doneTimeDate(UPDATED_DONE_TIME_DATE)
            .done(UPDATED_DONE);
        return patientQuestionnaire;
    }

    @BeforeEach
    public void initTest() {
        patientQuestionnaire = createEntity(em);
    }

    @Test
    @Transactional
    public void createPatientQuestionnaire() throws Exception {
        int databaseSizeBeforeCreate = patientQuestionnaireRepository.findAll().size();

        // Create the PatientQuestionnaire
        restPatientQuestionnaireMockMvc.perform(post("/api/patient-questionnaires")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patientQuestionnaire)))
            .andExpect(status().isCreated());

        // Validate the PatientQuestionnaire in the database
        List<PatientQuestionnaire> patientQuestionnaireList = patientQuestionnaireRepository.findAll();
        assertThat(patientQuestionnaireList).hasSize(databaseSizeBeforeCreate + 1);
        PatientQuestionnaire testPatientQuestionnaire = patientQuestionnaireList.get(patientQuestionnaireList.size() - 1);
        assertThat(testPatientQuestionnaire.getDoneTimeDate()).isEqualTo(DEFAULT_DONE_TIME_DATE);
        assertThat(testPatientQuestionnaire.isDone()).isEqualTo(DEFAULT_DONE);
    }

    @Test
    @Transactional
    public void createPatientQuestionnaireWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = patientQuestionnaireRepository.findAll().size();

        // Create the PatientQuestionnaire with an existing ID
        patientQuestionnaire.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPatientQuestionnaireMockMvc.perform(post("/api/patient-questionnaires")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patientQuestionnaire)))
            .andExpect(status().isBadRequest());

        // Validate the PatientQuestionnaire in the database
        List<PatientQuestionnaire> patientQuestionnaireList = patientQuestionnaireRepository.findAll();
        assertThat(patientQuestionnaireList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllPatientQuestionnaires() throws Exception {
        // Initialize the database
        patientQuestionnaireRepository.saveAndFlush(patientQuestionnaire);

        // Get all the patientQuestionnaireList
        restPatientQuestionnaireMockMvc.perform(get("/api/patient-questionnaires?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patientQuestionnaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].doneTimeDate").value(hasItem(DEFAULT_DONE_TIME_DATE.toString())))
            .andExpect(jsonPath("$.[*].done").value(hasItem(DEFAULT_DONE.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getPatientQuestionnaire() throws Exception {
        // Initialize the database
        patientQuestionnaireRepository.saveAndFlush(patientQuestionnaire);

        // Get the patientQuestionnaire
        restPatientQuestionnaireMockMvc.perform(get("/api/patient-questionnaires/{id}", patientQuestionnaire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(patientQuestionnaire.getId().intValue()))
            .andExpect(jsonPath("$.doneTimeDate").value(DEFAULT_DONE_TIME_DATE.toString()))
            .andExpect(jsonPath("$.done").value(DEFAULT_DONE.booleanValue()));
    }


    @Test
    @Transactional
    public void getPatientQuestionnairesByIdFiltering() throws Exception {
        // Initialize the database
        patientQuestionnaireRepository.saveAndFlush(patientQuestionnaire);

        Long id = patientQuestionnaire.getId();

        defaultPatientQuestionnaireShouldBeFound("id.equals=" + id);
        defaultPatientQuestionnaireShouldNotBeFound("id.notEquals=" + id);

        defaultPatientQuestionnaireShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPatientQuestionnaireShouldNotBeFound("id.greaterThan=" + id);

        defaultPatientQuestionnaireShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPatientQuestionnaireShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPatientQuestionnairesByDoneTimeDateIsEqualToSomething() throws Exception {
        // Initialize the database
        patientQuestionnaireRepository.saveAndFlush(patientQuestionnaire);

        // Get all the patientQuestionnaireList where doneTimeDate equals to DEFAULT_DONE_TIME_DATE
        defaultPatientQuestionnaireShouldBeFound("doneTimeDate.equals=" + DEFAULT_DONE_TIME_DATE);

        // Get all the patientQuestionnaireList where doneTimeDate equals to UPDATED_DONE_TIME_DATE
        defaultPatientQuestionnaireShouldNotBeFound("doneTimeDate.equals=" + UPDATED_DONE_TIME_DATE);
    }

    @Test
    @Transactional
    public void getAllPatientQuestionnairesByDoneTimeDateIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientQuestionnaireRepository.saveAndFlush(patientQuestionnaire);

        // Get all the patientQuestionnaireList where doneTimeDate not equals to DEFAULT_DONE_TIME_DATE
        defaultPatientQuestionnaireShouldNotBeFound("doneTimeDate.notEquals=" + DEFAULT_DONE_TIME_DATE);

        // Get all the patientQuestionnaireList where doneTimeDate not equals to UPDATED_DONE_TIME_DATE
        defaultPatientQuestionnaireShouldBeFound("doneTimeDate.notEquals=" + UPDATED_DONE_TIME_DATE);
    }

    @Test
    @Transactional
    public void getAllPatientQuestionnairesByDoneTimeDateIsInShouldWork() throws Exception {
        // Initialize the database
        patientQuestionnaireRepository.saveAndFlush(patientQuestionnaire);

        // Get all the patientQuestionnaireList where doneTimeDate in DEFAULT_DONE_TIME_DATE or UPDATED_DONE_TIME_DATE
        defaultPatientQuestionnaireShouldBeFound("doneTimeDate.in=" + DEFAULT_DONE_TIME_DATE + "," + UPDATED_DONE_TIME_DATE);

        // Get all the patientQuestionnaireList where doneTimeDate equals to UPDATED_DONE_TIME_DATE
        defaultPatientQuestionnaireShouldNotBeFound("doneTimeDate.in=" + UPDATED_DONE_TIME_DATE);
    }

    @Test
    @Transactional
    public void getAllPatientQuestionnairesByDoneTimeDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientQuestionnaireRepository.saveAndFlush(patientQuestionnaire);

        // Get all the patientQuestionnaireList where doneTimeDate is not null
        defaultPatientQuestionnaireShouldBeFound("doneTimeDate.specified=true");

        // Get all the patientQuestionnaireList where doneTimeDate is null
        defaultPatientQuestionnaireShouldNotBeFound("doneTimeDate.specified=false");
    }

    @Test
    @Transactional
    public void getAllPatientQuestionnairesByDoneIsEqualToSomething() throws Exception {
        // Initialize the database
        patientQuestionnaireRepository.saveAndFlush(patientQuestionnaire);

        // Get all the patientQuestionnaireList where done equals to DEFAULT_DONE
        defaultPatientQuestionnaireShouldBeFound("done.equals=" + DEFAULT_DONE);

        // Get all the patientQuestionnaireList where done equals to UPDATED_DONE
        defaultPatientQuestionnaireShouldNotBeFound("done.equals=" + UPDATED_DONE);
    }

    @Test
    @Transactional
    public void getAllPatientQuestionnairesByDoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientQuestionnaireRepository.saveAndFlush(patientQuestionnaire);

        // Get all the patientQuestionnaireList where done not equals to DEFAULT_DONE
        defaultPatientQuestionnaireShouldNotBeFound("done.notEquals=" + DEFAULT_DONE);

        // Get all the patientQuestionnaireList where done not equals to UPDATED_DONE
        defaultPatientQuestionnaireShouldBeFound("done.notEquals=" + UPDATED_DONE);
    }

    @Test
    @Transactional
    public void getAllPatientQuestionnairesByDoneIsInShouldWork() throws Exception {
        // Initialize the database
        patientQuestionnaireRepository.saveAndFlush(patientQuestionnaire);

        // Get all the patientQuestionnaireList where done in DEFAULT_DONE or UPDATED_DONE
        defaultPatientQuestionnaireShouldBeFound("done.in=" + DEFAULT_DONE + "," + UPDATED_DONE);

        // Get all the patientQuestionnaireList where done equals to UPDATED_DONE
        defaultPatientQuestionnaireShouldNotBeFound("done.in=" + UPDATED_DONE);
    }

    @Test
    @Transactional
    public void getAllPatientQuestionnairesByDoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientQuestionnaireRepository.saveAndFlush(patientQuestionnaire);

        // Get all the patientQuestionnaireList where done is not null
        defaultPatientQuestionnaireShouldBeFound("done.specified=true");

        // Get all the patientQuestionnaireList where done is null
        defaultPatientQuestionnaireShouldNotBeFound("done.specified=false");
    }

    @Test
    @Transactional
    public void getAllPatientQuestionnairesByPatientReponseIsEqualToSomething() throws Exception {
        // Initialize the database
        patientQuestionnaireRepository.saveAndFlush(patientQuestionnaire);
        PatientReponse patientReponse = PatientReponseResourceIT.createEntity(em);
        em.persist(patientReponse);
        em.flush();
        patientQuestionnaire.addPatientReponse(patientReponse);
        patientQuestionnaireRepository.saveAndFlush(patientQuestionnaire);
        Long patientReponseId = patientReponse.getId();

        // Get all the patientQuestionnaireList where patientReponse equals to patientReponseId
        defaultPatientQuestionnaireShouldBeFound("patientReponseId.equals=" + patientReponseId);

        // Get all the patientQuestionnaireList where patientReponse equals to patientReponseId + 1
        defaultPatientQuestionnaireShouldNotBeFound("patientReponseId.equals=" + (patientReponseId + 1));
    }


    @Test
    @Transactional
    public void getAllPatientQuestionnairesByPatientIsEqualToSomething() throws Exception {
        // Initialize the database
        patientQuestionnaireRepository.saveAndFlush(patientQuestionnaire);
        Patient patient = PatientResourceIT.createEntity(em);
        em.persist(patient);
        em.flush();
        patientQuestionnaire.setPatient(patient);
        patientQuestionnaireRepository.saveAndFlush(patientQuestionnaire);
        Long patientId = patient.getId();

        // Get all the patientQuestionnaireList where patient equals to patientId
        defaultPatientQuestionnaireShouldBeFound("patientId.equals=" + patientId);

        // Get all the patientQuestionnaireList where patient equals to patientId + 1
        defaultPatientQuestionnaireShouldNotBeFound("patientId.equals=" + (patientId + 1));
    }


    @Test
    @Transactional
    public void getAllPatientQuestionnairesByQuestionnaireIsEqualToSomething() throws Exception {
        // Initialize the database
        patientQuestionnaireRepository.saveAndFlush(patientQuestionnaire);
        Questionnaire questionnaire = QuestionnaireResourceIT.createEntity(em);
        em.persist(questionnaire);
        em.flush();
        patientQuestionnaire.setQuestionnaire(questionnaire);
        patientQuestionnaireRepository.saveAndFlush(patientQuestionnaire);
        Long questionnaireId = questionnaire.getId();

        // Get all the patientQuestionnaireList where questionnaire equals to questionnaireId
        defaultPatientQuestionnaireShouldBeFound("questionnaireId.equals=" + questionnaireId);

        // Get all the patientQuestionnaireList where questionnaire equals to questionnaireId + 1
        defaultPatientQuestionnaireShouldNotBeFound("questionnaireId.equals=" + (questionnaireId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPatientQuestionnaireShouldBeFound(String filter) throws Exception {
        restPatientQuestionnaireMockMvc.perform(get("/api/patient-questionnaires?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patientQuestionnaire.getId().intValue())))
            .andExpect(jsonPath("$.[*].doneTimeDate").value(hasItem(DEFAULT_DONE_TIME_DATE.toString())))
            .andExpect(jsonPath("$.[*].done").value(hasItem(DEFAULT_DONE.booleanValue())));

        // Check, that the count call also returns 1
        restPatientQuestionnaireMockMvc.perform(get("/api/patient-questionnaires/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPatientQuestionnaireShouldNotBeFound(String filter) throws Exception {
        restPatientQuestionnaireMockMvc.perform(get("/api/patient-questionnaires?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPatientQuestionnaireMockMvc.perform(get("/api/patient-questionnaires/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPatientQuestionnaire() throws Exception {
        // Get the patientQuestionnaire
        restPatientQuestionnaireMockMvc.perform(get("/api/patient-questionnaires/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePatientQuestionnaire() throws Exception {
        // Initialize the database
        patientQuestionnaireService.save(patientQuestionnaire);

        int databaseSizeBeforeUpdate = patientQuestionnaireRepository.findAll().size();

        // Update the patientQuestionnaire
        PatientQuestionnaire updatedPatientQuestionnaire = patientQuestionnaireRepository.findById(patientQuestionnaire.getId()).get();
        // Disconnect from session so that the updates on updatedPatientQuestionnaire are not directly saved in db
        em.detach(updatedPatientQuestionnaire);
        updatedPatientQuestionnaire
            .doneTimeDate(UPDATED_DONE_TIME_DATE)
            .done(UPDATED_DONE);

        restPatientQuestionnaireMockMvc.perform(put("/api/patient-questionnaires")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPatientQuestionnaire)))
            .andExpect(status().isOk());

        // Validate the PatientQuestionnaire in the database
        List<PatientQuestionnaire> patientQuestionnaireList = patientQuestionnaireRepository.findAll();
        assertThat(patientQuestionnaireList).hasSize(databaseSizeBeforeUpdate);
        PatientQuestionnaire testPatientQuestionnaire = patientQuestionnaireList.get(patientQuestionnaireList.size() - 1);
        assertThat(testPatientQuestionnaire.getDoneTimeDate()).isEqualTo(UPDATED_DONE_TIME_DATE);
        assertThat(testPatientQuestionnaire.isDone()).isEqualTo(UPDATED_DONE);
    }

    @Test
    @Transactional
    public void updateNonExistingPatientQuestionnaire() throws Exception {
        int databaseSizeBeforeUpdate = patientQuestionnaireRepository.findAll().size();

        // Create the PatientQuestionnaire

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientQuestionnaireMockMvc.perform(put("/api/patient-questionnaires")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patientQuestionnaire)))
            .andExpect(status().isBadRequest());

        // Validate the PatientQuestionnaire in the database
        List<PatientQuestionnaire> patientQuestionnaireList = patientQuestionnaireRepository.findAll();
        assertThat(patientQuestionnaireList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePatientQuestionnaire() throws Exception {
        // Initialize the database
        patientQuestionnaireService.save(patientQuestionnaire);

        int databaseSizeBeforeDelete = patientQuestionnaireRepository.findAll().size();

        // Delete the patientQuestionnaire
        restPatientQuestionnaireMockMvc.perform(delete("/api/patient-questionnaires/{id}", patientQuestionnaire.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PatientQuestionnaire> patientQuestionnaireList = patientQuestionnaireRepository.findAll();
        assertThat(patientQuestionnaireList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
