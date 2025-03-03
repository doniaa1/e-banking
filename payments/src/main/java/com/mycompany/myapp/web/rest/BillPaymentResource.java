package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.BillPayment;
import com.mycompany.myapp.repository.BillPaymentRepository;
import com.mycompany.myapp.security.SecurityUtils;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.BillPayment}.
 */
@RestController
@RequestMapping("/api/bill-payments")
@Transactional
public class BillPaymentResource {

    private static final Logger LOG = LoggerFactory.getLogger(BillPaymentResource.class);

    private static final String ENTITY_NAME = "paymentBillPayment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BillPaymentRepository billPaymentRepository;

    public BillPaymentResource(BillPaymentRepository billPaymentRepository) {
        this.billPaymentRepository = billPaymentRepository;
    }

    /**
     * {@code POST  /bill-payments} : Create a new billPayment.
     *
     * @param billPayment the billPayment to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new billPayment, or with status {@code 400 (Bad Request)} if the billPayment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<BillPayment> createBillPayment(@Valid @RequestBody BillPayment billPayment) throws URISyntaxException {
        LOG.debug("REST request to save BillPayment : {}", billPayment);
        if (billPayment.getId() != null) {
            throw new BadRequestAlertException("A new billPayment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        billPayment = billPaymentRepository.save(billPayment);
        return ResponseEntity.created(new URI("/api/bill-payments/" + billPayment.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, billPayment.getId().toString()))
            .body(billPayment);
    }

    /**
     * {@code PUT  /bill-payments/:id} : Updates an existing billPayment.
     *
     * @param id the id of the billPayment to save.
     * @param billPayment the billPayment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billPayment,
     * or with status {@code 400 (Bad Request)} if the billPayment is not valid,
     * or with status {@code 500 (Internal Server Error)} if the billPayment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<BillPayment> updateBillPayment(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody BillPayment billPayment
    ) throws URISyntaxException {
        LOG.debug("REST request to update BillPayment : {}, {}", id, billPayment);
        if (billPayment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billPayment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billPaymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        billPayment = billPaymentRepository.save(billPayment);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, billPayment.getId().toString()))
            .body(billPayment);
    }

    /**
     * {@code PATCH  /bill-payments/:id} : Partial updates given fields of an existing billPayment, field will ignore if it is null
     *
     * @param id the id of the billPayment to save.
     * @param billPayment the billPayment to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billPayment,
     * or with status {@code 400 (Bad Request)} if the billPayment is not valid,
     * or with status {@code 404 (Not Found)} if the billPayment is not found,
     * or with status {@code 500 (Internal Server Error)} if the billPayment couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<BillPayment> partialUpdateBillPayment(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody BillPayment billPayment
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update BillPayment partially : {}, {}", id, billPayment);
        if (billPayment.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, billPayment.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!billPaymentRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<BillPayment> result = billPaymentRepository
            .findById(billPayment.getId())
            .map(existingBillPayment -> {
                if (billPayment.getBillReference() != null) {
                    existingBillPayment.setBillReference(billPayment.getBillReference());
                }
                if (billPayment.getBillIssuer() != null) {
                    existingBillPayment.setBillIssuer(billPayment.getBillIssuer());
                }
                if (billPayment.getAmount() != null) {
                    existingBillPayment.setAmount(billPayment.getAmount());
                }
                if (billPayment.getPaymentDate() != null) {
                    existingBillPayment.setPaymentDate(billPayment.getPaymentDate());
                }
                if (billPayment.getDescription() != null) {
                    existingBillPayment.setDescription(billPayment.getDescription());
                }

                return existingBillPayment;
            })
            .map(billPaymentRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, billPayment.getId().toString())
        );
    }


    @GetMapping("")
    public ResponseEntity<List<BillPayment>> getAllBillPayments(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of BillPayments");
        Page<BillPayment> page;
        if (eagerload) {
            page = billPaymentRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = billPaymentRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BillPayment> getBillPayment(@PathVariable("id") Long id) {
        LOG.debug("REST request to get BillPayment : {}", id);
        Optional<BillPayment> billPayment = billPaymentRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(billPayment);
    }


    /**
     * {@code DELETE  /bill-payments/:id} : delete the "id" billPayment.
     *
     * @param id the id of the billPayment to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBillPayment(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete BillPayment : {}", id);
        billPaymentRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
