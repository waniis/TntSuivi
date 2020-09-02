package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import com.mycompany.myapp.domain.enumeration.Sexe;

import com.mycompany.myapp.domain.enumeration.Alcool;

import com.mycompany.myapp.domain.enumeration.Tobacco;

/**
 * A Patient.
 */
@Entity
@Table(name = "patient")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @NotNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @NotNull
    @Column(name = "adress", nullable = false)
    private String adress;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sexe", nullable = false)
    private Sexe sexe;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "alcool", nullable = false)
    private Alcool alcool;

    @Column(name = "start_date_alcool")
    private LocalDate startDateAlcool;

    @Column(name = "end_date_alcool")
    private LocalDate endDateAlcool;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "tobacoo", nullable = false)
    private Tobacco tobacoo;

    @Column(name = "start_date_tobacco")
    private LocalDate startDateTobacco;

    @Column(name = "end_date_tobacco")
    private LocalDate endDateTobacco;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "patient")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<PatientQuestionnaire> patientQuestionnaires = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties("patients")
    private Medecin medecin;

    @ManyToOne(optional = false)
    @JsonIgnoreProperties("patients")
    private Centre centre;

    @ManyToMany(mappedBy = "patients")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    @JsonIgnore
    private Set<GroupeDePatient> groupeDePatients = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public Patient fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public Patient phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAdress() {
        return adress;
    }

    public Patient adress(String adress) {
        this.adress = adress;
        return this;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public Patient sexe(Sexe sexe) {
        this.sexe = sexe;
        return this;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public Alcool getAlcool() {
        return alcool;
    }

    public Patient alcool(Alcool alcool) {
        this.alcool = alcool;
        return this;
    }

    public void setAlcool(Alcool alcool) {
        this.alcool = alcool;
    }

    public LocalDate getStartDateAlcool() {
        return startDateAlcool;
    }

    public Patient startDateAlcool(LocalDate startDateAlcool) {
        this.startDateAlcool = startDateAlcool;
        return this;
    }

    public void setStartDateAlcool(LocalDate startDateAlcool) {
        this.startDateAlcool = startDateAlcool;
    }

    public LocalDate getEndDateAlcool() {
        return endDateAlcool;
    }

    public Patient endDateAlcool(LocalDate endDateAlcool) {
        this.endDateAlcool = endDateAlcool;
        return this;
    }

    public void setEndDateAlcool(LocalDate endDateAlcool) {
        this.endDateAlcool = endDateAlcool;
    }

    public Tobacco getTobacoo() {
        return tobacoo;
    }

    public Patient tobacoo(Tobacco tobacoo) {
        this.tobacoo = tobacoo;
        return this;
    }

    public void setTobacoo(Tobacco tobacoo) {
        this.tobacoo = tobacoo;
    }

    public LocalDate getStartDateTobacco() {
        return startDateTobacco;
    }

    public Patient startDateTobacco(LocalDate startDateTobacco) {
        this.startDateTobacco = startDateTobacco;
        return this;
    }

    public void setStartDateTobacco(LocalDate startDateTobacco) {
        this.startDateTobacco = startDateTobacco;
    }

    public LocalDate getEndDateTobacco() {
        return endDateTobacco;
    }

    public Patient endDateTobacco(LocalDate endDateTobacco) {
        this.endDateTobacco = endDateTobacco;
        return this;
    }

    public void setEndDateTobacco(LocalDate endDateTobacco) {
        this.endDateTobacco = endDateTobacco;
    }

    public User getUser() {
        return user;
    }

    public Patient user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<PatientQuestionnaire> getPatientQuestionnaires() {
        return patientQuestionnaires;
    }

    public Patient patientQuestionnaires(Set<PatientQuestionnaire> patientQuestionnaires) {
        this.patientQuestionnaires = patientQuestionnaires;
        return this;
    }

    public Patient addPatientQuestionnaire(PatientQuestionnaire patientQuestionnaire) {
        this.patientQuestionnaires.add(patientQuestionnaire);
        patientQuestionnaire.setPatient(this);
        return this;
    }

    public Patient removePatientQuestionnaire(PatientQuestionnaire patientQuestionnaire) {
        this.patientQuestionnaires.remove(patientQuestionnaire);
        patientQuestionnaire.setPatient(null);
        return this;
    }

    public void setPatientQuestionnaires(Set<PatientQuestionnaire> patientQuestionnaires) {
        this.patientQuestionnaires = patientQuestionnaires;
    }

    public Medecin getMedecin() {
        return medecin;
    }

    public Patient medecin(Medecin medecin) {
        this.medecin = medecin;
        return this;
    }

    public void setMedecin(Medecin medecin) {
        this.medecin = medecin;
    }

    public Centre getCentre() {
        return centre;
    }

    public Patient centre(Centre centre) {
        this.centre = centre;
        return this;
    }

    public void setCentre(Centre centre) {
        this.centre = centre;
    }

    public Set<GroupeDePatient> getGroupeDePatients() {
        return groupeDePatients;
    }

    public Patient groupeDePatients(Set<GroupeDePatient> groupeDePatients) {
        this.groupeDePatients = groupeDePatients;
        return this;
    }

    public Patient addGroupeDePatient(GroupeDePatient groupeDePatient) {
        this.groupeDePatients.add(groupeDePatient);
        groupeDePatient.getPatients().add(this);
        return this;
    }

    public Patient removeGroupeDePatient(GroupeDePatient groupeDePatient) {
        this.groupeDePatients.remove(groupeDePatient);
        groupeDePatient.getPatients().remove(this);
        return this;
    }

    public void setGroupeDePatients(Set<GroupeDePatient> groupeDePatients) {
        this.groupeDePatients = groupeDePatients;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Patient)) {
            return false;
        }
        return id != null && id.equals(((Patient) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Patient{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", adress='" + getAdress() + "'" +
            ", sexe='" + getSexe() + "'" +
            ", alcool='" + getAlcool() + "'" +
            ", startDateAlcool='" + getStartDateAlcool() + "'" +
            ", endDateAlcool='" + getEndDateAlcool() + "'" +
            ", tobacoo='" + getTobacoo() + "'" +
            ", startDateTobacco='" + getStartDateTobacco() + "'" +
            ", endDateTobacco='" + getEndDateTobacco() + "'" +
            "}";
    }
}
