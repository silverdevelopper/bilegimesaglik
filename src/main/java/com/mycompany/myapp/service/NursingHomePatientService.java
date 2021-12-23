package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.NursingHomePatient;
import com.mycompany.myapp.repository.NursingHomePatientRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link NursingHomePatient}.
 */
@Service
@Transactional
public class NursingHomePatientService {

    private final Logger log = LoggerFactory.getLogger(NursingHomePatientService.class);

    private final NursingHomePatientRepository nursingHomePatientRepository;

    public NursingHomePatientService(NursingHomePatientRepository nursingHomePatientRepository) {
        this.nursingHomePatientRepository = nursingHomePatientRepository;
    }

    /**
     * Save a nursingHomePatient.
     *
     * @param nursingHomePatient the entity to save.
     * @return the persisted entity.
     */
    public NursingHomePatient save(NursingHomePatient nursingHomePatient) {
        log.debug("Request to save NursingHomePatient : {}", nursingHomePatient);
        return nursingHomePatientRepository.save(nursingHomePatient);
    }

    /**
     * Partially update a nursingHomePatient.
     *
     * @param nursingHomePatient the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NursingHomePatient> partialUpdate(NursingHomePatient nursingHomePatient) {
        log.debug("Request to partially update NursingHomePatient : {}", nursingHomePatient);

        return nursingHomePatientRepository
            .findById(nursingHomePatient.getId())
            .map(existingNursingHomePatient -> {
                return existingNursingHomePatient;
            })
            .map(nursingHomePatientRepository::save);
    }

    /**
     * Get all the nursingHomePatients.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<NursingHomePatient> findAll(Pageable pageable) {
        log.debug("Request to get all NursingHomePatients");
        return nursingHomePatientRepository.findAll(pageable);
    }

    /**
     * Get one nursingHomePatient by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NursingHomePatient> findOne(Long id) {
        log.debug("Request to get NursingHomePatient : {}", id);
        return nursingHomePatientRepository.findById(id);
    }

    /**
     * Delete the nursingHomePatient by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete NursingHomePatient : {}", id);
        nursingHomePatientRepository.deleteById(id);
    }
}
