package com.myproject.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class DataCollectionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static DataCollection getDataCollectionSample1() {
        return new DataCollection().id(1L).login("login1").name("name1").source("source1");
    }

    public static DataCollection getDataCollectionSample2() {
        return new DataCollection().id(2L).login("login2").name("name2").source("source2");
    }

    public static DataCollection getDataCollectionRandomSampleGenerator() {
        return new DataCollection()
            .id(longCount.incrementAndGet())
            .login(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .source(UUID.randomUUID().toString());
    }
}
