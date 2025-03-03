package com.myproject.myapp.web.rest;

import com.myproject.myapp.domain.DataCollection;
import com.myproject.myapp.repository.DataCollectionRepository;
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
 * REST controller for managing {@link com.myproject.myapp.domain.DataCollection}.
 */
@RestController
@RequestMapping("/api/data-collections")
@Transactional
public class DataCollectionResource {

    private static final Logger LOG = LoggerFactory.getLogger(DataCollectionResource.class);

    private static final String ENTITY_NAME = "analyticsDataCollection";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final DataCollectionRepository dataCollectionRepository;

    public DataCollectionResource(DataCollectionRepository dataCollectionRepository) {
        this.dataCollectionRepository = dataCollectionRepository;
    }

    /**
     * {@code POST  /data-collections} : Create a new dataCollection.
     *
     * @param dataCollection the dataCollection to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new dataCollection, or with status {@code 400 (Bad Request)} if the dataCollection has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<DataCollection> createDataCollection(@Valid @RequestBody DataCollection dataCollection)
        throws URISyntaxException {
        LOG.debug("REST request to save DataCollection : {}", dataCollection);
        if (dataCollection.getId() != null) {
            throw new BadRequestAlertException("A new dataCollection cannot already have an ID", ENTITY_NAME, "idexists");
        }
        dataCollection = dataCollectionRepository.save(dataCollection);
        return ResponseEntity.created(new URI("/api/data-collections/" + dataCollection.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, dataCollection.getId().toString()))
            .body(dataCollection);
    }

    /**
     * {@code PUT  /data-collections/:id} : Updates an existing dataCollection.
     *
     * @param id the id of the dataCollection to save.
     * @param dataCollection the dataCollection to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dataCollection,
     * or with status {@code 400 (Bad Request)} if the dataCollection is not valid,
     * or with status {@code 500 (Internal Server Error)} if the dataCollection couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<DataCollection> updateDataCollection(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody DataCollection dataCollection
    ) throws URISyntaxException {
        LOG.debug("REST request to update DataCollection : {}, {}", id, dataCollection);
        if (dataCollection.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dataCollection.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dataCollectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        dataCollection = dataCollectionRepository.save(dataCollection);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dataCollection.getId().toString()))
            .body(dataCollection);
    }

    /**
     * {@code PATCH  /data-collections/:id} : Partial updates given fields of an existing dataCollection, field will ignore if it is null
     *
     * @param id the id of the dataCollection to save.
     * @param dataCollection the dataCollection to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated dataCollection,
     * or with status {@code 400 (Bad Request)} if the dataCollection is not valid,
     * or with status {@code 404 (Not Found)} if the dataCollection is not found,
     * or with status {@code 500 (Internal Server Error)} if the dataCollection couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<DataCollection> partialUpdateDataCollection(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody DataCollection dataCollection
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update DataCollection partially : {}, {}", id, dataCollection);
        if (dataCollection.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, dataCollection.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!dataCollectionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<DataCollection> result = dataCollectionRepository
            .findById(dataCollection.getId())
            .map(existingDataCollection -> {
                if (dataCollection.getLogin() != null) {
                    existingDataCollection.setLogin(dataCollection.getLogin());
                }
                if (dataCollection.getName() != null) {
                    existingDataCollection.setName(dataCollection.getName());
                }
                if (dataCollection.getSource() != null) {
                    existingDataCollection.setSource(dataCollection.getSource());
                }
                if (dataCollection.getCollectedAt() != null) {
                    existingDataCollection.setCollectedAt(dataCollection.getCollectedAt());
                }
                if (dataCollection.getDataType() != null) {
                    existingDataCollection.setDataType(dataCollection.getDataType());
                }
                if (dataCollection.getStatus() != null) {
                    existingDataCollection.setStatus(dataCollection.getStatus());
                }
                if (dataCollection.getDescription() != null) {
                    existingDataCollection.setDescription(dataCollection.getDescription());
                }

                return existingDataCollection;
            })
            .map(dataCollectionRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, dataCollection.getId().toString())
        );
    }

    /**
     * {@code GET  /data-collections} : get all the dataCollections.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of dataCollections in body.
     */
    @GetMapping("")
    public ResponseEntity<List<DataCollection>> getAllDataCollections(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of DataCollections");
        Page<DataCollection> page = dataCollectionRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /data-collections/:id} : get the "id" dataCollection.
     *
     * @param id the id of the dataCollection to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the dataCollection, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<DataCollection> getDataCollection(@PathVariable("id") Long id) {
        LOG.debug("REST request to get DataCollection : {}", id);
        Optional<DataCollection> dataCollection = dataCollectionRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(dataCollection);
    }

    /**
     * {@code DELETE  /data-collections/:id} : delete the "id" dataCollection.
     *
     * @param id the id of the dataCollection to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDataCollection(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete DataCollection : {}", id);
        dataCollectionRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
