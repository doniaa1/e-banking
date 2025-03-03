package com.myproject.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.myproject.myapp.domain.enumeration.AnalysisType;
import com.myproject.myapp.domain.enumeration.ReportType;
import com.myproject.myapp.domain.enumeration.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;

/**
 * A AnalysisReport.
 */
@Entity
@Table(name = "analysis_report")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AnalysisReport implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "title", nullable = false)
    private String title;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "analysis_type", nullable = false)
    private AnalysisType analysisType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "report_type", nullable = false)
    private ReportType reportType;

    @Column(name = "generated_by")
    private String generatedBy;

    @Lob
    @Column(name = "content", nullable = false)
    private String content;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties(value = { "analysisReports" }, allowSetters = true)
    private DataCollection dataCollection;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AnalysisReport id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public AnalysisReport title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public AnalysisReport createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public AnalysisType getAnalysisType() {
        return this.analysisType;
    }

    public AnalysisReport analysisType(AnalysisType analysisType) {
        this.setAnalysisType(analysisType);
        return this;
    }

    public void setAnalysisType(AnalysisType analysisType) {
        this.analysisType = analysisType;
    }

    public ReportType getReportType() {
        return this.reportType;
    }

    public AnalysisReport reportType(ReportType reportType) {
        this.setReportType(reportType);
        return this;
    }

    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    public String getGeneratedBy() {
        return this.generatedBy;
    }

    public AnalysisReport generatedBy(String generatedBy) {
        this.setGeneratedBy(generatedBy);
        return this;
    }

    public void setGeneratedBy(String generatedBy) {
        this.generatedBy = generatedBy;
    }

    public String getContent() {
        return this.content;
    }

    public AnalysisReport content(String content) {
        this.setContent(content);
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Status getStatus() {
        return this.status;
    }

    public AnalysisReport status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public DataCollection getDataCollection() {
        return this.dataCollection;
    }

    public void setDataCollection(DataCollection dataCollection) {
        this.dataCollection = dataCollection;
    }

    public AnalysisReport dataCollection(DataCollection dataCollection) {
        this.setDataCollection(dataCollection);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AnalysisReport)) {
            return false;
        }
        return getId() != null && getId().equals(((AnalysisReport) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AnalysisReport{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", analysisType='" + getAnalysisType() + "'" +
            ", reportType='" + getReportType() + "'" +
            ", generatedBy='" + getGeneratedBy() + "'" +
            ", content='" + getContent() + "'" +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
