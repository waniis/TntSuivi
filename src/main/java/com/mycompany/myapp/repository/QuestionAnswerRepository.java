package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.QuestionAnswer;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the QuestionAnswer entity.
 */
@SuppressWarnings("unused")
@Repository
public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer, Long>, JpaSpecificationExecutor<QuestionAnswer> {

    QuestionAnswer findOneById(Long id);
}
