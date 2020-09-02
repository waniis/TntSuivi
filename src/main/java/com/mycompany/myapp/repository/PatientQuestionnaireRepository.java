package com.mycompany.myapp.repository;

import java.util.Optional;

import com.mycompany.myapp.domain.PatientQuestionnaire;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the PatientQuestionnaire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PatientQuestionnaireRepository extends JpaRepository<PatientQuestionnaire, Long>, JpaSpecificationExecutor<PatientQuestionnaire> {

    @Query("SELECT distinct patientQuestionnaire from PatientQuestionnaire patientQuestionnaire  left join fetch patientQuestionnaire.questionnaire q left join fetch  q.questions ques left join fetch ques.questionAnswers where patientQuestionnaire.id =:id  ")
	Optional<PatientQuestionnaire> findByIdWithQuestions (@Param("id") Long id);

    PatientQuestionnaire findOneById(Long id);
    


    @Query("SELECT  patientQuestionnaire from PatientQuestionnaire patientQuestionnaire    left join fetch PatientReponse  where patientQuestionnaire.id =:id  ")
    Optional<PatientQuestionnaire> findByIdWithQuestionsAndReponse (@Param("id") Long id);
    
    @Query("SELECT distinct patientQuestionnaire from PatientQuestionnaire patientQuestionnaire  left join fetch patientQuestionnaire.questionnaire q left join fetch  q.questions ques left join fetch ques.questionAnswers where patientQuestionnaire.id =:id   AND patientQuestionnaire.patient.user.login = ?#{principal.username} ")
    PatientQuestionnaire findOneByIdAndIdPatient(@Param("id")Long id);
    
    @Query("SELECT COUNT (patientQuestionnaire) from PatientQuestionnaire patientQuestionnaire Where patientQuestionnaire.patient.user.login=  ?#{principal.username}   AND patientQuestionnaire.id =:id  ")
	Long countPatient(@Param("id")Long id);
}
