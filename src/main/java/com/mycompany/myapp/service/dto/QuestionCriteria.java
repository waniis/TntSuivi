package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.mycompany.myapp.domain.enumeration.TypeQuestion;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Question} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.QuestionResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /questions?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class QuestionCriteria implements Serializable, Criteria {
    /**
     * Class for filtering TypeQuestion
     */
    public static class TypeQuestionFilter extends Filter<TypeQuestion> {

        public TypeQuestionFilter() {
        }

        public TypeQuestionFilter(TypeQuestionFilter filter) {
            super(filter);
        }

        @Override
        public TypeQuestionFilter copy() {
            return new TypeQuestionFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter label;

    private TypeQuestionFilter typeQuestion;

    private LongFilter questionAnswerId;

    private LongFilter patientReponseId;

    private LongFilter medecinId;

    private LongFilter questionnaireId;

    public QuestionCriteria() {
    }

    public QuestionCriteria(QuestionCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.label = other.label == null ? null : other.label.copy();
        this.typeQuestion = other.typeQuestion == null ? null : other.typeQuestion.copy();
        this.questionAnswerId = other.questionAnswerId == null ? null : other.questionAnswerId.copy();
        this.patientReponseId = other.patientReponseId == null ? null : other.patientReponseId.copy();
        this.medecinId = other.medecinId == null ? null : other.medecinId.copy();
        this.questionnaireId = other.questionnaireId == null ? null : other.questionnaireId.copy();
    }

    @Override
    public QuestionCriteria copy() {
        return new QuestionCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getLabel() {
        return label;
    }

    public void setLabel(StringFilter label) {
        this.label = label;
    }

    public TypeQuestionFilter getTypeQuestion() {
        return typeQuestion;
    }

    public void setTypeQuestion(TypeQuestionFilter typeQuestion) {
        this.typeQuestion = typeQuestion;
    }

    public LongFilter getQuestionAnswerId() {
        return questionAnswerId;
    }

    public void setQuestionAnswerId(LongFilter questionAnswerId) {
        this.questionAnswerId = questionAnswerId;
    }

    public LongFilter getPatientReponseId() {
        return patientReponseId;
    }

    public void setPatientReponseId(LongFilter patientReponseId) {
        this.patientReponseId = patientReponseId;
    }

    public LongFilter getMedecinId() {
        return medecinId;
    }

    public void setMedecinId(LongFilter medecinId) {
        this.medecinId = medecinId;
    }

    public LongFilter getQuestionnaireId() {
        return questionnaireId;
    }

    public void setQuestionnaireId(LongFilter questionnaireId) {
        this.questionnaireId = questionnaireId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final QuestionCriteria that = (QuestionCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(label, that.label) &&
            Objects.equals(typeQuestion, that.typeQuestion) &&
            Objects.equals(questionAnswerId, that.questionAnswerId) &&
            Objects.equals(patientReponseId, that.patientReponseId) &&
            Objects.equals(medecinId, that.medecinId) &&
            Objects.equals(questionnaireId, that.questionnaireId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        label,
        typeQuestion,
        questionAnswerId,
        patientReponseId,
        medecinId,
        questionnaireId
        );
    }

    @Override
    public String toString() {
        return "QuestionCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (label != null ? "label=" + label + ", " : "") +
                (typeQuestion != null ? "typeQuestion=" + typeQuestion + ", " : "") +
                (questionAnswerId != null ? "questionAnswerId=" + questionAnswerId + ", " : "") +
                (patientReponseId != null ? "patientReponseId=" + patientReponseId + ", " : "") +
                (medecinId != null ? "medecinId=" + medecinId + ", " : "") +
                (questionnaireId != null ? "questionnaireId=" + questionnaireId + ", " : "") +
            "}";
    }

}
