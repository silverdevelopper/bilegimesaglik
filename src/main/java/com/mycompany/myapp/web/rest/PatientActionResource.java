package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.PatientAction;
import com.mycompany.myapp.repository.PatientActionRepository;
import com.mycompany.myapp.service.PatientActionService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.PatientAction}.
 */
@RestController
@RequestMapping("/api")
public class PatientActionResource {

    private final Logger log = LoggerFactory.getLogger(PatientActionResource.class);

    private static final String ENTITY_NAME = "patientAction";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PatientActionService patientActionService;

    private final PatientActionRepository patientActionRepository;

    public PatientActionResource(PatientActionService patientActionService, PatientActionRepository patientActionRepository) {
        this.patientActionService = patientActionService;
        this.patientActionRepository = patientActionRepository;
    }

    /**
     * {@code POST  /patient-actions} : Create a new patientAction.
     *
     * @param patientAction the patientAction to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new patientAction, or with status {@code 400 (Bad Request)} if the patientAction has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/patient-actions")
    public ResponseEntity<PatientAction> createPatientAction(@RequestBody PatientAction patientAction) throws URISyntaxException {
        log.debug("REST request to save PatientAction : {}", patientAction);
        if (patientAction.getId() != null) {
            throw new BadRequestAlertException("A new patientAction cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PatientAction result = patientActionService.save(patientAction);
        return ResponseEntity
            .created(new URI("/api/patient-actions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /patient-actions/:id} : Updates an existing patientAction.
     *
     * @param id the id of the patientAction to save.
     * @param patientAction the patientAction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patientAction,
     * or with status {@code 400 (Bad Request)} if the patientAction is not valid,
     * or with status {@code 500 (Internal Server Error)} if the patientAction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/patient-actions/{id}")
    public ResponseEntity<PatientAction> updatePatientAction(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PatientAction patientAction
    ) throws URISyntaxException {
        log.debug("REST request to update PatientAction : {}, {}", id, patientAction);
        if (patientAction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patientAction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patientActionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PatientAction result = patientActionService.save(patientAction);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, patientAction.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /patient-actions/:id} : Partial updates given fields of an existing patientAction, field will ignore if it is null
     *
     * @param id the id of the patientAction to save.
     * @param patientAction the patientAction to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patientAction,
     * or with status {@code 400 (Bad Request)} if the patientAction is not valid,
     * or with status {@code 404 (Not Found)} if the patientAction is not found,
     * or with status {@code 500 (Internal Server Error)} if the patientAction couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/patient-actions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PatientAction> partialUpdatePatientAction(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PatientAction patientAction
    ) throws URISyntaxException {
        log.debug("REST request to partial update PatientAction partially : {}, {}", id, patientAction);
        if (patientAction.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patientAction.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patientActionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PatientAction> result = patientActionService.partialUpdate(patientAction);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, patientAction.getId().toString())
        );
    }

    /**
     * {@code GET  /patient-actions} : get all the patientActions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of patientActions in body.
     */
    @GetMapping("/patient-actions")
    public ResponseEntity<List<PatientAction>> getAllPatientActions(Pageable pageable) {
        log.debug("REST request to get a page of PatientActions");
        Page<PatientAction> page = patientActionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /patient-actions/:id} : get the "id" patientAction.
     *
     * @param id the id of the patientAction to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the patientAction, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/patient-actions/{id}")
    public ResponseEntity<PatientAction> getPatientAction(@PathVariable Long id) {
        log.debug("REST request to get PatientAction : {}", id);
        Optional<PatientAction> patientAction = patientActionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(patientAction);
    }

    /**
     * {@code DELETE  /patient-actions/:id} : delete the "id" patientAction.
     *
     * @param id the id of the patientAction to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/patient-actions/{id}")
    public ResponseEntity<Void> deletePatientAction(@PathVariable Long id) {
        log.debug("REST request to delete PatientAction : {}", id);
        patientActionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
