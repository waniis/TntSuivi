package com.mycompany.myapp.domain;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.mycompany.myapp.domain.enumeration.TypeQuestion;

import java.util.List;

public class QuestionRequest {
    @NotBlank
    @Size(max = 140)
    private String label;

    @NotNull
    @Size(min = 0, max = 5)
    @Valid
    private List<String> questionAnswers;

    private TypeQuestion typeQuestion;

  public TypeQuestion getTypeQuestion() {
    return typeQuestion;
}

public void setTypeQuestion(TypeQuestion typeQuestion) {
    this.typeQuestion = typeQuestion;
}

private String choice;

public String getChoice() {
  return choice;
}

public void setChoice(String choice) {
  this.choice = choice;
}

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public List<String> getQuestionAnswers() {
        return questionAnswers;
    }

    public void setQuestionAnswers(List<String> questionAnswers) {
        this.questionAnswers = questionAnswers;
    }

 
}