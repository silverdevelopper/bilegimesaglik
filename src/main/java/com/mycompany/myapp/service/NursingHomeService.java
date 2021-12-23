package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.NursingHome;
import com.mycompany.myapp.repository.NursingHomeRepository;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link NursingHome}.
 */
@Service
@Transactional
public class NursingHomeService {

    private final Logger log = LoggerFactory.getLogger(NursingHomeService.class);

    private final NursingHomeRepository nursingHomeRepository;

    public NursingHomeService(NursingHomeRepository nursingHomeRepository) {
        this.nursingHomeRepository = nursingHomeRepository;
    }

    /**
     * Save a nursingHome.
     *
     * @param nursingHome the entity to save.
     * @return the persisted entity.
     */
    public NursingHome save(NursingHome nursingHome) {
        log.debug("Request to save NursingHome : {}", nursingHome);
        return nursingHomeRepository.save(nursingHome);
    }

    /**
     * Partially update a nursingHome.
     *
     * @param nursingHome the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<NursingHome> partialUpdate(NursingHome nursingHome) {
        log.debug("Request to partially update NursingHome : {}", nursingHome);

        return nursingHomeRepository
            .findById(nursingHome.getId())
            .map(existingNursingHome -> {
                if (nursingHome.getName() != null) {
                    existingNursingHome.setName(nursingHome.getName());
                }
                if (nursingHome.getStreetAddress() != null) {
                    existingNursingHome.setStreetAddress(nursingHome.getStreetAddress());
                }
                if (nursingHome.getPostalCode() != null) {
                    existingNursingHome.setPostalCode(nursingHome.getPostalCode());
                }
                if (nursingHome.getCity() != null) {
                    existingNursingHome.setCity(nursingHome.getCity());
                }
                if (nursingHome.getCountry() != null) {
                    existingNursingHome.setCountry(nursingHome.getCountry());
                }

                return existingNursingHome;
            })
            .map(nursingHomeRepository::save);
    }

    /**
     * Get all the nursingHomes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<NursingHome> findAll(Pageable pageable) {
        log.debug("Request to get all NursingHomes");
        return nursingHomeRepository.findAll(pageable);
    }

    /**
     * Get one nursingHome by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<NursingHome> findOne(Long id) {
        log.debug("Request to get NursingHome : {}", id);
        return nursingHomeRepository.findById(id);
    }

    /**
     * Delete the nursingHome by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete NursingHome : {}", id);
        nursingHomeRepository.deleteById(id);
    }
}
