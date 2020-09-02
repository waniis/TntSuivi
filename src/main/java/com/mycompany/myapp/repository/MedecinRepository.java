package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.Medecin;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Medecin entity.
 */
@SuppressWarnings("unused")
@Repository
public interface MedecinRepository extends JpaRepository<Medecin, Long>, JpaSpecificationExecutor<Medecin> {
    @Query("SELECT m FROM Medecin m  WHERE m.user.login = ?#{principal.username}")
    Medecin findOneByUserIsCurrentUser(); // return le  medcin de la session courante d'un user 
}
