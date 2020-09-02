package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

/**
 * A PatientQuestionnaire.
 */
@Entity
@Table(name = "patient_questionnaire")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PatientQuestionnaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "done_time_date")
    private Instant doneTimeDate;

    @Column(name = "done")
    private Boolean done;

    @OneToMany(mappedBy = "patientQuestionnaire")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnoreProperties( {"patientQuestionnaires","patient","questionnaire"})
    private Set<PatientReponse> patientReponses = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties( {"patientQuestionnaires","medecin","centre"})
    private Patient patient;

    @ManyToOne
    @JsonIgnoreProperties("patientQuestionnaires")
    private Questionnaire questionnaire;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getDoneTimeDate() {
        return doneTimeDate;
    }

    public PatientQuestionnaire doneTimeDate(Instant doneTimeDate) {
        this.doneTimeDate = doneTimeDate;
        return this;
    }

    public void setDoneTimeDate(Instant doneTimeDate) {
        this.doneTimeDate = doneTimeDate;
    }

    public Boolean isDone() {
        return done;
    }

    public PatientQuestionnaire done(Boolean done) {
        this.done = done;
        return this;
    }

    public void setDone(Boolean done) {
        this.done = done;
    }

    public Set<PatientReponse> getPatientReponses() {
        return patientReponses;
    }

    public PatientQuestionnaire patientReponses(Set<PatientReponse> patientReponses) {
        this.patientReponses = patientReponses;
        return this;
    }

    public PatientQuestionnaire addPatientReponse(PatientReponse patientReponse) {
        this.patientReponses.add(patientReponse);
        patientReponse.setPatientQuestionnaire(this);
        return this;
    }

    public PatientQuestionnaire removePatientReponse(PatientReponse patientReponse) {
        this.patientReponses.remove(patientReponse);
        patientReponse.setPatientQuestionnaire(null);
        return this;
    }

    public void setPatientReponses(Set<PatientReponse> patientReponses) {
        this.patientReponses = patientReponses;
    }

    public Patient getPatient() {
        return patient;
    }

    public PatientQuestionnaire patient(Patient patient) {
        this.patient = patient;
        return this;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Questionnaire getQuestionnaire() {
        return questionnaire;
    }

    public PatientQuestionnaire questionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
        return this;
    }

    public void setQuestionnaire(Questionnaire questionnaire) {
        this.questionnaire = questionnaire;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PatientQuestionnaire)) {
            return false;
        }
        return id != null && id.equals(((PatientQuestionnaire) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PatientQuestionnaire{" +
            "id=" + getId() +
            ", doneTimeDate='" + getDoneTimeDate() + "'" +
            ", done='" + isDone() + "'" +
            "}";
    }
}
