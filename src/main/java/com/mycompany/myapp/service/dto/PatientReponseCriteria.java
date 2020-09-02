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
 * Criteria class for the {@link com.mycompany.myapp.domain.PatientReponse} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.PatientReponseResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /patient-reponses?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PatientReponseCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter content;

    private LongFilter patientQuestionnaireId;

    private LongFilter questionId;

    private LongFilter questionAnswerId;

    public PatientReponseCriteria() {
    }

    public PatientReponseCriteria(PatientReponseCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.content = other.content == null ? null : other.content.copy();
        this.patientQuestionnaireId = other.patientQuestionnaireId == null ? null : other.patientQuestionnaireId.copy();
        this.questionId = other.questionId == null ? null : other.questionId.copy();
        this.questionAnswerId = other.questionAnswerId == null ? null : other.questionAnswerId.copy();
    }

    @Override
    public PatientReponseCriteria copy() {
        return new PatientReponseCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getContent() {
        return content;
    }

    public void setContent(StringFilter content) {
        this.content = content;
    }

    public LongFilter getPatientQuestionnaireId() {
        return patientQuestionnaireId;
    }

    public void setPatientQuestionnaireId(LongFilter patientQuestionnaireId) {
        this.patientQuestionnaireId = patientQuestionnaireId;
    }

    public LongFilter getQuestionId() {
        return questionId;
    }

    public void setQuestionId(LongFilter questionId) {
        this.questionId = questionId;
    }

    public LongFilter getQuestionAnswerId() {
        return questionAnswerId;
    }

    public void setQuestionAnswerId(LongFilter questionAnswerId) {
        this.questionAnswerId = questionAnswerId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PatientReponseCriteria that = (PatientReponseCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(content, that.content) &&
            Objects.equals(patientQuestionnaireId, that.patientQuestionnaireId) &&
            Objects.equals(questionId, that.questionId) &&
            Objects.equals(questionAnswerId, that.questionAnswerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        content,
        patientQuestionnaireId,
        questionId,
        questionAnswerId
        );
    }

    @Override
    public String toString() {
        return "PatientReponseCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (content != null ? "content=" + content + ", " : "") +
                (patientQuestionnaireId != null ? "patientQuestionnaireId=" + patientQuestionnaireId + ", " : "") +
                (questionId != null ? "questionId=" + questionId + ", " : "") +
                (questionAnswerId != null ? "questionAnswerId=" + questionAnswerId + ", " : "") +
            "}";
    }

}
