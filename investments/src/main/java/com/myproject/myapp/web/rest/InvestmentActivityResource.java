package com.myproject.myapp.web.rest;

import com.myproject.myapp.domain.InvestmentActivity;
import com.myproject.myapp.repository.InvestmentActivityRepository;
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
 * REST controller for managing {@link com.myproject.myapp.domain.InvestmentActivity}.
 */
@RestController
@RequestMapping("/api/investment-activities")
@Transactional
public class InvestmentActivityResource {

    private static final Logger LOG = LoggerFactory.getLogger(InvestmentActivityResource.class);

    private static final String ENTITY_NAME = "investmentsInvestmentActivity";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InvestmentActivityRepository investmentActivityRepository;

    public InvestmentActivityResource(InvestmentActivityRepository investmentActivityRepository) {
        this.investmentActivityRepository = investmentActivityRepository;
    }

    /**
     * {@code POST  /investment-activities} : Create a new investmentActivity.
     *
     * @param investmentActivity the investmentActivity to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new investmentActivity, or with status {@code 400 (Bad Request)} if the investmentActivity has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InvestmentActivity> createInvestmentActivity(@Valid @RequestBody InvestmentActivity investmentActivity)
        throws URISyntaxException {
        LOG.debug("REST request to save InvestmentActivity : {}", investmentActivity);
        if (investmentActivity.getId() != null) {
            throw new BadRequestAlertException("A new investmentActivity cannot already have an ID", ENTITY_NAME, "idexists");
        }
        investmentActivity = investmentActivityRepository.save(investmentActivity);
        return ResponseEntity.created(new URI("/api/investment-activities/" + investmentActivity.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, investmentActivity.getId().toString()))
            .body(investmentActivity);
    }

    /**
     * {@code PUT  /investment-activities/:id} : Updates an existing investmentActivity.
     *
     * @param id the id of the investmentActivity to save.
     * @param investmentActivity the investmentActivity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated investmentActivity,
     * or with status {@code 400 (Bad Request)} if the investmentActivity is not valid,
     * or with status {@code 500 (Internal Server Error)} if the investmentActivity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InvestmentActivity> updateInvestmentActivity(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InvestmentActivity investmentActivity
    ) throws URISyntaxException {
        LOG.debug("REST request to update InvestmentActivity : {}, {}", id, investmentActivity);
        if (investmentActivity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, investmentActivity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!investmentActivityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        investmentActivity = investmentActivityRepository.save(investmentActivity);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, investmentActivity.getId().toString()))
            .body(investmentActivity);
    }

    /**
     * {@code PATCH  /investment-activities/:id} : Partial updates given fields of an existing investmentActivity, field will ignore if it is null
     *
     * @param id the id of the investmentActivity to save.
     * @param investmentActivity the investmentActivity to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated investmentActivity,
     * or with status {@code 400 (Bad Request)} if the investmentActivity is not valid,
     * or with status {@code 404 (Not Found)} if the investmentActivity is not found,
     * or with status {@code 500 (Internal Server Error)} if the investmentActivity couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InvestmentActivity> partialUpdateInvestmentActivity(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InvestmentActivity investmentActivity
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InvestmentActivity partially : {}, {}", id, investmentActivity);
        if (investmentActivity.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, investmentActivity.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!investmentActivityRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InvestmentActivity> result = investmentActivityRepository
            .findById(investmentActivity.getId())
            .map(existingInvestmentActivity -> {
                if (investmentActivity.getInvestmentType() != null) {
                    existingInvestmentActivity.setInvestmentType(investmentActivity.getInvestmentType());
                }
                if (investmentActivity.getActivityType() != null) {
                    existingInvestmentActivity.setActivityType(investmentActivity.getActivityType());
                }
                if (investmentActivity.getProjectName() != null) {
                    existingInvestmentActivity.setProjectName(investmentActivity.getProjectName());
                }
                if (investmentActivity.getDescription() != null) {
                    existingInvestmentActivity.setDescription(investmentActivity.getDescription());
                }
                if (investmentActivity.getLocation() != null) {
                    existingInvestmentActivity.setLocation(investmentActivity.getLocation());
                }
                if (investmentActivity.getTargetAmount() != null) {
                    existingInvestmentActivity.setTargetAmount(investmentActivity.getTargetAmount());
                }
                if (investmentActivity.getCurrentAmount() != null) {
                    existingInvestmentActivity.setCurrentAmount(investmentActivity.getCurrentAmount());
                }
                if (investmentActivity.getBondIssuer() != null) {
                    existingInvestmentActivity.setBondIssuer(investmentActivity.getBondIssuer());
                }
                if (investmentActivity.getActivityDate() != null) {
                    existingInvestmentActivity.setActivityDate(investmentActivity.getActivityDate());
                }
                if (investmentActivity.getActivityAmount() != null) {
                    existingInvestmentActivity.setActivityAmount(investmentActivity.getActivityAmount());
                }
                if (investmentActivity.getStatus() != null) {
                    existingInvestmentActivity.setStatus(investmentActivity.getStatus());
                }
                if (investmentActivity.getRiskLevel() != null) {
                    existingInvestmentActivity.setRiskLevel(investmentActivity.getRiskLevel());
                }
                if (investmentActivity.getLogin() != null) {
                    existingInvestmentActivity.setLogin(investmentActivity.getLogin());
                }
                if (investmentActivity.getCreatedBy() != null) {
                    existingInvestmentActivity.setCreatedBy(investmentActivity.getCreatedBy());
                }
                if (investmentActivity.getCreatedDate() != null) {
                    existingInvestmentActivity.setCreatedDate(investmentActivity.getCreatedDate());
                }
                if (investmentActivity.getLastModifiedBy() != null) {
                    existingInvestmentActivity.setLastModifiedBy(investmentActivity.getLastModifiedBy());
                }
                if (investmentActivity.getLastModifiedDate() != null) {
                    existingInvestmentActivity.setLastModifiedDate(investmentActivity.getLastModifiedDate());
                }

                return existingInvestmentActivity;
            })
            .map(investmentActivityRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, investmentActivity.getId().toString())
        );
    }

    /**
     * {@code GET  /investment-activities} : get all the investmentActivities.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of investmentActivities in body.
     */
    @GetMapping("")
    public ResponseEntity<List<InvestmentActivity>> getAllInvestmentActivities(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of InvestmentActivities");
        Page<InvestmentActivity> page = investmentActivityRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /investment-activities/:id} : get the "id" investmentActivity.
     *
     * @param id the id of the investmentActivity to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the investmentActivity, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<InvestmentActivity> getInvestmentActivity(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InvestmentActivity : {}", id);
        Optional<InvestmentActivity> investmentActivity = investmentActivityRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(investmentActivity);
    }

    /**
     * {@code DELETE  /investment-activities/:id} : delete the "id" investmentActivity.
     *
     * @param id the id of the investmentActivity to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvestmentActivity(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InvestmentActivity : {}", id);
        investmentActivityRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
