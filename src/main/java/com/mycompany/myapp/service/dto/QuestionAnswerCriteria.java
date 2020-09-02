package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.QuestionAnswer} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.QuestionAnswerResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /question-answers?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class QuestionAnswerCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter label;

    private LongFilter patientReponseId;

    private LongFilter questionId;

    public QuestionAnswerCriteria() {
    }

    public QuestionAnswerCriteria(QuestionAnswerCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.label = other.label == null ? null : other.label.copy();
        this.patientReponseId = other.patientReponseId == null ? null : other.patientReponseId.copy();
        this.questionId = other.questionId == null ? null : other.questionId.copy();
    }

    @Override
    public QuestionAnswerCriteria copy() {
        return new QuestionAnswerCriteria(this);
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

    public LongFilter getPatientReponseId() {
        return patientReponseId;
    }

    public void setPatientReponseId(LongFilter patientReponseId) {
        this.patientReponseId = patientReponseId;
    }

    public LongFilter getQuestionId() {
        return questionId;
    }

    public void setQuestionId(LongFilter questionId) {
        this.questionId = questionId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final QuestionAnswerCriteria that = (QuestionAnswerCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(label, that.label) &&
            Objects.equals(patientReponseId, that.patientReponseId) &&
            Objects.equals(questionId, that.questionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        label,
        patientReponseId,
        questionId
        );
    }

    @Override
    public String toString() {
        return "QuestionAnswerCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (label != null ? "label=" + label + ", " : "") +
                (patientReponseId != null ? "patientReponseId=" + patientReponseId + ", " : "") +
                (questionId != null ? "questionId=" + questionId + ", " : "") +
            "}";
    }

}
