package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PatientTask.
 */
@Entity
@Table(name = "patient_task")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PatientTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "schedule")
    private String schedule;

    @ManyToOne
    @JsonIgnoreProperties(value = { "user" }, allowSetters = true)
    private People patient;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PatientTask id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public PatientTask title(String title) {
        this.setTitle(title);
        return this;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return this.description;
    }

    public PatientTask description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSchedule() {
        return this.schedule;
    }

    public PatientTask schedule(String schedule) {
        this.setSchedule(schedule);
        return this;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public People getPatient() {
        return this.patient;
    }

    public void setPatient(People people) {
        this.patient = people;
    }

    public PatientTask patient(People people) {
        this.setPatient(people);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PatientTask)) {
            return false;
        }
        return id != null && id.equals(((PatientTask) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PatientTask{" +
            "id=" + getId() +
            ", title='" + getTitle() + "'" +
            ", description='" + getDescription() + "'" +
            ", schedule='" + getSchedule() + "'" +
            "}";
    }
}
