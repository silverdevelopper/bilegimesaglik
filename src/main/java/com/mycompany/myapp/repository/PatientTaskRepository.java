package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.PatientTask;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the PatientTask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PatientTaskRepository extends JpaRepository<PatientTask, Long> {}
