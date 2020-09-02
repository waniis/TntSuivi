package com.mycompany.myapp.domain;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import com.mycompany.myapp.web.rest.TestUtil;

public class QuestionAnswerTest {

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(QuestionAnswer.class);
        QuestionAnswer questionAnswer1 = new QuestionAnswer();
        questionAnswer1.setId(1L);
        QuestionAnswer questionAnswer2 = new QuestionAnswer();
        questionAnswer2.setId(questionAnswer1.getId());
        assertThat(questionAnswer1).isEqualTo(questionAnswer2);
        questionAnswer2.setId(2L);
        assertThat(questionAnswer1).isNotEqualTo(questionAnswer2);
        questionAnswer1.setId(null);
        assertThat(questionAnswer1).isNotEqualTo(questionAnswer2);
    }
}
