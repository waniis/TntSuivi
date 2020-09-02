package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.TnTsuiviApp;
import com.mycompany.myapp.domain.Medecin;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.domain.Question;
import com.mycompany.myapp.domain.Patient;
import com.mycompany.myapp.domain.Questionnaire;
import com.mycompany.myapp.domain.GroupeDePatient;
import com.mycompany.myapp.domain.Centre;
import com.mycompany.myapp.domain.Specialty;
import com.mycompany.myapp.repository.MedecinRepository;
import com.mycompany.myapp.service.MedecinService;
import com.mycompany.myapp.service.dto.MedecinCriteria;
import com.mycompany.myapp.service.MedecinQueryService;

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

import com.mycompany.myapp.domain.enumeration.Sexe;
/**
 * Integration tests for the {@link MedecinResource} REST controller.
 */
@SpringBootTest(classes = TnTsuiviApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class MedecinResourceIT {

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE_2 = "AAAAAAAAAA";
    private static final String UPDATED_PHONE_2 = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADRESS = "BBBBBBBBBB";

    private static final Sexe DEFAULT_SEXE = Sexe.Feminin;
    private static final Sexe UPDATED_SEXE = Sexe.Masculin;

    @Autowired
    private MedecinRepository medecinRepository;

    @Autowired
    private MedecinService medecinService;

    @Autowired
    private MedecinQueryService medecinQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedecinMockMvc;

    private Medecin medecin;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medecin createEntity(EntityManager em) {
        Medecin medecin = new Medecin()
            .fullName(DEFAULT_FULL_NAME)
            .phone(DEFAULT_PHONE)
            .phone2(DEFAULT_PHONE_2)
            .adress(DEFAULT_ADRESS)
            .sexe(DEFAULT_SEXE);
        // Add required entity
        Centre centre;
        if (TestUtil.findAll(em, Centre.class).isEmpty()) {
            centre = CentreResourceIT.createEntity(em);
            em.persist(centre);
            em.flush();
        } else {
            centre = TestUtil.findAll(em, Centre.class).get(0);
        }
        medecin.setCentre(centre);
        // Add required entity
        Specialty specialty;
        if (TestUtil.findAll(em, Specialty.class).isEmpty()) {
            specialty = SpecialtyResourceIT.createEntity(em);
            em.persist(specialty);
            em.flush();
        } else {
            specialty = TestUtil.findAll(em, Specialty.class).get(0);
        }
        medecin.setSpecialty(specialty);
        return medecin;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medecin createUpdatedEntity(EntityManager em) {
        Medecin medecin = new Medecin()
            .fullName(UPDATED_FULL_NAME)
            .phone(UPDATED_PHONE)
            .phone2(UPDATED_PHONE_2)
            .adress(UPDATED_ADRESS)
            .sexe(UPDATED_SEXE);
        // Add required entity
        Centre centre;
        if (TestUtil.findAll(em, Centre.class).isEmpty()) {
            centre = CentreResourceIT.createUpdatedEntity(em);
            em.persist(centre);
            em.flush();
        } else {
            centre = TestUtil.findAll(em, Centre.class).get(0);
        }
        medecin.setCentre(centre);
        // Add required entity
        Specialty specialty;
        if (TestUtil.findAll(em, Specialty.class).isEmpty()) {
            specialty = SpecialtyResourceIT.createUpdatedEntity(em);
            em.persist(specialty);
            em.flush();
        } else {
            specialty = TestUtil.findAll(em, Specialty.class).get(0);
        }
        medecin.setSpecialty(specialty);
        return medecin;
    }

    @BeforeEach
    public void initTest() {
        medecin = createEntity(em);
    }

    @Test
    @Transactional
    public void createMedecin() throws Exception {
        int databaseSizeBeforeCreate = medecinRepository.findAll().size();

        // Create the Medecin
        restMedecinMockMvc.perform(post("/api/medecins")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(medecin)))
            .andExpect(status().isCreated());

        // Validate the Medecin in the database
        List<Medecin> medecinList = medecinRepository.findAll();
        assertThat(medecinList).hasSize(databaseSizeBeforeCreate + 1);
        Medecin testMedecin = medecinList.get(medecinList.size() - 1);
        assertThat(testMedecin.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testMedecin.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testMedecin.getPhone2()).isEqualTo(DEFAULT_PHONE_2);
        assertThat(testMedecin.getAdress()).isEqualTo(DEFAULT_ADRESS);
        assertThat(testMedecin.getSexe()).isEqualTo(DEFAULT_SEXE);
    }

    @Test
    @Transactional
    public void createMedecinWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = medecinRepository.findAll().size();

        // Create the Medecin with an existing ID
        medecin.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedecinMockMvc.perform(post("/api/medecins")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(medecin)))
            .andExpect(status().isBadRequest());

        // Validate the Medecin in the database
        List<Medecin> medecinList = medecinRepository.findAll();
        assertThat(medecinList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = medecinRepository.findAll().size();
        // set the field null
        medecin.setPhone(null);

        // Create the Medecin, which fails.

        restMedecinMockMvc.perform(post("/api/medecins")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(medecin)))
            .andExpect(status().isBadRequest());

        List<Medecin> medecinList = medecinRepository.findAll();
        assertThat(medecinList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAdressIsRequired() throws Exception {
        int databaseSizeBeforeTest = medecinRepository.findAll().size();
        // set the field null
        medecin.setAdress(null);

        // Create the Medecin, which fails.

        restMedecinMockMvc.perform(post("/api/medecins")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(medecin)))
            .andExpect(status().isBadRequest());

        List<Medecin> medecinList = medecinRepository.findAll();
        assertThat(medecinList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkSexeIsRequired() throws Exception {
        int databaseSizeBeforeTest = medecinRepository.findAll().size();
        // set the field null
        medecin.setSexe(null);

        // Create the Medecin, which fails.

        restMedecinMockMvc.perform(post("/api/medecins")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(medecin)))
            .andExpect(status().isBadRequest());

        List<Medecin> medecinList = medecinRepository.findAll();
        assertThat(medecinList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMedecins() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList
        restMedecinMockMvc.perform(get("/api/medecins?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medecin.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].phone2").value(hasItem(DEFAULT_PHONE_2)))
            .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())));
    }
    
    @Test
    @Transactional
    public void getMedecin() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get the medecin
        restMedecinMockMvc.perform(get("/api/medecins/{id}", medecin.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medecin.getId().intValue()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.phone2").value(DEFAULT_PHONE_2))
            .andExpect(jsonPath("$.adress").value(DEFAULT_ADRESS))
            .andExpect(jsonPath("$.sexe").value(DEFAULT_SEXE.toString()));
    }


    @Test
    @Transactional
    public void getMedecinsByIdFiltering() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        Long id = medecin.getId();

        defaultMedecinShouldBeFound("id.equals=" + id);
        defaultMedecinShouldNotBeFound("id.notEquals=" + id);

        defaultMedecinShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMedecinShouldNotBeFound("id.greaterThan=" + id);

        defaultMedecinShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMedecinShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllMedecinsByFullNameIsEqualToSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where fullName equals to DEFAULT_FULL_NAME
        defaultMedecinShouldBeFound("fullName.equals=" + DEFAULT_FULL_NAME);

        // Get all the medecinList where fullName equals to UPDATED_FULL_NAME
        defaultMedecinShouldNotBeFound("fullName.equals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllMedecinsByFullNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where fullName not equals to DEFAULT_FULL_NAME
        defaultMedecinShouldNotBeFound("fullName.notEquals=" + DEFAULT_FULL_NAME);

        // Get all the medecinList where fullName not equals to UPDATED_FULL_NAME
        defaultMedecinShouldBeFound("fullName.notEquals=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllMedecinsByFullNameIsInShouldWork() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where fullName in DEFAULT_FULL_NAME or UPDATED_FULL_NAME
        defaultMedecinShouldBeFound("fullName.in=" + DEFAULT_FULL_NAME + "," + UPDATED_FULL_NAME);

        // Get all the medecinList where fullName equals to UPDATED_FULL_NAME
        defaultMedecinShouldNotBeFound("fullName.in=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllMedecinsByFullNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where fullName is not null
        defaultMedecinShouldBeFound("fullName.specified=true");

        // Get all the medecinList where fullName is null
        defaultMedecinShouldNotBeFound("fullName.specified=false");
    }
                @Test
    @Transactional
    public void getAllMedecinsByFullNameContainsSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where fullName contains DEFAULT_FULL_NAME
        defaultMedecinShouldBeFound("fullName.contains=" + DEFAULT_FULL_NAME);

        // Get all the medecinList where fullName contains UPDATED_FULL_NAME
        defaultMedecinShouldNotBeFound("fullName.contains=" + UPDATED_FULL_NAME);
    }

    @Test
    @Transactional
    public void getAllMedecinsByFullNameNotContainsSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where fullName does not contain DEFAULT_FULL_NAME
        defaultMedecinShouldNotBeFound("fullName.doesNotContain=" + DEFAULT_FULL_NAME);

        // Get all the medecinList where fullName does not contain UPDATED_FULL_NAME
        defaultMedecinShouldBeFound("fullName.doesNotContain=" + UPDATED_FULL_NAME);
    }


    @Test
    @Transactional
    public void getAllMedecinsByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where phone equals to DEFAULT_PHONE
        defaultMedecinShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the medecinList where phone equals to UPDATED_PHONE
        defaultMedecinShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllMedecinsByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where phone not equals to DEFAULT_PHONE
        defaultMedecinShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the medecinList where phone not equals to UPDATED_PHONE
        defaultMedecinShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllMedecinsByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultMedecinShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the medecinList where phone equals to UPDATED_PHONE
        defaultMedecinShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllMedecinsByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where phone is not null
        defaultMedecinShouldBeFound("phone.specified=true");

        // Get all the medecinList where phone is null
        defaultMedecinShouldNotBeFound("phone.specified=false");
    }
                @Test
    @Transactional
    public void getAllMedecinsByPhoneContainsSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where phone contains DEFAULT_PHONE
        defaultMedecinShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the medecinList where phone contains UPDATED_PHONE
        defaultMedecinShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllMedecinsByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where phone does not contain DEFAULT_PHONE
        defaultMedecinShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the medecinList where phone does not contain UPDATED_PHONE
        defaultMedecinShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }


    @Test
    @Transactional
    public void getAllMedecinsByPhone2IsEqualToSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where phone2 equals to DEFAULT_PHONE_2
        defaultMedecinShouldBeFound("phone2.equals=" + DEFAULT_PHONE_2);

        // Get all the medecinList where phone2 equals to UPDATED_PHONE_2
        defaultMedecinShouldNotBeFound("phone2.equals=" + UPDATED_PHONE_2);
    }

    @Test
    @Transactional
    public void getAllMedecinsByPhone2IsNotEqualToSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where phone2 not equals to DEFAULT_PHONE_2
        defaultMedecinShouldNotBeFound("phone2.notEquals=" + DEFAULT_PHONE_2);

        // Get all the medecinList where phone2 not equals to UPDATED_PHONE_2
        defaultMedecinShouldBeFound("phone2.notEquals=" + UPDATED_PHONE_2);
    }

    @Test
    @Transactional
    public void getAllMedecinsByPhone2IsInShouldWork() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where phone2 in DEFAULT_PHONE_2 or UPDATED_PHONE_2
        defaultMedecinShouldBeFound("phone2.in=" + DEFAULT_PHONE_2 + "," + UPDATED_PHONE_2);

        // Get all the medecinList where phone2 equals to UPDATED_PHONE_2
        defaultMedecinShouldNotBeFound("phone2.in=" + UPDATED_PHONE_2);
    }

    @Test
    @Transactional
    public void getAllMedecinsByPhone2IsNullOrNotNull() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where phone2 is not null
        defaultMedecinShouldBeFound("phone2.specified=true");

        // Get all the medecinList where phone2 is null
        defaultMedecinShouldNotBeFound("phone2.specified=false");
    }
                @Test
    @Transactional
    public void getAllMedecinsByPhone2ContainsSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where phone2 contains DEFAULT_PHONE_2
        defaultMedecinShouldBeFound("phone2.contains=" + DEFAULT_PHONE_2);

        // Get all the medecinList where phone2 contains UPDATED_PHONE_2
        defaultMedecinShouldNotBeFound("phone2.contains=" + UPDATED_PHONE_2);
    }

    @Test
    @Transactional
    public void getAllMedecinsByPhone2NotContainsSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where phone2 does not contain DEFAULT_PHONE_2
        defaultMedecinShouldNotBeFound("phone2.doesNotContain=" + DEFAULT_PHONE_2);

        // Get all the medecinList where phone2 does not contain UPDATED_PHONE_2
        defaultMedecinShouldBeFound("phone2.doesNotContain=" + UPDATED_PHONE_2);
    }


    @Test
    @Transactional
    public void getAllMedecinsByAdressIsEqualToSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where adress equals to DEFAULT_ADRESS
        defaultMedecinShouldBeFound("adress.equals=" + DEFAULT_ADRESS);

        // Get all the medecinList where adress equals to UPDATED_ADRESS
        defaultMedecinShouldNotBeFound("adress.equals=" + UPDATED_ADRESS);
    }

    @Test
    @Transactional
    public void getAllMedecinsByAdressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where adress not equals to DEFAULT_ADRESS
        defaultMedecinShouldNotBeFound("adress.notEquals=" + DEFAULT_ADRESS);

        // Get all the medecinList where adress not equals to UPDATED_ADRESS
        defaultMedecinShouldBeFound("adress.notEquals=" + UPDATED_ADRESS);
    }

    @Test
    @Transactional
    public void getAllMedecinsByAdressIsInShouldWork() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where adress in DEFAULT_ADRESS or UPDATED_ADRESS
        defaultMedecinShouldBeFound("adress.in=" + DEFAULT_ADRESS + "," + UPDATED_ADRESS);

        // Get all the medecinList where adress equals to UPDATED_ADRESS
        defaultMedecinShouldNotBeFound("adress.in=" + UPDATED_ADRESS);
    }

    @Test
    @Transactional
    public void getAllMedecinsByAdressIsNullOrNotNull() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where adress is not null
        defaultMedecinShouldBeFound("adress.specified=true");

        // Get all the medecinList where adress is null
        defaultMedecinShouldNotBeFound("adress.specified=false");
    }
                @Test
    @Transactional
    public void getAllMedecinsByAdressContainsSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where adress contains DEFAULT_ADRESS
        defaultMedecinShouldBeFound("adress.contains=" + DEFAULT_ADRESS);

        // Get all the medecinList where adress contains UPDATED_ADRESS
        defaultMedecinShouldNotBeFound("adress.contains=" + UPDATED_ADRESS);
    }

    @Test
    @Transactional
    public void getAllMedecinsByAdressNotContainsSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where adress does not contain DEFAULT_ADRESS
        defaultMedecinShouldNotBeFound("adress.doesNotContain=" + DEFAULT_ADRESS);

        // Get all the medecinList where adress does not contain UPDATED_ADRESS
        defaultMedecinShouldBeFound("adress.doesNotContain=" + UPDATED_ADRESS);
    }


    @Test
    @Transactional
    public void getAllMedecinsBySexeIsEqualToSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where sexe equals to DEFAULT_SEXE
        defaultMedecinShouldBeFound("sexe.equals=" + DEFAULT_SEXE);

        // Get all the medecinList where sexe equals to UPDATED_SEXE
        defaultMedecinShouldNotBeFound("sexe.equals=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    public void getAllMedecinsBySexeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where sexe not equals to DEFAULT_SEXE
        defaultMedecinShouldNotBeFound("sexe.notEquals=" + DEFAULT_SEXE);

        // Get all the medecinList where sexe not equals to UPDATED_SEXE
        defaultMedecinShouldBeFound("sexe.notEquals=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    public void getAllMedecinsBySexeIsInShouldWork() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where sexe in DEFAULT_SEXE or UPDATED_SEXE
        defaultMedecinShouldBeFound("sexe.in=" + DEFAULT_SEXE + "," + UPDATED_SEXE);

        // Get all the medecinList where sexe equals to UPDATED_SEXE
        defaultMedecinShouldNotBeFound("sexe.in=" + UPDATED_SEXE);
    }

    @Test
    @Transactional
    public void getAllMedecinsBySexeIsNullOrNotNull() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);

        // Get all the medecinList where sexe is not null
        defaultMedecinShouldBeFound("sexe.specified=true");

        // Get all the medecinList where sexe is null
        defaultMedecinShouldNotBeFound("sexe.specified=false");
    }

    @Test
    @Transactional
    public void getAllMedecinsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        medecin.setUser(user);
        medecinRepository.saveAndFlush(medecin);
        Long userId = user.getId();

        // Get all the medecinList where user equals to userId
        defaultMedecinShouldBeFound("userId.equals=" + userId);

        // Get all the medecinList where user equals to userId + 1
        defaultMedecinShouldNotBeFound("userId.equals=" + (userId + 1));
    }


    @Test
    @Transactional
    public void getAllMedecinsByQuestionIsEqualToSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);
        Question question = QuestionResourceIT.createEntity(em);
        em.persist(question);
        em.flush();
        medecin.addQuestion(question);
        medecinRepository.saveAndFlush(medecin);
        Long questionId = question.getId();

        // Get all the medecinList where question equals to questionId
        defaultMedecinShouldBeFound("questionId.equals=" + questionId);

        // Get all the medecinList where question equals to questionId + 1
        defaultMedecinShouldNotBeFound("questionId.equals=" + (questionId + 1));
    }


    @Test
    @Transactional
    public void getAllMedecinsByPatientIsEqualToSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);
        Patient patient = PatientResourceIT.createEntity(em);
        em.persist(patient);
        em.flush();
        medecin.addPatient(patient);
        medecinRepository.saveAndFlush(medecin);
        Long patientId = patient.getId();

        // Get all the medecinList where patient equals to patientId
        defaultMedecinShouldBeFound("patientId.equals=" + patientId);

        // Get all the medecinList where patient equals to patientId + 1
        defaultMedecinShouldNotBeFound("patientId.equals=" + (patientId + 1));
    }


    @Test
    @Transactional
    public void getAllMedecinsByQuestionnaireIsEqualToSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);
        Questionnaire questionnaire = QuestionnaireResourceIT.createEntity(em);
        em.persist(questionnaire);
        em.flush();
        medecin.addQuestionnaire(questionnaire);
        medecinRepository.saveAndFlush(medecin);
        Long questionnaireId = questionnaire.getId();

        // Get all the medecinList where questionnaire equals to questionnaireId
        defaultMedecinShouldBeFound("questionnaireId.equals=" + questionnaireId);

        // Get all the medecinList where questionnaire equals to questionnaireId + 1
        defaultMedecinShouldNotBeFound("questionnaireId.equals=" + (questionnaireId + 1));
    }


    @Test
    @Transactional
    public void getAllMedecinsByGroupeDePatientIsEqualToSomething() throws Exception {
        // Initialize the database
        medecinRepository.saveAndFlush(medecin);
        GroupeDePatient groupeDePatient = GroupeDePatientResourceIT.createEntity(em);
        em.persist(groupeDePatient);
        em.flush();
        medecin.addGroupeDePatient(groupeDePatient);
        medecinRepository.saveAndFlush(medecin);
        Long groupeDePatientId = groupeDePatient.getId();

        // Get all the medecinList where groupeDePatient equals to groupeDePatientId
        defaultMedecinShouldBeFound("groupeDePatientId.equals=" + groupeDePatientId);

        // Get all the medecinList where groupeDePatient equals to groupeDePatientId + 1
        defaultMedecinShouldNotBeFound("groupeDePatientId.equals=" + (groupeDePatientId + 1));
    }


    @Test
    @Transactional
    public void getAllMedecinsByCentreIsEqualToSomething() throws Exception {
        // Get already existing entity
        Centre centre = medecin.getCentre();
        medecinRepository.saveAndFlush(medecin);
        Long centreId = centre.getId();

        // Get all the medecinList where centre equals to centreId
        defaultMedecinShouldBeFound("centreId.equals=" + centreId);

        // Get all the medecinList where centre equals to centreId + 1
        defaultMedecinShouldNotBeFound("centreId.equals=" + (centreId + 1));
    }


    @Test
    @Transactional
    public void getAllMedecinsBySpecialtyIsEqualToSomething() throws Exception {
        // Get already existing entity
        Specialty specialty = medecin.getSpecialty();
        medecinRepository.saveAndFlush(medecin);
        Long specialtyId = specialty.getId();

        // Get all the medecinList where specialty equals to specialtyId
        defaultMedecinShouldBeFound("specialtyId.equals=" + specialtyId);

        // Get all the medecinList where specialty equals to specialtyId + 1
        defaultMedecinShouldNotBeFound("specialtyId.equals=" + (specialtyId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMedecinShouldBeFound(String filter) throws Exception {
        restMedecinMockMvc.perform(get("/api/medecins?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medecin.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].phone2").value(hasItem(DEFAULT_PHONE_2)))
            .andExpect(jsonPath("$.[*].adress").value(hasItem(DEFAULT_ADRESS)))
            .andExpect(jsonPath("$.[*].sexe").value(hasItem(DEFAULT_SEXE.toString())));

        // Check, that the count call also returns 1
        restMedecinMockMvc.perform(get("/api/medecins/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMedecinShouldNotBeFound(String filter) throws Exception {
        restMedecinMockMvc.perform(get("/api/medecins?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMedecinMockMvc.perform(get("/api/medecins/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingMedecin() throws Exception {
        // Get the medecin
        restMedecinMockMvc.perform(get("/api/medecins/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMedecin() throws Exception {
        // Initialize the database
        medecinService.save(medecin);

        int databaseSizeBeforeUpdate = medecinRepository.findAll().size();

        // Update the medecin
        Medecin updatedMedecin = medecinRepository.findById(medecin.getId()).get();
        // Disconnect from session so that the updates on updatedMedecin are not directly saved in db
        em.detach(updatedMedecin);
        updatedMedecin
            .fullName(UPDATED_FULL_NAME)
            .phone(UPDATED_PHONE)
            .phone2(UPDATED_PHONE_2)
            .adress(UPDATED_ADRESS)
            .sexe(UPDATED_SEXE);

        restMedecinMockMvc.perform(put("/api/medecins")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedMedecin)))
            .andExpect(status().isOk());

        // Validate the Medecin in the database
        List<Medecin> medecinList = medecinRepository.findAll();
        assertThat(medecinList).hasSize(databaseSizeBeforeUpdate);
        Medecin testMedecin = medecinList.get(medecinList.size() - 1);
        assertThat(testMedecin.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testMedecin.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testMedecin.getPhone2()).isEqualTo(UPDATED_PHONE_2);
        assertThat(testMedecin.getAdress()).isEqualTo(UPDATED_ADRESS);
        assertThat(testMedecin.getSexe()).isEqualTo(UPDATED_SEXE);
    }

    @Test
    @Transactional
    public void updateNonExistingMedecin() throws Exception {
        int databaseSizeBeforeUpdate = medecinRepository.findAll().size();

        // Create the Medecin

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedecinMockMvc.perform(put("/api/medecins")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(medecin)))
            .andExpect(status().isBadRequest());

        // Validate the Medecin in the database
        List<Medecin> medecinList = medecinRepository.findAll();
        assertThat(medecinList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMedecin() throws Exception {
        // Initialize the database
        medecinService.save(medecin);

        int databaseSizeBeforeDelete = medecinRepository.findAll().size();

        // Delete the medecin
        restMedecinMockMvc.perform(delete("/api/medecins/{id}", medecin.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Medecin> medecinList = medecinRepository.findAll();
        assertThat(medecinList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
