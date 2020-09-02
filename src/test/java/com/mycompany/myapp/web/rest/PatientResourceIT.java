package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.TnTsuiviApp;
import com.mycompany.myapp.domain.Patient;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.PatientQuestionnaire;
import com.mycompany.myapp.domain.Medecin;
import com.mycompany.myapp.domain.Centre;
import com.mycompany.myapp.domain.GroupeDePatient;
import com.mycompany.myapp.repository.PatientRepository;
import com.mycompany.myapp.service.PatientService;
import com.mycompany.myapp.service.dto.PatientCriteria;
import com.mycompany.myapp.service.PatientQueryService;

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
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.domain.enumeration.Sexe;
import com.mycompany.myapp.domain.enumeration.Alcool;
import com.mycompany.myapp.domain.enumeration.Tobacco;
/**
 * Integration tests for the {@link PatientResource} REST controller.
 */
@SpringBootTest(classes = TnTsuiviApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class PatientResourceIT {

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADRESS = "BBBBBBBBBB";

    private static final Sexe DEFAULT_SEXE = Sexe.Feminin;
    private static final Sexe UPDATED_SEXE = Sexe.Masculin;

    private static final Alcool DEFAULT_ALCOOL = Alcool.No;
    private static final Alcool UPDATED_ALCOOL = Alcool.Yes;

    private static final LocalDate DEFAULT_START_DATE_ALCOOL = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE_ALCOOL = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE_ALCOOL = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE_ALCOOL = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE_ALCOOL = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE_ALCOOL = LocalDate.ofEpochDay(-1L);

    private static final Tobacco DEFAULT_TOBACOO = Tobacco.No;
    private static final Tobacco UPDATED_TOBACOO = Tobacco.Yes;

    private static final LocalDate DEFAULT_START_DATE_TOBACCO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE_TOBACCO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_START_DATE_TOBACCO = LocalDate.ofEpochDay(-1L);

    private static final LocalDate DEFAULT_END_DATE_TOBACCO = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE_TOBACCO = LocalDate.now(ZoneId.systemDefault());
    private static final LocalDate SMALLER_END_DATE_TOBACCO = LocalDate.ofEpochDay(-1L);

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private PatientService patientService;

    @Autowired
    private PatientQueryService patientQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPatientMockMvc;

    private Patient patient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patient createEntity(EntityManager em) {
        Patient patient = new Patient()
            .fullName(DEFAULT_FULL_NAME)
            .phone(DEFAULT_PHONE)
            .adress(DEFAULT_ADRESS)
            .sexe(DEFAULT_SEXE)
            .alcool(DEFAULT_ALCOOL)
            .startDateAlcool(DEFAULT_START_DATE_ALCOOL)
            .endDateAlcool(DEFAULT_END_DATE_ALCOOL)
            .tobacoo(DEFAULT_TOBACOO)
            .startDateTobacco(DEFAULT_START_DATE_TOBACCO)
            .endDateTobacco(DEFAULT_END_DATE_TOBACCO);
        // Add required entity
        Centre centre;
        if (TestUtil.findAll(em, Centre.class).isEmpty()) {
            centre = CentreResourceIT.createEntity(em);
            em.persist(centre);
            em.flush();
        } else {
            centre = TestUtil.findAll(em, Centre.class).get(0);
        }
        patient.setCentre(centre);
        return patient;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Patient createUpdatedEntity(EntityManager em) {
        Patient patient = new Patient()
            .fullName(UPDATED_FULL_NAME)
            .phone(UPDATED_PHONE)
            .adress(UPDATED_ADRESS)
            .sexe(UPDATED_SEXE)
            .alcool(UPDATED_ALCOOL)
            .startDateAlcool(UPDATED_START_DATE_ALCOOL)
            .endDateAlcool(UPDATED_END_DATE_ALCOOL)
            .tobacoo(UPDATED_TOBACOO)
            .startDateTobacco(UPDATED_START_DATE_TOBACCO)
            .endDateTobacco(UPDATED_END_DATE_TOBACCO);
        // Add required entity
        Centre centre;
        if (TestUtil.findAll(em, Centre.class).isEmpty()) {
            centre = CentreResourceIT.createUpdatedEntity(em);
            em.persist(centre);
            em.flush();
        } else {
            centre = TestUtil.findAll(em, Centre.class).get(0);
        }
        patient.setCentre(centre);
        return patient;
    }

    @BeforeEach
    public void initTest() {
        patient = createEntity(em);
    }

    @Test
    @Transactional
    public void createPatient() throws Exception {
        int databaseSizeBeforeCreate = patientRepository.findAll().size();

        // Create the Patient
        restPatientMockMvc.perform(post("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isCreated());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeCreate + 1);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testPatient.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testPatient.getAdress()).isEqualTo(DEFAULT_ADRESS);
        assertThat(testPatient.getSexe()).isEqualTo(DEFAULT_SEXE);
        assertThat(testPatient.getAlcool()).isEqualTo(DEFAULT_ALCOOL);
        assertThat(testPatient.getStartDateAlcool()).isEqualTo(DEFAULT_START_DATE_ALCOOL);
        assertThat(testPatient.getEndDateAlcool()).isEqualTo(DEFAULT_END_DATE_ALCOOL);
        assertThat(testPatient.getTobacoo()).isEqualTo(DEFAULT_TOBACOO);
        assertThat(testPatient.getStartDateTobacco()).isEqualTo(DEFAULT_START_DATE_TOBACCO);
        assertThat(testPatient.getEndDateTobacco()).isEqualTo(DEFAULT_END_DATE_TOBACCO);
    }

    @Test
    @Transactional
    public void createPatientWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = patientRepository.findAll().size();

        // Create the Patient with an existing ID
        patient.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPatientMockMvc.perform(post("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setPhone(null);

        // Create the Patient, which fails.

        restPatientMockMvc.perform(post("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAdressIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setAdress(null);

        // Create the Patient, which fails.

        restPatientMockMvc.perform(post("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setSexe(null);

        // Create the Patient, which fails.

        restPatientMockMvc.perform(post("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAlcoolIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setAlcool(null);

        // Create the Patient, which fails.

        restPatientMockMvc.perform(post("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkTobacooIsRequired() throws Exception {
        int databaseSizeBeforeTest = patientRepository.findAll().size();
        // set the field null
        patient.setTobacoo(null);

        // Create the Patient, which fails.

        restPatientMockMvc.perform(post("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPatients() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList
        restPatientMockMvc.perform(get("/api/patients?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patient.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].alcool").value(hasItem(DEFAULT_ALCOOL.toString())))
            .andExpect(jsonPath("$.[*].startDateAlcool").value(hasItem(DEFAULT_START_DATE_ALCOOL.toString())))
            .andExpect(jsonPath("$.[*].endDateAlcool").value(hasItem(DEFAULT_END_DATE_ALCOOL.toString())))
            .andExpect(jsonPath("$.[*].tobacoo").value(hasItem(DEFAULT_TOBACOO.toString())))
            .andExpect(jsonPath("$.[*].startDateTobacco").value(hasItem(DEFAULT_START_DATE_TOBACCO.toString())))
            .andExpect(jsonPath("$.[*].endDateTobacco").value(hasItem(DEFAULT_END_DATE_TOBACCO.toString())));
    }
    
    @Test
    @Transactional
    public void getPatient() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get the patient
        restPatientMockMvc.perform(get("/api/patients/{id}", patient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(patient.getId().intValue()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.adress").value(DEFAULT_ADRESS))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()))
            .andExpect(jsonPath("$.alcool").value(DEFAULT_ALCOOL.toString()))
            .andExpect(jsonPath("$.startDateAlcool").value(DEFAULT_START_DATE_ALCOOL.toString()))
            .andExpect(jsonPath("$.endDateAlcool").value(DEFAULT_END_DATE_ALCOOL.toString()))
            .andExpect(jsonPath("$.tobacoo").value(DEFAULT_TOBACOO.toString()))
            .andExpect(jsonPath("$.startDateTobacco").value(DEFAULT_START_DATE_TOBACCO.toString()))
            .andExpect(jsonPath("$.endDateTobacco").value(DEFAULT_END_DATE_TOBACCO.toString()));
    }


    @Test
    @Transactional
    public void getPatientsByIdFiltering() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        Long id = patient.getId();

        defaultPatientShouldBeFound("id.equals=" + id);
        defaultPatientShouldNotBeFound("id.notEquals=" + id);

        defaultPatientShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultPatientShouldNotBeFound("id.greaterThan=" + id);

        defaultPatientShouldBeFound("id.lessThanOrEqual=" + id);
        defaultPatientShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllPatientsByFullNameIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where fullName equals to DEFAULT_FULL_NAME
        defaultPatientShouldBeFound("fullName.equals=" + DEFAULT_FULL_NAME);

        // Get all the patientList where fullName equals to UPDATED_FULL_NAME
        defaultPatientShouldNotBeFound("fullName.equals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllPatientsByFullNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where fullName not equals to DEFAULT_FULL_NAME
        defaultPatientShouldNotBeFound("fullName.notEquals=" + DEFAULT_FULL_NAME);

        // Get all the patientList where fullName not equals to UPDATED_FULL_NAME
        defaultPatientShouldBeFound("fullName.notEquals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllPatientsByFullNameIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where fullName in DEFAULT_FULL_NAME or UPDATED_FULL_NAME
        defaultPatientShouldBeFound("fullName.in=" + DEFAULT_FULL_NAME + "," + UPDATED_FULL_NAME);

        // Get all the patientList where fullName equals to UPDATED_FULL_NAME
        defaultPatientShouldNotBeFound("fullName.in=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllPatientsByFullNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where fullName is not null
        defaultPatientShouldBeFound("fullName.specified=true");

        // Get all the patientList where fullName is null
        defaultPatientShouldNotBeFound("fullName.specified=false");
    }
                @Test
    @Transactional
    public void getAllPatientsByFullNameContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where fullName contains DEFAULT_FULL_NAME
        defaultPatientShouldBeFound("fullName.contains=" + DEFAULT_FULL_NAME);

        // Get all the patientList where fullName contains UPDATED_FULL_NAME
        defaultPatientShouldNotBeFound("fullName.contains=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllPatientsByFullNameNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where fullName does not contain DEFAULT_FULL_NAME
        defaultPatientShouldNotBeFound("fullName.doesNotContain=" + DEFAULT_FULL_NAME);

        // Get all the patientList where fullName does not contain UPDATED_FULL_NAME
        defaultPatientShouldBeFound("fullName.doesNotContain=" + UPDATED_FULL_NAME);
    }


    @Test
    @Transactional
    public void getAllPatientsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where phone equals to DEFAULT_PHONE
        defaultPatientShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the patientList where phone equals to UPDATED_PHONE
        defaultPatientShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllPatientsByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where phone not equals to DEFAULT_PHONE
        defaultPatientShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the patientList where phone not equals to UPDATED_PHONE
        defaultPatientShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllPatientsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultPatientShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the patientList where phone equals to UPDATED_PHONE
        defaultPatientShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllPatientsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where phone is not null
        defaultPatientShouldBeFound("phone.specified=true");

        // Get all the patientList where phone is null
        defaultPatientShouldNotBeFound("phone.specified=false");
    }
                @Test
    @Transactional
    public void getAllPatientsByPhoneContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where phone contains DEFAULT_PHONE
        defaultPatientShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the patientList where phone contains UPDATED_PHONE
        defaultPatientShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllPatientsByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where phone does not contain DEFAULT_PHONE
        defaultPatientShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the patientList where phone does not contain UPDATED_PHONE
        defaultPatientShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }


    @Test
    @Transactional
    public void getAllPatientsByAdressIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where adress equals to DEFAULT_ADRESS
        defaultPatientShouldBeFound("adress.equals=" + DEFAULT_ADRESS);

        // Get all the patientList where adress equals to UPDATED_ADRESS
        defaultPatientShouldNotBeFound("adress.equals=" + UPDATED_ADRESS);
    }

    @Test
    @Transactional
    public void getAllPatientsByAdressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where adress not equals to DEFAULT_ADRESS
        defaultPatientShouldNotBeFound("adress.notEquals=" + DEFAULT_ADRESS);

        // Get all the patientList where adress not equals to UPDATED_ADRESS
        defaultPatientShouldBeFound("adress.notEquals=" + UPDATED_ADRESS);
    }

    @Test
    @Transactional
    public void getAllPatientsByAdressIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where adress in DEFAULT_ADRESS or UPDATED_ADRESS
        defaultPatientShouldBeFound("adress.in=" + DEFAULT_ADRESS + "," + UPDATED_ADRESS);

        // Get all the patientList where adress equals to UPDATED_ADRESS
        defaultPatientShouldNotBeFound("adress.in=" + UPDATED_ADRESS);
    }

    @Test
    @Transactional
    public void getAllPatientsByAdressIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where adress is not null
        defaultPatientShouldBeFound("adress.specified=true");

        // Get all the patientList where adress is null
        defaultPatientShouldNotBeFound("adress.specified=false");
    }
                @Test
    @Transactional
    public void getAllPatientsByAdressContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where adress contains DEFAULT_ADRESS
        defaultPatientShouldBeFound("adress.contains=" + DEFAULT_ADRESS);

        // Get all the patientList where adress contains UPDATED_ADRESS
        defaultPatientShouldNotBeFound("adress.contains=" + UPDATED_ADRESS);
    }

    @Test
    @Transactional
    public void getAllPatientsByAdressNotContainsSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where adress does not contain DEFAULT_ADRESS
        defaultPatientShouldNotBeFound("adress.doesNotContain=" + DEFAULT_ADRESS);

        // Get all the patientList where adress does not contain UPDATED_ADRESS
        defaultPatientShouldBeFound("adress.doesNotContain=" + UPDATED_ADRESS);
    }


    @Test
    @Transactional
    public void getAllPatientsBySexeIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where sexe equals to DEFAULT_SEXE
        defaultPatientShouldBeFound("sexe.equals=" + DEFAULT_SEXE);

        // Get all the patientList where sexe equals to UPDATED_SEXE
        defaultPatientShouldNotBeFound("sexe.equals=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    public void getAllPatientsBySexeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where sexe not equals to DEFAULT_SEXE
        defaultPatientShouldNotBeFound("sexe.notEquals=" + DEFAULT_SEXE);

        // Get all the patientList where sexe not equals to UPDATED_SEXE
        defaultPatientShouldBeFound("sexe.notEquals=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    public void getAllPatientsBySexeIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where sexe in DEFAULT_SEXE or UPDATED_SEXE
        defaultPatientShouldBeFound("sexe.in=" + DEFAULT_SEXE + "," + UPDATED_SEXE);

        // Get all the patientList where sexe equals to UPDATED_SEXE
        defaultPatientShouldNotBeFound("sexe.in=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    public void getAllPatientsBySexeIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where sexe is not null
        defaultPatientShouldBeFound("sexe.specified=true");

        // Get all the patientList where sexe is null
        defaultPatientShouldNotBeFound("sexe.specified=false");
    }

    @Test
    @Transactional
    public void getAllPatientsByAlcoolIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where alcool equals to DEFAULT_ALCOOL
        defaultPatientShouldBeFound("alcool.equals=" + DEFAULT_ALCOOL);

        // Get all the patientList where alcool equals to UPDATED_ALCOOL
        defaultPatientShouldNotBeFound("alcool.equals=" + UPDATED_ALCOOL);
    }

    @Test
    @Transactional
    public void getAllPatientsByAlcoolIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where alcool not equals to DEFAULT_ALCOOL
        defaultPatientShouldNotBeFound("alcool.notEquals=" + DEFAULT_ALCOOL);

        // Get all the patientList where alcool not equals to UPDATED_ALCOOL
        defaultPatientShouldBeFound("alcool.notEquals=" + UPDATED_ALCOOL);
    }

    @Test
    @Transactional
    public void getAllPatientsByAlcoolIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where alcool in DEFAULT_ALCOOL or UPDATED_ALCOOL
        defaultPatientShouldBeFound("alcool.in=" + DEFAULT_ALCOOL + "," + UPDATED_ALCOOL);

        // Get all the patientList where alcool equals to UPDATED_ALCOOL
        defaultPatientShouldNotBeFound("alcool.in=" + UPDATED_ALCOOL);
    }

    @Test
    @Transactional
    public void getAllPatientsByAlcoolIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where alcool is not null
        defaultPatientShouldBeFound("alcool.specified=true");

        // Get all the patientList where alcool is null
        defaultPatientShouldNotBeFound("alcool.specified=false");
    }

    @Test
    @Transactional
    public void getAllPatientsByStartDateAlcoolIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where startDateAlcool equals to DEFAULT_START_DATE_ALCOOL
        defaultPatientShouldBeFound("startDateAlcool.equals=" + DEFAULT_START_DATE_ALCOOL);

        // Get all the patientList where startDateAlcool equals to UPDATED_START_DATE_ALCOOL
        defaultPatientShouldNotBeFound("startDateAlcool.equals=" + UPDATED_START_DATE_ALCOOL);
    }

    @Test
    @Transactional
    public void getAllPatientsByStartDateAlcoolIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where startDateAlcool not equals to DEFAULT_START_DATE_ALCOOL
        defaultPatientShouldNotBeFound("startDateAlcool.notEquals=" + DEFAULT_START_DATE_ALCOOL);

        // Get all the patientList where startDateAlcool not equals to UPDATED_START_DATE_ALCOOL
        defaultPatientShouldBeFound("startDateAlcool.notEquals=" + UPDATED_START_DATE_ALCOOL);
    }

    @Test
    @Transactional
    public void getAllPatientsByStartDateAlcoolIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where startDateAlcool in DEFAULT_START_DATE_ALCOOL or UPDATED_START_DATE_ALCOOL
        defaultPatientShouldBeFound("startDateAlcool.in=" + DEFAULT_START_DATE_ALCOOL + "," + UPDATED_START_DATE_ALCOOL);

        // Get all the patientList where startDateAlcool equals to UPDATED_START_DATE_ALCOOL
        defaultPatientShouldNotBeFound("startDateAlcool.in=" + UPDATED_START_DATE_ALCOOL);
    }

    @Test
    @Transactional
    public void getAllPatientsByStartDateAlcoolIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where startDateAlcool is not null
        defaultPatientShouldBeFound("startDateAlcool.specified=true");

        // Get all the patientList where startDateAlcool is null
        defaultPatientShouldNotBeFound("startDateAlcool.specified=false");
    }

    @Test
    @Transactional
    public void getAllPatientsByStartDateAlcoolIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where startDateAlcool is greater than or equal to DEFAULT_START_DATE_ALCOOL
        defaultPatientShouldBeFound("startDateAlcool.greaterThanOrEqual=" + DEFAULT_START_DATE_ALCOOL);

        // Get all the patientList where startDateAlcool is greater than or equal to UPDATED_START_DATE_ALCOOL
        defaultPatientShouldNotBeFound("startDateAlcool.greaterThanOrEqual=" + UPDATED_START_DATE_ALCOOL);
    }

    @Test
    @Transactional
    public void getAllPatientsByStartDateAlcoolIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where startDateAlcool is less than or equal to DEFAULT_START_DATE_ALCOOL
        defaultPatientShouldBeFound("startDateAlcool.lessThanOrEqual=" + DEFAULT_START_DATE_ALCOOL);

        // Get all the patientList where startDateAlcool is less than or equal to SMALLER_START_DATE_ALCOOL
        defaultPatientShouldNotBeFound("startDateAlcool.lessThanOrEqual=" + SMALLER_START_DATE_ALCOOL);
    }

    @Test
    @Transactional
    public void getAllPatientsByStartDateAlcoolIsLessThanSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where startDateAlcool is less than DEFAULT_START_DATE_ALCOOL
        defaultPatientShouldNotBeFound("startDateAlcool.lessThan=" + DEFAULT_START_DATE_ALCOOL);

        // Get all the patientList where startDateAlcool is less than UPDATED_START_DATE_ALCOOL
        defaultPatientShouldBeFound("startDateAlcool.lessThan=" + UPDATED_START_DATE_ALCOOL);
    }

    @Test
    @Transactional
    public void getAllPatientsByStartDateAlcoolIsGreaterThanSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where startDateAlcool is greater than DEFAULT_START_DATE_ALCOOL
        defaultPatientShouldNotBeFound("startDateAlcool.greaterThan=" + DEFAULT_START_DATE_ALCOOL);

        // Get all the patientList where startDateAlcool is greater than SMALLER_START_DATE_ALCOOL
        defaultPatientShouldBeFound("startDateAlcool.greaterThan=" + SMALLER_START_DATE_ALCOOL);
    }


    @Test
    @Transactional
    public void getAllPatientsByEndDateAlcoolIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where endDateAlcool equals to DEFAULT_END_DATE_ALCOOL
        defaultPatientShouldBeFound("endDateAlcool.equals=" + DEFAULT_END_DATE_ALCOOL);

        // Get all the patientList where endDateAlcool equals to UPDATED_END_DATE_ALCOOL
        defaultPatientShouldNotBeFound("endDateAlcool.equals=" + UPDATED_END_DATE_ALCOOL);
    }

    @Test
    @Transactional
    public void getAllPatientsByEndDateAlcoolIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where endDateAlcool not equals to DEFAULT_END_DATE_ALCOOL
        defaultPatientShouldNotBeFound("endDateAlcool.notEquals=" + DEFAULT_END_DATE_ALCOOL);

        // Get all the patientList where endDateAlcool not equals to UPDATED_END_DATE_ALCOOL
        defaultPatientShouldBeFound("endDateAlcool.notEquals=" + UPDATED_END_DATE_ALCOOL);
    }

    @Test
    @Transactional
    public void getAllPatientsByEndDateAlcoolIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where endDateAlcool in DEFAULT_END_DATE_ALCOOL or UPDATED_END_DATE_ALCOOL
        defaultPatientShouldBeFound("endDateAlcool.in=" + DEFAULT_END_DATE_ALCOOL + "," + UPDATED_END_DATE_ALCOOL);

        // Get all the patientList where endDateAlcool equals to UPDATED_END_DATE_ALCOOL
        defaultPatientShouldNotBeFound("endDateAlcool.in=" + UPDATED_END_DATE_ALCOOL);
    }

    @Test
    @Transactional
    public void getAllPatientsByEndDateAlcoolIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where endDateAlcool is not null
        defaultPatientShouldBeFound("endDateAlcool.specified=true");

        // Get all the patientList where endDateAlcool is null
        defaultPatientShouldNotBeFound("endDateAlcool.specified=false");
    }

    @Test
    @Transactional
    public void getAllPatientsByEndDateAlcoolIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where endDateAlcool is greater than or equal to DEFAULT_END_DATE_ALCOOL
        defaultPatientShouldBeFound("endDateAlcool.greaterThanOrEqual=" + DEFAULT_END_DATE_ALCOOL);

        // Get all the patientList where endDateAlcool is greater than or equal to UPDATED_END_DATE_ALCOOL
        defaultPatientShouldNotBeFound("endDateAlcool.greaterThanOrEqual=" + UPDATED_END_DATE_ALCOOL);
    }

    @Test
    @Transactional
    public void getAllPatientsByEndDateAlcoolIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where endDateAlcool is less than or equal to DEFAULT_END_DATE_ALCOOL
        defaultPatientShouldBeFound("endDateAlcool.lessThanOrEqual=" + DEFAULT_END_DATE_ALCOOL);

        // Get all the patientList where endDateAlcool is less than or equal to SMALLER_END_DATE_ALCOOL
        defaultPatientShouldNotBeFound("endDateAlcool.lessThanOrEqual=" + SMALLER_END_DATE_ALCOOL);
    }

    @Test
    @Transactional
    public void getAllPatientsByEndDateAlcoolIsLessThanSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where endDateAlcool is less than DEFAULT_END_DATE_ALCOOL
        defaultPatientShouldNotBeFound("endDateAlcool.lessThan=" + DEFAULT_END_DATE_ALCOOL);

        // Get all the patientList where endDateAlcool is less than UPDATED_END_DATE_ALCOOL
        defaultPatientShouldBeFound("endDateAlcool.lessThan=" + UPDATED_END_DATE_ALCOOL);
    }

    @Test
    @Transactional
    public void getAllPatientsByEndDateAlcoolIsGreaterThanSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where endDateAlcool is greater than DEFAULT_END_DATE_ALCOOL
        defaultPatientShouldNotBeFound("endDateAlcool.greaterThan=" + DEFAULT_END_DATE_ALCOOL);

        // Get all the patientList where endDateAlcool is greater than SMALLER_END_DATE_ALCOOL
        defaultPatientShouldBeFound("endDateAlcool.greaterThan=" + SMALLER_END_DATE_ALCOOL);
    }


    @Test
    @Transactional
    public void getAllPatientsByTobacooIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where tobacoo equals to DEFAULT_TOBACOO
        defaultPatientShouldBeFound("tobacoo.equals=" + DEFAULT_TOBACOO);

        // Get all the patientList where tobacoo equals to UPDATED_TOBACOO
        defaultPatientShouldNotBeFound("tobacoo.equals=" + UPDATED_TOBACOO);
    }

    @Test
    @Transactional
    public void getAllPatientsByTobacooIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where tobacoo not equals to DEFAULT_TOBACOO
        defaultPatientShouldNotBeFound("tobacoo.notEquals=" + DEFAULT_TOBACOO);

        // Get all the patientList where tobacoo not equals to UPDATED_TOBACOO
        defaultPatientShouldBeFound("tobacoo.notEquals=" + UPDATED_TOBACOO);
    }

    @Test
    @Transactional
    public void getAllPatientsByTobacooIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where tobacoo in DEFAULT_TOBACOO or UPDATED_TOBACOO
        defaultPatientShouldBeFound("tobacoo.in=" + DEFAULT_TOBACOO + "," + UPDATED_TOBACOO);

        // Get all the patientList where tobacoo equals to UPDATED_TOBACOO
        defaultPatientShouldNotBeFound("tobacoo.in=" + UPDATED_TOBACOO);
    }

    @Test
    @Transactional
    public void getAllPatientsByTobacooIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where tobacoo is not null
        defaultPatientShouldBeFound("tobacoo.specified=true");

        // Get all the patientList where tobacoo is null
        defaultPatientShouldNotBeFound("tobacoo.specified=false");
    }

    @Test
    @Transactional
    public void getAllPatientsByStartDateTobaccoIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where startDateTobacco equals to DEFAULT_START_DATE_TOBACCO
        defaultPatientShouldBeFound("startDateTobacco.equals=" + DEFAULT_START_DATE_TOBACCO);

        // Get all the patientList where startDateTobacco equals to UPDATED_START_DATE_TOBACCO
        defaultPatientShouldNotBeFound("startDateTobacco.equals=" + UPDATED_START_DATE_TOBACCO);
    }

    @Test
    @Transactional
    public void getAllPatientsByStartDateTobaccoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where startDateTobacco not equals to DEFAULT_START_DATE_TOBACCO
        defaultPatientShouldNotBeFound("startDateTobacco.notEquals=" + DEFAULT_START_DATE_TOBACCO);

        // Get all the patientList where startDateTobacco not equals to UPDATED_START_DATE_TOBACCO
        defaultPatientShouldBeFound("startDateTobacco.notEquals=" + UPDATED_START_DATE_TOBACCO);
    }

    @Test
    @Transactional
    public void getAllPatientsByStartDateTobaccoIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where startDateTobacco in DEFAULT_START_DATE_TOBACCO or UPDATED_START_DATE_TOBACCO
        defaultPatientShouldBeFound("startDateTobacco.in=" + DEFAULT_START_DATE_TOBACCO + "," + UPDATED_START_DATE_TOBACCO);

        // Get all the patientList where startDateTobacco equals to UPDATED_START_DATE_TOBACCO
        defaultPatientShouldNotBeFound("startDateTobacco.in=" + UPDATED_START_DATE_TOBACCO);
    }

    @Test
    @Transactional
    public void getAllPatientsByStartDateTobaccoIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where startDateTobacco is not null
        defaultPatientShouldBeFound("startDateTobacco.specified=true");

        // Get all the patientList where startDateTobacco is null
        defaultPatientShouldNotBeFound("startDateTobacco.specified=false");
    }

    @Test
    @Transactional
    public void getAllPatientsByStartDateTobaccoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where startDateTobacco is greater than or equal to DEFAULT_START_DATE_TOBACCO
        defaultPatientShouldBeFound("startDateTobacco.greaterThanOrEqual=" + DEFAULT_START_DATE_TOBACCO);

        // Get all the patientList where startDateTobacco is greater than or equal to UPDATED_START_DATE_TOBACCO
        defaultPatientShouldNotBeFound("startDateTobacco.greaterThanOrEqual=" + UPDATED_START_DATE_TOBACCO);
    }

    @Test
    @Transactional
    public void getAllPatientsByStartDateTobaccoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where startDateTobacco is less than or equal to DEFAULT_START_DATE_TOBACCO
        defaultPatientShouldBeFound("startDateTobacco.lessThanOrEqual=" + DEFAULT_START_DATE_TOBACCO);

        // Get all the patientList where startDateTobacco is less than or equal to SMALLER_START_DATE_TOBACCO
        defaultPatientShouldNotBeFound("startDateTobacco.lessThanOrEqual=" + SMALLER_START_DATE_TOBACCO);
    }

    @Test
    @Transactional
    public void getAllPatientsByStartDateTobaccoIsLessThanSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where startDateTobacco is less than DEFAULT_START_DATE_TOBACCO
        defaultPatientShouldNotBeFound("startDateTobacco.lessThan=" + DEFAULT_START_DATE_TOBACCO);

        // Get all the patientList where startDateTobacco is less than UPDATED_START_DATE_TOBACCO
        defaultPatientShouldBeFound("startDateTobacco.lessThan=" + UPDATED_START_DATE_TOBACCO);
    }

    @Test
    @Transactional
    public void getAllPatientsByStartDateTobaccoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where startDateTobacco is greater than DEFAULT_START_DATE_TOBACCO
        defaultPatientShouldNotBeFound("startDateTobacco.greaterThan=" + DEFAULT_START_DATE_TOBACCO);

        // Get all the patientList where startDateTobacco is greater than SMALLER_START_DATE_TOBACCO
        defaultPatientShouldBeFound("startDateTobacco.greaterThan=" + SMALLER_START_DATE_TOBACCO);
    }


    @Test
    @Transactional
    public void getAllPatientsByEndDateTobaccoIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where endDateTobacco equals to DEFAULT_END_DATE_TOBACCO
        defaultPatientShouldBeFound("endDateTobacco.equals=" + DEFAULT_END_DATE_TOBACCO);

        // Get all the patientList where endDateTobacco equals to UPDATED_END_DATE_TOBACCO
        defaultPatientShouldNotBeFound("endDateTobacco.equals=" + UPDATED_END_DATE_TOBACCO);
    }

    @Test
    @Transactional
    public void getAllPatientsByEndDateTobaccoIsNotEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where endDateTobacco not equals to DEFAULT_END_DATE_TOBACCO
        defaultPatientShouldNotBeFound("endDateTobacco.notEquals=" + DEFAULT_END_DATE_TOBACCO);

        // Get all the patientList where endDateTobacco not equals to UPDATED_END_DATE_TOBACCO
        defaultPatientShouldBeFound("endDateTobacco.notEquals=" + UPDATED_END_DATE_TOBACCO);
    }

    @Test
    @Transactional
    public void getAllPatientsByEndDateTobaccoIsInShouldWork() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where endDateTobacco in DEFAULT_END_DATE_TOBACCO or UPDATED_END_DATE_TOBACCO
        defaultPatientShouldBeFound("endDateTobacco.in=" + DEFAULT_END_DATE_TOBACCO + "," + UPDATED_END_DATE_TOBACCO);

        // Get all the patientList where endDateTobacco equals to UPDATED_END_DATE_TOBACCO
        defaultPatientShouldNotBeFound("endDateTobacco.in=" + UPDATED_END_DATE_TOBACCO);
    }

    @Test
    @Transactional
    public void getAllPatientsByEndDateTobaccoIsNullOrNotNull() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where endDateTobacco is not null
        defaultPatientShouldBeFound("endDateTobacco.specified=true");

        // Get all the patientList where endDateTobacco is null
        defaultPatientShouldNotBeFound("endDateTobacco.specified=false");
    }

    @Test
    @Transactional
    public void getAllPatientsByEndDateTobaccoIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where endDateTobacco is greater than or equal to DEFAULT_END_DATE_TOBACCO
        defaultPatientShouldBeFound("endDateTobacco.greaterThanOrEqual=" + DEFAULT_END_DATE_TOBACCO);

        // Get all the patientList where endDateTobacco is greater than or equal to UPDATED_END_DATE_TOBACCO
        defaultPatientShouldNotBeFound("endDateTobacco.greaterThanOrEqual=" + UPDATED_END_DATE_TOBACCO);
    }

    @Test
    @Transactional
    public void getAllPatientsByEndDateTobaccoIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where endDateTobacco is less than or equal to DEFAULT_END_DATE_TOBACCO
        defaultPatientShouldBeFound("endDateTobacco.lessThanOrEqual=" + DEFAULT_END_DATE_TOBACCO);

        // Get all the patientList where endDateTobacco is less than or equal to SMALLER_END_DATE_TOBACCO
        defaultPatientShouldNotBeFound("endDateTobacco.lessThanOrEqual=" + SMALLER_END_DATE_TOBACCO);
    }

    @Test
    @Transactional
    public void getAllPatientsByEndDateTobaccoIsLessThanSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where endDateTobacco is less than DEFAULT_END_DATE_TOBACCO
        defaultPatientShouldNotBeFound("endDateTobacco.lessThan=" + DEFAULT_END_DATE_TOBACCO);

        // Get all the patientList where endDateTobacco is less than UPDATED_END_DATE_TOBACCO
        defaultPatientShouldBeFound("endDateTobacco.lessThan=" + UPDATED_END_DATE_TOBACCO);
    }

    @Test
    @Transactional
    public void getAllPatientsByEndDateTobaccoIsGreaterThanSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);

        // Get all the patientList where endDateTobacco is greater than DEFAULT_END_DATE_TOBACCO
        defaultPatientShouldNotBeFound("endDateTobacco.greaterThan=" + DEFAULT_END_DATE_TOBACCO);

        // Get all the patientList where endDateTobacco is greater than SMALLER_END_DATE_TOBACCO
        defaultPatientShouldBeFound("endDateTobacco.greaterThan=" + SMALLER_END_DATE_TOBACCO);
    }


    @Test
    @Transactional
    public void getAllPatientsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        patient.setUser(user);
        patientRepository.saveAndFlush(patient);
        Long userId = user.getId();

        // Get all the patientList where user equals to userId
        defaultPatientShouldBeFound("userId.equals=" + userId);

        // Get all the patientList where user equals to userId + 1
        defaultPatientShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllPatientsByPatientQuestionnaireIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);
        PatientQuestionnaire patientQuestionnaire = PatientQuestionnaireResourceIT.createEntity(em);
        em.persist(patientQuestionnaire);
        em.flush();
        patient.addPatientQuestionnaire(patientQuestionnaire);
        patientRepository.saveAndFlush(patient);
        Long patientQuestionnaireId = patientQuestionnaire.getId();

        // Get all the patientList where patientQuestionnaire equals to patientQuestionnaireId
        defaultPatientShouldBeFound("patientQuestionnaireId.equals=" + patientQuestionnaireId);

        // Get all the patientList where patientQuestionnaire equals to patientQuestionnaireId + 1
        defaultPatientShouldNotBeFound("patientQuestionnaireId.equals=" + (patientQuestionnaireId + 1));
    }


    @Test
    @Transactional
    public void getAllPatientsByMedecinIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);
        Medecin medecin = MedecinResourceIT.createEntity(em);
        em.persist(medecin);
        em.flush();
        patient.setMedecin(medecin);
        patientRepository.saveAndFlush(patient);
        Long medecinId = medecin.getId();

        // Get all the patientList where medecin equals to medecinId
        defaultPatientShouldBeFound("medecinId.equals=" + medecinId);

        // Get all the patientList where medecin equals to medecinId + 1
        defaultPatientShouldNotBeFound("medecinId.equals=" + (medecinId + 1));
    }


    @Test
    @Transactional
    public void getAllPatientsByCentreIsEqualToSomething() throws Exception {
        // Get already existing entity
        Centre centre = patient.getCentre();
        patientRepository.saveAndFlush(patient);
        Long centreId = centre.getId();

        // Get all the patientList where centre equals to centreId
        defaultPatientShouldBeFound("centreId.equals=" + centreId);

        // Get all the patientList where centre equals to centreId + 1
        defaultPatientShouldNotBeFound("centreId.equals=" + (centreId + 1));
    }


    @Test
    @Transactional
    public void getAllPatientsByGroupeDePatientIsEqualToSomething() throws Exception {
        // Initialize the database
        patientRepository.saveAndFlush(patient);
        GroupeDePatient groupeDePatient = GroupeDePatientResourceIT.createEntity(em);
        em.persist(groupeDePatient);
        em.flush();
        patient.addGroupeDePatient(groupeDePatient);
        patientRepository.saveAndFlush(patient);
        Long groupeDePatientId = groupeDePatient.getId();

        // Get all the patientList where groupeDePatient equals to groupeDePatientId
        defaultPatientShouldBeFound("groupeDePatientId.equals=" + groupeDePatientId);

        // Get all the patientList where groupeDePatient equals to groupeDePatientId + 1
        defaultPatientShouldNotBeFound("groupeDePatientId.equals=" + (groupeDePatientId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultPatientShouldBeFound(String filter) throws Exception {
        restPatientMockMvc.perform(get("/api/patients?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patient.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())))
            .andExpect(jsonPath("$.[*].alcool").value(hasItem(DEFAULT_ALCOOL.toString())))
            .andExpect(jsonPath("$.[*].startDateAlcool").value(hasItem(DEFAULT_START_DATE_ALCOOL.toString())))
            .andExpect(jsonPath("$.[*].endDateAlcool").value(hasItem(DEFAULT_END_DATE_ALCOOL.toString())))
            .andExpect(jsonPath("$.[*].tobacoo").value(hasItem(DEFAULT_TOBACOO.toString())))
            .andExpect(jsonPath("$.[*].startDateTobacco").value(hasItem(DEFAULT_START_DATE_TOBACCO.toString())))
            .andExpect(jsonPath("$.[*].endDateTobacco").value(hasItem(DEFAULT_END_DATE_TOBACCO.toString())));

        // Check, that the count call also returns 1
        restPatientMockMvc.perform(get("/api/patients/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultPatientShouldNotBeFound(String filter) throws Exception {
        restPatientMockMvc.perform(get("/api/patients?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restPatientMockMvc.perform(get("/api/patients/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingPatient() throws Exception {
        // Get the patient
        restPatientMockMvc.perform(get("/api/patients/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePatient() throws Exception {
        // Initialize the database
        patientService.save(patient);

        int databaseSizeBeforeUpdate = patientRepository.findAll().size();

        // Update the patient
        Patient updatedPatient = patientRepository.findById(patient.getId()).get();
        // Disconnect from session so that the updates on updatedPatient are not directly saved in db
        em.detach(updatedPatient);
        updatedPatient
            .fullName(UPDATED_FULL_NAME)
            .phone(UPDATED_PHONE)
            .adress(UPDATED_ADRESS)
            .sexe(UPDATED_SEXE)
            .alcool(UPDATED_ALCOOL)
            .startDateAlcool(UPDATED_START_DATE_ALCOOL)
            .endDateAlcool(UPDATED_END_DATE_ALCOOL)
            .tobacoo(UPDATED_TOBACOO)
            .startDateTobacco(UPDATED_START_DATE_TOBACCO)
            .endDateTobacco(UPDATED_END_DATE_TOBACCO);

        restPatientMockMvc.perform(put("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedPatient)))
            .andExpect(status().isOk());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
        Patient testPatient = patientList.get(patientList.size() - 1);
        assertThat(testPatient.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testPatient.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testPatient.getAdress()).isEqualTo(UPDATED_ADRESS);
        assertThat(testPatient.getSexe()).isEqualTo(UPDATED_SEXE);
        assertThat(testPatient.getAlcool()).isEqualTo(UPDATED_ALCOOL);
        assertThat(testPatient.getStartDateAlcool()).isEqualTo(UPDATED_START_DATE_ALCOOL);
        assertThat(testPatient.getEndDateAlcool()).isEqualTo(UPDATED_END_DATE_ALCOOL);
        assertThat(testPatient.getTobacoo()).isEqualTo(UPDATED_TOBACOO);
        assertThat(testPatient.getStartDateTobacco()).isEqualTo(UPDATED_START_DATE_TOBACCO);
        assertThat(testPatient.getEndDateTobacco()).isEqualTo(UPDATED_END_DATE_TOBACCO);
    }

    @Test
    @Transactional
    public void updateNonExistingPatient() throws Exception {
        int databaseSizeBeforeUpdate = patientRepository.findAll().size();

        // Create the Patient

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientMockMvc.perform(put("/api/patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(patient)))
            .andExpect(status().isBadRequest());

        // Validate the Patient in the database
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePatient() throws Exception {
        // Initialize the database
        patientService.save(patient);

        int databaseSizeBeforeDelete = patientRepository.findAll().size();

        // Delete the patient
        restPatientMockMvc.perform(delete("/api/patients/{id}", patient.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Patient> patientList = patientRepository.findAll();
        assertThat(patientList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
