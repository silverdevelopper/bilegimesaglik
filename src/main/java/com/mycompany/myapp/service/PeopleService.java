package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.People;
import com.mycompany.myapp.repository.PeopleRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link People}.
 */
@Service
@Transactional
public class PeopleService {

    private final Logger log = LoggerFactory.getLogger(PeopleService.class);

    private final PeopleRepository peopleRepository;

    public PeopleService(PeopleRepository peopleRepository) {
        this.peopleRepository = peopleRepository;
    }

    /**
     * Save a people.
     *
     * @param people the entity to save.
     * @return the persisted entity.
     */
    public People save(People people) {
        log.debug("Request to save People : {}", people);
        return peopleRepository.save(people);
    }

    /**
     * Partially update a people.
     *
     * @param people the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<People> partialUpdate(People people) {
        log.debug("Request to partially update People : {}", people);

        return peopleRepository
            .findById(people.getId())
            .map(existingPeople -> {
                if (people.getFirstName() != null) {
                    existingPeople.setFirstName(people.getFirstName());
                }
                if (people.getLastName() != null) {
                    existingPeople.setLastName(people.getLastName());
                }
                if (people.getEmail() != null) {
                    existingPeople.setEmail(people.getEmail());
                }
                if (people.getPhoneNumber() != null) {
                    existingPeople.setPhoneNumber(people.getPhoneNumber());
                }
                if (people.getBirthDate() != null) {
                    existingPeople.setBirthDate(people.getBirthDate());
                }

                return existingPeople;
            })
            .map(peopleRepository::save);
    }

    /**
     * Get all the people.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<People> findAll(Pageable pageable) {
        log.debug("Request to get all People");
        return peopleRepository.findAll(pageable);
    }

    /**
     * Get one people by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<People> findOne(Long id) {
        log.debug("Request to get People : {}", id);
        return peopleRepository.findById(id);
    }

    /**
     * Delete the people by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete People : {}", id);
        peopleRepository.deleteById(id);
    }
}
