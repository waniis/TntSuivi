package com.mycompany.myapp.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.Objects;

import com.mycompany.myapp.domain.enumeration.Forme;

/**
 * A Medicament.
 */
@Entity
@Table(name = "medicament")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Medicament implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "forme", nullable = false)
    private Forme forme;

    @Column(name = "descrpition")
    private String descrpition;

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

    public Medicament name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Forme getForme() {
        return forme;
    }

    public Medicament forme(Forme forme) {
        this.forme = forme;
        return this;
    }

    public void setForme(Forme forme) {
        this.forme = forme;
    }

    public String getDescrpition() {
        return descrpition;
    }

    public Medicament descrpition(String descrpition) {
        this.descrpition = descrpition;
        return this;
    }

    public void setDescrpition(String descrpition) {
        this.descrpition = descrpition;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Medicament)) {
            return false;
        }
        return id != null && id.equals(((Medicament) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Medicament{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", forme='" + getForme() + "'" +
            ", descrpition='" + getDescrpition() + "'" +
            "}";
    }
}
