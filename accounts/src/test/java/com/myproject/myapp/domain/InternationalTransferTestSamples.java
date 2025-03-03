package com.myproject.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class InternationalTransferTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static InternationalTransfer getInternationalTransferSample1() {
        return new InternationalTransfer()
            .id(1L)
            .senderAccountNumber("senderAccountNumber1")
            .recipientIban("recipientIban1")
            .swiftCode("swiftCode1")
            .recipientName("recipientName1")
            .description("description1");
    }

    public static InternationalTransfer getInternationalTransferSample2() {
        return new InternationalTransfer()
            .id(2L)
            .senderAccountNumber("senderAccountNumber2")
            .recipientIban("recipientIban2")
            .swiftCode("swiftCode2")
            .recipientName("recipientName2")
            .description("description2");
    }

    public static InternationalTransfer getInternationalTransferRandomSampleGenerator() {
        return new InternationalTransfer()
            .id(longCount.incrementAndGet())
            .senderAccountNumber(UUID.randomUUID().toString())
            .recipientIban(UUID.randomUUID().toString())
            .swiftCode(UUID.randomUUID().toString())
            .recipientName(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
