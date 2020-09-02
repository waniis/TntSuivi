package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.TnTsuiviApp;
import com.mycompany.myapp.domain.Specialty;
import com.mycompany.myapp.repository.SpecialtyRepository;

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
 * Integration tests for the {@link SpecialtyResource} REST controller.
 */
@SpringBootTest(classes = TnTsuiviApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class SpecialtyResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private SpecialtyRepository specialtyRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSpecialtyMockMvc;

    private Specialty specialty;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Specialty createEntity(EntityManager em) {
        Specialty specialty = new Specialty()
            .name(DEFAULT_NAME);
        return specialty;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Specialty createUpdatedEntity(EntityManager em) {
        Specialty specialty = new Specialty()
            .name(UPDATED_NAME);
        return specialty;
    }

    @BeforeEach
    public void initTest() {
        specialty = createEntity(em);
    }

    @Test
    @Transactional
    public void createSpecialty() throws Exception {
        int databaseSizeBeforeCreate = specialtyRepository.findAll().size();

        // Create the Specialty
        restSpecialtyMockMvc.perform(post("/api/specialties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(specialty)))
            .andExpect(status().isCreated());

        // Validate the Specialty in the database
        List<Specialty> specialtyList = specialtyRepository.findAll();
        assertThat(specialtyList).hasSize(databaseSizeBeforeCreate + 1);
        Specialty testSpecialty = specialtyList.get(specialtyList.size() - 1);
        assertThat(testSpecialty.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createSpecialtyWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = specialtyRepository.findAll().size();

        // Create the Specialty with an existing ID
        specialty.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restSpecialtyMockMvc.perform(post("/api/specialties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(specialty)))
            .andExpect(status().isBadRequest());

        // Validate the Specialty in the database
        List<Specialty> specialtyList = specialtyRepository.findAll();
        assertThat(specialtyList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = specialtyRepository.findAll().size();
        // set the field null
        specialty.setName(null);

        // Create the Specialty, which fails.

        restSpecialtyMockMvc.perform(post("/api/specialties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(specialty)))
            .andExpect(status().isBadRequest());

        List<Specialty> specialtyList = specialtyRepository.findAll();
        assertThat(specialtyList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllSpecialties() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get all the specialtyList
        restSpecialtyMockMvc.perform(get("/api/specialties?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(specialty.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @Test
    @Transactional
    public void getSpecialty() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        // Get the specialty
        restSpecialtyMockMvc.perform(get("/api/specialties/{id}", specialty.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(specialty.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingSpecialty() throws Exception {
        // Get the specialty
        restSpecialtyMockMvc.perform(get("/api/specialties/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateSpecialty() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        int databaseSizeBeforeUpdate = specialtyRepository.findAll().size();

        // Update the specialty
        Specialty updatedSpecialty = specialtyRepository.findById(specialty.getId()).get();
        // Disconnect from session so that the updates on updatedSpecialty are not directly saved in db
        em.detach(updatedSpecialty);
        updatedSpecialty
            .name(UPDATED_NAME);

        restSpecialtyMockMvc.perform(put("/api/specialties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedSpecialty)))
            .andExpect(status().isOk());

        // Validate the Specialty in the database
        List<Specialty> specialtyList = specialtyRepository.findAll();
        assertThat(specialtyList).hasSize(databaseSizeBeforeUpdate);
        Specialty testSpecialty = specialtyList.get(specialtyList.size() - 1);
        assertThat(testSpecialty.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingSpecialty() throws Exception {
        int databaseSizeBeforeUpdate = specialtyRepository.findAll().size();

        // Create the Specialty

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restSpecialtyMockMvc.perform(put("/api/specialties")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(specialty)))
            .andExpect(status().isBadRequest());

        // Validate the Specialty in the database
        List<Specialty> specialtyList = specialtyRepository.findAll();
        assertThat(specialtyList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteSpecialty() throws Exception {
        // Initialize the database
        specialtyRepository.saveAndFlush(specialty);

        int databaseSizeBeforeDelete = specialtyRepository.findAll().size();

        // Delete the specialty
        restSpecialtyMockMvc.perform(delete("/api/specialties/{id}", specialty.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Specialty> specialtyList = specialtyRepository.findAll();
        assertThat(specialtyList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
