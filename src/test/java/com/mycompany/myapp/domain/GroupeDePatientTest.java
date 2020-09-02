package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class GroupeDePatientTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(GroupeDePatient.class);
        GroupeDePatient groupeDePatient1 = new GroupeDePatient();
        groupeDePatient1.setId(1L);
        GroupeDePatient groupeDePatient2 = new GroupeDePatient();
        groupeDePatient2.setId(groupeDePatient1.getId());
        assertThat(groupeDePatient1).isEqualTo(groupeDePatient2);
        groupeDePatient2.setId(2L);
        assertThat(groupeDePatient1).isNotEqualTo(groupeDePatient2);
        groupeDePatient1.setId(null);
        assertThat(groupeDePatient1).isNotEqualTo(groupeDePatient2);
    }
}
