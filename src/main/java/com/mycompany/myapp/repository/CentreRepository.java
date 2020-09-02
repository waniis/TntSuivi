package com.mycompany.myapp.repository;

import java.util.List;

import com.mycompany.myapp.domain.Centre;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Centre entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CentreRepository extends JpaRepository<Centre, Long>, JpaSpecificationExecutor<Centre> {

  
}
