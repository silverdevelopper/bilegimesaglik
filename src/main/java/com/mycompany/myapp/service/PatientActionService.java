package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.PatientAction;
import com.mycompany.myapp.repository.PatientActionRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PatientAction}.
 */
@Service
@Transactional
public class PatientActionService {

    private final Logger log = LoggerFactory.getLogger(PatientActionService.class);

    private final PatientActionRepository patientActionRepository;

    public PatientActionService(PatientActionRepository patientActionRepository) {
        this.patientActionRepository = patientActionRepository;
    }

    /**
     * Save a patientAction.
     *
     * @param patientAction the entity to save.
     * @return the persisted entity.
     */
    public PatientAction save(PatientAction patientAction) {
        log.debug("Request to save PatientAction : {}", patientAction);
        return patientActionRepository.save(patientAction);
    }

    /**
     * Partially update a patientAction.
     *
     * @param patientAction the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PatientAction> partialUpdate(PatientAction patientAction) {
        log.debug("Request to partially update PatientAction : {}", patientAction);

        return patientActionRepository
            .findById(patientAction.getId())
            .map(existingPatientAction -> {
                if (patientAction.getStartDate() != null) {
                    existingPatientAction.setStartDate(patientAction.getStartDate());
                }
                if (patientAction.getEndDate() != null) {
                    existingPatientAction.setEndDate(patientAction.getEndDate());
                }
                if (patientAction.getActionDescription() != null) {
                    existingPatientAction.setActionDescription(patientAction.getActionDescription());
                }
                if (patientAction.getHealtstatus() != null) {
                    existingPatientAction.setHealtstatus(patientAction.getHealtstatus());
                }

                return existingPatientAction;
            })
            .map(patientActionRepository::save);
    }

    /**
     * Get all the patientActions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PatientAction> findAll(Pageable pageable) {
        log.debug("Request to get all PatientActions");
        return patientActionRepository.findAll(pageable);
    }

    /**
     * Get one patientAction by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PatientAction> findOne(Long id) {
        log.debug("Request to get PatientAction : {}", id);
        return patientActionRepository.findById(id);
    }

    /**
     * Delete the patientAction by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PatientAction : {}", id);
        patientActionRepository.deleteById(id);
    }
}
