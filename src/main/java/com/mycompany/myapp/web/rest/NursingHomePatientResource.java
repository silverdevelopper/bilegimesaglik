package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.NursingHomePatient;
import com.mycompany.myapp.repository.NursingHomePatientRepository;
import com.mycompany.myapp.service.NursingHomePatientService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.NursingHomePatient}.
 */
@RestController
@RequestMapping("/api")
public class NursingHomePatientResource {

    private final Logger log = LoggerFactory.getLogger(NursingHomePatientResource.class);

    private static final String ENTITY_NAME = "nursingHomePatient";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final NursingHomePatientService nursingHomePatientService;

    private final NursingHomePatientRepository nursingHomePatientRepository;

    public NursingHomePatientResource(
        NursingHomePatientService nursingHomePatientService,
        NursingHomePatientRepository nursingHomePatientRepository
    ) {
        this.nursingHomePatientService = nursingHomePatientService;
        this.nursingHomePatientRepository = nursingHomePatientRepository;
    }

    /**
     * {@code POST  /nursing-home-patients} : Create a new nursingHomePatient.
     *
     * @param nursingHomePatient the nursingHomePatient to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new nursingHomePatient, or with status {@code 400 (Bad Request)} if the nursingHomePatient has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/nursing-home-patients")
    public ResponseEntity<NursingHomePatient> createNursingHomePatient(@RequestBody NursingHomePatient nursingHomePatient)
        throws URISyntaxException {
        log.debug("REST request to save NursingHomePatient : {}", nursingHomePatient);
        if (nursingHomePatient.getId() != null) {
            throw new BadRequestAlertException("A new nursingHomePatient cannot already have an ID", ENTITY_NAME, "idexists");
        }
        NursingHomePatient result = nursingHomePatientService.save(nursingHomePatient);
        return ResponseEntity
            .created(new URI("/api/nursing-home-patients/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /nursing-home-patients/:id} : Updates an existing nursingHomePatient.
     *
     * @param id the id of the nursingHomePatient to save.
     * @param nursingHomePatient the nursingHomePatient to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nursingHomePatient,
     * or with status {@code 400 (Bad Request)} if the nursingHomePatient is not valid,
     * or with status {@code 500 (Internal Server Error)} if the nursingHomePatient couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/nursing-home-patients/{id}")
    public ResponseEntity<NursingHomePatient> updateNursingHomePatient(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody NursingHomePatient nursingHomePatient
    ) throws URISyntaxException {
        log.debug("REST request to update NursingHomePatient : {}, {}", id, nursingHomePatient);
        if (nursingHomePatient.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nursingHomePatient.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nursingHomePatientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        NursingHomePatient result = nursingHomePatientService.save(nursingHomePatient);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nursingHomePatient.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /nursing-home-patients/:id} : Partial updates given fields of an existing nursingHomePatient, field will ignore if it is null
     *
     * @param id the id of the nursingHomePatient to save.
     * @param nursingHomePatient the nursingHomePatient to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated nursingHomePatient,
     * or with status {@code 400 (Bad Request)} if the nursingHomePatient is not valid,
     * or with status {@code 404 (Not Found)} if the nursingHomePatient is not found,
     * or with status {@code 500 (Internal Server Error)} if the nursingHomePatient couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/nursing-home-patients/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<NursingHomePatient> partialUpdateNursingHomePatient(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody NursingHomePatient nursingHomePatient
    ) throws URISyntaxException {
        log.debug("REST request to partial update NursingHomePatient partially : {}, {}", id, nursingHomePatient);
        if (nursingHomePatient.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, nursingHomePatient.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!nursingHomePatientRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<NursingHomePatient> result = nursingHomePatientService.partialUpdate(nursingHomePatient);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, nursingHomePatient.getId().toString())
        );
    }

    /**
     * {@code GET  /nursing-home-patients} : get all the nursingHomePatients.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of nursingHomePatients in body.
     */
    @GetMapping("/nursing-home-patients")
    public ResponseEntity<List<NursingHomePatient>> getAllNursingHomePatients(Pageable pageable) {
        log.debug("REST request to get a page of NursingHomePatients");
        Page<NursingHomePatient> page = nursingHomePatientService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /nursing-home-patients/:id} : get the "id" nursingHomePatient.
     *
     * @param id the id of the nursingHomePatient to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the nursingHomePatient, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/nursing-home-patients/{id}")
    public ResponseEntity<NursingHomePatient> getNursingHomePatient(@PathVariable Long id) {
        log.debug("REST request to get NursingHomePatient : {}", id);
        Optional<NursingHomePatient> nursingHomePatient = nursingHomePatientService.findOne(id);
        return ResponseUtil.wrapOrNotFound(nursingHomePatient);
    }

    /**
     * {@code DELETE  /nursing-home-patients/:id} : delete the "id" nursingHomePatient.
     *
     * @param id the id of the nursingHomePatient to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/nursing-home-patients/{id}")
    public ResponseEntity<Void> deleteNursingHomePatient(@PathVariable Long id) {
        log.debug("REST request to delete NursingHomePatient : {}", id);
        nursingHomePatientService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
