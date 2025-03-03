package com.myproject.myapp.domain;

import com.myproject.myapp.domain.enumeration.ActivityType;
import com.myproject.myapp.domain.enumeration.InvestmentType;
import com.myproject.myapp.domain.enumeration.RiskLevel;
import com.myproject.myapp.domain.enumeration.StatusType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * A InvestmentActivity.
 */
@Entity
@Table(name = "investment_activity")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class InvestmentActivity implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "investment_type", nullable = false)
    private InvestmentType investmentType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "activity_type", nullable = false)
    private ActivityType activityType;

    @Column(name = "project_name")
    private String projectName;

    @Lob
    @Column(name = "description")
    private String description;

    @Column(name = "location")
    private String location;

    @Column(name = "target_amount", precision = 21, scale = 2)
    private BigDecimal targetAmount;

    @Column(name = "current_amount", precision = 21, scale = 2)
    private BigDecimal currentAmount;

    @Column(name = "bond_issuer")
    private String bondIssuer;

    @NotNull
    @Column(name = "activity_date", nullable = false)
    private Instant activityDate;

    @Column(name = "activity_amount", precision = 21, scale = 2)
    private BigDecimal activityAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private StatusType status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "risk_level", nullable = false)
    private RiskLevel riskLevel;

    @NotNull
    @Column(name = "login", nullable = false)
    private String login;

    @NotNull
    @Column(name = "created_by", nullable = false)
    private String createdBy;

    @NotNull
    @Column(name = "created_date", nullable = false)
    private Instant createdDate;

    @Column(name = "last_modified_by")
    private String lastModifiedBy;

    @Column(name = "last_modified_date")
    private Instant lastModifiedDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public InvestmentActivity id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public InvestmentType getInvestmentType() {
        return this.investmentType;
    }

    public InvestmentActivity investmentType(InvestmentType investmentType) {
        this.setInvestmentType(investmentType);
        return this;
    }

    public void setInvestmentType(InvestmentType investmentType) {
        this.investmentType = investmentType;
    }

    public ActivityType getActivityType() {
        return this.activityType;
    }

    public InvestmentActivity activityType(ActivityType activityType) {
        this.setActivityType(activityType);
        return this;
    }

    public void setActivityType(ActivityType activityType) {
        this.activityType = activityType;
    }

    public String getProjectName() {
        return this.projectName;
    }

    public InvestmentActivity projectName(String projectName) {
        this.setProjectName(projectName);
        return this;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getDescription() {
        return this.description;
    }

    public InvestmentActivity description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return this.location;
    }

    public InvestmentActivity location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public BigDecimal getTargetAmount() {
        return this.targetAmount;
    }

    public InvestmentActivity targetAmount(BigDecimal targetAmount) {
        this.setTargetAmount(targetAmount);
        return this;
    }

    public void setTargetAmount(BigDecimal targetAmount) {
        this.targetAmount = targetAmount;
    }

    public BigDecimal getCurrentAmount() {
        return this.currentAmount;
    }

    public InvestmentActivity currentAmount(BigDecimal currentAmount) {
        this.setCurrentAmount(currentAmount);
        return this;
    }

    public void setCurrentAmount(BigDecimal currentAmount) {
        this.currentAmount = currentAmount;
    }

    public String getBondIssuer() {
        return this.bondIssuer;
    }

    public InvestmentActivity bondIssuer(String bondIssuer) {
        this.setBondIssuer(bondIssuer);
        return this;
    }

    public void setBondIssuer(String bondIssuer) {
        this.bondIssuer = bondIssuer;
    }

    public Instant getActivityDate() {
        return this.activityDate;
    }

    public InvestmentActivity activityDate(Instant activityDate) {
        this.setActivityDate(activityDate);
        return this;
    }

    public void setActivityDate(Instant activityDate) {
        this.activityDate = activityDate;
    }

    public BigDecimal getActivityAmount() {
        return this.activityAmount;
    }

    public InvestmentActivity activityAmount(BigDecimal activityAmount) {
        this.setActivityAmount(activityAmount);
        return this;
    }

    public void setActivityAmount(BigDecimal activityAmount) {
        this.activityAmount = activityAmount;
    }

    public StatusType getStatus() {
        return this.status;
    }

    public InvestmentActivity status(StatusType status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(StatusType status) {
        this.status = status;
    }

    public RiskLevel getRiskLevel() {
        return this.riskLevel;
    }

    public InvestmentActivity riskLevel(RiskLevel riskLevel) {
        this.setRiskLevel(riskLevel);
        return this;
    }

    public void setRiskLevel(RiskLevel riskLevel) {
        this.riskLevel = riskLevel;
    }

    public String getLogin() {
        return this.login;
    }

    public InvestmentActivity login(String login) {
        this.setLogin(login);
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public InvestmentActivity createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return this.createdDate;
    }

    public InvestmentActivity createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return this.lastModifiedBy;
    }

    public InvestmentActivity lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return this.lastModifiedDate;
    }

    public InvestmentActivity lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof InvestmentActivity)) {
            return false;
        }
        return getId() != null && getId().equals(((InvestmentActivity) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "InvestmentActivity{" +
            "id=" + getId() +
            ", investmentType='" + getInvestmentType() + "'" +
            ", activityType='" + getActivityType() + "'" +
            ", projectName='" + getProjectName() + "'" +
            ", description='" + getDescription() + "'" +
            ", location='" + getLocation() + "'" +
            ", targetAmount=" + getTargetAmount() +
            ", currentAmount=" + getCurrentAmount() +
            ", bondIssuer='" + getBondIssuer() + "'" +
            ", activityDate='" + getActivityDate() + "'" +
            ", activityAmount=" + getActivityAmount() +
            ", status='" + getStatus() + "'" +
            ", riskLevel='" + getRiskLevel() + "'" +
            ", login='" + getLogin() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
