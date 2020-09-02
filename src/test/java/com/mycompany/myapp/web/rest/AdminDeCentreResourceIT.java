package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.TnTsuiviApp;
import com.mycompany.myapp.domain.AdminDeCentre;
import com.mycompany.myapp.repository.AdminDeCentreRepository;

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
 * Integration tests for the {@link AdminDeCentreResource} REST controller.
 */
@SpringBootTest(classes = TnTsuiviApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class AdminDeCentreResourceIT {

    private static final String DEFAULT_FULL_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FULL_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    @Autowired
    private AdminDeCentreRepository adminDeCentreRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdminDeCentreMockMvc;

    private AdminDeCentre adminDeCentre;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdminDeCentre createEntity(EntityManager em) {
        AdminDeCentre adminDeCentre = new AdminDeCentre()
            .fullName(DEFAULT_FULL_NAME)
            .phone(DEFAULT_PHONE);
        return adminDeCentre;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdminDeCentre createUpdatedEntity(EntityManager em) {
        AdminDeCentre adminDeCentre = new AdminDeCentre()
            .fullName(UPDATED_FULL_NAME)
            .phone(UPDATED_PHONE);
        return adminDeCentre;
    }

    @BeforeEach
    public void initTest() {
        adminDeCentre = createEntity(em);
    }

    @Test
    @Transactional
    public void createAdminDeCentre() throws Exception {
        int databaseSizeBeforeCreate = adminDeCentreRepository.findAll().size();

        // Create the AdminDeCentre
        restAdminDeCentreMockMvc.perform(post("/api/admin-de-centres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(adminDeCentre)))
            .andExpect(status().isCreated());

        // Validate the AdminDeCentre in the database
        List<AdminDeCentre> adminDeCentreList = adminDeCentreRepository.findAll();
        assertThat(adminDeCentreList).hasSize(databaseSizeBeforeCreate + 1);
        AdminDeCentre testAdminDeCentre = adminDeCentreList.get(adminDeCentreList.size() - 1);
        assertThat(testAdminDeCentre.getFullName()).isEqualTo(DEFAULT_FULL_NAME);
        assertThat(testAdminDeCentre.getPhone()).isEqualTo(DEFAULT_PHONE);
    }

    @Test
    @Transactional
    public void createAdminDeCentreWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = adminDeCentreRepository.findAll().size();

        // Create the AdminDeCentre with an existing ID
        adminDeCentre.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdminDeCentreMockMvc.perform(post("/api/admin-de-centres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(adminDeCentre)))
            .andExpect(status().isBadRequest());

        // Validate the AdminDeCentre in the database
        List<AdminDeCentre> adminDeCentreList = adminDeCentreRepository.findAll();
        assertThat(adminDeCentreList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkPhoneIsRequired() throws Exception {
        int databaseSizeBeforeTest = adminDeCentreRepository.findAll().size();
        // set the field null
        adminDeCentre.setPhone(null);

        // Create the AdminDeCentre, which fails.

        restAdminDeCentreMockMvc.perform(post("/api/admin-de-centres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(adminDeCentre)))
            .andExpect(status().isBadRequest());

        List<AdminDeCentre> adminDeCentreList = adminDeCentreRepository.findAll();
        assertThat(adminDeCentreList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllAdminDeCentres() throws Exception {
        // Initialize the database
        adminDeCentreRepository.saveAndFlush(adminDeCentre);

        // Get all the adminDeCentreList
        restAdminDeCentreMockMvc.perform(get("/api/admin-de-centres?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(adminDeCentre.getId().intValue())))
            .andExpect(jsonPath("$.[*].fullName").value(hasItem(DEFAULT_FULL_NAME)))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)));
    }
    
    @Test
    @Transactional
    public void getAdminDeCentre() throws Exception {
        // Initialize the database
        adminDeCentreRepository.saveAndFlush(adminDeCentre);

        // Get the adminDeCentre
        restAdminDeCentreMockMvc.perform(get("/api/admin-de-centres/{id}", adminDeCentre.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(adminDeCentre.getId().intValue()))
            .andExpect(jsonPath("$.fullName").value(DEFAULT_FULL_NAME))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE));
    }

    @Test
    @Transactional
    public void getNonExistingAdminDeCentre() throws Exception {
        // Get the adminDeCentre
        restAdminDeCentreMockMvc.perform(get("/api/admin-de-centres/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateAdminDeCentre() throws Exception {
        // Initialize the database
        adminDeCentreRepository.saveAndFlush(adminDeCentre);

        int databaseSizeBeforeUpdate = adminDeCentreRepository.findAll().size();

        // Update the adminDeCentre
        AdminDeCentre updatedAdminDeCentre = adminDeCentreRepository.findById(adminDeCentre.getId()).get();
        // Disconnect from session so that the updates on updatedAdminDeCentre are not directly saved in db
        em.detach(updatedAdminDeCentre);
        updatedAdminDeCentre
            .fullName(UPDATED_FULL_NAME)
            .phone(UPDATED_PHONE);

        restAdminDeCentreMockMvc.perform(put("/api/admin-de-centres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedAdminDeCentre)))
            .andExpect(status().isOk());

        // Validate the AdminDeCentre in the database
        List<AdminDeCentre> adminDeCentreList = adminDeCentreRepository.findAll();
        assertThat(adminDeCentreList).hasSize(databaseSizeBeforeUpdate);
        AdminDeCentre testAdminDeCentre = adminDeCentreList.get(adminDeCentreList.size() - 1);
        assertThat(testAdminDeCentre.getFullName()).isEqualTo(UPDATED_FULL_NAME);
        assertThat(testAdminDeCentre.getPhone()).isEqualTo(UPDATED_PHONE);
    }

    @Test
    @Transactional
    public void updateNonExistingAdminDeCentre() throws Exception {
        int databaseSizeBeforeUpdate = adminDeCentreRepository.findAll().size();

        // Create the AdminDeCentre

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdminDeCentreMockMvc.perform(put("/api/admin-de-centres")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(adminDeCentre)))
            .andExpect(status().isBadRequest());

        // Validate the AdminDeCentre in the database
        List<AdminDeCentre> adminDeCentreList = adminDeCentreRepository.findAll();
        assertThat(adminDeCentreList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteAdminDeCentre() throws Exception {
        // Initialize the database
        adminDeCentreRepository.saveAndFlush(adminDeCentre);

        int databaseSizeBeforeDelete = adminDeCentreRepository.findAll().size();

        // Delete the adminDeCentre
        restAdminDeCentreMockMvc.perform(delete("/api/admin-de-centres/{id}", adminDeCentre.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<AdminDeCentre> adminDeCentreList = adminDeCentreRepository.findAll();
        assertThat(adminDeCentreList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
