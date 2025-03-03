package com.myproject.myapp.domain;

import static com.myproject.myapp.domain.BankAccountTestSamples.*;
import static com.myproject.myapp.domain.InternationalTransferTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.myproject.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InternationalTransferTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InternationalTransfer.class);
        InternationalTransfer internationalTransfer1 = getInternationalTransferSample1();
        InternationalTransfer internationalTransfer2 = new InternationalTransfer();
        assertThat(internationalTransfer1).isNotEqualTo(internationalTransfer2);

        internationalTransfer2.setId(internationalTransfer1.getId());
        assertThat(internationalTransfer1).isEqualTo(internationalTransfer2);

        internationalTransfer2 = getInternationalTransferSample2();
        assertThat(internationalTransfer1).isNotEqualTo(internationalTransfer2);
    }

    @Test
    void bankAccountTest() {
        InternationalTransfer internationalTransfer = getInternationalTransferRandomSampleGenerator();
        BankAccount bankAccountBack = getBankAccountRandomSampleGenerator();

        internationalTransfer.setBankAccount(bankAccountBack);
        assertThat(internationalTransfer.getBankAccount()).isEqualTo(bankAccountBack);

        internationalTransfer.bankAccount(null);
        assertThat(internationalTransfer.getBankAccount()).isNull();
    }
}
