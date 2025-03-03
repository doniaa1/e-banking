package com.myproject.myapp.domain;

import static com.myproject.myapp.domain.BankAccountTestSamples.*;
import static com.myproject.myapp.domain.InternationalTransferTestSamples.*;
import static com.myproject.myapp.domain.LocalTransferTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.myproject.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class BankAccountTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BankAccount.class);
        BankAccount bankAccount1 = getBankAccountSample1();
        BankAccount bankAccount2 = new BankAccount();
        assertThat(bankAccount1).isNotEqualTo(bankAccount2);

        bankAccount2.setId(bankAccount1.getId());
        assertThat(bankAccount1).isEqualTo(bankAccount2);

        bankAccount2 = getBankAccountSample2();
        assertThat(bankAccount1).isNotEqualTo(bankAccount2);
    }

    @Test
    void localTransferTest() {
        BankAccount bankAccount = getBankAccountRandomSampleGenerator();
        LocalTransfer localTransferBack = getLocalTransferRandomSampleGenerator();

        bankAccount.addLocalTransfer(localTransferBack);
        assertThat(bankAccount.getLocalTransfers()).containsOnly(localTransferBack);
        assertThat(localTransferBack.getBankAccount()).isEqualTo(bankAccount);

        bankAccount.removeLocalTransfer(localTransferBack);
        assertThat(bankAccount.getLocalTransfers()).doesNotContain(localTransferBack);
        assertThat(localTransferBack.getBankAccount()).isNull();

        bankAccount.localTransfers(new HashSet<>(Set.of(localTransferBack)));
        assertThat(bankAccount.getLocalTransfers()).containsOnly(localTransferBack);
        assertThat(localTransferBack.getBankAccount()).isEqualTo(bankAccount);

        bankAccount.setLocalTransfers(new HashSet<>());
        assertThat(bankAccount.getLocalTransfers()).doesNotContain(localTransferBack);
        assertThat(localTransferBack.getBankAccount()).isNull();
    }

    @Test
    void internationalTransferTest() {
        BankAccount bankAccount = getBankAccountRandomSampleGenerator();
        InternationalTransfer internationalTransferBack = getInternationalTransferRandomSampleGenerator();

        bankAccount.addInternationalTransfer(internationalTransferBack);
        assertThat(bankAccount.getInternationalTransfers()).containsOnly(internationalTransferBack);
        assertThat(internationalTransferBack.getBankAccount()).isEqualTo(bankAccount);

        bankAccount.removeInternationalTransfer(internationalTransferBack);
        assertThat(bankAccount.getInternationalTransfers()).doesNotContain(internationalTransferBack);
        assertThat(internationalTransferBack.getBankAccount()).isNull();

        bankAccount.internationalTransfers(new HashSet<>(Set.of(internationalTransferBack)));
        assertThat(bankAccount.getInternationalTransfers()).containsOnly(internationalTransferBack);
        assertThat(internationalTransferBack.getBankAccount()).isEqualTo(bankAccount);

        bankAccount.setInternationalTransfers(new HashSet<>());
        assertThat(bankAccount.getInternationalTransfers()).doesNotContain(internationalTransferBack);
        assertThat(internationalTransferBack.getBankAccount()).isNull();
    }
}
