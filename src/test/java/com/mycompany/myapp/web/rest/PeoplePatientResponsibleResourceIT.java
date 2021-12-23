package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.PeoplePatientResponsible;
import com.mycompany.myapp.repository.PeoplePatientResponsibleRepository;
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
 * Integration tests for the {@link PeoplePatientResponsibleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PeoplePatientResponsibleResourceIT {

    private static final String ENTITY_API_URL = "/api/people-patient-responsibles";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PeoplePatientResponsibleRepository peoplePatientResponsibleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPeoplePatientResponsibleMockMvc;

    private PeoplePatientResponsible peoplePatientResponsible;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PeoplePatientResponsible createEntity(EntityManager em) {
        PeoplePatientResponsible peoplePatientResponsible = new PeoplePatientResponsible();
        return peoplePatientResponsible;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PeoplePatientResponsible createUpdatedEntity(EntityManager em) {
        PeoplePatientResponsible peoplePatientResponsible = new PeoplePatientResponsible();
        return peoplePatientResponsible;
    }

    @BeforeEach
    public void initTest() {
        peoplePatientResponsible = createEntity(em);
    }

    @Test
    @Transactional
    void createPeoplePatientResponsible() throws Exception {
        int databaseSizeBeforeCreate = peoplePatientResponsibleRepository.findAll().size();
        // Create the PeoplePatientResponsible
        restPeoplePatientResponsibleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(peoplePatientResponsible))
            )
            .andExpect(status().isCreated());

        // Validate the PeoplePatientResponsible in the database
        List<PeoplePatientResponsible> peoplePatientResponsibleList = peoplePatientResponsibleRepository.findAll();
        assertThat(peoplePatientResponsibleList).hasSize(databaseSizeBeforeCreate + 1);
        PeoplePatientResponsible testPeoplePatientResponsible = peoplePatientResponsibleList.get(peoplePatientResponsibleList.size() - 1);
    }

    @Test
    @Transactional
    void createPeoplePatientResponsibleWithExistingId() throws Exception {
        // Create the PeoplePatientResponsible with an existing ID
        peoplePatientResponsible.setId(1L);

        int databaseSizeBeforeCreate = peoplePatientResponsibleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPeoplePatientResponsibleMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(peoplePatientResponsible))
            )
            .andExpect(status().isBadRequest());

        // Validate the PeoplePatientResponsible in the database
        List<PeoplePatientResponsible> peoplePatientResponsibleList = peoplePatientResponsibleRepository.findAll();
        assertThat(peoplePatientResponsibleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPeoplePatientResponsibles() throws Exception {
        // Initialize the database
        peoplePatientResponsibleRepository.saveAndFlush(peoplePatientResponsible);

        // Get all the peoplePatientResponsibleList
        restPeoplePatientResponsibleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(peoplePatientResponsible.getId().intValue())));
    }

    @Test
    @Transactional
    void getPeoplePatientResponsible() throws Exception {
        // Initialize the database
        peoplePatientResponsibleRepository.saveAndFlush(peoplePatientResponsible);

        // Get the peoplePatientResponsible
        restPeoplePatientResponsibleMockMvc
            .perform(get(ENTITY_API_URL_ID, peoplePatientResponsible.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(peoplePatientResponsible.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingPeoplePatientResponsible() throws Exception {
        // Get the peoplePatientResponsible
        restPeoplePatientResponsibleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPeoplePatientResponsible() throws Exception {
        // Initialize the database
        peoplePatientResponsibleRepository.saveAndFlush(peoplePatientResponsible);

        int databaseSizeBeforeUpdate = peoplePatientResponsibleRepository.findAll().size();

        // Update the peoplePatientResponsible
        PeoplePatientResponsible updatedPeoplePatientResponsible = peoplePatientResponsibleRepository
            .findById(peoplePatientResponsible.getId())
            .get();
        // Disconnect from session so that the updates on updatedPeoplePatientResponsible are not directly saved in db
        em.detach(updatedPeoplePatientResponsible);

        restPeoplePatientResponsibleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPeoplePatientResponsible.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPeoplePatientResponsible))
            )
            .andExpect(status().isOk());

        // Validate the PeoplePatientResponsible in the database
        List<PeoplePatientResponsible> peoplePatientResponsibleList = peoplePatientResponsibleRepository.findAll();
        assertThat(peoplePatientResponsibleList).hasSize(databaseSizeBeforeUpdate);
        PeoplePatientResponsible testPeoplePatientResponsible = peoplePatientResponsibleList.get(peoplePatientResponsibleList.size() - 1);
    }

    @Test
    @Transactional
    void putNonExistingPeoplePatientResponsible() throws Exception {
        int databaseSizeBeforeUpdate = peoplePatientResponsibleRepository.findAll().size();
        peoplePatientResponsible.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeoplePatientResponsibleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, peoplePatientResponsible.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(peoplePatientResponsible))
            )
            .andExpect(status().isBadRequest());

        // Validate the PeoplePatientResponsible in the database
        List<PeoplePatientResponsible> peoplePatientResponsibleList = peoplePatientResponsibleRepository.findAll();
        assertThat(peoplePatientResponsibleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPeoplePatientResponsible() throws Exception {
        int databaseSizeBeforeUpdate = peoplePatientResponsibleRepository.findAll().size();
        peoplePatientResponsible.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeoplePatientResponsibleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(peoplePatientResponsible))
            )
            .andExpect(status().isBadRequest());

        // Validate the PeoplePatientResponsible in the database
        List<PeoplePatientResponsible> peoplePatientResponsibleList = peoplePatientResponsibleRepository.findAll();
        assertThat(peoplePatientResponsibleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPeoplePatientResponsible() throws Exception {
        int databaseSizeBeforeUpdate = peoplePatientResponsibleRepository.findAll().size();
        peoplePatientResponsible.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeoplePatientResponsibleMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(peoplePatientResponsible))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PeoplePatientResponsible in the database
        List<PeoplePatientResponsible> peoplePatientResponsibleList = peoplePatientResponsibleRepository.findAll();
        assertThat(peoplePatientResponsibleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePeoplePatientResponsibleWithPatch() throws Exception {
        // Initialize the database
        peoplePatientResponsibleRepository.saveAndFlush(peoplePatientResponsible);

        int databaseSizeBeforeUpdate = peoplePatientResponsibleRepository.findAll().size();

        // Update the peoplePatientResponsible using partial update
        PeoplePatientResponsible partialUpdatedPeoplePatientResponsible = new PeoplePatientResponsible();
        partialUpdatedPeoplePatientResponsible.setId(peoplePatientResponsible.getId());

        restPeoplePatientResponsibleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPeoplePatientResponsible.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPeoplePatientResponsible))
            )
            .andExpect(status().isOk());

        // Validate the PeoplePatientResponsible in the database
        List<PeoplePatientResponsible> peoplePatientResponsibleList = peoplePatientResponsibleRepository.findAll();
        assertThat(peoplePatientResponsibleList).hasSize(databaseSizeBeforeUpdate);
        PeoplePatientResponsible testPeoplePatientResponsible = peoplePatientResponsibleList.get(peoplePatientResponsibleList.size() - 1);
    }

    @Test
    @Transactional
    void fullUpdatePeoplePatientResponsibleWithPatch() throws Exception {
        // Initialize the database
        peoplePatientResponsibleRepository.saveAndFlush(peoplePatientResponsible);

        int databaseSizeBeforeUpdate = peoplePatientResponsibleRepository.findAll().size();

        // Update the peoplePatientResponsible using partial update
        PeoplePatientResponsible partialUpdatedPeoplePatientResponsible = new PeoplePatientResponsible();
        partialUpdatedPeoplePatientResponsible.setId(peoplePatientResponsible.getId());

        restPeoplePatientResponsibleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPeoplePatientResponsible.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPeoplePatientResponsible))
            )
            .andExpect(status().isOk());

        // Validate the PeoplePatientResponsible in the database
        List<PeoplePatientResponsible> peoplePatientResponsibleList = peoplePatientResponsibleRepository.findAll();
        assertThat(peoplePatientResponsibleList).hasSize(databaseSizeBeforeUpdate);
        PeoplePatientResponsible testPeoplePatientResponsible = peoplePatientResponsibleList.get(peoplePatientResponsibleList.size() - 1);
    }

    @Test
    @Transactional
    void patchNonExistingPeoplePatientResponsible() throws Exception {
        int databaseSizeBeforeUpdate = peoplePatientResponsibleRepository.findAll().size();
        peoplePatientResponsible.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPeoplePatientResponsibleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, peoplePatientResponsible.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(peoplePatientResponsible))
            )
            .andExpect(status().isBadRequest());

        // Validate the PeoplePatientResponsible in the database
        List<PeoplePatientResponsible> peoplePatientResponsibleList = peoplePatientResponsibleRepository.findAll();
        assertThat(peoplePatientResponsibleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPeoplePatientResponsible() throws Exception {
        int databaseSizeBeforeUpdate = peoplePatientResponsibleRepository.findAll().size();
        peoplePatientResponsible.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeoplePatientResponsibleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(peoplePatientResponsible))
            )
            .andExpect(status().isBadRequest());

        // Validate the PeoplePatientResponsible in the database
        List<PeoplePatientResponsible> peoplePatientResponsibleList = peoplePatientResponsibleRepository.findAll();
        assertThat(peoplePatientResponsibleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPeoplePatientResponsible() throws Exception {
        int databaseSizeBeforeUpdate = peoplePatientResponsibleRepository.findAll().size();
        peoplePatientResponsible.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPeoplePatientResponsibleMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(peoplePatientResponsible))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PeoplePatientResponsible in the database
        List<PeoplePatientResponsible> peoplePatientResponsibleList = peoplePatientResponsibleRepository.findAll();
        assertThat(peoplePatientResponsibleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePeoplePatientResponsible() throws Exception {
        // Initialize the database
        peoplePatientResponsibleRepository.saveAndFlush(peoplePatientResponsible);

        int databaseSizeBeforeDelete = peoplePatientResponsibleRepository.findAll().size();

        // Delete the peoplePatientResponsible
        restPeoplePatientResponsibleMockMvc
            .perform(delete(ENTITY_API_URL_ID, peoplePatientResponsible.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PeoplePatientResponsible> peoplePatientResponsibleList = peoplePatientResponsibleRepository.findAll();
        assertThat(peoplePatientResponsibleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
