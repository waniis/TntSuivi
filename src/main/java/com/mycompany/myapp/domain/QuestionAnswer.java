package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A QuestionAnswer.
 */
@Entity
@Table(name = "question_answer")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class QuestionAnswer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "label")
    private String label;

    @OneToMany(mappedBy = "questionAnswer")
    @JsonIgnore
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PatientReponse> patientReponses = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("questionAnswers")
    private Question question;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public QuestionAnswer label(String label) {
        this.label = label;
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Set<PatientReponse> getPatientReponses() {
        return patientReponses;
    }

    public QuestionAnswer patientReponses(Set<PatientReponse> patientReponses) {
        this.patientReponses = patientReponses;
        return this;
    }

    public QuestionAnswer addPatientReponse(PatientReponse patientReponse) {
        this.patientReponses.add(patientReponse);
        patientReponse.setQuestionAnswer(this);
        return this;
    }

    public QuestionAnswer removePatientReponse(PatientReponse patientReponse) {
        this.patientReponses.remove(patientReponse);
        patientReponse.setQuestionAnswer(null);
        return this;
    }

    public void setPatientReponses(Set<PatientReponse> patientReponses) {
        this.patientReponses = patientReponses;
    }

    public Question getQuestion() {
        return question;
    }

    public QuestionAnswer question(Question question) {
        this.question = question;
        return this;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof QuestionAnswer)) {
            return false;
        }
        return id != null && id.equals(((QuestionAnswer) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "QuestionAnswer{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            "}";
    }
}
