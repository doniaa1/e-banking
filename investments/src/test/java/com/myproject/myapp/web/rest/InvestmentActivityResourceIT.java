package com.myproject.myapp.web.rest;

import static com.myproject.myapp.domain.InvestmentActivityAsserts.*;
import static com.myproject.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.myproject.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.myapp.IntegrationTest;
import com.myproject.myapp.domain.InvestmentActivity;
import com.myproject.myapp.domain.enumeration.ActivityType;
import com.myproject.myapp.domain.enumeration.InvestmentType;
import com.myproject.myapp.domain.enumeration.RiskLevel;
import com.myproject.myapp.domain.enumeration.StatusType;
import com.myproject.myapp.repository.InvestmentActivityRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link InvestmentActivityResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class InvestmentActivityResourceIT {

    private static final InvestmentType DEFAULT_INVESTMENT_TYPE = InvestmentType.REAL_ESTATE;
    private static final InvestmentType UPDATED_INVESTMENT_TYPE = InvestmentType.BONDS;

    private static final ActivityType DEFAULT_ACTIVITY_TYPE = ActivityType.PROJECT;
    private static final ActivityType UPDATED_ACTIVITY_TYPE = ActivityType.INVESTMENT;

    private static final String DEFAULT_PROJECT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_PROJECT_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_LOCATION = "AAAAAAAAAA";
    private static final String UPDATED_LOCATION = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_TARGET_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_TARGET_AMOUNT = new BigDecimal(2);

    private static final BigDecimal DEFAULT_CURRENT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_CURRENT_AMOUNT = new BigDecimal(2);

    private static final String DEFAULT_BOND_ISSUER = "AAAAAAAAAA";
    private static final String UPDATED_BOND_ISSUER = "BBBBBBBBBB";

    private static final Instant DEFAULT_ACTIVITY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ACTIVITY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_ACTIVITY_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_ACTIVITY_AMOUNT = new BigDecimal(2);

    private static final StatusType DEFAULT_STATUS = StatusType.ACTIVE;
    private static final StatusType UPDATED_STATUS = StatusType.COMPLETED;

    private static final RiskLevel DEFAULT_RISK_LEVEL = RiskLevel.LOW;
    private static final RiskLevel UPDATED_RISK_LEVEL = RiskLevel.MEDIUM;

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

    private static final String DEFAULT_CREATED_BY = "AAAAAAAAAA";
    private static final String UPDATED_CREATED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_MODIFIED_BY = "AAAAAAAAAA";
    private static final String UPDATED_LAST_MODIFIED_BY = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_MODIFIED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_MODIFIED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String ENTITY_API_URL = "/api/investment-activities";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InvestmentActivityRepository investmentActivityRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInvestmentActivityMockMvc;

    private InvestmentActivity investmentActivity;

    private InvestmentActivity insertedInvestmentActivity;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvestmentActivity createEntity() {
        return new InvestmentActivity()
            .investmentType(DEFAULT_INVESTMENT_TYPE)
            .activityType(DEFAULT_ACTIVITY_TYPE)
            .projectName(DEFAULT_PROJECT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .location(DEFAULT_LOCATION)
            .targetAmount(DEFAULT_TARGET_AMOUNT)
            .currentAmount(DEFAULT_CURRENT_AMOUNT)
            .bondIssuer(DEFAULT_BOND_ISSUER)
            .activityDate(DEFAULT_ACTIVITY_DATE)
            .activityAmount(DEFAULT_ACTIVITY_AMOUNT)
            .status(DEFAULT_STATUS)
            .riskLevel(DEFAULT_RISK_LEVEL)
            .login(DEFAULT_LOGIN)
            .createdBy(DEFAULT_CREATED_BY)
            .createdDate(DEFAULT_CREATED_DATE)
            .lastModifiedBy(DEFAULT_LAST_MODIFIED_BY)
            .lastModifiedDate(DEFAULT_LAST_MODIFIED_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InvestmentActivity createUpdatedEntity() {
        return new InvestmentActivity()
            .investmentType(UPDATED_INVESTMENT_TYPE)
            .activityType(UPDATED_ACTIVITY_TYPE)
            .projectName(UPDATED_PROJECT_NAME)
            .description(UPDATED_DESCRIPTION)
            .location(UPDATED_LOCATION)
            .targetAmount(UPDATED_TARGET_AMOUNT)
            .currentAmount(UPDATED_CURRENT_AMOUNT)
            .bondIssuer(UPDATED_BOND_ISSUER)
            .activityDate(UPDATED_ACTIVITY_DATE)
            .activityAmount(UPDATED_ACTIVITY_AMOUNT)
            .status(UPDATED_STATUS)
            .riskLevel(UPDATED_RISK_LEVEL)
            .login(UPDATED_LOGIN)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);
    }

    @BeforeEach
    public void initTest() {
        investmentActivity = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedInvestmentActivity != null) {
            investmentActivityRepository.delete(insertedInvestmentActivity);
            insertedInvestmentActivity = null;
        }
    }

    @Test
    @Transactional
    void createInvestmentActivity() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InvestmentActivity
        var returnedInvestmentActivity = om.readValue(
            restInvestmentActivityMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(investmentActivity)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InvestmentActivity.class
        );

        // Validate the InvestmentActivity in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInvestmentActivityUpdatableFieldsEquals(
            returnedInvestmentActivity,
            getPersistedInvestmentActivity(returnedInvestmentActivity)
        );

        insertedInvestmentActivity = returnedInvestmentActivity;
    }

    @Test
    @Transactional
    void createInvestmentActivityWithExistingId() throws Exception {
        // Create the InvestmentActivity with an existing ID
        investmentActivity.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInvestmentActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(investmentActivity)))
            .andExpect(status().isBadRequest());

        // Validate the InvestmentActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkInvestmentTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        investmentActivity.setInvestmentType(null);

        // Create the InvestmentActivity, which fails.

        restInvestmentActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(investmentActivity)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivityTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        investmentActivity.setActivityType(null);

        // Create the InvestmentActivity, which fails.

        restInvestmentActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(investmentActivity)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkActivityDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        investmentActivity.setActivityDate(null);

        // Create the InvestmentActivity, which fails.

        restInvestmentActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(investmentActivity)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRiskLevelIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        investmentActivity.setRiskLevel(null);

        // Create the InvestmentActivity, which fails.

        restInvestmentActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(investmentActivity)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLoginIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        investmentActivity.setLogin(null);

        // Create the InvestmentActivity, which fails.

        restInvestmentActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(investmentActivity)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedByIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        investmentActivity.setCreatedBy(null);

        // Create the InvestmentActivity, which fails.

        restInvestmentActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(investmentActivity)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreatedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        investmentActivity.setCreatedDate(null);

        // Create the InvestmentActivity, which fails.

        restInvestmentActivityMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(investmentActivity)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInvestmentActivities() throws Exception {
        // Initialize the database
        insertedInvestmentActivity = investmentActivityRepository.saveAndFlush(investmentActivity);

        // Get all the investmentActivityList
        restInvestmentActivityMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(investmentActivity.getId().intValue())))
            .andExpect(jsonPath("$.[*].investmentType").value(hasItem(DEFAULT_INVESTMENT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].activityType").value(hasItem(DEFAULT_ACTIVITY_TYPE.toString())))
            .andExpect(jsonPath("$.[*].projectName").value(hasItem(DEFAULT_PROJECT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].location").value(hasItem(DEFAULT_LOCATION)))
            .andExpect(jsonPath("$.[*].targetAmount").value(hasItem(sameNumber(DEFAULT_TARGET_AMOUNT))))
            .andExpect(jsonPath("$.[*].currentAmount").value(hasItem(sameNumber(DEFAULT_CURRENT_AMOUNT))))
            .andExpect(jsonPath("$.[*].bondIssuer").value(hasItem(DEFAULT_BOND_ISSUER)))
            .andExpect(jsonPath("$.[*].activityDate").value(hasItem(DEFAULT_ACTIVITY_DATE.toString())))
            .andExpect(jsonPath("$.[*].activityAmount").value(hasItem(sameNumber(DEFAULT_ACTIVITY_AMOUNT))))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].riskLevel").value(hasItem(DEFAULT_RISK_LEVEL.toString())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)))
            .andExpect(jsonPath("$.[*].createdBy").value(hasItem(DEFAULT_CREATED_BY)))
            .andExpect(jsonPath("$.[*].createdDate").value(hasItem(DEFAULT_CREATED_DATE.toString())))
            .andExpect(jsonPath("$.[*].lastModifiedBy").value(hasItem(DEFAULT_LAST_MODIFIED_BY)))
            .andExpect(jsonPath("$.[*].lastModifiedDate").value(hasItem(DEFAULT_LAST_MODIFIED_DATE.toString())));
    }

    @Test
    @Transactional
    void getInvestmentActivity() throws Exception {
        // Initialize the database
        insertedInvestmentActivity = investmentActivityRepository.saveAndFlush(investmentActivity);

        // Get the investmentActivity
        restInvestmentActivityMockMvc
            .perform(get(ENTITY_API_URL_ID, investmentActivity.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(investmentActivity.getId().intValue()))
            .andExpect(jsonPath("$.investmentType").value(DEFAULT_INVESTMENT_TYPE.toString()))
            .andExpect(jsonPath("$.activityType").value(DEFAULT_ACTIVITY_TYPE.toString()))
            .andExpect(jsonPath("$.projectName").value(DEFAULT_PROJECT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.location").value(DEFAULT_LOCATION))
            .andExpect(jsonPath("$.targetAmount").value(sameNumber(DEFAULT_TARGET_AMOUNT)))
            .andExpect(jsonPath("$.currentAmount").value(sameNumber(DEFAULT_CURRENT_AMOUNT)))
            .andExpect(jsonPath("$.bondIssuer").value(DEFAULT_BOND_ISSUER))
            .andExpect(jsonPath("$.activityDate").value(DEFAULT_ACTIVITY_DATE.toString()))
            .andExpect(jsonPath("$.activityAmount").value(sameNumber(DEFAULT_ACTIVITY_AMOUNT)))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.riskLevel").value(DEFAULT_RISK_LEVEL.toString()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN))
            .andExpect(jsonPath("$.createdBy").value(DEFAULT_CREATED_BY))
            .andExpect(jsonPath("$.createdDate").value(DEFAULT_CREATED_DATE.toString()))
            .andExpect(jsonPath("$.lastModifiedBy").value(DEFAULT_LAST_MODIFIED_BY))
            .andExpect(jsonPath("$.lastModifiedDate").value(DEFAULT_LAST_MODIFIED_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingInvestmentActivity() throws Exception {
        // Get the investmentActivity
        restInvestmentActivityMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInvestmentActivity() throws Exception {
        // Initialize the database
        insertedInvestmentActivity = investmentActivityRepository.saveAndFlush(investmentActivity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the investmentActivity
        InvestmentActivity updatedInvestmentActivity = investmentActivityRepository.findById(investmentActivity.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedInvestmentActivity are not directly saved in db
        em.detach(updatedInvestmentActivity);
        updatedInvestmentActivity
            .investmentType(UPDATED_INVESTMENT_TYPE)
            .activityType(UPDATED_ACTIVITY_TYPE)
            .projectName(UPDATED_PROJECT_NAME)
            .description(UPDATED_DESCRIPTION)
            .location(UPDATED_LOCATION)
            .targetAmount(UPDATED_TARGET_AMOUNT)
            .currentAmount(UPDATED_CURRENT_AMOUNT)
            .bondIssuer(UPDATED_BOND_ISSUER)
            .activityDate(UPDATED_ACTIVITY_DATE)
            .activityAmount(UPDATED_ACTIVITY_AMOUNT)
            .status(UPDATED_STATUS)
            .riskLevel(UPDATED_RISK_LEVEL)
            .login(UPDATED_LOGIN)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restInvestmentActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInvestmentActivity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInvestmentActivity))
            )
            .andExpect(status().isOk());

        // Validate the InvestmentActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInvestmentActivityToMatchAllProperties(updatedInvestmentActivity);
    }

    @Test
    @Transactional
    void putNonExistingInvestmentActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        investmentActivity.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvestmentActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, investmentActivity.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(investmentActivity))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvestmentActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInvestmentActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        investmentActivity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvestmentActivityMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(investmentActivity))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvestmentActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInvestmentActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        investmentActivity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvestmentActivityMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(investmentActivity)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvestmentActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInvestmentActivityWithPatch() throws Exception {
        // Initialize the database
        insertedInvestmentActivity = investmentActivityRepository.saveAndFlush(investmentActivity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the investmentActivity using partial update
        InvestmentActivity partialUpdatedInvestmentActivity = new InvestmentActivity();
        partialUpdatedInvestmentActivity.setId(investmentActivity.getId());

        partialUpdatedInvestmentActivity
            .projectName(UPDATED_PROJECT_NAME)
            .description(UPDATED_DESCRIPTION)
            .location(UPDATED_LOCATION)
            .currentAmount(UPDATED_CURRENT_AMOUNT)
            .bondIssuer(UPDATED_BOND_ISSUER)
            .activityDate(UPDATED_ACTIVITY_DATE)
            .activityAmount(UPDATED_ACTIVITY_AMOUNT)
            .status(UPDATED_STATUS)
            .login(UPDATED_LOGIN)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY);

        restInvestmentActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvestmentActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvestmentActivity))
            )
            .andExpect(status().isOk());

        // Validate the InvestmentActivity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvestmentActivityUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInvestmentActivity, investmentActivity),
            getPersistedInvestmentActivity(investmentActivity)
        );
    }

    @Test
    @Transactional
    void fullUpdateInvestmentActivityWithPatch() throws Exception {
        // Initialize the database
        insertedInvestmentActivity = investmentActivityRepository.saveAndFlush(investmentActivity);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the investmentActivity using partial update
        InvestmentActivity partialUpdatedInvestmentActivity = new InvestmentActivity();
        partialUpdatedInvestmentActivity.setId(investmentActivity.getId());

        partialUpdatedInvestmentActivity
            .investmentType(UPDATED_INVESTMENT_TYPE)
            .activityType(UPDATED_ACTIVITY_TYPE)
            .projectName(UPDATED_PROJECT_NAME)
            .description(UPDATED_DESCRIPTION)
            .location(UPDATED_LOCATION)
            .targetAmount(UPDATED_TARGET_AMOUNT)
            .currentAmount(UPDATED_CURRENT_AMOUNT)
            .bondIssuer(UPDATED_BOND_ISSUER)
            .activityDate(UPDATED_ACTIVITY_DATE)
            .activityAmount(UPDATED_ACTIVITY_AMOUNT)
            .status(UPDATED_STATUS)
            .riskLevel(UPDATED_RISK_LEVEL)
            .login(UPDATED_LOGIN)
            .createdBy(UPDATED_CREATED_BY)
            .createdDate(UPDATED_CREATED_DATE)
            .lastModifiedBy(UPDATED_LAST_MODIFIED_BY)
            .lastModifiedDate(UPDATED_LAST_MODIFIED_DATE);

        restInvestmentActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInvestmentActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInvestmentActivity))
            )
            .andExpect(status().isOk());

        // Validate the InvestmentActivity in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInvestmentActivityUpdatableFieldsEquals(
            partialUpdatedInvestmentActivity,
            getPersistedInvestmentActivity(partialUpdatedInvestmentActivity)
        );
    }

    @Test
    @Transactional
    void patchNonExistingInvestmentActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        investmentActivity.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInvestmentActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, investmentActivity.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(investmentActivity))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvestmentActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInvestmentActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        investmentActivity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvestmentActivityMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(investmentActivity))
            )
            .andExpect(status().isBadRequest());

        // Validate the InvestmentActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInvestmentActivity() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        investmentActivity.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInvestmentActivityMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(investmentActivity)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InvestmentActivity in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInvestmentActivity() throws Exception {
        // Initialize the database
        insertedInvestmentActivity = investmentActivityRepository.saveAndFlush(investmentActivity);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the investmentActivity
        restInvestmentActivityMockMvc
            .perform(delete(ENTITY_API_URL_ID, investmentActivity.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return investmentActivityRepository.count();
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

    protected InvestmentActivity getPersistedInvestmentActivity(InvestmentActivity investmentActivity) {
        return investmentActivityRepository.findById(investmentActivity.getId()).orElseThrow();
    }

    protected void assertPersistedInvestmentActivityToMatchAllProperties(InvestmentActivity expectedInvestmentActivity) {
        assertInvestmentActivityAllPropertiesEquals(expectedInvestmentActivity, getPersistedInvestmentActivity(expectedInvestmentActivity));
    }

    protected void assertPersistedInvestmentActivityToMatchUpdatableProperties(InvestmentActivity expectedInvestmentActivity) {
        assertInvestmentActivityAllUpdatablePropertiesEquals(
            expectedInvestmentActivity,
            getPersistedInvestmentActivity(expectedInvestmentActivity)
        );
    }
}
