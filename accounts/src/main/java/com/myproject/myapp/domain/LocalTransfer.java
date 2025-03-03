package com.myproject.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * A LocalTransfer.
 */
@Entity
@Table(name = "local_transfer")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class LocalTransfer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "sender_account_number", nullable = false)
    private String senderAccountNumber;

    @NotNull
    @Column(name = "recipient_account_number", nullable = false)
    private String recipientAccountNumber;

    @NotNull
    @Column(name = "recipient_bank_name", nullable = false)
    private String recipientBankName;

    @Size(max = 50)
    @Column(name = "recipient_bank_branch", length = 50)
    private String recipientBankBranch;

    @NotNull
    @Column(name = "amount", precision = 21, scale = 2, nullable = false)
    private BigDecimal amount;

    @NotNull
    @Column(name = "transaction_date", nullable = false)
    private Instant transactionDate;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "localTransfers", "internationalTransfers" }, allowSetters = true)
    private BankAccount bankAccount;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public LocalTransfer id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenderAccountNumber() {
        return this.senderAccountNumber;
    }

    public LocalTransfer senderAccountNumber(String senderAccountNumber) {
        this.setSenderAccountNumber(senderAccountNumber);
        return this;
    }

    public void setSenderAccountNumber(String senderAccountNumber) {
        this.senderAccountNumber = senderAccountNumber;
    }

    public String getRecipientAccountNumber() {
        return this.recipientAccountNumber;
    }

    public LocalTransfer recipientAccountNumber(String recipientAccountNumber) {
        this.setRecipientAccountNumber(recipientAccountNumber);
        return this;
    }

    public void setRecipientAccountNumber(String recipientAccountNumber) {
        this.recipientAccountNumber = recipientAccountNumber;
    }

    public String getRecipientBankName() {
        return this.recipientBankName;
    }

    public LocalTransfer recipientBankName(String recipientBankName) {
        this.setRecipientBankName(recipientBankName);
        return this;
    }

    public void setRecipientBankName(String recipientBankName) {
        this.recipientBankName = recipientBankName;
    }

    public String getRecipientBankBranch() {
        return this.recipientBankBranch;
    }

    public LocalTransfer recipientBankBranch(String recipientBankBranch) {
        this.setRecipientBankBranch(recipientBankBranch);
        return this;
    }

    public void setRecipientBankBranch(String recipientBankBranch) {
        this.recipientBankBranch = recipientBankBranch;
    }

    public BigDecimal getAmount() {
        return this.amount;
    }

    public LocalTransfer amount(BigDecimal amount) {
        this.setAmount(amount);
        return this;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public Instant getTransactionDate() {
        return this.transactionDate;
    }

    public LocalTransfer transactionDate(Instant transactionDate) {
        this.setTransactionDate(transactionDate);
        return this;
    }

    public void setTransactionDate(Instant transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getDescription() {
        return this.description;
    }

    public LocalTransfer description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BankAccount getBankAccount() {
        return this.bankAccount;
    }

    public void setBankAccount(BankAccount bankAccount) {
        this.bankAccount = bankAccount;
    }

    public LocalTransfer bankAccount(BankAccount bankAccount) {
        this.setBankAccount(bankAccount);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof LocalTransfer)) {
            return false;
        }
        return getId() != null && getId().equals(((LocalTransfer) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "LocalTransfer{" +
            "id=" + getId() +
            ", senderAccountNumber='" + getSenderAccountNumber() + "'" +
            ", recipientAccountNumber='" + getRecipientAccountNumber() + "'" +
            ", recipientBankName='" + getRecipientBankName() + "'" +
            ", recipientBankBranch='" + getRecipientBankBranch() + "'" +
            ", amount=" + getAmount() +
            ", transactionDate='" + getTransactionDate() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
