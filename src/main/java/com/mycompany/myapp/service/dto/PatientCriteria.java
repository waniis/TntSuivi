package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.mycompany.myapp.domain.enumeration.Sexe;
import com.mycompany.myapp.domain.enumeration.Alcool;
import com.mycompany.myapp.domain.enumeration.Tobacco;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;
import io.github.jhipster.service.filter.LocalDateFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Patient} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.PatientResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /patients?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class PatientCriteria implements Serializable, Criteria {
    /**
     * Class for filtering Sexe
     */
    public static class SexeFilter extends Filter<Sexe> {

        public SexeFilter() {
        }

        public SexeFilter(SexeFilter filter) {
            super(filter);
        }

        @Override
        public SexeFilter copy() {
            return new SexeFilter(this);
        }

    }
    /**
     * Class for filtering Alcool
     */
    public static class AlcoolFilter extends Filter<Alcool> {

        public AlcoolFilter() {
        }

        public AlcoolFilter(AlcoolFilter filter) {
            super(filter);
        }

        @Override
        public AlcoolFilter copy() {
            return new AlcoolFilter(this);
        }

    }
    /**
     * Class for filtering Tobacco
     */
    public static class TobaccoFilter extends Filter<Tobacco> {

        public TobaccoFilter() {
        }

        public TobaccoFilter(TobaccoFilter filter) {
            super(filter);
        }

        @Override
        public TobaccoFilter copy() {
            return new TobaccoFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fullName;

    private StringFilter phone;

    private StringFilter adress;

    private SexeFilter sexe;

    private AlcoolFilter alcool;

    private LocalDateFilter startDateAlcool;

    private LocalDateFilter endDateAlcool;

    private TobaccoFilter tobacoo;

    private LocalDateFilter startDateTobacco;

    private LocalDateFilter endDateTobacco;

    private LongFilter userId;

    private LongFilter patientQuestionnaireId;

    private LongFilter patientReponseId;

    private LongFilter medecinId;

    private LongFilter centreId;

    private LongFilter groupeDePatientId;

    public PatientCriteria() {
    }

    public PatientCriteria(PatientCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fullName = other.fullName == null ? null : other.fullName.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.adress = other.adress == null ? null : other.adress.copy();
        this.sexe = other.sexe == null ? null : other.sexe.copy();
        this.alcool = other.alcool == null ? null : other.alcool.copy();
        this.startDateAlcool = other.startDateAlcool == null ? null : other.startDateAlcool.copy();
        this.endDateAlcool = other.endDateAlcool == null ? null : other.endDateAlcool.copy();
        this.tobacoo = other.tobacoo == null ? null : other.tobacoo.copy();
        this.startDateTobacco = other.startDateTobacco == null ? null : other.startDateTobacco.copy();
        this.endDateTobacco = other.endDateTobacco == null ? null : other.endDateTobacco.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.patientQuestionnaireId = other.patientQuestionnaireId == null ? null : other.patientQuestionnaireId.copy();
        this.patientReponseId = other.patientReponseId == null ? null : other.patientReponseId.copy();
        this.medecinId = other.medecinId == null ? null : other.medecinId.copy();
        this.centreId = other.centreId == null ? null : other.centreId.copy();
        this.groupeDePatientId = other.groupeDePatientId == null ? null : other.groupeDePatientId.copy();
    }

    @Override
    public PatientCriteria copy() {
        return new PatientCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getFullName() {
        return fullName;
    }

    public void setFullName(StringFilter fullName) {
        this.fullName = fullName;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getAdress() {
        return adress;
    }

    public void setAdress(StringFilter adress) {
        this.adress = adress;
    }

    public SexeFilter getSexe() {
        return sexe;
    }

    public void setSexe(SexeFilter sexe) {
        this.sexe = sexe;
    }

    public AlcoolFilter getAlcool() {
        return alcool;
    }

    public void setAlcool(AlcoolFilter alcool) {
        this.alcool = alcool;
    }

    public LocalDateFilter getStartDateAlcool() {
        return startDateAlcool;
    }

    public void setStartDateAlcool(LocalDateFilter startDateAlcool) {
        this.startDateAlcool = startDateAlcool;
    }

    public LocalDateFilter getEndDateAlcool() {
        return endDateAlcool;
    }

    public void setEndDateAlcool(LocalDateFilter endDateAlcool) {
        this.endDateAlcool = endDateAlcool;
    }

    public TobaccoFilter getTobacoo() {
        return tobacoo;
    }

    public void setTobacoo(TobaccoFilter tobacoo) {
        this.tobacoo = tobacoo;
    }

    public LocalDateFilter getStartDateTobacco() {
        return startDateTobacco;
    }

    public void setStartDateTobacco(LocalDateFilter startDateTobacco) {
        this.startDateTobacco = startDateTobacco;
    }

    public LocalDateFilter getEndDateTobacco() {
        return endDateTobacco;
    }

    public void setEndDateTobacco(LocalDateFilter endDateTobacco) {
        this.endDateTobacco = endDateTobacco;
    }

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getPatientQuestionnaireId() {
        return patientQuestionnaireId;
    }

    public void setPatientQuestionnaireId(LongFilter patientQuestionnaireId) {
        this.patientQuestionnaireId = patientQuestionnaireId;
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

    public LongFilter getCentreId() {
        return centreId;
    }

    public void setCentreId(LongFilter centreId) {
        this.centreId = centreId;
    }

    public LongFilter getGroupeDePatientId() {
        return groupeDePatientId;
    }

    public void setGroupeDePatientId(LongFilter groupeDePatientId) {
        this.groupeDePatientId = groupeDePatientId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PatientCriteria that = (PatientCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(fullName, that.fullName) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(adress, that.adress) &&
            Objects.equals(sexe, that.sexe) &&
            Objects.equals(alcool, that.alcool) &&
            Objects.equals(startDateAlcool, that.startDateAlcool) &&
            Objects.equals(endDateAlcool, that.endDateAlcool) &&
            Objects.equals(tobacoo, that.tobacoo) &&
            Objects.equals(startDateTobacco, that.startDateTobacco) &&
            Objects.equals(endDateTobacco, that.endDateTobacco) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(patientQuestionnaireId, that.patientQuestionnaireId) &&
            Objects.equals(patientReponseId, that.patientReponseId) &&
            Objects.equals(medecinId, that.medecinId) &&
            Objects.equals(centreId, that.centreId) &&
            Objects.equals(groupeDePatientId, that.groupeDePatientId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        fullName,
        phone,
        adress,
        sexe,
        alcool,
        startDateAlcool,
        endDateAlcool,
        tobacoo,
        startDateTobacco,
        endDateTobacco,
        userId,
        patientQuestionnaireId,
        patientReponseId,
        medecinId,
        centreId,
        groupeDePatientId
        );
    }

    @Override
    public String toString() {
        return "PatientCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (fullName != null ? "fullName=" + fullName + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (adress != null ? "adress=" + adress + ", " : "") +
                (sexe != null ? "sexe=" + sexe + ", " : "") +
                (alcool != null ? "alcool=" + alcool + ", " : "") +
                (startDateAlcool != null ? "startDateAlcool=" + startDateAlcool + ", " : "") +
                (endDateAlcool != null ? "endDateAlcool=" + endDateAlcool + ", " : "") +
                (tobacoo != null ? "tobacoo=" + tobacoo + ", " : "") +
                (startDateTobacco != null ? "startDateTobacco=" + startDateTobacco + ", " : "") +
                (endDateTobacco != null ? "endDateTobacco=" + endDateTobacco + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (patientQuestionnaireId != null ? "patientQuestionnaireId=" + patientQuestionnaireId + ", " : "") +
                (patientReponseId != null ? "patientReponseId=" + patientReponseId + ", " : "") +
                (medecinId != null ? "medecinId=" + medecinId + ", " : "") +
                (centreId != null ? "centreId=" + centreId + ", " : "") +
                (groupeDePatientId != null ? "groupeDePatientId=" + groupeDePatientId + ", " : "") +
            "}";
    }

}
