package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.NursingHome;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data SQL repository for the NursingHome entity.
 */
@SuppressWarnings("unused")
@Repository
public interface NursingHomeRepository extends JpaRepository<NursingHome, Long> {}
