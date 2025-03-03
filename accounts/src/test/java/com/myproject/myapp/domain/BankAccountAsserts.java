package com.myproject.myapp.domain;

import static com.myproject.myapp.domain.AssertUtils.bigDecimalCompareTo;
import static org.assertj.core.api.Assertions.assertThat;

public class BankAccountAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBankAccountAllPropertiesEquals(BankAccount expected, BankAccount actual) {
        assertBankAccountAutoGeneratedPropertiesEquals(expected, actual);
        assertBankAccountAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBankAccountAllUpdatablePropertiesEquals(BankAccount expected, BankAccount actual) {
        assertBankAccountUpdatableFieldsEquals(expected, actual);
        assertBankAccountUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBankAccountAutoGeneratedPropertiesEquals(BankAccount expected, BankAccount actual) {
        assertThat(expected)
            .as("Verify BankAccount auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBankAccountUpdatableFieldsEquals(BankAccount expected, BankAccount actual) {
        assertThat(expected)
            .as("Verify BankAccount relevant properties")
            .satisfies(e -> assertThat(e.getLogin()).as("check login").isEqualTo(actual.getLogin()))
            .satisfies(e -> assertThat(e.getAccountNumber()).as("check accountNumber").isEqualTo(actual.getAccountNumber()))
            .satisfies(e -> assertThat(e.getIban()).as("check iban").isEqualTo(actual.getIban()))
            .satisfies(e ->
                assertThat(e.getBalance()).as("check balance").usingComparator(bigDecimalCompareTo).isEqualTo(actual.getBalance())
            )
            .satisfies(e -> assertThat(e.getCurrency()).as("check currency").isEqualTo(actual.getCurrency()))
            .satisfies(e -> assertThat(e.getOpeningDate()).as("check openingDate").isEqualTo(actual.getOpeningDate()))
            .satisfies(e -> assertThat(e.getStatus()).as("check status").isEqualTo(actual.getStatus()))
            .satisfies(e -> assertThat(e.getAccountType()).as("check accountType").isEqualTo(actual.getAccountType()))
            .satisfies(e -> assertThat(e.getBranch()).as("check branch").isEqualTo(actual.getBranch()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertBankAccountUpdatableRelationshipsEquals(BankAccount expected, BankAccount actual) {
        // empty method
    }
}
