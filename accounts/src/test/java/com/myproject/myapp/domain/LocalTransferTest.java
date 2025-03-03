package com.myproject.myapp.domain;

import static com.myproject.myapp.domain.BankAccountTestSamples.*;
import static com.myproject.myapp.domain.LocalTransferTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.myproject.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class LocalTransferTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(LocalTransfer.class);
        LocalTransfer localTransfer1 = getLocalTransferSample1();
        LocalTransfer localTransfer2 = new LocalTransfer();
        assertThat(localTransfer1).isNotEqualTo(localTransfer2);

        localTransfer2.setId(localTransfer1.getId());
        assertThat(localTransfer1).isEqualTo(localTransfer2);

        localTransfer2 = getLocalTransferSample2();
        assertThat(localTransfer1).isNotEqualTo(localTransfer2);
    }

    @Test
    void bankAccountTest() {
        LocalTransfer localTransfer = getLocalTransferRandomSampleGenerator();
        BankAccount bankAccountBack = getBankAccountRandomSampleGenerator();

        localTransfer.setBankAccount(bankAccountBack);
        assertThat(localTransfer.getBankAccount()).isEqualTo(bankAccountBack);

        localTransfer.bankAccount(null);
        assertThat(localTransfer.getBankAccount()).isNull();
    }
}
