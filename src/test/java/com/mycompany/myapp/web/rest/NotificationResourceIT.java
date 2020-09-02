package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.TnTsuiviApp;
import com.mycompany.myapp.domain.Notification;
import com.mycompany.myapp.domain.User;
import com.mycompany.myapp.repository.NotificationRepository;
import com.mycompany.myapp.service.NotificationService;
import com.mycompany.myapp.service.dto.NotificationCriteria;
import com.mycompany.myapp.service.NotificationQueryService;

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
 * Integration tests for the {@link NotificationResource} REST controller.
 */
@SpringBootTest(classes = TnTsuiviApp.class)

@AutoConfigureMockMvc
@WithMockUser
public class NotificationResourceIT {

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_DATE_TIME = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_DATE_TIME = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LINK = "AAAAAAAAAA";
    private static final String UPDATED_LINK = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SEEN = false;
    private static final Boolean UPDATED_SEEN = true;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private NotificationQueryService notificationQueryService;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNotificationMockMvc;

    private Notification notification;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notification createEntity(EntityManager em) {
        Notification notification = new Notification()
            .description(DEFAULT_DESCRIPTION)
            .dateTime(DEFAULT_DATE_TIME)
            .link(DEFAULT_LINK)
            .seen(DEFAULT_SEEN);
        return notification;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Notification createUpdatedEntity(EntityManager em) {
        Notification notification = new Notification()
            .description(UPDATED_DESCRIPTION)
            .dateTime(UPDATED_DATE_TIME)
            .link(UPDATED_LINK)
            .seen(UPDATED_SEEN);
        return notification;
    }

    @BeforeEach
    public void initTest() {
        notification = createEntity(em);
    }

    @Test
    @Transactional
    public void createNotification() throws Exception {
        int databaseSizeBeforeCreate = notificationRepository.findAll().size();

        // Create the Notification
        restNotificationMockMvc.perform(post("/api/notifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notification)))
            .andExpect(status().isCreated());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeCreate + 1);
        Notification testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testNotification.getDateTime()).isEqualTo(DEFAULT_DATE_TIME);
        assertThat(testNotification.getLink()).isEqualTo(DEFAULT_LINK);
        assertThat(testNotification.isSeen()).isEqualTo(DEFAULT_SEEN);
    }

    @Test
    @Transactional
    public void createNotificationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = notificationRepository.findAll().size();

        // Create the Notification with an existing ID
        notification.setId(1L);

        // An entity with an existing ID cannot be created, so this API call must fail
        restNotificationMockMvc.perform(post("/api/notifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notification)))
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllNotifications() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList
        restNotificationMockMvc.perform(get("/api/notifications?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notification.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateTime").value(hasItem(DEFAULT_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK)))
            .andExpect(jsonPath("$.[*].seen").value(hasItem(DEFAULT_SEEN.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getNotification() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get the notification
        restNotificationMockMvc.perform(get("/api/notifications/{id}", notification.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(notification.getId().intValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.dateTime").value(DEFAULT_DATE_TIME.toString()))
            .andExpect(jsonPath("$.link").value(DEFAULT_LINK))
            .andExpect(jsonPath("$.seen").value(DEFAULT_SEEN.booleanValue()));
    }


    @Test
    @Transactional
    public void getNotificationsByIdFiltering() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        Long id = notification.getId();

        defaultNotificationShouldBeFound("id.equals=" + id);
        defaultNotificationShouldNotBeFound("id.notEquals=" + id);

        defaultNotificationShouldBeFound("id.greaterThanOrEqual=" + id);
        defaultNotificationShouldNotBeFound("id.greaterThan=" + id);

        defaultNotificationShouldBeFound("id.lessThanOrEqual=" + id);
        defaultNotificationShouldNotBeFound("id.lessThan=" + id);
    }


    @Test
    @Transactional
    public void getAllNotificationsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where description equals to DEFAULT_DESCRIPTION
        defaultNotificationShouldBeFound("description.equals=" + DEFAULT_DESCRIPTION);

        // Get all the notificationList where description equals to UPDATED_DESCRIPTION
        defaultNotificationShouldNotBeFound("description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllNotificationsByDescriptionIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where description not equals to DEFAULT_DESCRIPTION
        defaultNotificationShouldNotBeFound("description.notEquals=" + DEFAULT_DESCRIPTION);

        // Get all the notificationList where description not equals to UPDATED_DESCRIPTION
        defaultNotificationShouldBeFound("description.notEquals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllNotificationsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where description in DEFAULT_DESCRIPTION or UPDATED_DESCRIPTION
        defaultNotificationShouldBeFound("description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION);

        // Get all the notificationList where description equals to UPDATED_DESCRIPTION
        defaultNotificationShouldNotBeFound("description.in=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllNotificationsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where description is not null
        defaultNotificationShouldBeFound("description.specified=true");

        // Get all the notificationList where description is null
        defaultNotificationShouldNotBeFound("description.specified=false");
    }
                @Test
    @Transactional
    public void getAllNotificationsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where description contains DEFAULT_DESCRIPTION
        defaultNotificationShouldBeFound("description.contains=" + DEFAULT_DESCRIPTION);

        // Get all the notificationList where description contains UPDATED_DESCRIPTION
        defaultNotificationShouldNotBeFound("description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    public void getAllNotificationsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where description does not contain DEFAULT_DESCRIPTION
        defaultNotificationShouldNotBeFound("description.doesNotContain=" + DEFAULT_DESCRIPTION);

        // Get all the notificationList where description does not contain UPDATED_DESCRIPTION
        defaultNotificationShouldBeFound("description.doesNotContain=" + UPDATED_DESCRIPTION);
    }


    @Test
    @Transactional
    public void getAllNotificationsByDateTimeIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where dateTime equals to DEFAULT_DATE_TIME
        defaultNotificationShouldBeFound("dateTime.equals=" + DEFAULT_DATE_TIME);

        // Get all the notificationList where dateTime equals to UPDATED_DATE_TIME
        defaultNotificationShouldNotBeFound("dateTime.equals=" + UPDATED_DATE_TIME);
    }

    @Test
    @Transactional
    public void getAllNotificationsByDateTimeIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where dateTime not equals to DEFAULT_DATE_TIME
        defaultNotificationShouldNotBeFound("dateTime.notEquals=" + DEFAULT_DATE_TIME);

        // Get all the notificationList where dateTime not equals to UPDATED_DATE_TIME
        defaultNotificationShouldBeFound("dateTime.notEquals=" + UPDATED_DATE_TIME);
    }

    @Test
    @Transactional
    public void getAllNotificationsByDateTimeIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where dateTime in DEFAULT_DATE_TIME or UPDATED_DATE_TIME
        defaultNotificationShouldBeFound("dateTime.in=" + DEFAULT_DATE_TIME + "," + UPDATED_DATE_TIME);

        // Get all the notificationList where dateTime equals to UPDATED_DATE_TIME
        defaultNotificationShouldNotBeFound("dateTime.in=" + UPDATED_DATE_TIME);
    }

    @Test
    @Transactional
    public void getAllNotificationsByDateTimeIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where dateTime is not null
        defaultNotificationShouldBeFound("dateTime.specified=true");

        // Get all the notificationList where dateTime is null
        defaultNotificationShouldNotBeFound("dateTime.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotificationsByLinkIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where link equals to DEFAULT_LINK
        defaultNotificationShouldBeFound("link.equals=" + DEFAULT_LINK);

        // Get all the notificationList where link equals to UPDATED_LINK
        defaultNotificationShouldNotBeFound("link.equals=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    public void getAllNotificationsByLinkIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where link not equals to DEFAULT_LINK
        defaultNotificationShouldNotBeFound("link.notEquals=" + DEFAULT_LINK);

        // Get all the notificationList where link not equals to UPDATED_LINK
        defaultNotificationShouldBeFound("link.notEquals=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    public void getAllNotificationsByLinkIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where link in DEFAULT_LINK or UPDATED_LINK
        defaultNotificationShouldBeFound("link.in=" + DEFAULT_LINK + "," + UPDATED_LINK);

        // Get all the notificationList where link equals to UPDATED_LINK
        defaultNotificationShouldNotBeFound("link.in=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    public void getAllNotificationsByLinkIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where link is not null
        defaultNotificationShouldBeFound("link.specified=true");

        // Get all the notificationList where link is null
        defaultNotificationShouldNotBeFound("link.specified=false");
    }
                @Test
    @Transactional
    public void getAllNotificationsByLinkContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where link contains DEFAULT_LINK
        defaultNotificationShouldBeFound("link.contains=" + DEFAULT_LINK);

        // Get all the notificationList where link contains UPDATED_LINK
        defaultNotificationShouldNotBeFound("link.contains=" + UPDATED_LINK);
    }

    @Test
    @Transactional
    public void getAllNotificationsByLinkNotContainsSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where link does not contain DEFAULT_LINK
        defaultNotificationShouldNotBeFound("link.doesNotContain=" + DEFAULT_LINK);

        // Get all the notificationList where link does not contain UPDATED_LINK
        defaultNotificationShouldBeFound("link.doesNotContain=" + UPDATED_LINK);
    }


    @Test
    @Transactional
    public void getAllNotificationsBySeenIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where seen equals to DEFAULT_SEEN
        defaultNotificationShouldBeFound("seen.equals=" + DEFAULT_SEEN);

        // Get all the notificationList where seen equals to UPDATED_SEEN
        defaultNotificationShouldNotBeFound("seen.equals=" + UPDATED_SEEN);
    }

    @Test
    @Transactional
    public void getAllNotificationsBySeenIsNotEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where seen not equals to DEFAULT_SEEN
        defaultNotificationShouldNotBeFound("seen.notEquals=" + DEFAULT_SEEN);

        // Get all the notificationList where seen not equals to UPDATED_SEEN
        defaultNotificationShouldBeFound("seen.notEquals=" + UPDATED_SEEN);
    }

    @Test
    @Transactional
    public void getAllNotificationsBySeenIsInShouldWork() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where seen in DEFAULT_SEEN or UPDATED_SEEN
        defaultNotificationShouldBeFound("seen.in=" + DEFAULT_SEEN + "," + UPDATED_SEEN);

        // Get all the notificationList where seen equals to UPDATED_SEEN
        defaultNotificationShouldNotBeFound("seen.in=" + UPDATED_SEEN);
    }

    @Test
    @Transactional
    public void getAllNotificationsBySeenIsNullOrNotNull() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);

        // Get all the notificationList where seen is not null
        defaultNotificationShouldBeFound("seen.specified=true");

        // Get all the notificationList where seen is null
        defaultNotificationShouldNotBeFound("seen.specified=false");
    }

    @Test
    @Transactional
    public void getAllNotificationsByUserIsEqualToSomething() throws Exception {
        // Initialize the database
        notificationRepository.saveAndFlush(notification);
        User user = UserResourceIT.createEntity(em);
        em.persist(user);
        em.flush();
        notification.setUser(user);
        notificationRepository.saveAndFlush(notification);
        Long userId = user.getId();

        // Get all the notificationList where user equals to userId
        defaultNotificationShouldBeFound("userId.equals=" + userId);

        // Get all the notificationList where user equals to userId + 1
        defaultNotificationShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultNotificationShouldBeFound(String filter) throws Exception {
        restNotificationMockMvc.perform(get("/api/notifications?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(notification.getId().intValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].dateTime").value(hasItem(DEFAULT_DATE_TIME.toString())))
            .andExpect(jsonPath("$.[*].link").value(hasItem(DEFAULT_LINK)))
            .andExpect(jsonPath("$.[*].seen").value(hasItem(DEFAULT_SEEN.booleanValue())));

        // Check, that the count call also returns 1
        restNotificationMockMvc.perform(get("/api/notifications/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultNotificationShouldNotBeFound(String filter) throws Exception {
        restNotificationMockMvc.perform(get("/api/notifications?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restNotificationMockMvc.perform(get("/api/notifications/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }


    @Test
    @Transactional
    public void getNonExistingNotification() throws Exception {
        // Get the notification
        restNotificationMockMvc.perform(get("/api/notifications/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateNotification() throws Exception {
        // Initialize the database
        notificationService.save(notification);

        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();

        // Update the notification
        Notification updatedNotification = notificationRepository.findById(notification.getId()).get();
        // Disconnect from session so that the updates on updatedNotification are not directly saved in db
        em.detach(updatedNotification);
        updatedNotification
            .description(UPDATED_DESCRIPTION)
            .dateTime(UPDATED_DATE_TIME)
            .link(UPDATED_LINK)
            .seen(UPDATED_SEEN);

        restNotificationMockMvc.perform(put("/api/notifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedNotification)))
            .andExpect(status().isOk());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
        Notification testNotification = notificationList.get(notificationList.size() - 1);
        assertThat(testNotification.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testNotification.getDateTime()).isEqualTo(UPDATED_DATE_TIME);
        assertThat(testNotification.getLink()).isEqualTo(UPDATED_LINK);
        assertThat(testNotification.isSeen()).isEqualTo(UPDATED_SEEN);
    }

    @Test
    @Transactional
    public void updateNonExistingNotification() throws Exception {
        int databaseSizeBeforeUpdate = notificationRepository.findAll().size();

        // Create the Notification

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNotificationMockMvc.perform(put("/api/notifications")
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(notification)))
            .andExpect(status().isBadRequest());

        // Validate the Notification in the database
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteNotification() throws Exception {
        // Initialize the database
        notificationService.save(notification);

        int databaseSizeBeforeDelete = notificationRepository.findAll().size();

        // Delete the notification
        restNotificationMockMvc.perform(delete("/api/notifications/{id}", notification.getId())
            .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Notification> notificationList = notificationRepository.findAll();
        assertThat(notificationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
