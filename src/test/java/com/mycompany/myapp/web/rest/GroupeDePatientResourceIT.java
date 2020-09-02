package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.TnTsuiviApp;
import com.mycompany.myapp.domain.GroupeDePatient;
import com.mycompany.myapp.repository.GroupeDePatientRepository;

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
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link GroupeDePatientResource} REST controller.
 */
@SpringBootTest(classes = TnTsuiviApp.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
public class GroupeDePatientResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    @Autowired
    private GroupeDePatientRepository groupeDePatientRepository;

    @Mock
    private GroupeDePatientRepository groupeDePatientRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restGroupeDePatientMockMvc;

    private GroupeDePatient groupeDePatient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GroupeDePatient createEntity(EntityManager em) {
        GroupeDePatient groupeDePatient = new GroupeDePatient()
            .name(DEFAULT_NAME);
        return groupeDePatient;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static GroupeDePatient createUpdatedEntity(EntityManager em) {
        GroupeDePatient groupeDePatient = new GroupeDePatient()
            .name(UPDATED_NAME);
        return groupeDePatient;
    }

    @BeforeEach
    public void initTest() {
        groupeDePatient = createEntity(em);
    }

    @Test
    @Transactional
    public void createGroupeDePatient() throws Exception {
        int databaseSizeBeforeCreate = groupeDePatientRepository.findAll().size();

        // Create the GroupeDePatient
        restGroupeDePatientMockMvc.perform(post("/api/groupe-de-patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(groupeDePatient)))
            .andExpect(status().isCreated());

        // Validate the GroupeDePatient in the database
        List<GroupeDePatient> groupeDePatientList = groupeDePatientRepository.findAll();
        assertThat(groupeDePatientList).hasSize(databaseSizeBeforeCreate + 1);
        GroupeDePatient testGroupeDePatient = groupeDePatientList.get(groupeDePatientList.size() - 1);
        assertThat(testGroupeDePatient.getName()).isEqualTo(DEFAULT_NAME);
    }

    @Test
    @Transactional
    public void createGroupeDePatientWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = groupeDePatientRepository.findAll().size();

        // Create the GroupeDePatient with an existing ID
        groupeDePatient.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restGroupeDePatientMockMvc.perform(post("/api/groupe-de-patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(groupeDePatient)))
            .andExpect(status().isBadRequest());

        // Validate the GroupeDePatient in the database
        List<GroupeDePatient> groupeDePatientList = groupeDePatientRepository.findAll();
        assertThat(groupeDePatientList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllGroupeDePatients() throws Exception {
        // Initialize the database
        groupeDePatientRepository.saveAndFlush(groupeDePatient);

        // Get all the groupeDePatientList
        restGroupeDePatientMockMvc.perform(get("/api/groupe-de-patients?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(groupeDePatient.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }
    
    @SuppressWarnings({"unchecked"})
    public void getAllGroupeDePatientsWithEagerRelationshipsIsEnabled() throws Exception {
        GroupeDePatientResource groupeDePatientResource = new GroupeDePatientResource(null,
                groupeDePatientRepositoryMock);
        when(groupeDePatientRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGroupeDePatientMockMvc.perform(get("/api/groupe-de-patients?eagerload=true"))
            .andExpect(status().isOk());

        verify(groupeDePatientRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({"unchecked"})
    public void getAllGroupeDePatientsWithEagerRelationshipsIsNotEnabled() throws Exception {
        GroupeDePatientResource groupeDePatientResource = new GroupeDePatientResource(null,
                groupeDePatientRepositoryMock);
        when(groupeDePatientRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restGroupeDePatientMockMvc.perform(get("/api/groupe-de-patients?eagerload=true"))
            .andExpect(status().isOk());

        verify(groupeDePatientRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @Test
    @Transactional
    public void getGroupeDePatient() throws Exception {
        // Initialize the database
        groupeDePatientRepository.saveAndFlush(groupeDePatient);

        // Get the groupeDePatient
        restGroupeDePatientMockMvc.perform(get("/api/groupe-de-patients/{id}", groupeDePatient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(groupeDePatient.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingGroupeDePatient() throws Exception {
        // Get the groupeDePatient
        restGroupeDePatientMockMvc.perform(get("/api/groupe-de-patients/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateGroupeDePatient() throws Exception {
        // Initialize the database
        groupeDePatientRepository.saveAndFlush(groupeDePatient);

        int databaseSizeBeforeUpdate = groupeDePatientRepository.findAll().size();

        // Update the groupeDePatient
        GroupeDePatient updatedGroupeDePatient = groupeDePatientRepository.findById(groupeDePatient.getId()).get();
        // Disconnect from session so that the updates on updatedGroupeDePatient are not directly saved in db
        em.detach(updatedGroupeDePatient);
        updatedGroupeDePatient
            .name(UPDATED_NAME);

        restGroupeDePatientMockMvc.perform(put("/api/groupe-de-patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedGroupeDePatient)))
            .andExpect(status().isOk());

        // Validate the GroupeDePatient in the database
        List<GroupeDePatient> groupeDePatientList = groupeDePatientRepository.findAll();
        assertThat(groupeDePatientList).hasSize(databaseSizeBeforeUpdate);
        GroupeDePatient testGroupeDePatient = groupeDePatientList.get(groupeDePatientList.size() - 1);
        assertThat(testGroupeDePatient.getName()).isEqualTo(UPDATED_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingGroupeDePatient() throws Exception {
        int databaseSizeBeforeUpdate = groupeDePatientRepository.findAll().size();

        // Create the GroupeDePatient

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restGroupeDePatientMockMvc.perform(put("/api/groupe-de-patients")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(groupeDePatient)))
            .andExpect(status().isBadRequest());

        // Validate the GroupeDePatient in the database
        List<GroupeDePatient> groupeDePatientList = groupeDePatientRepository.findAll();
        assertThat(groupeDePatientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteGroupeDePatient() throws Exception {
        // Initialize the database
        groupeDePatientRepository.saveAndFlush(groupeDePatient);

        int databaseSizeBeforeDelete = groupeDePatientRepository.findAll().size();

        // Delete the groupeDePatient
        restGroupeDePatientMockMvc.perform(delete("/api/groupe-de-patients/{id}", groupeDePatient.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<GroupeDePatient> groupeDePatientList = groupeDePatientRepository.findAll();
        assertThat(groupeDePatientList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
