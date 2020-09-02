package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

/**
 * A PatientReponse.
 */
@Entity
@Table(name = "patient_reponse")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class PatientReponse implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JsonIgnoreProperties({"patientReponses","patient","medecin","questionnaire","questionAnswer","question"})
    private PatientQuestionnaire patientQuestionnaire;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties({"patientReponses","medecin","questionAnswers"})
    private Question question;

    @ManyToOne
    @JsonIgnoreProperties({"patientReponses" ,"medecin","question"})
    private QuestionAnswer questionAnswer;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public PatientReponse content(String content) {
        this.content = content;
        return this;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public PatientQuestionnaire getPatientQuestionnaire() {
        return patientQuestionnaire;
    }

    public PatientReponse patientQuestionnaire(PatientQuestionnaire patientQuestionnaire) {
        this.patientQuestionnaire = patientQuestionnaire;
        return this;
    }

    public void setPatientQuestionnaire(PatientQuestionnaire patientQuestionnaire) {
        this.patientQuestionnaire = patientQuestionnaire;
    }

    public Question getQuestion() {
        return question;
    }

    public PatientReponse question(Question question) {
        this.question = question;
        return this;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public QuestionAnswer getQuestionAnswer() {
        return questionAnswer;
    }

    public PatientReponse questionAnswer(QuestionAnswer questionAnswer) {
        this.questionAnswer = questionAnswer;
        return this;
    }

    public void setQuestionAnswer(QuestionAnswer questionAnswer) {
        this.questionAnswer = questionAnswer;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PatientReponse)) {
            return false;
        }
        return id != null && id.equals(((PatientReponse) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "PatientReponse{" +
            "id=" + getId() +
            ", content='" + getContent() + "'" +
            "}";
    }
}
