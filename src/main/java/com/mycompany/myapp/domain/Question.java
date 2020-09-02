package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

import com.mycompany.myapp.domain.enumeration.TypeQuestion;

/**
 * A Question.
 */
@Entity
@Table(name = "question")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Question implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "label", nullable = false)
    private String label;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type_question", nullable = false)
    private TypeQuestion typeQuestion;

    @OneToMany(mappedBy = "question" ,fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"question" ,"medecin"})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<QuestionAnswer> questionAnswers = new HashSet<>();

    @OneToMany(mappedBy = "question")
    @JsonIgnoreProperties({"patient", "question"})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PatientReponse> patientReponses = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("questions")
    private Medecin medecin;

    @ManyToMany(mappedBy = "questions")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<Questionnaire> questionnaires = new HashSet<>();

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

    public Question label(String label) {
        this.label = label;
        return this;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public TypeQuestion getTypeQuestion() {
        return typeQuestion;
    }

    public Question typeQuestion(TypeQuestion typeQuestion) {
        this.typeQuestion = typeQuestion;
        return this;
    }

    public void setTypeQuestion(TypeQuestion typeQuestion) {
        this.typeQuestion = typeQuestion;
    }

    public Set<QuestionAnswer> getQuestionAnswers() {
        return questionAnswers;
    }

    public Question questionAnswers(Set<QuestionAnswer> questionAnswers) {
        this.questionAnswers = questionAnswers;
        return this;
    }

    public Question addQuestionAnswer(QuestionAnswer questionAnswer) {
        this.questionAnswers.add(questionAnswer);
        questionAnswer.setQuestion(this);
        return this;
    }

    public Question removeQuestionAnswer(QuestionAnswer questionAnswer) {
        this.questionAnswers.remove(questionAnswer);
        questionAnswer.setQuestion(null);
        return this;
    }

    public void setQuestionAnswers(Set<QuestionAnswer> questionAnswers) {
        this.questionAnswers = questionAnswers;
    }

    public Set<PatientReponse> getPatientReponses() {
        return patientReponses;
    }

    public Question patientReponses(Set<PatientReponse> patientReponses) {
        this.patientReponses = patientReponses;
        return this;
    }

    public Question addPatientReponse(PatientReponse patientReponse) {
        this.patientReponses.add(patientReponse);
        patientReponse.setQuestion(this);
        return this;
    }

    public Question removePatientReponse(PatientReponse patientReponse) {
        this.patientReponses.remove(patientReponse);
        patientReponse.setQuestion(null);
        return this;
    }

    public void setPatientReponses(Set<PatientReponse> patientReponses) {
        this.patientReponses = patientReponses;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public Question medecin(Medecin medecin) {
        this.medecin = medecin;
        return this;
    }

    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }

    public Set<Questionnaire> getQuestionnaires() {
        return questionnaires;
    }

    public Question questionnaires(Set<Questionnaire> questionnaires) {
        this.questionnaires = questionnaires;
        return this;
    }

    public Question addQuestionnaire(Questionnaire questionnaire) {
        this.questionnaires.add(questionnaire);
        questionnaire.getQuestions().add(this);
        return this;
    }

    public Question removeQuestionnaire(Questionnaire questionnaire) {
        this.questionnaires.remove(questionnaire);
        questionnaire.getQuestions().remove(this);
        return this;
    }

    public void setQuestionnaires(Set<Questionnaire> questionnaires) {
        this.questionnaires = questionnaires;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Question)) {
            return false;
        }
        return id != null && id.equals(((Question) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Question{" +
            "id=" + getId() +
            ", label='" + getLabel() + "'" +
            ", typeQuestion='" + getTypeQuestion() + "'" +
            "}";
    }
}
