package com.myproject.myapp.web.rest;

import static com.myproject.myapp.domain.AnalysisReportAsserts.*;
import static com.myproject.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.myapp.IntegrationTest;
import com.myproject.myapp.domain.AnalysisReport;
import com.myproject.myapp.domain.DataCollection;
import com.myproject.myapp.domain.enumeration.AnalysisType;
import com.myproject.myapp.domain.enumeration.ReportType;
import com.myproject.myapp.domain.enumeration.Status;
import com.myproject.myapp.repository.AnalysisReportRepository;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link AnalysisReportResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class AnalysisReportResourceIT {

    private static final String DEFAULT_TITLE = "AAAAAAAAAA";
    private static final String UPDATED_TITLE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_AT = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_AT = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final AnalysisType DEFAULT_ANALYSIS_TYPE = AnalysisType.STATISTICAL;
    private static final AnalysisType UPDATED_ANALYSIS_TYPE = AnalysisType.GRAPHICAL;

    private static final ReportType DEFAULT_REPORT_TYPE = ReportType.PAYMENT;
    private static final ReportType UPDATED_REPORT_TYPE = ReportType.INVESTMENT;

    private static final String DEFAULT_GENERATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_GENERATED_BY = "BBBBBBBBBB";

    private static final String DEFAULT_CONTENT = "AAAAAAAAAA";
    private static final String UPDATED_CONTENT = "BBBBBBBBBB";

    private static final Status DEFAULT_STATUS = Status.ACTIVE;
    private static final Status UPDATED_STATUS = Status.ARCHIVED;

    private static final String ENTITY_API_URL = "/api/analysis-reports";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AnalysisReportRepository analysisReportRepository;

    @Mock
    private AnalysisReportRepository analysisReportRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAnalysisReportMockMvc;

    private AnalysisReport analysisReport;

    private AnalysisReport insertedAnalysisReport;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnalysisReport createEntity(EntityManager em) {
        AnalysisReport analysisReport = new AnalysisReport()
            .title(DEFAULT_TITLE)
            .createdAt(DEFAULT_CREATED_AT)
            .analysisType(DEFAULT_ANALYSIS_TYPE)
            .reportType(DEFAULT_REPORT_TYPE)
            .generatedBy(DEFAULT_GENERATED_BY)
            .content(DEFAULT_CONTENT)
            .status(DEFAULT_STATUS);
        // Add required entity
        DataCollection dataCollection;
        if (TestUtil.findAll(em, DataCollection.class).isEmpty()) {
            dataCollection = DataCollectionResourceIT.createEntity();
            em.persist(dataCollection);
            em.flush();
        } else {
            dataCollection = TestUtil.findAll(em, DataCollection.class).get(0);
        }
        analysisReport.setDataCollection(dataCollection);
        return analysisReport;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AnalysisReport createUpdatedEntity(EntityManager em) {
        AnalysisReport updatedAnalysisReport = new AnalysisReport()
            .title(UPDATED_TITLE)
            .createdAt(UPDATED_CREATED_AT)
            .analysisType(UPDATED_ANALYSIS_TYPE)
            .reportType(UPDATED_REPORT_TYPE)
            .generatedBy(UPDATED_GENERATED_BY)
            .content(UPDATED_CONTENT)
            .status(UPDATED_STATUS);
        // Add required entity
        DataCollection dataCollection;
        if (TestUtil.findAll(em, DataCollection.class).isEmpty()) {
            dataCollection = DataCollectionResourceIT.createUpdatedEntity();
            em.persist(dataCollection);
            em.flush();
        } else {
            dataCollection = TestUtil.findAll(em, DataCollection.class).get(0);
        }
        updatedAnalysisReport.setDataCollection(dataCollection);
        return updatedAnalysisReport;
    }

    @BeforeEach
    public void initTest() {
        analysisReport = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedAnalysisReport != null) {
            analysisReportRepository.delete(insertedAnalysisReport);
            insertedAnalysisReport = null;
        }
    }

    @Test
    @Transactional
    void createAnalysisReport() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AnalysisReport
        var returnedAnalysisReport = om.readValue(
            restAnalysisReportMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(analysisReport)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AnalysisReport.class
        );

        // Validate the AnalysisReport in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertAnalysisReportUpdatableFieldsEquals(returnedAnalysisReport, getPersistedAnalysisReport(returnedAnalysisReport));

        insertedAnalysisReport = returnedAnalysisReport;
    }

    @Test
    @Transactional
    void createAnalysisReportWithExistingId() throws Exception {
        // Create the AnalysisReport with an existing ID
        analysisReport.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAnalysisReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(analysisReport)))
            .andExpect(status().isBadRequest());

        // Validate the AnalysisReport in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkTitleIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        analysisReport.setTitle(null);

        // Create the AnalysisReport, which fails.

        restAnalysisReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(analysisReport)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedAtIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        analysisReport.setCreatedAt(null);

        // Create the AnalysisReport, which fails.

        restAnalysisReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(analysisReport)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAnalysisTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        analysisReport.setAnalysisType(null);

        // Create the AnalysisReport, which fails.

        restAnalysisReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(analysisReport)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkReportTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        analysisReport.setReportType(null);

        // Create the AnalysisReport, which fails.

        restAnalysisReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(analysisReport)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        analysisReport.setStatus(null);

        // Create the AnalysisReport, which fails.

        restAnalysisReportMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(analysisReport)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAnalysisReports() throws Exception {
        // Initialize the database
        insertedAnalysisReport = analysisReportRepository.saveAndFlush(analysisReport);

        // Get all the analysisReportList
        restAnalysisReportMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(analysisReport.getId().intValue())))
            .andExpect(jsonPath("$.[*].title").value(hasItem(DEFAULT_TITLE)))
            .andExpect(jsonPath("$.[*].createdAt").value(hasItem(DEFAULT_CREATED_AT.toString())))
            .andExpect(jsonPath("$.[*].analysisType").value(hasItem(DEFAULT_ANALYSIS_TYPE.toString())))
            .andExpect(jsonPath("$.[*].reportType").value(hasItem(DEFAULT_REPORT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].generatedBy").value(hasItem(DEFAULT_GENERATED_BY)))
            .andExpect(jsonPath("$.[*].content").value(hasItem(DEFAULT_CONTENT.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAnalysisReportsWithEagerRelationshipsIsEnabled() throws Exception {
        when(analysisReportRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAnalysisReportMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(analysisReportRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllAnalysisReportsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(analysisReportRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restAnalysisReportMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(analysisReportRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getAnalysisReport() throws Exception {
        // Initialize the database
        insertedAnalysisReport = analysisReportRepository.saveAndFlush(analysisReport);

        // Get the analysisReport
        restAnalysisReportMockMvc
            .perform(get(ENTITY_API_URL_ID, analysisReport.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(analysisReport.getId().intValue()))
            .andExpect(jsonPath("$.title").value(DEFAULT_TITLE))
            .andExpect(jsonPath("$.createdAt").value(DEFAULT_CREATED_AT.toString()))
            .andExpect(jsonPath("$.analysisType").value(DEFAULT_ANALYSIS_TYPE.toString()))
            .andExpect(jsonPath("$.reportType").value(DEFAULT_REPORT_TYPE.toString()))
            .andExpect(jsonPath("$.generatedBy").value(DEFAULT_GENERATED_BY))
            .andExpect(jsonPath("$.content").value(DEFAULT_CONTENT.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAnalysisReport() throws Exception {
        // Get the analysisReport
        restAnalysisReportMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAnalysisReport() throws Exception {
        // Initialize the database
        insertedAnalysisReport = analysisReportRepository.saveAndFlush(analysisReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the analysisReport
        AnalysisReport updatedAnalysisReport = analysisReportRepository.findById(analysisReport.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAnalysisReport are not directly saved in db
        em.detach(updatedAnalysisReport);
        updatedAnalysisReport
            .title(UPDATED_TITLE)
            .createdAt(UPDATED_CREATED_AT)
            .analysisType(UPDATED_ANALYSIS_TYPE)
            .reportType(UPDATED_REPORT_TYPE)
            .generatedBy(UPDATED_GENERATED_BY)
            .content(UPDATED_CONTENT)
            .status(UPDATED_STATUS);

        restAnalysisReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedAnalysisReport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedAnalysisReport))
            )
            .andExpect(status().isOk());

        // Validate the AnalysisReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAnalysisReportToMatchAllProperties(updatedAnalysisReport);
    }

    @Test
    @Transactional
    void putNonExistingAnalysisReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        analysisReport.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnalysisReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, analysisReport.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(analysisReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnalysisReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAnalysisReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        analysisReport.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnalysisReportMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(analysisReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnalysisReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAnalysisReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        analysisReport.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnalysisReportMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(analysisReport)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AnalysisReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAnalysisReportWithPatch() throws Exception {
        // Initialize the database
        insertedAnalysisReport = analysisReportRepository.saveAndFlush(analysisReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the analysisReport using partial update
        AnalysisReport partialUpdatedAnalysisReport = new AnalysisReport();
        partialUpdatedAnalysisReport.setId(analysisReport.getId());

        partialUpdatedAnalysisReport
            .title(UPDATED_TITLE)
            .analysisType(UPDATED_ANALYSIS_TYPE)
            .reportType(UPDATED_REPORT_TYPE)
            .status(UPDATED_STATUS);

        restAnalysisReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnalysisReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAnalysisReport))
            )
            .andExpect(status().isOk());

        // Validate the AnalysisReport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAnalysisReportUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAnalysisReport, analysisReport),
            getPersistedAnalysisReport(analysisReport)
        );
    }

    @Test
    @Transactional
    void fullUpdateAnalysisReportWithPatch() throws Exception {
        // Initialize the database
        insertedAnalysisReport = analysisReportRepository.saveAndFlush(analysisReport);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the analysisReport using partial update
        AnalysisReport partialUpdatedAnalysisReport = new AnalysisReport();
        partialUpdatedAnalysisReport.setId(analysisReport.getId());

        partialUpdatedAnalysisReport
            .title(UPDATED_TITLE)
            .createdAt(UPDATED_CREATED_AT)
            .analysisType(UPDATED_ANALYSIS_TYPE)
            .reportType(UPDATED_REPORT_TYPE)
            .generatedBy(UPDATED_GENERATED_BY)
            .content(UPDATED_CONTENT)
            .status(UPDATED_STATUS);

        restAnalysisReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAnalysisReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAnalysisReport))
            )
            .andExpect(status().isOk());

        // Validate the AnalysisReport in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAnalysisReportUpdatableFieldsEquals(partialUpdatedAnalysisReport, getPersistedAnalysisReport(partialUpdatedAnalysisReport));
    }

    @Test
    @Transactional
    void patchNonExistingAnalysisReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        analysisReport.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAnalysisReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, analysisReport.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(analysisReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnalysisReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAnalysisReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        analysisReport.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnalysisReportMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(analysisReport))
            )
            .andExpect(status().isBadRequest());

        // Validate the AnalysisReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAnalysisReport() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        analysisReport.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAnalysisReportMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(analysisReport)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AnalysisReport in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAnalysisReport() throws Exception {
        // Initialize the database
        insertedAnalysisReport = analysisReportRepository.saveAndFlush(analysisReport);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the analysisReport
        restAnalysisReportMockMvc
            .perform(delete(ENTITY_API_URL_ID, analysisReport.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return analysisReportRepository.count();
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

    protected AnalysisReport getPersistedAnalysisReport(AnalysisReport analysisReport) {
        return analysisReportRepository.findById(analysisReport.getId()).orElseThrow();
    }

    protected void assertPersistedAnalysisReportToMatchAllProperties(AnalysisReport expectedAnalysisReport) {
        assertAnalysisReportAllPropertiesEquals(expectedAnalysisReport, getPersistedAnalysisReport(expectedAnalysisReport));
    }

    protected void assertPersistedAnalysisReportToMatchUpdatableProperties(AnalysisReport expectedAnalysisReport) {
        assertAnalysisReportAllUpdatablePropertiesEquals(expectedAnalysisReport, getPersistedAnalysisReport(expectedAnalysisReport));
    }
}
