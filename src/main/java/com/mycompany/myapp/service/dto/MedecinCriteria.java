package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.mycompany.myapp.domain.enumeration.Sexe;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Medecin} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.MedecinResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /medecins?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MedecinCriteria implements Serializable, Criteria {
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

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter fullName;

    private StringFilter phone;

    private StringFilter phone2;

    private StringFilter adress;

    private SexeFilter sexe;

    private LongFilter userId;

    private LongFilter questionId;

    private LongFilter patientId;

    private LongFilter questionnaireId;

    private LongFilter groupeDePatientId;

    private LongFilter centreId;

    private LongFilter specialtyId;

    public MedecinCriteria() {
    }

    public MedecinCriteria(MedecinCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.fullName = other.fullName == null ? null : other.fullName.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.phone2 = other.phone2 == null ? null : other.phone2.copy();
        this.adress = other.adress == null ? null : other.adress.copy();
        this.sexe = other.sexe == null ? null : other.sexe.copy();
        this.userId = other.userId == null ? null : other.userId.copy();
        this.questionId = other.questionId == null ? null : other.questionId.copy();
        this.patientId = other.patientId == null ? null : other.patientId.copy();
        this.questionnaireId = other.questionnaireId == null ? null : other.questionnaireId.copy();
        this.groupeDePatientId = other.groupeDePatientId == null ? null : other.groupeDePatientId.copy();
        this.centreId = other.centreId == null ? null : other.centreId.copy();
        this.specialtyId = other.specialtyId == null ? null : other.specialtyId.copy();
    }

    @Override
    public MedecinCriteria copy() {
        return new MedecinCriteria(this);
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

    public StringFilter getPhone2() {
        return phone2;
    }

    public void setPhone2(StringFilter phone2) {
        this.phone2 = phone2;
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

    public LongFilter getUserId() {
        return userId;
    }

    public void setUserId(LongFilter userId) {
        this.userId = userId;
    }

    public LongFilter getQuestionId() {
        return questionId;
    }

    public void setQuestionId(LongFilter questionId) {
        this.questionId = questionId;
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

    public LongFilter getGroupeDePatientId() {
        return groupeDePatientId;
    }

    public void setGroupeDePatientId(LongFilter groupeDePatientId) {
        this.groupeDePatientId = groupeDePatientId;
    }

    public LongFilter getCentreId() {
        return centreId;
    }

    public void setCentreId(LongFilter centreId) {
        this.centreId = centreId;
    }

    public LongFilter getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(LongFilter specialtyId) {
        this.specialtyId = specialtyId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MedecinCriteria that = (MedecinCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(fullName, that.fullName) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(phone2, that.phone2) &&
            Objects.equals(adress, that.adress) &&
            Objects.equals(sexe, that.sexe) &&
            Objects.equals(userId, that.userId) &&
            Objects.equals(questionId, that.questionId) &&
            Objects.equals(patientId, that.patientId) &&
            Objects.equals(questionnaireId, that.questionnaireId) &&
            Objects.equals(groupeDePatientId, that.groupeDePatientId) &&
            Objects.equals(centreId, that.centreId) &&
            Objects.equals(specialtyId, that.specialtyId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        fullName,
        phone,
        phone2,
        adress,
        sexe,
        userId,
        questionId,
        patientId,
        questionnaireId,
        groupeDePatientId,
        centreId,
        specialtyId
        );
    }

    @Override
    public String toString() {
        return "MedecinCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (fullName != null ? "fullName=" + fullName + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (phone2 != null ? "phone2=" + phone2 + ", " : "") +
                (adress != null ? "adress=" + adress + ", " : "") +
                (sexe != null ? "sexe=" + sexe + ", " : "") +
                (userId != null ? "userId=" + userId + ", " : "") +
                (questionId != null ? "questionId=" + questionId + ", " : "") +
                (patientId != null ? "patientId=" + patientId + ", " : "") +
                (questionnaireId != null ? "questionnaireId=" + questionnaireId + ", " : "") +
                (groupeDePatientId != null ? "groupeDePatientId=" + groupeDePatientId + ", " : "") +
                (centreId != null ? "centreId=" + centreId + ", " : "") +
                (specialtyId != null ? "specialtyId=" + specialtyId + ", " : "") +
            "}";
    }

}
