package com.myproject.myapp.domain;

import static com.myproject.myapp.domain.AnalysisReportTestSamples.*;
import static com.myproject.myapp.domain.DataCollectionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.myproject.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class DataCollectionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(DataCollection.class);
        DataCollection dataCollection1 = getDataCollectionSample1();
        DataCollection dataCollection2 = new DataCollection();
        assertThat(dataCollection1).isNotEqualTo(dataCollection2);

        dataCollection2.setId(dataCollection1.getId());
        assertThat(dataCollection1).isEqualTo(dataCollection2);

        dataCollection2 = getDataCollectionSample2();
        assertThat(dataCollection1).isNotEqualTo(dataCollection2);
    }

    @Test
    void analysisReportTest() {
        DataCollection dataCollection = getDataCollectionRandomSampleGenerator();
        AnalysisReport analysisReportBack = getAnalysisReportRandomSampleGenerator();

        dataCollection.addAnalysisReport(analysisReportBack);
        assertThat(dataCollection.getAnalysisReports()).containsOnly(analysisReportBack);
        assertThat(analysisReportBack.getDataCollection()).isEqualTo(dataCollection);

        dataCollection.removeAnalysisReport(analysisReportBack);
        assertThat(dataCollection.getAnalysisReports()).doesNotContain(analysisReportBack);
        assertThat(analysisReportBack.getDataCollection()).isNull();

        dataCollection.analysisReports(new HashSet<>(Set.of(analysisReportBack)));
        assertThat(dataCollection.getAnalysisReports()).containsOnly(analysisReportBack);
        assertThat(analysisReportBack.getDataCollection()).isEqualTo(dataCollection);

        dataCollection.setAnalysisReports(new HashSet<>());
        assertThat(dataCollection.getAnalysisReports()).doesNotContain(analysisReportBack);
        assertThat(analysisReportBack.getDataCollection()).isNull();
    }
}
