package com.myproject.myapp.web.rest;

import com.myproject.myapp.domain.InternationalTransfer;
import com.myproject.myapp.repository.InternationalTransferRepository;
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
 * REST controller for managing {@link com.myproject.myapp.domain.InternationalTransfer}.
 */
@RestController
@RequestMapping("/api/international-transfers")
@Transactional
public class InternationalTransferResource {

    private static final Logger LOG = LoggerFactory.getLogger(InternationalTransferResource.class);

    private static final String ENTITY_NAME = "accountsInternationalTransfer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final InternationalTransferRepository internationalTransferRepository;

    public InternationalTransferResource(InternationalTransferRepository internationalTransferRepository) {
        this.internationalTransferRepository = internationalTransferRepository;
    }

    /**
     * {@code POST  /international-transfers} : Create a new internationalTransfer.
     *
     * @param internationalTransfer the internationalTransfer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new internationalTransfer, or with status {@code 400 (Bad Request)} if the internationalTransfer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<InternationalTransfer> createInternationalTransfer(
        @Valid @RequestBody InternationalTransfer internationalTransfer
    ) throws URISyntaxException {
        LOG.debug("REST request to save InternationalTransfer : {}", internationalTransfer);
        if (internationalTransfer.getId() != null) {
            throw new BadRequestAlertException("A new internationalTransfer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        internationalTransfer = internationalTransferRepository.save(internationalTransfer);
        return ResponseEntity.created(new URI("/api/international-transfers/" + internationalTransfer.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, internationalTransfer.getId().toString()))
            .body(internationalTransfer);
    }

    /**
     * {@code PUT  /international-transfers/:id} : Updates an existing internationalTransfer.
     *
     * @param id the id of the internationalTransfer to save.
     * @param internationalTransfer the internationalTransfer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated internationalTransfer,
     * or with status {@code 400 (Bad Request)} if the internationalTransfer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the internationalTransfer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<InternationalTransfer> updateInternationalTransfer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody InternationalTransfer internationalTransfer
    ) throws URISyntaxException {
        LOG.debug("REST request to update InternationalTransfer : {}, {}", id, internationalTransfer);
        if (internationalTransfer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, internationalTransfer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!internationalTransferRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        internationalTransfer = internationalTransferRepository.save(internationalTransfer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, internationalTransfer.getId().toString()))
            .body(internationalTransfer);
    }

    /**
     * {@code PATCH  /international-transfers/:id} : Partial updates given fields of an existing internationalTransfer, field will ignore if it is null
     *
     * @param id the id of the internationalTransfer to save.
     * @param internationalTransfer the internationalTransfer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated internationalTransfer,
     * or with status {@code 400 (Bad Request)} if the internationalTransfer is not valid,
     * or with status {@code 404 (Not Found)} if the internationalTransfer is not found,
     * or with status {@code 500 (Internal Server Error)} if the internationalTransfer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<InternationalTransfer> partialUpdateInternationalTransfer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody InternationalTransfer internationalTransfer
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update InternationalTransfer partially : {}, {}", id, internationalTransfer);
        if (internationalTransfer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, internationalTransfer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!internationalTransferRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<InternationalTransfer> result = internationalTransferRepository
            .findById(internationalTransfer.getId())
            .map(existingInternationalTransfer -> {
                if (internationalTransfer.getSenderAccountNumber() != null) {
                    existingInternationalTransfer.setSenderAccountNumber(internationalTransfer.getSenderAccountNumber());
                }
                if (internationalTransfer.getRecipientIban() != null) {
                    existingInternationalTransfer.setRecipientIban(internationalTransfer.getRecipientIban());
                }
                if (internationalTransfer.getSwiftCode() != null) {
                    existingInternationalTransfer.setSwiftCode(internationalTransfer.getSwiftCode());
                }
                if (internationalTransfer.getRecipientName() != null) {
                    existingInternationalTransfer.setRecipientName(internationalTransfer.getRecipientName());
                }
                if (internationalTransfer.getAmount() != null) {
                    existingInternationalTransfer.setAmount(internationalTransfer.getAmount());
                }
                if (internationalTransfer.getCurrency() != null) {
                    existingInternationalTransfer.setCurrency(internationalTransfer.getCurrency());
                }
                if (internationalTransfer.getTransactionDate() != null) {
                    existingInternationalTransfer.setTransactionDate(internationalTransfer.getTransactionDate());
                }
                if (internationalTransfer.getDescription() != null) {
                    existingInternationalTransfer.setDescription(internationalTransfer.getDescription());
                }

                return existingInternationalTransfer;
            })
            .map(internationalTransferRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, internationalTransfer.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<InternationalTransfer>> getAllInternationalTransfers(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of InternationalTransfers");
        Page<InternationalTransfer> page;
        if (eagerload) {
            page = internationalTransferRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = internationalTransferRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InternationalTransfer> getInternationalTransfer(@PathVariable("id") Long id) {
        LOG.debug("REST request to get InternationalTransfer : {}", id);
        Optional<InternationalTransfer> internationalTransfer = internationalTransferRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(internationalTransfer);
    }

    /**
     * {@code DELETE  /international-transfers/:id} : delete the "id" internationalTransfer.
     *
     * @param id the id of the internationalTransfer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInternationalTransfer(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete InternationalTransfer : {}", id);
        internationalTransferRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
