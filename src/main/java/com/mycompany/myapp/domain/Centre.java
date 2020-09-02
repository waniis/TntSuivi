package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A Centre.
 */
@Entity
@Table(name = "centre")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Centre implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "address", nullable = false)
    private String address;

    @NotNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @NotNull
    @Column(name = "fax", nullable = false)
    private String fax;

    @Column(name = "emergency")
    private String emergency;

    @OneToMany(mappedBy = "centre" , fetch = FetchType.EAGER)
    @JsonIgnoreProperties("centre")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<AdminDeCentre> adminDeCentres = new HashSet<>();

    @OneToMany(mappedBy = "centre" , fetch = FetchType.EAGER )
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnoreProperties( {"centre","patients"})
    private Set<Medecin> medecins = new HashSet<>();
 
    @OneToMany(mappedBy = "centre" , fetch = FetchType.EAGER)
    @JsonIgnoreProperties({"centre" ,"medecin"})
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Patient> patients = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("centres")
    private Region region;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Centre name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public Centre address(String address) {
        this.address = address;
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public Centre phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getFax() {
        return fax;
    }

    public Centre fax(String fax) {
        this.fax = fax;
        return this;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getEmergency() {
        return emergency;
    }

    public Centre emergency(String emergency) {
        this.emergency = emergency;
        return this;
    }

    public void setEmergency(String emergency) {
        this.emergency = emergency;
    }

    public Set<AdminDeCentre> getAdminDeCentres() {
        return adminDeCentres;
    }

    public Centre adminDeCentres(Set<AdminDeCentre> adminDeCentres) {
        this.adminDeCentres = adminDeCentres;
        return this;
    }

    public Centre addAdminDeCentre(AdminDeCentre adminDeCentre) {
        this.adminDeCentres.add(adminDeCentre);
        adminDeCentre.setCentre(this);
        return this;
    }

    public Centre removeAdminDeCentre(AdminDeCentre adminDeCentre) {
        this.adminDeCentres.remove(adminDeCentre);
        adminDeCentre.setCentre(null);
        return this;
    }

    public void setAdminDeCentres(Set<AdminDeCentre> adminDeCentres) {
        this.adminDeCentres = adminDeCentres;
    }

    public Set<Medecin> getMedecins() {
        return medecins;
    }

    public Centre medecins(Set<Medecin> medecins) {
        this.medecins = medecins;
        return this;
    }

    public Centre addMedecin(Medecin medecin) {
        this.medecins.add(medecin);
        medecin.setCentre(this);
        return this;
    }

    public Centre removeMedecin(Medecin medecin) {
        this.medecins.remove(medecin);
        medecin.setCentre(null);
        return this;
    }

    public void setMedecins(Set<Medecin> medecins) {
        this.medecins = medecins;
    }

    public Set<Patient> getPatients() {
        return patients;
    }

    public Centre patients(Set<Patient> patients) {
        this.patients = patients;
        return this;
    }

    public Centre addPatient(Patient patient) {
        this.patients.add(patient);
        patient.setCentre(this);
        return this;
    }

    public Centre removePatient(Patient patient) {
        this.patients.remove(patient);
        patient.setCentre(null);
        return this;
    }

    public void setPatients(Set<Patient> patients) {
        this.patients = patients;
    }

    public Region getRegion() {
        return region;
    }

    public Centre region(Region region) {
        this.region = region;
        return this;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Centre)) {
            return false;
        }
        return id != null && id.equals(((Centre) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Centre{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", address='" + getAddress() + "'" +
            ", phone='" + getPhone() + "'" +
            ", fax='" + getFax() + "'" +
            ", emergency='" + getEmergency() + "'" +
            "}";
    }
}
