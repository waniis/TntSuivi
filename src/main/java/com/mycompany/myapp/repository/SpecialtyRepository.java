package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Specialty;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Specialty entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SpecialtyRepository extends JpaRepository<Specialty, Long> {
    boolean existsByName( String name); // return true if the spacialty exist in DB
}
