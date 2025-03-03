package com.myproject.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.myproject.myapp.domain.enumeration.DataType;
import com.myproject.myapp.domain.enumeration.Status;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A DataCollection.
 */
@Entity
@Table(name = "data_collection")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class DataCollection implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "login", nullable = false)
    private String login;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "source", nullable = false)
    private String source;

    @NotNull
    @Column(name = "collected_at", nullable = false)
    private Instant collectedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "data_type")
    private DataType dataType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Lob
    @Column(name = "description")
    private String description;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "dataCollection")
    @JsonIgnoreProperties(value = { "dataCollection" }, allowSetters = true)
    private Set<AnalysisReport> analysisReports = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public DataCollection id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLogin() {
        return this.login;
    }

    public DataCollection login(String login) {
        this.setLogin(login);
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return this.name;
    }

    public DataCollection name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return this.source;
    }

    public DataCollection source(String source) {
        this.setSource(source);
        return this;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Instant getCollectedAt() {
        return this.collectedAt;
    }

    public DataCollection collectedAt(Instant collectedAt) {
        this.setCollectedAt(collectedAt);
        return this;
    }

    public void setCollectedAt(Instant collectedAt) {
        this.collectedAt = collectedAt;
    }

    public DataType getDataType() {
        return this.dataType;
    }

    public DataCollection dataType(DataType dataType) {
        this.setDataType(dataType);
        return this;
    }

    public void setDataType(DataType dataType) {
        this.dataType = dataType;
    }

    public Status getStatus() {
        return this.status;
    }

    public DataCollection status(Status status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getDescription() {
        return this.description;
    }

    public DataCollection description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Set<AnalysisReport> getAnalysisReports() {
        return this.analysisReports;
    }

    public void setAnalysisReports(Set<AnalysisReport> analysisReports) {
        if (this.analysisReports != null) {
            this.analysisReports.forEach(i -> i.setDataCollection(null));
        }
        if (analysisReports != null) {
            analysisReports.forEach(i -> i.setDataCollection(this));
        }
        this.analysisReports = analysisReports;
    }

    public DataCollection analysisReports(Set<AnalysisReport> analysisReports) {
        this.setAnalysisReports(analysisReports);
        return this;
    }

    public DataCollection addAnalysisReport(AnalysisReport analysisReport) {
        this.analysisReports.add(analysisReport);
        analysisReport.setDataCollection(this);
        return this;
    }

    public DataCollection removeAnalysisReport(AnalysisReport analysisReport) {
        this.analysisReports.remove(analysisReport);
        analysisReport.setDataCollection(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof DataCollection)) {
            return false;
        }
        return getId() != null && getId().equals(((DataCollection) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "DataCollection{" +
            "id=" + getId() +
            ", login='" + getLogin() + "'" +
            ", name='" + getName() + "'" +
            ", source='" + getSource() + "'" +
            ", collectedAt='" + getCollectedAt() + "'" +
            ", dataType='" + getDataType() + "'" +
            ", status='" + getStatus() + "'" +
            ", description='" + getDescription() + "'" +
            "}";
    }
}
