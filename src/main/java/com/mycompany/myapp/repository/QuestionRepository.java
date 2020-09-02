package com.mycompany.myapp.repository;

import java.util.List;
import java.util.Optional;

import com.mycompany.myapp.domain.Question;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Question entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>, JpaSpecificationExecutor<Question> {
   
    @Query("select question from Question question where question.medecin.user.login = ?#{principal.username}")
    List<Question> findByUserIsCurrentUser();

    @Query("SELECT COUNT (question) from Question question where question.medecin.user.login = ?#{principal.username}")
    Long CountByUserIsCurrentUser();
   
    @Query("select question from Question question where question.medecin.user.login = ?#{principal.username} AND question.id = :id")
    Optional<Question> findByIdAndUserIsCurrentUser(@Param("id") long id );

}
