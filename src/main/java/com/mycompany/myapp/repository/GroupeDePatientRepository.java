package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.GroupeDePatient;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Spring Data  repository for the GroupeDePatient entity.
 */
@Repository
public interface GroupeDePatientRepository extends JpaRepository<GroupeDePatient, Long> {

    @Query(value = "select distinct groupeDePatient from GroupeDePatient groupeDePatient left join fetch groupeDePatient.patients",
        countQuery = "select count(distinct groupeDePatient) from GroupeDePatient groupeDePatient")
    Page<GroupeDePatient> findAllWithEagerRelationships(Pageable pageable);

    @Query("select distinct groupeDePatient from GroupeDePatient groupeDePatient left join fetch groupeDePatient.patients")
    List<GroupeDePatient> findAllWithEagerRelationships();

    @Query("select groupeDePatient from GroupeDePatient groupeDePatient left join fetch groupeDePatient.patients where groupeDePatient.id =:id")
    Optional<GroupeDePatient> findOneWithEagerRelationships(@Param("id") Long id);

	GroupeDePatient findOneById(Long gpId);

 
}
