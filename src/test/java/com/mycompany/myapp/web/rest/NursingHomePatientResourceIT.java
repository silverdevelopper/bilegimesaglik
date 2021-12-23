package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.NursingHomePatient;
import com.mycompany.myapp.repository.NursingHomePatientRepository;
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
 * Integration tests for the {@link NursingHomePatientResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NursingHomePatientResourceIT {

    private static final String ENTITY_API_URL = "/api/nursing-home-patients";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NursingHomePatientRepository nursingHomePatientRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNursingHomePatientMockMvc;

    private NursingHomePatient nursingHomePatient;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NursingHomePatient createEntity(EntityManager em) {
        NursingHomePatient nursingHomePatient = new NursingHomePatient();
        return nursingHomePatient;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NursingHomePatient createUpdatedEntity(EntityManager em) {
        NursingHomePatient nursingHomePatient = new NursingHomePatient();
        return nursingHomePatient;
    }

    @BeforeEach
    public void initTest() {
        nursingHomePatient = createEntity(em);
    }

    @Test
    @Transactional
    void createNursingHomePatient() throws Exception {
        int databaseSizeBeforeCreate = nursingHomePatientRepository.findAll().size();
        // Create the NursingHomePatient
        restNursingHomePatientMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nursingHomePatient))
            )
            .andExpect(status().isCreated());

        // Validate the NursingHomePatient in the database
        List<NursingHomePatient> nursingHomePatientList = nursingHomePatientRepository.findAll();
        assertThat(nursingHomePatientList).hasSize(databaseSizeBeforeCreate + 1);
        NursingHomePatient testNursingHomePatient = nursingHomePatientList.get(nursingHomePatientList.size() - 1);
    }

    @Test
    @Transactional
    void createNursingHomePatientWithExistingId() throws Exception {
        // Create the NursingHomePatient with an existing ID
        nursingHomePatient.setId(1L);

        int databaseSizeBeforeCreate = nursingHomePatientRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNursingHomePatientMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nursingHomePatient))
            )
            .andExpect(status().isBadRequest());

        // Validate the NursingHomePatient in the database
        List<NursingHomePatient> nursingHomePatientList = nursingHomePatientRepository.findAll();
        assertThat(nursingHomePatientList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllNursingHomePatients() throws Exception {
        // Initialize the database
        nursingHomePatientRepository.saveAndFlush(nursingHomePatient);

        // Get all the nursingHomePatientList
        restNursingHomePatientMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nursingHomePatient.getId().intValue())));
    }

    @Test
    @Transactional
    void getNursingHomePatient() throws Exception {
        // Initialize the database
        nursingHomePatientRepository.saveAndFlush(nursingHomePatient);

        // Get the nursingHomePatient
        restNursingHomePatientMockMvc
            .perform(get(ENTITY_API_URL_ID, nursingHomePatient.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nursingHomePatient.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingNursingHomePatient() throws Exception {
        // Get the nursingHomePatient
        restNursingHomePatientMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNursingHomePatient() throws Exception {
        // Initialize the database
        nursingHomePatientRepository.saveAndFlush(nursingHomePatient);

        int databaseSizeBeforeUpdate = nursingHomePatientRepository.findAll().size();

        // Update the nursingHomePatient
        NursingHomePatient updatedNursingHomePatient = nursingHomePatientRepository.findById(nursingHomePatient.getId()).get();
        // Disconnect from session so that the updates on updatedNursingHomePatient are not directly saved in db
        em.detach(updatedNursingHomePatient);

        restNursingHomePatientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNursingHomePatient.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedNursingHomePatient))
            )
            .andExpect(status().isOk());

        // Validate the NursingHomePatient in the database
        List<NursingHomePatient> nursingHomePatientList = nursingHomePatientRepository.findAll();
        assertThat(nursingHomePatientList).hasSize(databaseSizeBeforeUpdate);
        NursingHomePatient testNursingHomePatient = nursingHomePatientList.get(nursingHomePatientList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingNursingHomePatient() throws Exception {
        int databaseSizeBeforeUpdate = nursingHomePatientRepository.findAll().size();
        nursingHomePatient.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNursingHomePatientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nursingHomePatient.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nursingHomePatient))
            )
            .andExpect(status().isBadRequest());

        // Validate the NursingHomePatient in the database
        List<NursingHomePatient> nursingHomePatientList = nursingHomePatientRepository.findAll();
        assertThat(nursingHomePatientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNursingHomePatient() throws Exception {
        int databaseSizeBeforeUpdate = nursingHomePatientRepository.findAll().size();
        nursingHomePatient.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNursingHomePatientMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nursingHomePatient))
            )
            .andExpect(status().isBadRequest());

        // Validate the NursingHomePatient in the database
        List<NursingHomePatient> nursingHomePatientList = nursingHomePatientRepository.findAll();
        assertThat(nursingHomePatientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNursingHomePatient() throws Exception {
        int databaseSizeBeforeUpdate = nursingHomePatientRepository.findAll().size();
        nursingHomePatient.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNursingHomePatientMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nursingHomePatient))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NursingHomePatient in the database
        List<NursingHomePatient> nursingHomePatientList = nursingHomePatientRepository.findAll();
        assertThat(nursingHomePatientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNursingHomePatientWithPatch() throws Exception {
        // Initialize the database
        nursingHomePatientRepository.saveAndFlush(nursingHomePatient);

        int databaseSizeBeforeUpdate = nursingHomePatientRepository.findAll().size();

        // Update the nursingHomePatient using partial update
        NursingHomePatient partialUpdatedNursingHomePatient = new NursingHomePatient();
        partialUpdatedNursingHomePatient.setId(nursingHomePatient.getId());

        restNursingHomePatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNursingHomePatient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNursingHomePatient))
            )
            .andExpect(status().isOk());

        // Validate the NursingHomePatient in the database
        List<NursingHomePatient> nursingHomePatientList = nursingHomePatientRepository.findAll();
        assertThat(nursingHomePatientList).hasSize(databaseSizeBeforeUpdate);
        NursingHomePatient testNursingHomePatient = nursingHomePatientList.get(nursingHomePatientList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdateNursingHomePatientWithPatch() throws Exception {
        // Initialize the database
        nursingHomePatientRepository.saveAndFlush(nursingHomePatient);

        int databaseSizeBeforeUpdate = nursingHomePatientRepository.findAll().size();

        // Update the nursingHomePatient using partial update
        NursingHomePatient partialUpdatedNursingHomePatient = new NursingHomePatient();
        partialUpdatedNursingHomePatient.setId(nursingHomePatient.getId());

        restNursingHomePatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNursingHomePatient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNursingHomePatient))
            )
            .andExpect(status().isOk());

        // Validate the NursingHomePatient in the database
        List<NursingHomePatient> nursingHomePatientList = nursingHomePatientRepository.findAll();
        assertThat(nursingHomePatientList).hasSize(databaseSizeBeforeUpdate);
        NursingHomePatient testNursingHomePatient = nursingHomePatientList.get(nursingHomePatientList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingNursingHomePatient() throws Exception {
        int databaseSizeBeforeUpdate = nursingHomePatientRepository.findAll().size();
        nursingHomePatient.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNursingHomePatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nursingHomePatient.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nursingHomePatient))
            )
            .andExpect(status().isBadRequest());

        // Validate the NursingHomePatient in the database
        List<NursingHomePatient> nursingHomePatientList = nursingHomePatientRepository.findAll();
        assertThat(nursingHomePatientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNursingHomePatient() throws Exception {
        int databaseSizeBeforeUpdate = nursingHomePatientRepository.findAll().size();
        nursingHomePatient.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNursingHomePatientMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nursingHomePatient))
            )
            .andExpect(status().isBadRequest());

        // Validate the NursingHomePatient in the database
        List<NursingHomePatient> nursingHomePatientList = nursingHomePatientRepository.findAll();
        assertThat(nursingHomePatientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNursingHomePatient() throws Exception {
        int databaseSizeBeforeUpdate = nursingHomePatientRepository.findAll().size();
        nursingHomePatient.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNursingHomePatientMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nursingHomePatient))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NursingHomePatient in the database
        List<NursingHomePatient> nursingHomePatientList = nursingHomePatientRepository.findAll();
        assertThat(nursingHomePatientList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNursingHomePatient() throws Exception {
        // Initialize the database
        nursingHomePatientRepository.saveAndFlush(nursingHomePatient);

        int databaseSizeBeforeDelete = nursingHomePatientRepository.findAll().size();

        // Delete the nursingHomePatient
        restNursingHomePatientMockMvc
            .perform(delete(ENTITY_API_URL_ID, nursingHomePatient.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NursingHomePatient> nursingHomePatientList = nursingHomePatientRepository.findAll();
        assertThat(nursingHomePatientList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
