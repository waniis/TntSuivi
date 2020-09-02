package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class MedicamentTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Medicament.class);
        Medicament medicament1 = new Medicament();
        medicament1.setId(1L);
        Medicament medicament2 = new Medicament();
        medicament2.setId(medicament1.getId());
        assertThat(medicament1).isEqualTo(medicament2);
        medicament2.setId(2L);
        assertThat(medicament1).isNotEqualTo(medicament2);
        medicament1.setId(null);
        assertThat(medicament1).isNotEqualTo(medicament2);
    }
}
