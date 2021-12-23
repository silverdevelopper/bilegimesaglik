package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PeoplePatientResponsibleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PeoplePatientResponsible.class);
        PeoplePatientResponsible peoplePatientResponsible1 = new PeoplePatientResponsible();
        peoplePatientResponsible1.setId(1L);
        PeoplePatientResponsible peoplePatientResponsible2 = new PeoplePatientResponsible();
        peoplePatientResponsible2.setId(peoplePatientResponsible1.getId());
        assertThat(peoplePatientResponsible1).isEqualTo(peoplePatientResponsible2);
        peoplePatientResponsible2.setId(2L);
        assertThat(peoplePatientResponsible1).isNotEqualTo(peoplePatientResponsible2);
        peoplePatientResponsible1.setId(null);
        assertThat(peoplePatientResponsible1).isNotEqualTo(peoplePatientResponsible2);
    }
}
