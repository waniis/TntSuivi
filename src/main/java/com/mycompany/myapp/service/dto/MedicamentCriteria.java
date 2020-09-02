package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;
import io.github.jhipster.service.Criteria;
import com.mycompany.myapp.domain.enumeration.Forme;
import io.github.jhipster.service.filter.BooleanFilter;
import io.github.jhipster.service.filter.DoubleFilter;
import io.github.jhipster.service.filter.Filter;
import io.github.jhipster.service.filter.FloatFilter;
import io.github.jhipster.service.filter.IntegerFilter;
import io.github.jhipster.service.filter.LongFilter;
import io.github.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.mycompany.myapp.domain.Medicament} entity. This class is used
 * in {@link com.mycompany.myapp.web.rest.MedicamentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /medicaments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
public class MedicamentCriteria implements Serializable, Criteria {
    /**
     * Class for filtering Forme
     */
    public static class FormeFilter extends Filter<Forme> {

        public FormeFilter() {
        }

        public FormeFilter(FormeFilter filter) {
            super(filter);
        }

        @Override
        public FormeFilter copy() {
            return new FormeFilter(this);
        }

    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private FormeFilter forme;

    private StringFilter descrpition;

    public MedicamentCriteria() {
    }

    public MedicamentCriteria(MedicamentCriteria other) {
        this.id = other.id == null ? null : other.id.copy();
        this.name = other.name == null ? null : other.name.copy();
        this.forme = other.forme == null ? null : other.forme.copy();
        this.descrpition = other.descrpition == null ? null : other.descrpition.copy();
    }

    @Override
    public MedicamentCriteria copy() {
        return new MedicamentCriteria(this);
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

    public FormeFilter getForme() {
        return forme;
    }

    public void setForme(FormeFilter forme) {
        this.forme = forme;
    }

    public StringFilter getDescrpition() {
        return descrpition;
    }

    public void setDescrpition(StringFilter descrpition) {
        this.descrpition = descrpition;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final MedicamentCriteria that = (MedicamentCriteria) o;
        return
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(forme, that.forme) &&
            Objects.equals(descrpition, that.descrpition);
    }

    @Override
    public int hashCode() {
        return Objects.hash(
        id,
        name,
        forme,
        descrpition
        );
    }

    @Override
    public String toString() {
        return "MedicamentCriteria{" +
                (id != null ? "id=" + id + ", " : "") +
                (name != null ? "name=" + name + ", " : "") +
                (forme != null ? "forme=" + forme + ", " : "") +
                (descrpition != null ? "descrpition=" + descrpition + ", " : "") +
            "}";
    }

}
