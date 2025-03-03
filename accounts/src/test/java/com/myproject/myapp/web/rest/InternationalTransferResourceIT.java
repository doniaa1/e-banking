package com.myproject.myapp.web.rest;

import static com.myproject.myapp.domain.InternationalTransferAsserts.*;
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
import com.myproject.myapp.domain.InternationalTransfer;
import com.myproject.myapp.domain.enumeration.CurrencyType;
import com.myproject.myapp.repository.InternationalTransferRepository;
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
 * Integration tests for the {@link InternationalTransferResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class InternationalTransferResourceIT {

    private static final String DEFAULT_SENDER_ACCOUNT_NUMBER = "AAAAAAAAAA";
    private static final String UPDATED_SENDER_ACCOUNT_NUMBER = "BBBBBBBBBB";

    private static final String DEFAULT_RECIPIENT_IBAN = "AAAAAAAAAAAAAAA";
    private static final String UPDATED_RECIPIENT_IBAN = "BBBBBBBBBBBBBBB";

    private static final String DEFAULT_SWIFT_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SWIFT_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_RECIPIENT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_RECIPIENT_NAME = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_AMOUNT = new BigDecimal(2);

    private static final CurrencyType DEFAULT_CURRENCY = CurrencyType.USD;
    private static final CurrencyType UPDATED_CURRENCY = CurrencyType.EUR;

    private static final Instant DEFAULT_TRANSACTION_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_TRANSACTION_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/international-transfers";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private InternationalTransferRepository internationalTransferRepository;

    @Mock
    private InternationalTransferRepository internationalTransferRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restInternationalTransferMockMvc;

    private InternationalTransfer internationalTransfer;

    private InternationalTransfer insertedInternationalTransfer;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InternationalTransfer createEntity(EntityManager em) {
        InternationalTransfer internationalTransfer = new InternationalTransfer()
            .senderAccountNumber(DEFAULT_SENDER_ACCOUNT_NUMBER)
            .recipientIban(DEFAULT_RECIPIENT_IBAN)
            .swiftCode(DEFAULT_SWIFT_CODE)
            .recipientName(DEFAULT_RECIPIENT_NAME)
            .amount(DEFAULT_AMOUNT)
            .currency(DEFAULT_CURRENCY)
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
        internationalTransfer.setBankAccount(bankAccount);
        return internationalTransfer;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static InternationalTransfer createUpdatedEntity(EntityManager em) {
        InternationalTransfer updatedInternationalTransfer = new InternationalTransfer()
            .senderAccountNumber(UPDATED_SENDER_ACCOUNT_NUMBER)
            .recipientIban(UPDATED_RECIPIENT_IBAN)
            .swiftCode(UPDATED_SWIFT_CODE)
            .recipientName(UPDATED_RECIPIENT_NAME)
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
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
        updatedInternationalTransfer.setBankAccount(bankAccount);
        return updatedInternationalTransfer;
    }

    @BeforeEach
    public void initTest() {
        internationalTransfer = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedInternationalTransfer != null) {
            internationalTransferRepository.delete(insertedInternationalTransfer);
            insertedInternationalTransfer = null;
        }
    }

    @Test
    @Transactional
    void createInternationalTransfer() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the InternationalTransfer
        var returnedInternationalTransfer = om.readValue(
            restInternationalTransferMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(internationalTransfer)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            InternationalTransfer.class
        );

        // Validate the InternationalTransfer in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertInternationalTransferUpdatableFieldsEquals(
            returnedInternationalTransfer,
            getPersistedInternationalTransfer(returnedInternationalTransfer)
        );

        insertedInternationalTransfer = returnedInternationalTransfer;
    }

    @Test
    @Transactional
    void createInternationalTransferWithExistingId() throws Exception {
        // Create the InternationalTransfer with an existing ID
        internationalTransfer.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restInternationalTransferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(internationalTransfer)))
            .andExpect(status().isBadRequest());

        // Validate the InternationalTransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSenderAccountNumberIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        internationalTransfer.setSenderAccountNumber(null);

        // Create the InternationalTransfer, which fails.

        restInternationalTransferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(internationalTransfer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRecipientIbanIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        internationalTransfer.setRecipientIban(null);

        // Create the InternationalTransfer, which fails.

        restInternationalTransferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(internationalTransfer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSwiftCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        internationalTransfer.setSwiftCode(null);

        // Create the InternationalTransfer, which fails.

        restInternationalTransferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(internationalTransfer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkRecipientNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        internationalTransfer.setRecipientName(null);

        // Create the InternationalTransfer, which fails.

        restInternationalTransferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(internationalTransfer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAmountIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        internationalTransfer.setAmount(null);

        // Create the InternationalTransfer, which fails.

        restInternationalTransferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(internationalTransfer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCurrencyIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        internationalTransfer.setCurrency(null);

        // Create the InternationalTransfer, which fails.

        restInternationalTransferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(internationalTransfer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTransactionDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        internationalTransfer.setTransactionDate(null);

        // Create the InternationalTransfer, which fails.

        restInternationalTransferMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(internationalTransfer)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllInternationalTransfers() throws Exception {
        // Initialize the database
        insertedInternationalTransfer = internationalTransferRepository.saveAndFlush(internationalTransfer);

        // Get all the internationalTransferList
        restInternationalTransferMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(internationalTransfer.getId().intValue())))
            .andExpect(jsonPath("$.[*].senderAccountNumber").value(hasItem(DEFAULT_SENDER_ACCOUNT_NUMBER)))
            .andExpect(jsonPath("$.[*].recipientIban").value(hasItem(DEFAULT_RECIPIENT_IBAN)))
            .andExpect(jsonPath("$.[*].swiftCode").value(hasItem(DEFAULT_SWIFT_CODE)))
            .andExpect(jsonPath("$.[*].recipientName").value(hasItem(DEFAULT_RECIPIENT_NAME)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(sameNumber(DEFAULT_AMOUNT))))
            .andExpect(jsonPath("$.[*].currency").value(hasItem(DEFAULT_CURRENCY.toString())))
            .andExpect(jsonPath("$.[*].transactionDate").value(hasItem(DEFAULT_TRANSACTION_DATE.toString())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInternationalTransfersWithEagerRelationshipsIsEnabled() throws Exception {
        when(internationalTransferRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInternationalTransferMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(internationalTransferRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllInternationalTransfersWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(internationalTransferRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restInternationalTransferMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(internationalTransferRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getInternationalTransfer() throws Exception {
        // Initialize the database
        insertedInternationalTransfer = internationalTransferRepository.saveAndFlush(internationalTransfer);

        // Get the internationalTransfer
        restInternationalTransferMockMvc
            .perform(get(ENTITY_API_URL_ID, internationalTransfer.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(internationalTransfer.getId().intValue()))
            .andExpect(jsonPath("$.senderAccountNumber").value(DEFAULT_SENDER_ACCOUNT_NUMBER))
            .andExpect(jsonPath("$.recipientIban").value(DEFAULT_RECIPIENT_IBAN))
            .andExpect(jsonPath("$.swiftCode").value(DEFAULT_SWIFT_CODE))
            .andExpect(jsonPath("$.recipientName").value(DEFAULT_RECIPIENT_NAME))
            .andExpect(jsonPath("$.amount").value(sameNumber(DEFAULT_AMOUNT)))
            .andExpect(jsonPath("$.currency").value(DEFAULT_CURRENCY.toString()))
            .andExpect(jsonPath("$.transactionDate").value(DEFAULT_TRANSACTION_DATE.toString()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION));
    }

    @Test
    @Transactional
    void getNonExistingInternationalTransfer() throws Exception {
        // Get the internationalTransfer
        restInternationalTransferMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingInternationalTransfer() throws Exception {
        // Initialize the database
        insertedInternationalTransfer = internationalTransferRepository.saveAndFlush(internationalTransfer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the internationalTransfer
        InternationalTransfer updatedInternationalTransfer = internationalTransferRepository
            .findById(internationalTransfer.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedInternationalTransfer are not directly saved in db
        em.detach(updatedInternationalTransfer);
        updatedInternationalTransfer
            .senderAccountNumber(UPDATED_SENDER_ACCOUNT_NUMBER)
            .recipientIban(UPDATED_RECIPIENT_IBAN)
            .swiftCode(UPDATED_SWIFT_CODE)
            .recipientName(UPDATED_RECIPIENT_NAME)
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .description(UPDATED_DESCRIPTION);

        restInternationalTransferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedInternationalTransfer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedInternationalTransfer))
            )
            .andExpect(status().isOk());

        // Validate the InternationalTransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedInternationalTransferToMatchAllProperties(updatedInternationalTransfer);
    }

    @Test
    @Transactional
    void putNonExistingInternationalTransfer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        internationalTransfer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInternationalTransferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, internationalTransfer.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(internationalTransfer))
            )
            .andExpect(status().isBadRequest());

        // Validate the InternationalTransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchInternationalTransfer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        internationalTransfer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInternationalTransferMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(internationalTransfer))
            )
            .andExpect(status().isBadRequest());

        // Validate the InternationalTransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamInternationalTransfer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        internationalTransfer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInternationalTransferMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(internationalTransfer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InternationalTransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateInternationalTransferWithPatch() throws Exception {
        // Initialize the database
        insertedInternationalTransfer = internationalTransferRepository.saveAndFlush(internationalTransfer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the internationalTransfer using partial update
        InternationalTransfer partialUpdatedInternationalTransfer = new InternationalTransfer();
        partialUpdatedInternationalTransfer.setId(internationalTransfer.getId());

        partialUpdatedInternationalTransfer
            .senderAccountNumber(UPDATED_SENDER_ACCOUNT_NUMBER)
            .recipientIban(UPDATED_RECIPIENT_IBAN)
            .swiftCode(UPDATED_SWIFT_CODE)
            .currency(UPDATED_CURRENCY);

        restInternationalTransferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInternationalTransfer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInternationalTransfer))
            )
            .andExpect(status().isOk());

        // Validate the InternationalTransfer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInternationalTransferUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedInternationalTransfer, internationalTransfer),
            getPersistedInternationalTransfer(internationalTransfer)
        );
    }

    @Test
    @Transactional
    void fullUpdateInternationalTransferWithPatch() throws Exception {
        // Initialize the database
        insertedInternationalTransfer = internationalTransferRepository.saveAndFlush(internationalTransfer);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the internationalTransfer using partial update
        InternationalTransfer partialUpdatedInternationalTransfer = new InternationalTransfer();
        partialUpdatedInternationalTransfer.setId(internationalTransfer.getId());

        partialUpdatedInternationalTransfer
            .senderAccountNumber(UPDATED_SENDER_ACCOUNT_NUMBER)
            .recipientIban(UPDATED_RECIPIENT_IBAN)
            .swiftCode(UPDATED_SWIFT_CODE)
            .recipientName(UPDATED_RECIPIENT_NAME)
            .amount(UPDATED_AMOUNT)
            .currency(UPDATED_CURRENCY)
            .transactionDate(UPDATED_TRANSACTION_DATE)
            .description(UPDATED_DESCRIPTION);

        restInternationalTransferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedInternationalTransfer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedInternationalTransfer))
            )
            .andExpect(status().isOk());

        // Validate the InternationalTransfer in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertInternationalTransferUpdatableFieldsEquals(
            partialUpdatedInternationalTransfer,
            getPersistedInternationalTransfer(partialUpdatedInternationalTransfer)
        );
    }

    @Test
    @Transactional
    void patchNonExistingInternationalTransfer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        internationalTransfer.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restInternationalTransferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, internationalTransfer.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(internationalTransfer))
            )
            .andExpect(status().isBadRequest());

        // Validate the InternationalTransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchInternationalTransfer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        internationalTransfer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInternationalTransferMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(internationalTransfer))
            )
            .andExpect(status().isBadRequest());

        // Validate the InternationalTransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamInternationalTransfer() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        internationalTransfer.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restInternationalTransferMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(internationalTransfer)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the InternationalTransfer in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteInternationalTransfer() throws Exception {
        // Initialize the database
        insertedInternationalTransfer = internationalTransferRepository.saveAndFlush(internationalTransfer);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the internationalTransfer
        restInternationalTransferMockMvc
            .perform(delete(ENTITY_API_URL_ID, internationalTransfer.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return internationalTransferRepository.count();
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

    protected InternationalTransfer getPersistedInternationalTransfer(InternationalTransfer internationalTransfer) {
        return internationalTransferRepository.findById(internationalTransfer.getId()).orElseThrow();
    }

    protected void assertPersistedInternationalTransferToMatchAllProperties(InternationalTransfer expectedInternationalTransfer) {
        assertInternationalTransferAllPropertiesEquals(
            expectedInternationalTransfer,
            getPersistedInternationalTransfer(expectedInternationalTransfer)
        );
    }

    protected void assertPersistedInternationalTransferToMatchUpdatableProperties(InternationalTransfer expectedInternationalTransfer) {
        assertInternationalTransferAllUpdatablePropertiesEquals(
            expectedInternationalTransfer,
            getPersistedInternationalTransfer(expectedInternationalTransfer)
        );
    }
}
