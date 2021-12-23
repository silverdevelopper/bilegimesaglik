package com.mycompany.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.NursingHome;
import com.mycompany.myapp.repository.NursingHomeRepository;
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
 * Integration tests for the {@link NursingHomeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class NursingHomeResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_STREET_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_STREET_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_POSTAL_CODE = "AAAAAAAAAA";
    private static final String UPDATED_POSTAL_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_CITY = "AAAAAAAAAA";
    private static final String UPDATED_CITY = "BBBBBBBBBB";

    private static final String DEFAULT_COUNTRY = "AAAAAAAAAA";
    private static final String UPDATED_COUNTRY = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/nursing-homes";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private NursingHomeRepository nursingHomeRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restNursingHomeMockMvc;

    private NursingHome nursingHome;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NursingHome createEntity(EntityManager em) {
        NursingHome nursingHome = new NursingHome()
            .name(DEFAULT_NAME)
            .streetAddress(DEFAULT_STREET_ADDRESS)
            .postalCode(DEFAULT_POSTAL_CODE)
            .city(DEFAULT_CITY)
            .country(DEFAULT_COUNTRY);
        return nursingHome;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static NursingHome createUpdatedEntity(EntityManager em) {
        NursingHome nursingHome = new NursingHome()
            .name(UPDATED_NAME)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY);
        return nursingHome;
    }

    @BeforeEach
    public void initTest() {
        nursingHome = createEntity(em);
    }

    @Test
    @Transactional
    void createNursingHome() throws Exception {
        int databaseSizeBeforeCreate = nursingHomeRepository.findAll().size();
        // Create the NursingHome
        restNursingHomeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nursingHome)))
            .andExpect(status().isCreated());

        // Validate the NursingHome in the database
        List<NursingHome> nursingHomeList = nursingHomeRepository.findAll();
        assertThat(nursingHomeList).hasSize(databaseSizeBeforeCreate + 1);
        NursingHome testNursingHome = nursingHomeList.get(nursingHomeList.size() - 1);
        assertThat(testNursingHome.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testNursingHome.getStreetAddress()).isEqualTo(DEFAULT_STREET_ADDRESS);
        assertThat(testNursingHome.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testNursingHome.getCity()).isEqualTo(DEFAULT_CITY);
        assertThat(testNursingHome.getCountry()).isEqualTo(DEFAULT_COUNTRY);
    }

    @Test
    @Transactional
    void createNursingHomeWithExistingId() throws Exception {
        // Create the NursingHome with an existing ID
        nursingHome.setId(1L);

        int databaseSizeBeforeCreate = nursingHomeRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restNursingHomeMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nursingHome)))
            .andExpect(status().isBadRequest());

        // Validate the NursingHome in the database
        List<NursingHome> nursingHomeList = nursingHomeRepository.findAll();
        assertThat(nursingHomeList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllNursingHomes() throws Exception {
        // Initialize the database
        nursingHomeRepository.saveAndFlush(nursingHome);

        // Get all the nursingHomeList
        restNursingHomeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(nursingHome.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].streetAddress").value(hasItem(DEFAULT_STREET_ADDRESS)))
            .andExpect(jsonPath("$.[*].postalCode").value(hasItem(DEFAULT_POSTAL_CODE)))
            .andExpect(jsonPath("$.[*].city").value(hasItem(DEFAULT_CITY)))
            .andExpect(jsonPath("$.[*].country").value(hasItem(DEFAULT_COUNTRY)));
    }

    @Test
    @Transactional
    void getNursingHome() throws Exception {
        // Initialize the database
        nursingHomeRepository.saveAndFlush(nursingHome);

        // Get the nursingHome
        restNursingHomeMockMvc
            .perform(get(ENTITY_API_URL_ID, nursingHome.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(nursingHome.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.streetAddress").value(DEFAULT_STREET_ADDRESS))
            .andExpect(jsonPath("$.postalCode").value(DEFAULT_POSTAL_CODE))
            .andExpect(jsonPath("$.city").value(DEFAULT_CITY))
            .andExpect(jsonPath("$.country").value(DEFAULT_COUNTRY));
    }

    @Test
    @Transactional
    void getNonExistingNursingHome() throws Exception {
        // Get the nursingHome
        restNursingHomeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewNursingHome() throws Exception {
        // Initialize the database
        nursingHomeRepository.saveAndFlush(nursingHome);

        int databaseSizeBeforeUpdate = nursingHomeRepository.findAll().size();

        // Update the nursingHome
        NursingHome updatedNursingHome = nursingHomeRepository.findById(nursingHome.getId()).get();
        // Disconnect from session so that the updates on updatedNursingHome are not directly saved in db
        em.detach(updatedNursingHome);
        updatedNursingHome
            .name(UPDATED_NAME)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY);

        restNursingHomeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedNursingHome.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedNursingHome))
            )
            .andExpect(status().isOk());

        // Validate the NursingHome in the database
        List<NursingHome> nursingHomeList = nursingHomeRepository.findAll();
        assertThat(nursingHomeList).hasSize(databaseSizeBeforeUpdate);
        NursingHome testNursingHome = nursingHomeList.get(nursingHomeList.size() - 1);
        assertThat(testNursingHome.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testNursingHome.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testNursingHome.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testNursingHome.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testNursingHome.getCountry()).isEqualTo(UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void putNonExistingNursingHome() throws Exception {
        int databaseSizeBeforeUpdate = nursingHomeRepository.findAll().size();
        nursingHome.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNursingHomeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, nursingHome.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nursingHome))
            )
            .andExpect(status().isBadRequest());

        // Validate the NursingHome in the database
        List<NursingHome> nursingHomeList = nursingHomeRepository.findAll();
        assertThat(nursingHomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchNursingHome() throws Exception {
        int databaseSizeBeforeUpdate = nursingHomeRepository.findAll().size();
        nursingHome.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNursingHomeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(nursingHome))
            )
            .andExpect(status().isBadRequest());

        // Validate the NursingHome in the database
        List<NursingHome> nursingHomeList = nursingHomeRepository.findAll();
        assertThat(nursingHomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamNursingHome() throws Exception {
        int databaseSizeBeforeUpdate = nursingHomeRepository.findAll().size();
        nursingHome.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNursingHomeMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(nursingHome)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the NursingHome in the database
        List<NursingHome> nursingHomeList = nursingHomeRepository.findAll();
        assertThat(nursingHomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateNursingHomeWithPatch() throws Exception {
        // Initialize the database
        nursingHomeRepository.saveAndFlush(nursingHome);

        int databaseSizeBeforeUpdate = nursingHomeRepository.findAll().size();

        // Update the nursingHome using partial update
        NursingHome partialUpdatedNursingHome = new NursingHome();
        partialUpdatedNursingHome.setId(nursingHome.getId());

        partialUpdatedNursingHome.city(UPDATED_CITY).country(UPDATED_COUNTRY);

        restNursingHomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNursingHome.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNursingHome))
            )
            .andExpect(status().isOk());

        // Validate the NursingHome in the database
        List<NursingHome> nursingHomeList = nursingHomeRepository.findAll();
        assertThat(nursingHomeList).hasSize(databaseSizeBeforeUpdate);
        NursingHome testNursingHome = nursingHomeList.get(nursingHomeList.size() - 1);
        assertThat(testNursingHome.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testNursingHome.getStreetAddress()).isEqualTo(DEFAULT_STREET_ADDRESS);
        assertThat(testNursingHome.getPostalCode()).isEqualTo(DEFAULT_POSTAL_CODE);
        assertThat(testNursingHome.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testNursingHome.getCountry()).isEqualTo(UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void fullUpdateNursingHomeWithPatch() throws Exception {
        // Initialize the database
        nursingHomeRepository.saveAndFlush(nursingHome);

        int databaseSizeBeforeUpdate = nursingHomeRepository.findAll().size();

        // Update the nursingHome using partial update
        NursingHome partialUpdatedNursingHome = new NursingHome();
        partialUpdatedNursingHome.setId(nursingHome.getId());

        partialUpdatedNursingHome
            .name(UPDATED_NAME)
            .streetAddress(UPDATED_STREET_ADDRESS)
            .postalCode(UPDATED_POSTAL_CODE)
            .city(UPDATED_CITY)
            .country(UPDATED_COUNTRY);

        restNursingHomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedNursingHome.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedNursingHome))
            )
            .andExpect(status().isOk());

        // Validate the NursingHome in the database
        List<NursingHome> nursingHomeList = nursingHomeRepository.findAll();
        assertThat(nursingHomeList).hasSize(databaseSizeBeforeUpdate);
        NursingHome testNursingHome = nursingHomeList.get(nursingHomeList.size() - 1);
        assertThat(testNursingHome.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testNursingHome.getStreetAddress()).isEqualTo(UPDATED_STREET_ADDRESS);
        assertThat(testNursingHome.getPostalCode()).isEqualTo(UPDATED_POSTAL_CODE);
        assertThat(testNursingHome.getCity()).isEqualTo(UPDATED_CITY);
        assertThat(testNursingHome.getCountry()).isEqualTo(UPDATED_COUNTRY);
    }

    @Test
    @Transactional
    void patchNonExistingNursingHome() throws Exception {
        int databaseSizeBeforeUpdate = nursingHomeRepository.findAll().size();
        nursingHome.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restNursingHomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, nursingHome.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nursingHome))
            )
            .andExpect(status().isBadRequest());

        // Validate the NursingHome in the database
        List<NursingHome> nursingHomeList = nursingHomeRepository.findAll();
        assertThat(nursingHomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchNursingHome() throws Exception {
        int databaseSizeBeforeUpdate = nursingHomeRepository.findAll().size();
        nursingHome.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNursingHomeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(nursingHome))
            )
            .andExpect(status().isBadRequest());

        // Validate the NursingHome in the database
        List<NursingHome> nursingHomeList = nursingHomeRepository.findAll();
        assertThat(nursingHomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamNursingHome() throws Exception {
        int databaseSizeBeforeUpdate = nursingHomeRepository.findAll().size();
        nursingHome.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restNursingHomeMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(nursingHome))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the NursingHome in the database
        List<NursingHome> nursingHomeList = nursingHomeRepository.findAll();
        assertThat(nursingHomeList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteNursingHome() throws Exception {
        // Initialize the database
        nursingHomeRepository.saveAndFlush(nursingHome);

        int databaseSizeBeforeDelete = nursingHomeRepository.findAll().size();

        // Delete the nursingHome
        restNursingHomeMockMvc
            .perform(delete(ENTITY_API_URL_ID, nursingHome.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<NursingHome> nursingHomeList = nursingHomeRepository.findAll();
        assertThat(nursingHomeList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
