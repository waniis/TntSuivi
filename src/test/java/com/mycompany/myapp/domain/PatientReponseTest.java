package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class PatientReponseTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PatientReponse.class);
        PatientReponse patientReponse1 = new PatientReponse();
        patientReponse1.setId(1L);
        PatientReponse patientReponse2 = new PatientReponse();
        patientReponse2.setId(patientReponse1.getId());
        assertThat(patientReponse1).isEqualTo(patientReponse2);
        patientReponse2.setId(2L);
        assertThat(patientReponse1).isNotEqualTo(patientReponse2);
        patientReponse1.setId(null);
        assertThat(patientReponse1).isNotEqualTo(patientReponse2);
    }
}
