package com.myproject.myapp.web.rest;

import static com.myproject.myapp.domain.LocalTransferAsserts.*;
import static com.myproject.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static com.myproject.myapp.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myproject.myapp.IntegrationTest;
import com.myproject.myapp.domain.BankAccount;
import com.myproject.myapp.domain.LocalTransfer;
import com.myproject.myapp.repository.LocalTransferRepository;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link LocalTransferResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class LocalTransferResourceIT {

    private static final String DEFAULT_SENDER_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SENDER_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_RECIPIENT_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_RECIPIENT_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_RECIPIENT_BANK_NAME = "AAAAAAAAAA";
    private static final String UPDATED_RECIPIENT_BANK_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_RECIPIENT_BANK_BRANCH = "AAAAAAAAAA";
    private static final String UPDATED_RECIPIENT_BANK_BRANCH = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final Instant DEFAULT_TRANSACTION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TRANSACTION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/local-transfers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private LocalTransferRepository localTransferRepository;

    @Mock
    private LocalTransferRepository localTransferRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restLocalTransferMockMvc;

    private LocalTransfer localTransfer;

    private LocalTransfer insertedLocalTransfer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocalTransfer createEntity(EntityManager em) {
        LocalTransfer localTransfer = new LocalTransfer()
            .senderAccountNumber(DEFAULT_SENDER_ACCOUNT_NUMBER)
            .recipientAccountNumber(DEFAULT_RECIPIENT_ACCOUNT_NUMBER)
            .recipientBankName(DEFAULT_RECIPIENT_BANK_NAME)
            .recipientBankBranch(DEFAULT_RECIPIENT_BANK_BRANCH)
            .amount(DEFAULT_AMOUNT)
            .transactionDate(DEFAULT_TRANSACTION_DATE)
            .description(DEFAULT_DESCRIPTION);
        // Add required entity
        BankAccount bankAccount;
        if (TestUtil.findAll(em, BankAccount.class).isEmpty()) {
            bankAccount = BankAccountResourceIT.createEntity();
            em.persist(bankAccount);
            em.flush();
        } else {
            bankAccount = TestUtil.findAll(em, BankAccount.class).get(0);
        }
        localTransfer.setBankAccount(bankAccount);
        return localTransfer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static LocalTransfer createUpdatedEntity(EntityManager em) {
        LocalTransfer updatedLocalTransfer = new LocalTransfer()
            .senderAccountNumber(UPDATED_SENDER_ACCOUNT_NUMBER)
            .recipientAccountNumber(UPDATED_RECIPIENT_ACCOUNT_NUMBER)
            .recipientBankName(UPDATED_RECIPIENT_BANK_NAME)
            .recipientBankBranch(UPDATED_RECIPIENT_BANK_BRANCH)
            .amount(UPDATED_AMOUNT)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .description(UPDATED_DESCRIPTION);
        // Add required entity
        BankAccount bankAccount;
        if (TestUtil.findAll(em, BankAccount.class).isEmpty()) {
            bankAccount = BankAccountResourceIT.createUpdatedEntity();
            em.persist(bankAccount);
            em.flush();
        } else {
            bankAccount = TestUtil.findAll(em, BankAccount.class).get(0);
        }
        updatedLocalTransfer.setBankAccount(bankAccount);
        return updatedLocalTransfer;
    }

    @BeforeEach
    public void initTest() {
        localTransfer = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedLocalTransfer != null) {
            localTransferRepository.delete(insertedLocalTransfer);
            insertedLocalTransfer = null;
        }
    }

    @Test
    @Transactional
    void createLocalTransfer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the LocalTransfer
        var returnedLocalTransfer = om.readValue(
            restLocalTransferMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(localTransfer)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            LocalTransfer.class
        );

        // Validate the LocalTransfer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertLocalTransferUpdatableFieldsEquals(returnedLocalTransfer, getPersistedLocalTransfer(returnedLocalTransfer));

        insertedLocalTransfer = returnedLocalTransfer;
    }

    @Test
    @Transactional
    void createLocalTransferWithExistingId() throws Exception {
        // Create the LocalTransfer with an existing ID
        localTransfer.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restLocalTransferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(localTransfer)))
            .andExpect(status().isBadRequest());

        // Validate the LocalTransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSenderAccountNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        localTransfer.setSenderAccountNumber(null);

        // Create the LocalTransfer, which fails.

        restLocalTransferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(localTransfer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRecipientAccountNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        localTransfer.setRecipientAccountNumber(null);

        // Create the LocalTransfer, which fails.

        restLocalTransferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(localTransfer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRecipientBankNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        localTransfer.setRecipientBankName(null);

        // Create the LocalTransfer, which fails.

        restLocalTransferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(localTransfer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        localTransfer.setAmount(null);

        // Create the LocalTransfer, which fails.

        restLocalTransferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(localTransfer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        localTransfer.setTransactionDate(null);

        // Create the LocalTransfer, which fails.

        restLocalTransferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(localTransfer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllLocalTransfers() throws Exception {
        // Initialize the database
        insertedLocalTransfer = localTransferRepository.saveAndFlush(localTransfer);

        // Get all the localTransferList
        restLocalTransferMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(localTransfer.getId().intValue())))
            .andExpect(jsonPath("$.[*].senderAccountNumber").value(hasItem(DEFAULT_SENDER_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].recipientAccountNumber").value(hasItem(DEFAULT_RECIPIENT_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].recipientBankName").value(hasItem(DEFAULT_RECIPIENT_BANK_NAME)))
            .andExpect(jsonPath("$.[*].recipientBankBranch").value(hasItem(DEFAULT_RECIPIENT_BANK_BRANCH)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLocalTransfersWithEagerRelationshipsIsEnabled() throws Exception {
        when(localTransferRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLocalTransferMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(localTransferRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllLocalTransfersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(localTransferRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restLocalTransferMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(localTransferRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getLocalTransfer() throws Exception {
        // Initialize the database
        insertedLocalTransfer = localTransferRepository.saveAndFlush(localTransfer);

        // Get the localTransfer
        restLocalTransferMockMvc
            .perform(get(ENTITY_API_URL_ID, localTransfer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(localTransfer.getId().intValue()))
            .andExpect(jsonPath("$.senderAccountNumber").value(DEFAULT_SENDER_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.recipientAccountNumber").value(DEFAULT_RECIPIENT_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.recipientBankName").value(DEFAULT_RECIPIENT_BANK_NAME))
            .andExpect(jsonPath("$.recipientBankBranch").value(DEFAULT_RECIPIENT_BANK_BRANCH))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingLocalTransfer() throws Exception {
        // Get the localTransfer
        restLocalTransferMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingLocalTransfer() throws Exception {
        // Initialize the database
        insertedLocalTransfer = localTransferRepository.saveAndFlush(localTransfer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the localTransfer
        LocalTransfer updatedLocalTransfer = localTransferRepository.findById(localTransfer.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedLocalTransfer are not directly saved in db
        em.detach(updatedLocalTransfer);
        updatedLocalTransfer
            .senderAccountNumber(UPDATED_SENDER_ACCOUNT_NUMBER)
            .recipientAccountNumber(UPDATED_RECIPIENT_ACCOUNT_NUMBER)
            .recipientBankName(UPDATED_RECIPIENT_BANK_NAME)
            .recipientBankBranch(UPDATED_RECIPIENT_BANK_BRANCH)
            .amount(UPDATED_AMOUNT)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .description(UPDATED_DESCRIPTION);

        restLocalTransferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedLocalTransfer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedLocalTransfer))
            )
            .andExpect(status().isOk());

        // Validate the LocalTransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedLocalTransferToMatchAllProperties(updatedLocalTransfer);
    }

    @Test
    @Transactional
    void putNonExistingLocalTransfer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        localTransfer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocalTransferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, localTransfer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(localTransfer))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocalTransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchLocalTransfer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        localTransfer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalTransferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(localTransfer))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocalTransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamLocalTransfer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        localTransfer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalTransferMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(localTransfer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LocalTransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateLocalTransferWithPatch() throws Exception {
        // Initialize the database
        insertedLocalTransfer = localTransferRepository.saveAndFlush(localTransfer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the localTransfer using partial update
        LocalTransfer partialUpdatedLocalTransfer = new LocalTransfer();
        partialUpdatedLocalTransfer.setId(localTransfer.getId());

        partialUpdatedLocalTransfer
            .senderAccountNumber(UPDATED_SENDER_ACCOUNT_NUMBER)
            .recipientBankName(UPDATED_RECIPIENT_BANK_NAME)
            .amount(UPDATED_AMOUNT)
            .description(UPDATED_DESCRIPTION);

        restLocalTransferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocalTransfer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLocalTransfer))
            )
            .andExpect(status().isOk());

        // Validate the LocalTransfer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLocalTransferUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedLocalTransfer, localTransfer),
            getPersistedLocalTransfer(localTransfer)
        );
    }

    @Test
    @Transactional
    void fullUpdateLocalTransferWithPatch() throws Exception {
        // Initialize the database
        insertedLocalTransfer = localTransferRepository.saveAndFlush(localTransfer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the localTransfer using partial update
        LocalTransfer partialUpdatedLocalTransfer = new LocalTransfer();
        partialUpdatedLocalTransfer.setId(localTransfer.getId());

        partialUpdatedLocalTransfer
            .senderAccountNumber(UPDATED_SENDER_ACCOUNT_NUMBER)
            .recipientAccountNumber(UPDATED_RECIPIENT_ACCOUNT_NUMBER)
            .recipientBankName(UPDATED_RECIPIENT_BANK_NAME)
            .recipientBankBranch(UPDATED_RECIPIENT_BANK_BRANCH)
            .amount(UPDATED_AMOUNT)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .description(UPDATED_DESCRIPTION);

        restLocalTransferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedLocalTransfer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedLocalTransfer))
            )
            .andExpect(status().isOk());

        // Validate the LocalTransfer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertLocalTransferUpdatableFieldsEquals(partialUpdatedLocalTransfer, getPersistedLocalTransfer(partialUpdatedLocalTransfer));
    }

    @Test
    @Transactional
    void patchNonExistingLocalTransfer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        localTransfer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restLocalTransferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, localTransfer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(localTransfer))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocalTransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchLocalTransfer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        localTransfer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalTransferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(localTransfer))
            )
            .andExpect(status().isBadRequest());

        // Validate the LocalTransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamLocalTransfer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        localTransfer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restLocalTransferMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(localTransfer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the LocalTransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteLocalTransfer() throws Exception {
        // Initialize the database
        insertedLocalTransfer = localTransferRepository.saveAndFlush(localTransfer);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the localTransfer
        restLocalTransferMockMvc
            .perform(delete(ENTITY_API_URL_ID, localTransfer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return localTransferRepository.count();
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

    protected LocalTransfer getPersistedLocalTransfer(LocalTransfer localTransfer) {
        return localTransferRepository.findById(localTransfer.getId()).orElseThrow();
    }

    protected void assertPersistedLocalTransferToMatchAllProperties(LocalTransfer expectedLocalTransfer) {
        assertLocalTransferAllPropertiesEquals(expectedLocalTransfer, getPersistedLocalTransfer(expectedLocalTransfer));
    }

    protected void assertPersistedLocalTransferToMatchUpdatableProperties(LocalTransfer expectedLocalTransfer) {
        assertLocalTransferAllUpdatablePropertiesEquals(expectedLocalTransfer, getPersistedLocalTransfer(expectedLocalTransfer));
    }
}
