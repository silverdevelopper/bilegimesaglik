package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PatientAction.
 */
@Entity
@Table(name = "patient_action")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PatientAction implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "action_description")
    private String actionDescription;

    @Column(name = "healtstatus")
    private String healtstatus;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private People patient;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private People staff;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PatientAction id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public PatientAction startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public PatientAction endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public String getActionDescription() {
        return this.actionDescription;
    }

    public PatientAction actionDescription(String actionDescription) {
        this.setActionDescription(actionDescription);
        return this;
    }

    public void setActionDescription(String actionDescription) {
        this.actionDescription = actionDescription;
    }

    public String getHealtstatus() {
        return this.healtstatus;
    }

    public PatientAction healtstatus(String healtstatus) {
        this.setHealtstatus(healtstatus);
        return this;
    }

    public void setHealtstatus(String healtstatus) {
        this.healtstatus = healtstatus;
    }

    public People getPatient() {
        return this.patient;
    }

    public void setPatient(People people) {
        this.patient = people;
    }

    public PatientAction patient(People people) {
        this.setPatient(people);
        return this;
    }

    public People getStaff() {
        return this.staff;
    }

    public void setStaff(People people) {
        this.staff = people;
    }

    public PatientAction staff(People people) {
        this.setStaff(people);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PatientAction)) {
            return false;
        }
        return id != null && id.equals(((PatientAction) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PatientAction{" +
            "id=" + getId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", actionDescription='" + getActionDescription() + "'" +
            ", healtstatus='" + getHealtstatus() + "'" +
            "}";
    }
}
