package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.PatientTask;
import com.mycompany.myapp.repository.PatientTaskRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PatientTask}.
 */
@Service
@Transactional
public class PatientTaskService {

    private final Logger log = LoggerFactory.getLogger(PatientTaskService.class);

    private final PatientTaskRepository patientTaskRepository;

    public PatientTaskService(PatientTaskRepository patientTaskRepository) {
        this.patientTaskRepository = patientTaskRepository;
    }

    /**
     * Save a patientTask.
     *
     * @param patientTask the entity to save.
     * @return the persisted entity.
     */
    public PatientTask save(PatientTask patientTask) {
        log.debug("Request to save PatientTask : {}", patientTask);
        return patientTaskRepository.save(patientTask);
    }

    /**
     * Partially update a patientTask.
     *
     * @param patientTask the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PatientTask> partialUpdate(PatientTask patientTask) {
        log.debug("Request to partially update PatientTask : {}", patientTask);

        return patientTaskRepository
            .findById(patientTask.getId())
            .map(existingPatientTask -> {
                if (patientTask.getTitle() != null) {
                    existingPatientTask.setTitle(patientTask.getTitle());
                }
                if (patientTask.getDescription() != null) {
                    existingPatientTask.setDescription(patientTask.getDescription());
                }
                if (patientTask.getSchedule() != null) {
                    existingPatientTask.setSchedule(patientTask.getSchedule());
                }

                return existingPatientTask;
            })
            .map(patientTaskRepository::save);
    }

    /**
     * Get all the patientTasks.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PatientTask> findAll(Pageable pageable) {
        log.debug("Request to get all PatientTasks");
        return patientTaskRepository.findAll(pageable);
    }

    /**
     * Get one patientTask by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PatientTask> findOne(Long id) {
        log.debug("Request to get PatientTask : {}", id);
        return patientTaskRepository.findById(id);
    }

    /**
     * Delete the patientTask by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PatientTask : {}", id);
        patientTaskRepository.deleteById(id);
    }
}
