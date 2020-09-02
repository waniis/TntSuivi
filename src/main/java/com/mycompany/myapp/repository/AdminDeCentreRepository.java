package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.AdminDeCentre;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the AdminDeCentre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface AdminDeCentreRepository extends JpaRepository<AdminDeCentre, Long> {

    @Query("SELECT adc FROM AdminDeCentre adc  WHERE adc.user.login = ?#{principal.username}")
    AdminDeCentre findOneByUserIsCurrentUser(); // return l'admin de centre de la session courante d'un user 



}
