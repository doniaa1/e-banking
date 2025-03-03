package com.myproject.myapp.domain;

import static com.myproject.myapp.domain.InvestmentActivityTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.myproject.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class InvestmentActivityTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(InvestmentActivity.class);
        InvestmentActivity investmentActivity1 = getInvestmentActivitySample1();
        InvestmentActivity investmentActivity2 = new InvestmentActivity();
        assertThat(investmentActivity1).isNotEqualTo(investmentActivity2);

        investmentActivity2.setId(investmentActivity1.getId());
        assertThat(investmentActivity1).isEqualTo(investmentActivity2);

        investmentActivity2 = getInvestmentActivitySample2();
        assertThat(investmentActivity1).isNotEqualTo(investmentActivity2);
    }
}
