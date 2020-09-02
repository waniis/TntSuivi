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
 * Criteria class for the {@link com.mycompany.myapp.domain.PatientQuestionnaire} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.PatientQuestionnaireResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /patient-questionnaires?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PatientQuestionnaireCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter doneTimeDate;

    private BooleanFilter done;

    private LongFilter patientReponseId;

    private LongFilter patientId;

    private LongFilter questionnaireId;

    public PatientQuestionnaireCriteria() {
    }

    public PatientQuestionnaireCriteria(PatientQuestionnaireCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.doneTimeDate = other.doneTimeDate == null ? null : other.doneTimeDate.copy();
        this.done = other.done == null ? null : other.done.copy();
        this.patientReponseId = other.patientReponseId == null ? null : other.patientReponseId.copy();
        this.patientId = other.patientId == null ? null : other.patientId.copy();
        this.questionnaireId = other.questionnaireId == null ? null : other.questionnaireId.copy();
    }

    @Override
    public PatientQuestionnaireCriteria copy() {
        return new PatientQuestionnaireCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public InstantFilter getDoneTimeDate() {
        return doneTimeDate;
    }

    public void setDoneTimeDate(InstantFilter doneTimeDate) {
        this.doneTimeDate = doneTimeDate;
    }

    public BooleanFilter getDone() {
        return done;
    }

    public void setDone(BooleanFilter done) {
        this.done = done;
    }

    public LongFilter getPatientReponseId() {
        return patientReponseId;
    }

    public void setPatientReponseId(LongFilter patientReponseId) {
        this.patientReponseId = patientReponseId;
    }

    public LongFilter getPatientId() {
        return patientId;
    }

    public void setPatientId(LongFilter patientId) {
        this.patientId = patientId;
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
        final PatientQuestionnaireCriteria that = (PatientQuestionnaireCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(doneTimeDate, that.doneTimeDate) &&
            Objects.equals(done, that.done) &&
            Objects.equals(patientReponseId, that.patientReponseId) &&
            Objects.equals(patientId, that.patientId) &&
            Objects.equals(questionnaireId, that.questionnaireId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        doneTimeDate,
        done,
        patientReponseId,
        patientId,
        questionnaireId
        );
    }

    @Override
    public String toString() {
        return "PatientQuestionnaireCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (doneTimeDate != null ? "doneTimeDate=" + doneTimeDate + ", " : "") +
                (done != null ? "done=" + done + ", " : "") +
                (patientReponseId != null ? "patientReponseId=" + patientReponseId + ", " : "") +
                (patientId != null ? "patientId=" + patientId + ", " : "") +
                (questionnaireId != null ? "questionnaireId=" + questionnaireId + ", " : "") +
            "}";
    }

}
