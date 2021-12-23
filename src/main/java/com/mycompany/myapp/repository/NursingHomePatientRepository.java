package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.NursingHomePatient;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the NursingHomePatient entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NursingHomePatientRepository extends JpaRepository<NursingHomePatient, Long> {}
