package com.myproject.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class LocalTransferTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static LocalTransfer getLocalTransferSample1() {
        return new LocalTransfer()
            .id(1L)
            .senderAccountNumber("senderAccountNumber1")
            .recipientAccountNumber("recipientAccountNumber1")
            .recipientBankName("recipientBankName1")
            .recipientBankBranch("recipientBankBranch1")
            .description("description1");
    }

    public static LocalTransfer getLocalTransferSample2() {
        return new LocalTransfer()
            .id(2L)
            .senderAccountNumber("senderAccountNumber2")
            .recipientAccountNumber("recipientAccountNumber2")
            .recipientBankName("recipientBankName2")
            .recipientBankBranch("recipientBankBranch2")
            .description("description2");
    }

    public static LocalTransfer getLocalTransferRandomSampleGenerator() {
        return new LocalTransfer()
            .id(longCount.incrementAndGet())
            .senderAccountNumber(UUID.randomUUID().toString())
            .recipientAccountNumber(UUID.randomUUID().toString())
            .recipientBankName(UUID.randomUUID().toString())
            .recipientBankBranch(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString());
    }
}
