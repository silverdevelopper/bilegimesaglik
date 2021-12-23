package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.NursingHome;
import com.mycompany.myapp.repository.NursingHomeRepository;
import com.mycompany.myapp.service.NursingHomeService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.NursingHome}.
 */
@RestController
@RequestMapping("/api")
public class NursingHomeResource {

    private final Logger log = LoggerFactory.getLogger(NursingHomeResource.class);

    private static final String ENTITY_NAME = "nursingHome";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NursingHomeService nursingHomeService;

    private final NursingHomeRepository nursingHomeRepository;

    public NursingHomeResource(NursingHomeService nursingHomeService, NursingHomeRepository nursingHomeRepository) {
        this.nursingHomeService = nursingHomeService;
        this.nursingHomeRepository = nursingHomeRepository;
    }

    /**
     * {@code POST  /nursing-homes} : Create a new nursingHome.
     *
     * @param nursingHome the nursingHome to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nursingHome, or with status {@code 400 (Bad Request)} if the nursingHome has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/nursing-homes")
    public ResponseEntity<NursingHome> createNursingHome(@RequestBody NursingHome nursingHome) throws URISyntaxException {
        log.debug("REST request to save NursingHome : {}", nursingHome);
        if (nursingHome.getId() != null) {
            throw new BadRequestAlertException("A new nursingHome cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NursingHome result = nursingHomeService.save(nursingHome);
        return ResponseEntity
            .created(new URI("/api/nursing-homes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /nursing-homes/:id} : Updates an existing nursingHome.
     *
     * @param id the id of the nursingHome to save.
     * @param nursingHome the nursingHome to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nursingHome,
     * or with status {@code 400 (Bad Request)} if the nursingHome is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nursingHome couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/nursing-homes/{id}")
    public ResponseEntity<NursingHome> updateNursingHome(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody NursingHome nursingHome
    ) throws URISyntaxException {
        log.debug("REST request to update NursingHome : {}, {}", id, nursingHome);
        if (nursingHome.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nursingHome.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nursingHomeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NursingHome result = nursingHomeService.save(nursingHome);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nursingHome.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /nursing-homes/:id} : Partial updates given fields of an existing nursingHome, field will ignore if it is null
     *
     * @param id the id of the nursingHome to save.
     * @param nursingHome the nursingHome to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nursingHome,
     * or with status {@code 400 (Bad Request)} if the nursingHome is not valid,
     * or with status {@code 404 (Not Found)} if the nursingHome is not found,
     * or with status {@code 500 (Internal Server Error)} if the nursingHome couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/nursing-homes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NursingHome> partialUpdateNursingHome(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody NursingHome nursingHome
    ) throws URISyntaxException {
        log.debug("REST request to partial update NursingHome partially : {}, {}", id, nursingHome);
        if (nursingHome.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nursingHome.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nursingHomeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NursingHome> result = nursingHomeService.partialUpdate(nursingHome);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nursingHome.getId().toString())
        );
    }

    /**
     * {@code GET  /nursing-homes} : get all the nursingHomes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nursingHomes in body.
     */
    @GetMapping("/nursing-homes")
    public ResponseEntity<List<NursingHome>> getAllNursingHomes(Pageable pageable) {
        log.debug("REST request to get a page of NursingHomes");
        Page<NursingHome> page = nursingHomeService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /nursing-homes/:id} : get the "id" nursingHome.
     *
     * @param id the id of the nursingHome to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nursingHome, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/nursing-homes/{id}")
    public ResponseEntity<NursingHome> getNursingHome(@PathVariable Long id) {
        log.debug("REST request to get NursingHome : {}", id);
        Optional<NursingHome> nursingHome = nursingHomeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nursingHome);
    }

    /**
     * {@code DELETE  /nursing-homes/:id} : delete the "id" nursingHome.
     *
     * @param id the id of the nursingHome to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/nursing-homes/{id}")
    public ResponseEntity<Void> deleteNursingHome(@PathVariable Long id) {
        log.debug("REST request to delete NursingHome : {}", id);
        nursingHomeService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
