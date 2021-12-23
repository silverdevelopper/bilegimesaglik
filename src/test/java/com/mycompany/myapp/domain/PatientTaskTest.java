package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PatientTaskTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PatientTask.class);
        PatientTask patientTask1 = new PatientTask();
        patientTask1.setId(1L);
        PatientTask patientTask2 = new PatientTask();
        patientTask2.setId(patientTask1.getId());
        assertThat(patientTask1).isEqualTo(patientTask2);
        patientTask2.setId(2L);
        assertThat(patientTask1).isNotEqualTo(patientTask2);
        patientTask1.setId(null);
        assertThat(patientTask1).isNotEqualTo(patientTask2);
    }
}
