package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A NursingHomePatient.
 */
@Entity
@Table(name = "nursing_home_patient")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class NursingHomePatient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private People patient;

    @ManyToOne
    private NursingHome nusingHome;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public NursingHomePatient id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public People getPatient() {
        return this.patient;
    }

    public void setPatient(People people) {
        this.patient = people;
    }

    public NursingHomePatient patient(People people) {
        this.setPatient(people);
        return this;
    }

    public NursingHome getNusingHome() {
        return this.nusingHome;
    }

    public void setNusingHome(NursingHome nursingHome) {
        this.nusingHome = nursingHome;
    }

    public NursingHomePatient nusingHome(NursingHome nursingHome) {
        this.setNusingHome(nursingHome);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof NursingHomePatient)) {
            return false;
        }
        return id != null && id.equals(((NursingHomePatient) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "NursingHomePatient{" +
            "id=" + getId() +
            "}";
    }
}
