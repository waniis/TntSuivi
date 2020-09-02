package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Questionnaire;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the Questionnaire entity.
 */
@Repository
public interface QuestionnaireRepository extends JpaRepository<Questionnaire, Long>, JpaSpecificationExecutor<Questionnaire> {

    @Query(value = "select distinct questionnaire from Questionnaire questionnaire left join fetch questionnaire.questions",
    countQuery = "select count(distinct questionnaire) from Questionnaire questionnaire")
Page<Questionnaire> findAllWithEagerRelationships(Pageable pageable);

@Query("select distinct questionnaire from Questionnaire questionnaire left join fetch questionnaire.questions")
List<Questionnaire> findAllWithEagerRelationships();

@Query("select questionnaire from Questionnaire questionnaire left join fetch questionnaire.questions where questionnaire.id =:id")
Optional<Questionnaire> findOneWithEagerRelationships(@Param("id") Long id);

@Query("select  distinct questionnaire from Questionnaire questionnaire left join fetch questionnaire.questions where questionnaire.medecin.user.login = ?#{principal.username}")
List<Questionnaire> findByUserIsCurrentUser();

@Query("SELECT COUNT (questionnaire) from Questionnaire questionnaire  where questionnaire.medecin.user.login = ?#{principal.username}") 
Long CountByUserIsCurrentUser();

@Query("select questionnaire from Questionnaire questionnaire left join fetch questionnaire.questions left join fetch questionnaire.patientQuestionnaires pq left join fetch  pq.patientReponses ps left join fetch ps.questionAnswer where questionnaire.medecin.user.login = ?#{principal.username} AND questionnaire.id =:id")
Optional<Questionnaire> findByIdAndUserIsCurrentUser(@Param("id") Long id);


@Query("SELECT COUNT (questionnaire) from Questionnaire questionnaire  join questionnaire.patientQuestionnaires  pq where pq.patient.user.login=  ?#{principal.username}   AND questionnaire.id =:id  ")
Long countp(@Param("id") Long id);

@Query("SELECT distinct questionnaire from Questionnaire questionnaire  left join fetch questionnaire.questions left join fetch questionnaire.patientQuestionnaires   pq where pq.patient.user.login=  ?#{principal.username}   AND questionnaire.id =:id  ")
 Questionnaire findoneBypatientAndid(@Param("id") Long id);


 @Query("SELECT  distinct questionnaire from Questionnaire questionnaire left join fetch questionnaire.patientQuestionnaires pq WHERE pq.patient.user.login=  ?#{principal.username} ")
 List<Questionnaire> findAllByPatient();
 
 Questionnaire findOneById (Long id);
}
