package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.People;
import com.mycompany.myapp.repository.PeopleRepository;
import com.mycompany.myapp.service.PeopleService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.People}.
 */
@RestController
@RequestMapping("/api")
public class PeopleResource {

    private final Logger log = LoggerFactory.getLogger(PeopleResource.class);

    private static final String ENTITY_NAME = "people";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PeopleService peopleService;

    private final PeopleRepository peopleRepository;

    public PeopleResource(PeopleService peopleService, PeopleRepository peopleRepository) {
        this.peopleService = peopleService;
        this.peopleRepository = peopleRepository;
    }

    /**
     * {@code POST  /people} : Create a new people.
     *
     * @param people the people to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new people, or with status {@code 400 (Bad Request)} if the people has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/people")
    public ResponseEntity<People> createPeople(@RequestBody People people) throws URISyntaxException {
        log.debug("REST request to save People : {}", people);
        if (people.getId() != null) {
            throw new BadRequestAlertException("A new people cannot already have an ID", ENTITY_NAME, "idexists");
        }
        People result = peopleService.save(people);
        return ResponseEntity
            .created(new URI("/api/people/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /people/:id} : Updates an existing people.
     *
     * @param id the id of the people to save.
     * @param people the people to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated people,
     * or with status {@code 400 (Bad Request)} if the people is not valid,
     * or with status {@code 500 (Internal Server Error)} if the people couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/people/{id}")
    public ResponseEntity<People> updatePeople(@PathVariable(value = "id", required = false) final Long id, @RequestBody People people)
        throws URISyntaxException {
        log.debug("REST request to update People : {}, {}", id, people);
        if (people.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, people.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!peopleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        People result = peopleService.save(people);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, people.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /people/:id} : Partial updates given fields of an existing people, field will ignore if it is null
     *
     * @param id the id of the people to save.
     * @param people the people to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated people,
     * or with status {@code 400 (Bad Request)} if the people is not valid,
     * or with status {@code 404 (Not Found)} if the people is not found,
     * or with status {@code 500 (Internal Server Error)} if the people couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/people/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<People> partialUpdatePeople(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody People people
    ) throws URISyntaxException {
        log.debug("REST request to partial update People partially : {}, {}", id, people);
        if (people.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, people.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!peopleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<People> result = peopleService.partialUpdate(people);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, people.getId().toString())
        );
    }

    /**
     * {@code GET  /people} : get all the people.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of people in body.
     */
    @GetMapping("/people")
    public ResponseEntity<List<People>> getAllPeople(Pageable pageable) {
        log.debug("REST request to get a page of People");
        Page<People> page = peopleService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /people/:id} : get the "id" people.
     *
     * @param id the id of the people to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the people, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/people/{id}")
    public ResponseEntity<People> getPeople(@PathVariable Long id) {
        log.debug("REST request to get People : {}", id);
        Optional<People> people = peopleService.findOne(id);
        return ResponseUtil.wrapOrNotFound(people);
    }

    /**
     * {@code DELETE  /people/:id} : delete the "id" people.
     *
     * @param id the id of the people to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/people/{id}")
    public ResponseEntity<Void> deletePeople(@PathVariable Long id) {
        log.debug("REST request to delete People : {}", id);
        peopleService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
