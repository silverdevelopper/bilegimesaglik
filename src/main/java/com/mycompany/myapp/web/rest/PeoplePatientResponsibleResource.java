package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.PeoplePatientResponsible;
import com.mycompany.myapp.repository.PeoplePatientResponsibleRepository;
import com.mycompany.myapp.service.PeoplePatientResponsibleService;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.PeoplePatientResponsible}.
 */
@RestController
@RequestMapping("/api")
public class PeoplePatientResponsibleResource {

    private final Logger log = LoggerFactory.getLogger(PeoplePatientResponsibleResource.class);

    private static final String ENTITY_NAME = "peoplePatientResponsible";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PeoplePatientResponsibleService peoplePatientResponsibleService;

    private final PeoplePatientResponsibleRepository peoplePatientResponsibleRepository;

    public PeoplePatientResponsibleResource(
        PeoplePatientResponsibleService peoplePatientResponsibleService,
        PeoplePatientResponsibleRepository peoplePatientResponsibleRepository
    ) {
        this.peoplePatientResponsibleService = peoplePatientResponsibleService;
        this.peoplePatientResponsibleRepository = peoplePatientResponsibleRepository;
    }

    /**
     * {@code POST  /people-patient-responsibles} : Create a new peoplePatientResponsible.
     *
     * @param peoplePatientResponsible the peoplePatientResponsible to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new peoplePatientResponsible, or with status {@code 400 (Bad Request)} if the peoplePatientResponsible has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/people-patient-responsibles")
    public ResponseEntity<PeoplePatientResponsible> createPeoplePatientResponsible(
        @RequestBody PeoplePatientResponsible peoplePatientResponsible
    ) throws URISyntaxException {
        log.debug("REST request to save PeoplePatientResponsible : {}", peoplePatientResponsible);
        if (peoplePatientResponsible.getId() != null) {
            throw new BadRequestAlertException("A new peoplePatientResponsible cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PeoplePatientResponsible result = peoplePatientResponsibleService.save(peoplePatientResponsible);
        return ResponseEntity
            .created(new URI("/api/people-patient-responsibles/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /people-patient-responsibles/:id} : Updates an existing peoplePatientResponsible.
     *
     * @param id the id of the peoplePatientResponsible to save.
     * @param peoplePatientResponsible the peoplePatientResponsible to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated peoplePatientResponsible,
     * or with status {@code 400 (Bad Request)} if the peoplePatientResponsible is not valid,
     * or with status {@code 500 (Internal Server Error)} if the peoplePatientResponsible couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/people-patient-responsibles/{id}")
    public ResponseEntity<PeoplePatientResponsible> updatePeoplePatientResponsible(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PeoplePatientResponsible peoplePatientResponsible
    ) throws URISyntaxException {
        log.debug("REST request to update PeoplePatientResponsible : {}, {}", id, peoplePatientResponsible);
        if (peoplePatientResponsible.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, peoplePatientResponsible.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!peoplePatientResponsibleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PeoplePatientResponsible result = peoplePatientResponsibleService.save(peoplePatientResponsible);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, peoplePatientResponsible.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /people-patient-responsibles/:id} : Partial updates given fields of an existing peoplePatientResponsible, field will ignore if it is null
     *
     * @param id the id of the peoplePatientResponsible to save.
     * @param peoplePatientResponsible the peoplePatientResponsible to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated peoplePatientResponsible,
     * or with status {@code 400 (Bad Request)} if the peoplePatientResponsible is not valid,
     * or with status {@code 404 (Not Found)} if the peoplePatientResponsible is not found,
     * or with status {@code 500 (Internal Server Error)} if the peoplePatientResponsible couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/people-patient-responsibles/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PeoplePatientResponsible> partialUpdatePeoplePatientResponsible(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PeoplePatientResponsible peoplePatientResponsible
    ) throws URISyntaxException {
        log.debug("REST request to partial update PeoplePatientResponsible partially : {}, {}", id, peoplePatientResponsible);
        if (peoplePatientResponsible.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, peoplePatientResponsible.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!peoplePatientResponsibleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PeoplePatientResponsible> result = peoplePatientResponsibleService.partialUpdate(peoplePatientResponsible);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, peoplePatientResponsible.getId().toString())
        );
    }

    /**
     * {@code GET  /people-patient-responsibles} : get all the peoplePatientResponsibles.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of peoplePatientResponsibles in body.
     */
    @GetMapping("/people-patient-responsibles")
    public ResponseEntity<List<PeoplePatientResponsible>> getAllPeoplePatientResponsibles(Pageable pageable) {
        log.debug("REST request to get a page of PeoplePatientResponsibles");
        Page<PeoplePatientResponsible> page = peoplePatientResponsibleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /people-patient-responsibles/:id} : get the "id" peoplePatientResponsible.
     *
     * @param id the id of the peoplePatientResponsible to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the peoplePatientResponsible, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/people-patient-responsibles/{id}")
    public ResponseEntity<PeoplePatientResponsible> getPeoplePatientResponsible(@PathVariable Long id) {
        log.debug("REST request to get PeoplePatientResponsible : {}", id);
        Optional<PeoplePatientResponsible> peoplePatientResponsible = peoplePatientResponsibleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(peoplePatientResponsible);
    }

    /**
     * {@code DELETE  /people-patient-responsibles/:id} : delete the "id" peoplePatientResponsible.
     *
     * @param id the id of the peoplePatientResponsible to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/people-patient-responsibles/{id}")
    public ResponseEntity<Void> deletePeoplePatientResponsible(@PathVariable Long id) {
        log.debug("REST request to delete PeoplePatientResponsible : {}", id);
        peoplePatientResponsibleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
