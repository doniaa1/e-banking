package com.myproject.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InvestmentActivityTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static InvestmentActivity getInvestmentActivitySample1() {
        return new InvestmentActivity()
            .id(1L)
            .projectName("projectName1")
            .location("location1")
            .bondIssuer("bondIssuer1")
            .login("login1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1");
    }

    public static InvestmentActivity getInvestmentActivitySample2() {
        return new InvestmentActivity()
            .id(2L)
            .projectName("projectName2")
            .location("location2")
            .bondIssuer("bondIssuer2")
            .login("login2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2");
    }

    public static InvestmentActivity getInvestmentActivityRandomSampleGenerator() {
        return new InvestmentActivity()
            .id(longCount.incrementAndGet())
            .projectName(UUID.randomUUID().toString())
            .location(UUID.randomUUID().toString())
            .bondIssuer(UUID.randomUUID().toString())
            .login(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString());
    }
}
