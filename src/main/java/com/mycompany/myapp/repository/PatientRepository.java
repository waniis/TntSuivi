package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Patient;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Patient entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long>, JpaSpecificationExecutor<Patient> {

    @Query("SELECT p FROM Patient p  WHERE p.user.login = ?#{principal.username}")
    Patient findOneByUserIsCurrentUser(); // return le  medcin de la session courante d'un user 
}
