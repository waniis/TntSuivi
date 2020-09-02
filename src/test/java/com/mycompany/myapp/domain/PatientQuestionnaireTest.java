package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class PatientQuestionnaireTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PatientQuestionnaire.class);
        PatientQuestionnaire patientQuestionnaire1 = new PatientQuestionnaire();
        patientQuestionnaire1.setId(1L);
        PatientQuestionnaire patientQuestionnaire2 = new PatientQuestionnaire();
        patientQuestionnaire2.setId(patientQuestionnaire1.getId());
        assertThat(patientQuestionnaire1).isEqualTo(patientQuestionnaire2);
        patientQuestionnaire2.setId(2L);
        assertThat(patientQuestionnaire1).isNotEqualTo(patientQuestionnaire2);
        patientQuestionnaire1.setId(null);
        assertThat(patientQuestionnaire1).isNotEqualTo(patientQuestionnaire2);
    }
}
