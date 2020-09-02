package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

/**
 * A GroupeDePatient.
 */
@Entity
@Table(name = "groupe_de_patient")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class GroupeDePatient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToMany
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnoreProperties(value = {"groupeDePatients","centre","medecin"}, allowSetters = true)
    @JoinTable(name = "groupe_de_patient_patient",
               joinColumns = @JoinColumn(name = "groupe_de_patient_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "patient_id", referencedColumnName = "id"))
    private Set<Patient> patients = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = {"groupeDePatients","centre","patients","questionnaires","questions"}, allowSetters = true)
    private Medecin medecin;


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

    public GroupeDePatient name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Patient> getPatients() {
        return patients;
    }

    public GroupeDePatient patients(Set<Patient> patients) {
        this.patients = patients;
        return this;
    }

    public GroupeDePatient addPatient(Patient patient) {
        this.patients.add(patient);
        patient.getGroupeDePatients().add(this);
        return this;
    }

    public GroupeDePatient removePatient(Patient patient) {
        this.patients.remove(patient);
        patient.getGroupeDePatients().remove(this);
        return this;
    }

    public void setPatients(Set<Patient> patients) {
        this.patients = patients;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public GroupeDePatient medecin(Medecin medecin) {
        this.medecin = medecin;
        return this;
    }

    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof GroupeDePatient)) {
            return false;
        }
        return id != null && id.equals(((GroupeDePatient) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "GroupeDePatient{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            "}";
    }
}
