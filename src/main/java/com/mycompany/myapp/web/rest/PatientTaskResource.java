package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.PatientTask;
import com.mycompany.myapp.repository.PatientTaskRepository;
import com.mycompany.myapp.service.PatientTaskService;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.PatientTask}.
 */
@RestController
@RequestMapping("/api")
public class PatientTaskResource {

    private final Logger log = LoggerFactory.getLogger(PatientTaskResource.class);

    private static final String ENTITY_NAME = "patientTask";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PatientTaskService patientTaskService;

    private final PatientTaskRepository patientTaskRepository;

    public PatientTaskResource(PatientTaskService patientTaskService, PatientTaskRepository patientTaskRepository) {
        this.patientTaskService = patientTaskService;
        this.patientTaskRepository = patientTaskRepository;
    }

    /**
     * {@code POST  /patient-tasks} : Create a new patientTask.
     *
     * @param patientTask the patientTask to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new patientTask, or with status {@code 400 (Bad Request)} if the patientTask has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/patient-tasks")
    public ResponseEntity<PatientTask> createPatientTask(@RequestBody PatientTask patientTask) throws URISyntaxException {
        log.debug("REST request to save PatientTask : {}", patientTask);
        if (patientTask.getId() != null) {
            throw new BadRequestAlertException("A new patientTask cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PatientTask result = patientTaskService.save(patientTask);
        return ResponseEntity
            .created(new URI("/api/patient-tasks/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /patient-tasks/:id} : Updates an existing patientTask.
     *
     * @param id the id of the patientTask to save.
     * @param patientTask the patientTask to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patientTask,
     * or with status {@code 400 (Bad Request)} if the patientTask is not valid,
     * or with status {@code 500 (Internal Server Error)} if the patientTask couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/patient-tasks/{id}")
    public ResponseEntity<PatientTask> updatePatientTask(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PatientTask patientTask
    ) throws URISyntaxException {
        log.debug("REST request to update PatientTask : {}, {}", id, patientTask);
        if (patientTask.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patientTask.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patientTaskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PatientTask result = patientTaskService.save(patientTask);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, patientTask.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /patient-tasks/:id} : Partial updates given fields of an existing patientTask, field will ignore if it is null
     *
     * @param id the id of the patientTask to save.
     * @param patientTask the patientTask to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated patientTask,
     * or with status {@code 400 (Bad Request)} if the patientTask is not valid,
     * or with status {@code 404 (Not Found)} if the patientTask is not found,
     * or with status {@code 500 (Internal Server Error)} if the patientTask couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/patient-tasks/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PatientTask> partialUpdatePatientTask(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PatientTask patientTask
    ) throws URISyntaxException {
        log.debug("REST request to partial update PatientTask partially : {}, {}", id, patientTask);
        if (patientTask.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, patientTask.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!patientTaskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PatientTask> result = patientTaskService.partialUpdate(patientTask);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, patientTask.getId().toString())
        );
    }

    /**
     * {@code GET  /patient-tasks} : get all the patientTasks.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of patientTasks in body.
     */
    @GetMapping("/patient-tasks")
    public ResponseEntity<List<PatientTask>> getAllPatientTasks(Pageable pageable) {
        log.debug("REST request to get a page of PatientTasks");
        Page<PatientTask> page = patientTaskService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /patient-tasks/:id} : get the "id" patientTask.
     *
     * @param id the id of the patientTask to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the patientTask, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/patient-tasks/{id}")
    public ResponseEntity<PatientTask> getPatientTask(@PathVariable Long id) {
        log.debug("REST request to get PatientTask : {}", id);
        Optional<PatientTask> patientTask = patientTaskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(patientTask);
    }

    /**
     * {@code DELETE  /patient-tasks/:id} : delete the "id" patientTask.
     *
     * @param id the id of the patientTask to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/patient-tasks/{id}")
    public ResponseEntity<Void> deletePatientTask(@PathVariable Long id) {
        log.debug("REST request to delete PatientTask : {}", id);
        patientTaskService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
