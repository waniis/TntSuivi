package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.TnTsuiviApp;
import com.mycompany.myapp.domain.Centre;
import com.mycompany.myapp.domain.AdminDeCentre;
import com.mycompany.myapp.domain.Medecin;
import com.mycompany.myapp.domain.Patient;
import com.mycompany.myapp.domain.Region;
import com.mycompany.myapp.repository.CentreRepository;
import com.mycompany.myapp.service.CentreService;
import com.mycompany.myapp.service.dto.CentreCriteria;
import com.mycompany.myapp.service.CentreQueryService;

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
 * Integration tests for the {@link CentreResource} REST controller.
 */
@SpringBootTest(classes = TnTsuiviApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class CentreResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_FAX = "AAAAAAAAAA";
    private static final String UPDATED_FAX = "BBBBBBBBBB";

    private static final String DEFAULT_EMERGENCY = "AAAAAAAAAA";
    private static final String UPDATED_EMERGENCY = "BBBBBBBBBB";

    @Autowired
    private CentreRepository centreRepository;

    @Autowired
    private CentreService centreService;

    @Autowired
    private CentreQueryService centreQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCentreMockMvc;

    private Centre centre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Centre createEntity(EntityManager em) {
        Centre centre = new Centre()
            .name(DEFAULT_NAME)
            .address(DEFAULT_ADDRESS)
            .phone(DEFAULT_PHONE)
            .fax(DEFAULT_FAX)
            .emergency(DEFAULT_EMERGENCY);
        // Add required entity
        Region region;
        if (TestUtil.findAll(em, Region.class).isEmpty()) {
            region = RegionResourceIT.createEntity(em);
            em.persist(region);
            em.flush();
        } else {
            region = TestUtil.findAll(em, Region.class).get(0);
        }
        centre.setRegion(region);
        return centre;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Centre createUpdatedEntity(EntityManager em) {
        Centre centre = new Centre()
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .phone(UPDATED_PHONE)
            .fax(UPDATED_FAX)
            .emergency(UPDATED_EMERGENCY);
        // Add required entity
        Region region;
        if (TestUtil.findAll(em, Region.class).isEmpty()) {
            region = RegionResourceIT.createUpdatedEntity(em);
            em.persist(region);
            em.flush();
        } else {
            region = TestUtil.findAll(em, Region.class).get(0);
        }
        centre.setRegion(region);
        return centre;
    }

    @BeforeEach
    public void initTest() {
        centre = createEntity(em);
    }

    @Test
    @Transactional
    public void createCentre() throws Exception {
        int databaseSizeBeforeCreate = centreRepository.findAll().size();

        // Create the Centre
        restCentreMockMvc.perform(post("/api/centres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(centre)))
            .andExpect(status().isCreated());

        // Validate the Centre in the database
        List<Centre> centreList = centreRepository.findAll();
        assertThat(centreList).hasSize(databaseSizeBeforeCreate + 1);
        Centre testCentre = centreList.get(centreList.size() - 1);
        assertThat(testCentre.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCentre.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testCentre.getPhone()).isEqualTo(DEFAULT_PHONE);
        assertThat(testCentre.getFax()).isEqualTo(DEFAULT_FAX);
        assertThat(testCentre.getEmergency()).isEqualTo(DEFAULT_EMERGENCY);
    }

    @Test
    @Transactional
    public void createCentreWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = centreRepository.findAll().size();

        // Create the Centre with an existing ID
        centre.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restCentreMockMvc.perform(post("/api/centres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(centre)))
            .andExpect(status().isBadRequest());

        // Validate the Centre in the database
        List<Centre> centreList = centreRepository.findAll();
        assertThat(centreList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = centreRepository.findAll().size();
        // set the field null
        centre.setName(null);

        // Create the Centre, which fails.

        restCentreMockMvc.perform(post("/api/centres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(centre)))
            .andExpect(status().isBadRequest());

        List<Centre> centreList = centreRepository.findAll();
        assertThat(centreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = centreRepository.findAll().size();
        // set the field null
        centre.setAddress(null);

        // Create the Centre, which fails.

        restCentreMockMvc.perform(post("/api/centres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(centre)))
            .andExpect(status().isBadRequest());

        List<Centre> centreList = centreRepository.findAll();
        assertThat(centreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = centreRepository.findAll().size();
        // set the field null
        centre.setPhone(null);

        // Create the Centre, which fails.

        restCentreMockMvc.perform(post("/api/centres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(centre)))
            .andExpect(status().isBadRequest());

        List<Centre> centreList = centreRepository.findAll();
        assertThat(centreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkFaxIsRequired() throws Exception {
        int databaseSizeBeforeTest = centreRepository.findAll().size();
        // set the field null
        centre.setFax(null);

        // Create the Centre, which fails.

        restCentreMockMvc.perform(post("/api/centres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(centre)))
            .andExpect(status().isBadRequest());

        List<Centre> centreList = centreRepository.findAll();
        assertThat(centreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllCentres() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        // Get all the centreList
        restCentreMockMvc.perform(get("/api/centres?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(centre.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].fax").value(hasItem(DEFAULT_FAX)))
            .andExpect(jsonPath("$.[*].emergency").value(hasItem(DEFAULT_EMERGENCY)));
    }
    
    @Test
    @Transactional
    public void getCentre() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        // Get the centre
        restCentreMockMvc.perform(get("/api/centres/{id}", centre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(centre.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.fax").value(DEFAULT_FAX))
            .andExpect(jsonPath("$.emergency").value(DEFAULT_EMERGENCY));
    }


    @Test
    @Transactional
    public void getCentresByIdFiltering() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        Long id = centre.getId();

        defaultCentreShouldBeFound("id.equals=" + id);
        defaultCentreShouldNotBeFound("id.notEquals=" + id);

        defaultCentreShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultCentreShouldNotBeFound("id.greaterThan=" + id);

        defaultCentreShouldBeFound("id.lessThanOrEqual=" + id);
        defaultCentreShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllCentresByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        // Get all the centreList where name equals to DEFAULT_NAME
        defaultCentreShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the centreList where name equals to UPDATED_NAME
        defaultCentreShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCentresByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        // Get all the centreList where name not equals to DEFAULT_NAME
        defaultCentreShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the centreList where name not equals to UPDATED_NAME
        defaultCentreShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCentresByNameIsInShouldWork() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        // Get all the centreList where name in DEFAULT_NAME or UPDATED_NAME
        defaultCentreShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the centreList where name equals to UPDATED_NAME
        defaultCentreShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCentresByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        // Get all the centreList where name is not null
        defaultCentreShouldBeFound("name.specified=true");

        // Get all the centreList where name is null
        defaultCentreShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllCentresByNameContainsSomething() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        // Get all the centreList where name contains DEFAULT_NAME
        defaultCentreShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the centreList where name contains UPDATED_NAME
        defaultCentreShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllCentresByNameNotContainsSomething() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        // Get all the centreList where name does not contain DEFAULT_NAME
        defaultCentreShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the centreList where name does not contain UPDATED_NAME
        defaultCentreShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllCentresByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        // Get all the centreList where address equals to DEFAULT_ADDRESS
        defaultCentreShouldBeFound("address.equals=" + DEFAULT_ADDRESS);

        // Get all the centreList where address equals to UPDATED_ADDRESS
        defaultCentreShouldNotBeFound("address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllCentresByAddressIsNotEqualToSomething() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        // Get all the centreList where address not equals to DEFAULT_ADDRESS
        defaultCentreShouldNotBeFound("address.notEquals=" + DEFAULT_ADDRESS);

        // Get all the centreList where address not equals to UPDATED_ADDRESS
        defaultCentreShouldBeFound("address.notEquals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllCentresByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        // Get all the centreList where address in DEFAULT_ADDRESS or UPDATED_ADDRESS
        defaultCentreShouldBeFound("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS);

        // Get all the centreList where address equals to UPDATED_ADDRESS
        defaultCentreShouldNotBeFound("address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllCentresByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        // Get all the centreList where address is not null
        defaultCentreShouldBeFound("address.specified=true");

        // Get all the centreList where address is null
        defaultCentreShouldNotBeFound("address.specified=false");
    }
                @Test
    @Transactional
    public void getAllCentresByAddressContainsSomething() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        // Get all the centreList where address contains DEFAULT_ADDRESS
        defaultCentreShouldBeFound("address.contains=" + DEFAULT_ADDRESS);

        // Get all the centreList where address contains UPDATED_ADDRESS
        defaultCentreShouldNotBeFound("address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    public void getAllCentresByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        // Get all the centreList where address does not contain DEFAULT_ADDRESS
        defaultCentreShouldNotBeFound("address.doesNotContain=" + DEFAULT_ADDRESS);

        // Get all the centreList where address does not contain UPDATED_ADDRESS
        defaultCentreShouldBeFound("address.doesNotContain=" + UPDATED_ADDRESS);
    }


    @Test
    @Transactional
    public void getAllCentresByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        // Get all the centreList where phone equals to DEFAULT_PHONE
        defaultCentreShouldBeFound("phone.equals=" + DEFAULT_PHONE);

        // Get all the centreList where phone equals to UPDATED_PHONE
        defaultCentreShouldNotBeFound("phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllCentresByPhoneIsNotEqualToSomething() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        // Get all the centreList where phone not equals to DEFAULT_PHONE
        defaultCentreShouldNotBeFound("phone.notEquals=" + DEFAULT_PHONE);

        // Get all the centreList where phone not equals to UPDATED_PHONE
        defaultCentreShouldBeFound("phone.notEquals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllCentresByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        // Get all the centreList where phone in DEFAULT_PHONE or UPDATED_PHONE
        defaultCentreShouldBeFound("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE);

        // Get all the centreList where phone equals to UPDATED_PHONE
        defaultCentreShouldNotBeFound("phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllCentresByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        // Get all the centreList where phone is not null
        defaultCentreShouldBeFound("phone.specified=true");

        // Get all the centreList where phone is null
        defaultCentreShouldNotBeFound("phone.specified=false");
    }
                @Test
    @Transactional
    public void getAllCentresByPhoneContainsSomething() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        // Get all the centreList where phone contains DEFAULT_PHONE
        defaultCentreShouldBeFound("phone.contains=" + DEFAULT_PHONE);

        // Get all the centreList where phone contains UPDATED_PHONE
        defaultCentreShouldNotBeFound("phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void getAllCentresByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        // Get all the centreList where phone does not contain DEFAULT_PHONE
        defaultCentreShouldNotBeFound("phone.doesNotContain=" + DEFAULT_PHONE);

        // Get all the centreList where phone does not contain UPDATED_PHONE
        defaultCentreShouldBeFound("phone.doesNotContain=" + UPDATED_PHONE);
    }


    @Test
    @Transactional
    public void getAllCentresByFaxIsEqualToSomething() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        // Get all the centreList where fax equals to DEFAULT_FAX
        defaultCentreShouldBeFound("fax.equals=" + DEFAULT_FAX);

        // Get all the centreList where fax equals to UPDATED_FAX
        defaultCentreShouldNotBeFound("fax.equals=" + UPDATED_FAX);
    }

    @Test
    @Transactional
    public void getAllCentresByFaxIsNotEqualToSomething() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        // Get all the centreList where fax not equals to DEFAULT_FAX
        defaultCentreShouldNotBeFound("fax.notEquals=" + DEFAULT_FAX);

        // Get all the centreList where fax not equals to UPDATED_FAX
        defaultCentreShouldBeFound("fax.notEquals=" + UPDATED_FAX);
    }

    @Test
    @Transactional
    public void getAllCentresByFaxIsInShouldWork() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        // Get all the centreList where fax in DEFAULT_FAX or UPDATED_FAX
        defaultCentreShouldBeFound("fax.in=" + DEFAULT_FAX + "," + UPDATED_FAX);

        // Get all the centreList where fax equals to UPDATED_FAX
        defaultCentreShouldNotBeFound("fax.in=" + UPDATED_FAX);
    }

    @Test
    @Transactional
    public void getAllCentresByFaxIsNullOrNotNull() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        // Get all the centreList where fax is not null
        defaultCentreShouldBeFound("fax.specified=true");

        // Get all the centreList where fax is null
        defaultCentreShouldNotBeFound("fax.specified=false");
    }
                @Test
    @Transactional
    public void getAllCentresByFaxContainsSomething() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        // Get all the centreList where fax contains DEFAULT_FAX
        defaultCentreShouldBeFound("fax.contains=" + DEFAULT_FAX);

        // Get all the centreList where fax contains UPDATED_FAX
        defaultCentreShouldNotBeFound("fax.contains=" + UPDATED_FAX);
    }

    @Test
    @Transactional
    public void getAllCentresByFaxNotContainsSomething() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        // Get all the centreList where fax does not contain DEFAULT_FAX
        defaultCentreShouldNotBeFound("fax.doesNotContain=" + DEFAULT_FAX);

        // Get all the centreList where fax does not contain UPDATED_FAX
        defaultCentreShouldBeFound("fax.doesNotContain=" + UPDATED_FAX);
    }


    @Test
    @Transactional
    public void getAllCentresByEmergencyIsEqualToSomething() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        // Get all the centreList where emergency equals to DEFAULT_EMERGENCY
        defaultCentreShouldBeFound("emergency.equals=" + DEFAULT_EMERGENCY);

        // Get all the centreList where emergency equals to UPDATED_EMERGENCY
        defaultCentreShouldNotBeFound("emergency.equals=" + UPDATED_EMERGENCY);
    }

    @Test
    @Transactional
    public void getAllCentresByEmergencyIsNotEqualToSomething() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        // Get all the centreList where emergency not equals to DEFAULT_EMERGENCY
        defaultCentreShouldNotBeFound("emergency.notEquals=" + DEFAULT_EMERGENCY);

        // Get all the centreList where emergency not equals to UPDATED_EMERGENCY
        defaultCentreShouldBeFound("emergency.notEquals=" + UPDATED_EMERGENCY);
    }

    @Test
    @Transactional
    public void getAllCentresByEmergencyIsInShouldWork() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        // Get all the centreList where emergency in DEFAULT_EMERGENCY or UPDATED_EMERGENCY
        defaultCentreShouldBeFound("emergency.in=" + DEFAULT_EMERGENCY + "," + UPDATED_EMERGENCY);

        // Get all the centreList where emergency equals to UPDATED_EMERGENCY
        defaultCentreShouldNotBeFound("emergency.in=" + UPDATED_EMERGENCY);
    }

    @Test
    @Transactional
    public void getAllCentresByEmergencyIsNullOrNotNull() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        // Get all the centreList where emergency is not null
        defaultCentreShouldBeFound("emergency.specified=true");

        // Get all the centreList where emergency is null
        defaultCentreShouldNotBeFound("emergency.specified=false");
    }
                @Test
    @Transactional
    public void getAllCentresByEmergencyContainsSomething() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        // Get all the centreList where emergency contains DEFAULT_EMERGENCY
        defaultCentreShouldBeFound("emergency.contains=" + DEFAULT_EMERGENCY);

        // Get all the centreList where emergency contains UPDATED_EMERGENCY
        defaultCentreShouldNotBeFound("emergency.contains=" + UPDATED_EMERGENCY);
    }

    @Test
    @Transactional
    public void getAllCentresByEmergencyNotContainsSomething() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);

        // Get all the centreList where emergency does not contain DEFAULT_EMERGENCY
        defaultCentreShouldNotBeFound("emergency.doesNotContain=" + DEFAULT_EMERGENCY);

        // Get all the centreList where emergency does not contain UPDATED_EMERGENCY
        defaultCentreShouldBeFound("emergency.doesNotContain=" + UPDATED_EMERGENCY);
    }


    @Test
    @Transactional
    public void getAllCentresByAdminDeCentreIsEqualToSomething() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);
        AdminDeCentre adminDeCentre = AdminDeCentreResourceIT.createEntity(em);
        em.persist(adminDeCentre);
        em.flush();
        centre.addAdminDeCentre(adminDeCentre);
        centreRepository.saveAndFlush(centre);
        Long adminDeCentreId = adminDeCentre.getId();

        // Get all the centreList where adminDeCentre equals to adminDeCentreId
        defaultCentreShouldBeFound("adminDeCentreId.equals=" + adminDeCentreId);

        // Get all the centreList where adminDeCentre equals to adminDeCentreId + 1
        defaultCentreShouldNotBeFound("adminDeCentreId.equals=" + (adminDeCentreId + 1));
    }


    @Test
    @Transactional
    public void getAllCentresByMedecinIsEqualToSomething() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);
        Medecin medecin = MedecinResourceIT.createEntity(em);
        em.persist(medecin);
        em.flush();
        centre.addMedecin(medecin);
        centreRepository.saveAndFlush(centre);
        Long medecinId = medecin.getId();

        // Get all the centreList where medecin equals to medecinId
        defaultCentreShouldBeFound("medecinId.equals=" + medecinId);

        // Get all the centreList where medecin equals to medecinId + 1
        defaultCentreShouldNotBeFound("medecinId.equals=" + (medecinId + 1));
    }


    @Test
    @Transactional
    public void getAllCentresByPatientIsEqualToSomething() throws Exception {
        // Initialize the database
        centreRepository.saveAndFlush(centre);
        Patient patient = PatientResourceIT.createEntity(em);
        em.persist(patient);
        em.flush();
        centre.addPatient(patient);
        centreRepository.saveAndFlush(centre);
        Long patientId = patient.getId();

        // Get all the centreList where patient equals to patientId
        defaultCentreShouldBeFound("patientId.equals=" + patientId);

        // Get all the centreList where patient equals to patientId + 1
        defaultCentreShouldNotBeFound("patientId.equals=" + (patientId + 1));
    }


    @Test
    @Transactional
    public void getAllCentresByRegionIsEqualToSomething() throws Exception {
        // Get already existing entity
        Region region = centre.getRegion();
        centreRepository.saveAndFlush(centre);
        Long regionId = region.getId();

        // Get all the centreList where region equals to regionId
        defaultCentreShouldBeFound("regionId.equals=" + regionId);

        // Get all the centreList where region equals to regionId + 1
        defaultCentreShouldNotBeFound("regionId.equals=" + (regionId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultCentreShouldBeFound(String filter) throws Exception {
        restCentreMockMvc.perform(get("/api/centres?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(centre.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].fax").value(hasItem(DEFAULT_FAX)))
            .andExpect(jsonPath("$.[*].emergency").value(hasItem(DEFAULT_EMERGENCY)));

        // Check, that the count call also returns 1
        restCentreMockMvc.perform(get("/api/centres/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultCentreShouldNotBeFound(String filter) throws Exception {
        restCentreMockMvc.perform(get("/api/centres?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restCentreMockMvc.perform(get("/api/centres/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingCentre() throws Exception {
        // Get the centre
        restCentreMockMvc.perform(get("/api/centres/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateCentre() throws Exception {
        // Initialize the database
        centreService.save(centre);

        int databaseSizeBeforeUpdate = centreRepository.findAll().size();

        // Update the centre
        Centre updatedCentre = centreRepository.findById(centre.getId()).get();
        // Disconnect from session so that the updates on updatedCentre are not directly saved in db
        em.detach(updatedCentre);
        updatedCentre
            .name(UPDATED_NAME)
            .address(UPDATED_ADDRESS)
            .phone(UPDATED_PHONE)
            .fax(UPDATED_FAX)
            .emergency(UPDATED_EMERGENCY);

        restCentreMockMvc.perform(put("/api/centres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCentre)))
            .andExpect(status().isOk());

        // Validate the Centre in the database
        List<Centre> centreList = centreRepository.findAll();
        assertThat(centreList).hasSize(databaseSizeBeforeUpdate);
        Centre testCentre = centreList.get(centreList.size() - 1);
        assertThat(testCentre.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCentre.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testCentre.getPhone()).isEqualTo(UPDATED_PHONE);
        assertThat(testCentre.getFax()).isEqualTo(UPDATED_FAX);
        assertThat(testCentre.getEmergency()).isEqualTo(UPDATED_EMERGENCY);
    }

    @Test
    @Transactional
    public void updateNonExistingCentre() throws Exception {
        int databaseSizeBeforeUpdate = centreRepository.findAll().size();

        // Create the Centre

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCentreMockMvc.perform(put("/api/centres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(centre)))
            .andExpect(status().isBadRequest());

        // Validate the Centre in the database
        List<Centre> centreList = centreRepository.findAll();
        assertThat(centreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCentre() throws Exception {
        // Initialize the database
        centreService.save(centre);

        int databaseSizeBeforeDelete = centreRepository.findAll().size();

        // Delete the centre
        restCentreMockMvc.perform(delete("/api/centres/{id}", centre.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Centre> centreList = centreRepository.findAll();
        assertThat(centreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
