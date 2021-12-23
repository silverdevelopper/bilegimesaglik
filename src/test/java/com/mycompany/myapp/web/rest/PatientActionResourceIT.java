package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.PatientAction;
import com.mycompany.myapp.repository.PatientActionRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link PatientActionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PatientActionResourceIT {

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_ACTION_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_ACTION_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_HEALTSTATUS = "AAAAAAAAAA";
    private static final String UPDATED_HEALTSTATUS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/patient-actions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PatientActionRepository patientActionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPatientActionMockMvc;

    private PatientAction patientAction;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PatientAction createEntity(EntityManager em) {
        PatientAction patientAction = new PatientAction()
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .actionDescription(DEFAULT_ACTION_DESCRIPTION)
            .healtstatus(DEFAULT_HEALTSTATUS);
        return patientAction;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PatientAction createUpdatedEntity(EntityManager em) {
        PatientAction patientAction = new PatientAction()
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .actionDescription(UPDATED_ACTION_DESCRIPTION)
            .healtstatus(UPDATED_HEALTSTATUS);
        return patientAction;
    }

    @BeforeEach
    public void initTest() {
        patientAction = createEntity(em);
    }

    @Test
    @Transactional
    void createPatientAction() throws Exception {
        int databaseSizeBeforeCreate = patientActionRepository.findAll().size();
        // Create the PatientAction
        restPatientActionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patientAction)))
            .andExpect(status().isCreated());

        // Validate the PatientAction in the database
        List<PatientAction> patientActionList = patientActionRepository.findAll();
        assertThat(patientActionList).hasSize(databaseSizeBeforeCreate + 1);
        PatientAction testPatientAction = patientActionList.get(patientActionList.size() - 1);
        assertThat(testPatientAction.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testPatientAction.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testPatientAction.getActionDescription()).isEqualTo(DEFAULT_ACTION_DESCRIPTION);
        assertThat(testPatientAction.getHealtstatus()).isEqualTo(DEFAULT_HEALTSTATUS);
    }

    @Test
    @Transactional
    void createPatientActionWithExistingId() throws Exception {
        // Create the PatientAction with an existing ID
        patientAction.setId(1L);

        int databaseSizeBeforeCreate = patientActionRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPatientActionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patientAction)))
            .andExpect(status().isBadRequest());

        // Validate the PatientAction in the database
        List<PatientAction> patientActionList = patientActionRepository.findAll();
        assertThat(patientActionList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPatientActions() throws Exception {
        // Initialize the database
        patientActionRepository.saveAndFlush(patientAction);

        // Get all the patientActionList
        restPatientActionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(patientAction.getId().intValue())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].actionDescription").value(hasItem(DEFAULT_ACTION_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].healtstatus").value(hasItem(DEFAULT_HEALTSTATUS)));
    }

    @Test
    @Transactional
    void getPatientAction() throws Exception {
        // Initialize the database
        patientActionRepository.saveAndFlush(patientAction);

        // Get the patientAction
        restPatientActionMockMvc
            .perform(get(ENTITY_API_URL_ID, patientAction.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(patientAction.getId().intValue()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.actionDescription").value(DEFAULT_ACTION_DESCRIPTION))
            .andExpect(jsonPath("$.healtstatus").value(DEFAULT_HEALTSTATUS));
    }

    @Test
    @Transactional
    void getNonExistingPatientAction() throws Exception {
        // Get the patientAction
        restPatientActionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPatientAction() throws Exception {
        // Initialize the database
        patientActionRepository.saveAndFlush(patientAction);

        int databaseSizeBeforeUpdate = patientActionRepository.findAll().size();

        // Update the patientAction
        PatientAction updatedPatientAction = patientActionRepository.findById(patientAction.getId()).get();
        // Disconnect from session so that the updates on updatedPatientAction are not directly saved in db
        em.detach(updatedPatientAction);
        updatedPatientAction
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .actionDescription(UPDATED_ACTION_DESCRIPTION)
            .healtstatus(UPDATED_HEALTSTATUS);

        restPatientActionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPatientAction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPatientAction))
            )
            .andExpect(status().isOk());

        // Validate the PatientAction in the database
        List<PatientAction> patientActionList = patientActionRepository.findAll();
        assertThat(patientActionList).hasSize(databaseSizeBeforeUpdate);
        PatientAction testPatientAction = patientActionList.get(patientActionList.size() - 1);
        assertThat(testPatientAction.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testPatientAction.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testPatientAction.getActionDescription()).isEqualTo(UPDATED_ACTION_DESCRIPTION);
        assertThat(testPatientAction.getHealtstatus()).isEqualTo(UPDATED_HEALTSTATUS);
    }

    @Test
    @Transactional
    void putNonExistingPatientAction() throws Exception {
        int databaseSizeBeforeUpdate = patientActionRepository.findAll().size();
        patientAction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientActionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, patientAction.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patientAction))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientAction in the database
        List<PatientAction> patientActionList = patientActionRepository.findAll();
        assertThat(patientActionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPatientAction() throws Exception {
        int databaseSizeBeforeUpdate = patientActionRepository.findAll().size();
        patientAction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientActionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(patientAction))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientAction in the database
        List<PatientAction> patientActionList = patientActionRepository.findAll();
        assertThat(patientActionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPatientAction() throws Exception {
        int databaseSizeBeforeUpdate = patientActionRepository.findAll().size();
        patientAction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientActionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(patientAction)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PatientAction in the database
        List<PatientAction> patientActionList = patientActionRepository.findAll();
        assertThat(patientActionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePatientActionWithPatch() throws Exception {
        // Initialize the database
        patientActionRepository.saveAndFlush(patientAction);

        int databaseSizeBeforeUpdate = patientActionRepository.findAll().size();

        // Update the patientAction using partial update
        PatientAction partialUpdatedPatientAction = new PatientAction();
        partialUpdatedPatientAction.setId(patientAction.getId());

        partialUpdatedPatientAction.endDate(UPDATED_END_DATE).healtstatus(UPDATED_HEALTSTATUS);

        restPatientActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatientAction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPatientAction))
            )
            .andExpect(status().isOk());

        // Validate the PatientAction in the database
        List<PatientAction> patientActionList = patientActionRepository.findAll();
        assertThat(patientActionList).hasSize(databaseSizeBeforeUpdate);
        PatientAction testPatientAction = patientActionList.get(patientActionList.size() - 1);
        assertThat(testPatientAction.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testPatientAction.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testPatientAction.getActionDescription()).isEqualTo(DEFAULT_ACTION_DESCRIPTION);
        assertThat(testPatientAction.getHealtstatus()).isEqualTo(UPDATED_HEALTSTATUS);
    }

    @Test
    @Transactional
    void fullUpdatePatientActionWithPatch() throws Exception {
        // Initialize the database
        patientActionRepository.saveAndFlush(patientAction);

        int databaseSizeBeforeUpdate = patientActionRepository.findAll().size();

        // Update the patientAction using partial update
        PatientAction partialUpdatedPatientAction = new PatientAction();
        partialUpdatedPatientAction.setId(patientAction.getId());

        partialUpdatedPatientAction
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .actionDescription(UPDATED_ACTION_DESCRIPTION)
            .healtstatus(UPDATED_HEALTSTATUS);

        restPatientActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPatientAction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPatientAction))
            )
            .andExpect(status().isOk());

        // Validate the PatientAction in the database
        List<PatientAction> patientActionList = patientActionRepository.findAll();
        assertThat(patientActionList).hasSize(databaseSizeBeforeUpdate);
        PatientAction testPatientAction = patientActionList.get(patientActionList.size() - 1);
        assertThat(testPatientAction.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testPatientAction.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testPatientAction.getActionDescription()).isEqualTo(UPDATED_ACTION_DESCRIPTION);
        assertThat(testPatientAction.getHealtstatus()).isEqualTo(UPDATED_HEALTSTATUS);
    }

    @Test
    @Transactional
    void patchNonExistingPatientAction() throws Exception {
        int databaseSizeBeforeUpdate = patientActionRepository.findAll().size();
        patientAction.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPatientActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, patientAction.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patientAction))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientAction in the database
        List<PatientAction> patientActionList = patientActionRepository.findAll();
        assertThat(patientActionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPatientAction() throws Exception {
        int databaseSizeBeforeUpdate = patientActionRepository.findAll().size();
        patientAction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientActionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(patientAction))
            )
            .andExpect(status().isBadRequest());

        // Validate the PatientAction in the database
        List<PatientAction> patientActionList = patientActionRepository.findAll();
        assertThat(patientActionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPatientAction() throws Exception {
        int databaseSizeBeforeUpdate = patientActionRepository.findAll().size();
        patientAction.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPatientActionMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(patientAction))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PatientAction in the database
        List<PatientAction> patientActionList = patientActionRepository.findAll();
        assertThat(patientActionList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePatientAction() throws Exception {
        // Initialize the database
        patientActionRepository.saveAndFlush(patientAction);

        int databaseSizeBeforeDelete = patientActionRepository.findAll().size();

        // Delete the patientAction
        restPatientActionMockMvc
            .perform(delete(ENTITY_API_URL_ID, patientAction.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PatientAction> patientActionList = patientActionRepository.findAll();
        assertThat(patientActionList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
