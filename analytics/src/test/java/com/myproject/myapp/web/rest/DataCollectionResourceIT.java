package com.myproject.myapp.web.rest;

import static com.myproject.myapp.domain.DataCollectionAsserts.*;
import static com.myproject.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.myapp.IntegrationTest;
import com.myproject.myapp.domain.DataCollection;
import com.myproject.myapp.domain.enumeration.DataType;
import com.myproject.myapp.domain.enumeration.Status;
import com.myproject.myapp.repository.DataCollectionRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link DataCollectionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class DataCollectionResourceIT {

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_SOURCE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE = "BBBBBBBBBB";

    private static final Instant DEFAULT_COLLECTED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COLLECTED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final DataType DEFAULT_DATA_TYPE = DataType.TEXT;
    private static final DataType UPDATED_DATA_TYPE = DataType.NUMERIC;

    private static final Status DEFAULT_STATUS = Status.ACTIVE;
    private static final Status UPDATED_STATUS = Status.ARCHIVED;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/data-collections";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private DataCollectionRepository dataCollectionRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restDataCollectionMockMvc;

    private DataCollection dataCollection;

    private DataCollection insertedDataCollection;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DataCollection createEntity() {
        return new DataCollection()
            .login(DEFAULT_LOGIN)
            .name(DEFAULT_NAME)
            .source(DEFAULT_SOURCE)
            .collectedAt(DEFAULT_COLLECTED_AT)
            .dataType(DEFAULT_DATA_TYPE)
            .status(DEFAULT_STATUS)
            .description(DEFAULT_DESCRIPTION);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static DataCollection createUpdatedEntity() {
        return new DataCollection()
            .login(UPDATED_LOGIN)
            .name(UPDATED_NAME)
            .source(UPDATED_SOURCE)
            .collectedAt(UPDATED_COLLECTED_AT)
            .dataType(UPDATED_DATA_TYPE)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION);
    }

    @BeforeEach
    public void initTest() {
        dataCollection = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedDataCollection != null) {
            dataCollectionRepository.delete(insertedDataCollection);
            insertedDataCollection = null;
        }
    }

    @Test
    @Transactional
    void createDataCollection() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the DataCollection
        var returnedDataCollection = om.readValue(
            restDataCollectionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dataCollection)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            DataCollection.class
        );

        // Validate the DataCollection in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertDataCollectionUpdatableFieldsEquals(returnedDataCollection, getPersistedDataCollection(returnedDataCollection));

        insertedDataCollection = returnedDataCollection;
    }

    @Test
    @Transactional
    void createDataCollectionWithExistingId() throws Exception {
        // Create the DataCollection with an existing ID
        dataCollection.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restDataCollectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dataCollection)))
            .andExpect(status().isBadRequest());

        // Validate the DataCollection in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkLoginIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dataCollection.setLogin(null);

        // Create the DataCollection, which fails.

        restDataCollectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dataCollection)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dataCollection.setName(null);

        // Create the DataCollection, which fails.

        restDataCollectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dataCollection)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSourceIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dataCollection.setSource(null);

        // Create the DataCollection, which fails.

        restDataCollectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dataCollection)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCollectedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dataCollection.setCollectedAt(null);

        // Create the DataCollection, which fails.

        restDataCollectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dataCollection)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        dataCollection.setStatus(null);

        // Create the DataCollection, which fails.

        restDataCollectionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dataCollection)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllDataCollections() throws Exception {
        // Initialize the database
        insertedDataCollection = dataCollectionRepository.saveAndFlush(dataCollection);

        // Get all the dataCollectionList
        restDataCollectionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(dataCollection.getId().intValue())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].source").value(hasItem(DEFAULT_SOURCE)))
            .andExpect(jsonPath("$.[*].collectedAt").value(hasItem(DEFAULT_COLLECTED_AT.toString())))
            .andExpect(jsonPath("$.[*].dataType").value(hasItem(DEFAULT_DATA_TYPE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())));
    }

    @Test
    @Transactional
    void getDataCollection() throws Exception {
        // Initialize the database
        insertedDataCollection = dataCollectionRepository.saveAndFlush(dataCollection);

        // Get the dataCollection
        restDataCollectionMockMvc
            .perform(get(ENTITY_API_URL_ID, dataCollection.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(dataCollection.getId().intValue()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.source").value(DEFAULT_SOURCE))
            .andExpect(jsonPath("$.collectedAt").value(DEFAULT_COLLECTED_AT.toString()))
            .andExpect(jsonPath("$.dataType").value(DEFAULT_DATA_TYPE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()));
    }

    @Test
    @Transactional
    void getNonExistingDataCollection() throws Exception {
        // Get the dataCollection
        restDataCollectionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingDataCollection() throws Exception {
        // Initialize the database
        insertedDataCollection = dataCollectionRepository.saveAndFlush(dataCollection);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dataCollection
        DataCollection updatedDataCollection = dataCollectionRepository.findById(dataCollection.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedDataCollection are not directly saved in db
        em.detach(updatedDataCollection);
        updatedDataCollection
            .login(UPDATED_LOGIN)
            .name(UPDATED_NAME)
            .source(UPDATED_SOURCE)
            .collectedAt(UPDATED_COLLECTED_AT)
            .dataType(UPDATED_DATA_TYPE)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION);

        restDataCollectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedDataCollection.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedDataCollection))
            )
            .andExpect(status().isOk());

        // Validate the DataCollection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedDataCollectionToMatchAllProperties(updatedDataCollection);
    }

    @Test
    @Transactional
    void putNonExistingDataCollection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dataCollection.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataCollectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, dataCollection.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dataCollection))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataCollection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchDataCollection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dataCollection.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataCollectionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(dataCollection))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataCollection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamDataCollection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dataCollection.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataCollectionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(dataCollection)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DataCollection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateDataCollectionWithPatch() throws Exception {
        // Initialize the database
        insertedDataCollection = dataCollectionRepository.saveAndFlush(dataCollection);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dataCollection using partial update
        DataCollection partialUpdatedDataCollection = new DataCollection();
        partialUpdatedDataCollection.setId(dataCollection.getId());

        partialUpdatedDataCollection.login(UPDATED_LOGIN).source(UPDATED_SOURCE);

        restDataCollectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDataCollection.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDataCollection))
            )
            .andExpect(status().isOk());

        // Validate the DataCollection in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDataCollectionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedDataCollection, dataCollection),
            getPersistedDataCollection(dataCollection)
        );
    }

    @Test
    @Transactional
    void fullUpdateDataCollectionWithPatch() throws Exception {
        // Initialize the database
        insertedDataCollection = dataCollectionRepository.saveAndFlush(dataCollection);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the dataCollection using partial update
        DataCollection partialUpdatedDataCollection = new DataCollection();
        partialUpdatedDataCollection.setId(dataCollection.getId());

        partialUpdatedDataCollection
            .login(UPDATED_LOGIN)
            .name(UPDATED_NAME)
            .source(UPDATED_SOURCE)
            .collectedAt(UPDATED_COLLECTED_AT)
            .dataType(UPDATED_DATA_TYPE)
            .status(UPDATED_STATUS)
            .description(UPDATED_DESCRIPTION);

        restDataCollectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedDataCollection.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedDataCollection))
            )
            .andExpect(status().isOk());

        // Validate the DataCollection in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertDataCollectionUpdatableFieldsEquals(partialUpdatedDataCollection, getPersistedDataCollection(partialUpdatedDataCollection));
    }

    @Test
    @Transactional
    void patchNonExistingDataCollection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dataCollection.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restDataCollectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, dataCollection.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dataCollection))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataCollection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchDataCollection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dataCollection.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataCollectionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(dataCollection))
            )
            .andExpect(status().isBadRequest());

        // Validate the DataCollection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamDataCollection() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        dataCollection.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restDataCollectionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(dataCollection)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the DataCollection in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteDataCollection() throws Exception {
        // Initialize the database
        insertedDataCollection = dataCollectionRepository.saveAndFlush(dataCollection);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the dataCollection
        restDataCollectionMockMvc
            .perform(delete(ENTITY_API_URL_ID, dataCollection.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return dataCollectionRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected DataCollection getPersistedDataCollection(DataCollection dataCollection) {
        return dataCollectionRepository.findById(dataCollection.getId()).orElseThrow();
    }

    protected void assertPersistedDataCollectionToMatchAllProperties(DataCollection expectedDataCollection) {
        assertDataCollectionAllPropertiesEquals(expectedDataCollection, getPersistedDataCollection(expectedDataCollection));
    }

    protected void assertPersistedDataCollectionToMatchUpdatableProperties(DataCollection expectedDataCollection) {
        assertDataCollectionAllUpdatablePropertiesEquals(expectedDataCollection, getPersistedDataCollection(expectedDataCollection));
    }
}
