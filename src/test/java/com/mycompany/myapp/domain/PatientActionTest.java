package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PatientActionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PatientAction.class);
        PatientAction patientAction1 = new PatientAction();
        patientAction1.setId(1L);
        PatientAction patientAction2 = new PatientAction();
        patientAction2.setId(patientAction1.getId());
        assertThat(patientAction1).isEqualTo(patientAction2);
        patientAction2.setId(2L);
        assertThat(patientAction1).isNotEqualTo(patientAction2);
        patientAction1.setId(null);
        assertThat(patientAction1).isNotEqualTo(patientAction2);
    }
}
