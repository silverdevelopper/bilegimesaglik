package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PeoplePatientResponsible;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PeoplePatientResponsible entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PeoplePatientResponsibleRepository extends JpaRepository<PeoplePatientResponsible, Long> {}
