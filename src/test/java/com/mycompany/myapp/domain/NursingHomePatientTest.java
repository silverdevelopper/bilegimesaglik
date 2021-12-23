package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NursingHomePatientTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NursingHomePatient.class);
        NursingHomePatient nursingHomePatient1 = new NursingHomePatient();
        nursingHomePatient1.setId(1L);
        NursingHomePatient nursingHomePatient2 = new NursingHomePatient();
        nursingHomePatient2.setId(nursingHomePatient1.getId());
        assertThat(nursingHomePatient1).isEqualTo(nursingHomePatient2);
        nursingHomePatient2.setId(2L);
        assertThat(nursingHomePatient1).isNotEqualTo(nursingHomePatient2);
        nursingHomePatient1.setId(null);
        assertThat(nursingHomePatient1).isNotEqualTo(nursingHomePatient2);
    }
}
