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
import io.github.jhipster.service.filter.InstantFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Questionnaire} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.QuestionnaireResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /questionnaires?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class QuestionnaireCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter subject;

    private InstantFilter startDate;

    private InstantFilter endDate;

    private LongFilter patientQuestionnaireId;

    private LongFilter questionId;

    private LongFilter medecinId;

    public QuestionnaireCriteria() {
    }

    public QuestionnaireCriteria(QuestionnaireCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.subject = other.subject == null ? null : other.subject.copy();
        this.startDate = other.startDate == null ? null : other.startDate.copy();
        this.endDate = other.endDate == null ? null : other.endDate.copy();
        this.patientQuestionnaireId = other.patientQuestionnaireId == null ? null : other.patientQuestionnaireId.copy();
        this.questionId = other.questionId == null ? null : other.questionId.copy();
        this.medecinId = other.medecinId == null ? null : other.medecinId.copy();
    }

    @Override
    public QuestionnaireCriteria copy() {
        return new QuestionnaireCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getSubject() {
        return subject;
    }

    public void setSubject(StringFilter subject) {
        this.subject = subject;
    }

    public InstantFilter getStartDate() {
        return startDate;
    }

    public void setStartDate(InstantFilter startDate) {
        this.startDate = startDate;
    }

    public InstantFilter getEndDate() {
        return endDate;
    }

    public void setEndDate(InstantFilter endDate) {
        this.endDate = endDate;
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

    public LongFilter getMedecinId() {
        return medecinId;
    }

    public void setMedecinId(LongFilter medecinId) {
        this.medecinId = medecinId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final QuestionnaireCriteria that = (QuestionnaireCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(subject, that.subject) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(patientQuestionnaireId, that.patientQuestionnaireId) &&
            Objects.equals(questionId, that.questionId) &&
            Objects.equals(medecinId, that.medecinId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        subject,
        startDate,
        endDate,
        patientQuestionnaireId,
        questionId,
        medecinId
        );
    }

    @Override
    public String toString() {
        return "QuestionnaireCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (subject != null ? "subject=" + subject + ", " : "") +
                (startDate != null ? "startDate=" + startDate + ", " : "") +
                (endDate != null ? "endDate=" + endDate + ", " : "") +
                (patientQuestionnaireId != null ? "patientQuestionnaireId=" + patientQuestionnaireId + ", " : "") +
                (questionId != null ? "questionId=" + questionId + ", " : "") +
                (medecinId != null ? "medecinId=" + medecinId + ", " : "") +
            "}";
    }

}
