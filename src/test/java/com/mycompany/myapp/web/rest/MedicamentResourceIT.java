package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.TnTsuiviApp;
import com.mycompany.myapp.domain.Medicament;
import com.mycompany.myapp.repository.MedicamentRepository;
import com.mycompany.myapp.service.MedicamentService;
import com.mycompany.myapp.service.dto.MedicamentCriteria;
import com.mycompany.myapp.service.MedicamentQueryService;

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

import com.mycompany.myapp.domain.enumeration.Forme;
/**
 * Integration tests for the {@link MedicamentResource} REST controller.
 */
@SpringBootTest(classes = TnTsuiviApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class MedicamentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Forme DEFAULT_FORME = Forme.Orales;
    private static final Forme UPDATED_FORME = Forme.Injectables;

    private static final String DEFAULT_DESCRPITION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRPITION = "BBBBBBBBBB";

    @Autowired
    private MedicamentRepository medicamentRepository;

    @Autowired
    private MedicamentService medicamentService;

    @Autowired
    private MedicamentQueryService medicamentQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restMedicamentMockMvc;

    private Medicament medicament;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medicament createEntity(EntityManager em) {
        Medicament medicament = new Medicament()
            .name(DEFAULT_NAME)
            .forme(DEFAULT_FORME)
            .descrpition(DEFAULT_DESCRPITION);
        return medicament;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Medicament createUpdatedEntity(EntityManager em) {
        Medicament medicament = new Medicament()
            .name(UPDATED_NAME)
            .forme(UPDATED_FORME)
            .descrpition(UPDATED_DESCRPITION);
        return medicament;
    }

    @BeforeEach
    public void initTest() {
        medicament = createEntity(em);
    }

    @Test
    @Transactional
    public void createMedicament() throws Exception {
        int databaseSizeBeforeCreate = medicamentRepository.findAll().size();

        // Create the Medicament
        restMedicamentMockMvc.perform(post("/api/medicaments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(medicament)))
            .andExpect(status().isCreated());

        // Validate the Medicament in the database
        List<Medicament> medicamentList = medicamentRepository.findAll();
        assertThat(medicamentList).hasSize(databaseSizeBeforeCreate + 1);
        Medicament testMedicament = medicamentList.get(medicamentList.size() - 1);
        assertThat(testMedicament.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testMedicament.getForme()).isEqualTo(DEFAULT_FORME);
        assertThat(testMedicament.getDescrpition()).isEqualTo(DEFAULT_DESCRPITION);
    }

    @Test
    @Transactional
    public void createMedicamentWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = medicamentRepository.findAll().size();

        // Create the Medicament with an existing ID
        medicament.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restMedicamentMockMvc.perform(post("/api/medicaments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(medicament)))
            .andExpect(status().isBadRequest());

        // Validate the Medicament in the database
        List<Medicament> medicamentList = medicamentRepository.findAll();
        assertThat(medicamentList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFormeIsRequired() throws Exception {
        int databaseSizeBeforeTest = medicamentRepository.findAll().size();
        // set the field null
        medicament.setForme(null);

        // Create the Medicament, which fails.

        restMedicamentMockMvc.perform(post("/api/medicaments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(medicament)))
            .andExpect(status().isBadRequest());

        List<Medicament> medicamentList = medicamentRepository.findAll();
        assertThat(medicamentList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllMedicaments() throws Exception {
        // Initialize the database
        medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList
        restMedicamentMockMvc.perform(get("/api/medicaments?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicament.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].forme").value(hasItem(DEFAULT_FORME.toString())))
            .andExpect(jsonPath("$.[*].descrpition").value(hasItem(DEFAULT_DESCRPITION)));
    }
    
    @Test
    @Transactional
    public void getMedicament() throws Exception {
        // Initialize the database
        medicamentRepository.saveAndFlush(medicament);

        // Get the medicament
        restMedicamentMockMvc.perform(get("/api/medicaments/{id}", medicament.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(medicament.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.forme").value(DEFAULT_FORME.toString()))
            .andExpect(jsonPath("$.descrpition").value(DEFAULT_DESCRPITION));
    }


    @Test
    @Transactional
    public void getMedicamentsByIdFiltering() throws Exception {
        // Initialize the database
        medicamentRepository.saveAndFlush(medicament);

        Long id = medicament.getId();

        defaultMedicamentShouldBeFound("id.equals=" + id);
        defaultMedicamentShouldNotBeFound("id.notEquals=" + id);

        defaultMedicamentShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultMedicamentShouldNotBeFound("id.greaterThan=" + id);

        defaultMedicamentShouldBeFound("id.lessThanOrEqual=" + id);
        defaultMedicamentShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllMedicamentsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where name equals to DEFAULT_NAME
        defaultMedicamentShouldBeFound("name.equals=" + DEFAULT_NAME);

        // Get all the medicamentList where name equals to UPDATED_NAME
        defaultMedicamentShouldNotBeFound("name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMedicamentsByNameIsNotEqualToSomething() throws Exception {
        // Initialize the database
        medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where name not equals to DEFAULT_NAME
        defaultMedicamentShouldNotBeFound("name.notEquals=" + DEFAULT_NAME);

        // Get all the medicamentList where name not equals to UPDATED_NAME
        defaultMedicamentShouldBeFound("name.notEquals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMedicamentsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where name in DEFAULT_NAME or UPDATED_NAME
        defaultMedicamentShouldBeFound("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME);

        // Get all the medicamentList where name equals to UPDATED_NAME
        defaultMedicamentShouldNotBeFound("name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMedicamentsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where name is not null
        defaultMedicamentShouldBeFound("name.specified=true");

        // Get all the medicamentList where name is null
        defaultMedicamentShouldNotBeFound("name.specified=false");
    }
                @Test
    @Transactional
    public void getAllMedicamentsByNameContainsSomething() throws Exception {
        // Initialize the database
        medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where name contains DEFAULT_NAME
        defaultMedicamentShouldBeFound("name.contains=" + DEFAULT_NAME);

        // Get all the medicamentList where name contains UPDATED_NAME
        defaultMedicamentShouldNotBeFound("name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    public void getAllMedicamentsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where name does not contain DEFAULT_NAME
        defaultMedicamentShouldNotBeFound("name.doesNotContain=" + DEFAULT_NAME);

        // Get all the medicamentList where name does not contain UPDATED_NAME
        defaultMedicamentShouldBeFound("name.doesNotContain=" + UPDATED_NAME);
    }


    @Test
    @Transactional
    public void getAllMedicamentsByFormeIsEqualToSomething() throws Exception {
        // Initialize the database
        medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where forme equals to DEFAULT_FORME
        defaultMedicamentShouldBeFound("forme.equals=" + DEFAULT_FORME);

        // Get all the medicamentList where forme equals to UPDATED_FORME
        defaultMedicamentShouldNotBeFound("forme.equals=" + UPDATED_FORME);
    }

    @Test
    @Transactional
    public void getAllMedicamentsByFormeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where forme not equals to DEFAULT_FORME
        defaultMedicamentShouldNotBeFound("forme.notEquals=" + DEFAULT_FORME);

        // Get all the medicamentList where forme not equals to UPDATED_FORME
        defaultMedicamentShouldBeFound("forme.notEquals=" + UPDATED_FORME);
    }

    @Test
    @Transactional
    public void getAllMedicamentsByFormeIsInShouldWork() throws Exception {
        // Initialize the database
        medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where forme in DEFAULT_FORME or UPDATED_FORME
        defaultMedicamentShouldBeFound("forme.in=" + DEFAULT_FORME + "," + UPDATED_FORME);

        // Get all the medicamentList where forme equals to UPDATED_FORME
        defaultMedicamentShouldNotBeFound("forme.in=" + UPDATED_FORME);
    }

    @Test
    @Transactional
    public void getAllMedicamentsByFormeIsNullOrNotNull() throws Exception {
        // Initialize the database
        medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where forme is not null
        defaultMedicamentShouldBeFound("forme.specified=true");

        // Get all the medicamentList where forme is null
        defaultMedicamentShouldNotBeFound("forme.specified=false");
    }

    @Test
    @Transactional
    public void getAllMedicamentsByDescrpitionIsEqualToSomething() throws Exception {
        // Initialize the database
        medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where descrpition equals to DEFAULT_DESCRPITION
        defaultMedicamentShouldBeFound("descrpition.equals=" + DEFAULT_DESCRPITION);

        // Get all the medicamentList where descrpition equals to UPDATED_DESCRPITION
        defaultMedicamentShouldNotBeFound("descrpition.equals=" + UPDATED_DESCRPITION);
    }

    @Test
    @Transactional
    public void getAllMedicamentsByDescrpitionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where descrpition not equals to DEFAULT_DESCRPITION
        defaultMedicamentShouldNotBeFound("descrpition.notEquals=" + DEFAULT_DESCRPITION);

        // Get all the medicamentList where descrpition not equals to UPDATED_DESCRPITION
        defaultMedicamentShouldBeFound("descrpition.notEquals=" + UPDATED_DESCRPITION);
    }

    @Test
    @Transactional
    public void getAllMedicamentsByDescrpitionIsInShouldWork() throws Exception {
        // Initialize the database
        medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where descrpition in DEFAULT_DESCRPITION or UPDATED_DESCRPITION
        defaultMedicamentShouldBeFound("descrpition.in=" + DEFAULT_DESCRPITION + "," + UPDATED_DESCRPITION);

        // Get all the medicamentList where descrpition equals to UPDATED_DESCRPITION
        defaultMedicamentShouldNotBeFound("descrpition.in=" + UPDATED_DESCRPITION);
    }

    @Test
    @Transactional
    public void getAllMedicamentsByDescrpitionIsNullOrNotNull() throws Exception {
        // Initialize the database
        medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where descrpition is not null
        defaultMedicamentShouldBeFound("descrpition.specified=true");

        // Get all the medicamentList where descrpition is null
        defaultMedicamentShouldNotBeFound("descrpition.specified=false");
    }
                @Test
    @Transactional
    public void getAllMedicamentsByDescrpitionContainsSomething() throws Exception {
        // Initialize the database
        medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where descrpition contains DEFAULT_DESCRPITION
        defaultMedicamentShouldBeFound("descrpition.contains=" + DEFAULT_DESCRPITION);

        // Get all the medicamentList where descrpition contains UPDATED_DESCRPITION
        defaultMedicamentShouldNotBeFound("descrpition.contains=" + UPDATED_DESCRPITION);
    }

    @Test
    @Transactional
    public void getAllMedicamentsByDescrpitionNotContainsSomething() throws Exception {
        // Initialize the database
        medicamentRepository.saveAndFlush(medicament);

        // Get all the medicamentList where descrpition does not contain DEFAULT_DESCRPITION
        defaultMedicamentShouldNotBeFound("descrpition.doesNotContain=" + DEFAULT_DESCRPITION);

        // Get all the medicamentList where descrpition does not contain UPDATED_DESCRPITION
        defaultMedicamentShouldBeFound("descrpition.doesNotContain=" + UPDATED_DESCRPITION);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultMedicamentShouldBeFound(String filter) throws Exception {
        restMedicamentMockMvc.perform(get("/api/medicaments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(medicament.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].forme").value(hasItem(DEFAULT_FORME.toString())))
            .andExpect(jsonPath("$.[*].descrpition").value(hasItem(DEFAULT_DESCRPITION)));

        // Check, that the count call also returns 1
        restMedicamentMockMvc.perform(get("/api/medicaments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultMedicamentShouldNotBeFound(String filter) throws Exception {
        restMedicamentMockMvc.perform(get("/api/medicaments?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restMedicamentMockMvc.perform(get("/api/medicaments/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingMedicament() throws Exception {
        // Get the medicament
        restMedicamentMockMvc.perform(get("/api/medicaments/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateMedicament() throws Exception {
        // Initialize the database
        medicamentService.save(medicament);

        int databaseSizeBeforeUpdate = medicamentRepository.findAll().size();

        // Update the medicament
        Medicament updatedMedicament = medicamentRepository.findById(medicament.getId()).get();
        // Disconnect from session so that the updates on updatedMedicament are not directly saved in db
        em.detach(updatedMedicament);
        updatedMedicament
            .name(UPDATED_NAME)
            .forme(UPDATED_FORME)
            .descrpition(UPDATED_DESCRPITION);

        restMedicamentMockMvc.perform(put("/api/medicaments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedMedicament)))
            .andExpect(status().isOk());

        // Validate the Medicament in the database
        List<Medicament> medicamentList = medicamentRepository.findAll();
        assertThat(medicamentList).hasSize(databaseSizeBeforeUpdate);
        Medicament testMedicament = medicamentList.get(medicamentList.size() - 1);
        assertThat(testMedicament.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testMedicament.getForme()).isEqualTo(UPDATED_FORME);
        assertThat(testMedicament.getDescrpition()).isEqualTo(UPDATED_DESCRPITION);
    }

    @Test
    @Transactional
    public void updateNonExistingMedicament() throws Exception {
        int databaseSizeBeforeUpdate = medicamentRepository.findAll().size();

        // Create the Medicament

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restMedicamentMockMvc.perform(put("/api/medicaments")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(medicament)))
            .andExpect(status().isBadRequest());

        // Validate the Medicament in the database
        List<Medicament> medicamentList = medicamentRepository.findAll();
        assertThat(medicamentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteMedicament() throws Exception {
        // Initialize the database
        medicamentService.save(medicament);

        int databaseSizeBeforeDelete = medicamentRepository.findAll().size();

        // Delete the medicament
        restMedicamentMockMvc.perform(delete("/api/medicaments/{id}", medicament.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Medicament> medicamentList = medicamentRepository.findAll();
        assertThat(medicamentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
