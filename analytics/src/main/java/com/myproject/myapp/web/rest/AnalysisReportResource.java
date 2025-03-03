package com.myproject.myapp.web.rest;

import com.myproject.myapp.domain.AnalysisReport;
import com.myproject.myapp.repository.AnalysisReportRepository;
import com.myproject.myapp.security.SecurityUtils;
import com.myproject.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.myproject.myapp.domain.AnalysisReport}.
 */
@RestController
@RequestMapping("/api/analysis-reports")
@Transactional
public class AnalysisReportResource {

    private static final Logger LOG = LoggerFactory.getLogger(AnalysisReportResource.class);

    private static final String ENTITY_NAME = "analyticsAnalysisReport";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AnalysisReportRepository analysisReportRepository;

    public AnalysisReportResource(AnalysisReportRepository analysisReportRepository) {
        this.analysisReportRepository = analysisReportRepository;
    }

    /**
     * {@code POST  /analysis-reports} : Create a new analysisReport.
     *
     * @param analysisReport the analysisReport to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new analysisReport, or with status {@code 400 (Bad Request)} if the analysisReport has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AnalysisReport> createAnalysisReport(@Valid @RequestBody AnalysisReport analysisReport)
        throws URISyntaxException {
        LOG.debug("REST request to save AnalysisReport : {}", analysisReport);
        if (analysisReport.getId() != null) {
            throw new BadRequestAlertException("A new analysisReport cannot already have an ID", ENTITY_NAME, "idexists");
        }
        analysisReport = analysisReportRepository.save(analysisReport);
        return ResponseEntity.created(new URI("/api/analysis-reports/" + analysisReport.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, analysisReport.getId().toString()))
            .body(analysisReport);
    }

    /**
     * {@code PUT  /analysis-reports/:id} : Updates an existing analysisReport.
     *
     * @param id the id of the analysisReport to save.
     * @param analysisReport the analysisReport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated analysisReport,
     * or with status {@code 400 (Bad Request)} if the analysisReport is not valid,
     * or with status {@code 500 (Internal Server Error)} if the analysisReport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AnalysisReport> updateAnalysisReport(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AnalysisReport analysisReport
    ) throws URISyntaxException {
        LOG.debug("REST request to update AnalysisReport : {}, {}", id, analysisReport);
        if (analysisReport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, analysisReport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!analysisReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        analysisReport = analysisReportRepository.save(analysisReport);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, analysisReport.getId().toString()))
            .body(analysisReport);
    }

    /**
     * {@code PATCH  /analysis-reports/:id} : Partial updates given fields of an existing analysisReport, field will ignore if it is null
     *
     * @param id the id of the analysisReport to save.
     * @param analysisReport the analysisReport to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated analysisReport,
     * or with status {@code 400 (Bad Request)} if the analysisReport is not valid,
     * or with status {@code 404 (Not Found)} if the analysisReport is not found,
     * or with status {@code 500 (Internal Server Error)} if the analysisReport couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AnalysisReport> partialUpdateAnalysisReport(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AnalysisReport analysisReport
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AnalysisReport partially : {}, {}", id, analysisReport);
        if (analysisReport.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, analysisReport.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!analysisReportRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AnalysisReport> result = analysisReportRepository
            .findById(analysisReport.getId())
            .map(existingAnalysisReport -> {
                if (analysisReport.getTitle() != null) {
                    existingAnalysisReport.setTitle(analysisReport.getTitle());
                }
                if (analysisReport.getCreatedAt() != null) {
                    existingAnalysisReport.setCreatedAt(analysisReport.getCreatedAt());
                }
                if (analysisReport.getAnalysisType() != null) {
                    existingAnalysisReport.setAnalysisType(analysisReport.getAnalysisType());
                }
                if (analysisReport.getReportType() != null) {
                    existingAnalysisReport.setReportType(analysisReport.getReportType());
                }
                if (analysisReport.getGeneratedBy() != null) {
                    existingAnalysisReport.setGeneratedBy(analysisReport.getGeneratedBy());
                }
                if (analysisReport.getContent() != null) {
                    existingAnalysisReport.setContent(analysisReport.getContent());
                }
                if (analysisReport.getStatus() != null) {
                    existingAnalysisReport.setStatus(analysisReport.getStatus());
                }

                return existingAnalysisReport;
            })
            .map(analysisReportRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, analysisReport.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<AnalysisReport>> getAllAnalysisReports(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of AnalysisReports");
        Page<AnalysisReport> page;
        if (eagerload) {
            page = analysisReportRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = analysisReportRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AnalysisReport> getAnalysisReport(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AnalysisReport : {}", id);
        Optional<AnalysisReport> analysisReport = analysisReportRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(analysisReport);
    }

    /**
     * {@code DELETE  /analysis-reports/:id} : delete the "id" analysisReport.
     *
     * @param id the id of the analysisReport to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAnalysisReport(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AnalysisReport : {}", id);
        analysisReportRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
