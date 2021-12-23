package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PatientAction;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PatientAction entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PatientActionRepository extends JpaRepository<PatientAction, Long> {}
