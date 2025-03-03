package com.myproject.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class AnalysisReportTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static AnalysisReport getAnalysisReportSample1() {
        return new AnalysisReport().id(1L).title("title1").generatedBy("generatedBy1");
    }

    public static AnalysisReport getAnalysisReportSample2() {
        return new AnalysisReport().id(2L).title("title2").generatedBy("generatedBy2");
    }

    public static AnalysisReport getAnalysisReportRandomSampleGenerator() {
        return new AnalysisReport()
            .id(longCount.incrementAndGet())
            .title(UUID.randomUUID().toString())
            .generatedBy(UUID.randomUUID().toString());
    }
}
