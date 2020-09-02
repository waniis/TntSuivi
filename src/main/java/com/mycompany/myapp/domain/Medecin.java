package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;
import java.util.HashSet;
import java.util.Set;

import com.mycompany.myapp.domain.enumeration.Sexe;

/**
 * A Medecin.
 */
@Entity
@Table(name = "medecin")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Medecin implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @NotNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "phone_2")
    private String phone2;

    @NotNull
    @Column(name = "adress", nullable = false)
    private String adress;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "sexe", nullable = false)
    private Sexe sexe;

    @OneToOne
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(mappedBy = "medecin", fetch = FetchType.EAGER)
    @JsonIgnoreProperties("medecin")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Question> questions = new HashSet<>();

    @OneToMany(mappedBy = "medecin" , fetch = FetchType.EAGER)
    @JsonIgnoreProperties("medecin")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Patient> patients = new HashSet<>();

    @OneToMany(mappedBy = "medecin",fetch = FetchType.EAGER)
    @JsonIgnoreProperties("medecin")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Questionnaire> questionnaires = new HashSet<>();

    @OneToMany(mappedBy = "medecin", fetch = FetchType.EAGER)
    @JsonIgnoreProperties("medecin")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<GroupeDePatient> groupeDePatients = new HashSet<>();

    @ManyToOne(optional = false)
    @JsonIgnoreProperties("medecins")
    private Centre centre;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnoreProperties("medecins")
    private Specialty specialty;

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

    public Medecin fullName(String fullName) {
        this.fullName = fullName;
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public Medecin phone(String phone) {
        this.phone = phone;
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone2() {
        return phone2;
    }

    public Medecin phone2(String phone2) {
        this.phone2 = phone2;
        return this;
    }

    public void setPhone2(String phone2) {
        this.phone2 = phone2;
    }

    public String getAdress() {
        return adress;
    }

    public Medecin adress(String adress) {
        this.adress = adress;
        return this;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public Sexe getSexe() {
        return sexe;
    }

    public Medecin sexe(Sexe sexe) {
        this.sexe = sexe;
        return this;
    }

    public void setSexe(Sexe sexe) {
        this.sexe = sexe;
    }

    public User getUser() {
        return user;
    }

    public Medecin user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Question> getQuestions() {
        return questions;
    }

    public Medecin questions(Set<Question> questions) {
        this.questions = questions;
        return this;
    }

    public Medecin addQuestion(Question question) {
        this.questions.add(question);
        question.setMedecin(this);
        return this;
    }

    public Medecin removeQuestion(Question question) {
        this.questions.remove(question);
        question.setMedecin(null);
        return this;
    }

    public void setQuestions(Set<Question> questions) {
        this.questions = questions;
    }

    public Set<Patient> getPatients() {
        return patients;
    }

    public Medecin patients(Set<Patient> patients) {
        this.patients = patients;
        return this;
    }

    public Medecin addPatient(Patient patient) {
        this.patients.add(patient);
        patient.setMedecin(this);
        return this;
    }

    public Medecin removePatient(Patient patient) {
        this.patients.remove(patient);
        patient.setMedecin(null);
        return this;
    }

    public void setPatients(Set<Patient> patients) {
        this.patients = patients;
    }

    public Set<Questionnaire> getQuestionnaires() {
        return questionnaires;
    }

    public Medecin questionnaires(Set<Questionnaire> questionnaires) {
        this.questionnaires = questionnaires;
        return this;
    }

    public Medecin addQuestionnaire(Questionnaire questionnaire) {
        this.questionnaires.add(questionnaire);
        questionnaire.setMedecin(this);
        return this;
    }

    public Medecin removeQuestionnaire(Questionnaire questionnaire) {
        this.questionnaires.remove(questionnaire);
        questionnaire.setMedecin(null);
        return this;
    }

    public void setQuestionnaires(Set<Questionnaire> questionnaires) {
        this.questionnaires = questionnaires;
    }

    public Set<GroupeDePatient> getGroupeDePatients() {
        return groupeDePatients;
    }

    public Medecin groupeDePatients(Set<GroupeDePatient> groupeDePatients) {
        this.groupeDePatients = groupeDePatients;
        return this;
    }

    public Medecin addGroupeDePatient(GroupeDePatient groupeDePatient) {
        this.groupeDePatients.add(groupeDePatient);
        groupeDePatient.setMedecin(this);
        return this;
    }

    public Medecin removeGroupeDePatient(GroupeDePatient groupeDePatient) {
        this.groupeDePatients.remove(groupeDePatient);
        groupeDePatient.setMedecin(null);
        return this;
    }

    public void setGroupeDePatients(Set<GroupeDePatient> groupeDePatients) {
        this.groupeDePatients = groupeDePatients;
    }

    public Centre getCentre() {
        return centre;
    }

    public Medecin centre(Centre centre) {
        this.centre = centre;
        return this;
    }

    public void setCentre(Centre centre) {
        this.centre = centre;
    }

    public Specialty getSpecialty() {
        return specialty;
    }

    public Medecin specialty(Specialty specialty) {
        this.specialty = specialty;
        return this;
    }

    public void setSpecialty(Specialty specialty) {
        this.specialty = specialty;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Medecin)) {
            return false;
        }
        return id != null && id.equals(((Medecin) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Medecin{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", phone='" + getPhone() + "'" +
            ", phone2='" + getPhone2() + "'" +
            ", adress='" + getAdress() + "'" +
            ", sexe='" + getSexe() + "'" +
            "}";
    }
}
