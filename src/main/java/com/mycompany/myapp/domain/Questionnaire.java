package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
 * A Questionnaire.
 */
@Entity
@Table(name = "questionnaire")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Questionnaire implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "subject")
    private String subject;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @OneToMany(mappedBy = "questionnaire")
    @JsonIgnoreProperties({"patient","questionnaire"})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PatientQuestionnaire> patientQuestionnaires = new HashSet<>();

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnoreProperties({"medecin" ,"patientReponses"})
    @JoinTable(name = "questionnaire_question",
               joinColumns = @JoinColumn(name = "questionnaire_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "question_id", referencedColumnName = "id"))
    private Set<Question> questions = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties({"questionnaires", "centre","patients","questions","groupeDePatients" })
    private Medecin medecin;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public Questionnaire subject(String subject) {
        this.subject = subject;
        return this;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Questionnaire startDate(Instant startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public Questionnaire endDate(Instant endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Set<PatientQuestionnaire> getPatientQuestionnaires() {
        return patientQuestionnaires;
    }

    public Questionnaire patientQuestionnaires(Set<PatientQuestionnaire> patientQuestionnaires) {
        this.patientQuestionnaires = patientQuestionnaires;
        return this;
    }

    public Questionnaire addPatientQuestionnaire(PatientQuestionnaire patientQuestionnaire) {
        this.patientQuestionnaires.add(patientQuestionnaire);
        patientQuestionnaire.setQuestionnaire(this);
        return this;
    }

    public Questionnaire removePatientQuestionnaire(PatientQuestionnaire patientQuestionnaire) {
        this.patientQuestionnaires.remove(patientQuestionnaire);
        patientQuestionnaire.setQuestionnaire(null);
        return this;
    }

    public void setPatientQuestionnaires(Set<PatientQuestionnaire> patientQuestionnaires) {
        this.patientQuestionnaires = patientQuestionnaires;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public Questionnaire questions(Set<Question> questions) {
        this.questions = questions;
        return this;
    }

    public Questionnaire addQuestion(Question question) {
        this.questions.add(question);
        question.getQuestionnaires().add(this);
        return this;
    }

    public Questionnaire removeQuestion(Question question) {
        this.questions.remove(question);
        question.getQuestionnaires().remove(this);
        return this;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public Questionnaire medecin(Medecin medecin) {
        this.medecin = medecin;
        return this;
    }

    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Questionnaire)) {
            return false;
        }
        return id != null && id.equals(((Questionnaire) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Questionnaire{" +
            "id=" + getId() +
            ", subject='" + getSubject() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            "}";
    }
}
