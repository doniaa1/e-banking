package com.myproject.myapp.web.rest;

import com.myproject.myapp.domain.LocalTransfer;
import com.myproject.myapp.repository.LocalTransferRepository;
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
 * REST controller for managing {@link com.myproject.myapp.domain.LocalTransfer}.
 */
@RestController
@RequestMapping("/api/local-transfers")
@Transactional
public class LocalTransferResource {

    private static final Logger LOG = LoggerFactory.getLogger(LocalTransferResource.class);

    private static final String ENTITY_NAME = "accountsLocalTransfer";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final LocalTransferRepository localTransferRepository;

    public LocalTransferResource(LocalTransferRepository localTransferRepository) {
        this.localTransferRepository = localTransferRepository;
    }

    /**
     * {@code POST  /local-transfers} : Create a new localTransfer.
     *
     * @param localTransfer the localTransfer to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new localTransfer, or with status {@code 400 (Bad Request)} if the localTransfer has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<LocalTransfer> createLocalTransfer(@Valid @RequestBody LocalTransfer localTransfer) throws URISyntaxException {
        LOG.debug("REST request to save LocalTransfer : {}", localTransfer);
        if (localTransfer.getId() != null) {
            throw new BadRequestAlertException("A new localTransfer cannot already have an ID", ENTITY_NAME, "idexists");
        }
        localTransfer = localTransferRepository.save(localTransfer);
        return ResponseEntity.created(new URI("/api/local-transfers/" + localTransfer.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, localTransfer.getId().toString()))
            .body(localTransfer);
    }

    /**
     * {@code PUT  /local-transfers/:id} : Updates an existing localTransfer.
     *
     * @param id the id of the localTransfer to save.
     * @param localTransfer the localTransfer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated localTransfer,
     * or with status {@code 400 (Bad Request)} if the localTransfer is not valid,
     * or with status {@code 500 (Internal Server Error)} if the localTransfer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<LocalTransfer> updateLocalTransfer(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody LocalTransfer localTransfer
    ) throws URISyntaxException {
        LOG.debug("REST request to update LocalTransfer : {}, {}", id, localTransfer);
        if (localTransfer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, localTransfer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!localTransferRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        localTransfer = localTransferRepository.save(localTransfer);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, localTransfer.getId().toString()))
            .body(localTransfer);
    }

    /**
     * {@code PATCH  /local-transfers/:id} : Partial updates given fields of an existing localTransfer, field will ignore if it is null
     *
     * @param id the id of the localTransfer to save.
     * @param localTransfer the localTransfer to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated localTransfer,
     * or with status {@code 400 (Bad Request)} if the localTransfer is not valid,
     * or with status {@code 404 (Not Found)} if the localTransfer is not found,
     * or with status {@code 500 (Internal Server Error)} if the localTransfer couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<LocalTransfer> partialUpdateLocalTransfer(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody LocalTransfer localTransfer
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update LocalTransfer partially : {}, {}", id, localTransfer);
        if (localTransfer.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, localTransfer.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!localTransferRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<LocalTransfer> result = localTransferRepository
            .findById(localTransfer.getId())
            .map(existingLocalTransfer -> {
                if (localTransfer.getSenderAccountNumber() != null) {
                    existingLocalTransfer.setSenderAccountNumber(localTransfer.getSenderAccountNumber());
                }
                if (localTransfer.getRecipientAccountNumber() != null) {
                    existingLocalTransfer.setRecipientAccountNumber(localTransfer.getRecipientAccountNumber());
                }
                if (localTransfer.getRecipientBankName() != null) {
                    existingLocalTransfer.setRecipientBankName(localTransfer.getRecipientBankName());
                }
                if (localTransfer.getRecipientBankBranch() != null) {
                    existingLocalTransfer.setRecipientBankBranch(localTransfer.getRecipientBankBranch());
                }
                if (localTransfer.getAmount() != null) {
                    existingLocalTransfer.setAmount(localTransfer.getAmount());
                }
                if (localTransfer.getTransactionDate() != null) {
                    existingLocalTransfer.setTransactionDate(localTransfer.getTransactionDate());
                }
                if (localTransfer.getDescription() != null) {
                    existingLocalTransfer.setDescription(localTransfer.getDescription());
                }

                return existingLocalTransfer;
            })
            .map(localTransferRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, localTransfer.getId().toString())
        );
    }

    @GetMapping("")
    public ResponseEntity<List<LocalTransfer>> getAllLocalTransfers(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable,
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get a page of LocalTransfers");
        Page<LocalTransfer> page;
        if (eagerload) {
            page = localTransferRepository.findAllWithEagerRelationships(pageable);
        } else {
            page = localTransferRepository.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocalTransfer> getLocalTransfer(@PathVariable("id") Long id) {
        LOG.debug("REST request to get LocalTransfer : {}", id);
        Optional<LocalTransfer> localTransfer = localTransferRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(localTransfer);
    }

    /**
     * {@code DELETE  /local-transfers/:id} : delete the "id" localTransfer.
     *
     * @param id the id of the localTransfer to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocalTransfer(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete LocalTransfer : {}", id);
        localTransferRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
