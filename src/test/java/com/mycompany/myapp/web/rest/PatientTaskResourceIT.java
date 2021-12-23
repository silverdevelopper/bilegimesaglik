package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.PatientTask;
import com.mycompany.myapp.repository.PatientTaskRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PatientTaskResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PatientTaskResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_SCHEDULE = "AAAAAAAAAA";
    private static final String UPDATED_SCHEDULE = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/patient-tasks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PatientTaskRepository patientTaskRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPatientTaskMockMvc;

    private PatientTask patientTask;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PatientTask createEntity(EntityManager em) {
        PatientTask patientTask = new PatientTask().title(DEFAULT_TITLE).description(DEFAULT_DESCRIPTION).schedule(DEFAULT_SCHEDULE);
        return patientTask;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PatientTask createUpdatedEntity(EntityManager em) {
        PatientTask patientTask = new PatientTask().title(UPDATED_TITLE).description(UPDATED_DESCRIPTION).schedule(UPDATED_SCHEDULE);
        return patientTask;
    }

    @BeforeEach
    public void initTest() {
        patientTask = createEntity(em);
    }

    @Test
    @Transactional
    void createPatientTask() throws Exception {
        int databaseSizeBeforeCreate = patientTaskRepository.findAll().size();
        // Create the PatientTask
        restPatientTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patientTask)))
            .andExpect(status().isCreated());

        // Validate the PatientTask in the database
        List<PatientTask> patientTaskList = patientTaskRepository.findAll();
        assertThat(patientTaskList).hasSize(databaseSizeBeforeCreate + 1);
        PatientTask testPatientTask = patientTaskList.get(patientTaskList.size() - 1);
        assertThat(testPatientTask.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testPatientTask.getDescription()).isEqualTo(DEFAULT_DESCRIPTION);
        assertThat(testPatientTask.getSchedule()).isEqualTo(DEFAULT_SCHEDULE);
    }

    @Test
    @Transactional
    void createPatientTaskWithExistingId() throws Exception {
        // Create the PatientTask with an existing ID
        patientTask.setId(1L);

        int databaseSizeBeforeCreate = patientTaskRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPatientTaskMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patientTask)))
            .andExpect(status().isBadRequest());

        // Validate the PatientTask in the database
        List<PatientTask> patientTaskList = patientTaskRepository.findAll();
        assertThat(patientTaskList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPatientTasks() throws Exception {
        // Initialize the database
        patientTaskRepository.saveAndFlush(patientTask);

        // Get all the patientTaskList
        restPatientTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patientTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].schedule").value(hasItem(DEFAULT_SCHEDULE)));
    }

    @Test
    @Transactional
    void getPatientTask() throws Exception {
        // Initialize the database
        patientTaskRepository.saveAndFlush(patientTask);

        // Get the patientTask
        restPatientTaskMockMvc
            .perform(get(ENTITY_API_URL_ID, patientTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(patientTask.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.schedule").value(DEFAULT_SCHEDULE));
    }

    @Test
    @Transactional
    void getNonExistingPatientTask() throws Exception {
        // Get the patientTask
        restPatientTaskMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPatientTask() throws Exception {
        // Initialize the database
        patientTaskRepository.saveAndFlush(patientTask);

        int databaseSizeBeforeUpdate = patientTaskRepository.findAll().size();

        // Update the patientTask
        PatientTask updatedPatientTask = patientTaskRepository.findById(patientTask.getId()).get();
        // Disconnect from session so that the updates on updatedPatientTask are not directly saved in db
        em.detach(updatedPatientTask);
        updatedPatientTask.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION).schedule(UPDATED_SCHEDULE);

        restPatientTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPatientTask.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPatientTask))
            )
            .andExpect(status().isOk());

        // Validate the PatientTask in the database
        List<PatientTask> patientTaskList = patientTaskRepository.findAll();
        assertThat(patientTaskList).hasSize(databaseSizeBeforeUpdate);
        PatientTask testPatientTask = patientTaskList.get(patientTaskList.size() - 1);
        assertThat(testPatientTask.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPatientTask.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPatientTask.getSchedule()).isEqualTo(UPDATED_SCHEDULE);
    }

    @Test
    @Transactional
    void putNonExistingPatientTask() throws Exception {
        int databaseSizeBeforeUpdate = patientTaskRepository.findAll().size();
        patientTask.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, patientTask.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patientTask))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientTask in the database
        List<PatientTask> patientTaskList = patientTaskRepository.findAll();
        assertThat(patientTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPatientTask() throws Exception {
        int databaseSizeBeforeUpdate = patientTaskRepository.findAll().size();
        patientTask.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patientTask))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientTask in the database
        List<PatientTask> patientTaskList = patientTaskRepository.findAll();
        assertThat(patientTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPatientTask() throws Exception {
        int databaseSizeBeforeUpdate = patientTaskRepository.findAll().size();
        patientTask.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientTaskMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patientTask)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PatientTask in the database
        List<PatientTask> patientTaskList = patientTaskRepository.findAll();
        assertThat(patientTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePatientTaskWithPatch() throws Exception {
        // Initialize the database
        patientTaskRepository.saveAndFlush(patientTask);

        int databaseSizeBeforeUpdate = patientTaskRepository.findAll().size();

        // Update the patientTask using partial update
        PatientTask partialUpdatedPatientTask = new PatientTask();
        partialUpdatedPatientTask.setId(patientTask.getId());

        partialUpdatedPatientTask.description(UPDATED_DESCRIPTION);

        restPatientTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatientTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPatientTask))
            )
            .andExpect(status().isOk());

        // Validate the PatientTask in the database
        List<PatientTask> patientTaskList = patientTaskRepository.findAll();
        assertThat(patientTaskList).hasSize(databaseSizeBeforeUpdate);
        PatientTask testPatientTask = patientTaskList.get(patientTaskList.size() - 1);
        assertThat(testPatientTask.getTitle()).isEqualTo(DEFAULT_TITLE);
        assertThat(testPatientTask.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPatientTask.getSchedule()).isEqualTo(DEFAULT_SCHEDULE);
    }

    @Test
    @Transactional
    void fullUpdatePatientTaskWithPatch() throws Exception {
        // Initialize the database
        patientTaskRepository.saveAndFlush(patientTask);

        int databaseSizeBeforeUpdate = patientTaskRepository.findAll().size();

        // Update the patientTask using partial update
        PatientTask partialUpdatedPatientTask = new PatientTask();
        partialUpdatedPatientTask.setId(patientTask.getId());

        partialUpdatedPatientTask.title(UPDATED_TITLE).description(UPDATED_DESCRIPTION).schedule(UPDATED_SCHEDULE);

        restPatientTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatientTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPatientTask))
            )
            .andExpect(status().isOk());

        // Validate the PatientTask in the database
        List<PatientTask> patientTaskList = patientTaskRepository.findAll();
        assertThat(patientTaskList).hasSize(databaseSizeBeforeUpdate);
        PatientTask testPatientTask = patientTaskList.get(patientTaskList.size() - 1);
        assertThat(testPatientTask.getTitle()).isEqualTo(UPDATED_TITLE);
        assertThat(testPatientTask.getDescription()).isEqualTo(UPDATED_DESCRIPTION);
        assertThat(testPatientTask.getSchedule()).isEqualTo(UPDATED_SCHEDULE);
    }

    @Test
    @Transactional
    void patchNonExistingPatientTask() throws Exception {
        int databaseSizeBeforeUpdate = patientTaskRepository.findAll().size();
        patientTask.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, patientTask.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patientTask))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientTask in the database
        List<PatientTask> patientTaskList = patientTaskRepository.findAll();
        assertThat(patientTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPatientTask() throws Exception {
        int databaseSizeBeforeUpdate = patientTaskRepository.findAll().size();
        patientTask.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patientTask))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientTask in the database
        List<PatientTask> patientTaskList = patientTaskRepository.findAll();
        assertThat(patientTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPatientTask() throws Exception {
        int databaseSizeBeforeUpdate = patientTaskRepository.findAll().size();
        patientTask.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientTaskMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(patientTask))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PatientTask in the database
        List<PatientTask> patientTaskList = patientTaskRepository.findAll();
        assertThat(patientTaskList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePatientTask() throws Exception {
        // Initialize the database
        patientTaskRepository.saveAndFlush(patientTask);

        int databaseSizeBeforeDelete = patientTaskRepository.findAll().size();

        // Delete the patientTask
        restPatientTaskMockMvc
            .perform(delete(ENTITY_API_URL_ID, patientTask.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PatientTask> patientTaskList = patientTaskRepository.findAll();
        assertThat(patientTaskList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
