package com.myproject.myapp.domain;

import static com.myproject.myapp.domain.AnalysisReportTestSamples.*;
import static com.myproject.myapp.domain.DataCollectionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.myproject.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AnalysisReportTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AnalysisReport.class);
        AnalysisReport analysisReport1 = getAnalysisReportSample1();
        AnalysisReport analysisReport2 = new AnalysisReport();
        assertThat(analysisReport1).isNotEqualTo(analysisReport2);

        analysisReport2.setId(analysisReport1.getId());
        assertThat(analysisReport1).isEqualTo(analysisReport2);

        analysisReport2 = getAnalysisReportSample2();
        assertThat(analysisReport1).isNotEqualTo(analysisReport2);
    }

    @Test
    void dataCollectionTest() {
        AnalysisReport analysisReport = getAnalysisReportRandomSampleGenerator();
        DataCollection dataCollectionBack = getDataCollectionRandomSampleGenerator();

        analysisReport.setDataCollection(dataCollectionBack);
        assertThat(analysisReport.getDataCollection()).isEqualTo(dataCollectionBack);

        analysisReport.dataCollection(null);
        assertThat(analysisReport.getDataCollection()).isNull();
    }
}
