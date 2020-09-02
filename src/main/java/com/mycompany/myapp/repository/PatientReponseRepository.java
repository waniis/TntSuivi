package com.mycompany.myapp.repository;

import java.util.List;

import com.mycompany.myapp.domain.Patient;
import com.mycompany.myapp.domain.PatientReponse;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PatientReponse entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PatientReponseRepository extends JpaRepository<PatientReponse, Long>, JpaSpecificationExecutor<PatientReponse> {
    @Query("SELECT  patientReponse from PatientReponse patientReponse  where patientReponse.patientQuestionnaire.id =:id  ")
	List<PatientReponse> FindByIdPatientQuestionnaire (@Param("id") Long id);


    @Query("SELECT patient FROM Patient patient  WHERE patient.user.login = ?#{principal.username}")
    Patient findOneByUserIsCurrentUser(); // return l'admin de centre de la session courante d'un user 

   
 
}
