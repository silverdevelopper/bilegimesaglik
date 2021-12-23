package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class NursingHomeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(NursingHome.class);
        NursingHome nursingHome1 = new NursingHome();
        nursingHome1.setId(1L);
        NursingHome nursingHome2 = new NursingHome();
        nursingHome2.setId(nursingHome1.getId());
        assertThat(nursingHome1).isEqualTo(nursingHome2);
        nursingHome2.setId(2L);
        assertThat(nursingHome1).isNotEqualTo(nursingHome2);
        nursingHome1.setId(null);
        assertThat(nursingHome1).isNotEqualTo(nursingHome2);
    }
}
