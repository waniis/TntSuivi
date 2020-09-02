package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class AdminDeCentreTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdminDeCentre.class);
        AdminDeCentre adminDeCentre1 = new AdminDeCentre();
        adminDeCentre1.setId(1L);
        AdminDeCentre adminDeCentre2 = new AdminDeCentre();
        adminDeCentre2.setId(adminDeCentre1.getId());
        assertThat(adminDeCentre1).isEqualTo(adminDeCentre2);
        adminDeCentre2.setId(2L);
        assertThat(adminDeCentre1).isNotEqualTo(adminDeCentre2);
        adminDeCentre1.setId(null);
        assertThat(adminDeCentre1).isNotEqualTo(adminDeCentre2);
    }
}
