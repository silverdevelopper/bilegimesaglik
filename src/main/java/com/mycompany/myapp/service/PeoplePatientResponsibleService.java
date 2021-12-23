package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.PeoplePatientResponsible;
import com.mycompany.myapp.repository.PeoplePatientResponsibleRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link PeoplePatientResponsible}.
 */
@Service
@Transactional
public class PeoplePatientResponsibleService {

    private final Logger log = LoggerFactory.getLogger(PeoplePatientResponsibleService.class);

    private final PeoplePatientResponsibleRepository peoplePatientResponsibleRepository;

    public PeoplePatientResponsibleService(PeoplePatientResponsibleRepository peoplePatientResponsibleRepository) {
        this.peoplePatientResponsibleRepository = peoplePatientResponsibleRepository;
    }

    /**
     * Save a peoplePatientResponsible.
     *
     * @param peoplePatientResponsible the entity to save.
     * @return the persisted entity.
     */
    public PeoplePatientResponsible save(PeoplePatientResponsible peoplePatientResponsible) {
        log.debug("Request to save PeoplePatientResponsible : {}", peoplePatientResponsible);
        return peoplePatientResponsibleRepository.save(peoplePatientResponsible);
    }

    /**
     * Partially update a peoplePatientResponsible.
     *
     * @param peoplePatientResponsible the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<PeoplePatientResponsible> partialUpdate(PeoplePatientResponsible peoplePatientResponsible) {
        log.debug("Request to partially update PeoplePatientResponsible : {}", peoplePatientResponsible);

        return peoplePatientResponsibleRepository
            .findById(peoplePatientResponsible.getId())
            .map(existingPeoplePatientResponsible -> {
                return existingPeoplePatientResponsible;
            })
            .map(peoplePatientResponsibleRepository::save);
    }

    /**
     * Get all the peoplePatientResponsibles.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<PeoplePatientResponsible> findAll(Pageable pageable) {
        log.debug("Request to get all PeoplePatientResponsibles");
        return peoplePatientResponsibleRepository.findAll(pageable);
    }

    /**
     * Get one peoplePatientResponsible by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<PeoplePatientResponsible> findOne(Long id) {
        log.debug("Request to get PeoplePatientResponsible : {}", id);
        return peoplePatientResponsibleRepository.findById(id);
    }

    /**
     * Delete the peoplePatientResponsible by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete PeoplePatientResponsible : {}", id);
        peoplePatientResponsibleRepository.deleteById(id);
    }
}
