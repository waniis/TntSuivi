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
 * Criteria class for the {@link com.mycompany.myapp.domain.Centre} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.CentreResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /centres?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class CentreCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter address;

    private StringFilter phone;

    private StringFilter fax;

    private StringFilter emergency;

    private LongFilter adminDeCentreId;

    private LongFilter medecinId;

    private LongFilter patientId;

    private LongFilter regionId;

    public CentreCriteria() {
    }

    public CentreCriteria(CentreCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.address = other.address == null ? null : other.address.copy();
        this.phone = other.phone == null ? null : other.phone.copy();
        this.fax = other.fax == null ? null : other.fax.copy();
        this.emergency = other.emergency == null ? null : other.emergency.copy();
        this.adminDeCentreId = other.adminDeCentreId == null ? null : other.adminDeCentreId.copy();
        this.medecinId = other.medecinId == null ? null : other.medecinId.copy();
        this.patientId = other.patientId == null ? null : other.patientId.copy();
        this.regionId = other.regionId == null ? null : other.regionId.copy();
    }

    @Override
    public CentreCriteria copy() {
        return new CentreCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getAddress() {
        return address;
    }

    public void setAddress(StringFilter address) {
        this.address = address;
    }

    public StringFilter getPhone() {
        return phone;
    }

    public void setPhone(StringFilter phone) {
        this.phone = phone;
    }

    public StringFilter getFax() {
        return fax;
    }

    public void setFax(StringFilter fax) {
        this.fax = fax;
    }

    public StringFilter getEmergency() {
        return emergency;
    }

    public void setEmergency(StringFilter emergency) {
        this.emergency = emergency;
    }

    public LongFilter getAdminDeCentreId() {
        return adminDeCentreId;
    }

    public void setAdminDeCentreId(LongFilter adminDeCentreId) {
        this.adminDeCentreId = adminDeCentreId;
    }

    public LongFilter getMedecinId() {
        return medecinId;
    }

    public void setMedecinId(LongFilter medecinId) {
        this.medecinId = medecinId;
    }

    public LongFilter getPatientId() {
        return patientId;
    }

    public void setPatientId(LongFilter patientId) {
        this.patientId = patientId;
    }

    public LongFilter getRegionId() {
        return regionId;
    }

    public void setRegionId(LongFilter regionId) {
        this.regionId = regionId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final CentreCriteria that = (CentreCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(address, that.address) &&
            Objects.equals(phone, that.phone) &&
            Objects.equals(fax, that.fax) &&
            Objects.equals(emergency, that.emergency) &&
            Objects.equals(adminDeCentreId, that.adminDeCentreId) &&
            Objects.equals(medecinId, that.medecinId) &&
            Objects.equals(patientId, that.patientId) &&
            Objects.equals(regionId, that.regionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        address,
        phone,
        fax,
        emergency,
        adminDeCentreId,
        medecinId,
        patientId,
        regionId
        );
    }

    @Override
    public String toString() {
        return "CentreCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (address != null ? "address=" + address + ", " : "") +
                (phone != null ? "phone=" + phone + ", " : "") +
                (fax != null ? "fax=" + fax + ", " : "") +
                (emergency != null ? "emergency=" + emergency + ", " : "") +
                (adminDeCentreId != null ? "adminDeCentreId=" + adminDeCentreId + ", " : "") +
                (medecinId != null ? "medecinId=" + medecinId + ", " : "") +
                (patientId != null ? "patientId=" + patientId + ", " : "") +
                (regionId != null ? "regionId=" + regionId + ", " : "") +
            "}";
    }

}
